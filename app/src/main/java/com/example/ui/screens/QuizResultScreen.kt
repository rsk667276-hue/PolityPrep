package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.QuestionUserResponse
import com.example.model.QuizSummary
import com.example.ui.components.AdMobBannerSimulator
import com.example.ui.components.ConfettiBurst
import com.example.ui.components.PolityTopAppBar
import com.example.ui.components.SourcesVerifiedBadge
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorGold
import com.example.ui.theme.ColorIncorrect
import com.example.ui.theme.ColorWarning
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

enum class SolutionFilter { ALL, INCORRECT, CORRECT, SKIPPED }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizResultScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val activeState by viewModel.activeQuizState.collectAsStateWithLifecycle()
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsStateWithLifecycle()
    val confettiTrigger by viewModel.confettiTrigger.collectAsStateWithLifecycle()
    val summary = activeState.latestSummary
    val isPremium = userStats?.isPremium == true

    var selectedFilter by remember { mutableStateOf(SolutionFilter.ALL) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                PolityTopAppBar(
                    title = "UPSC Performance Report",
                    subtitle = summary?.quizTitle ?: "Detailed Analysis",
                    showBackButton = true,
                    onBackClick = { viewModel.navigateTo(ScreenDestination.Home) },
                    userStats = userStats,
                    onStreakClick = { viewModel.openStreakDialog() },
                    onPremiumClick = { viewModel.openSubscriptionModal() },
                    onXpClick = { viewModel.navigateTo(ScreenDestination.Badges) },
                    onToggleSound = { viewModel.toggleSoundEffects() }
                )
            }
        ) { innerPadding ->
            if (summary == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No result data available.")
                }
            } else {
                val filteredResponses = summary.userResponses.filter { resp ->
                    when (selectedFilter) {
                        SolutionFilter.ALL -> true
                        SolutionFilter.INCORRECT -> resp.isAttempted && !resp.isCorrect
                        SolutionFilter.CORRECT -> resp.isCorrect
                        SolutionFilter.SKIPPED -> !resp.isAttempted
                    }
                }

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Score Header Card (UPSC Marks Format)
                    item {
                        ScoreOverviewCard(summary = summary)
                    }

                    // 90%+ Extra Celebration Banner
                    if (summary.accuracyPercentage >= 90) {
                        item {
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = ColorGold.copy(alpha = 0.15f)),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, ColorGold),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "🏆", fontSize = 32.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Outstanding! 🏆 Prelims Cutoff Cleared!",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = ColorGold
                                            )
                                        )
                                        Text(
                                            text = "You scored ${summary.accuracyPercentage}% accuracy. Exceptional constitutional recall matching top AIR ranks.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onSurface,
                                                lineHeight = 15.sp
                                            ),
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Spaced Repetition SRS Card (if incorrect answers exist)
                    if (summary.incorrectCount > 0) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondaryLight.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "🧠 Scheduled for SRS Revision",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "${summary.incorrectCount} missed questions queued for 2-day spaced repetition interval.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 11.sp
                                            ),
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedButton(
                                        onClick = { viewModel.navigateTo(ScreenDestination.SpacedRepetition) },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("SRS Deck", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Weak Topics Alert & Revision Recommendations
                    if (summary.weakTopicsDetected.isNotEmpty()) {
                        item {
                            WeakTopicsCard(
                                weakTopics = summary.weakTopicsDetected,
                                onOpenBooks = { viewModel.navigateTo(ScreenDestination.BooksAffiliate) }
                            )
                        }
                    }

                    // Quick Action Bar
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.navigateTo(ScreenDestination.AiGenerator) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("result_new_ai_quiz_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = ColorGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New AI Quiz")
                            }

                            OutlinedButton(
                                onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("result_home_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Home")
                            }
                        }
                    }

                    // Section: Detailed Solutions
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question-by-Question Solutions",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        // Filter Chips
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = selectedFilter == SolutionFilter.ALL,
                                onClick = { selectedFilter = SolutionFilter.ALL },
                                label = { Text("All (${summary.totalQuestions})") }
                            )
                            FilterChip(
                                selected = selectedFilter == SolutionFilter.INCORRECT,
                                onClick = { selectedFilter = SolutionFilter.INCORRECT },
                                label = { Text("Incorrect (${summary.incorrectCount})") }
                            )
                            FilterChip(
                                selected = selectedFilter == SolutionFilter.CORRECT,
                                onClick = { selectedFilter = SolutionFilter.CORRECT },
                                label = { Text("Correct (${summary.correctCount})") }
                            )
                        }
                    }

                    itemsIndexed(filteredResponses) { index, resp ->
                        val isBookmarked = bookmarkedIds.contains(resp.question.id)
                        SolutionItemCard(
                            index = index + 1,
                            response = resp,
                            isBookmarked = isBookmarked,
                            onToggleBookmark = { viewModel.toggleBookmark(resp.question) }
                        )
                    }

                    // Simulated AdMob banner on free tier
                    item {
                        AdMobBannerSimulator(
                            isPremium = isPremium,
                            onUpgradeClick = { viewModel.openSubscriptionModal() }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

        ConfettiBurst(trigger = confettiTrigger)
    }
}

