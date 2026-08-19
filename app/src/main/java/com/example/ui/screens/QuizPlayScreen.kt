package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.QuizMode
import com.example.model.QuizQuestion
import com.example.ui.components.ConfettiBurst
import com.example.ui.components.FloatingXpBadge
import com.example.ui.components.SourcesVerifiedBadge
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorGold
import com.example.ui.theme.ColorIncorrect
import com.example.ui.theme.ColorReview
import com.example.ui.theme.ColorWarning
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val activeState by viewModel.activeQuizState.collectAsStateWithLifecycle()
    val bookmarkedIds by viewModel.bookmarkedIds.collectAsStateWithLifecycle()
    val floatingXp by viewModel.floatingXp.collectAsStateWithLifecycle()
    val confettiTrigger by viewModel.confettiTrigger.collectAsStateWithLifecycle()

    var showSubmitConfirmationDialog by remember { mutableStateOf(false) }
    var showOmrSheet by remember { mutableStateOf(false) }
    var showQuitConfirmationDialog by remember { mutableStateOf(false) }

    val currentQuestion = activeState.questions.getOrNull(activeState.currentQuestionIndex)
    val totalQuestions = activeState.questions.size
    val currentResponse = currentQuestion?.let { activeState.userResponses[it.id] }
    val isBookmarked = currentQuestion?.let { bookmarkedIds.contains(it.id) } == true

    val isPracticeMode = activeState.mode == QuizMode.PRACTICE_MODE

    // Timer formatting
    val minutes = activeState.timeRemainingSeconds / 60
    val seconds = activeState.timeRemainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val isTimeLow = activeState.timeRemainingSeconds < 60

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = activeState.quizTitle,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1
                            )
                            Text(
                                text = "Question ${activeState.currentQuestionIndex + 1} of $totalQuestions",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { showQuitConfirmationDialog = true },
                            modifier = Modifier.testTag("quiz_quit_button")
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Quit Quiz")
                        }
                    },
                    actions = {
                        // Combo Indicator Pill in Top Bar
                        if (activeState.currentComboStreak >= 2) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = SaffronSecondaryLight,
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = "Combo Streak",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${activeState.currentComboStreak}x",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }

                        // Timer Badge (in Exam / Daily mode)
                        if (activeState.mode != QuizMode.PRACTICE_MODE) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isTimeLow) ColorIncorrect.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = if (isTimeLow) ColorIncorrect else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = formattedTime,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isTimeLow) ColorIncorrect else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }

                        // OMR Grid Sheet Button
                        IconButton(onClick = { showOmrSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.GridOn,
                                contentDescription = "Question Grid",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Bookmark Button
                        if (currentQuestion != null) {
                            IconButton(onClick = { viewModel.toggleBookmark(currentQuestion) }) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) SaffronSecondaryLight else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                // Action Navigation Bottom Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        OutlinedButton(
                            onClick = { viewModel.previousQuestion() },
                            enabled = activeState.currentQuestionIndex > 0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("quiz_prev_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Prev")
                        }

                        // Mark for Review Flag Button
                        if (currentQuestion != null && !isPracticeMode) {
                            val isMarked = currentResponse?.isMarkedForReview == true
                            OutlinedButton(
                                onClick = { viewModel.toggleMarkForReview(currentQuestion.id) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (isMarked) ColorReview else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isMarked) ColorReview else MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier.testTag("quiz_mark_review_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Flag,
                                    contentDescription = null,
                                    tint = if (isMarked) ColorReview else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isMarked) "Marked" else "Review",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isMarked) ColorReview else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        // Next or Submit Button
                        if (activeState.currentQuestionIndex < totalQuestions - 1) {
                            Button(
                                onClick = { viewModel.nextQuestion() },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.testTag("quiz_next_button")
                            ) {
                                Text("Next")
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        } else {
                            Button(
                                onClick = { showSubmitConfirmationDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenTertiaryLight),
                                modifier = Modifier.testTag("quiz_submit_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Submit Test")
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            if (currentQuestion == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No questions loaded.")
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
                    // Progress Bar
                    item {
                        LinearProgressIndicator(
                            progress = { (activeState.currentQuestionIndex + 1).toFloat() / totalQuestions },
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .padding(top = 4.dp)
                        )
                    }

                    // Combo Streak Banner (if 3+ streak)
                    if (activeState.currentComboStreak >= 3) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SaffronSecondaryLight.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondaryLight.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocalFireDepartment,
                                        contentDescription = null,
                                        tint = SaffronSecondaryLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "🔥 ${activeState.currentComboStreak}x COMBO STREAK! Bonus XP Active!",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = SaffronSecondaryLight,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Question Card
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("question_card_${currentQuestion.id}")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Q${activeState.currentQuestionIndex + 1}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            ),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (currentQuestion.yearTag != null) {
                                            Surface(
                                                color = SaffronSecondaryLight.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.padding(end = 6.dp)
                                            ) {
                                                Text(
                                                    text = currentQuestion.yearTag,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 10.sp,
                                                        color = SaffronSecondaryLight
                                                    ),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = currentQuestion.difficulty.badge,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                ),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = currentQuestion.questionText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        lineHeight = 24.sp
                                    )
                                )
                            }
                        }
                    }

                    // Options (A, B, C, D)
                    items(currentQuestion.options.size) { optionIdx ->
                        val optionLabel = when (optionIdx) {
                            0 -> "A"
                            1 -> "B"
                            2 -> "C"
                            3 -> "D"
                            else -> "${optionIdx + 1}"
                        }
                        val optionText = currentQuestion.options[optionIdx]
                        val isSelected = currentResponse?.selectedOptionIndex == optionIdx
                        val isAnswered = currentResponse?.selectedOptionIndex != null
                        val isCorrectOption = optionIdx == currentQuestion.correctOptionIndex

                        // Option color logic & glowing effect
                        val (cardBg, borderColor, textColor) = when {
                            isPracticeMode && isAnswered -> {
                                when {
                                    isCorrectOption -> {
                                        Triple(
                                            ColorCorrect.copy(alpha = 0.18f),
                                            ColorCorrect,
                                            ColorCorrect
                                        )
                                    }
                                    isSelected && !isCorrectOption -> {
                                        Triple(
                                            ColorIncorrect.copy(alpha = 0.18f),
                                            ColorIncorrect,
                                            ColorIncorrect
                                        )
                                    }
                                    else -> Triple(
                                        MaterialTheme.colorScheme.surface,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                        MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            isSelected -> {
                                Triple(
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary
                                )
                            }
                            else -> {
                                Triple(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected || (isPracticeMode && isCorrectOption)) 2.dp else 1.dp,
                                borderColor
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.selectOption(currentQuestion.id, optionIdx) }
                                .testTag("option_${optionLabel}_${currentQuestion.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                        )
                                ) {
                                    Text(
                                        text = optionLabel,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Instant Practice Mode Explanation Reveal Card
                    if (isPracticeMode && currentResponse?.selectedOptionIndex != null) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .testTag("practice_explanation_card")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val isCorrect = currentResponse.selectedOptionIndex == currentQuestion.correctOptionIndex
                                        Surface(
                                            color = if (isCorrect) ColorCorrect.copy(alpha = 0.15f) else ColorIncorrect.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = if (isCorrect) "✓ Correct Answer" else "✗ Incorrect Option",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (isCorrect) ColorCorrect else ColorIncorrect,
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }

                                        SourcesVerifiedBadge(articleReference = currentQuestion.articleReference)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Official Constitutional Rationale:",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = currentQuestion.shortExplanation,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 17.sp
                                        ),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // Floating XP Popup & Confetti Layer
        ConfettiBurst(trigger = confettiTrigger)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            FloatingXpBadge(
                text = floatingXp.text,
                visible = floatingXp.isVisible,
                isCombo = floatingXp.isCombo
            )
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmationDialog) {
        val attempted = activeState.userResponses.values.count { it.selectedOptionIndex != null }
        val unattempted = totalQuestions - attempted

        AlertDialog(
            onDismissRequest = { showSubmitConfirmationDialog = false },
            title = {
                Text(text = "Submit UPSC Exam?")
            },
            text = {
                Column {
                    Text(text = "You have attempted $attempted of $totalQuestions questions.")
                    if (unattempted > 0) {
                        Text(
                            text = "$unattempted questions are still unattempted.",
                            color = ColorWarning,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Text(
                        text = "Standard UPSC Prelims scoring will apply (+2.00 / -0.66 marks).",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmationDialog = false
                        viewModel.submitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenTertiaryLight),
                    modifier = Modifier.testTag("confirm_submit_button")
                ) {
                    Text("Confirm Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmationDialog = false }) {
                    Text("Continue Test")
                }
            }
        )
    }

    // Quit Confirmation Dialog
    if (showQuitConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showQuitConfirmationDialog = false },
            title = { Text("Leave Quiz?") },
            text = { Text("Your progress on this quiz attempt will not be saved.") },
            confirmButton = {
                Button(
                    onClick = {
                        showQuitConfirmationDialog = false
                        viewModel.navigateTo(ScreenDestination.Home)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ColorIncorrect)
                ) {
                    Text("Exit Quiz")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuitConfirmationDialog = false }) {
                    Text("Stay")
                }
            }
        )
    }

    // OMR Question Palette Bottom Sheet
    if (showOmrSheet) {
        ModalBottomSheet(
            onDismissRequest = { showOmrSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "OMR Question Palette",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OmrLegendItem(color = ColorCorrect, label = "Attempted")
                    OmrLegendItem(color = ColorReview, label = "Review")
                    OmrLegendItem(color = MaterialTheme.colorScheme.surfaceVariant, label = "Unvisited")
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(260.dp)
                ) {
                    itemsIndexed(activeState.questions) { idx, question ->
                        val resp = activeState.userResponses[question.id]
                        val isCurrent = idx == activeState.currentQuestionIndex
                        val isAttempted = resp?.selectedOptionIndex != null
                        val isMarked = resp?.isMarkedForReview == true

                        val (bg, textColor) = when {
                            isMarked -> Pair(ColorReview, Color.White)
                            isAttempted -> Pair(ColorCorrect, Color.White)
                            else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bg)
                                .border(
                                    if (isCurrent) 2.dp else 0.dp,
                                    if (isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    viewModel.goToQuestion(idx)
                                    showOmrSheet = false
                                }
                                .testTag("omr_item_$idx")
                        ) {
                            Text(
                                text = "${idx + 1}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showOmrSheet = false
                        showSubmitConfirmationDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenTertiaryLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Test Now")
                }
            }
        }
    }
}

@Composable
fun OmrLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}
