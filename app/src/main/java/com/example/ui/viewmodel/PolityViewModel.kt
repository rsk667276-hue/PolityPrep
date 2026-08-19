package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QuestionBank
import com.example.data.local.PolityDatabase
import com.example.data.local.entity.BookmarkedQuestionEntity
import com.example.data.local.entity.QuizAttemptEntity
import com.example.data.local.entity.SpacedRepetitionQuestionEntity
import com.example.data.local.entity.UserStatsEntity
import com.example.data.repository.PolityRepository
import com.example.model.BadgeItem
import com.example.model.DifficultyLevel
import com.example.model.LeaderboardData
import com.example.model.LeaderboardUser
import com.example.model.PolityBadges
import com.example.model.PolityCategories
import com.example.model.PolityCategory
import com.example.model.QuestionUserResponse
import com.example.model.QuizMode
import com.example.model.QuizQuestion
import com.example.model.QuizSummary
import com.example.util.SoundEffectHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Home : ScreenDestination()
    object AiGenerator : ScreenDestination()
    object QuizPlay : ScreenDestination()
    object QuizResult : ScreenDestination()
    object Analytics : ScreenDestination()
    object Bookmarks : ScreenDestination()
    object BooksAffiliate : ScreenDestination()
    object PyqPack : ScreenDestination()
    object Leaderboard : ScreenDestination()
    object Badges : ScreenDestination()
    object SpacedRepetition : ScreenDestination()
    object WeeklyReport : ScreenDestination()
}

data class ActiveQuizState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val userResponses: Map<String, QuestionUserResponse> = emptyMap(),
    val mode: QuizMode = QuizMode.EXAM_MODE,
    val difficulty: DifficultyLevel = DifficultyLevel.UPSC_STANDARD,
    val categoryId: String = "preamble_basic_structure",
    val quizTitle: String = "Polity Quiz",
    val timeRemainingSeconds: Int = 300,
    val totalTimeSeconds: Int = 300,
    val isTimerRunning: Boolean = false,
    val isSubmitted: Boolean = false,
    val latestSummary: QuizSummary? = null,
    val currentComboStreak: Int = 0,
    val lastAnsweredCorrectly: Boolean? = null
)

