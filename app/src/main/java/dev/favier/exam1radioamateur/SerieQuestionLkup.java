package dev.favier.exam1radioamateur;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "serie_question_cross_ref",
        primaryKeys = {"serieNum", "questionNumero"},
        indices = {@Index("questionNumero")}
)
public class SerieQuestionLkup {
    private int serieNum;
    private int questionNumero; // Correspond au champ imgNum de Question

    public SerieQuestionLkup(int serieNum, int questionNumero) {
        this.serieNum = serieNum;
        this.questionNumero = questionNumero;
    }

    public int getSerieNum() { return serieNum; }
    public void setSerieNum(int serieNum) { this.serieNum = serieNum; }

    public int getQuestionNumero() { return questionNumero; }
    public void setQuestionNumero(int questionNumero) { this.questionNumero = questionNumero; }
}

