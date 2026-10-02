package com.example.janakiprepacademy

import com.example.janakiprepacademy.data.PdfQuestionExtractor
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class PdfQuestionExtractorTest {

    @Test
    fun testParseBsebOfficialPaper() {
        val dumpFile = File("d:/JanakiPrepAcademy/pdf_dump.txt")
        if (!dumpFile.exists()) {
            println("pdf_dump.txt not found, skipping dump test")
            return
        }

        val rawText = dumpFile.readText(Charsets.UTF_8)
        val questions = PdfQuestionExtractor.parseQuestionsFromDocumentText(rawText, "Comprehensive Paper")

        println("Extracted ${questions.size} questions from official paper dump")
        assertEquals(150, questions.size)

        // Verify Question 1
        val q1 = questions[0]
        assertTrue("Q1 text should contain Boolean function", q1.text.contains("Boolean function"))
        assertEquals(4, q1.options.size)
        assertEquals("A", q1.correctOption)
        assertFalse("Option A should not contain trailing ID number", q1.options[0].text.endsWith("1001"))

        // Verify sections
        val csQuestions = questions.filter { it.sectionName == "Computer Science" }
        val artQuestions = questions.filter { it.sectionName == "Art Of Teaching" }
        val otherQuestions = questions.filter { it.sectionName == "Other Skills" }

        println("CS: ${csQuestions.size}, Art: ${artQuestions.size}, Other: ${otherQuestions.size}")
        assertEquals(100, csQuestions.size)
        assertEquals(30, artQuestions.size)
        assertEquals(20, otherQuestions.size)

        // Verify Question 146 (from user's screenshot)
        val q146 = questions[145]
        assertTrue("Q146 should be Monday : April", q146.text.contains("Monday : April"))
        assertEquals("Other Skills", q146.sectionName)
        assertEquals("C", q146.correctOption)
        assertEquals("July", q146.options.find { it.id == "A" }?.text)
        assertEquals("Saturday", q146.options.find { it.id == "B" }?.text)
        assertEquals("August", q146.options.find { it.id == "C" }?.text)
        assertEquals("Tuesday", q146.options.find { it.id == "D" }?.text)

        // Verify every question has at least 4 options and valid answer
        for (q in questions) {
            assertTrue("Every question must have >= 4 options: ${q.text}", q.options.size >= 4)
            assertTrue("Every question must have correct option in options: ${q.correctOption}", q.options.any { it.id == q.correctOption })
            assertFalse("Question text should not be blank", q.text.isBlank())
        }
    }

    @Test
    fun testParseBseb2024OfficialPaper() {
        val dumpFile = File("d:/JanakiPrepAcademy/pdf_2024_dump.txt")
        if (!dumpFile.exists()) {
            println("pdf_2024_dump.txt not found, skipping 2024 test")
            return
        }

        val rawText = dumpFile.readText(Charsets.UTF_8)
        val questions = PdfQuestionExtractor.parseQuestionsFromDocumentText(rawText, "Comprehensive Paper")

        println("Extracted ${questions.size} questions from 2024 official paper dump")
        assertEquals(150, questions.size)

        // Verify Question 1 (QID 301 from user's image)
        val q1 = questions[0]
        assertTrue("Q1 text should contain Boolean function", q1.text.contains("Boolean function"))
        assertEquals(4, q1.options.size)
        assertEquals("C", q1.correctOption)
        assertEquals("A + B + C", q1.options[0].text)
        assertEquals("A'B + AC", q1.options[1].text)
        assertEquals("A + B + C'", q1.options[2].text)
        assertEquals("A'B'C", q1.options[3].text)

        // Verify sections
        val csQuestions = questions.filter { it.sectionName == "Computer Science" }
        val artQuestions = questions.filter { it.sectionName == "Art Of Teaching" }
        val otherQuestions = questions.filter { it.sectionName == "Other Skills" }

        println("2024: CS: ${csQuestions.size}, Art: ${artQuestions.size}, Other: ${otherQuestions.size}")
        assertEquals(100, csQuestions.size)
        assertEquals(30, artQuestions.size)
        assertEquals(20, otherQuestions.size)

        // Verify all 150 questions have at least 4 options and valid answer
        for (q in questions) {
            assertTrue("Every question must have >= 4 options: ${q.text}", q.options.size >= 4)
            assertTrue("Every question must have correct option in options: ${q.correctOption}", q.options.any { it.id == q.correctOption })
            assertFalse("Question text should not be blank", q.text.isBlank())
        }
    }

    @Test
    fun testParseSideBySideHindiOptions() {
        val sampleText = """
            सामान्य ज्ञान एवं अन्य दक्षता
            General Knowledge and Other Skills
            
            1. B का भाई है A, D का पिता है C, B की माता है E, A और D भाई हैं?
            (A) बहन (B) साली
            (C) भतीजी (D) पत्नी
            Answer: C
            
            2. Which component is used for arithmetic operations in a computer?
            (A) ALU (B) CU
            (C) MU (D) BUS
            Ans: A
        """.trimIndent()

        val parsed = PdfQuestionExtractor.parseQuestionsFromDocumentText(sampleText, "General Knowledge")
        assertEquals(2, parsed.size)

        val q1 = parsed[0]
        assertEquals(4, q1.options.size)
        assertEquals("A", q1.options[0].id)
        assertEquals("बहन", q1.options[0].text)
        assertEquals("B", q1.options[1].id)
        assertEquals("साली", q1.options[1].text)
        assertEquals("C", q1.options[2].id)
        assertEquals("भतीजी", q1.options[2].text)
        assertEquals("D", q1.options[3].id)
        assertEquals("पत्नी", q1.options[3].text)
        assertEquals("C", q1.correctOption)

        val q2 = parsed[1]
        assertEquals(4, q2.options.size)
        assertEquals("ALU", q2.options[0].text)
        assertEquals("CU", q2.options[1].text)
        assertEquals("MU", q2.options[2].text)
        assertEquals("BUS", q2.options[3].text)
        assertEquals("A", q2.correctOption)
    }
}
