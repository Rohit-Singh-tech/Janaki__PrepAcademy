package com.example.janakiprepacademy.data

import com.example.janakiprepacademy.data.model.*

/**
 * Provides sample exam data for Janaki PrepAcademy.
 * In production, this data comes from the Node.js backend API.
 * For now, it serves as a fully functional offline demo dataset.
 */
object SampleDataProvider {

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Bihar STET — Computer Science Mock Test
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private val stetQuestions = listOf(
        Question(
            questionId = "stet_q_001",
            sectionName = "Computer Organization & Architecture",
            text = "Which of the following is used to minimize Boolean expressions?",
            options = listOf(
                QuestionOption("A", "Truth Table"),
                QuestionOption("B", "Karnaugh Map (K-Map)"),
                QuestionOption("C", "Venn Diagram"),
                QuestionOption("D", "Flow Chart")
            ),
            correctOption = "B",
            explanation = "Karnaugh Maps (K-Maps) are used to simplify Boolean algebra expressions, reducing the number of logic gates needed."
        ),
        Question(
            questionId = "stet_q_002",
            sectionName = "Programming & Data Structures",
            text = "What is the time complexity of searching an element in a balanced Binary Search Tree (BST)?",
            options = listOf(
                QuestionOption("A", "O(n)"),
                QuestionOption("B", "O(log n)"),
                QuestionOption("C", "O(n log n)"),
                QuestionOption("D", "O(1)")
            ),
            correctOption = "B",
            explanation = "In a balanced BST, the height is O(log n), so searching takes O(log n) time as we traverse from root to leaf."
        ),
        Question(
            questionId = "stet_q_003",
            sectionName = "Database Management Systems",
            text = "Which normal form eliminates transitive dependency?",
            options = listOf(
                QuestionOption("A", "First Normal Form (1NF)"),
                QuestionOption("B", "Second Normal Form (2NF)"),
                QuestionOption("C", "Third Normal Form (3NF)"),
                QuestionOption("D", "BCNF")
            ),
            correctOption = "C",
            explanation = "Third Normal Form (3NF) removes transitive dependencies, where a non-key attribute depends on another non-key attribute."
        ),
        Question(
            questionId = "stet_q_004",
            sectionName = "Operating Systems",
            text = "Which CPU scheduling algorithm can cause starvation?",
            options = listOf(
                QuestionOption("A", "Round Robin"),
                QuestionOption("B", "First Come First Serve"),
                QuestionOption("C", "Shortest Job First (Preemptive)"),
                QuestionOption("D", "FIFO")
            ),
            correctOption = "C",
            explanation = "Shortest Job First (SJF) preemptive scheduling can starve long processes because shorter jobs keep arriving and getting scheduled first."
        ),
        Question(
            questionId = "stet_q_005",
            sectionName = "Computer Networks",
            text = "Which layer of the OSI model is responsible for routing and logical addressing?",
            options = listOf(
                QuestionOption("A", "Data Link Layer"),
                QuestionOption("B", "Transport Layer"),
                QuestionOption("C", "Network Layer"),
                QuestionOption("D", "Session Layer")
            ),
            correctOption = "C",
            explanation = "The Network Layer (Layer 3) handles logical addressing (IP addresses) and routing decisions to forward packets across networks."
        ),
        Question(
            questionId = "stet_q_006",
            sectionName = "Programming & Data Structures",
            text = "Which data structure uses LIFO (Last In, First Out) principle?",
            options = listOf(
                QuestionOption("A", "Queue"),
                QuestionOption("B", "Stack"),
                QuestionOption("C", "Linked List"),
                QuestionOption("D", "Array")
            ),
            correctOption = "B",
            explanation = "Stack follows LIFO principle — the last element pushed onto the stack is the first one to be popped off."
        ),
        Question(
            questionId = "stet_q_007",
            sectionName = "Database Management Systems",
            text = "SQL command 'DROP TABLE' belongs to which category?",
            options = listOf(
                QuestionOption("A", "DML (Data Manipulation Language)"),
                QuestionOption("B", "DDL (Data Definition Language)"),
                QuestionOption("C", "DCL (Data Control Language)"),
                QuestionOption("D", "TCL (Transaction Control Language)")
            ),
            correctOption = "B",
            explanation = "DROP TABLE is a DDL command as it modifies the database schema structure by removing an entire table definition."
        ),
        Question(
            questionId = "stet_q_008",
            sectionName = "Computer Organization & Architecture",
            text = "What is the function of the Program Counter (PC) register in a CPU?",
            options = listOf(
                QuestionOption("A", "Stores the result of ALU operations"),
                QuestionOption("B", "Holds the address of the next instruction to be executed"),
                QuestionOption("C", "Stores the current instruction being decoded"),
                QuestionOption("D", "Manages interrupt handling")
            ),
            correctOption = "B",
            explanation = "The Program Counter (PC) always holds the memory address of the next instruction that the CPU should fetch and execute."
        ),
        Question(
            questionId = "stet_q_009",
            sectionName = "Art of Teaching & Pedagogy",
            text = "According to Bloom's Taxonomy, which is the highest level of cognitive learning?",
            options = listOf(
                QuestionOption("A", "Analysis"),
                QuestionOption("B", "Application"),
                QuestionOption("C", "Evaluation"),
                QuestionOption("D", "Creation")
            ),
            correctOption = "D",
            explanation = "In the revised Bloom's Taxonomy, 'Create' (synthesis/creation of new ideas) is the highest level of cognitive learning."
        ),
        Question(
            questionId = "stet_q_010",
            sectionName = "Art of Teaching & Pedagogy",
            text = "The concept of 'Zone of Proximal Development' (ZPD) was proposed by which psychologist?",
            options = listOf(
                QuestionOption("A", "Jean Piaget"),
                QuestionOption("B", "Lev Vygotsky"),
                QuestionOption("C", "B.F. Skinner"),
                QuestionOption("D", "John Dewey")
            ),
            correctOption = "B",
            explanation = "Lev Vygotsky proposed the ZPD — the gap between what a learner can do independently and what they can achieve with expert guidance."
        )
    )

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // BPSC Teacher — Computer Science (with 5th option)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private val bpscTeacherQuestions = listOf(
        Question(
            questionId = "bpsc_t_001",
            sectionName = "Computer Science Core",
            text = "Which of the following layers of the OSI model ensures data security and encryption between applications?",
            options = listOf(
                QuestionOption("A", "Session Layer"),
                QuestionOption("B", "Presentation Layer"),
                QuestionOption("C", "Transport Layer"),
                QuestionOption("D", "Application Layer"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "B",
            explanation = "The Presentation Layer (Layer 6) handles data encryption, compression, and translation between application formats."
        ),
        Question(
            questionId = "bpsc_t_002",
            sectionName = "Computer Science Core",
            text = "In Python, which of the following is used to handle exceptions?",
            options = listOf(
                QuestionOption("A", "if-else"),
                QuestionOption("B", "for-while"),
                QuestionOption("C", "try-except"),
                QuestionOption("D", "switch-case"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "C",
            explanation = "Python uses try-except blocks for exception handling. The 'try' block contains code that might raise an exception, and 'except' handles it."
        ),
        Question(
            questionId = "bpsc_t_003",
            sectionName = "General Studies",
            text = "The Mahabodhi Temple, a UNESCO World Heritage Site, is located in which district of Bihar?",
            options = listOf(
                QuestionOption("A", "Patna"),
                QuestionOption("B", "Nalanda"),
                QuestionOption("C", "Gaya"),
                QuestionOption("D", "Vaishali"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "C",
            explanation = "The Mahabodhi Temple is located in Bodh Gaya, Gaya district, Bihar, where Gautama Buddha is said to have attained enlightenment."
        ),
        Question(
            questionId = "bpsc_t_004",
            sectionName = "Computer Science Core",
            text = "What is a deadlock in operating systems?",
            options = listOf(
                QuestionOption("A", "A process waiting for CPU time"),
                QuestionOption("B", "A situation where processes wait for each other indefinitely"),
                QuestionOption("C", "A memory overflow condition"),
                QuestionOption("D", "A type of scheduling algorithm"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "B",
            explanation = "Deadlock occurs when two or more processes are blocked forever, each waiting for a resource held by the other."
        ),
        Question(
            questionId = "bpsc_t_005",
            sectionName = "Computer Science Core",
            text = "In SQL, which keyword is used to remove duplicate rows from a query result?",
            options = listOf(
                QuestionOption("A", "UNIQUE"),
                QuestionOption("B", "DISTINCT"),
                QuestionOption("C", "REMOVE"),
                QuestionOption("D", "FILTER"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "B",
            explanation = "The DISTINCT keyword eliminates duplicate rows from the result set of a SELECT query."
        ),
        Question(
            questionId = "bpsc_t_006",
            sectionName = "General Studies",
            text = "Which river is known as the 'Sorrow of Bihar'?",
            options = listOf(
                QuestionOption("A", "Gandak"),
                QuestionOption("B", "Son"),
                QuestionOption("C", "Kosi"),
                QuestionOption("D", "Bagmati"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "C",
            explanation = "The Kosi River is called the 'Sorrow of Bihar' due to its frequent course changes and devastating floods."
        ),
        Question(
            questionId = "bpsc_t_007",
            sectionName = "Computer Science Core",
            text = "Which of the following is NOT a valid Python data type?",
            options = listOf(
                QuestionOption("A", "list"),
                QuestionOption("B", "tuple"),
                QuestionOption("C", "dictionary"),
                QuestionOption("D", "array"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "D",
            explanation = "Python does not have a built-in 'array' data type. Lists serve a similar purpose. The 'array' module exists but is not a fundamental type."
        ),
        Question(
            questionId = "bpsc_t_008",
            sectionName = "Computer Science Core",
            text = "What is the primary purpose of a firewall in network security?",
            options = listOf(
                QuestionOption("A", "To increase network speed"),
                QuestionOption("B", "To monitor and filter incoming/outgoing network traffic"),
                QuestionOption("C", "To compress data packets"),
                QuestionOption("D", "To assign IP addresses"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "B",
            explanation = "A firewall monitors and controls network traffic based on predetermined security rules, acting as a barrier between trusted and untrusted networks."
        ),
        Question(
            questionId = "bpsc_t_009",
            sectionName = "Language",
            text = "Choose the correct meaning of the idiom: 'To burn the midnight oil'",
            options = listOf(
                QuestionOption("A", "To waste resources"),
                QuestionOption("B", "To work or study late into the night"),
                QuestionOption("C", "To start a fire"),
                QuestionOption("D", "To cook at night"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "B",
            explanation = "'To burn the midnight oil' means to work or study late into the night, referring to the historical use of oil lamps."
        ),
        Question(
            questionId = "bpsc_t_010",
            sectionName = "Computer Science Core",
            text = "Which of the following sorting algorithms has the best average-case time complexity?",
            options = listOf(
                QuestionOption("A", "Bubble Sort"),
                QuestionOption("B", "Insertion Sort"),
                QuestionOption("C", "Merge Sort"),
                QuestionOption("D", "Selection Sort"),
                QuestionOption("E", "None of the above / More than one")
            ),
            correctOption = "C",
            explanation = "Merge Sort has an average (and worst) case time complexity of O(n log n), making it more efficient than the O(n²) algorithms listed."
        )
    )

    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    // Available Mock Tests (Built-in + Admin Created)
    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    private const val PREFS_NAME = "janaki_custom_exams_prefs"
    private const val KEY_CUSTOM_EXAMS = "custom_exams_json"

    private val customExams = mutableListOf<Exam>()

    fun init(context: android.content.Context) {
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
            val json = prefs.getString(KEY_CUSTOM_EXAMS, null)
            if (!json.isNullOrBlank()) {
                val type = object : com.google.gson.reflect.TypeToken<List<Exam>>() {}.type
                val saved: List<Exam> = com.google.gson.Gson().fromJson(json, type)
                customExams.clear()
                customExams.addAll(saved)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun addCustomExam(exam: Exam, context: android.content.Context? = null) {
        customExams.removeAll { it.examId == exam.examId }
        customExams.add(0, exam)
        persistExams(context)
    }

    fun appendQuestionsToExam(examId: String, newQuestions: List<Question>, context: android.content.Context? = null): Exam? {
        val target = getAvailableExams().find { it.examId == examId } ?: return null
        val groupedNew = newQuestions.groupBy { it.sectionName.ifBlank { "Domain Subject" } }

        val existingSections = target.sections.toMutableList()
        val updatedSections = mutableListOf<ExamSection>()

        if (existingSections.size <= 1 && (existingSections.isEmpty() || existingSections.first().questions.isEmpty())) {
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
        return customExams + builtInExams.filter { it.examId !in customIds }
    }

    private val builtInExams: List<Exam> by lazy {
        listOf(
            Exam(
            examId = "stet_cs_mock_01",
            title = "Bihar STET CS - Mock Test 1",
            category = ExamTrack.BIHAR_STET,
            totalDurationMinutes = 150,
            correctMarks = 1.0,
            negativeMarks = 0.0,
            optionsPerQuestion = 4,
            sections = listOf(
                ExamSection("sec_coa", "Computer Organization & Architecture",
                    stetQuestions.filter { it.sectionName == "Computer Organization & Architecture" }),
                ExamSection("sec_pds", "Programming & Data Structures",
                    stetQuestions.filter { it.sectionName == "Programming & Data Structures" }),
                ExamSection("sec_dbms", "Database Management Systems",
                    stetQuestions.filter { it.sectionName == "Database Management Systems" }),
                ExamSection("sec_os", "Operating Systems",
                    stetQuestions.filter { it.sectionName == "Operating Systems" }),
                ExamSection("sec_cn", "Computer Networks",
                    stetQuestions.filter { it.sectionName == "Computer Networks" }),
                ExamSection("sec_pedagogy", "Art of Teaching & Pedagogy",
                    stetQuestions.filter { it.sectionName == "Art of Teaching & Pedagogy" })
            ),
            isFree = true,
            totalAttempts = 2847
        ),
        Exam(
            examId = "stet_cs_mock_02",
            title = "Bihar STET CS - Mock Test 2",
            category = ExamTrack.BIHAR_STET,
            totalDurationMinutes = 150,
            correctMarks = 1.0,
            negativeMarks = 0.0,
            optionsPerQuestion = 4,
            sections = listOf(
                ExamSection("sec_all", "Full Syllabus", stetQuestions)
            ),
            isFree = true,
            totalAttempts = 1523
        ),
        Exam(
            examId = "bpsc_tre_cs_mock_01",
            title = "BPSC Teacher CS - Mock Test 1",
            category = ExamTrack.BPSC_TEACHER,
            totalDurationMinutes = 120,
            correctMarks = 1.0,
            negativeMarks = 0.25,
            optionsPerQuestion = 5,
            sections = listOf(
                ExamSection("sec_lang", "Language (Qualifying)",
                    bpscTeacherQuestions.filter { it.sectionName == "Language" }),
                ExamSection("sec_gs", "General Studies",
                    bpscTeacherQuestions.filter { it.sectionName == "General Studies" }),
                ExamSection("sec_cs", "Computer Science Core",
                    bpscTeacherQuestions.filter { it.sectionName == "Computer Science Core" })
            ),
            isFree = true,
            totalAttempts = 3421
        ),
        Exam(
            examId = "bpsc_tre_cs_mock_02",
            title = "BPSC Teacher CS - Mock Test 2",
            category = ExamTrack.BPSC_TEACHER,
            totalDurationMinutes = 120,
            correctMarks = 1.0,
            negativeMarks = 0.25,
            optionsPerQuestion = 5,
            sections = listOf(
                ExamSection("sec_all", "Full Syllabus", bpscTeacherQuestions)
            ),
            isFree = true,
            totalAttempts = 1876
        ),
        Exam(
            examId = "bpsc_cce_mock_01",
            title = "BPSC 72nd CCE Prelims - Bihar GK",
            category = ExamTrack.BPSC_CCE,
            totalDurationMinutes = 120,
            correctMarks = 1.0,
            negativeMarks = 0.25,
            optionsPerQuestion = 5,
            sections = listOf(
                ExamSection("sec_gs", "General Studies", bpscTeacherQuestions.filter {
                    it.sectionName == "General Studies"
                }),
                ExamSection("sec_cs", "Science & Technology", bpscTeacherQuestions.filter {
                    it.sectionName == "Computer Science Core"
                })
            ),
            isFree = true,
            totalAttempts = 5632
        ),
        Exam(
            examId = "upsc_prelims_mock_01",
            title = "UPSC Prelims GS - Mock Test 1",
            category = ExamTrack.UPSC_CSE,
            totalDurationMinutes = 120,
            correctMarks = 2.0,
            negativeMarks = 0.66,
            optionsPerQuestion = 4,
            sections = listOf(
                ExamSection("sec_gs", "General Studies", stetQuestions.take(5)),
                ExamSection("sec_science", "Science & Technology", stetQuestions.takeLast(5))
            ),
            isFree = true,
            totalAttempts = 8945
        )
    ) }

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
