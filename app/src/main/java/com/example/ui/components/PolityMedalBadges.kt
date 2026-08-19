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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

enum class MedalTier {
    BRONZE,
    SILVER,
    GOLD,
    DIAMOND,
    ASHOKA_SPECIAL,
    LOCKED
}

/**
 * Realistic Metallic Medal Graphics for Bronze, Silver, Gold, Diamond & Ashoka Milestones.
 * Features 3D beveled medallion discs, folded tricolor/satin neck ribbons,
 * engraved laurel leaves / stars / Chakra emblems, and realistic metallic sheen.
 */
@Composable
fun PolityMedalBadgeGraphic(
    tier: MedalTier,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    isUnlocked: Boolean = true,
    showShimmer: Boolean = true
) {
    val actualTier = if (!isUnlocked) MedalTier.LOCKED else tier

    val infiniteTransition = rememberInfiniteTransition(label = "MedalShimmer")
    val shimmerOffset by if (showShimmer && isUnlocked) {
        infiniteTransition.animateFloat(
            initialValue = -1f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "ShimmerOffset"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(-1f) }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            // 1. Draw Folded Satin Hanging Ribbon at Top
            drawSatinRibbon(w, h, actualTier)

            // 2. Draw 3D Metallic Medallion Disc
            val discCenter = Offset(w / 2f, h * 0.62f)
            val discRadius = w * 0.36f

            drawMedalDisc(discCenter, discRadius, actualTier, shimmerOffset)

            // 3. Draw Engraved Emblem inside Medal
            drawMedalEmblem(discCenter, discRadius, actualTier)
        }
    }
}

private fun DrawScope.drawSatinRibbon(w: Float, h: Float, tier: MedalTier) {
    val ribbonHeight = h * 0.44f
    val ribbonWidth = w * 0.44f
    val centerX = w / 2f

    // Ribbon Colors based on tier
    val ribbonColors = when (tier) {
        MedalTier.GOLD, MedalTier.ASHOKA_SPECIAL -> listOf(
            Color(0xFFFF9933), // Saffron
            Color(0xFFFFFFFF), // White
            Color(0xFF138808)  // Green
        )
        MedalTier.SILVER -> listOf(
            Color(0xFF1E3A8A), // Royal Navy
            Color(0xFF93C5FD), // Light Blue
            Color(0xFF1E3A8A)
        )
        MedalTier.BRONZE -> listOf(
            Color(0xFF78350F), // Bronze Amber
            Color(0xFFFDE68A), // Gold Thread
            Color(0xFF78350F)
        )
        MedalTier.DIAMOND -> listOf(
            Color(0xFF0284C7),
            Color(0xFFE0F2FE),
            Color(0xFF0284C7)
        )
        MedalTier.LOCKED -> listOf(
            Color(0xFF475569),
            Color(0xFF64748B),
            Color(0xFF475569)
        )
    }

    // Left Ribbon Stripe
    val leftPath = Path().apply {
        moveTo(centerX - ribbonWidth * 0.6f, 0f)
        lineTo(centerX - ribbonWidth * 0.1f, 0f)
        lineTo(centerX - ribbonWidth * 0.1f, ribbonHeight)
        lineTo(centerX - ribbonWidth * 0.35f, ribbonHeight * 0.85f)
        lineTo(centerX - ribbonWidth * 0.6f, ribbonHeight)
        close()
    }

    // Right Ribbon Stripe
    val rightPath = Path().apply {
        moveTo(centerX + ribbonWidth * 0.1f, 0f)
        lineTo(centerX + ribbonWidth * 0.6f, 0f)
        lineTo(centerX + ribbonWidth * 0.6f, ribbonHeight)
        lineTo(centerX + ribbonWidth * 0.35f, ribbonHeight * 0.85f)
        lineTo(centerX + ribbonWidth * 0.1f, ribbonHeight)
        close()
    }

    // Top loop connector
    val topLoopPath = Path().apply {
        moveTo(centerX - ribbonWidth * 0.45f, 0f)
        lineTo(centerX + ribbonWidth * 0.45f, 0f)
        lineTo(centerX + ribbonWidth * 0.2f, ribbonHeight * 0.55f)
        lineTo(centerX - ribbonWidth * 0.2f, ribbonHeight * 0.55f)
        close()
    }

    drawPath(
        path = topLoopPath,
        brush = Brush.linearGradient(
            colors = listOf(ribbonColors[0], ribbonColors[1], ribbonColors[2]),
            start = Offset(centerX - ribbonWidth * 0.5f, 0f),
            end = Offset(centerX + ribbonWidth * 0.5f, ribbonHeight * 0.5f)
        )
    )

    drawPath(
        path = leftPath,
        brush = Brush.horizontalGradient(
            colors = listOf(ribbonColors[0], ribbonColors[1]),
            startX = centerX - ribbonWidth * 0.6f,
            endX = centerX
        )
    )

    drawPath(
        path = rightPath,
        brush = Brush.horizontalGradient(
            colors = listOf(ribbonColors[1], ribbonColors[2]),
            startX = centerX,
            endX = centerX + ribbonWidth * 0.6f
        )
    )

    // Ribbon Hanging Shadow over bottom medal connector
    drawCircle(
        color = Color.Black.copy(alpha = 0.25f),
        radius = w * 0.12f,
        center = Offset(centerX, ribbonHeight * 0.7f)
    )
}

