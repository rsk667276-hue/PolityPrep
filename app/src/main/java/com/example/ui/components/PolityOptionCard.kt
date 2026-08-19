package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorGold
import com.example.ui.theme.ColorIncorrect
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.NavyPrimaryLight
import com.example.ui.theme.SaffronSecondaryLight

enum class OptionCardState {
    DEFAULT,
    SELECTED,
    CORRECT,
    INCORRECT
}

/**
 * High-Tactile 3D Option Card for UPSC Quizzes.
 * Features hover/press slide translation, 3D bottom bevel depth,
 * metallic option badge pill (A, B, C, D), and smooth animated state feedback.
 */
@Composable
fun PolityOptionCard(
    optionIndex: Int,
    optionText: String,
    state: OptionCardState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    testTag: String = "quiz_option_$optionIndex"
) {
    val isDark = isSystemInDarkTheme()
    val optionLabel = when (optionIndex) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        else -> "${optionIndex + 1}"
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Slide animation (translates rightward horizontally on select/press)
    val offsetX by animateDpAsState(
        targetValue = when {
            state == OptionCardState.SELECTED -> 6.dp
            state == OptionCardState.CORRECT -> 4.dp
            state == OptionCardState.INCORRECT -> 2.dp
            isPressed -> 3.dp
            else -> 0.dp
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "OptionSlideOffsetX"
    )

    val elevation by animateDpAsState(
        targetValue = when {
            state == OptionCardState.SELECTED -> 8.dp
            state == OptionCardState.CORRECT -> 8.dp
            state == OptionCardState.INCORRECT -> 4.dp
            isPressed -> 2.dp
            else -> 4.dp
        },
        label = "OptionCardElevation"
    )

    // Dynamic background colors & borders based on state
    val (bgColors, borderColor, badgeBgColor, badgeTextColor, bottomBevelColor) = when (state) {
        OptionCardState.DEFAULT -> if (isDark) {
            Pentad(
                listOf(Color(0xFF1E293B).copy(alpha = 0.90f), Color(0xFF0F172A).copy(alpha = 0.85f)),
                Color(0xFF334155),
                Color(0xFF334155),
                Color.White,
                Color(0xFF020617)
            )
        } else {
            Pentad(
                listOf(Color.White, Color(0xFFF8FAFC)),
                Color(0xFFE2E8F0),
                Color(0xFFEEF2F6),
                Color(0xFF0F2E59),
                Color(0xFFCBD5E1)
            )
        }
        OptionCardState.SELECTED -> Pentad(
            listOf(Color(0xFFE0E7FF), Color(0xFFC7D2FE)),
            NavyPrimaryLight,
            NavyPrimaryLight,
            Color.White,
            Color(0xFF3730A3)
        )
        OptionCardState.CORRECT -> Pentad(
            listOf(Color(0xFFDCFCE7), Color(0xFFBBF7D0)),
            ColorCorrect,
            ColorCorrect,
            Color.White,
            Color(0xFF15803D)
        )
        OptionCardState.INCORRECT -> Pentad(
            listOf(Color(0xFFFEE2E2), Color(0xFFFECACA)),
            ColorIncorrect,
            ColorIncorrect,
            Color.White,
            Color(0xFFB91C1C)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset(x = offsetX)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(14.dp),
                ambientColor = if (state == OptionCardState.CORRECT) ColorCorrect.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.15f),
                spotColor = if (state == OptionCardState.CORRECT) ColorCorrect.copy(alpha = 0.4f) else Color(0xFF0F2E59).copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(14.dp))
            .background(brush = Brush.verticalGradient(bgColors))
            .drawBehind {
                // 3D Bottom Bevel Edge
                drawRoundRect(
                    color = bottomBevelColor.copy(alpha = 0.65f),
                    topLeft = Offset(0f, size.height - 3.dp.toPx()),
                    size = Size(size.width, 3.dp.toPx()),
                    cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                )
            }
            .border(
                border = BorderStroke(
                    width = if (state != OptionCardState.DEFAULT) 2.dp else 1.2.dp,
                    color = borderColor
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(color = borderColor),
                enabled = enabled,
                onClick = onClick
            )
            .testTag(testTag)
            .padding(horizontal = 14.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Option Letter Pill / Status Icon
            Surface(
                shape = CircleShape,
                color = badgeBgColor,
                shadowElevation = 2.dp,
                modifier = Modifier.size(36.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (state) {
                        OptionCardState.CORRECT -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Correct",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        OptionCardState.INCORRECT -> Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Incorrect",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        else -> Text(
                            text = optionLabel,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = badgeTextColor,
                                fontSize = 15.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Option Text Content
            Text(
                text = optionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (state == OptionCardState.SELECTED || state == OptionCardState.CORRECT) FontWeight.Bold else FontWeight.Medium,
                    color = if (state == OptionCardState.SELECTED) ColorAshokaNavy else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 21.sp
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class Pentad<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
