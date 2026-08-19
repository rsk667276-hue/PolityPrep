package com.example.model

data class BadgeItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val tier: String, // "BRONZE", "SILVER", "GOLD", "SPECIAL"
    val requirement: String,
    val xpReward: Int
)

object PolityBadges {
    val ALL = listOf(
        BadgeItem(
            id = "BRONZE_STARTER",
            title = "Bronze Starter",
            subtitle = "First Milestone",
            iconEmoji = "🥉",
            tier = "BRONZE",
            requirement = "Score 40%+ on any UPSC Polity quiz",
            xpReward = 50
        ),
        BadgeItem(
            id = "SILVER_SCHOLAR",
            title = "Silver Scholar",
            subtitle = "Prelims Ready",
            iconEmoji = "🥈",
            tier = "SILVER",
            requirement = "Score 70%+ on any UPSC Polity quiz",
            xpReward = 100
        ),
        BadgeItem(
            id = "GOLD_CONQUEROR",
            title = "Gold Conqueror",
            subtitle = "Outstanding 90%+ Cutoff",
            iconEmoji = "🥇",
            tier = "GOLD",
            requirement = "Score 90%+ on any UPSC Polity quiz",
            xpReward = 200
        ),
        BadgeItem(
            id = "COMBO_MASTER",
            title = "Lightning Combo Master",
            subtitle = "5x Streak in Test",
            iconEmoji = "⚡",
            tier = "GOLD",
            requirement = "Achieve 5+ consecutive correct answers in a quiz",
            xpReward = 150
        ),
        BadgeItem(
            id = "DAILY_CRUSHER",
            title = "Daily Target Crusher",
            subtitle = "10 Questions Daily Goal",
            iconEmoji = "🎯",
            tier = "SILVER",
            requirement = "Complete daily 10 constitutional questions goal",
            xpReward = 75
        ),
        BadgeItem(
            id = "STREAK_7",
            title = "7-Day Consistency Master",
            subtitle = "1 Week Study Streak",
            iconEmoji = "🔥",
            tier = "GOLD",
            requirement = "Maintain a 7-day study streak",
            xpReward = 300
        ),
        BadgeItem(
            id = "STREAK_30",
            title = "30-Day Constitution Titan",
            subtitle = "1 Month Iron Discipline",
            iconEmoji = "👑",
            tier = "SPECIAL",
            requirement = "Maintain a 30-day study streak",
            xpReward = 1000
        ),
        BadgeItem(
            id = "SRS_CONQUEROR",
            title = "Memory Champion",
            subtitle = "Spaced Repetition Master",
            iconEmoji = "🧠",
            tier = "SILVER",
            requirement = "Review and clear previously incorrect questions via SRS",
            xpReward = 120
        ),
        BadgeItem(
            id = "PYQ_CONQUEROR",
            title = "UPSC PYQ Veteran",
            subtitle = "2018-2024 Prelims Master",
            iconEmoji = "📜",
            tier = "GOLD",
            requirement = "Complete the UPSC Prelims PYQ Pack",
            xpReward = 250
        )
    )

    fun getBadge(id: String): BadgeItem? = ALL.firstOrNull { it.id == id }
}

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val stateTag: String,
    val xp: Int,
    val level: Int,
    val accuracy: String,
    val streak: Int,
    val isCurrentUser: Boolean = false,
    val badgeEmoji: String = "🥇"
)

object LeaderboardData {
    val MOCK_LEADERBOARD = listOf(
        LeaderboardUser(1, "Aditi Sharma (AIR 1)", "Delhi", 3450, 35, "94.2%", 24, badgeEmoji = "👑"),
        LeaderboardUser(2, "Rahul Verma", "Uttar Pradesh", 3120, 32, "91.5%", 19, badgeEmoji = "🥇"),
        LeaderboardUser(3, "Priya Sundaram", "Tamil Nadu", 2890, 29, "89.0%", 15, badgeEmoji = "🥈"),
        LeaderboardUser(4, "Kavita Deshmukh", "Maharashtra", 2640, 27, "88.4%", 14, badgeEmoji = "🥉"),
        LeaderboardUser(5, "Amanpreet Singh", "Punjab", 2410, 25, "87.0%", 12, badgeEmoji = "⭐"),
        LeaderboardUser(6, "Debashish Roy", "West Bengal", 2250, 23, "86.1%", 11, badgeEmoji = "⭐"),
        LeaderboardUser(7, "Sneha Reddy", "Telangana", 2100, 21, "85.3%", 9, badgeEmoji = "⭐"),
        LeaderboardUser(8, "Vikas Meena", "Rajasthan", 1950, 20, "84.2%", 8, badgeEmoji = "⭐"),
        LeaderboardUser(9, "Ananya Jha", "Bihar", 1820, 19, "83.5%", 7, badgeEmoji = "⭐"),
        LeaderboardUser(10, "Karan Patel", "Gujarat", 1740, 18, "82.0%", 6, badgeEmoji = "⭐")
    )
}
