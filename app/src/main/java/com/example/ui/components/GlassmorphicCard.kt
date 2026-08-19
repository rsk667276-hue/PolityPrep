package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Premium Glassmorphism Card with frosted translucent layers, specular light borders,
 * layered ambient shadow depth, and UPSC prestige aesthetic.
 */
@Composable
fun PolityGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 6.dp,
    borderAlpha: Float = 0.45f,
    accentGlow: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // Frosted glass background gradient
    val glassBrush = if (isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1E293B).copy(alpha = 0.85f),
                Color(0xFF0F172A).copy(alpha = 0.72f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.92f),
                Color(0xFFF8FAFC).copy(alpha = 0.80f)
            )
        )
    }

    // Specular light edge highlight border
    val borderBrush = if (accentGlow != null) {
        Brush.linearGradient(
            colors = listOf(
                accentGlow.copy(alpha = 0.8f),
                accentGlow.copy(alpha = 0.3f),
                if (isDark) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.5f)
            )
        )
    } else if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.30f * borderAlpha),
                Color(0xFFF59E0B).copy(alpha = 0.25f * borderAlpha),
                Color.White.copy(alpha = 0.08f * borderAlpha)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.95f),
                Color(0xFFCBD5E1).copy(alpha = 0.6f * borderAlpha),
                Color(0xFFF59E0B).copy(alpha = 0.25f * borderAlpha)
            )
        )
    }

    val baseModifier = modifier
        .shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = if (isDark) Color.Black.copy(alpha = 0.6f) else Color(0xFF0A1931).copy(alpha = 0.12f),
            spotColor = if (isDark) Color(0xFF0A1931).copy(alpha = 0.8f) else Color(0xFF0F2E59).copy(alpha = 0.16f)
        )
        .clip(shape)
        .background(brush = glassBrush)
        .border(
            border = BorderStroke(1.dp, borderBrush),
            shape = shape
        )

    val finalModifier = if (onClick != null) {
        baseModifier.clickable(onClick = onClick)
    } else {
        baseModifier
    }

    Box(
        modifier = finalModifier,
        content = content
    )
}
