package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorGold
import com.example.ui.theme.SaffronSecondaryLight

enum class GlossyButtonStyle {
    PRIMARY_NAVY,
    SAFFRON_GOLD,
    EMERALD_GREEN,
    OUTLINED_GLASS
}

/**
 * High-craft Glossy Button with 3D depth, specular glass reflection sheen,
 * tactile press-lift animation, and rich colored shadow glow.
 */
@Composable
fun PolityGlossyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlossyButtonStyle = GlossyButtonStyle.PRIMARY_NAVY,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(14.dp),
    height: Dp = 50.dp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val translationY by animateDpAsState(
        targetValue = if (isPressed && enabled) 2.5.dp else 0.dp,
        label = "ButtonPressLift"
    )

    val shadowElevation by animateDpAsState(
        targetValue = if (!enabled) 0.dp else if (isPressed) 2.dp else 8.dp,
        label = "ButtonShadowElevation"
    )

    val (bgGradient, borderColor, shadowGlowColor, bottomBevelColor) = when (style) {
        GlossyButtonStyle.PRIMARY_NAVY -> Quadruple(
            listOf(Color(0xFF1E427B), Color(0xFF0F2E59), Color(0xFF091E3B)),
            Color(0xFF60A5FA).copy(alpha = 0.55f),
            Color(0xFF0F2E59).copy(alpha = 0.45f),
            Color(0xFF051329)
        )
        GlossyButtonStyle.SAFFRON_GOLD -> Quadruple(
            listOf(Color(0xFFFFB347), Color(0xFFF59E0B), Color(0xFFB45309)),
            Color(0xFFFFF7C2).copy(alpha = 0.75f),
            Color(0xFFD97706).copy(alpha = 0.50f),
            Color(0xFF78350F)
        )
        GlossyButtonStyle.EMERALD_GREEN -> Quadruple(
            listOf(Color(0xFF22C55E), Color(0xFF15803D), Color(0xFF0A4F23)),
            Color(0xFF86EFAC).copy(alpha = 0.65f),
            Color(0xFF16A34A).copy(alpha = 0.45f),
            Color(0xFF063316)
        )
        GlossyButtonStyle.OUTLINED_GLASS -> Quadruple(
            listOf(Color.White.copy(alpha = 0.85f), Color(0xFFF1F5F9).copy(alpha = 0.75f)),
            Color(0xFFCBD5E1),
            Color(0xFF0A1931).copy(alpha = 0.12f),
            Color(0xFF94A3B8)
        )
    }

    val disabledGradient = listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))

    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .offset(y = translationY)
            .shadow(
                elevation = shadowElevation,
                shape = shape,
                ambientColor = shadowGlowColor,
                spotColor = shadowGlowColor
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (enabled) bgGradient else disabledGradient
                )
            )
            .drawBehind {
                if (enabled && style != GlossyButtonStyle.OUTLINED_GLASS) {
                    // 1. Top Specular Glass Sheen (Upper half transparent white gradient)
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.35f),
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = size.height * 0.52f
                        ),
                        size = Size(size.width, size.height * 0.52f),
                        cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                    )

                    // 2. Bottom 3D Bevel Shadow Strip
                    drawRoundRect(
                        color = bottomBevelColor.copy(alpha = 0.55f),
                        topLeft = Offset(0f, size.height - 3.5.dp.toPx()),
                        size = Size(size.width, 3.5.dp.toPx()),
                        cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                    )
                }
            }
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    brush = if (enabled) {
                        Brush.verticalGradient(
                            listOf(borderColor, borderColor.copy(alpha = 0.25f))
                        )
                    } else {
                        Brush.linearGradient(listOf(Color(0xFFCBD5E1), Color(0xFF94A3B8)))
                    }
                ),
                shape = shape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(color = Color.White),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .height(height)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
