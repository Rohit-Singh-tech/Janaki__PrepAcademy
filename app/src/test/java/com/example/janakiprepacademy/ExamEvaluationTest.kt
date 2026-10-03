package com.example.janakiprepacademy

import com.example.janakiprepacademy.data.ExamAttemptRepository
import com.example.janakiprepacademy.data.model.*
import org.junit.Assert.*
import org.junit.Test

class ExamEvaluationTest {

    @Test
    fun testRealTimeEvaluationComputesAccurateScore() {
        val q1 = Question(
            questionId = "q1",
            sectionName = "Computer Science",
            text = "What is 2+2?",
            options = listOf(
                QuestionOption("A", "4"),
                QuestionOption("B", "3"),
                QuestionOption("C", "5"),
                QuestionOption("D", "6")
            ),
            correctOption = "A"
        )
        val q2 = Question(
            questionId = "q2",
            sectionName = "Computer Science",
            text = "What is 3*3?",
            options = listOf(
                QuestionOption("A", "6"),
                QuestionOption("B", "9"),
                QuestionOption("C", "8"),
                QuestionOption("D", "12")
            ),
            correctOption = "B"
        )
        val q3 = Question(
            questionId = "q3",
            sectionName = "General Studies",
            text = "Capital of Bihar?",
            options = listOf(
                QuestionOption("A", "Patna"),
                QuestionOption("B", "Gaya"),
                QuestionOption("C", "Muzaffarpur"),
                QuestionOption("D", "Darbhanga")
            ),
            correctOption = "A"
        )

        val testExam = Exam(
            examId = "test_exam_01",
            title = "Test CBT Exam",
            category = ExamTrack.BPSC_TEACHER,
            totalDurationMinutes = 150,
            correctMarks = 1.0,
            negativeMarks = 0.33,
            optionsPerQuestion = 4,
            sections = listOf(
                ExamSection("sec_cs", "Computer Science", listOf(q1, q2)),
                ExamSection("sec_gs", "General Studies", listOf(q3))
            )
        )

        // User answers:
        // q1: "A" (CORRECT)
        // q2: "C" (INCORRECT - correct is B)
        // q3: null (SKIPPED)
        val responses = mapOf(
            "q1" to UserResponse("q1", "A", QuestionState.ANSWERED),
            "q2" to UserResponse("q2", "C", QuestionState.ANSWERED),
            "q3" to UserResponse("q3", null, QuestionState.UNVISITED)
        )

        val result = ExamAttemptRepository.evaluateExam(testExam, responses, timeTakenSeconds = 120L)

        assertEquals("Should have 1 correct", 1, result.correctCount)
        assertEquals("Should have 1 incorrect", 1, result.incorrectCount)
        assertEquals("Should have 1 skipped", 1, result.skippedCount)
        assertEquals("Total questions should be 3", 3, result.totalQuestions)
        assertEquals("Max score should be 3.0", 3.0, result.maxScore, 0.01)

        // Expected score: 1 correct (1.0) - 1 incorrect (0.33) = 0.67
        assertEquals(0.67, result.finalScore, 0.01)
        // Expected accuracy: 1 / 2 = 50.0%
        assertEquals(50.0, result.accuracyPercentage, 0.1)

        assertEquals(3, result.questionReviews.size)
        assertTrue("q1 should be correct", result.questionReviews[0].isCorrect)
        assertFalse("q2 should be incorrect", result.questionReviews[1].isCorrect)
        assertTrue("q3 should be skipped", result.questionReviews[2].isSkipped)

        assertEquals(2, result.sectionWiseBreakdown.size)
    }
}
