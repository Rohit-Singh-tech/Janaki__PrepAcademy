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
     * Parses questions, options, answer keys, and explanations from extracted document text.
     * Supports:
     * - Questions: 1., Q1., Question 1:, 1), प्रश्न 1:, प्र. 1
     * - Options: (A)-(E), A.-E., A)-E), [A]-[E], (क)-(ङ), 1.-5.
     * - Answer keys: Answer: B, Ans: C, उत्तर: D, Key: A, Ans - B
     * - Explanations: Explanation:, Solution:, व्याख्या:, हल:
     */
    fun parseQuestionsFromDocumentText(text: String, defaultSection: String = "Uploaded Section"): List<Question> {
        val result = mutableListOf<Question>()
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }

        var currentSection = defaultSection
        var currentQText = ""
        var currentOptions = mutableListOf<QuestionOption>()
        var currentAns = "A"
        var currentExp = ""

        val qRegex = Regex("^(?:Q(?:uestion)?\\s*[.:\\-]?\\s*|प्रश्न\\s*[.:\\-]?\\s*|प्र\\s*[.:\\-]?\\s*)?(\\d+)[.:\\)\\-–—]\\s*(.*)", RegexOption.IGNORE_CASE)
        val optRegex = Regex("^[({\\[]?([A-Ea-eक-ङ1-5])[.:\\)\\]}\\-–—]\\s*(.*)")
        val ansRegex = Regex("(?:Answer|Ans|उत्तर|Correct\\s*Option|Key)\\s*[:\\-–—.]?\\s*[({\\[]?([A-Ea-eक-ङ1-5])[)\\]}]?", RegexOption.IGNORE_CASE)
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
                // Ensure unique option IDs
                val dedupOptions = mutableListOf<QuestionOption>()
                val seenIds = mutableSetOf<String>()
                for (opt in currentOptions) {
                    if (opt.id !in seenIds) {
                        seenIds.add(opt.id)
                        dedupOptions.add(opt)
                    }
                }

                // If only 2-3 options parsed, add standard fallbacks to reach at least 4
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
                    currentAns = mapToLetter(ansMatch.groupValues[1])
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
                    val num = qMatch.groupValues[1]
                    val textPart = qMatch.groupValues[2].trim()
                    currentQText = if (textPart.isNotBlank()) textPart else line
                }
                else -> {
                    // Multi-line continuation
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