private fun DrawScope.drawMedalDisc(
    center: Offset,
    radius: Float,
    tier: MedalTier,
    shimmerOffset: Float
) {
    // Disc metallic multi-stop color palette
    val (primaryGradients, rimColor, shadowColor) = when (tier) {
        MedalTier.GOLD, MedalTier.ASHOKA_SPECIAL -> Triple(
            listOf(
                Color(0xFFFFFBEB), // Intense Gold Specular
                Color(0xFFFDE68A), // Light Gold
                Color(0xFFF59E0B), // Saffron Gold
                Color(0xFFD97706), // Antique Bronze-Gold
                Color(0xFF92400E)  // Dark Shadow Rim
            ),
            Color(0xFFFFD54F),
            Color(0xFF78350F)
        )
        MedalTier.SILVER -> Triple(
            listOf(
                Color(0xFFFFFFFF), // Pure White Shine
                Color(0xFFF1F5F9), // Silver Highlight
                Color(0xFFCBD5E1), // Platinum Silver
                Color(0xFF94A3B8), // Brushed Steel
                Color(0xFF475569)  // Dark Metallic
            ),
            Color(0xFFE2E8F0),
            Color(0xFF334155)
        )
        MedalTier.BRONZE -> Triple(
            listOf(
                Color(0xFFFFEDD5), // Bright Bronze Specular
                Color(0xFFFDBA74), // Warm Copper
                Color(0xFFCD7F32), // Classic Bronze
                Color(0xFF9A3412), // Deep Antique Bronze
                Color(0xFF451A03)  // Cast Iron Base
            ),
            Color(0xFFF97316),
            Color(0xFF431407)
        )
        MedalTier.DIAMOND -> Triple(
            listOf(
                Color(0xFFFFFFFF),
                Color(0xFFE0F2FE),
                Color(0xFF38BDF8),
                Color(0xFF0284C7),
                Color(0xFF0C4A6E)
            ),
            Color(0xFF7DD3FC),
            Color(0xFF082F49)
        )
        MedalTier.LOCKED -> Triple(
            listOf(
                Color(0xFF94A3B8),
                Color(0xFF64748B),
                Color(0xFF475569),
                Color(0xFF334155),
                Color(0xFF1E293B)
            ),
            Color(0xFF64748B),
            Color(0xFF0F172A)
        )
    }

    // 1. Outer Deep Cast Shadow
    drawCircle(
        color = shadowColor.copy(alpha = 0.4f),
        radius = radius * 1.05f,
        center = Offset(center.x, center.y + radius * 0.08f)
    )

    // 2. Beveled Metallic Medallion Rim (Radial Sweep Gradient)
    val medalBrush = Brush.radialGradient(
        colors = primaryGradients,
        center = Offset(center.x - radius * 0.3f, center.y - radius * 0.35f),
        radius = radius * 1.3f
    )
    drawCircle(
        brush = medalBrush,
        radius = radius,
        center = center
    )

    // 3. Relief Concentric Inner Ring
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(primaryGradients[0], primaryGradients.last()),
            start = Offset(center.x, center.y - radius),
            end = Offset(center.x, center.y + radius)
        ),
        radius = radius * 0.82f,
        center = center,
        style = Stroke(width = radius * 0.06f)
    )

    // 4. Inner Medallion Recessed Face
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(primaryGradients[1], primaryGradients[3]),
            center = Offset(center.x + radius * 0.2f, center.y + radius * 0.2f),
            radius = radius * 0.9f
        ),
        radius = radius * 0.78f,
        center = center
    )

    // 5. Specular Light Sweep (Dynamic Shimmer Angle)
    if (shimmerOffset in -0.5f..1.5f && tier != MedalTier.LOCKED) {
        val sweepBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.45f),
                Color.Transparent
            ),
            start = Offset(center.x + (shimmerOffset - 0.5f) * radius * 2f, center.y - radius),
            end = Offset(center.x + (shimmerOffset + 0.2f) * radius * 2f, center.y + radius)
        )
        drawCircle(
            brush = sweepBrush,
            radius = radius * 0.95f,
            center = center
        )
    }
}

