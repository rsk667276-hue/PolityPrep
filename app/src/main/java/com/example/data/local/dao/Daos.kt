package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.BookmarkedQuestionEntity
import com.example.data.local.entity.QuizAttemptEntity
import com.example.data.local.entity.SpacedRepetitionQuestionEntity
import com.example.data.local.entity.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizAttemptDao {
    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuizAttemptEntity>>

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentAttempts(limit: Int): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttemptEntity): Long

    @Query("DELETE FROM quiz_attempts")
    suspend fun clearHistory()
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarked_questions ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkedQuestionEntity>>

    @Query("SELECT questionId FROM bookmarked_questions")
    fun getAllBookmarkedIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkedQuestionEntity)

    @Query("DELETE FROM bookmarked_questions WHERE questionId = :questionId")
    suspend fun removeBookmark(questionId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_questions WHERE questionId = :questionId)")
    suspend fun isBookmarked(questionId: String): Boolean
}

@Dao
interface SpacedRepetitionDao {
    @Query("SELECT * FROM spaced_repetition_questions ORDER BY nextReviewTimestamp ASC")
    fun getAllSrsQuestions(): Flow<List<SpacedRepetitionQuestionEntity>>

    @Query("SELECT * FROM spaced_repetition_questions WHERE nextReviewTimestamp <= :currentTime ORDER BY nextReviewTimestamp ASC")
    fun getDueQuestions(currentTime: Long): Flow<List<SpacedRepetitionQuestionEntity>>

    @Query("SELECT COUNT(*) FROM spaced_repetition_questions WHERE nextReviewTimestamp <= :currentTime")
    fun getDueQuestionsCount(currentTime: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(question: SpacedRepetitionQuestionEntity)

    @Query("DELETE FROM spaced_repetition_questions WHERE questionId = :questionId")
    suspend fun removeQuestion(questionId: String)

    @Query("SELECT * FROM spaced_repetition_questions WHERE questionId = :questionId LIMIT 1")
    suspend fun getQuestionById(questionId: String): SpacedRepetitionQuestionEntity?
}

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE userId = 'local_user' LIMIT 1")
    fun getUserStatsFlow(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE userId = 'local_user' LIMIT 1")
    suspend fun getUserStats(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: UserStatsEntity)
}
