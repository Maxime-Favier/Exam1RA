package dev.favier.exam1radioamateur;

import android.content.Context;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class Examen {
    /**
     * Gere toute la logique relatif à l'examen
     */
    private ArrayList<Question> questions; // la liste de questions posés à l'examen
    private ArrayList<Integer> themesList; // la liste des themes
    private Context context;

    AppDatabase appDb;

    // les settings de l'examen
    private boolean malusEnable; // active le malus de -1 à chaque question fausse
    private int nbrQuestionParTheme; // le nombre de question de l'examen pour chaque theme
    private boolean showReponse; // active l'affichage des réponses
    private boolean timerEnable; // active le compte à rebour
    private int timer; // le temps en seconde restant

    // les themes de l'examen
    public static final int codeCouleurs = 201;
    public static final int groupementsDeResistances = 202;
    public static final int diodesEtTransistors = 203;
    public static final int synoptiques = 204;
    public static final int etagesRF = 205;
    public static final int electriciteDeBase = 206;
    public static final int courantsAlternatifs = 207;
    public static final int condensateursetBobines = 208;
    public static final int transformateursAmpli = 209;
    public static final int ligneDeTransmis = 210;
    public static final int classesEmission = 301;
    public static final int indicatifs = 302;
    public static final int codeQ = 303;
    public static final int epellation = 304;
    public static final int questionsEntrainement = 305;
    public static final int sanctions = 306;
    public static final int exposition = 307;
    public static final int longueurOnde = 308;
    public static final int adaptation = 309;
    public static final int cem = 310;

    public Examen(Context context, ArrayList<Integer> themesList, int nbrQuestionParTheme, boolean malusEnable) {
        this.context = context;
        this.themesList = themesList;
        this.nbrQuestionParTheme = nbrQuestionParTheme;
        this.malusEnable = malusEnable;
        questions = new ArrayList<>();
        appDb = AppDatabase.getInstance(context);
    }

    /**
     * retourne l'obj de calcul de résulatats
     * @return
     */
    public ResultCalculator getResults(){
        return new ResultCalculator(questions, themesList, nbrQuestionParTheme, malusEnable);
    }

    /**
     * Ajoute les questions dans la bdd depuis un json
     * @throws IOException
     * @throws JSONException
     */
    /*public void populateDbFromJson() throws IOException, JSONException {

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

    }*/

    /**
     * Genène les question de l'examen aléatoirement
     */
    public void genrateQuestions() {
        //Log.w("debug", String.valueOf(nbrQuestionParTheme));
        for (int theme : themesList) {
            //Log.w("debug", String.valueOf(theme));
            questions.addAll(appDb.questionDao().getRandomQuestion(theme, nbrQuestionParTheme));
        }
        Log.w("debug", "nbr de questions " + String.valueOf(questions.size()));
    }

    /*public void debug() {
        ArrayList<Question> tes = new ArrayList<>();
        tes = (ArrayList<Question>) appDb.questionDao().getRandomQuestion(303, 5);
        for (Question q : tes) {
            q.demo();
        }
    }*/

    /**
     * defini la réponse donné par l'utilisateur d'une question de l"examen
     * @param index index de la question.
     * @param userReponse numéro de la réponse de l'utilsateur de 0 à 3
     */
    public void setReponse(int index, int userReponse) {
        Question currentQuestion = questions.get(index);
        currentQuestion.setUserReponse(userReponse);
        questions.set(index, currentQuestion);
    }

    /**
     * retourne la question depuis un index
     * @param index index de la question
     * @return obj {@link Question}
     */
    public Question getQuestion(int index) {
        return questions.get(index);
    }

    /**
     * defini comme quoi la reponse à ete demandé pour la question à un index
     * @param index index de la question
     */
    public void setReponseAsked(int index) {
        Question currentQuestion = questions.get(index);
        currentQuestion.setReponseAsked(true);
        questions.set(index, currentQuestion);
    }
}