private fun DrawScope.drawMedalEmblem(
    center: Offset,
    radius: Float,
    tier: MedalTier
) {
    val emblemColor = when (tier) {
        MedalTier.GOLD, MedalTier.ASHOKA_SPECIAL -> Color(0xFF78350F)
        MedalTier.SILVER -> Color(0xFF334155)
        MedalTier.BRONZE -> Color(0xFF431407)
        MedalTier.DIAMOND -> Color(0xFF0369A1)
        MedalTier.LOCKED -> Color(0xFF1E293B)
    }
    val highlightColor = Color.White.copy(alpha = 0.65f)

    when (tier) {
        MedalTier.GOLD, MedalTier.ASHOKA_SPECIAL -> {
            // Embossed 16-Spoke Ashoka / Star Emblem in Center
            val innerR = radius * 0.48f
            drawCircle(
                color = emblemColor,
                radius = innerR * 0.22f,
                center = center,
                style = Fill
            )
            for (i in 0 until 12) {
                val ang = (i * 30f) * (Math.PI / 180.0)
                val tip = Offset(
                    center.x + innerR * cos(ang).toFloat(),
                    center.y + innerR * sin(ang).toFloat()
                )
                drawLine(
                    color = emblemColor,
                    start = center,
                    end = tip,
                    strokeWidth = radius * 0.05f,
                    cap = StrokeCap.Round
                )
            }
            // Laurel Leaf Wreath Border
            drawWreath(center, radius * 0.62f, emblemColor, highlightColor)
        }
        MedalTier.SILVER -> {
            // Embossed 5-Pointed Star
            drawStar(center, radius * 0.44f, radius * 0.20f, 5, emblemColor, highlightColor)
            drawWreath(center, radius * 0.60f, emblemColor, highlightColor)
        }
        MedalTier.BRONZE -> {
            // Embossed Roman Pillar / Shield
            drawShieldEmblem(center, radius * 0.42f, emblemColor, highlightColor)
            drawWreath(center, radius * 0.60f, emblemColor, highlightColor)
        }
        MedalTier.DIAMOND -> {
            // Brilliant Cut Gem Polygon
            drawDiamondGem(center, radius * 0.42f, emblemColor, highlightColor)
        }
        MedalTier.LOCKED -> {
            // Lock Silhouette
            drawLockIcon(center, radius * 0.38f, emblemColor, highlightColor)
        }
    }
}

