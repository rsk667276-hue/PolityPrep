package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.QuestionBank
import com.example.model.PolityCategories
import com.example.model.PolityCategory
import com.example.model.QuizMode
import com.example.ui.components.AdMobBannerSimulator
import com.example.ui.components.AmbientMeshBackground
import com.example.ui.components.AshokaChakraEmblem
import com.example.ui.components.GlossyButtonStyle
import com.example.ui.components.PolityGlassCard
import com.example.ui.components.PolityGlossyButton
import com.example.ui.components.PolityTopAppBar
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorGold
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun HomeScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val isPremium = userStats?.isPremium == true
    val dailyUsed = userStats?.dailyFreeQuizzesUsed ?: 0
    val bonusAttempts = userStats?.bonusAttemptsRemaining ?: 0
    val freeLimit = 5
    val attemptsLeft = if (isPremium) 999 else (freeLimit - dailyUsed + bonusAttempts).coerceAtLeast(0)

    Scaffold(
        topBar = {
            PolityTopAppBar(
                title = "PolityPrep UPSC",
                subtitle = "Indian Constitution & Governance Quiz",
                showBackButton = false,
                userStats = userStats,
                onStreakClick = { viewModel.openStreakDialog() },
                onPremiumClick = { viewModel.openSubscriptionModal() },
                onXpClick = { viewModel.navigateTo(ScreenDestination.Badges) },
                onToggleSound = { viewModel.toggleSoundEffects() }
            )
        },
        bottomBar = {
            // Bottom Quick Nav Strip
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavAction(
                        icon = Icons.Default.AutoAwesome,
                        label = "AI Quiz",
                        onClick = { viewModel.navigateTo(ScreenDestination.AiGenerator) },
                        testTag = "nav_ai_quiz"
                    )
                    BottomNavAction(
                        icon = Icons.Default.EmojiEvents,
                        label = "Leaderboard",
                        onClick = { viewModel.navigateTo(ScreenDestination.Leaderboard) },
                        testTag = "nav_leaderboard"
                    )
                    BottomNavAction(
                        icon = Icons.Default.Psychology,
                        label = "SRS Deck",
                        onClick = { viewModel.navigateTo(ScreenDestination.SpacedRepetition) },
                        testTag = "nav_srs"
                    )
                    BottomNavAction(
                        icon = Icons.Default.Insights,
                        label = "Report",
                        onClick = { viewModel.navigateTo(ScreenDestination.WeeklyReport) },
                        testTag = "nav_analytics"
                    )
                    BottomNavAction(
                        icon = Icons.Default.MenuBook,
                        label = "Books",
                        onClick = { viewModel.navigateTo(ScreenDestination.BooksAffiliate) },
                        testTag = "nav_books"
                    )
                }
            }
        }
    ) { innerPadding ->
        val dueSrsCount by viewModel.dueSrsCount.collectAsStateWithLifecycle()
        val dailyTarget = userStats?.dailyQuestionsTarget ?: 10
        val completedToday = userStats?.dailyQuestionsCompletedToday ?: 0

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Hero Banner with Sansad Bhavan illustration
            item {
                HeroHeaderCard(
                    isPremium = isPremium,
                    attemptsLeft = attemptsLeft,
                    onStartAiQuiz = { viewModel.navigateTo(ScreenDestination.AiGenerator) },
                    onStartDaily = { viewModel.startDailyChallenge() },
                    onWatchAdForBonus = { viewModel.playRewardedAd() }
                )
            }

            // Daily Target & Gamification Hub Card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("home_daily_goal_card")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = GreenTertiaryLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Aaj ka Target: $dailyTarget Questions",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Text(
                                text = "$completedToday / $dailyTarget Completed",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (completedToday >= dailyTarget) GreenTertiaryLight else MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (completedToday.toFloat() / dailyTarget).coerceIn(0f, 1f) },
                            color = GreenTertiaryLight,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Hub Chips: Spaced Repetition + Leaderboard + Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // SRS Card
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (dueSrsCount > 0) SaffronSecondaryLight.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.navigateTo(ScreenDestination.SpacedRepetition) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "🧠 SRS", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                    Text(
                                        text = if (dueSrsCount > 0) "$dueSrsCount Due!" else "Deck Clean",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (dueSrsCount > 0) SaffronSecondaryLight else GreenTertiaryLight,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.5.sp
                                        )
                                    )
                                }
                            }

                            // Badges Card
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ColorGold.copy(alpha = 0.15f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.navigateTo(ScreenDestination.Badges) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "🎖️ Badges", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                    Text(
                                        text = "Milestones",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = ColorGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.5.sp
                                        )
                                    )
                                }
                            }

                            // AIR Ranks Card
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.navigateTo(ScreenDestination.Leaderboard) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "🏆 Leaderboard", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                    Text(
                                        text = "AIR Ranks",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.5.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Daily Quota / Freemium Status Card
            item {
                FreemiumQuotaCard(
                    isPremium = isPremium,
                    dailyUsed = dailyUsed,
                    freeLimit = freeLimit,
                    bonusAttempts = bonusAttempts,
                    onUpgrade = { viewModel.openSubscriptionModal() },
                    onWatchRewardedAd = { viewModel.playRewardedAd() }
                )
            }

            // PYQ 2018-2024 & UPSC 2026 Special Banner
            item {
                SpecialExamPacksRow(
                    onOpenPyq = { viewModel.startPyqPack() },
                    onOpenBooks = { viewModel.navigateTo(ScreenDestination.BooksAffiliate) }
                )
            }

            // Section Header: Core Syllabus Modules
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Constitutional Syllabus Modules",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = "Standard UPSC & SSC Prelims Topic-wise Tests",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // 10 Core Syllabus Categories
            items(PolityCategories.ALL) { category ->
                CategoryQuizCard(
                    category = category,
                    onStartExam = { viewModel.startCategoryQuiz(category, QuizMode.EXAM_MODE) },
                    onStartPractice = { viewModel.startCategoryQuiz(category, QuizMode.PRACTICE_MODE) },
                    onAiGenerate = {
                        viewModel.setAiCategory(category)
                        viewModel.navigateTo(ScreenDestination.AiGenerator)
                    }
                )
            }

            // Recommended Books Strip
            item {
                RecommendedBooksStrip(
                    onViewAllBooks = { viewModel.navigateTo(ScreenDestination.BooksAffiliate) }
                )
            }

            // Simulated AdMob Banner on free tier
            item {
                AdMobBannerSimulator(
                    isPremium = isPremium,
                    onUpgradeClick = { viewModel.openSubscriptionModal() }
                )
            }
        }
    }
}

