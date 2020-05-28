package dev.favier.exam1radioamateur;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Question.class}, exportSchema = false, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String dbName = "Questions_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract QuestionDao questionDao();

}
