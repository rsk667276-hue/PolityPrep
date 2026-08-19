package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quizTitle: String,
    val categoryId: String,
    val mode: String,
    val difficulty: String,
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val score: Double,
    val maxMarks: Double,
    val accuracy: Double,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarked_questions")
data class BookmarkedQuestionEntity(
    @PrimaryKey
    val questionId: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    val explanation: String,
    val articleReference: String,
    val categoryId: String,
    val difficulty: String,
    val yearTag: String?,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "spaced_repetition_questions")
data class SpacedRepetitionQuestionEntity(
    @PrimaryKey
    val questionId: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    val explanation: String,
    val articleReference: String,
    val categoryId: String,
    val difficulty: String,
    val yearTag: String?,
    val repetitionLevel: Int = 1, // 1 = 2 days, 2 = 4 days, 3 = 7 days
    val incorrectCount: Int = 1,
    val nextReviewTimestamp: Long = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000L),
    val lastReviewedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val userId: String = "local_user",
    val dailyStreak: Int = 1,
    val lastActiveDate: String = "",
    val totalXp: Int = 120, // Initial starter XP
    val highestCombo: Int = 0,
    val dailyQuestionsTarget: Int = 10,
    val dailyQuestionsCompletedToday: Int = 0,
    val unlockedBadges: String = "BRONZE_STARTER", // Comma separated badge IDs
    val soundEffectsEnabled: Boolean = true,
    val totalQuizzesCompleted: Int = 0,
    val totalQuestionsAnswered: Int = 0,
    val totalCorrect: Int = 0,
    val totalIncorrect: Int = 0,
    val dailyFreeQuizzesUsed: Int = 0,
    val bonusAttemptsRemaining: Int = 0,
    val isPremium: Boolean = false,
    val selectedTier: String = "FREE" // "FREE", "PREMIUM_MONTHLY", "PREMIUM_ANNUAL"
)
