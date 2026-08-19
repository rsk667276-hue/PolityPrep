package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.BadgeItem
import com.example.model.PolityBadges
import com.example.ui.components.BadgeUnlockedModal
import com.example.ui.components.PolityTopAppBar
import com.example.ui.theme.ColorAshokaNavy
import com.example.ui.theme.ColorGold
import com.example.ui.theme.GreenTertiaryLight
import com.example.ui.theme.SaffronSecondaryLight
import com.example.ui.viewmodel.PolityViewModel
import com.example.ui.viewmodel.ScreenDestination

@Composable
fun BadgesScreen(
    viewModel: PolityViewModel,
    modifier: Modifier = Modifier
) {
    val userStats by viewModel.userStats.collectAsStateWithLifecycle()
    val unlockedBadgesSet = remember(userStats?.unlockedBadges) {
        userStats?.unlockedBadges?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
    }
    var selectedBadgeForModal by remember { mutableStateOf<BadgeItem?>(null) }

    Scaffold(
        topBar = {
            PolityTopAppBar(
                title = "Milestones & Badges",
                subtitle = "${unlockedBadgesSet.size} of ${PolityBadges.ALL.size} Badges Earned",
                showBackButton = true,
                onBackClick = { viewModel.navigateTo(ScreenDestination.Home) },
                userStats = userStats,
                onStreakClick = { viewModel.openStreakDialog() },
                onPremiumClick = { viewModel.openSubscriptionModal() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Progress Banner
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ColorAshokaNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aspirant Honors Showcase",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Earn badges through consistent practice, UPSC Prelims cutoffs, and streak discipline.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ColorGold.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ColorGold)
                    ) {
                        Text(
                            text = "${unlockedBadgesSet.size}/${PolityBadges.ALL.size} 🏆",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = ColorGold,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(PolityBadges.ALL) { badge ->
                    val isUnlocked = unlockedBadgesSet.contains(badge.id)
                    BadgeGridItem(
                        badge = badge,
                        isUnlocked = isUnlocked,
                        onClick = { selectedBadgeForModal = badge }
                    )
                }
            }
        }

        // Details dialog if clicked
        if (selectedBadgeForModal != null) {
            BadgeUnlockedModal(
                badge = selectedBadgeForModal!!,
                onDismiss = { selectedBadgeForModal = null }
            )
        }
    }
}

@Composable
fun BadgeGridItem(
    badge: BadgeItem,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = if (isUnlocked) {
            when (badge.tier) {
                "GOLD" -> androidx.compose.foundation.BorderStroke(1.5.dp, ColorGold)
                "SILVER" -> androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF94A3B8))
                "SPECIAL" -> androidx.compose.foundation.BorderStroke(1.5.dp, SaffronSecondaryLight)
                else -> androidx.compose.foundation.BorderStroke(1.dp, GreenTertiaryLight.copy(alpha = 0.5f))
            }
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("badge_item_${badge.id}")
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(14.dp)
                .alpha(if (isUnlocked) 1f else 0.5f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) ColorGold.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f)
                    )
            ) {
                Text(
                    text = badge.iconEmoji,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = badge.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Text(
                text = badge.requirement,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    lineHeight = 13.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = if (isUnlocked) GreenTertiaryLight.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    if (isUnlocked) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Earned",
                            tint = GreenTertiaryLight,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "EARNED (+${badge.xpReward} XP)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GreenTertiaryLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "LOCKED (+${badge.xpReward} XP)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
