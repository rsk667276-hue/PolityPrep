package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium Tricolor Animated Gradient Mesh Background with subtle dot-grid texture.
 * Features smooth pulsing ambient light sources for Saffron (top), Warm Pearl (center),
 * and Indian Green (bottom), providing a prestigious UPSC exam atmosphere.
 */
@Composable
fun AmbientMeshBackground(
    modifier: Modifier = Modifier,
    showDotGrid: Boolean = true,
    dotSpacing: Dp = 24.dp,
    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val infiniteTransition = rememberInfiniteTransition(label = "AmbientMeshMotion")

    // Ambient floating offsets for gradient light orbs
    val animTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "MeshMotion"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "MeshGlowPulse"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val angleRad = (animTime * Math.PI / 180.0).toFloat()

            // Base Canvas Tint
            val baseBgColor = if (isDark) Color(0xFF091024) else Color(0xFFF6F8FC)
            drawRect(color = baseBgColor)

            // 1. Saffron Ambient Light Orb (Top-Right drifting)
            val saffronCenter = Offset(
                x = width * 0.82f + 40f * cos(angleRad),
                y = height * 0.12f + 30f * sin(angleRad)
            )
            val saffronColor = if (isDark) Color(0xFFD97706) else Color(0xFFFF9933)
            val saffronAlpha = if (isDark) 0.18f * pulseGlow else 0.14f * pulseGlow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        saffronColor.copy(alpha = saffronAlpha),
                        saffronColor.copy(alpha = saffronAlpha * 0.4f),
                        Color.Transparent
                    ),
                    center = saffronCenter,
                    radius = width * 0.85f
                ),
                center = saffronCenter,
                radius = width * 0.85f
            )

            // 2. India Green Ambient Light Orb (Bottom-Left drifting)
            val greenCenter = Offset(
                x = width * 0.15f - 35f * sin(angleRad),
                y = height * 0.88f - 25f * cos(angleRad)
            )
            val greenColor = if (isDark) Color(0xFF15803D) else Color(0xFF138808)
            val greenAlpha = if (isDark) 0.16f * pulseGlow else 0.12f * pulseGlow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        greenColor.copy(alpha = greenAlpha),
                        greenColor.copy(alpha = greenAlpha * 0.35f),
                        Color.Transparent
                    ),
                    center = greenCenter,
                    radius = width * 0.85f
                ),
                center = greenCenter,
                radius = width * 0.85f
            )

            // 3. Royal Ashoka Navy Deep Radial Accent (Top-Left / Center-Right)
            val navyCenter = Offset(
                x = width * 0.20f + 25f * cos(angleRad * 0.7f),
                y = height * 0.35f + 20f * sin(angleRad * 0.7f)
            )
            val navyColor = if (isDark) Color(0xFF1E3A8A) else Color(0xFF0F2E59)
            val navyAlpha = if (isDark) 0.22f else 0.06f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        navyColor.copy(alpha = navyAlpha),
                        navyColor.copy(alpha = navyAlpha * 0.3f),
                        Color.Transparent
                    ),
                    center = navyCenter,
                    radius = width * 0.75f
                ),
                center = navyCenter,
                radius = width * 0.75f
            )

            // 4. Subtle Dot-Grid Texture
            if (showDotGrid) {
                drawDotGridPattern(
                    dotSpacingPx = dotSpacing.toPx(),
                    dotRadiusPx = 1.2f,
                    dotColor = if (isDark) Color.White.copy(alpha = 0.05f) else Color(0xFF0A1931).copy(alpha = 0.045f)
                )
            }
        }

        // Foreground application content
        content()
    }
}

private fun DrawScope.drawDotGridPattern(
    dotSpacingPx: Float,
    dotRadiusPx: Float,
    dotColor: Color
) {
    var x = dotSpacingPx / 2f
    while (x < size.width) {
        var y = dotSpacingPx / 2f
        while (y < size.height) {
            drawCircle(
                color = dotColor,
                radius = dotRadiusPx,
                center = Offset(x, y)
            )
            y += dotSpacingPx
        }
        x += dotSpacingPx
    }
}
