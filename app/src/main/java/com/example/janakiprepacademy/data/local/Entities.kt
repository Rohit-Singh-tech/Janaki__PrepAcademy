package com.example.janakiprepacademy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room database entities for local CBT exam state caching.
 * Ensures zero data loss even if the phone dies mid-exam.
 */

@Entity(tableName = "cbt_progress")
data class CbtProgressEntity(
    @PrimaryKey
    val questionId: String,
    val attemptId: String,
    val examId: String,
    val selectedOptionId: String?,       // "A", "B", "C", "D", "E" or null
    val navigationState: String,         // Maps to QuestionState enum name
    val timeSpentSeconds: Long = 0,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_exams")
data class CachedExamEntity(
    @PrimaryKey
    val examId: String,
    val title: String,
    val category: String,                // ExamTrack enum name
    val totalDurationMinutes: Int,
    val correctMarks: Double,
    val negativeMarks: Double,
    val optionsPerQuestion: Int,
    val jsonPayload: String,             // Full exam JSON for offline mode
    val downloadedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exam_results")
data class ExamResultEntity(
    @PrimaryKey
    val attemptId: String,
    val examId: String,
    val examTitle: String,
    val finalScore: Double,
    val maxScore: Double,
    val accuracyPercentage: Double,
    val allIndiaRank: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val totalQuestions: Int,
    val timeTakenSeconds: Long,
    val completedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val key: String,
    val value: String
)