@Composable
fun HeroHeaderCard(
    isPremium: Boolean,
    attemptsLeft: Int,
    onStartAiQuiz: () -> Unit,
    onStartDaily: () -> Unit,
    onWatchAdForBonus: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorAshokaNavy),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("hero_header_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = SaffronSecondaryLight,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "UPSC CSE PRELIMS 2026",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Master Indian Polity &\nConstitution with AI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 26.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Articles • Schedules • Landmark SC Judgements • PYQs",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 11.sp
                        )
                    )
                }

                // App Icon / Emblem in hero card
                Image(
                    painter = painterResource(id = R.drawable.polity_app_icon_1787163231566),
                    contentDescription = "Emblem",
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartDaily,
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronSecondaryLight),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_daily_challenge_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Daily 5-Min Quiz",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onStartAiQuiz,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_custom_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ColorGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "AI Generator",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FreemiumQuotaCard(
    isPremium: Boolean,
    dailyUsed: Int,
    freeLimit: Int,
    bonusAttempts: Int,
    onUpgrade: () -> Unit,
    onWatchRewardedAd: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPremium) SaffronSecondaryLight.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPremium) SaffronSecondaryLight.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("freemium_quota_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isPremium) Icons.Default.WorkspacePremium else Icons.Default.Security,
                        contentDescription = null,
                        tint = if (isPremium) SaffronSecondaryLight else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPremium) "PolityPro: Unlimited Quizzes" else "Daily Free Tier Quota",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                if (!isPremium) {
                    Text(
                        text = "${(freeLimit - dailyUsed).coerceAtLeast(0)} / $freeLimit Free Left",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                } else {
                    Surface(
                        color = ColorGold,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "ACTIVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            ),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            if (!isPremium) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (dailyUsed.toFloat() / freeLimit).coerceIn(0f, 1f) },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )

                if (bonusAttempts > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "+$bonusAttempts Bonus rewarded attempts active",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GreenTertiaryLight,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rewarded ad trigger
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GreenTertiaryLight.copy(alpha = 0.12f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onWatchRewardedAd() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VideoLibrary,
                                contentDescription = null,
                                tint = GreenTertiaryLight,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Watch Ad for +3 Attempts",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GreenTertiaryLight
                                )
                            )
                        }
                    }

                    // Upgrade Pill
                    TextButton(
                        onClick = onUpgrade,
                        modifier = Modifier.testTag("upgrade_plan_button")
                    ) {
                        Text(
                            text = "Go Pro ₹99 →",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SaffronSecondaryLight
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpecialExamPacksRow(
    onOpenPyq: () -> Unit,
    onOpenBooks: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // PYQ Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            ),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onOpenPyq() }
                .testTag("pyq_pack_card")
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PYQ Pack",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "2018-2024 Prelims",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Authentic UPSC Questions",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Books & Coaching Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = SaffronSecondaryLight.copy(alpha = 0.12f)
            ),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onOpenBooks() }
                .testTag("books_affiliate_card")
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = SaffronSecondaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "M. Laxmikanth",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Book References",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Chapter-wise Study Map",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}

@Composable
fun CategoryQuizCard(
    category: PolityCategory,
    onStartExam: () -> Unit,
    onStartPractice: () -> Unit,
    onAiGenerate: () -> Unit
) {
    val icon = getCategoryIcon(category.iconName)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .testTag("category_card_${category.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${category.hindiTitle} • ${category.articlesSpan}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Surface(
                    color = GreenTertiaryLight.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = category.weightageTag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GreenTertiaryLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onStartPractice,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Practice",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = onStartExam,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Exam Mode",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SaffronSecondaryLight.copy(alpha = 0.15f),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onAiGenerate() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 9.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Quiz",
                            tint = SaffronSecondaryLight,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "AI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SaffronSecondaryLight
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendedBooksStrip(
    onViewAllBooks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Recommended Standard Textbooks",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Official UPSC Civil Services Polity Syllabi Sources",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            TextButton(onClick = onViewAllBooks) {
                Text(
                    text = "View All →",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(QuestionBank.RECOMMENDED_BOOKS) { book ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .width(220.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onViewAllBooks() }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Surface(
                            color = SaffronSecondaryLight.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = book.badge,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SaffronSecondaryLight,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2
                        )
                        Text(
                            text = book.author,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = book.price,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GreenTertiaryLight
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )
        )
    }
}

fun getCategoryIcon(name: String): ImageVector {
    return when (name) {
        "Balance" -> Icons.Default.Balance
        "Shield" -> Icons.Default.Shield
        "Bookmark" -> Icons.Default.Bookmark
        "AccountBalance" -> Icons.Default.AccountBalance
        "Gavel" -> Icons.Default.Gavel
        "Apartment" -> Icons.Default.Apartment
        "VerifiedUser" -> Icons.Default.VerifiedUser
        "HistoryEdu" -> Icons.Default.HistoryEdu
        "NaturePeople" -> Icons.Default.NaturePeople
        "Newspaper" -> Icons.Default.Newspaper
        else -> Icons.Default.Balance
    }
}
