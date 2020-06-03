package dev.favier.exam1radioamateur;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * rempli la base de donne de question
 */
public class DbPopulator {

    AppDatabase appDb;
    Context context;

    public DbPopulator(Context context) throws IOException, JSONException {
        this.context = context;
        appDb = AppDatabase.getInstance(context);
        populateDbFromJson();
    }

    /**
     * Ajoute les questions dans la bdd depuis un json
     * @throws IOException
     * @throws JSONException
     */
    public void populateDbFromJson() throws IOException, JSONException {

        //AppDatabase appDb = AppDatabase.getInstance(context);
        appDb.questionDao().clearQuestions();
        InputStream is = context.getResources().openRawResource(R.raw.questions);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }

        JSONObject mainJSObject = new JSONObject(writer.toString());
        JSONArray questionsJsonObject = mainJSObject.getJSONArray("questions");

        Question question;

        for (int i = 0; i < questionsJsonObject.length(); i++) {
            JSONObject questionObj = questionsJsonObject.getJSONObject(i);

            question = new Question();
            question.setNumero(questionObj.getInt("num"));
            question.setQuestion(questionObj.getString("question"));

            JSONArray propositionArray = questionObj.getJSONArray("propositions");
            for (int j = 0; j < propositionArray.length(); j++) {
                question.addProposition(propositionArray.getString(j));
            }

            question.setReponse(questionObj.getInt("reponse"));
            question.setThemeID(questionObj.getInt("themeNum"));
            question.setCommentaire(questionObj.getString("commentaire"));
            question.setCoursUrl(questionObj.getString("cours"));

            //question.demo();

            appDb.questionDao().insertQuestion(question);
        }

        //appDb.close();

    }
}
