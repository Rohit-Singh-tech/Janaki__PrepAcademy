package com.example.janakiprepacademy.data.model

/**
 * Core domain models for Janaki PrepAcademy.
 * These models represent the business entities used across the app.
 */

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Exam Track — The four pillars of Janaki PrepAcademy
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
enum class ExamTrack(
    val displayName: String,
    val shortName: String,
    val description: String,
    val iconEmoji: String,
    val totalQuestions: Int,
    val durationMinutes: Int,
    val hasNegativeMarking: Boolean,
    val negativeMarkFraction: Double, // e.g. 0.33 for 1/3rd
    val optionsCount: Int            // 4 or 5
) {
    BIHAR_STET(
        displayName = "Bihar STET (Paper II)",
        shortName = "STET",
        description = "State Teacher Eligibility Test — Computer Science",
        iconEmoji = "📝",
        totalQuestions = 150,
        durationMinutes = 150,
        hasNegativeMarking = false,
        negativeMarkFraction = 0.0,
        optionsCount = 4
    ),
    BPSC_TEACHER(
        displayName = "BPSC Teacher (TRE)",
        shortName = "TRE",
        description = "Bihar Public Service Commission — Teacher Recruitment",
        iconEmoji = "🏫",
        totalQuestions = 150,
        durationMinutes = 120,
        hasNegativeMarking = true,
        negativeMarkFraction = 0.25,
        optionsCount = 5
    ),
    BPSC_CCE(
        displayName = "BPSC Civil Services",
        shortName = "BPSC",
        description = "Bihar Combined Competitive Examination — Prelims & Mains",
        iconEmoji = "🏛️",
        totalQuestions = 150,
        durationMinutes = 120,
        hasNegativeMarking = true,
        negativeMarkFraction = 0.25,
        optionsCount = 5
    ),
    UPSC_CSE(
        displayName = "UPSC Civil Services",
        shortName = "UPSC",
        description = "Union Public Service Commission — Prelims, Mains & Interview",
        iconEmoji = "🇮🇳",
        totalQuestions = 100,
        durationMinutes = 120,
        hasNegativeMarking = true,
        negativeMarkFraction = 0.33,
        optionsCount = 4
    )
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// CBT Question State — Matches official exam center palette
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
enum class QuestionState {
    UNVISITED,        // Grey — Haven't opened this question yet
    NOT_ANSWERED,     // Red — Visited but skipped / cleared response
    ANSWERED,         // Green — Selected an option and saved
    MARKED_REVIEW,    // Purple — Flagged for later review (unanswered)
    ANSWERED_MARKED   // Blue — Answered AND marked for review
}

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Question & Option Models
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
data class QuestionOption(
    val id: String,     // "A", "B", "C", "D", "E"
    val text: String,
    val textHindi: String = ""  // Bilingual support
)

data class Question(
    val questionId: String,
    val sectionName: String,
    val text: String,
    val textHindi: String = "",
    val hasImage: Boolean = false,
    val imageUrl: String? = null,
    val options: List<QuestionOption>,
    val correctOption: String,       // "A", "B", etc.
    val explanation: String = "",
    val explanationHindi: String = ""
)

data class ExamSection(
    val sectionId: String,
    val name: String,
    val questions: List<Question>
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Exam (Mock Test) Model
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
data class Exam(
    val examId: String,
    val title: String,
    val category: ExamTrack,
    val totalDurationMinutes: Int,
    val correctMarks: Double = 1.0,
    val negativeMarks: Double = 0.0,
    val allowSectionHop: Boolean = true,
    val optionsPerQuestion: Int = 4,
    val sections: List<ExamSection>,
    val isFree: Boolean = true,
    val isPractice: Boolean = false,
    val totalAttempts: Int = 0
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// User Response (CBT Progress)
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
data class UserResponse(
    val questionId: String,
    val selectedOption: String?,       // null if skipped
    val state: QuestionState,
    val timeSpentSeconds: Long = 0
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Scorecard / Analytics Model
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
data class ExamResult(
    val attemptId: String,
    val examId: String,
    val examTitle: String,
    val finalScore: Double,
    val maxScore: Double,
    val accuracyPercentage: Double,
    val allIndiaRank: Int,
    val totalParticipants: Int,
    val percentile: Double,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Long,
    val totalTimeSeconds: Long,
    val sectionWiseBreakdown: List<SectionResult> = emptyList()
)

data class SectionResult(
    val sectionName: String,
    val correct: Int,
    val incorrect: Int,
    val skipped: Int,
    val timeSpentSeconds: Long
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Leaderboard Entry
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
data class LeaderboardEntry(
    val rank: Int,
    val userName: String,
    val district: String,
    val score: Double,
    val accuracy: Double
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// User Profile
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val district: String = "Sitamarhi",
    val selectedTrack: ExamTrack = ExamTrack.BIHAR_STET,
    val isOnboarded: Boolean = false,
    val totalTestsTaken: Int = 0,
    val averageScore: Double = 0.0,
    val bestRank: Int = 0
)

// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
// Bihar Districts List — For onboarding and leaderboard filtering
// ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
val BIHAR_DISTRICTS = listOf(
    "Sitamarhi", "Patna", "Muzaffarpur", "Darbhanga", "Gaya",
    "Bhagalpur", "Purnia", "Saran", "Vaishali", "Nalanda",
    "Begusarai", "Samastipur", "Munger", "Madhubani", "East Champaran",
    "West Champaran", "Saharsa", "Katihar", "Araria", "Kishanganj",
    "Supaul", "Madhepura", "Gopalganj", "Siwan", "Nawada",
    "Aurangabad", "Jehanabad", "Arwal", "Buxar", "Bhojpur",
    "Rohtas", "Kaimur", "Jamui", "Lakhisarai", "Sheikhpura",
    "Sheohar", "Khagaria"
)