data class AiGeneratorState(
    val selectedCategory: PolityCategory = PolityCategories.ALL.first(),
    val customTopic: String = "",
    val difficulty: DifficultyLevel = DifficultyLevel.UPSC_STANDARD,
    val questionCount: Int = 5,
    val mode: QuizMode = QuizMode.EXAM_MODE,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class FloatingXpState(
    val text: String = "",
    val isVisible: Boolean = false,
    val isCombo: Boolean = false,
    val triggerId: Long = 0L
)

class PolityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PolityRepository = PolityRepository(
        PolityDatabase.getDatabase(application)
    )

    // Navigation Screen
    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // User Stats & Persistence Flows
    val userStats: StateFlow<UserStatsEntity?> = repository.userStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val attemptHistory: StateFlow<List<QuizAttemptEntity>> = repository.allAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkedQuestionEntity>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedIds: StateFlow<List<String>> = repository.bookmarkedIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dueSrsQuestions: StateFlow<List<SpacedRepetitionQuestionEntity>> = repository.getDueSrsQuestions(System.currentTimeMillis())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dueSrsCount: StateFlow<Int> = repository.getDueSrsCount(System.currentTimeMillis())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allSrsQuestions: StateFlow<List<SpacedRepetitionQuestionEntity>> = repository.allSrsQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Quiz State
    private val _activeQuizState = MutableStateFlow(ActiveQuizState())
    val activeQuizState: StateFlow<ActiveQuizState> = _activeQuizState.asStateFlow()

    // AI Generator State
    private val _aiGeneratorState = MutableStateFlow(AiGeneratorState())
    val aiGeneratorState: StateFlow<AiGeneratorState> = _aiGeneratorState.asStateFlow()

    // Gamification & Animations
    private val _confettiTrigger = MutableStateFlow(0L)
    val confettiTrigger: StateFlow<Long> = _confettiTrigger.asStateFlow()

    private val _floatingXp = MutableStateFlow(FloatingXpState())
    val floatingXp: StateFlow<FloatingXpState> = _floatingXp.asStateFlow()

    private val _showOutstandingCelebration = MutableStateFlow(false)
    val showOutstandingCelebration: StateFlow<Boolean> = _showOutstandingCelebration.asStateFlow()

    private val _unlockedBadgeModal = MutableStateFlow<BadgeItem?>(null)
    val unlockedBadgeModal: StateFlow<BadgeItem?> = _unlockedBadgeModal.asStateFlow()

    private val _showStreakDialog = MutableStateFlow(false)
    val showStreakDialog: StateFlow<Boolean> = _showStreakDialog.asStateFlow()

    // Monetization & Rewarded Ad State
    private val _isRewardedAdPlaying = MutableStateFlow(false)
    val isRewardedAdPlaying: StateFlow<Boolean> = _isRewardedAdPlaying.asStateFlow()

    private val _rewardedAdCountdown = MutableStateFlow(5)
    val rewardedAdCountdown: StateFlow<Int> = _rewardedAdCountdown.asStateFlow()

    private val _showRewardGrantedDialog = MutableStateFlow(false)
    val showRewardGrantedDialog: StateFlow<Boolean> = _showRewardGrantedDialog.asStateFlow()

    private val _showSubscriptionModal = MutableStateFlow(false)
    val showSubscriptionModal: StateFlow<Boolean> = _showSubscriptionModal.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getOrCreateUserStats()
        }
    }

    fun openStreakDialog() {
        _showStreakDialog.value = true
    }

    fun dismissStreakDialog() {
        _showStreakDialog.value = false
    }

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    fun toggleSoundEffects() {
        val currentSound = userStats.value?.soundEffectsEnabled ?: true
        viewModelScope.launch {
            repository.toggleSoundEffects(!currentSound)
        }
    }

    fun openSubscriptionModal() {
        _showSubscriptionModal.value = true
    }

    fun dismissSubscriptionModal() {
        _showSubscriptionModal.value = false
    }

    fun dismissOutstandingCelebration() {
        _showOutstandingCelebration.value = false
    }

    fun dismissBadgeModal() {
        _unlockedBadgeModal.value = null
    }

    fun upgradeSubscription(tier: String) {
        viewModelScope.launch {
            repository.updateSubscription(isPremium = true, tierName = tier)
            _showSubscriptionModal.value = false
        }
    }

    fun playRewardedAd() {
        _isRewardedAdPlaying.value = true
        _rewardedAdCountdown.value = 5
        viewModelScope.launch {
            while (_rewardedAdCountdown.value > 0) {
                delay(1000)
                _rewardedAdCountdown.value -= 1
            }
            _isRewardedAdPlaying.value = false
            repository.addBonusAttempts(3)
            _showRewardGrantedDialog.value = true
        }
    }

    fun dismissRewardDialog() {
        _showRewardGrantedDialog.value = false
    }

    // AI Quiz Generator Controls
    fun setAiCategory(category: PolityCategory) {
        _aiGeneratorState.update { it.copy(selectedCategory = category) }
    }

    fun setAiCustomTopic(topic: String) {
        _aiGeneratorState.update { it.copy(customTopic = topic) }
    }

    fun setAiDifficulty(difficulty: DifficultyLevel) {
        _aiGeneratorState.update { it.copy(difficulty = difficulty) }
    }

    fun setAiQuestionCount(count: Int) {
        _aiGeneratorState.update { it.copy(questionCount = count) }
    }

    fun setAiQuizMode(mode: QuizMode) {
        _aiGeneratorState.update { it.copy(mode = mode) }
    }

    fun generateAndStartQuiz(useAi: Boolean = true) {
        val state = _aiGeneratorState.value
        _aiGeneratorState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val questions = repository.generateQuestions(
                    categoryId = state.selectedCategory.id,
                    customTopic = state.customTopic.takeIf { it.isNotBlank() },
                    difficulty = state.difficulty,
                    count = state.questionCount,
                    useAi = useAi
                )

                if (questions.isNotEmpty()) {
                    val title = if (state.customTopic.isNotBlank()) {
                        "UPSC: ${state.customTopic}"
                    } else {
                        state.selectedCategory.title
                    }
                    startQuiz(
                        questions = questions,
                        title = title,
                        categoryId = state.selectedCategory.id,
                        mode = state.mode,
                        difficulty = state.difficulty
                    )
                } else {
                    _aiGeneratorState.update {
                        it.copy(isLoading = false, errorMessage = "Could not generate questions. Please try again.")
                    }
                }
            } catch (e: Exception) {
                _aiGeneratorState.update {
                    it.copy(isLoading = false, errorMessage = "Error: ${e.message}")
                }
            }
        }
    }

    fun startCategoryQuiz(category: PolityCategory, mode: QuizMode = QuizMode.EXAM_MODE) {
        val questions = QuestionBank.getQuestionsForCategory(category.id)
        startQuiz(
            questions = questions,
            title = category.title,
            categoryId = category.id,
            mode = mode,
            difficulty = DifficultyLevel.UPSC_STANDARD
        )
    }

    fun startDailyChallenge() {
        val questions = QuestionBank.getDailyChallengeQuestions()
        startQuiz(
            questions = questions,
            title = "Daily 5-Min UPSC Challenge",
            categoryId = "all_mixed",
            mode = QuizMode.DAILY_CHALLENGE,
            difficulty = DifficultyLevel.UPSC_STANDARD
        )
    }

    fun startPyqPack() {
        val questions = QuestionBank.getPyqPackQuestions()
        startQuiz(
            questions = questions,
            title = "UPSC Prelims PYQs (2018-2024)",
            categoryId = "pyq_pack",
            mode = QuizMode.PYQ_PACK,
            difficulty = DifficultyLevel.UPSC_STANDARD
        )
    }

    fun startSrsRevisionQuiz() {
        viewModelScope.launch {
            val due = repository.getDueSrsQuestions(System.currentTimeMillis()).firstOrNull() ?: emptyList()
            if (due.isNotEmpty()) {
                val questions = due.map { srs ->
                    QuizQuestion(
                        id = srs.questionId,
                        questionText = srs.questionText,
                        options = listOf(srs.optionA, srs.optionB, srs.optionC, srs.optionD),
                        correctOptionIndex = srs.correctIndex,
                        shortExplanation = srs.explanation,
                        articleReference = srs.articleReference,
                        categoryId = srs.categoryId,
                        difficulty = DifficultyLevel.UPSC_STANDARD,
                        yearTag = "SRS Due: Level ${srs.repetitionLevel}"
                    )
                }
                startQuiz(
                    questions = questions,
                    title = "Spaced Repetition Flashcards",
                    categoryId = "srs_revision",
                    mode = QuizMode.PRACTICE_MODE,
                    difficulty = DifficultyLevel.UPSC_STANDARD
                )
            }
        }
    }

    private fun startQuiz(
        questions: List<QuizQuestion>,
        title: String,
        categoryId: String,
        mode: QuizMode,
        difficulty: DifficultyLevel
    ) {
        val initialResponses = questions.associate { q ->
            q.id to QuestionUserResponse(question = q)
        }

        val totalTime = when (mode) {
            QuizMode.EXAM_MODE -> questions.size * 60
            QuizMode.DAILY_CHALLENGE -> 300
            QuizMode.PYQ_PACK -> questions.size * 70
            QuizMode.PRACTICE_MODE -> questions.size * 120
        }

        _activeQuizState.value = ActiveQuizState(
            questions = questions,
            currentQuestionIndex = 0,
            userResponses = initialResponses,
            mode = mode,
            difficulty = difficulty,
            categoryId = categoryId,
            quizTitle = title,
            timeRemainingSeconds = totalTime,
            totalTimeSeconds = totalTime,
            isTimerRunning = true,
            isSubmitted = false,
            latestSummary = null,
            currentComboStreak = 0,
            lastAnsweredCorrectly = null
        )

        _aiGeneratorState.update { it.copy(isLoading = false) }
        _currentScreen.value = ScreenDestination.QuizPlay

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_activeQuizState.value.isTimerRunning && _activeQuizState.value.timeRemainingSeconds > 0) {
                delay(1000)
                _activeQuizState.update {
                    val nextTime = it.timeRemainingSeconds - 1
                    if (nextTime <= 0) {
                        it.copy(timeRemainingSeconds = 0, isTimerRunning = false)
                    } else {
                        it.copy(timeRemainingSeconds = nextTime)
                    }
                }
            }
            if (_activeQuizState.value.timeRemainingSeconds == 0 && !_activeQuizState.value.isSubmitted) {
                submitQuiz()
            }
        }
    }

    fun selectOption(questionId: String, optionIndex: Int) {
        val state = _activeQuizState.value
        val currentMap = state.userResponses.toMutableMap()
        val currentResponse = currentMap[questionId] ?: return
        val question = currentResponse.question

        val isSelected = currentResponse.selectedOptionIndex == optionIndex
        val newSelectedIndex = if (isSelected) null else optionIndex
        val isCorrect = newSelectedIndex == question.correctOptionIndex

        val soundEnabled = userStats.value?.soundEffectsEnabled ?: true

        // Update response
        val updated = currentResponse.copy(
            selectedOptionIndex = newSelectedIndex,
            isVisited = true
        )
        currentMap[questionId] = updated

        if (newSelectedIndex != null) {
            val newCombo = if (isCorrect) state.currentComboStreak + 1 else 0

            // Sound feedback
            if (isCorrect) {
                if (newCombo >= 3) {
                    SoundEffectHelper.playComboSound(newCombo, soundEnabled)
                } else {
                    SoundEffectHelper.playCorrectSound(soundEnabled)
                }
            } else {
                SoundEffectHelper.playWrongSound(soundEnabled)
            }

            // Gamification XP calculation & Floating Popup Animation
            viewModelScope.launch {
                val earnedXp = if (isCorrect) {
                    when {
                        newCombo >= 5 -> 25
                        newCombo >= 3 -> 15
                        else -> 10
                    }
                } else 0

                if (earnedXp > 0) {
                    repository.addXp(earnedXp)
                    val popupText = if (newCombo >= 3) {
                        "+$earnedXp XP 🔥 ${newCombo}x Combo!"
                    } else {
                        "+$earnedXp XP"
                    }
                    _floatingXp.value = FloatingXpState(
                        text = popupText,
                        isVisible = true,
                        isCombo = newCombo >= 3,
                        triggerId = System.currentTimeMillis()
                    )
                    _confettiTrigger.value = System.currentTimeMillis()

                    // Auto dismiss floating XP badge
                    delay(1200)
                    _floatingXp.update { it.copy(isVisible = false) }
                }

                // Record question stats & check badge unlocks
                val newBadges = repository.recordQuestionAnswered(isCorrect, question, newCombo)
                if (newBadges.isNotEmpty()) {
                    _unlockedBadgeModal.value = newBadges.first()
                }

                // If this is an SRS review session, record SRS repetition progress
                if (state.categoryId == "srs_revision") {
                    repository.handleSrsReviewResult(question.id, isCorrect)
                }
            }

            _activeQuizState.update {
                it.copy(
                    userResponses = currentMap,
                    currentComboStreak = newCombo,
                    lastAnsweredCorrectly = isCorrect
                )
            }
        } else {
            _activeQuizState.update { it.copy(userResponses = currentMap) }
        }
    }

    fun toggleMarkForReview(questionId: String) {
        val currentMap = _activeQuizState.value.userResponses.toMutableMap()
        val currentResponse = currentMap[questionId]
        if (currentResponse != null) {
            val updated = currentResponse.copy(
                isMarkedForReview = !currentResponse.isMarkedForReview,
                isVisited = true
            )
            currentMap[questionId] = updated
            _activeQuizState.update { it.copy(userResponses = currentMap) }
        }
    }

    fun goToQuestion(index: Int) {
        if (index in _activeQuizState.value.questions.indices) {
            _activeQuizState.update { it.copy(currentQuestionIndex = index) }
        }
    }

    fun nextQuestion() {
        val currentIndex = _activeQuizState.value.currentQuestionIndex
        if (currentIndex < _activeQuizState.value.questions.size - 1) {
            _activeQuizState.update { it.copy(currentQuestionIndex = currentIndex + 1) }
        }
    }

    fun previousQuestion() {
        val currentIndex = _activeQuizState.value.currentQuestionIndex
        if (currentIndex > 0) {
            _activeQuizState.update { it.copy(currentQuestionIndex = currentIndex - 1) }
        }
    }

    fun submitQuiz() {
        timerJob?.cancel()
        val state = _activeQuizState.value
        if (state.isSubmitted) return

        val total = state.questions.size
        var correct = 0
        var incorrect = 0
        var skipped = 0
        val weakTopics = mutableListOf<String>()

        state.questions.forEach { question ->
            val response = state.userResponses[question.id]
            if (response == null || response.selectedOptionIndex == null) {
                skipped++
            } else if (response.selectedOptionIndex == question.correctOptionIndex) {
                correct++
            } else {
                incorrect++
                val cat = PolityCategories.getCategoryById(question.categoryId)
                weakTopics.add(cat.title)
            }
        }

        val attempted = correct + incorrect
        val rawScore = (correct * 2.0) - (incorrect * 0.66)
        val maxMarks = total * 2.0
        val accuracy = if (attempted > 0) (correct.toDouble() / attempted * 100) else 0.0
        val percentage = if (maxMarks > 0) (rawScore / maxMarks * 100) else 0.0
        val timeSpent = state.totalTimeSeconds - state.timeRemainingSeconds

        val summary = QuizSummary(
            quizTitle = state.quizTitle,
            categoryId = state.categoryId,
            mode = state.mode,
            difficulty = state.difficulty,
            totalQuestions = total,
            attemptedCount = attempted,
            correctCount = correct,
            incorrectCount = incorrect,
            skippedCount = skipped,
            rawScore = (Math.round(rawScore * 100.0) / 100.0),
            maxMarks = maxMarks,
            percentage = (Math.round(percentage * 10.0) / 10.0),
            accuracyPercentage = (Math.round(accuracy * 10.0) / 10.0),
            timeSpentSeconds = timeSpent,
            userResponses = state.questions.map { q ->
                state.userResponses[q.id] ?: QuestionUserResponse(question = q)
            },
            weakTopicsDetected = weakTopics.distinct()
        )

        _activeQuizState.update {
            it.copy(
                isSubmitted = true,
                isTimerRunning = false,
                latestSummary = summary
            )
        }

        val soundEnabled = userStats.value?.soundEffectsEnabled ?: true

        // Outstanding 90%+ score celebration
        if (percentage >= 90.0) {
            _showOutstandingCelebration.value = true
            SoundEffectHelper.playVictoryFanfare(soundEnabled)
            _confettiTrigger.value = System.currentTimeMillis()
        }

        viewModelScope.launch {
            val (attemptId, newBadges) = repository.recordQuizAttempt(summary)
            if (newBadges.isNotEmpty() && !_showOutstandingCelebration.value) {
                _unlockedBadgeModal.value = newBadges.first()
            }
        }

        _currentScreen.value = ScreenDestination.QuizResult
    }

    fun toggleBookmark(question: QuizQuestion) {
        viewModelScope.launch {
            repository.toggleBookmark(question)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
