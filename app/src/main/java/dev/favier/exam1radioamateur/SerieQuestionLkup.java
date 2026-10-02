package dev.favier.exam1radioamateur;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "serie_question_cross_ref",
        primaryKeys = {"serieNum", "questionUid"},
        foreignKeys = {
                @ForeignKey(
                        entity = SerieEntity.class,
                        parentColumns = "num",        // Clé primaire de SerieEntity
                        childColumns = "serieNum",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Question.class,
                        parentColumns = "uid",        // Clé primaire unique de Question
                        childColumns = "questionUid",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("serieNum"),
                @Index("questionUid")
        }
)
public class SerieQuestionLkup {
    private int serieNum;
    private int questionUid;

    public SerieQuestionLkup(int serieNum, int questionUid) {
        this.serieNum = serieNum;
        this.questionUid = questionUid;
    }

    public int getSerieNum() { return serieNum; }
    public void setSerieNum(int serieNum) { this.serieNum = serieNum; }

    public int getQuestionUid() { return questionUid; }
    public void setQuestionUid(int questionUid) { this.questionUid = questionUid; }
}
