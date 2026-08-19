package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.BadgeUnlockedModal
import com.example.ui.components.RewardClaimedDialog
import com.example.ui.components.RewardedAdPlayerDialog
import com.example.ui.components.StreakDetailDialog
import com.example.ui.components.SubscriptionUpgradeModal
import com.example.ui.screens.AiGeneratorScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.BadgesScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.BooksAffiliateScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.QuizPlayScreen
import com.example.ui.screens.QuizResultScreen
import com.example.ui.screens.SpacedRepetitionScreen
import com.example.ui.screens.WeeklyReportScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {

    private val viewModel: PolityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PolityApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun PolityApp(viewModel: PolityViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isRewardedAdPlaying by viewModel.isRewardedAdPlaying.collectAsStateWithLifecycle()
    val rewardedCountdown by viewModel.rewardedAdCountdown.collectAsStateWithLifecycle()
    val showRewardDialog by viewModel.showRewardGrantedDialog.collectAsStateWithLifecycle()
    val showSubscriptionModal by viewModel.showSubscriptionModal.collectAsStateWithLifecycle()
    val showStreakDialog by viewModel.showStreakDialog.collectAsStateWithLifecycle()
    val unlockedBadgeModal by viewModel.unlockedBadgeModal.collectAsStateWithLifecycle()
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()

    // Handle system back navigation
    BackHandler(enabled = currentScreen !is ScreenDestination.Home) {
        when (currentScreen) {
            is ScreenDestination.QuizPlay -> {
                // Handled inside QuizPlayScreen via quit dialog
                viewModel.navigateTo(ScreenDestination.Home)
            }
            else -> {
                viewModel.navigateTo(ScreenDestination.Home)
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition"
        ) { destination ->
            when (destination) {
                is ScreenDestination.Home -> HomeScreen(viewModel = viewModel)
                is ScreenDestination.AiGenerator -> AiGeneratorScreen(viewModel = viewModel)
                is ScreenDestination.QuizPlay -> QuizPlayScreen(viewModel = viewModel)
                is ScreenDestination.QuizResult -> QuizResultScreen(viewModel = viewModel)
                is ScreenDestination.Analytics -> AnalyticsScreen(viewModel = viewModel)
                is ScreenDestination.Bookmarks -> BookmarksScreen(viewModel = viewModel)
                is ScreenDestination.BooksAffiliate -> BooksAffiliateScreen(viewModel = viewModel)
                is ScreenDestination.PyqPack -> QuizPlayScreen(viewModel = viewModel)
                is ScreenDestination.Leaderboard -> LeaderboardScreen(viewModel = viewModel)
                is ScreenDestination.SpacedRepetition -> SpacedRepetitionScreen(viewModel = viewModel)
                is ScreenDestination.Badges -> BadgesScreen(viewModel = viewModel)
                is ScreenDestination.WeeklyReport -> WeeklyReportScreen(viewModel = viewModel)
            }
        }

        // Overlay: Rewarded Ad Player
        if (isRewardedAdPlaying) {
            RewardedAdPlayerDialog(
                countdownSeconds = rewardedCountdown,
                onFinished = { /* Automatically handled in ViewModel */ }
            )
        }

        // Overlay: Reward Claimed Notification Dialog
        if (showRewardDialog) {
            RewardClaimedDialog(
                onDismiss = { viewModel.dismissRewardDialog() }
            )
        }

        // Overlay: Subscription Modal
        if (showSubscriptionModal) {
            SubscriptionUpgradeModal(
                isCurrentPremium = userStats?.isPremium == true,
                onUpgrade = { tier -> viewModel.upgradeSubscription(tier) },
                onDismiss = { viewModel.dismissSubscriptionModal() }
            )
        }

        // Overlay: Badge Unlocked Modal
        if (unlockedBadgeModal != null) {
            BadgeUnlockedModal(
                badge = unlockedBadgeModal!!,
                onDismiss = { viewModel.dismissBadgeModal() }
            )
        }

        // Overlay: Daily Streak Detail Modal
        if (showStreakDialog) {
            val streak = userStats?.dailyStreak ?: 1
            val completed = userStats?.dailyQuestionsCompletedToday ?: 0
            val target = userStats?.dailyQuestionsTarget ?: 10
            StreakDetailDialog(
                streakCount = streak,
                dailyTargetCompleted = completed,
                dailyTarget = target,
                onDismiss = { viewModel.dismissStreakDialog() }
            )
        }
    }
}
