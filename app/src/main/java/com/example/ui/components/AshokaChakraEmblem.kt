package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * High-precision Ashoka Chakra emblem with authentic 24 spokes, 24 outer rim beads,
 * multi-stop golden metallic gradient, glowing radial halo, and optional smooth rotation.
 */
@Composable
fun AshokaChakraEmblem(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    animated: Boolean = true,
    rotationDurationMs: Int = 30000,
    glowColor: Color = Color(0xFFFFD54F),
    isGold: Boolean = true,
    customColors: List<Color>? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ChakraRotation")
    val rotationAngle by if (animated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = rotationDurationMs, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "RotationAngle"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ChakraPulse"
    )

    val colors = customColors ?: if (isGold) {
        listOf(
            Color(0xFFFFF7C2), // Light gold highlight
            Color(0xFFF59E0B), // Saffron gold
            Color(0xFFD97706), // Deep antique gold
            Color(0xFFFFE082), // Soft gold
            Color(0xFFB45309)  // Shadow gold
        )
    } else {
        // Ashok Navy / Royal Blue theme
        listOf(
            Color(0xFF90CAF9),
            Color(0xFF1E40AF),
            Color(0xFF0F2E59),
            Color(0xFF3B82F6),
            Color(0xFF0A1931)
        )
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val radius = (minOf(this.size.width, this.size.height) / 2f) * 0.92f

            // 1. Ambient Golden Halo Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.35f * pulseScale),
                        glowColor.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius * 1.35f
                ),
                radius = radius * 1.35f,
                center = center
            )

            // 2. Rotating Chakra Graphic
            rotate(degrees = rotationAngle, pivot = center) {
                drawChakraGeometry(center, radius, colors)
            }
        }
    }
}

private fun DrawScope.drawChakraGeometry(
    center: Offset,
    radius: Float,
    gradientColors: List<Color>
) {
    val goldBrush = Brush.sweepGradient(
        colors = gradientColors + gradientColors.first(),
        center = center
    )
    val outerRimRadius = radius * 0.98f
    val innerRimRadius = radius * 0.85f
    val hubOuterRadius = radius * 0.22f
    val hubInnerRadius = radius * 0.11f

    // 1. Outer Concentric Rim
    drawCircle(
        brush = goldBrush,
        radius = outerRimRadius,
        center = center,
        style = Stroke(width = radius * 0.05f)
    )

    // 2. Inner Concentric Rim
    drawCircle(
        brush = goldBrush,
        radius = innerRimRadius,
        center = center,
        style = Stroke(width = radius * 0.04f)
    )

    // 3. 24 Outer Rim Decorative Beads / Dots between inner and outer rims
    val beadRingRadius = (outerRimRadius + innerRimRadius) / 2f
    val beadRadius = radius * 0.025f
    for (i in 0 until 24) {
        val angleDeg = (i * 15f + 7.5f) * (Math.PI / 180f)
        val beadCenter = Offset(
            x = center.x + beadRingRadius * cos(angleDeg).toFloat(),
            y = center.y + beadRingRadius * sin(angleDeg).toFloat()
        )
        drawCircle(
            brush = goldBrush,
            radius = beadRadius,
            center = beadCenter,
            style = Fill
        )
    }

    // 4. 24 Authentic Tapered Spokes (Base at hub, tip at inner rim)
    for (i in 0 until 24) {
        val angleDeg = (i * 15f) * (Math.PI / 180f)
        val cosA = cos(angleDeg).toFloat()
        val sinA = sin(angleDeg).toFloat()

        // Perpendicular unit vector for spoke width
        val perpAngle = angleDeg + (Math.PI / 2.0)
        val perpCos = cos(perpAngle).toFloat()
        val perpSin = sin(perpAngle).toFloat()

        val spokeBaseHalfWidth = radius * 0.032f
        val spokeTipHalfWidth = radius * 0.012f

        val baseCenter = Offset(
            center.x + hubOuterRadius * cosA,
            center.y + hubOuterRadius * sinA
        )
        val tipCenter = Offset(
            center.x + innerRimRadius * cosA,
            center.y + innerRimRadius * sinA
        )

        val p1 = Offset(baseCenter.x - spokeBaseHalfWidth * perpCos, baseCenter.y - spokeBaseHalfWidth * perpSin)
        val p2 = Offset(baseCenter.x + spokeBaseHalfWidth * perpCos, baseCenter.y + spokeBaseHalfWidth * perpSin)
        val p3 = Offset(tipCenter.x + spokeTipHalfWidth * perpCos, tipCenter.y + spokeTipHalfWidth * perpSin)
        val p4 = Offset(tipCenter.x - spokeTipHalfWidth * perpCos, tipCenter.y - spokeTipHalfWidth * perpSin)

        val spokePath = Path().apply {
            moveTo(p1.x, p1.y)
            lineTo(p2.x, p2.y)
            lineTo(p3.x, p3.y)
            lineTo(p4.x, p4.y)
            close()
        }

        drawPath(
            path = spokePath,
            brush = goldBrush,
            style = Fill
        )

        // Central spine line inside spoke for 3D ridge effect
        drawLine(
            color = Color.White.copy(alpha = 0.55f),
            start = baseCenter,
            end = tipCenter,
            strokeWidth = radius * 0.01f,
            cap = StrokeCap.Round
        )
    }

    // 5. Central Hub (Outer ring + Solid center dot)
    drawCircle(
        brush = goldBrush,
        radius = hubOuterRadius,
        center = center,
        style = Stroke(width = radius * 0.035f)
    )

    drawCircle(
        brush = goldBrush,
        radius = hubInnerRadius,
        center = center,
        style = Fill
    )

    // Specular center light bead
    drawCircle(
        color = Color.White.copy(alpha = 0.85f),
        radius = hubInnerRadius * 0.45f,
        center = Offset(center.x - hubInnerRadius * 0.2f, center.y - hubInnerRadius * 0.2f),
        style = Fill
    )
}
