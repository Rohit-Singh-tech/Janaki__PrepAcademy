package com.example.janakiprepacademy.data

import android.content.Context
import android.net.Uri
import com.example.janakiprepacademy.data.model.Question
import com.example.janakiprepacademy.data.model.QuestionOption
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream
import java.util.UUID

/**
 * High-performance, 100% Free / Open Source (Apache 2.0)
 * PDF & Document Question Extractor for Janaki PrepAcademy.
 *
 * Extracts text from digital PDFs and plain text question papers,
 * and parses them into structured Question & QuestionOption models
 * with bilingual English + Hindi and 4 or 5 option support.
 *
 * Fully supports official state board formats including:
 * - Bihar STET / BSEB / TCS official CBT response sheets (with Question Ids, Option Ids, and Right Option Ids)
 * - BPSC TRE / BPSC CCE question papers
 * - General coaching and standard question formats (Q1., Question 1:, 1., प्रश्न 1:, etc.)
 */
object PdfQuestionExtractor {

    private var isPdfBoxInitialized = false

    private fun ensurePdfBoxInit(context: Context) {
        if (!isPdfBoxInitialized) {
            try {
                PDFBoxResourceLoader.init(context.applicationContext)
                isPdfBoxInitialized = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Extracts plain text from a given document Uri (PDF or TXT).
     */
    fun extractTextFromUri(context: Context, uri: Uri): Result<String> {
        return try {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: ""
            val name = uri.lastPathSegment?.lowercase() ?: ""

            val isLikelyText = mimeType.startsWith("text/") || name.endsWith(".txt")

            if (isLikelyText) {
                val text = contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
                if (text.isNotBlank()) {
                    Result.success(text)
                } else {
                    Result.failure(Exception("The selected text file is empty."))
                }
            } else {
                // PDF extraction
                ensurePdfBoxInit(context)
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    return Result.failure(Exception("Cannot open file stream for selected document."))
                }

                inputStream.use { stream ->
                    val document = PDDocument.load(stream)
                    val stripper = PDFTextStripper()
                    stripper.sortByPosition = true
                    val extractedText = stripper.getText(document)
                    document.close()

                    if (extractedText.isNotBlank()) {
                        Result.success(extractedText)
                    } else {
                        Result.failure(
                            Exception(
                                "No digital text found in this PDF.\n" +
                                "It appears to be a scanned image or photo PDF.\n" +
                                "Please copy-paste the text using 'Paste Paper Text' or use a digital text file."
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to read document: ${e.localizedMessage ?: "Unknown error"}", e))
        }
    }

    /**
     * Normalizes Unicode whitespaces (non-breaking spaces, zero-width chars)
     * and quotation/dash variations to standard ASCII equivalents.
     */
    fun normalizeText(text: String): String {
        return text
            .replace("\r\n", "\n")
            .replace("\r", "\n")
            .replace("\u00A0", " ") // Non-breaking space
            .replace("\u200B", "")  // Zero-width space
            .replace("\uFEFF", "")  // BOM
            .replace("\u2013", "-") // En-dash
            .replace("\u2014", "-") // Em-dash
            .replace("\u2212", "-") // Minus sign
            .replace("\u2018", "'") // Left single quote
            .replace("\u2019", "'") // Right single quote
            .replace("\u201C", "\"") // Left double quote
            .replace("\u201D", "\"") // Right double quote
    }

    /**
     * Parses questions, options, answer keys, and explanations from extracted document text.
     * Automatically detects Bihar STET / BSEB / TCS response sheet format,
     * with fallback to standard line-by-line question format.
     */
    fun parseQuestionsFromDocumentText(text: String, defaultSection: String = "Uploaded Section"): List<Question> {
        val normalized = normalizeText(text)

        // Check if this document uses BSEB / TCS CBT question paper format
        val isBsebFormat = normalized.contains("Question Id :", ignoreCase = true) ||
                (normalized.contains("Right Option Id", ignoreCase = true) && normalized.contains("Answer : Option Id", ignoreCase = true))

        return if (isBsebFormat) {
            parseBsebStetQuestions(normalized, defaultSection)
        } else {
            parseGeneralQuestions(normalized, defaultSection)
        }
    }

    /**
     * Specialized parser for Bihar STET / BSEB / TCS official response sheets.
     * Extracts all questions (e.g. 150 questions across Domain Subject, Art of Teaching, Other Skills),
     * cleans option IDs, removes page headers, and accurately maps correct answer keys via Right Option Id.
     */
    private fun parseBsebStetQuestions(text: String, defaultSection: String): List<Question> {
        val result = mutableListOf<Question>()

        // Split document by "Question <N> Question Id : <ID>"
        val splitRegex = Regex("Question\\s+(\\d+)\\s+Question\\s+Id\\s*:\\s*(\\d+)", RegexOption.IGNORE_CASE)
        val splits = splitRegex.split(text)
        val matches = splitRegex.findAll(text).toList()

        if (matches.isEmpty()) {
            return parseGeneralQuestions(text, defaultSection)
        }

        // Determine initial subject from preamble
        var currentSection = defaultSection
        val preamble = splits.firstOrNull() ?: ""
        val initialSubMatch = Regex("Subject\\s+Name\\s*:\\s*([^:\\n]+?)(?:Subject\\s+Code|$)", RegexOption.IGNORE_CASE).find(preamble)
        if (initialSubMatch != null) {
            val name = initialSubMatch.groupValues[1].replace(Regex("Subject\\s+Code.*", RegexOption.IGNORE_CASE), "").trim()
            if (name.isNotBlank()) currentSection = name
        } else if (preamble.contains("Computer Science", ignoreCase = true)) {
            currentSection = "Computer Science"
        }

        val optPattern = Regex("\\(([A-Ea-e])\\)\\s+(.*?)(?=\\([A-Ea-e]\\)|Right\\s+Answer|$)", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        val rightOptPattern = Regex("Right\\s+Option\\s+Id\\s*:\\s*(\\d+)", RegexOption.IGNORE_CASE)
        val rightAnsPattern = Regex("Right\\s+Answer\\s*:\\s*(.*?)(?:Right\\s+Option\\s+Id|$)", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        val qTextPattern = Regex("^(.*?)(?:Answer\\s*:\\s*Option\\s+Id)", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))

        for (i in matches.indices) {
            val match = matches[i]
            val qNum = match.groupValues[1].toIntOrNull() ?: (i + 1)
            val qId = match.groupValues[2]
            val qBody = if (i + 1 < splits.size) splits[i + 1] else ""

            // Check if section changes after or inside this question (e.g. Art of Teaching, Other Skills)
            var nextSection: String? = null
            when {
                qBody.contains("Art Of Teaching", ignoreCase = true) -> nextSection = "Art Of Teaching"
                qBody.contains("Other Skills", ignoreCase = true) -> nextSection = "Other Skills"
                else -> {
                    val subM = Regex("Subject\\s+Name\\s*:\\s*([^:\\n]+?)(?:Subject\\s+Code|$)", RegexOption.IGNORE_CASE).find(qBody)
                    if (subM != null) {
                        val sName = subM.groupValues[1].replace(Regex("Subject\\s+Code.*", RegexOption.IGNORE_CASE), "").trim()
                        if (sName.isNotBlank()) nextSection = sName
                    }
                }
            }

            // Extract Question Text
            val textMatch = qTextPattern.find(qBody)
            var rawQText = if (textMatch != null) {
                textMatch.groupValues[1]
            } else {
                qBody.substringBefore("(A)")
            }

            // Clean headers, exam info, and page breaks from question text
            rawQText = rawQText
                .replace(Regex("QUESTION\\s+PAPER.*?(?:Subject\\s+Question)?", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)), "")
                .replace(Regex("Subject\\s+Name\\s*:[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("--- PAGE \\d+ ---", RegexOption.IGNORE_CASE), "")
                .replace(Regex("\\s+"), " ")
                .trim()

            // Extract Options
            val optMatches = optPattern.findAll(qBody).toList()
            val parsedOptions = mutableListOf<QuestionOption>()
            val optIdMap = mutableMapOf<String, String>() // e.g. "1001" -> "A"

            for (om in optMatches) {
                val optLetter = om.groupValues[1].uppercase()
                var rawOptText = om.groupValues[2].trim()

                // Remove trailing option ID number (e.g. "1001", "55001", "134001")
                val trailingIdMatch = Regex("\\b(\\d{3,8})\\s*$").find(rawOptText)
                if (trailingIdMatch != null) {
                    val idNumber = trailingIdMatch.groupValues[1]
                    optIdMap[idNumber] = optLetter
                    rawOptText = rawOptText.substring(0, trailingIdMatch.range.first).trim()
                }

                // Clean unwanted artifacts from option text
                rawOptText = rawOptText
                    .replace(Regex("--- PAGE \\d+ ---", RegexOption.IGNORE_CASE), "")
                    .replace(Regex("\\s+"), " ")
                    .trim()

                if (rawOptText.isNotBlank()) {
                    parsedOptions.add(QuestionOption(optLetter, rawOptText))
                }
            }

            // Ensure at least 4 options
            val seenLetters = parsedOptions.map { it.id }.toSet()
            if ("A" !in seenLetters) parsedOptions.add(0, QuestionOption("A", "Option A"))
            if ("B" !in seenLetters) parsedOptions.add(1.coerceAtMost(parsedOptions.size), QuestionOption("B", "Option B"))
            if ("C" !in seenLetters) parsedOptions.add(2.coerceAtMost(parsedOptions.size), QuestionOption("C", "Option C"))
            if ("D" !in seenLetters) parsedOptions.add(3.coerceAtMost(parsedOptions.size), QuestionOption("D", "Option D"))

            // Extract Right Answer & Option ID
            val rightOptMatch = rightOptPattern.find(qBody)
            val rightAnsMatch = rightAnsPattern.find(qBody)
            val rightAnsText = rightAnsMatch?.groupValues?.get(1)?.replace(Regex("\\s+"), " ")?.trim() ?: ""

            var correctLetter = "A"
            if (rightOptMatch != null && rightOptMatch.groupValues[1] in optIdMap) {
                correctLetter = optIdMap[rightOptMatch.groupValues[1]] ?: "A"
            } else if (rightAnsText.isNotBlank()) {
                // Fallback: match text against option texts
                val matchingOpt = parsedOptions.find { it.text.equals(rightAnsText, ignoreCase = true) }
                if (matchingOpt != null) {
                    correctLetter = matchingOpt.id
                }
            }

            val explanation = if (rightAnsText.isNotBlank()) {
                "Correct Answer: Option $correctLetter — $rightAnsText"
            } else {
                "Correct Answer: Option $correctLetter"
            }

            result.add(
                Question(
                    questionId = "bseb_q_${qNum}_${qId}",
                    sectionName = currentSection,
                    text = rawQText.ifBlank { "Question $qNum" },
                    options = parsedOptions.take(5),
                    correctOption = correctLetter,
                    explanation = explanation
                )
            )

            if (nextSection != null) {
                currentSection = nextSection
            }
        }

        return result
    }

    /**
     * Enhanced general parser for standard question papers (1., Q1., Question 1:, प्रश्न 1:, etc.)
     */
    private fun parseGeneralQuestions(text: String, defaultSection: String): List<Question> {
        val result = mutableListOf<Question>()
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }

        var currentSection = defaultSection
        var currentQText = ""
        var currentOptions = mutableListOf<QuestionOption>()
        var currentAns = "A"
        var currentExp = ""

        // Matches: "Question 1", "Question 1:", "Q.1", "Q1.", "1.", "1)", "प्रश्न 1:", "प्र. 1"
        val qRegex = Regex("^(?:(?:Q(?:uestion)?|प्रश्न|प्र)\\s*[.:\\-]?\\s*(\\d+)[.:\\)\\-–—]?\\s*|(\\d+)[.:\\)\\-–—]\\s*)(.*)", RegexOption.IGNORE_CASE)
        val optRegex = Regex("^[({\\[]?([A-Ea-eक-ङ1-5])[.:\\)\\]}\\-–—]\\s*(.*)")
        val ansRegex = Regex("(?:Right\\s*Option\\s*Id|Right\\s*Answer|Answer|Ans|उत्तर|Correct\\s*Option|Key)\\s*[:\\-–—.]?\\s*[({\\[]?([A-Ea-eक-ङ1-5]?)[)\\]}]?\\s*(.*)", RegexOption.IGNORE_CASE)
        val expRegex = Regex("(?:Explanation|Solution|व्याख्या|हल)\\s*[:\\-–—.]?\\s*(.*)", RegexOption.IGNORE_CASE)
        val secRegex = Regex("^(?:Section|Subject|विषय|खंड|भाग)\\s*[:\\-–—.]?\\s*(.*)", RegexOption.IGNORE_CASE)

        fun mapToLetter(id: String): String {
            return when (id.uppercase()) {
                "A", "1", "क" -> "A"
                "B", "2", "ख" -> "B"
                "C", "3", "ग" -> "C"
                "D", "4", "घ" -> "D"
                "E", "5", "ङ" -> "E"
                else -> id.uppercase()
            }
        }

        fun flushCurrentQuestion() {
            if (currentQText.isNotBlank() && currentOptions.isNotEmpty()) {
                val dedupOptions = mutableListOf<QuestionOption>()
                val seenIds = mutableSetOf<String>()
                for (opt in currentOptions) {
                    if (opt.id !in seenIds) {
                        seenIds.add(opt.id)
                        dedupOptions.add(opt)
                    }
                }

                if (dedupOptions.size == 2) {
                    dedupOptions.add(QuestionOption("C", "Option C"))
                    dedupOptions.add(QuestionOption("D", "Option D"))
                } else if (dedupOptions.size == 3) {
                    dedupOptions.add(QuestionOption("D", "Option D"))
                }

                val finalAns = if (dedupOptions.any { it.id.equals(currentAns, ignoreCase = true) }) {
                    currentAns
                } else {
                    dedupOptions.firstOrNull()?.id ?: "A"
                }

                result.add(
                    Question(
                        questionId = "parsed_q_${UUID.randomUUID().toString().take(8)}",
                        sectionName = currentSection,
                        text = currentQText.trim(),
                        options = dedupOptions,
                        correctOption = finalAns,
                        explanation = currentExp.ifBlank { "Correct option is $finalAns" }
                    )
                )
                currentQText = ""
                currentOptions = mutableListOf()
                currentAns = "A"
                currentExp = ""
            }
        }

        for (line in lines) {
            // Ignore standalone numeric lines (like option IDs 1001, 55001) or page markers
            if (Regex("^\\d{3,8}$").matches(line) || Regex("^--- PAGE \\d+ ---$", RegexOption.IGNORE_CASE).matches(line)) {
                continue
            }

            val secMatch = secRegex.find(line)
            val ansMatch = ansRegex.find(line)
            val expMatch = expRegex.find(line)
            val qMatch = qRegex.find(line)
            val optMatch = optRegex.find(line)

            when {
                secMatch != null -> {
                    val secName = secMatch.groupValues[1].trim()
                    if (secName.isNotBlank()) {
                        currentSection = secName
                    }
                }
                ansMatch != null -> {
                    val rawLetter = ansMatch.groupValues[1].trim()
                    if (rawLetter.isNotBlank()) {
                        currentAns = mapToLetter(rawLetter)
                    }
                }
                expMatch != null -> {
                    currentExp = expMatch.groupValues[1].trim()
                }
                optMatch != null -> {
                    val rawId = optMatch.groupValues[1]
                    val optId = mapToLetter(rawId)
                    val optText = optMatch.groupValues[2].trim()

                    if (currentQText.isNotBlank()) {
                        currentOptions.add(QuestionOption(optId, optText))
                    } else {
                        currentQText += " $line"
                    }
                }
                qMatch != null && (line.startsWith("Q", ignoreCase = true) || line.startsWith("प्रश्न") || line.startsWith("प्र") || currentOptions.isNotEmpty() || currentQText.isBlank()) -> {
                    flushCurrentQuestion()
                    val num = qMatch.groupValues[1].ifBlank { qMatch.groupValues[2] }
                    val textPart = qMatch.groupValues[3].trim()
                    currentQText = if (textPart.isNotBlank()) textPart else line
                }
                else -> {
                    if (currentOptions.isNotEmpty()) {
                        if (currentExp.isNotBlank()) {
                            currentExp += " $line"
                        } else {
                            val lastIdx = currentOptions.size - 1
                            val lastOpt = currentOptions[lastIdx]
                            currentOptions[lastIdx] = lastOpt.copy(text = "${lastOpt.text} $line")
                        }
                    } else if (currentQText.isNotBlank()) {
                        currentQText += " $line"
                    } else {
                        currentQText = line
                    }
                }
            }
        }
        flushCurrentQuestion()

        return result
    }
}
