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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.BookmarkedQuestionEntity
import com.example.model.DifficultyLevel
import com.example.model.QuizQuestion
import com.example.ui.components.PolityTopAppBar
import com.example.ui.components.SourcesVerifiedBadge
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun BookmarksScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            PolityTopAppBar(
                title = "Saved Constitutional Questions",
                subtitle = "${bookmarks.size} Questions Bookmarked",
                showBackButton = true,
                onBackClick = { viewModel.navigateTo(ScreenDestination.Home) },
                userStats = userStats,
                onStreakClick = { viewModel.openStreakDialog() },
                onPremiumClick = { viewModel.openSubscriptionModal() }
            )
        }
    ) { innerPadding ->
        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmarks,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Bookmarks Saved Yet",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tap the bookmark icon during any quiz or solution review to save high-yield questions for quick revision.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(bookmarks) { item ->
                    BookmarkQuestionCard(
                        item = item,
                        onRemove = {
                            val dummyQ = QuizQuestion(
                                id = item.questionId,
                                questionText = item.questionText,
                                options = listOf(item.optionA, item.optionB, item.optionC, item.optionD),
                                correctOptionIndex = item.correctIndex,
                                shortExplanation = item.explanation,
                                articleReference = item.articleReference,
                                categoryId = item.categoryId,
                                difficulty = DifficultyLevel.UPSC_STANDARD
                            )
                            viewModel.toggleBookmark(dummyQ)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun BookmarkQuestionCard(
    item: BookmarkedQuestionEntity,
    onRemove: () -> Unit
) {
    val options = listOf(item.optionA, item.optionB, item.optionC, item.optionD)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bookmark_item_${item.questionId}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.yearTag != null) {
                    Surface(
                        color = SaffronSecondaryLight.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = item.yearTag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SaffronSecondaryLight,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkRemove,
                        contentDescription = "Remove Bookmark",
                        tint = SaffronSecondaryLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.questionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Correct option highlight
            val correctOptionText = options.getOrElse(item.correctIndex) { "" }
            val correctLetter = when (item.correctIndex) {
                0 -> "A"
                1 -> "B"
                2 -> "C"
                3 -> "D"
                else -> "A"
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = ColorCorrect.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ColorCorrect.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Correct Answer ($correctLetter):",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ColorCorrect
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = correctOptionText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            SourcesVerifiedBadge(articleReference = item.articleReference)

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.explanation,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            )
        }
    }
}
