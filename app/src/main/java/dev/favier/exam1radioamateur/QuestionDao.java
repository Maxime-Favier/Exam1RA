package dev.favier.exam1radioamateur;

import androidx.room.*;

import java.util.List;

@androidx.room.Dao
public interface QuestionDao {

    @Query("SELECT * from Questions")
    List<Question> getAllQuestions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> questions);

    @Update
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);

    @Query("DELETE FROM Questions")
    void clearQuestions();

    @Query("SELECT * FROM Questions WHERE themeId = :theme ORDER BY RANDOM() LIMIT :limit")
    List<Question> getRandomQuestion(int theme, int limit);


}

