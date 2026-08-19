package com.example.data

import com.example.model.DifficultyLevel
import com.example.model.QuizQuestion
import com.example.model.RecommendedBook

object QuestionBank {

    val RECOMMENDED_BOOKS = listOf(
        RecommendedBook(
            id = "laxmikanth",
            title = "Indian Polity for UPSC & State Examinations",
            author = "M. Laxmikanth (7th Revised Edition)",
            subtitle = "The 'Bible' for UPSC Civil Services Prelims & Mains Polity",
            badge = "Must-Have Core Bible",
            price = "₹680 (32% OFF on Amazon)",
            amazonAffiliateUrl = "https://www.amazon.in/dp/9355325852?tag=polityprep-21",
            rating = 4.8,
            reviewCount = "24,800+ Ratings",
            relevantCategories = listOf("fundamental_rights", "parliament_executive", "dpsp_duties", "judiciary", "amendments_schedules"),
            whyRecommended = "Covers complete constitutional provisions, historical background, parliamentary procedures, tables and constitutional amendments in exhaustive detail."
        ),
        RecommendedBook(
            id = "dd_basu",
            title = "Introduction to the Constitution of India",
            author = "Dr. Durga Das Basu (26th Edition)",
            subtitle = "Authoritative judicial interpretation & constitutional law masterwork",
            badge = "Advanced Legal Rationale",
            price = "₹595 (25% OFF on Amazon)",
            amazonAffiliateUrl = "https://www.amazon.in/dp/9391211145?tag=polityprep-21",
            rating = 4.7,
            reviewCount = "8,400+ Ratings",
            relevantCategories = listOf("judiciary", "preamble_basic_structure", "federalism_emergency"),
            whyRecommended = "Essential for deep conceptual clarity on Supreme Court doctrines, judicial review, constitutionalism, and comparative federal systems."
        ),
        RecommendedBook(
            id = "subhash_kashyap",
            title = "Our Parliament & Our Constitution",
            author = "Dr. Subhash C. Kashyap (Former Sec-Gen Lok Sabha)",
            subtitle = "Deep dive into parliamentary functioning, motions & legislative procedures",
            badge = "Parliamentary Procedures Specialist",
            price = "₹420 on Amazon",
            amazonAffiliateUrl = "https://www.amazon.in/dp/8123701479?tag=polityprep-21",
            rating = 4.6,
            reviewCount = "5,100+ Ratings",
            relevantCategories = listOf("parliament_executive", "federalism_emergency"),
            whyRecommended = "Written by an authentic Lok Sabha authority detailing how motions, money bills, committees, and presiding officer rulings operate in real time."
        ),
        RecommendedBook(
            id = "pmf_ias",
            title = "PMF IAS Indian Polity with Mindmaps & Color Visuals",
            author = "Manjunath Thamminidi (PMF IAS)",
            subtitle = "Color-coded flowcharts, tabular comparisons & quick revision maps",
            badge = "Visual Mindmap & PYQ Mapping",
            price = "₹540 on Amazon",
            amazonAffiliateUrl = "https://www.amazon.in/dp/9357463004?tag=polityprep-21",
            rating = 4.7,
            reviewCount = "3,900+ Ratings",
            relevantCategories = listOf("bodies", "panchayati_raj", "current_polity", "amendments_schedules"),
            whyRecommended = "Outstanding visual summaries, clear distinction between Constitutional vs Statutory vs Regulatory bodies, and recent acts."
        )
    )

