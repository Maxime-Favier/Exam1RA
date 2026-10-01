package dev.favier.exam1radioamateur;

import androidx.room.*;

import java.util.List;
@Dao
public interface SerieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSeries(List<SerieEntity> series);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCrossRefs(List<SerieQuestionLkup> crossRefsSQ);

    @Transaction
    @Query("SELECT * FROM series WHERE nom = :nom LIMIT 1")
    SerieWithQuestions getSerieWithQuestions(String nom);

    @Transaction
    @Query("SELECT * FROM series ORDER BY num ASC")
    List<SerieWithQuestions> getAllSeriesWithQuestions();

    // Supprime toutes les séries
    @Query("DELETE FROM series")
    void deleteAllSeries();

    // Supprime toutes les liaisons séries questions
    @Query("DELETE FROM serie_question_cross_ref")
    void deleteAllCrossRefs();

    @Transaction
    default void clearAllSeriesData() {
        // Pour vider en une transaction
        deleteAllCrossRefs();
        deleteAllSeries();
    }
}
