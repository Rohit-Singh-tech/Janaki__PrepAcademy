package com.example.janakiprepacademy.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.example.janakiprepacademy.data.model.Question
import com.example.janakiprepacademy.data.model.QuestionOption
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream
import java.util.UUID
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * High-performance, 100% Free / Open Source (Apache 2.0)
 * PDF & Document Question Extractor for Janaki PrepAcademy.
 *
 * Supports:
 * 1. Digital text PDFs (PDFBox text stripper)
 * 2. Scanned / Photo / Image-based PDFs (100% Free On-Device Google ML Kit Devanagari + English OCR)
 * 3. Official state board formats:
 *    - Bihar STET 2024 / Sify / Testbook (QID : 301-450, Options 1-4)
 *    - Bihar STET 2023 / BSEB / TCS (Question Id, Option Id, Right Option Id)
 *    - Bihar STET 2020 Re-Exam / Scanned Papers (Photo booklets with Hindi/English side-by-side options)
 *    - Standard question formats (Q1., Question 1:, 1., प्रश्न 1:, etc.)
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
     * Extracts text from a given document Uri (PDF or TXT).
     * Automatically falls back to Google ML Kit On-Device Devanagari + English OCR
     * if the PDF is scanned or image-based (e.g. 2020 Re-Exam paper).
     */
    suspend fun extractTextFromUri(context: Context, uri: Uri): Result<String> {
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
                var extractedText = ""
                var pageCount = 0

                try {
                    contentResolver.openInputStream(uri)?.use { stream ->
                        val document = PDDocument.load(stream)
                        val stripper = PDFTextStripper()
                        stripper.sortByPosition = true
                        extractedText = stripper.getText(document)
                        pageCount = document.numberOfPages
                        document.close()
                    }
                } catch (e: Exception) {
                    // PDFBox fallback
                }

                // Evaluate whether the digital text contains real questions or only watermarks/links
                val stripped = extractedText
                    .replace(Regex("https?://[^\\s]+"), "")
                    .replace(Regex("Page-\\s*\\d+", RegexOption.IGNORE_CASE), "")
                    .replace(Regex("\\s+"), " ")
                    .trim()

                // If digital text is empty or predominantly image-based (< 35 real characters per page),
                // automatically activate on-device Google ML Kit Devanagari OCR
                val isScannedPdf = extractedText.isBlank() || (pageCount > 0 && stripped.length / pageCount < 35)

                if (isScannedPdf) {
                    val ocrResult = extractTextViaOcr(context, uri)
                    if (ocrResult.isSuccess && ocrResult.getOrNull()?.isNotBlank() == true) {
                        return ocrResult
                    }
                }

                if (extractedText.isNotBlank()) {
                    Result.success(extractedText)
                } else {
                    Result.failure(
                        Exception(
                            "No text could be extracted from this PDF.\n" +
                            "Please ensure the document contains readable text or clear page scans."
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Failed to read document: ${e.localizedMessage ?: "Unknown error"}", e))
        }
    }

    /**
     * Free, on-device OCR using Google ML Kit Devanagari & Latin Text Recognizer.
     * Renders each page of the scanned PDF into a high-resolution Bitmap and extracts
     * both Hindi and English text directly on the device with zero cloud or API cost.
     */
    suspend fun extractTextViaOcr(context: Context, uri: Uri): Result<String> {
        return try {
            val pfd = context.contentResolver.openFileDescriptor(uri, "r")
                ?: return Result.failure(Exception("Cannot open PDF file descriptor"))

            val renderer = PdfRenderer(pfd)
            val recognizer = TextRecognition.getClient(DevanagariTextRecognizerOptions.Builder().build())

            val sb = StringBuilder()
            val pageCount = renderer.pageCount

            for (i in 0 until pageCount) {
                val page = renderer.openPage(i)
                val targetWidth = 1400f
                val scale = (targetWidth / page.width.toFloat()).coerceIn(1.4f, 2.2f)
                val width = (page.width * scale).toInt().coerceAtLeast(1)
                val height = (page.height * scale).toInt().coerceAtLeast(1)
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.WHITE)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()

                val inputImage = InputImage.fromBitmap(bitmap, 0)
                val visionText = suspendCancellableCoroutine<com.google.mlkit.vision.text.Text> { cont ->
                    recognizer.process(inputImage)
                        .addOnSuccessListener { if (cont.isActive) cont.resume(it) }
                        .addOnFailureListener { if (cont.isActive) cont.resumeWithException(it) }
                }

                sb.append("\n--- PAGE ${i + 1} ---\n")
                val orderedLines = reconstructLinesInReadingOrder(visionText)
                for (line in orderedLines) {
                    sb.append(line).append("\n")
                }
                bitmap.recycle()
            }

            renderer.close()
            pfd.close()
            recognizer.close()

            val result = sb.toString()
            if (result.isNotBlank()) {
                Result.success(result)
            } else {
                Result.failure(Exception("OCR could not recognize text in this scanned PDF."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("On-device OCR failed: ${e.localizedMessage ?: "Unknown error"}", e))
        }
    }

    /**
     * Reconstructs text lines from Google ML Kit TextBlocks in true visual reading order.
     * ML Kit often returns multi-column and side-by-side option blocks out of order.
     * This function extracts every detected line with its bounding box, groups lines that share
     * the same horizontal band into a visual row, sorts each row from left-to-right (preserving
     * side-by-side options like "(A) बहन (B) साली"), and sorts rows from top-to-bottom.
     */
    private fun reconstructLinesInReadingOrder(visionText: com.google.mlkit.vision.text.Text): List<String> {
        val rawLines = mutableListOf<Pair<Rect, String>>()
        for (block in visionText.textBlocks) {
            for (line in block.lines) {
                val text = line.text.trim()
                val box = line.boundingBox
                if (text.isNotBlank() && box != null) {
                    rawLines.add(Pair(box, text))
                }
            }
        }

        if (rawLines.isEmpty()) {
            return visionText.text.lines().map { it.trim() }.filter { it.isNotBlank() }
        }

        // Sort initially by top Y coordinate
        rawLines.sortBy { it.first.top }

        // Group lines into visual horizontal rows
        val rows = mutableListOf<MutableList<Pair<Rect, String>>>()

        for (item in rawLines) {
            val box = item.first
            val itemCenterY = box.centerY()

            var bestRow: MutableList<Pair<Rect, String>>? = null
            var minDiff = Int.MAX_VALUE

            for (row in rows) {
                val rowAvgCenterY = row.map { it.first.centerY() }.average().toInt()
                val rowAvgHeight = row.map { it.first.height() }.average().toInt().coerceAtLeast(12)
                // Threshold: two lines belong to the same visual row if vertical center difference is <= 45% of row height
                val threshold = (rowAvgHeight * 0.45).toInt().coerceIn(12, 35)

                val diff = kotlin.math.abs(itemCenterY - rowAvgCenterY)
                if (diff <= threshold && diff < minDiff) {
                    minDiff = diff
                    bestRow = row
                }
            }

            if (bestRow != null) {
                bestRow.add(item)
            } else {
                rows.add(mutableListOf(item))
            }
        }

        // Sort rows strictly from top to bottom
        rows.sortBy { row ->
            row.map { it.first.centerY() }.average()
        }

        // Within each row, sort lines strictly from left to right and join with whitespace
        return rows.map { row ->
            row.sortBy { it.first.left }
            row.joinToString("   ") { it.second }
        }
    }

    /**
     * Normalizes Unicode whitespaces (non-breaking spaces, zero-width chars),
     * quotation/dash variations, and Devanagari numerals to standard ASCII equivalents.
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
            .replace('०', '0')
            .replace('१', '1')
            .replace('२', '2')
            .replace('३', '3')
            .replace('४', '4')
            .replace('५', '5')
            .replace('६', '6')
            .replace('७', '7')
            .replace('८', '8')
            .replace('९', '9')
    }

    /**
     * Parses questions, options, answer keys, and explanations from extracted document text.
     * Automatically detects:
     * 1. Bihar STET 2024 / Sify / Testbook CBT response sheets (with QID : <ID> and Options: 1-4)
     * 2. Bihar STET 2023 / BSEB / TCS CBT response sheets (with Question Ids, Option Ids, and Right Option Ids)
     * 3. Scanned papers & General formats (side-by-side options, Q1., Question 1:, 1., प्रश्न 1:, etc.)
     */
    fun parseQuestionsFromDocumentText(text: String, defaultSection: String = "Uploaded Section"): List<Question> {
        val normalized = normalizeText(text)

        val isBseb2024Format = normalized.contains("QID :", ignoreCase = true) &&
                normalized.contains("Options:", ignoreCase = true)

        val isBseb2023Format = normalized.contains("Question Id :", ignoreCase = true) ||
                (normalized.contains("Right Option Id", ignoreCase = true) && normalized.contains("Answer : Option Id", ignoreCase = true))

        return when {
            isBseb2024Format -> parseBseb2024QidQuestions(normalized, defaultSection)
            isBseb2023Format -> parseBsebStetQuestions(normalized, defaultSection)
            else -> parseGeneralQuestions(normalized, defaultSection)
        }
    }

    /**
     * Specialized parser for Bihar STET 2024 / Sify / Testbook official response sheets.
     * Extracts all 150 questions (QID 301-450) across Computer Science, Art of Teaching, and Other Skills,
     * strips browser URLs and page headers, cleanly formats bilingual question and options (1-4 -> A-D),
     * and accurately maps official correct answers.
     */
    private fun parseBseb2024QidQuestions(text: String, defaultSection: String): List<Question> {
        val result = mutableListOf<Question>()
        val splitRegex = Regex("QID\\s*:\\s*(\\d+)", RegexOption.IGNORE_CASE)
        val splits = splitRegex.split(text)
        val matches = splitRegex.findAll(text).toList()

        if (matches.isEmpty()) {
            return parseGeneralQuestions(text, defaultSection)
        }

        // Detect domain section from preamble
        var defaultDomainSection = defaultSection
        val preamble = splits.firstOrNull() ?: ""
        val subMatch = Regex("Subject\\s+&\\s+Subject\\s+Code\\s*([^\\n(]+)", RegexOption.IGNORE_CASE).find(preamble)
        if (subMatch != null) {
            val name = subMatch.groupValues[1].trim()
            if (name.isNotBlank()) defaultDomainSection = name
        } else if (preamble.contains("Computer Science", ignoreCase = true)) {
            defaultDomainSection = "Computer Science"
        }

        for (i in matches.indices) {
            val match = matches[i]
            val qidStr = match.groupValues[1]
            val qidNum = qidStr.toIntOrNull() ?: (i + 1)
            val qBody = if (i + 1 < splits.size) splits[i + 1] else ""

            // Clean web noise and browser print headers
            val cleaned = qBody
                .replace(Regex("https?://[^\\s]+"), "")
                .replace(Regex("\\d{2}/\\d{2}/\\d{4},\\s*\\d{2}:\\d{2}"), "")
                .replace(Regex("\\b\\d+/\\d+\\b"), "")
                .replace(Regex("BIHAR\\s+SCHOOL\\s+EXAMINATION\\s+BOARD", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Secondary\\s+Teacher\\s+Eligibility\\s+Test[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("LogoutView\\s+response[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("--- PAGE \\d+ ---", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Paper\\s+Paper\\s*-\\s*2[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Subject\\s+&\\s+Subject\\s+Code[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Date\\s+of\\s+Examination[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Batch\\s+Start\\s+Time[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Batch\\s+End\\s+Time[^\\n]*", RegexOption.IGNORE_CASE), "")
                .replace(Regex("Name\\s+Application\\s+No\\.\\s+Roll\\s+No\\.", RegexOption.IGNORE_CASE), "")

            val optSplit = cleaned.split(Regex("\\bOptions\\s*:\\s*", RegexOption.IGNORE_CASE))
            if (optSplit.size < 2) continue

            val rawQText = optSplit[0]
                .replace(Regex("^[-\\s]+"), "")
                .replace(Regex("\\s+"), " ")
                .trim()

            val rawOptionsPart = optSplit[1]

            // Extract Correct Answer
            val ansMatch = Regex("Correct\\s+Answer\\s*:\\s*([1-4A-Da-d])\\)?\\s*(.*?)(?:Candidate\\s+Answer|$)", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).find(rawOptionsPart)
            val rawKey = ansMatch?.groupValues?.get(1)?.uppercase() ?: "1"
            val correctLetter = when (rawKey) {
                "1" -> "A"
                "2" -> "B"
                "3" -> "C"
                "4" -> "D"
                else -> rawKey
            }
            val ansText = ansMatch?.groupValues?.get(2)?.replace(Regex("\\s+"), " ")?.trim() ?: ""

            // Extract options with line-start requirement to avoid confusing formulas or acronyms like (LSB) with option keys
            val optsOnly = rawOptionsPart.split(Regex("Correct\\s+Answer\\s*:", RegexOption.IGNORE_CASE)).firstOrNull() ?: rawOptionsPart
            val optMatches = Regex("(?:^|\\n)\\s*([1-4A-Da-d])\\)\\s*(.*?)(?=(?:\\n\\s*[1-4A-Da-d]\\)|$))", RegexOption.DOT_MATCHES_ALL).findAll(optsOnly).toList()

            val parsedOptions = mutableListOf<QuestionOption>()
            for (om in optMatches) {
                val numKey = om.groupValues[1].uppercase()
                val letter = when (numKey) {
                    "1" -> "A"
                    "2" -> "B"
                    "3" -> "C"
                    "4" -> "D"
                    else -> numKey
                }
                val oText = om.groupValues[2].replace(Regex("\\s+"), " ").trim()
                if (oText.isNotBlank()) {
                    parsedOptions.add(QuestionOption(letter, oText))
                }
            }

            // Fallback ensure at least 4 options
            val seenLetters = parsedOptions.map { it.id }.toSet()
            if ("A" !in seenLetters) parsedOptions.add(0, QuestionOption("A", "Option A"))
            if ("B" !in seenLetters) parsedOptions.add(1.coerceAtMost(parsedOptions.size), QuestionOption("B", "Option B"))
            if ("C" !in seenLetters) parsedOptions.add(2.coerceAtMost(parsedOptions.size), QuestionOption("C", "Option C"))
            if ("D" !in seenLetters) parsedOptions.add(3.coerceAtMost(parsedOptions.size), QuestionOption("D", "Option D"))

            // Determine Section based on official STET 150-mark pattern:
            // First 100 questions (QID 301-400): Domain Subject (Computer Science)
            // Next 30 questions (QID 401-430): Art of Teaching
            // Final 20 questions (QID 431-450): Other Skills
            val questionIndex = result.size + 1
            val section = when {
                qidNum <= 400 || questionIndex <= 100 -> defaultDomainSection
                qidNum in 401..430 || (questionIndex in 101..130) -> "Art Of Teaching"
                else -> "Other Skills"
            }

            val displayText = if (rawQText.isNotBlank()) rawQText else "Question #$questionIndex (Refer to diagram)"
            val explanation = if (ansText.isNotBlank()) {
                "Correct Answer: Option $correctLetter — $ansText"
            } else {
                "Correct Answer: Option $correctLetter"
            }

            result.add(
                Question(
                    questionId = "bseb2024_qid_${qidStr}",
                    sectionName = section,
                    text = displayText,
                    options = parsedOptions.take(5),
                    correctOption = correctLetter,
                    explanation = explanation
                )
            )
        }

        return result
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
     * Enhanced general parser for standard question papers and scanned papers.
     * Supports side-by-side options (A) (B) on the same line, bilingual Hindi+English questions, etc.
     */
    private fun parseGeneralQuestions(text: String, defaultSection: String): List<Question> {
        val result = mutableListOf<Question>()
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }

        var currentSection = defaultSection
        var currentQText = ""
        var currentOptions = mutableListOf<QuestionOption>()
        var currentAns = "A"
        var currentExp = ""

        // Matches: "Question 1", "Q.1", "1.", "1)", "1 -", "1 B का भाई है", "2 अशोक ने", "5 निम्नलिखित", or standalone "2"
        val qRegex = Regex(
            "^\\s*(?:(?:Q(?:uestion)?|प्रश्न|प्र)\\s*[.:\\-–—]?\\s*([0-9०-९]{1,3})[.:\\)\\-–—]?\\s*(.*)|([0-9०-९]{1,3})\\s*[.:\\)\\-–—]\\s*(.*)|([0-9०-९]{1,3})\\s+([A-Za-z\u0900-\u097F].*)|([0-9०-९]{1,3})\\s*$)",
            RegexOption.IGNORE_CASE
        )
        val unitWords = Regex("^(?:m|मीटर|km|cm|mm|kg|gm|%|s|sec|min|hr|hours?|days?)\\b", RegexOption.IGNORE_CASE)

        val singleOptRegex = Regex("^(?:[({\\[](?:Option\\s*)?([A-Ea-eक-ङ1-5])[)\\]}]?|([A-Ea-eक-ङ])\\s*[.:\\)\\]}–—-]\\s*|(?:Option\\s+([A-Ea-eक-ङ1-5])|([1-5])[)\\]}]))\\s*(.*)", RegexOption.IGNORE_CASE)
        val multiOptPattern = Regex(
            "(?:[({\\[]\\s*([A-Ea-eक-ङ1-5])\\s*[)\\]}]?|(?:^|\\s{2,})([A-Ea-eक-ङ])[.:\\)\\]–—-]\\s*)\\s*([^(){\\[]+?)(?=(?:[({\\[]\\s*[A-Ea-eक-ङ1-5]\\s*[)\\]}]?|\\s{2,}[A-Ea-eक-ङ][.:\\)\\]–—-]\\s*)|$)",
            RegexOption.IGNORE_CASE
        )
        val implicitAPattern = Regex("^(.*?)\\s+[({\\[]?\\s*([Bb2ख])\\s*[)\\]}.:–—-]\\s*(.*)$")
        val ansRegex = Regex("^(?:Right\\s*Option\\s*Id|Right\\s*Answer|Answer|Ans|उत्तर|Correct\\s*Option|Key)\\s*[:\\-–—.]?\\s*(?:Option\\s*)?[({\\[]?([A-Ea-eक-ङ1-5])[)\\]}]?", RegexOption.IGNORE_CASE)
        val expRegex = Regex("^(?:Explanation|Solution|व्याख्या|हल)\\s*[:\\-–—.]?\\s*(.*)", RegexOption.IGNORE_CASE)
        val secRegex = Regex("^(?:Section|Subject|General Knowledge|Computer Science|Art of Teaching|Other Skills|सामान्य ज्ञान|शिक्षण कला|अन्य दक्षता|विषय|खंड|भाग)\\b.*", RegexOption.IGNORE_CASE)

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

        fun addOrMergeOption(rawId: String, text: String) {
            val optText = text.trim()
            if (optText.isBlank()) return
            val optId = mapToLetter(rawId)
            val existingIdx = currentOptions.indexOfFirst { it.id.equals(optId, ignoreCase = true) }
            if (existingIdx != -1) {
                val existing = currentOptions[existingIdx]
                // If the new text is not already present, merge as "Hindi / English"
                if (!existing.text.contains(optText, ignoreCase = true) && !optText.contains(existing.text, ignoreCase = true)) {
                    currentOptions[existingIdx] = existing.copy(text = "${existing.text} / $optText")
                }
            } else {
                currentOptions.add(QuestionOption(optId, optText))
            }
        }

        fun flushCurrentQuestion() {
            if (currentQText.isNotBlank() && currentOptions.isNotEmpty()) {
                val dedupOptions = mutableListOf<QuestionOption>()
                val seenIds = mutableSetOf<String>()
                val sortOrder = listOf("A", "B", "C", "D", "E")
                val sortedList = currentOptions.sortedBy { sortOrder.indexOf(it.id.uppercase()).let { idx -> if (idx >= 0) idx else 99 } }
                for (opt in sortedList) {
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
            // Ignore standalone numeric lines, URLs, page markers, booklet serials, and scan watermarks
            if (Regex("^\\d{1,8}$").matches(line) ||
                Regex("^\\[?\\s*\\d{1,6}\\s*\\]?$").matches(line) ||
                line.contains("CamScanner", ignoreCase = true) ||
                line.contains("testbook", ignoreCase = true) ||
                line.contains("Google Play", ignoreCase = true) ||
                line.contains("GET IT ON", ignoreCase = true) ||
                line.contains("App Store", ignoreCase = true) ||
                Regex("^[|(\\[]?\\s*\\d+\\s*[\\])\\|}]?\\s*Set-[A-Za-z0-9]+.*$", RegexOption.IGNORE_CASE).matches(line) ||
                Regex("^(?:RE-ST|STET|BSEB|SET|PAPER|CODE)[\\w\\s\\-/–—.:]*$", RegexOption.IGNORE_CASE).matches(line) ||
                Regex("^\\d+\\s*/\\s*\\d+$").matches(line) ||
                Regex("^\\(?\\s*Q\\.?\\s*Nos?\\.?\\s*\\d+\\s*to\\s*\\d+\\s*\\)?$", RegexOption.IGNORE_CASE).matches(line) ||
                Regex("^--- PAGE \\d+ ---$", RegexOption.IGNORE_CASE).matches(line) ||
                line.startsWith("http://", ignoreCase = true) ||
                line.startsWith("https://", ignoreCase = true) ||
                Regex("^Page-?\\s*\\d+$", RegexOption.IGNORE_CASE).matches(line)
            ) {
                continue
            }

            val secMatch = secRegex.find(line)
            val ansMatch = ansRegex.find(line)
            val expMatch = expRegex.find(line)
            var qMatch = qRegex.find(line)

            // Guard against math equations (5 + 4 - 18) or measurements (30 m) falsely matching as question numbers
            if (qMatch != null) {
                val rawNum = qMatch.groupValues[1].ifBlank { qMatch.groupValues[3] }.ifBlank { qMatch.groupValues[5] }.ifBlank { qMatch.groupValues[7] }
                val textPart = qMatch.groupValues[2].ifBlank { qMatch.groupValues[4] }.ifBlank { qMatch.groupValues[6] }.trim()
                val isMathOrUnit = unitWords.containsMatchIn(textPart) ||
                    textPart.startsWith("+") || textPart.startsWith("-") || textPart.startsWith("*") ||
                    textPart.startsWith("/") || textPart.startsWith("÷") || textPart.startsWith("=")
                if (isMathOrUnit) {
                    qMatch = null
                }
            }

            // Check if line contains multiple side-by-side options e.g. "(A) बहन   (B) साली"
            val multiOptMatches = multiOptPattern.findAll(line).toList()

            when {
                secMatch != null -> {
                    val secTitle = line.trim()
                    val cleanSec = when {
                        secTitle.contains("Computer Science", ignoreCase = true) || secTitle.contains("कंप्यूटर", ignoreCase = true) -> "Computer Science"
                        secTitle.contains("Art of Teaching", ignoreCase = true) || secTitle.contains("शिक्षण कला", ignoreCase = true) -> "Art of Teaching"
                        secTitle.contains("Other Skills", ignoreCase = true) || secTitle.contains("अन्य दक्षता", ignoreCase = true) -> "Other Skills"
                        secTitle.contains("General Knowledge", ignoreCase = true) || secTitle.contains("सामान्य ज्ञान", ignoreCase = true) -> "General Knowledge"
                        else -> secTitle
                    }
                    currentSection = cleanSec
                    if (currentOptions.isEmpty()) {
                        currentQText = ""
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
                qMatch != null -> {
                    flushCurrentQuestion()
                    val textPart = qMatch.groupValues[2].ifBlank { qMatch.groupValues[4] }.ifBlank { qMatch.groupValues[6] }.trim()
                    currentQText = textPart // can be empty if question number was alone on the line
                }
                multiOptMatches.size >= 2 -> {
                    for (m in multiOptMatches) {
                        val rawId = m.groupValues[1].ifBlank { m.groupValues[2] }
                        val optText = m.groupValues[3].trim()
                        if (rawId.isNotBlank() && optText.isNotBlank()) {
                            addOrMergeOption(rawId, optText)
                        }
                    }
                }
                implicitAPattern.matches(line) -> {
                    val impMatch = implicitAPattern.find(line)!!
                    val prefix = impMatch.groupValues[1].trim()
                    val bId = mapToLetter(impMatch.groupValues[2])
                    val rest = impMatch.groupValues[3].trim()
                    if (prefix.isNotBlank() && prefix.length < 60 && !prefix.endsWith("?")) {
                        addOrMergeOption("A", prefix)
                        addOrMergeOption(bId, rest)
                    } else {
                        if (currentOptions.size >= 4) {
                            currentQText += "\n$line"
                        } else if (currentOptions.isNotEmpty()) {
                            val lastIdx = currentOptions.size - 1
                            val lastOpt = currentOptions[lastIdx]
                            currentOptions[lastIdx] = lastOpt.copy(text = "${lastOpt.text} $line")
                        } else if (currentQText.isNotBlank()) {
                            currentQText += "\n$line"
                        } else {
                            currentQText = line
                        }
                    }
                }
                singleOptRegex.find(line) != null -> {
                    val match = singleOptRegex.find(line)!!
                    val rawId = match.groupValues[1].ifBlank { match.groupValues[2] }.ifBlank { match.groupValues[3] }.ifBlank { match.groupValues[4] }
                    val optText = match.groupValues[5].trim()

                    if (currentQText.isNotBlank()) {
                        addOrMergeOption(rawId, optText)
                    } else {
                        currentQText = line
                    }
                }
                else -> {
                    if (currentOptions.size >= 4) {
                        if (currentExp.isNotBlank()) {
                            currentExp += " $line"
                        } else {
                            currentQText += "\n$line"
                        }
                    } else if (currentOptions.isNotEmpty()) {
                        val lastIdx = currentOptions.size - 1
                        val lastOpt = currentOptions[lastIdx]
                        currentOptions[lastIdx] = lastOpt.copy(text = "${lastOpt.text} $line")
                    } else if (currentQText.isNotBlank()) {
                        currentQText += "\n$line"
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
