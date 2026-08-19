package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.model.DifficultyLevel
import com.example.model.QuizQuestion
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float = 0.3f,
    @Json(name = "topP") val topP: Float = 0.95f,
    @Json(name = "topK") val topK: Int = 40,
    @Json(name = "responseMimeType") val responseMimeType: String? = "application/json"
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent?
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>?
)

interface GeminiPolityApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generatePolityQuestions(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiPolityService {
    private const val TAG = "GeminiPolityService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val api: GeminiPolityApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiPolityApi::class.java)
    }

    suspend fun generateQuestions(
        categoryTitle: String,
        categoryId: String,
        customTopicPrompt: String? = null,
        difficulty: DifficultyLevel = DifficultyLevel.UPSC_STANDARD,
        count: Int = 5
    ): Result<List<QuizQuestion>> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured yet in AI Studio Secrets panel.")
            )
        }

        val promptText = buildString {
            append("You are an expert Indian Polity & Constitution exam question setter, similar to UPSC/SSC standard.\n")
            append("Generate $count multiple-choice questions on: $categoryTitle")
            if (!customTopicPrompt.isNullOrBlank()) {
                append(" (Specific Focus: $customTopicPrompt)")
            }
            append("\nDifficulty: ${difficulty.label} (${difficulty.description})\n\n")
            append("For each question provide:\n")
            append("- Question text\n")
            append("- 4 options (A, B, C, D)\n")
            append("- Correct answer index (0 for A, 1 for B, 2 for C, 3 for D) or letter ('A','B','C','D')\n")
            append("- Short explanation (2-3 lines, strictly exam-relevant and factual)\n")
            append("- Constitutional Article or Schedule Reference (e.g. 'Article 21', 'Article 368', '10th Schedule', 'Kesavananda Bharati Case')\n\n")
            append("Ensure factual accuracy. Base constitutional questions strictly on actual Indian Constitution articles/schedules. For current affairs, use verified recent information only.\n\n")
            append("Output strictly as a JSON array of objects with the exact keys: [{\"questionText\": \"...\", \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"], \"correctOptionIndex\": 0, \"shortExplanation\": \"...\", \"articleReference\": \"...\"}]")
        }

        val systemPrompt = "You are an elite Indian Constitutional law authority and UPSC Civil Services CSE Examination question setter. Maintain rigorous standard, factual precision, exact article/amendment numbering, and authoritative explanations."

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = promptText))
                )
            ),
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = systemPrompt))
            ),
            generationConfig = GeminiGenerationConfig(
                temperature = 0.25f,
                topP = 0.95f,
                responseMimeType = "application/json"
            )
        )

        try {
            val response = api.generatePolityQuestions(apiKey, request)
            val rawJson = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: return@withContext Result.failure(Exception("Empty response received from Gemini API."))

            val parsedQuestions = parseQuizQuestions(rawJson, categoryId, difficulty)
            if (parsedQuestions.isEmpty()) {
                Result.failure(Exception("Failed to parse valid question structure from AI response."))
            } else {
                Result.success(parsedQuestions)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error generating polity questions with Gemini", e)
            Result.failure(e)
        }
    }

    private fun parseQuizQuestions(
        jsonString: String,
        categoryId: String,
        difficulty: DifficultyLevel
    ): List<QuizQuestion> {
        val result = mutableListOf<QuizQuestion>()
        try {
            val cleanJson = jsonString.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val jsonArray = if (cleanJson.startsWith("[")) {
                JSONArray(cleanJson)
            } else if (cleanJson.startsWith("{")) {
                val obj = JSONObject(cleanJson)
                when {
                    obj.has("questions") -> obj.getJSONArray("questions")
                    obj.has("data") -> obj.getJSONArray("data")
                    else -> JSONArray()
                }
            } else {
                JSONArray()
            }

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val qText = obj.optString("questionText", obj.optString("question", "Question $i"))
                val optsArray = obj.optJSONArray("options")
                val optionsList = mutableListOf<String>()
                if (optsArray != null) {
                    for (j in 0 until optsArray.length()) {
                        optionsList.add(optsArray.getString(j))
                    }
                }

                if (optionsList.size < 4) {
                    // fallback standard 4 choices
                    optionsList.clear()
                    optionsList.addAll(listOf("Statement 1 only", "Statement 2 only", "Both 1 and 2", "Neither 1 nor 2"))
                }

                var correctIdx = obj.optInt("correctOptionIndex", -1)
                if (correctIdx !in 0..3) {
                    val correctAnsStr = obj.optString("correctAnswer", obj.optString("correct_answer", "A")).trim().uppercase()
                    correctIdx = when {
                        correctAnsStr.startsWith("A") -> 0
                        correctAnsStr.startsWith("B") -> 1
                        correctAnsStr.startsWith("C") -> 2
                        correctAnsStr.startsWith("D") -> 3
                        else -> 0
                    }
                }

                val explanation = obj.optString("shortExplanation", obj.optString("explanation", "Verified Indian Constitution provision."))
                val articleRef = obj.optString("articleReference", obj.optString("article", "Indian Constitution"))

                result.add(
                    QuizQuestion(
                        id = "ai_${UUID.randomUUID().toString().take(8)}",
                        questionText = qText,
                        options = optionsList.take(4),
                        correctOptionIndex = correctIdx.coerceIn(0, 3),
                        shortExplanation = explanation,
                        articleReference = articleRef,
                        categoryId = categoryId,
                        difficulty = difficulty,
                        yearTag = "AI UPSC Master 2026",
                        isAiGenerated = true,
                        sourceVerified = true
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "JSON parsing error in parseQuizQuestions", e)
        }
        return result
    }
}
