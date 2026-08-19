package com.example.data.repository

import com.example.data.QuestionBank
import com.example.data.local.PolityDatabase
import com.example.data.local.entity.BookmarkedQuestionEntity
import com.example.data.local.entity.QuizAttemptEntity
import com.example.data.local.entity.SpacedRepetitionQuestionEntity
import com.example.data.local.entity.UserStatsEntity
import com.example.data.remote.GeminiPolityService
import com.example.model.BadgeItem
import com.example.model.DifficultyLevel
import com.example.model.PolityBadges
import com.example.model.PolityCategories
import com.example.model.PolityCategory
import com.example.model.QuizMode
import com.example.model.QuizQuestion
import com.example.model.QuizSummary
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PolityRepository(private val database: PolityDatabase) {

    val allAttempts: Flow<List<QuizAttemptEntity>> = database.quizAttemptDao().getAllAttempts()
    val allBookmarks: Flow<List<BookmarkedQuestionEntity>> = database.bookmarkDao().getAllBookmarks()
    val bookmarkedIds: Flow<List<String>> = database.bookmarkDao().getAllBookmarkedIds()
    val userStats: Flow<UserStatsEntity?> = database.userStatsDao().getUserStatsFlow()

    val allSrsQuestions: Flow<List<SpacedRepetitionQuestionEntity>> = database.spacedRepetitionDao().getAllSrsQuestions()
    fun getDueSrsQuestions(currentTime: Long): Flow<List<SpacedRepetitionQuestionEntity>> =
        database.spacedRepetitionDao().getDueQuestions(currentTime)
    fun getDueSrsCount(currentTime: Long): Flow<Int> =
        database.spacedRepetitionDao().getDueQuestionsCount(currentTime)

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun calculateUpdatedStreak(lastActiveDateStr: String, currentStreak: Int, isActionToday: Boolean): Int {
        if (lastActiveDateStr.isBlank()) return if (isActionToday) 1 else 0
        return try {
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val lastDate = Calendar.getInstance().apply {
                val parsed = dateFormat.parse(lastActiveDateStr) ?: return if (isActionToday) 1 else 0
                time = parsed
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val diffDays = (today.timeInMillis - lastDate.timeInMillis) / (24 * 60 * 60 * 1000L)
            when {
                diffDays == 0L -> currentStreak // Active today already
                diffDays == 1L -> if (isActionToday) currentStreak + 1 else currentStreak // Active yesterday, ready to increment today
                else -> if (isActionToday) 1 else 0 // Broken streak
            }
        } catch (e: Exception) {
            if (isActionToday) 1 else 0
        }
    }

    suspend fun getOrCreateUserStats(): UserStatsEntity {
        val existing = database.userStatsDao().getUserStats()
        val today = dateFormat.format(Date())
        if (existing != null) {
            // Check if it's a new day to reset daily free quizzes and daily target counter
            if (existing.lastActiveDate != today) {
                val maintainedStreak = calculateUpdatedStreak(existing.lastActiveDate, existing.dailyStreak, isActionToday = false)
                val updated = existing.copy(
                    dailyFreeQuizzesUsed = 0,
                    dailyQuestionsCompletedToday = 0,
                    dailyStreak = if (maintainedStreak > 0) maintainedStreak else existing.dailyStreak
                )
                database.userStatsDao().insertOrUpdateStats(updated)
                return updated
            }
            return existing
        } else {
            val initial = UserStatsEntity(
                userId = "local_user",
                dailyStreak = 1,
                lastActiveDate = today,
                totalXp = 120,
                highestCombo = 0,
                dailyQuestionsTarget = 10,
                dailyQuestionsCompletedToday = 0,
                unlockedBadges = "BRONZE_STARTER",
                soundEffectsEnabled = true,
                totalQuizzesCompleted = 0,
                totalQuestionsAnswered = 0,
                totalCorrect = 0,
                totalIncorrect = 0,
                dailyFreeQuizzesUsed = 0,
                bonusAttemptsRemaining = 0,
                isPremium = false,
                selectedTier = "FREE"
            )
            database.userStatsDao().insertOrUpdateStats(initial)
            return initial
        }
    }

    suspend fun addXp(amount: Int): UserStatsEntity {
        val current = getOrCreateUserStats()
        val updated = current.copy(totalXp = current.totalXp + amount)
        database.userStatsDao().insertOrUpdateStats(updated)
        return updated
    }

    suspend fun toggleSoundEffects(enabled: Boolean) {
        val current = getOrCreateUserStats()
        val updated = current.copy(soundEffectsEnabled = enabled)
        database.userStatsDao().insertOrUpdateStats(updated)
    }

    suspend fun recordQuestionAnswered(isCorrect: Boolean, question: QuizQuestion, currentCombo: Int): List<BadgeItem> {
        val newlyUnlocked = mutableListOf<BadgeItem>()
        val current = getOrCreateUserStats()
        val today = dateFormat.format(Date())

        val newCompletedToday = current.dailyQuestionsCompletedToday + 1
        val newHighestCombo = maxOf(current.highestCombo, currentCombo)
        val newStreak = calculateUpdatedStreak(current.lastActiveDate, current.dailyStreak, isActionToday = true)

        var badgesSet = current.unlockedBadges.split(",").filter { it.isNotBlank() }.toMutableSet()

        // Check Daily Target Crusher badge
        if (newCompletedToday >= current.dailyQuestionsTarget && !badgesSet.contains("DAILY_CRUSHER")) {
            badgesSet.add("DAILY_CRUSHER")
            PolityBadges.getBadge("DAILY_CRUSHER")?.let { newlyUnlocked.add(it) }
        }

        // Check Combo Master badge
        if (currentCombo >= 5 && !badgesSet.contains("COMBO_MASTER")) {
            badgesSet.add("COMBO_MASTER")
            PolityBadges.getBadge("COMBO_MASTER")?.let { newlyUnlocked.add(it) }
        }

        // Streak milestones
        if (newStreak >= 7 && !badgesSet.contains("STREAK_7")) {
            badgesSet.add("STREAK_7")
            PolityBadges.getBadge("STREAK_7")?.let { newlyUnlocked.add(it) }
        }
        if (newStreak >= 30 && !badgesSet.contains("STREAK_30")) {
            badgesSet.add("STREAK_30")
            PolityBadges.getBadge("STREAK_30")?.let { newlyUnlocked.add(it) }
        }

        val updated = current.copy(
            dailyQuestionsCompletedToday = newCompletedToday,
            highestCombo = newHighestCombo,
            dailyStreak = newStreak,
            unlockedBadges = badgesSet.joinToString(","),
            lastActiveDate = today
        )
        database.userStatsDao().insertOrUpdateStats(updated)

        // If incorrect, add to Spaced Repetition queue for 2-day review
        if (!isCorrect) {
            val existingSrs = database.spacedRepetitionDao().getQuestionById(question.id)
            val reviewDelay = 2 * 24 * 60 * 60 * 1000L // 2 days in ms
            val srsEntity = SpacedRepetitionQuestionEntity(
                questionId = question.id,
                questionText = question.questionText,
                optionA = question.options.getOrElse(0) { "" },
                optionB = question.options.getOrElse(1) { "" },
                optionC = question.options.getOrElse(2) { "" },
                optionD = question.options.getOrElse(3) { "" },
                correctIndex = question.correctOptionIndex,
                explanation = question.shortExplanation,
                articleReference = question.articleReference,
                categoryId = question.categoryId,
                difficulty = question.difficulty.name,
                yearTag = question.yearTag,
                repetitionLevel = 1,
                incorrectCount = (existingSrs?.incorrectCount ?: 0) + 1,
                nextReviewTimestamp = System.currentTimeMillis() + reviewDelay,
                lastReviewedTimestamp = System.currentTimeMillis()
            )
            database.spacedRepetitionDao().insertOrUpdate(srsEntity)
        }

        return newlyUnlocked
    }

    suspend fun handleSrsReviewResult(questionId: String, isCorrect: Boolean) {
        val existing = database.spacedRepetitionDao().getQuestionById(questionId) ?: return
        if (isCorrect) {
            val newLevel = existing.repetitionLevel + 1
            if (newLevel >= 3) {
                // Mastered & Graduated from SRS
                database.spacedRepetitionDao().removeQuestion(questionId)
            } else {
                val delayDays = if (newLevel == 2) 4 else 7
                val nextTime = System.currentTimeMillis() + (delayDays * 24 * 60 * 60 * 1000L)
                val updated = existing.copy(
                    repetitionLevel = newLevel,
                    nextReviewTimestamp = nextTime,
                    lastReviewedTimestamp = System.currentTimeMillis()
                )
                database.spacedRepetitionDao().insertOrUpdate(updated)
            }
        } else {
            // Reset to level 1 (review in 2 days)
            val nextTime = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000L)
            val updated = existing.copy(
                repetitionLevel = 1,
                incorrectCount = existing.incorrectCount + 1,
                nextReviewTimestamp = nextTime,
                lastReviewedTimestamp = System.currentTimeMillis()
            )
            database.spacedRepetitionDao().insertOrUpdate(updated)
        }
    }

    suspend fun recordQuizAttempt(summary: QuizSummary): Pair<Long, List<BadgeItem>> {
        val entity = QuizAttemptEntity(
            quizTitle = summary.quizTitle,
            categoryId = summary.categoryId,
            mode = summary.mode.name,
            difficulty = summary.difficulty.name,
            totalQuestions = summary.totalQuestions,
            correctCount = summary.correctCount,
            incorrectCount = summary.incorrectCount,
            skippedCount = summary.skippedCount,
            score = summary.rawScore,
            maxMarks = summary.maxMarks,
            accuracy = summary.accuracyPercentage,
            durationSeconds = summary.timeSpentSeconds
        )

        val id = database.quizAttemptDao().insertAttempt(entity)

        val currentStats = getOrCreateUserStats()
        val today = dateFormat.format(Date())
        val newStreak = calculateUpdatedStreak(currentStats.lastActiveDate, currentStats.dailyStreak, isActionToday = true)

        // Calculate XP bonus for quiz completion
        val completionXp = when {
            summary.percentage >= 90.0 -> 200
            summary.percentage >= 70.0 -> 100
            summary.percentage >= 40.0 -> 50
            else -> 20
        }

        val newlyUnlocked = mutableListOf<BadgeItem>()
        var badgesSet = currentStats.unlockedBadges.split(",").filter { it.isNotBlank() }.toMutableSet()

        // Score based Badges
        if (summary.percentage >= 40.0 && !badgesSet.contains("BRONZE_STARTER")) {
            badgesSet.add("BRONZE_STARTER")
            PolityBadges.getBadge("BRONZE_STARTER")?.let { newlyUnlocked.add(it) }
        }
        if (summary.percentage >= 70.0 && !badgesSet.contains("SILVER_SCHOLAR")) {
            badgesSet.add("SILVER_SCHOLAR")
            PolityBadges.getBadge("SILVER_SCHOLAR")?.let { newlyUnlocked.add(it) }
        }
        if (summary.percentage >= 90.0 && !badgesSet.contains("GOLD_CONQUEROR")) {
            badgesSet.add("GOLD_CONQUEROR")
            PolityBadges.getBadge("GOLD_CONQUEROR")?.let { newlyUnlocked.add(it) }
        }

        // Streak based Badges
        if (newStreak >= 7 && !badgesSet.contains("STREAK_7")) {
            badgesSet.add("STREAK_7")
            PolityBadges.getBadge("STREAK_7")?.let { newlyUnlocked.add(it) }
        }
        if (newStreak >= 30 && !badgesSet.contains("STREAK_30")) {
            badgesSet.add("STREAK_30")
            PolityBadges.getBadge("STREAK_30")?.let { newlyUnlocked.add(it) }
        }

        // PYQ Badge
        if (summary.mode == QuizMode.PYQ_PACK && !badgesSet.contains("PYQ_CONQUEROR")) {
            badgesSet.add("PYQ_CONQUEROR")
            PolityBadges.getBadge("PYQ_CONQUEROR")?.let { newlyUnlocked.add(it) }
        }

        val updatedStats = currentStats.copy(
            dailyStreak = newStreak,
            lastActiveDate = today,
            totalXp = currentStats.totalXp + completionXp,
            unlockedBadges = badgesSet.joinToString(","),
            totalQuizzesCompleted = currentStats.totalQuizzesCompleted + 1,
            totalQuestionsAnswered = currentStats.totalQuestionsAnswered + summary.attemptedCount,
            totalCorrect = currentStats.totalCorrect + summary.correctCount,
            totalIncorrect = currentStats.totalIncorrect + summary.incorrectCount,
            dailyFreeQuizzesUsed = currentStats.dailyFreeQuizzesUsed + 1
        )
        database.userStatsDao().insertOrUpdateStats(updatedStats)

        return Pair(id, newlyUnlocked)
    }

    suspend fun incrementFreeQuizzesUsed() {
        val current = getOrCreateUserStats()
        database.userStatsDao().insertOrUpdateStats(
            current.copy(dailyFreeQuizzesUsed = current.dailyFreeQuizzesUsed + 1)
        )
    }

    suspend fun addBonusAttempts(amount: Int) {
        val current = getOrCreateUserStats()
        database.userStatsDao().insertOrUpdateStats(
            current.copy(bonusAttemptsRemaining = current.bonusAttemptsRemaining + amount)
        )
    }

    suspend fun grantBonusAttempts(amount: Int) {
        addBonusAttempts(amount)
    }

    suspend fun consumeBonusAttempt() {
        val current = getOrCreateUserStats()
        if (current.bonusAttemptsRemaining > 0) {
            database.userStatsDao().insertOrUpdateStats(
                current.copy(bonusAttemptsRemaining = current.bonusAttemptsRemaining - 1)
            )
        }
    }

    suspend fun updateSubscription(isPremium: Boolean, tierName: String) {
        val current = getOrCreateUserStats()
        database.userStatsDao().insertOrUpdateStats(
            current.copy(isPremium = isPremium, selectedTier = tierName)
        )
    }

    suspend fun toggleBookmark(question: QuizQuestion) {
        val isBookmarked = database.bookmarkDao().isBookmarked(question.id)
        if (isBookmarked) {
            database.bookmarkDao().removeBookmark(question.id)
        } else {
            database.bookmarkDao().insertBookmark(
                BookmarkedQuestionEntity(
                    questionId = question.id,
                    questionText = question.questionText,
                    optionA = question.options.getOrElse(0) { "" },
                    optionB = question.options.getOrElse(1) { "" },
                    optionC = question.options.getOrElse(2) { "" },
                    optionD = question.options.getOrElse(3) { "" },
                    correctIndex = question.correctOptionIndex,
                    explanation = question.shortExplanation,
                    articleReference = question.articleReference,
                    categoryId = question.categoryId,
                    difficulty = question.difficulty.name,
                    yearTag = question.yearTag
                )
            )
        }
    }

    suspend fun generateQuestions(
        categoryId: String,
        customTopic: String? = null,
        difficulty: DifficultyLevel = DifficultyLevel.UPSC_STANDARD,
        count: Int = 5,
        useAi: Boolean = true
    ): List<QuizQuestion> {
        val category = PolityCategories.getCategoryById(categoryId) ?: PolityCategories.ALL.first()
        if (useAi) {
            val result = GeminiPolityService.generateQuestions(
                categoryTitle = category.title,
                categoryId = category.id,
                customTopicPrompt = customTopic,
                difficulty = difficulty,
                count = count
            )
            if (result.isSuccess) {
                val questions = result.getOrNull()
                if (!questions.isNullOrEmpty()) {
                    return questions
                }
            }
        }
        return QuestionBank.getQuestionsForCategory(categoryId).take(count)
    }

    fun getQuestionsForCategory(categoryId: String): List<QuizQuestion> {
        return QuestionBank.getQuestionsForCategory(categoryId)
    }

    fun getQuestionsForCategory(categoryId: String, difficulty: DifficultyLevel, count: Int): List<QuizQuestion> {
        return QuestionBank.getQuestionsForCategory(categoryId).take(count)
    }

    fun getPyqQuestions(): List<QuizQuestion> {
        return QuestionBank.getPyqPackQuestions()
    }

    suspend fun clearHistory() {
        database.quizAttemptDao().clearHistory()
    }
}
