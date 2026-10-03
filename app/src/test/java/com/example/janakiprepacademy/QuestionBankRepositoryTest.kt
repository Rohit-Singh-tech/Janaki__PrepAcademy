package com.example.janakiprepacademy

import com.google.gson.Gson
import com.google.gson.JsonArray
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.FileReader

class QuestionBankRepositoryTest {

    @Test
    fun testQuestionBankContainsAll155ExamsAnd23300Questions() {
        val file = File("src/main/assets/question_bank.json")
        assertTrue("question_bank.json should exist in assets", file.exists())

        val reader = FileReader(file)
        val jsonArray = Gson().fromJson(reader, JsonArray::class.java)
        reader.close()

        assertEquals("Should contain exactly 155 exams", 155, jsonArray.size())

        var totalQuestions = 0
        var stetCount = 0
        var bpscCount = 0

        for (i in 0 until jsonArray.size()) {
            val examObj = jsonArray.get(i).asJsonObject
            val track = examObj.get("exam_track").asString
            if (track == "BIHAR_STET") stetCount++
            if (track == "BPSC_TEACHER") bpscCount++

            val sections = examObj.getAsJsonArray("sections")
            assertNotNull("Sections should not be null", sections)
            for (s in 0 until sections.size()) {
                val secObj = sections.get(s).asJsonObject
                val questions = secObj.getAsJsonArray("questions")
                assertNotNull("Questions array should not be null", questions)
                totalQuestions += questions.size()
            }
        }

        assertEquals("Should contain 75 Bihar STET exams", 75, stetCount)
        assertEquals("Should contain 80 BPSC TRE exams", 80, bpscCount)
        assertEquals("Should contain exactly 23,300 questions", 23300, totalQuestions)
    }
}
