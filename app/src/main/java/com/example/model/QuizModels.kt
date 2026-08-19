package com.example.model

enum class DifficultyLevel(val label: String, val description: String, val badge: String) {
    FOUNDATION("Prelims Foundation", "Direct conceptual & factual questions for SSC / State PSCs / UPSC basics", "Easy"),
    UPSC_STANDARD("UPSC Standard", "Standard UPSC Prelims 2-statement & analytical questions", "Standard"),
    UPSC_ADVANCED("UPSC Advanced", "Complex 3-statement, Assertion-Reason, & Landmark Judgements", "Hard")
}

enum class QuizMode(val title: String, val description: String) {
    EXAM_MODE("UPSC Prelims Exam Mode", "Timer +2.0 marks, -0.66 negative marking penalty, OMR review sheet"),
    PRACTICE_MODE("Instant Learning Mode", "Instant answer reveal with Constitution Article references & rationale"),
    DAILY_CHALLENGE("Daily 5-Min Challenge", "5 high-yield questions daily to build your retention streak"),
    PYQ_PACK("Previous Year Questions (PYQs)", "Authentic questions from UPSC Prelims 2018-2024")
}

data class PolityCategory(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val articlesSpan: String,
    val iconName: String,
    val description: String,
    val weightageTag: String
)

object PolityCategories {
    val ALL = listOf(
        PolityCategory(
            id = "preamble_basic_structure",
            title = "Preamble & Basic Structure",
            hindiTitle = "प्रस्तावना एवं मूल ढांचा",
            articlesSpan = "Preamble, Kesavananda Bharati",
            iconName = "Balance",
            description = "Sovereign, Socialist, Secular, Democratic Republic, 42nd Amendment, Basic structure doctrine",
            weightageTag = "High Yield (1-2 Qs/yr)"
        ),
        PolityCategory(
            id = "fundamental_rights",
            title = "Fundamental Rights & Writs",
            hindiTitle = "मौलिक अधिकार एवं रिट",
            articlesSpan = "Articles 12 to 35",
            iconName = "Shield",
            description = "Right to Equality (14-18), Freedoms (19), Life & Privacy (21), Religion (25-28), Writs (Art 32/226)",
            weightageTag = "Core Focus (2-3 Qs/yr)"
        ),
        PolityCategory(
            id = "dpsp_duties",
            title = "DPSP & Fundamental Duties",
            hindiTitle = "नीति निर्देशक तत्व एवं मौलिक कर्तव्य",
            articlesSpan = "Articles 36 to 51A",
            iconName = "Bookmark",
            description = "Socialistic, Gandhian & Liberal-Intellectual principles, 86th & 42nd CAA, Swaran Singh Committee",
            weightageTag = "Crucial (1-2 Qs/yr)"
        ),
        PolityCategory(
            id = "parliament_executive",
            title = "Parliament & Union Executive",
            hindiTitle = "संसद एवं केंद्रीय कार्यपालिका",
            articlesSpan = "Articles 52 to 123",
            iconName = "AccountBalance",
            description = "President, PM & CoM, Lok Sabha, Rajya Sabha, Money Bills, Joint Sitting, Parliamentary Committees",
            weightageTag = "Highest Weightage (3-4 Qs/yr)"
        ),
        PolityCategory(
            id = "judiciary",
            title = "Judiciary & Landmark Cases",
            hindiTitle = "न्यायपालिका एवं प्रमुख निर्णय",
            articlesSpan = "Articles 124 to 147, 214-237",
            iconName = "Gavel",
            description = "Supreme Court, High Courts, Collegium System, Judicial Review, Curative Petition, Contempt of Court",
            weightageTag = "High Yield (2-3 Qs/yr)"
        ),
        PolityCategory(
            id = "federalism_emergency",
            title = "Federalism & Centre-State Relations",
            hindiTitle = "संघवाद एवं आपातकालीन उपबंध",
            articlesSpan = "Articles 245-300, 352, 356, 360",
            iconName = "Apartment",
            description = "Union/State/Concurrent Lists, Governor's powers, Financial Relations, National Emergency, President's Rule",
            weightageTag = "Frequent (1-2 Qs/yr)"
        ),
        PolityCategory(
            id = "bodies",
            title = "Constitutional & Statutory Bodies",
            hindiTitle = "संवैधानिक एवं सांविधिक निकाय",
            articlesSpan = "Art 324 (ECI), 280 (FC), 315 (UPSC), 148 (CAG)",
            iconName = "VerifiedUser",
            description = "Election Commission, CAG, UPSC, Finance Commission, Attorney General, NITI Aayog, NHRC, Lokpal",
            weightageTag = "Regular (2 Qs/yr)"
        ),
        PolityCategory(
            id = "amendments_schedules",
            title = "Amendments & 12 Schedules",
            hindiTitle = "संविधान संशोधन एवं अनुसूचियां",
            articlesSpan = "Article 368, Schedules 1-12",
            iconName = "HistoryEdu",
            description = "Major amendments (42nd, 44th, 73rd, 86th, 101st GST, 103rd EWS, 106th Nari Shakti), 10th Anti-Defection",
            weightageTag = "Core (2 Qs/yr)"
        ),
        PolityCategory(
            id = "panchayati_raj",
            title = "Panchayati Raj & Local Bodies",
            hindiTitle = "पंचायती राज एवं स्थानीय निकाय",
            articlesSpan = "Articles 243 to 243ZG",
            iconName = "NaturePeople",
            description = "73rd & 74th Amendments, Balwant Rai Mehta, 11th & 12th Schedules, State Finance Commission, PESA 1996",
            weightageTag = "Essential (1-2 Qs/yr)"
        ),
        PolityCategory(
            id = "current_polity",
            title = "Current Polity & 2024-26 Judgements",
            hindiTitle = "समसामयिक राजव्यवस्था एवं हालिया निर्णय",
            articlesSpan = "Recent Bills, Acts & SC Judgements",
            iconName = "Newspaper",
            description = "Electoral Bonds struck down, Art 370 Verdict, CEC Appointment Act, Sub-classification of SC/ST, Uniform Civil Code",
            weightageTag = "Trending (2-3 Qs/yr)"
        )
    )

