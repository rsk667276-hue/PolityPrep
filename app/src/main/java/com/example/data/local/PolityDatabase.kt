package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BookmarkDao
import com.example.data.local.dao.QuizAttemptDao
import com.example.data.local.dao.SpacedRepetitionDao
import com.example.data.local.dao.UserStatsDao
import com.example.data.local.entity.BookmarkedQuestionEntity
import com.example.data.local.entity.QuizAttemptEntity
import com.example.data.local.entity.SpacedRepetitionQuestionEntity
import com.example.data.local.entity.UserStatsEntity

@Database(
    entities = [
        QuizAttemptEntity::class,
        BookmarkedQuestionEntity::class,
        SpacedRepetitionQuestionEntity::class,
        UserStatsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class PolityDatabase : RoomDatabase() {
    abstract fun quizAttemptDao(): QuizAttemptDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun spacedRepetitionDao(): SpacedRepetitionDao
    abstract fun userStatsDao(): UserStatsDao

    companion object {
        @Volatile
        private var INSTANCE: PolityDatabase? = null

        fun getDatabase(context: Context): PolityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PolityDatabase::class.java,
                    "indian_polity_quiz_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
