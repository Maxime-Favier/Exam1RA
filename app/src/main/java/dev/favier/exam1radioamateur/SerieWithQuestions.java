package dev.favier.exam1radioamateur;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class SerieWithQuestions {
    @Embedded
    public SerieEntity serieEntity;

    @Relation(
            parentColumn = "num",
            entityColumn = "uid",
            associateBy = @Junction(
                    value = SerieQuestionLkup.class,
                    parentColumn = "serieNum",
                    entityColumn = "questionUid"
            )
    )
    public List<Question> questions;
}
