package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.SpacedRepetitionQuestionEntity
import com.example.ui.components.PolityTopAppBar
import com.example.ui.components.SourcesVerifiedBadge
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorGold
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun SpacedRepetitionScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val dueQuestions by viewModel.dueSrsQuestions.collectAsStateWithLifecycle()
    val allSrsQuestions by viewModel.allSrsQuestions.collectAsStateWithLifecycle()
    val dueCount by viewModel.dueSrsCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            PolityTopAppBar(
                title = "Spaced Repetition (SRS)",
                subtitle = "Scientific Leitner Error Correction Deck",
                showBackButton = true,
                onBackClick = { viewModel.navigateTo(ScreenDestination.Home) },
                userStats = userStats,
                onStreakClick = { viewModel.openStreakDialog() },
                onPremiumClick = { viewModel.openSubscriptionModal() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(2.dp)) }

            // SRS Explanation Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorAshokaNavy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = SaffronSecondaryLight,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "PROVEN MEMORY RETENTION",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = ColorGold,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Smart Interval Error Recovery",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Questions you get wrong are automatically saved here and scheduled for revision in 2 to 3 days. Clearing a review strengthens synaptic memory pathways for Prelims day.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats counters
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$dueCount",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = ColorGold,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                    Text(
                                        text = "Due Today",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Surface(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${allSrsQuestions.size}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                    Text(
                                        text = "Total Deck Cards",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Start Review Action Button
            item {
                if (allSrsQuestions.isNotEmpty()) {
                    Button(
                        onClick = { viewModel.startSrsRevisionQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("start_srs_review_button")
                    ) {
                        Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (dueQuestions.isNotEmpty()) "Start Flashcard Review (${dueQuestions.size} Due)" else "Practice Entire SRS Deck (${allSrsQuestions.size} Cards)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Text(
                    text = "SRS Active Question Queue",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (allSrsQuestions.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🎉", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Your SRS Deck is Clear!",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Take a full UPSC Mock Quiz. Any questions you miss will be added here automatically for scheduled spaced revision.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            } else {
                items(allSrsQuestions) { srsQuestion ->
                    SrsQuestionCard(srsQuestion = srsQuestion)
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun SrsQuestionCard(srsQuestion: SpacedRepetitionQuestionEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = SaffronSecondaryLight.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "SRS Level ${srsQuestion.repetitionLevel} • Missed ${srsQuestion.incorrectCount}x",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = SaffronSecondaryLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Next: in ${if (srsQuestion.repetitionLevel == 1) "2 days" else if (srsQuestion.repetitionLevel == 2) "4 days" else "7 days"}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = srsQuestion.questionText,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = GreenTertiaryLight.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    val correctText = when (srsQuestion.correctIndex) {
                        0 -> srsQuestion.optionA
                        1 -> srsQuestion.optionB
                        2 -> srsQuestion.optionC
                        else -> srsQuestion.optionD
                    }
                    Text(
                        text = "Correct: $correctText",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GreenTertiaryLight
                        )
                    )
                    Text(
                        text = srsQuestion.explanation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            if (srsQuestion.articleReference.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                SourcesVerifiedBadge(articleReference = srsQuestion.articleReference)
            }
        }
    }
}
