package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * rempli la base de donne de question
 */
public class DbPopulator {
    private static final String TAG = "DbPopulator";
    private final AppDatabase appDb;
    private final Context context;

    public DbPopulator(Context context) {
        this.context = context;
        this.appDb = AppDatabase.getInstance(context);
    }

    // Interface pour remonter la progression à l'interface graphique
    public interface DownloadProgressListener {
        void onProgressUpdate(int percentage);
    }

    /**
     * Ajoute les questions dans la bdd depuis un json
     *
     */
    public void populateQDbFromJson() {
        // Exécute tout le bloc en une seule transaction atomique
        appDb.runInTransaction(() -> {
            try {
                appDb.questionDao().clearQuestions();

                InputStream is = context.openFileInput("questions.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                is.close();

                JSONObject mainJSObject = new JSONObject(builder.toString());
                JSONArray questionsJsonObject = mainJSObject.getJSONArray("questions");

                for (int i = 0; i < questionsJsonObject.length(); i++) {
                    JSONObject questionObj = questionsJsonObject.getJSONObject(i);
                    Question question = new Question();
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

                    appDb.questionDao().insertQuestion(question);
                }
            } catch (Exception e) {
                // Lève une RuntimeException pour forcer le rollback de la transaction
                throw new RuntimeException("Erreur lors du remplissage de la BDD", e);
            }
        });
    }


    /**
     * Ajoute les séries et leurs liaisons dans la BDD.
     */
    public void populateSeriesFromJson() {
        List<SerieEntity> seriesList = new ArrayList<>();
        Map<Integer, List<Integer>> seriesQuestionsMap = new HashMap<>();

        // Ouvre le fichier téléchargé dans le stockage interne (filesDir)
        try (FileInputStream is = context.openFileInput("series.json");
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (root.has("series") && root.get("series").isJsonArray()) {
                JsonArray seriesArray = root.getAsJsonArray("series");

                for (JsonElement element : seriesArray) {
                    if (!element.isJsonObject()) continue;

                    JsonObject seriesObj = element.getAsJsonObject();
                    int num = seriesObj.get("num").getAsInt();
                    String nom = seriesObj.get("nom").getAsString();
                    String type = seriesObj.get("type").getAsString();

                    seriesList.add(new SerieEntity(num, nom, type));

                    if (seriesObj.has("questions") && seriesObj.get("questions").isJsonArray()) {
                        JsonArray questionsArray = seriesObj.getAsJsonArray("questions");
                        List<Integer> questionNumeros = new ArrayList<>();

                        for (JsonElement qElem : questionsArray) {
                            questionNumeros.add(qElem.getAsInt());
                        }
                        seriesQuestionsMap.put(num, questionNumeros);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la lecture de series.json depuis le stockage interne", e);
            return;
        }

        appDb.runInTransaction(() -> {
            try {
                List<Question> allQuestions = appDb.questionDao().getAllQuestions();
                Log.d(TAG, "Nombre de questions en BDD : " + allQuestions.size());

                if (allQuestions.isEmpty()) {
                    Log.w(TAG, "Attention : La table Questions est vide !");
                }

                // Map : imgNum (numero) -> Liste des UIDs correspondants
                Map<Integer, List<Integer>> imgNumToUidsMap = new HashMap<>();
                for (Question q : allQuestions) {
                    int imgNum = q.getNumero();
                    if (!imgNumToUidsMap.containsKey(imgNum)) {
                        imgNumToUidsMap.put(imgNum, new ArrayList<>());
                    }
                    imgNumToUidsMap.get(imgNum).add(q.getUid());
                }

                // Construction des liaisons CrossRef
                List<SerieQuestionLkup> crossRefs = new ArrayList<>();
                for (Map.Entry<Integer, List<Integer>> entry : seriesQuestionsMap.entrySet()) {
                    int serieNum = entry.getKey();
                    List<Integer> questionImgNums = entry.getValue();

                    for (int imgNum : questionImgNums) {
                        List<Integer> uids = imgNumToUidsMap.get(imgNum);
                        if (uids != null) {
                            for (int uid : uids) {
                                crossRefs.add(new SerieQuestionLkup(serieNum, uid));
                            }
                        } else {
                            Log.w(TAG, "Question avec imgNum=" + imgNum + " introuvable pour la série " + serieNum);
                        }
                    }
                }

                // Insertion en base
                appDb.serieDao().clearAllSeriesData();
                appDb.serieDao().insertSeries(seriesList);
                appDb.serieDao().insertCrossRefs(crossRefs);

                Log.d(TAG, "Insertion réussie : " + seriesList.size() + " séries et " + crossRefs.size() + " liaisons.");

            } catch (Exception e) {
                Log.e(TAG, "Erreur lors de la transaction d'insertion des séries", e);
                throw new RuntimeException("Transaction annulée", e);
            }
        });
    }

    /**
     * Télécharge un fichier générique avec gestion d'erreur HTTP et barre de progression
     */
    private String downloadFile(String urlString, String fileName, DownloadProgressListener listener) {
        File targetFile = new File(context.getFilesDir(), fileName);
        File tempFile = new File(context.getFilesDir(), fileName + ".tmp");

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Erreur HTTP " + connection.getResponseCode();
            }

            int fileLength = connection.getContentLength();

            try (InputStream input = new BufferedInputStream(connection.getInputStream(), 8192);
                 OutputStream output = new FileOutputStream(tempFile)) { // Écrit dans le .tmp

                byte[] buffer = new byte[8192];
                long total = 0;
                int count;

                while ((count = input.read(buffer)) != -1) {
                    // Vérifie si le thread a été interrompu par onDestroy()
                    if (Thread.currentThread().isInterrupted()) {
                        tempFile.delete();
                        return "Téléchargement annulé.";
                    }
                    total += count;
                    if (fileLength > 0 && listener != null) {
                        listener.onProgressUpdate((int) (total * 100 / fileLength));
                    }
                    output.write(buffer, 0, count);
                }
                output.flush();
            }

            // Téléchargement réussi : on remplace le fichier final
            if (tempFile.renameTo(targetFile)) {
                return null;
            } else {
                tempFile.delete();
                return "Erreur lors de la sauvegarde du fichier.";
            }

        } catch (IOException e) {
            if (tempFile.exists()) tempFile.delete(); // Nettoyage en cas d'échec
            return "Erreur réseau : " + e.getLocalizedMessage();
        }
    }



    public String downloadZipImg(DownloadProgressListener listener) {
        Log.d(TAG, "start download question zip");
        return downloadFile("https://exam1.r-e-f.org/assets/questions.zip", "questions.zip", listener);
    }

    public String downloadQJson(DownloadProgressListener listener) {
        Log.d(TAG, "start download question json");
        return downloadFile("https://exam1.r-e-f.org/assets/questions.json", "questions.json", listener);
    }

    public String downloadSeriesJson(DownloadProgressListener listener){
        Log.d(TAG, "start download serie json");
        return downloadFile("https://exam1.r-e-f.org/assets/series.json", "series.json", listener);
    }

    public boolean unzipImg() {
        Log.d(TAG, "unzip start");
        try {
            unzip(new File(context.getFilesDir(), "questions.zip"), new File(context.getFilesDir(), ""));
            // supprime le .zip quand c'est terminé
            File fileDel = new File(context.getFilesDir(), "questions.zip");
            fileDel.delete();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la décompression du fichier ZIP", e);
            return false;
        }
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry ze;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory()) continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    int count;
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                }
            }
        }
    }

    public void setFirstRunFlag() {
        Log.d(TAG, "set first run flag done");
        SharedPreferences sharedPref = context.getSharedPreferences("UIPref" + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean("firstrun", false).apply();
    }
}
