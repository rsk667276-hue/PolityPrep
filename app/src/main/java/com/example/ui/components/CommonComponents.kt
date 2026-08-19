package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.UserStatsEntity
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorGold
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolityTopAppBar(
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    userStats: UserStatsEntity? = null,
    onStreakClick: () -> Unit = {},
    onPremiumClick: () -> Unit = {},
    onXpClick: () -> Unit = {},
    onToggleSound: (() -> Unit)? = null
) {
    val totalXp = userStats?.totalXp ?: 120
    val level = (totalXp / 100) + 1
    val soundEnabled = userStats?.soundEffectsEnabled ?: true

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                listOf(
                    SaffronSecondaryLight.copy(alpha = 0.35f),
                    Color(0xFFE2E8F0).copy(alpha = 0.2f),
                    GreenTertiaryLight.copy(alpha = 0.35f)
                )
            )
        )
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ashoka Chakra Glowing Golden Emblem in Header
                    AshokaChakraEmblem(
                        size = 32.dp,
                        animated = true,
                        glowColor = Color(0xFFFFD54F),
                        modifier = Modifier.padding(end = 10.dp)
                    )

                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.4.sp
                            ),
                            maxLines = 1
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
        actions = {
            // Sound Effects Toggle Icon
            if (onToggleSound != null) {
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Sound FX",
                        tint = if (soundEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
            }

            // Level / XP Pill
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = GreenTertiaryLight.copy(alpha = 0.15f),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onXpClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "XP",
                        tint = ColorGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Lv.$level",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = GreenTertiaryLight,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = " ($totalXp)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = GreenTertiaryLight.copy(alpha = 0.8f),
                            fontSize = 9.5.sp
                        )
                    )
                }
            }

            // Streak Pill
            val streak = userStats?.dailyStreak ?: 1
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SaffronSecondaryLight.copy(alpha = 0.15f),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onStreakClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = SaffronSecondaryLight,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "$streak d",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SaffronSecondaryLight,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Premium / Quota Pill
            val isPremium = userStats?.isPremium == true
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isPremium) ColorGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onPremiumClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (isPremium) Icons.Default.WorkspacePremium else Icons.Default.AutoAwesome,
                        contentDescription = "Subscription",
                        tint = if (isPremium) ColorGold else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (isPremium) "PRO" else "FREE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isPremium) SaffronSecondaryLight else MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        )
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun SourcesVerifiedBadge(
    articleReference: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = GreenTertiaryLight.copy(alpha = 0.12f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GreenTertiaryLight.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Verified Constitutional Source",
                tint = GreenTertiaryLight,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Constitution Source: $articleReference",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = GreenTertiaryLight,
                    fontSize = 11.sp
                )
            )
        }
    }
}

@Composable
fun AdMobBannerSimulator(
    isPremium: Boolean,
    onUpgradeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isPremium) return

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("admob_banner")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(3.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = "Ad",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
                Column {
                    Text(
                        text = "Drishti / Vision IAS Prelims Test Series 2026",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "Sponsored Mock Test Partner • Get 20% off",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onUpgradeClick() }
            ) {
                Text(
                    text = "Ad-Free Pro",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun RewardedAdPlayerDialog(
    countdownSeconds: Int,
    onFinished: () -> Unit
) {
    Dialog(onDismissRequest = { }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ColorAshokaNavy),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("rewarded_ad_player")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SaffronSecondaryLight,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "SPONSORED VIDEO AD",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "Reward in: ${countdownSeconds}s",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1E3A8A), Color(0xFF0F172A))
                            )
                        )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Playing video",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Unacademy & NextIAS UPSC Batch 2026",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Complete Indian Polity by Top Educators",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { (5 - countdownSeconds) / 5f },
                    color = SaffronSecondaryLight,
                    trackColor = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Watching this ad grants +3 Free AI Quiz attempts!",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
fun RewardClaimedDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = ColorCorrect,
                modifier = Modifier.size(44.dp)
            )
        },
        title = {
            Text(
                text = "Reward Granted! 🎉",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "3 bonus UPSC/SSC quiz attempts have been credited to your account. Keep mastering Indian Polity!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.testTag("reward_dialog_confirm_button")
            ) {
                Text("Start Practicing")
            }
        }
    )
}

@Composable
fun SubscriptionUpgradeModal(
    isCurrentPremium: Boolean,
    onUpgrade: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("subscription_modal")
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = "PolityPro",
                            tint = ColorGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PolityPrep Pro",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "Crack UPSC Civil Services Prelims 2026 with Unlimited AI Quizzes & Advanced Analytics",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                val features = listOf(
                    "⚡ Unlimited AI-generated UPSC/SSC Quizzes",
                    "📚 Full 2018-2024 Prelims PYQ Question Bank",
                    "🧠 Spaced Repetition SRS Flashcard Revision",
                    "🎯 Weak Topic Diagnostic Radar & Article Map",
                    "⏱️ Real Exam Mode with -0.66 Negative Marking",
                    "🚫 100% Ad-Free Clean Focus Experience"
                )

                features.forEach { feat ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = ColorCorrect,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = feat,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Monthly Plan Option
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onUpgrade("PREMIUM_MONTHLY") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Monthly Pass",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Billed monthly • Cancel anytime",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                            )
                        }
                        Text(
                            text = "₹99 / mo",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Annual Pass
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SaffronSecondaryLight.copy(alpha = 0.15f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(2.dp, SaffronSecondaryLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onUpgrade("PREMIUM_ANNUAL") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "UPSC 2026 Annual Pass",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = SaffronSecondaryLight,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "SAVE 58%",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Complete 1-Year Prelims Prep Access",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                            )
                        }
                        Text(
                            text = "₹499 / yr",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = SaffronSecondaryLight
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = { onUpgrade("PREMIUM_ANNUAL") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("subscribe_now_button")
                ) {
                    Text(
                        text = if (isCurrentPremium) "Manage Active Subscription" else "Unlock Unlimited Pro (₹499/yr)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StreakDetailDialog(
    streakCount: Int,
    dailyTargetCompleted: Int,
    dailyTarget: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        PolityGlassCard(
            shape = RoundedCornerShape(22.dp),
            elevation = 16.dp,
            accentGlow = SaffronSecondaryLight,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("streak_detail_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(22.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = SaffronSecondaryLight.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SaffronSecondaryLight.copy(alpha = 0.5f)),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "🔥", fontSize = 38.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "$streakCount-Day Study Streak",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (streakCount > 1) {
                        "You've maintained active UPSC Polity quizzing for $streakCount consecutive days! Keep going to unlock prestige streak badges."
                    } else {
                        "You're on Day 1 of your daily study streak! Complete daily questions consistently to build exam discipline and momentum."
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress towards today's target
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondaryLight.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Today's Target",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "$dailyTargetCompleted / $dailyTarget Questions",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SaffronSecondaryLight
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val progress = (dailyTargetCompleted.toFloat() / dailyTarget.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SaffronSecondaryLight,
                            trackColor = SaffronSecondaryLight.copy(alpha = 0.2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                PolityGlossyButton(
                    onClick = onDismiss,
                    style = GlossyButtonStyle.SAFFRON_GOLD,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Got It, Keep Quizzing!", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