    val PRELOADED_QUESTIONS: List<QuizQuestion> = listOf(
        // Preamble & Basic Structure
        QuizQuestion(
            id = "pre_01",
            questionText = "With reference to the Preamble of the Constitution of India, consider the following statements:\n1. The Preamble is an integral part of the Constitution.\n2. The Preamble is directly enforceable in a court of law.\n3. The words 'Socialist', 'Secular', and 'Integrity' were added to the Preamble by the 42nd Constitutional Amendment Act, 1976.\n\nWhich of the statements given above is/are correct?",
            options = listOf(
                "1 and 2 only",
                "1 and 3 only",
                "3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 1,
            shortExplanation = "In Kesavananda Bharati (1973) and LIC of India (1995), the SC ruled Preamble is an integral part of the Constitution. However, it is non-justiciable (not directly enforceable in courts). 42nd CAA 1976 added 'Socialist', 'Secular', and 'Integrity'.",
            articleReference = "Preamble & Kesavananda Bharati Case (1973)",
            categoryId = "preamble_basic_structure",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2020",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "pre_02",
            questionText = "Which one of the following factors constitutes the best safeguard of liberty in a liberal democracy?",
            options = listOf(
                "A committed judiciary",
                "Centralization of powers",
                "Elected government",
                "Separation of powers"
            ),
            correctOptionIndex = 3,
            shortExplanation = "Separation of powers (between Legislature, Executive, and Judiciary) prevents concentration of arbitrary authority in one organ, serving as the bedrock safeguard of individual liberty against tyranny.",
            articleReference = "Article 50 (DPSP) & Basic Structure Doctrine",
            categoryId = "preamble_basic_structure",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2021",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "pre_03",
            questionText = "In which landmark judgement did the Supreme Court of India hold that the 'Basic Structure' of the Constitution cannot be altered or destroyed even by a constitutional amendment under Article 368?",
            options = listOf(
                "Golaknath v. State of Punjab (1967)",
                "Kesavananda Bharati v. State of Kerala (1973)",
                "Minerva Mills v. Union of India (1980)",
                "Maneka Gandhi v. Union of India (1978)"
            ),
            correctOptionIndex = 1,
            shortExplanation = "A historic 13-judge constitutional bench ruled in Kesavananda Bharati (1973) by a 7-6 majority that Parliament has wide amending powers under Art 368, but cannot alter the 'Basic Structure' of the Constitution.",
            articleReference = "Article 368 & Basic Structure Doctrine",
            categoryId = "preamble_basic_structure",
            difficulty = DifficultyLevel.FOUNDATION,
            yearTag = "UPSC / SSC CGL Classic",
            sourceVerified = true
        ),

        // Fundamental Rights
        QuizQuestion(
            id = "fr_01",
            questionText = "Which of the following Fundamental Rights under the Indian Constitution is/are available ONLY to Indian citizens and NOT to foreigners?\n1. Prohibition of discrimination on grounds of religion, race, caste, sex or place of birth\n2. Equality of opportunity in matters of public employment\n3. Protection of life and personal liberty\n4. Protection of language, script and culture of minorities\n\nSelect the correct answer using the code given below:",
            options = listOf(
                "1, 2 and 4 only",
                "1 and 2 only",
                "2, 3 and 4 only",
                "1, 2, 3 and 4"
            ),
            correctOptionIndex = 0,
            shortExplanation = "Articles 15, 16, 19, 29, and 30 are available exclusively to Indian citizens. Article 21 (Protection of life & personal liberty) and Article 14 (Equality before law) are available to both citizens and foreigners (except enemy aliens).",
            articleReference = "Articles 15, 16, 21, 29 & 30",
            categoryId = "fundamental_rights",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims High-Yield",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "fr_02",
            questionText = "Consider the following statements regarding the Writ of 'Quo-Warranto':\n1. It is issued to prevent illegal usurpation of a public office by a person.\n2. Unlike other writs, it can be sought even by an interested person who is not personally aggrieved.\n3. It cannot be issued in case of a ministerial office or private office.\n\nWhich of the statements given above are correct?",
            options = listOf(
                "1 and 2 only",
                "2 and 3 only",
                "1 and 3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 3,
            shortExplanation = "All 3 statements are correct. Quo-Warranto (By what authority?) inquires into legality of claim to public office. Locus standi is relaxed (anyone can petition). It applies only to substantive public offices created by statute/Constitution, not ministerial or private offices.",
            articleReference = "Article 32 & Article 226 (Writs)",
            categoryId = "fundamental_rights",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims 2022",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "fr_03",
            questionText = "A legislation which confers on the executive or administrative authority an unguided and uncontrolled discretionary power in the matter of application of law violates which one of the following Articles of the Constitution of India?",
            options = listOf(
                "Article 14",
                "Article 28",
                "Article 32",
                "Article 44"
            ),
            correctOptionIndex = 0,
            shortExplanation = "Article 14 embodies the Rule of Law and non-arbitrariness. Any unguided, uncanalized discretionary power conferred on executive without proper guidelines is arbitrary and strikes at the heart of Article 14.",
            articleReference = "Article 14 (Right to Equality & Non-arbitrariness)",
            categoryId = "fundamental_rights",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2021",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "fr_04",
            questionText = "In India, 'Right to Privacy' is protected as an intrinsic part of the right to life and personal liberty under which Article of the Constitution?",
            options = listOf(
                "Article 14",
                "Article 19",
                "Article 21",
                "Article 25"
            ),
            correctOptionIndex = 2,
            shortExplanation = "In Justice K.S. Puttaswamy (Retd.) v. Union of India (2017), a unanimous 9-judge Constitution Bench held that the Right to Privacy is a Fundamental Right guaranteed under Article 21 and Part III of the Constitution.",
            articleReference = "Article 21 & K.S. Puttaswamy (2017)",
            categoryId = "fundamental_rights",
            difficulty = DifficultyLevel.FOUNDATION,
            yearTag = "UPSC Prelims 2018 / 2021",
            sourceVerified = true
        ),

        // DPSP & Fundamental Duties
        QuizQuestion(
            id = "dp_01",
            questionText = "Which of the following Directive Principles of State Policy was/were added to the Constitution by the 42nd Constitutional Amendment Act, 1976?\n1. Equal justice and free legal aid\n2. Participation of workers in the management of industries\n3. Protection and improvement of environment and safeguarding of forests and wild life\n4. Promotion of cooperative societies\n\nSelect the correct answer using the code given below:",
            options = listOf(
                "1, 2 and 3 only",
                "1 and 3 only",
                "2 and 4 only",
                "1, 2, 3 and 4"
            ),
            correctOptionIndex = 0,
            shortExplanation = "The 42nd Amendment (1976) added Article 39A (Free legal aid), Article 43A (Participation of workers in management), and Article 48A (Environment & wildlife). Promotion of cooperative societies (Art 43B) was added by the 97th CAA, 2011.",
            articleReference = "Articles 39A, 43A, 48A (42nd CAA) & Art 43B (97th CAA)",
            categoryId = "dpsp_duties",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Core Standard",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "dp_02",
            questionText = "Under the Indian Constitution, concentration of wealth violates which of the following?",
            options = listOf(
                "The Right to Equality",
                "The Directive Principles of State Policy",
                "The Right to Freedom",
                "The Concept of Welfare"
            ),
            correctOptionIndex = 1,
            shortExplanation = "Article 39(c) of DPSP directs the State to ensure that the operation of the economic system does not result in the concentration of wealth and means of production to the common detriment.",
            articleReference = "Article 39(c) - DPSP (Part IV)",
            categoryId = "dpsp_duties",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2021",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "dp_03",
            questionText = "Which constitutional amendment introduced the 11th Fundamental Duty (providing opportunities for education to child between 6 to 14 years) under Article 51A(k)?",
            options = listOf(
                "42nd Constitutional Amendment Act, 1976",
                "44th Constitutional Amendment Act, 1978",
                "86th Constitutional Amendment Act, 2002",
                "91st Constitutional Amendment Act, 2003"
            ),
            correctOptionIndex = 2,
            shortExplanation = "The 86th CAA 2002 inserted Article 21A (Right to Education as FR), amended Article 45 in DPSP, and added the 11th Fundamental Duty under Article 51A(k) for parent/guardian.",
            articleReference = "Article 51A(k) & 86th CAA, 2002",
            categoryId = "dpsp_duties",
            difficulty = DifficultyLevel.FOUNDATION,
            yearTag = "UPSC / SSC Prelims Core",
            sourceVerified = true
        ),

        // Parliament & Union Executive
        QuizQuestion(
            id = "par_01",
            questionText = "Consider the following statements with reference to the Money Bill in the Parliament of India:\n1. A Money Bill can only be introduced in the Lok Sabha with the prior recommendation of the President.\n2. If any question arises whether a Bill is a Money Bill or not, the decision of the Speaker of the Lok Sabha thereon is final.\n3. The Rajya Sabha can withhold or reject a Money Bill for a maximum period of 6 months.\n\nWhich of the statements given above is/are correct?",
            options = listOf(
                "1 and 2 only",
                "2 and 3 only",
                "1 and 3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 0,
            shortExplanation = "Statements 1 and 2 are correct. Statement 3 is incorrect because the Rajya Sabha has only 14 days to return a Money Bill with or without recommendations. If not returned within 14 days, it is deemed passed by both Houses.",
            articleReference = "Articles 109 & 110 (Definition & Special Procedure for Money Bills)",
            categoryId = "parliament_executive",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2018 / 2023",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "par_02",
            questionText = "Consider the following statements regarding the Ordinance-making power of the President under Article 123:\n1. The President can promulgate an Ordinance only when both Houses of Parliament are not in session.\n2. An Ordinance has the same force and effect as an Act of Parliament.\n3. Every Ordinance must be laid before both Houses and ceases to operate at the expiration of 6 weeks from the reassembly of Parliament.\n\nWhich of the statements given above are correct?",
            options = listOf(
                "1 and 2 only",
                "2 and 3 only",
                "1 and 3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 1,
            shortExplanation = "Statement 1 is incorrect because an Ordinance can be promulgated if EITHER of the two Houses is not in session (since a bill requires both houses to pass). Statements 2 and 3 are correct under Art 123.",
            articleReference = "Article 123 (Legislative Powers of the President)",
            categoryId = "parliament_executive",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims 2015 / 2022",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "par_03",
            questionText = "With reference to the Union Executive, consider the following statements regarding the Council of Ministers:\n1. The total number of Ministers, including the Prime Minister, in the Council of Ministers shall not exceed 15 percent of the total number of members of the Lok Sabha.\n2. The Ministers hold office during the pleasure of the President.\n3. The Council of Ministers is collectively responsible to the Parliament.\n\nWhich of the statements given above is/are correct?",
            options = listOf(
                "1 and 2 only",
                "2 and 3 only",
                "1 and 3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 0,
            shortExplanation = "Statement 1 is correct (91st CAA 2003, Art 75(1A)). Statement 2 is correct (Art 75(2)). Statement 3 is incorrect because the Council of Ministers is collectively responsible specifically to the LOK SABHA (House of the People), not the entire Parliament (Art 75(3)).",
            articleReference = "Article 75 & 91st Constitutional Amendment Act",
            categoryId = "parliament_executive",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims 2022",
            sourceVerified = true
        ),

        // Judiciary & Landmark Cases
        QuizQuestion(
            id = "jud_01",
            questionText = "With reference to the Supreme Court of India, consider the following statements:\n1. Under Article 142, the Supreme Court in the exercise of its jurisdiction may pass such decree or make such order as is necessary for doing complete justice in any cause or matter.\n2. The power of Judicial Review in India is explicitly defined in Article 13 of the Constitution.\n3. The Curative Petition was evolved by the Supreme Court in the Rupa Ashok Hurra case (2002).\n\nWhich of the statements given above are correct?",
            options = listOf(
                "1 and 2 only",
                "1 and 3 only",
                "2 and 3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 3,
            shortExplanation = "All 3 are correct. Art 142 empowers SC to do complete justice. Art 13 provides the bedrock for Judicial Review by declaring laws inconsistent with FRs void. Curative petition was created in Rupa Ashok Hurra (2002) after dismissal of review petition.",
            articleReference = "Articles 13, 142 & Rupa Ashok Hurra v. Ashok Hurra (2002)",
            categoryId = "judiciary",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims 2019 / 2024",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "jud_02",
            questionText = "Who among the following was the Chief Justice of India when Public Interest Litigation (PIL) was introduced to the Indian judicial system?",
            options = listOf(
                "Justice M. Hidayatullah",
                "Justice A.M. Ahmadi",
                "Justice P.N. Bhagwati",
                "Justice V.R. Krishna Iyer"
            ),
            correctOptionIndex = 2,
            shortExplanation = "Justice P.N. Bhagwati along with Justice V.R. Krishna Iyer pioneered PIL in India during late 1970s and 1980s. P.N. Bhagwati served as the 17th Chief Justice of India during its institutionalization.",
            articleReference = "Article 32, SP Gupta Case (1981) & PIL Doctrine",
            categoryId = "judiciary",
            difficulty = DifficultyLevel.FOUNDATION,
            yearTag = "UPSC Prelims Classic",
            sourceVerified = true
        ),

        // Constitutional & Statutory Bodies
        QuizQuestion(
            id = "bod_01",
            questionText = "Consider the following statements regarding the Comptroller and Auditor General of India (CAG):\n1. The CAG is appointed by the President by warrant under his hand and seal.\n2. The CAG is removed in the like manner and on the like grounds as a Judge of the Supreme Court.\n3. The CAG is eligible for further office under the Government of India or the Government of any State after ceasing to hold office.\n\nWhich of the statements given above is/are correct?",
            options = listOf(
                "1 and 2 only",
                "2 and 3 only",
                "1 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 0,
            shortExplanation = "Statements 1 & 2 are correct (Article 148). Statement 3 is incorrect because under Article 148(4), the CAG is strictly NOT eligible for further office either under the Government of India or the Government of any State to safeguard independence.",
            articleReference = "Article 148 (Comptroller and Auditor-General of India)",
            categoryId = "bodies",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims Standard",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "bod_02",
            questionText = "Which one of the following is a NON-CONSTITUTIONAL (Statutory) body in India?",
            options = listOf(
                "National Commission for Scheduled Castes (NCSC)",
                "National Commission for Scheduled Tribes (NCST)",
                "National Commission for Backward Classes (NCBC)",
                "National Human Rights Commission (NHRC)"
            ),
            correctOptionIndex = 3,
            shortExplanation = "NCSC (Art 338), NCST (Art 338A), and NCBC (Art 338B via 102nd CAA) are constitutional bodies. NHRC was established by an act of Parliament (Protection of Human Rights Act, 1993) and is a statutory body.",
            articleReference = "Articles 338, 338A, 338B vs Protection of Human Rights Act 1993",
            categoryId = "bodies",
            difficulty = DifficultyLevel.FOUNDATION,
            yearTag = "UPSC Prelims 2019",
            sourceVerified = true
        ),

        // Amendments & Schedules
        QuizQuestion(
            id = "amend_01",
            questionText = "Consider the following pairs of Constitutional Amendments and their subjects:\n1. 101st Amendment Act — Goods and Services Tax (GST)\n2. 103rd Amendment Act — 10% Reservation for Economically Weaker Sections (EWS)\n3. 106th Amendment Act — 33% Reservation for Women in Lok Sabha and State Legislative Assemblies\n\nWhich of the pairs given above are correctly matched?",
            options = listOf(
                "1 and 2 only",
                "2 and 3 only",
                "1 and 3 only",
                "1, 2 and 3"
            ),
            correctOptionIndex = 3,
            shortExplanation = "All 3 pairs are correctly matched. 101st CAA (2016) introduced GST. 103rd CAA (2019) introduced 10% EWS quota (Articles 15(6) & 16(6)). 106th CAA (Nari Shakti Vandan Adhiniyam, 2023) provides 33% women's reservation in Lok Sabha and State Assemblies.",
            articleReference = "Articles 279A, 15(6), 16(6), 239AA, 330A, 332A",
            categoryId = "amendments_schedules",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2024 Hot Topic",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "amend_02",
            questionText = "Under the Tenth Schedule (Anti-Defection Law) of the Constitution of India, who is the deciding authority regarding the disqualification of a Member of Parliament on grounds of defection?",
            options = listOf(
                "The President of India acting on the advice of the Election Commission",
                "The Chairman of Rajya Sabha or Speaker of Lok Sabha",
                "The Supreme Court of India directly",
                "The Election Commission of India"
            ),
            correctOptionIndex = 1,
            shortExplanation = "Paragraph 6 of the 10th Schedule specifies that the question of disqualification on ground of defection is decided by the Chairman (Rajya Sabha) or Speaker (Lok Sabha). In Kihoto Hollohan (1992), SC held this decision is subject to judicial review.",
            articleReference = "Tenth Schedule (52nd & 91st CAA) & Kihoto Hollohan (1992)",
            categoryId = "amendments_schedules",
            difficulty = DifficultyLevel.FOUNDATION,
            yearTag = "UPSC / SSC Core Classic",
            sourceVerified = true
        ),

        // Panchayati Raj
        QuizQuestion(
            id = "pan_01",
            questionText = "Which of the following provisions are COMPULSORY (Mandatory) under the 73rd Constitutional Amendment Act, 1992?\n1. Organization of Gram Sabha in a village or group of villages.\n2. Reservation of not less than one-third of total seats for women.\n3. Granting financial powers to the Panchayats to levy, collect and appropriate taxes.\n4. Establishment of a State Finance Commission every five years.\n\nSelect the correct answer using the code given below:",
            options = listOf(
                "1, 2 and 4 only",
                "1, 2 and 3 only",
                "2 and 4 only",
                "1, 2, 3 and 4"
            ),
            correctOptionIndex = 0,
            shortExplanation = "Compulsory provisions include: Gram Sabha, 3-tier system (except states <20 lakh population), 1/3rd women reservation, 5-year fixed tenure, State Election Commission, and State Finance Commission. Giving tax levying powers (3) is a VOLUNTARY provision left to state legislature discretion.",
            articleReference = "Articles 243A, 243D, 243H, 243I (73rd CAA, 1992)",
            categoryId = "panchayati_raj",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims Standard",
            sourceVerified = true
        ),

        // Current Polity & Landmark Judgements
        QuizQuestion(
            id = "cur_01",
            questionText = "In Association for Democratic Reforms (ADR) v. Union of India (February 2024), on what primary constitutional grounds did the 5-judge Constitution Bench strike down the Electoral Bonds Scheme, 2018?",
            options = listOf(
                "Violation of the Right to Information under Article 19(1)(a)",
                "Violation of Freedom of Trade and Commerce under Article 301",
                "Violation of Financial Federalism under Article 280",
                "Violation of Freedom of Religion under Article 25"
            ),
            correctOptionIndex = 0,
            shortExplanation = "The Supreme Court unanimously held that anonymous political contributions through Electoral Bonds violate the voter's Fundamental Right to Information under Article 19(1)(a), which is essential for participatory democracy and free & fair elections.",
            articleReference = "Article 19(1)(a) & ADR v. Union of India (2024)",
            categoryId = "current_polity",
            difficulty = DifficultyLevel.UPSC_STANDARD,
            yearTag = "UPSC Prelims 2024/2025 Recent Landmark",
            sourceVerified = true
        ),
        QuizQuestion(
            id = "cur_02",
            questionText = "In State of Punjab v. Davinder Singh (August 2024), a 7-judge Constitution Bench of the Supreme Court of India ruled by a 6:1 majority on which significant constitutional issue?",
            options = listOf(
                "States have constitutional authority to sub-classify Scheduled Castes (SCs) and Scheduled Tribes (STs) for more targeted affirmative action",
                "Introduction of creamy layer concept in General Category quotas",
                "Mandatory 50% reservation ceiling is abolished completely",
                "Governor can pocket veto state bills indefinitely"
            ),
            correctOptionIndex = 0,
            shortExplanation = "The 7-judge bench led by CJI overruled EV Chinnaiah (2004) and held that SCs/STs are not a homogenous group; State Governments have legislative competence under Art 15(4) & 16(4) to sub-classify SC/STs based on empirical backwardness without violating Art 341.",
            articleReference = "Articles 14, 15(4), 16(4), 341 & Davinder Singh Judgement (2024)",
            categoryId = "current_polity",
            difficulty = DifficultyLevel.UPSC_ADVANCED,
            yearTag = "UPSC Prelims 2025/2026 Landmark",
            sourceVerified = true
        )
    )

    fun getQuestionsForCategory(categoryId: String): List<QuizQuestion> {
        val categoryQuestions = PRELOADED_QUESTIONS.filter { it.categoryId == categoryId }
        return if (categoryQuestions.isNotEmpty()) categoryQuestions else PRELOADED_QUESTIONS.shuffled().take(5)
    }

    fun getDailyChallengeQuestions(): List<QuizQuestion> {
        return PRELOADED_QUESTIONS.shuffled().take(5)
    }

    fun getPyqPackQuestions(): List<QuizQuestion> {
        return PRELOADED_QUESTIONS.filter { it.yearTag != null }
    }
}
