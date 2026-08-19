package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BadgeItem
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorGold
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight
import kotlin.random.Random

data class ConfettiParticle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val shapeType: Int // 0 = rect, 1 = circle
)

@Composable
fun ConfettiBurst(
    trigger: Long,
    modifier: Modifier = Modifier,
    particleCount: Int = 45
) {
    val particles = remember(trigger) {
        val colors = listOf(
            SaffronSecondaryLight,
            ColorGold,
            GreenTertiaryLight,
            Color(0xFF38BDF8),
            Color(0xFFF43F5E),
            Color(0xFFA855F7)
        )
        List(particleCount) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 12f + 4f
            ConfettiParticle(
                x = 0.5f,
                y = 0.4f,
                vx = (Math.cos(angle.toDouble()).toFloat() * speed),
                vy = (Math.sin(angle.toDouble()).toFloat() * speed) - 8f,
                color = colors.random(),
                size = Random.nextFloat() * 10f + 6f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 15f - 7.5f,
                shapeType = Random.nextInt(2)
            )
        }
    }

    val progress = remember(trigger) { Animatable(0f) }

    LaunchedEffect(trigger) {
        if (trigger > 0) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
            )
        }
    }

    if (progress.value in 0.001f..0.999f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val t = progress.value
            val alpha = (1f - t).coerceIn(0f, 1f)

            particles.forEach { p ->
                val px = (p.x * size.width) + (p.vx * t * 45f)
                val py = (p.y * size.height) + (p.vy * t * 35f) + (0.5f * 9.8f * t * t * 180f)
                val rot = p.rotation + (p.rotationSpeed * t * 50f)

                rotate(rot, pivot = Offset(px, py)) {
                    if (p.shapeType == 0) {
                        drawRect(
                            color = p.color.copy(alpha = alpha),
                            topLeft = Offset(px - p.size / 2, py - p.size / 2),
                            size = Size(p.size, p.size * 1.6f)
                        )
                    } else {
                        drawCircle(
                            color = p.color.copy(alpha = alpha),
                            radius = p.size / 2,
                            center = Offset(px, py)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingXpBadge(
    text: String,
    visible: Boolean,
    isCombo: Boolean = false,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(150)) + slideInVertically(
            initialOffsetY = { 60 },
            animationSpec = tween(250, easing = FastOutSlowInEasing)
        ) + scaleIn(initialScale = 0.7f),
        exit = fadeOut(tween(350)) + slideOutVertically(
            targetOffsetY = { -80 },
            animationSpec = tween(350)
        ) + scaleOut(targetScale = 1.1f),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isCombo) SaffronSecondaryLight else GreenTertiaryLight,
            shadowElevation = 8.dp,
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                if (isCombo) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Combo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                } else {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "XP",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

@Composable
fun OutstandingCelebrationOverlay(
    score: Double,
    maxMarks: Double,
    accuracy: Double,
    onDismiss: () -> Unit
) {
    var triggerConfetti by remember { mutableStateOf(1L) }

    LaunchedEffect(Unit) {
        triggerConfetti = System.currentTimeMillis()
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ConfettiBurst(trigger = triggerConfetti)

            PolityGlassCard(
                shape = RoundedCornerShape(24.dp),
                elevation = 16.dp,
                accentGlow = ColorGold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("outstanding_celebration_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF1E3A8A),
                                    Color(0xFF0F2E59),
                                    Color(0xFF06101E)
                                )
                            )
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Glowing Ashoka Chakra Centerpiece
                        AshokaChakraEmblem(
                            size = 76.dp,
                            animated = true,
                            glowColor = Color(0xFFFFD54F)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            color = SaffronSecondaryLight,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "AIR 1 CALIBER • 90%+ SCORE 🇮🇳",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Outstanding Officer! 🏆",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Your constitutional clarity is exceptional! You achieved ${accuracy.toInt()}% accuracy with a net score of $score / $maxMarks marks.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bonus Reward Pill with Metallic Medal Graphic
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ColorGold.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ColorGold)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                PolityMedalBadgeGraphic(
                                    tier = MedalTier.GOLD,
                                    size = 28.dp,
                                    showShimmer = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Gold Conqueror Medal • +200 XP Bonus",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ColorGold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        PolityGlossyButton(
                            onClick = onDismiss,
                            style = GlossyButtonStyle.SAFFRON_GOLD,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("celebration_continue_button")
                        ) {
                            Text(
                                text = "Claim Honors & Review Solutions",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeUnlockedModal(
    badge: BadgeItem,
    onDismiss: () -> Unit
) {
    val medalTier = when (badge.tier.uppercase()) {
        "GOLD" -> MedalTier.GOLD
        "SILVER" -> MedalTier.SILVER
        "BRONZE" -> MedalTier.BRONZE
        "SPECIAL", "DIAMOND" -> MedalTier.ASHOKA_SPECIAL
        else -> MedalTier.GOLD
    }

    Dialog(onDismissRequest = onDismiss) {
        PolityGlassCard(
            shape = RoundedCornerShape(22.dp),
            elevation = 16.dp,
            accentGlow = ColorGold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("badge_unlocked_dialog")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1E3A8A),
                                Color(0xFF0F2E59),
                                Color(0xFF071426)
                            )
                        )
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(22.dp)
                ) {
                    PolityMedalBadgeGraphic(
                        tier = medalTier,
                        size = 78.dp,
                        isUnlocked = true,
                        showShimmer = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = ColorGold,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "PRESTIGE BADGE UNLOCKED",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = badge.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = badge.requirement,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GreenTertiaryLight.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GreenTertiaryLight.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "+${badge.xpReward} XP Added to Profile",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = GreenTertiaryLight,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PolityGlossyButton(
                        onClick = onDismiss,
                        style = GlossyButtonStyle.SAFFRON_GOLD,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Accept Honor & Continue", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}
