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

    @Test
    fun testParse2020ScannedBilingualExamBooklet() {
        val sampleText = """
            [ 307 ]
            सामान्य ज्ञान एवं अन्य दक्षता
            General Knowledge and Other Skills
            ( Q.Nos. 1 to 50 )

            1   B का भाई है A, D का पिता है C, B की माता है E, A और D भाई हैं, तो E का C से क्या रिश्ता
            है ?
            बहन (B) साली
            (C)   भतीजी   (D)   पत्नी

            A is the brother of B, C is the father of D, E is the mother of B, A and D are
            brothers. What is the relation of C with E?
            (A)   Sister   (B)   Sister-in-law
            (C)   Niece   (D)   Wife

            2   अशोक ने उत्तर दिशा की ओर चलना प्रारंभ किया। 30 मीटर चलने के बाद वह अपने बायीं तरफ
            मुड़ा और 40 मीटर चला। पुनः वह बायीं तरफ मुड़ा और 30 मीटर चला। अब वह प्रारंभिक स्थान
            से कितनी दूरी पर है ?
            (A)   50 मीटर   (B)   40 मीटर
            (C)   30 मीटर   (D)   20 मीटर

            Ashok startcd to move in the direction of north. After moving 30m, he turned to
            his left and moved 40 m. Again he turned to his left and moved 30m. Now how
            far is he from the starting point ?
            (A)   50 m   (B)   40 m
            (C)   30 m   (D)   20 m

            RE-ST-608 20 2/48
            |74268 ] Set-B
            Scanned with CamScanner
            testbook GET IT ON
            Google Play

            3.   किसी सांकेतिक भाषा में यदि CHAIR को 53260 तथा pAR को 729 लिखा जाए, तो HEAR को कैसे लिखा जाएगा?
            (A)   3792   (B)   3729
            (C)   3972   (D)   3929

            Tr CHAIR is coded in a symbolic language as 53269 and EAR as 729, then how
            will HEAR be coded ? 3729
            (A)   3792   (B)   3729
            (C)   3972   (D)   3929

            4   यदि '+' का अर्थ ' 4 '-' का अर्थ " + ' और 4' का अर्थ * +' हों, तो
            5 + 4 - 18 ÷ 3 का मान होगा
            (A 26   (B) 14
            (C)   - 34   (D) 6

            If '+ mcans means'+' and' x' means' +', what will be the value of
            5 + 4 - 18 ÷ 3 ?
            (A)   26   (B)   14
            (C)   - 34   (D)   6

            5   निम्नलिखित प्रश्न में लुप्त पद को ज्ञात कीजिए :
            (A)   35   (B)   41
            (C)   36   (D)   40

            Scanned with CamScanner
            testbook GET IT ON
            Google Play
        """.trimIndent()

        val parsed = PdfQuestionExtractor.parseQuestionsFromDocumentText(sampleText, "Default")
        assertEquals(5, parsed.size)

        // Verify Question 1
        val q1 = parsed[0]
        assertEquals("Other Skills", q1.sectionName)
        assertTrue("Q1 text should contain Hindi question", q1.text.contains("B का भाई है A"))
        assertTrue("Q1 text should contain English question", q1.text.contains("A is the brother of B"))
        assertFalse("Q1 text should not contain noise header", q1.text.contains("307"))
        assertFalse("Q1 text should not contain Q.Nos", q1.text.contains("Q.Nos"))
        assertEquals(4, q1.options.size)
        assertEquals("A", q1.options[0].id)
        assertEquals("B", q1.options[1].id)
        assertEquals("C", q1.options[2].id)
        assertEquals("D", q1.options[3].id)
        assertEquals("बहन / Sister", q1.options[0].text)
        assertEquals("साली / Sister-in-law", q1.options[1].text)
        assertEquals("भतीजी / Niece", q1.options[2].text)
        assertEquals("पत्नी / Wife", q1.options[3].text)

        // Verify Question 2
        val q2 = parsed[1]
        assertTrue("Q2 text should contain Ashok question", q2.text.contains("अशोक ने उत्तर दिशा की ओर चलना प्रारंभ किया"))
        assertTrue("Q2 text should contain English Ashok question", q2.text.contains("Ashok startcd to move in the direction of north"))
        assertFalse("Q2 should not contain footer", q2.text.contains("74268"))
        assertFalse("Q2 should not contain RE-ST-608", q2.text.contains("RE-ST-608"))
        assertFalse("Q2 should not contain CamScanner", q2.text.contains("CamScanner"))
        assertEquals(4, q2.options.size)
        assertEquals("50 मीटर / 50 m", q2.options[0].text)
        assertEquals("40 मीटर / 40 m", q2.options[1].text)
        assertEquals("30 मीटर / 30 m", q2.options[2].text)
        assertEquals("20 मीटर / 20 m", q2.options[3].text)

        // Verify Question 3
        val q3 = parsed[2]
        assertTrue("Q3 should contain CHAIR question", q3.text.contains("CHAIR"))
        assertEquals(4, q3.options.size)
        assertEquals("3792", q3.options[0].text)
        assertEquals("3729", q3.options[1].text)

        // Verify Question 4 (math operators in question text and lenient (A 26)
        val q4 = parsed[3]
        assertTrue("Q4 should contain math equation", q4.text.contains("5 + 4 - 18"))
        assertEquals(4, q4.options.size)
        assertEquals("26", q4.options[0].text)
        assertEquals("14", q4.options[1].text)

        // Verify Question 5 (question starting with '5 निम्नलिखित' without dot)
        val q5 = parsed[4]
        assertTrue("Q5 should contain लुप्त पद", q5.text.contains("लुप्त पद"))
        assertEquals(4, q5.options.size)
        assertEquals("35", q5.options[0].text)
        assertEquals("41", q5.options[1].text)
    }
}