@Composable
fun ScoreOverviewCard(summary: QuizSummary) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorAshokaNavy),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .testTag("score_overview_card")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "UPSC PRELIMS SCORECARD",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = SaffronSecondaryLight,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Marks Display
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${summary.rawScore}",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = " / ${summary.maxMarks} Marks",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                )
            }

            Text(
                text = "Marking Scheme: +2.00 Correct | -0.66 Incorrect",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Performance Statistics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ResultStatPill(
                    label = "Accuracy",
                    value = "${summary.accuracyPercentage}%",
                    color = if (summary.accuracyPercentage >= 70) ColorCorrect else ColorGold
                )
                ResultStatPill(
                    label = "Correct",
                    value = "${summary.correctCount}",
                    color = ColorCorrect
                )
                ResultStatPill(
                    label = "Negative (-0.66)",
                    value = "-${(summary.incorrectCount * 0.66).let { Math.round(it * 100.0) / 100.0 }}",
                    color = ColorIncorrect
                )
                ResultStatPill(
                    label = "Skipped",
                    value = "${summary.skippedCount}",
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ResultStatPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        )
    }
}

@Composable
fun WeakTopicsCard(
    weakTopics: List<String>,
    onOpenBooks: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorWarning.copy(alpha = 0.1f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, ColorWarning.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ColorWarning,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Weak Areas Detected (${weakTopics.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ColorWarning
                        )
                    )
                }

                OutlinedButton(
                    onClick = onOpenBooks,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Read Laxmikanth", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            weakTopics.forEach { topic ->
                Text(
                    text = "• $topic (Review Articles & Exceptions)",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
fun SolutionItemCard(
    index: Int,
    response: QuestionUserResponse,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    val q = response.question
    val isCorrect = response.isCorrect
    val isAttempted = response.isAttempted

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            when {
                !isAttempted -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                isCorrect -> ColorCorrect.copy(alpha = 0.4f)
                else -> ColorIncorrect.copy(alpha = 0.4f)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("solution_card_${q.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Status bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = when {
                            !isAttempted -> MaterialTheme.colorScheme.surfaceVariant
                            isCorrect -> ColorCorrect.copy(alpha = 0.15f)
                            else -> ColorIncorrect.copy(alpha = 0.15f)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = when {
                                !isAttempted -> "Skipped"
                                isCorrect -> "+2.00 Marks"
                                else -> "-0.66 Penalty"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    !isAttempted -> MaterialTheme.colorScheme.onSurfaceVariant
                                    isCorrect -> ColorCorrect
                                    else -> ColorIncorrect
                                }
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (q.yearTag != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = q.yearTag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) SaffronSecondaryLight else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question Text
            Text(
                text = q.questionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Options summary
            q.options.forEachIndexed { optIdx, optText ->
                val optLetter = when (optIdx) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    3 -> "D"
                    else -> "$optIdx"
                }
                val isThisCorrect = optIdx == q.correctOptionIndex
                val isThisUserChoice = response.selectedOptionIndex == optIdx

                val optBg = when {
                    isThisCorrect -> ColorCorrect.copy(alpha = 0.12f)
                    isThisUserChoice && !isThisCorrect -> ColorIncorrect.copy(alpha = 0.12f)
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = optBg,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        when {
                            isThisCorrect -> ColorCorrect.copy(alpha = 0.4f)
                            isThisUserChoice && !isThisCorrect -> ColorIncorrect.copy(alpha = 0.4f)
                            else -> Color.Transparent
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "($optLetter)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isThisCorrect) ColorCorrect else MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = optText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                fontWeight = if (isThisCorrect || isThisUserChoice) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        if (isThisCorrect) {
                            Text(
                                text = "✓ Correct",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ColorCorrect,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        } else if (isThisUserChoice) {
                            Text(
                                text = "Your choice",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = ColorIncorrect,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sources Verified & Explanation
            SourcesVerifiedBadge(articleReference = q.articleReference)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = q.shortExplanation,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
            )
        }
    }
}
