package dev.favier.exam1radioamateur;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface QuestionDao {

    @Query("SELECT * from Questions")
    public List<Question> getAllQuestions();

    @Insert
    void insertQuestion(Question question);

    @Update
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);

    @Query("DELETE FROM Questions")
    void clearQuestions();

    @Query("SELECT * FROM Questions WHERE themeId = :theme ORDER BY RANDOM() LIMIT :limit")
    public List<Question> getRandomQuestion(int theme, int limit);


}