    fun getCategoryById(id: String): PolityCategory {
        return ALL.firstOrNull { it.id == id } ?: ALL.first()
    }
}

data class QuizQuestion(
    val id: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val shortExplanation: String,
    val articleReference: String,
    val categoryId: String,
    val difficulty: DifficultyLevel = DifficultyLevel.UPSC_STANDARD,
    val yearTag: String? = null,
    val isAiGenerated: Boolean = false,
    val sourceVerified: Boolean = true
)

data class QuestionUserResponse(
    val question: QuizQuestion,
    val selectedOptionIndex: Int? = null,
    val isMarkedForReview: Boolean = false,
    val isVisited: Boolean = false
) {
    val isAttempted: Boolean get() = selectedOptionIndex != null
    val isCorrect: Boolean get() = selectedOptionIndex == question.correctOptionIndex
}

data class QuizSummary(
    val quizTitle: String,
    val categoryId: String,
    val mode: QuizMode,
    val difficulty: DifficultyLevel,
    val totalQuestions: Int,
    val attemptedCount: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val rawScore: Double, // UPSC Prelims: +2.0 for correct, -0.66 for incorrect
    val maxMarks: Double,
    val percentage: Double,
    val accuracyPercentage: Double,
    val timeSpentSeconds: Int,
    val userResponses: List<QuestionUserResponse>,
    val weakTopicsDetected: List<String> = emptyList()
)

data class RecommendedBook(
    val id: String,
    val title: String,
    val author: String,
    val subtitle: String,
    val badge: String,
    val price: String,
    val amazonAffiliateUrl: String,
    val rating: Double,
    val reviewCount: String,
    val relevantCategories: List<String>,
    val whyRecommended: String
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val price: String,
    val billingCycle: String,
    val isPopular: Boolean,
    val features: List<String>
)