private fun DrawScope.drawWreath(
    center: Offset,
    wreathRadius: Float,
    color: Color,
    highlight: Color
) {
    for (i in 0 until 10) {
        val angleLeft = (120f + i * 14f) * (Math.PI / 180.0)
        val pLeft = Offset(
            center.x + wreathRadius * cos(angleLeft).toFloat(),
            center.y + wreathRadius * sin(angleLeft).toFloat()
        )
        drawCircle(color = color, radius = wreathRadius * 0.09f, center = pLeft)

        val angleRight = (60f - i * 14f) * (Math.PI / 180.0)
        val pRight = Offset(
            center.x + wreathRadius * cos(angleRight).toFloat(),
            center.y + wreathRadius * sin(angleRight).toFloat()
        )
        drawCircle(color = color, radius = wreathRadius * 0.09f, center = pRight)
    }
}

private fun DrawScope.drawStar(
    center: Offset,
    outerR: Float,
    innerR: Float,
    points: Int,
    color: Color,
    highlight: Color
) {
    val path = Path()
    val totalPoints = points * 2
    for (i in 0 until totalPoints) {
        val r = if (i % 2 == 0) outerR else innerR
        val angle = (i * (360f / totalPoints) - 90f) * (Math.PI / 180.0)
        val x = center.x + r * cos(angle).toFloat()
        val y = center.y + r * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(path = path, color = color, style = Fill)
    drawPath(path = path, color = highlight, style = Stroke(width = outerR * 0.08f))
}

private fun DrawScope.drawShieldEmblem(
    center: Offset,
    size: Float,
    color: Color,
    highlight: Color
) {
    val path = Path().apply {
        moveTo(center.x, center.y - size * 0.8f)
        lineTo(center.x + size * 0.65f, center.y - size * 0.45f)
        lineTo(center.x + size * 0.55f, center.y + size * 0.3f)
        lineTo(center.x, center.y + size * 0.8f)
        lineTo(center.x - size * 0.55f, center.y + size * 0.3f)
        lineTo(center.x - size * 0.65f, center.y - size * 0.45f)
        close()
    }
    drawPath(path = path, color = color, style = Fill)
    drawPath(path = path, color = highlight, style = Stroke(width = size * 0.08f))
}

private fun DrawScope.drawDiamondGem(
    center: Offset,
    size: Float,
    color: Color,
    highlight: Color
) {
    val path = Path().apply {
        moveTo(center.x - size * 0.5f, center.y - size * 0.4f)
        lineTo(center.x + size * 0.5f, center.y - size * 0.4f)
        lineTo(center.x + size * 0.8f, center.y)
        lineTo(center.x, center.y + size * 0.8f)
        lineTo(center.x - size * 0.8f, center.y)
        close()
    }
    drawPath(path = path, color = color, style = Fill)
    drawPath(path = path, color = highlight, style = Stroke(width = size * 0.08f))
}

private fun DrawScope.drawLockIcon(
    center: Offset,
    size: Float,
    color: Color,
    highlight: Color
) {
    // Shackle
    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(center.x - size * 0.32f, center.y - size * 0.7f),
        size = Size(size * 0.64f, size * 0.64f),
        style = Stroke(width = size * 0.16f, cap = StrokeCap.Round)
    )
    // Body
    val bodyPath = Path().apply {
        addRoundRect(
            androidx.compose.ui.geometry.RoundRect(
                left = center.x - size * 0.45f,
                top = center.y - size * 0.2f,
                right = center.x + size * 0.45f,
                bottom = center.y + size * 0.55f,
                radiusX = size * 0.15f,
                radiusY = size * 0.15f
            )
        )
    }
    drawPath(path = bodyPath, color = color, style = Fill)
    // Keyhole
    drawCircle(color = highlight, radius = size * 0.1f, center = Offset(center.x, center.y + size * 0.05f))
    drawLine(
        color = highlight,
        start = Offset(center.x, center.y + size * 0.05f),
        end = Offset(center.x, center.y + size * 0.3f),
        strokeWidth = size * 0.08f,
        cap = StrokeCap.Round
    )
}
