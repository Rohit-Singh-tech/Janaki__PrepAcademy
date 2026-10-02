package com.example.janakiprepacademy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Central Room database hub for Janaki PrepAcademy.
 * Manages all local data persistence for offline CBT, results, and preferences.
 */
@Database(
    entities = [
        CbtProgressEntity::class,
        CachedExamEntity::class,
        ExamResultEntity::class,
        UserPreferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cbtProgressDao(): CbtProgressDao
    abstract fun cachedExamDao(): CachedExamDao
    abstract fun examResultDao(): ExamResultDao
    abstract fun userPreferencesDao(): UserPreferencesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "janaki_prepacademy_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
