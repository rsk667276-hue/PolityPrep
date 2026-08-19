package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.model.DifficultyLevel
import com.example.model.PolityCategories
import com.example.model.PolityCategory
import com.example.model.QuizMode
import com.example.ui.components.PolityTopAppBar
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorGold
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiGeneratorScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val aiState by viewModel.aiGeneratorState.collectAsStateWithLifecycle()
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            PolityTopAppBar(
                title = "AI Quiz Generator",
                subtitle = "UPSC/SSC Expert Constitutional Question Setter",
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
            // Header Info Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorAshokaNavy),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ColorGold,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Gemini 3.5 Flash Constitutional AI",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Generates high-yield MCQs with exact Articles, 1st-106th CAA, and UPSC standard statements.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            // Step 1: Select Syllabus Category
            item {
                Text(
                    text = "1. Select Constitution Category",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PolityCategories.ALL.forEach { cat ->
                        val isSelected = aiState.selectedCategory.id == cat.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setAiCategory(cat) },
                            label = {
                                Text(
                                    text = cat.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Step 2: Custom Sub-topic or Case law focus
            item {
                Text(
                    text = "2. Specific Topic / Judgement Focus (Optional)",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = aiState.customTopic,
                    onValueChange = { viewModel.setAiCustomTopic(it) },
                    placeholder = {
                        Text(
                            text = "e.g. Pardoning powers under Art 72 vs 161, Defection Law, Electoral Bonds",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_custom_topic_input"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Quick Topic Suggestions Chips
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = SaffronSecondaryLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "High-Yield Suggestions:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SaffronSecondaryLight,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val suggestions = listOf(
                        "Money Bills vs Financial Bills",
                        "Right to Privacy Art 21",
                        "Kesavananda Bharati Basic Structure",
                        "Anti-Defection 10th Schedule",
                        "Governor's Discretionary Powers"
                    )
                    suggestions.forEach { suggestion ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setAiCustomTopic(suggestion) }
                        ) {
                            Text(
                                text = "+ $suggestion",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Step 3: Difficulty Level
            item {
                Text(
                    text = "3. Exam Difficulty Standard",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DifficultyLevel.values().forEach { diff ->
                        val isSelected = aiState.difficulty == diff
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setAiDifficulty(diff) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (diff) {
                                        DifficultyLevel.FOUNDATION -> GreenTertiaryLight.copy(alpha = 0.15f)
                                        DifficultyLevel.UPSC_STANDARD -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        DifficultyLevel.UPSC_ADVANCED -> SaffronSecondaryLight.copy(alpha = 0.15f)
                                    }
                                ) {
                                    Text(
                                        text = diff.badge,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = when (diff) {
                                                DifficultyLevel.FOUNDATION -> GreenTertiaryLight
                                                DifficultyLevel.UPSC_STANDARD -> MaterialTheme.colorScheme.primary
                                                DifficultyLevel.UPSC_ADVANCED -> SaffronSecondaryLight
                                            }
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = diff.label,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = diff.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step 4: Question Count & Quiz Mode
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Question Count
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "4. Question Count",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5, 10, 15).forEach { count ->
                                val isSelected = aiState.questionCount == count
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.setAiQuestionCount(count) },
                                    label = { Text("$count Qs") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            }
                        }
                    }

                    // Mode Selection
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "5. Mode",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = aiState.mode == QuizMode.EXAM_MODE,
                                onClick = { viewModel.setAiQuizMode(QuizMode.EXAM_MODE) },
                                label = { Text("Exam (-0.66)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            )
                            FilterChip(
                                selected = aiState.mode == QuizMode.PRACTICE_MODE,
                                onClick = { viewModel.setAiQuizMode(QuizMode.PRACTICE_MODE) },
                                label = { Text("Learning") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Error Message (if any)
            item {
                AnimatedVisibility(visible = aiState.errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = aiState.errorMessage ?: "",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            )
                        }
                    }
                }
            }

            // Action Buttons
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Button(
                        onClick = { viewModel.generateAndStartQuiz(useAi = true) },
                        enabled = !aiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("generate_ai_quiz_button")
                    ) {
                        if (aiState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Setting UPSC Questions via Gemini AI...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ColorGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate & Start Quiz (${aiState.questionCount} Questions)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.generateAndStartQuiz(useAi = false) },
                        enabled = !aiState.isLoading,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("start_offline_curated_quiz_button")
                    ) {
                        Text(
                            text = "Start Instant Offline Curated Quiz",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}
