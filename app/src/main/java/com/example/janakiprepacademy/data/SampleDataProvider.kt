package com.example.janakiprepacademy.data

import com.example.janakiprepacademy.data.model.*

/**
 * Provides exam repository data for Janaki PrepAcademy.
 * All tests are dynamically driven by official question papers uploaded via PDF or the Admin Panel.
 */
object SampleDataProvider {

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Available Mock Tests (Official Tracks + Admin Created)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private const val PREFS_NAME = "janaki_custom_exams_prefs"
    private const val KEY_CUSTOM_EXAMS = "custom_exams_json"

    private val customExams = mutableListOf<Exam>()

    fun init(context: android.content.Context) {
        try {
            QuestionBankRepository.init(context)
            val prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
            val json = prefs.getString(KEY_CUSTOM_EXAMS, null)
            if (!json.isNullOrBlank()) {
                val type = object : com.google.gson.reflect.TypeToken<List<Exam>>() {}.type
                val saved: List<Exam> = com.google.gson.Gson().fromJson(json, type)
                customExams.clear()
                // Purge any legacy static mock tests that had dummy questions, or on-the-fly CBT simulation sessions
                val filtered = saved.filterNot { exam ->
                    (exam.examId in setOf("stet_cs_mock_01", "stet_cs_mock_02", "bpsc_tre_cs_mock_01", "bpsc_tre_cs_mock_02", "bpsc_cce_mock_01", "upsc_prelims_mock_01") &&
                            exam.sections.all { s -> s.questions.all { q -> q.questionId.startsWith("stet_q_") || q.questionId.startsWith("bpsc_q_") } }) ||
                    exam.examId.startsWith("cbt_sim_") ||
                    exam.title.contains("Simulation", ignoreCase = true) ||
                    exam.title.contains("CBT-SIM", ignoreCase = true)
                }
                customExams.addAll(filtered)
                persistExams(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getExamById(examId: String): Exam? {
        if (QuestionBankRepository.activeSimulationExam?.examId == examId) {
            return QuestionBankRepository.activeSimulationExam
        }
        return getAvailableExams().find { it.examId == examId }
    }

    private fun persistExams(context: android.content.Context?) {
        if (context == null) return
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
            val json = com.google.gson.Gson().toJson(customExams)
            prefs.edit().putString(KEY_CUSTOM_EXAMS, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearCustomExams(context: android.content.Context?) {
        customExams.clear()
        persistExams(context)
    }

    fun addCustomExam(exam: Exam, context: android.content.Context? = null) {
        customExams.removeAll { it.examId == exam.examId }
        customExams.add(0, exam)
        persistExams(context)
    }

    fun appendQuestionsToExam(examId: String, newQuestions: List<Question>, context: android.content.Context? = null): Exam? {
        val target = getExamById(examId) ?: return null
        val groupedNew = newQuestions.groupBy { it.sectionName.ifBlank { "Domain Subject" } }

        val existingSections = target.sections.toMutableList()
        val updatedSections = mutableListOf<ExamSection>()

        if (existingSections.isEmpty() || (existingSections.size == 1 && existingSections.first().questions.isEmpty())) {
            // Replace placeholder with new structured sections
            groupedNew.entries.forEachIndexed { idx, entry ->
                updatedSections.add(ExamSection("sec_${idx + 1}", entry.key, entry.value))
            }
        } else {
            val assignedKeys = mutableSetOf<String>()
            for (sec in existingSections) {
                val matching = groupedNew.entries.find { it.key.equals(sec.name, ignoreCase = true) }
                if (matching != null) {
                    updatedSections.add(sec.copy(questions = sec.questions + matching.value))
                    assignedKeys.add(matching.key)
                } else {
                    updatedSections.add(sec)
                }
            }
            for ((secName, qList) in groupedNew) {
                if (secName !in assignedKeys) {
                    if (existingSections.size <= 1 && assignedKeys.isEmpty()) {
                        updatedSections.add(
                            ExamSection("sec_${java.util.UUID.randomUUID().toString().take(6)}", secName, qList)
                        )
                    } else {
                        val first = updatedSections.firstOrNull()
                        if (first != null) {
                            updatedSections[0] = first.copy(questions = first.questions + qList)
                        } else {
                            updatedSections.add(ExamSection("sec_main", secName, qList))
                        }
                    }
                }
            }
        }

        val finalSections = if (updatedSections.isEmpty()) {
            listOf(ExamSection("sec_main", "Uploaded Questions", newQuestions))
        } else {
            updatedSections
        }

        val updated = target.copy(sections = finalSections)
        customExams.removeAll { it.examId == examId }
        customExams.add(0, updated)
        persistExams(context)
        return updated
    }

    fun getAvailableExams(): List<Exam> {
        val customIds = customExams.map { it.examId }.toSet()
        val officialFromBank = QuestionBankRepository.getLoadedOfficialExams().filter { it.examId !in customIds }
        val allSeenIds = (customIds + officialFromBank.map { it.examId }).toSet()
        val officialCategories = (customExams + officialFromBank).map { it.category }.toSet()
        val filteredBuiltIn = builtInExams.filter { builtIn ->
            builtIn.examId !in allSeenIds && (builtIn.sections.isNotEmpty() || builtIn.category !in officialCategories)
        }
        return customExams + officialFromBank + filteredBuiltIn
    }

    private val builtInExams: List<Exam> by lazy {
        listOf(
            Exam(
                examId = "stet_cs_official_paper",
                title = "Bihar STET Paper II - Computer Science",
                category = ExamTrack.BIHAR_STET,
                totalDurationMinutes = 150,
                correctMarks = 1.0,
                negativeMarks = 0.0,
                optionsPerQuestion = 4,
                sections = emptyList(),
                isFree = true,
                totalAttempts = 0
            ),
            Exam(
                examId = "bpsc_tre_cs_official_paper",
                title = "BPSC Teacher (TRE) - PGT Computer Science",
                category = ExamTrack.BPSC_TEACHER,
                totalDurationMinutes = 150,
                correctMarks = 1.0,
                negativeMarks = 0.33,
                optionsPerQuestion = 5,
                sections = emptyList(),
                isFree = true,
                totalAttempts = 0
            ),
            Exam(
                examId = "bpsc_cce_official_paper",
                title = "BPSC Civil Services (CCE) Prelims",
                category = ExamTrack.BPSC_CCE,
                totalDurationMinutes = 120,
                correctMarks = 1.0,
                negativeMarks = 0.33,
                optionsPerQuestion = 5,
                sections = emptyList(),
                isFree = true,
                totalAttempts = 0
            ),
            Exam(
                examId = "upsc_cse_prelims_official_paper",
                title = "UPSC Civil Services (CSE) Prelims Paper I",
                category = ExamTrack.UPSC_CSE,
                totalDurationMinutes = 120,
                correctMarks = 2.0,
                negativeMarks = 0.66,
                optionsPerQuestion = 4,
                sections = emptyList(),
                isFree = true,
                totalAttempts = 0
            )
        )
    }

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Sample Leaderboard Data
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    fun getSampleLeaderboard(): List<LeaderboardEntry> = listOf(
        LeaderboardEntry(1, "Amit Kumar", "Patna", 148.0, 98.7),
        LeaderboardEntry(2, "Priya Kumari", "Sitamarhi", 145.0, 96.7),
        LeaderboardEntry(3, "Rajesh Ranjan", "Muzaffarpur", 142.0, 94.7),
        LeaderboardEntry(4, "Sneha Sinha", "Darbhanga", 140.0, 93.3),
        LeaderboardEntry(5, "Vikash Yadav", "Sitamarhi", 138.5, 92.3),
        LeaderboardEntry(6, "Anita Devi", "Gaya", 136.0, 90.7),
        LeaderboardEntry(7, "Rahul Kumar", "Bhagalpur", 134.0, 89.3),
        LeaderboardEntry(8, "Sunita Kumari", "Purnia", 132.5, 88.3),
        LeaderboardEntry(9, "Manoj Singh", "Sitamarhi", 130.0, 86.7),
        LeaderboardEntry(10, "Pooja Sharma", "Saran", 128.0, 85.3),
        LeaderboardEntry(11, "Arun Jha", "Vaishali", 126.5, 84.3),
        LeaderboardEntry(12, "Kavita Kumari", "Sitamarhi", 125.0, 83.3),
        LeaderboardEntry(13, "Sunil Paswan", "Nalanda", 123.0, 82.0),
        LeaderboardEntry(14, "Ritu Rani", "Begusarai", 121.5, 81.0),
        LeaderboardEntry(15, "Deepak Thakur", "Madhubani", 120.0, 80.0)
    )
}
