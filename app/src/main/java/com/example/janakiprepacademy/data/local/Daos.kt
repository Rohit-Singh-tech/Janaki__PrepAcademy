package com.example.janakiprepacademy.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Objects for Room database operations.
 */

@Dao
interface CbtProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: CbtProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllProgress(progressList: List<CbtProgressEntity>)

    @Query("SELECT * FROM cbt_progress WHERE attemptId = :attemptId ORDER BY lastUpdatedTimestamp")
    suspend fun getProgressForAttempt(attemptId: String): List<CbtProgressEntity>

    @Query("SELECT * FROM cbt_progress WHERE attemptId = :attemptId AND questionId = :questionId")
    suspend fun getProgressForQuestion(attemptId: String, questionId: String): CbtProgressEntity?

    @Query("DELETE FROM cbt_progress WHERE attemptId = :attemptId")
    suspend fun clearProgressForAttempt(attemptId: String)

    @Query("SELECT COUNT(*) FROM cbt_progress WHERE attemptId = :attemptId AND selectedOptionId IS NOT NULL")
    suspend fun getAnsweredCount(attemptId: String): Int
}

@Dao
interface CachedExamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheExam(exam: CachedExamEntity)

    @Query("SELECT * FROM cached_exams WHERE examId = :examId")
    suspend fun getCachedExam(examId: String): CachedExamEntity?

    @Query("SELECT * FROM cached_exams WHERE category = :category ORDER BY downloadedAt DESC")
    fun getCachedExamsByCategory(category: String): Flow<List<CachedExamEntity>>

    @Query("SELECT * FROM cached_exams ORDER BY downloadedAt DESC")
    fun getAllCachedExams(): Flow<List<CachedExamEntity>>

    @Query("DELETE FROM cached_exams WHERE examId = :examId")
    suspend fun deleteCachedExam(examId: String)
}

@Dao
interface ExamResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveResult(result: ExamResultEntity)

    @Query("SELECT * FROM exam_results ORDER BY completedAt DESC")
    fun getAllResults(): Flow<List<ExamResultEntity>>

    @Query("SELECT * FROM exam_results WHERE examId = :examId ORDER BY completedAt DESC")
    fun getResultsForExam(examId: String): Flow<List<ExamResultEntity>>

    @Query("SELECT * FROM exam_results ORDER BY completedAt DESC LIMIT :limit")
    fun getRecentResults(limit: Int = 10): Flow<List<ExamResultEntity>>

    @Query("SELECT AVG(accuracyPercentage) FROM exam_results")
    suspend fun getAverageAccuracy(): Double?

    @Query("SELECT COUNT(*) FROM exam_results")
    suspend fun getTotalTestsTaken(): Int
}

@Dao
interface UserPreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePref(pref: UserPreferencesEntity)

    @Query("SELECT value FROM user_preferences WHERE `key` = :key")
    suspend fun getPref(key: String): String?

    @Query("DELETE FROM user_preferences WHERE `key` = :key")
    suspend fun deletePref(key: String)
}
