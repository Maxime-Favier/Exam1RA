package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * rempli la base de donne de question
 */
public class DbPopulator {

    AppDatabase appDb;
    Context context;

    public DbPopulator(Context context) throws IOException, JSONException {
        this.context = context;
        appDb = AppDatabase.getInstance(context);
        //populateDbFromJson();
    }

    /**
     * Ajoute les questions dans la bdd depuis un json
     *
     * @throws IOException
     * @throws JSONException
     */
    public void populateDbFromJson() throws IOException, JSONException {
        //AppDatabase appDb = AppDatabase.getInstance(context);
        appDb.questionDao().clearQuestions();
        InputStream is = context.openFileInput("questions.json"); //context.getResources().openRawResource(R.raw.questions);
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
        Log.w("debug", "db populated");
        //appDb.close();

    }

    /**
     * download zip from website
     * @return error state
     */
    public boolean downloadZipImg() {
        try {
            Log.w("debug", "start dowload question zip");
            URL url = new URL("https://exam1.r-e-f.org/assets/questions.zip");
            URLConnection connection = url.openConnection();
            connection.connect();

            try (InputStream input = new BufferedInputStream(url.openStream(), 8192)) {
                File file = new File(context.getFilesDir(), "questions.zip");
                // save to file
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[8192]; // or other buffer size
                    int read;

                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                }
            }
            Log.w("debug", "downloaded zip");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    /**
     * download question json
     * @return
     */
    public boolean downloadJson() {
        try {
            Log.w("debug", "start download question json");
            URL url = new URL("https://exam1.r-e-f.org/assets/questions.json");
            URLConnection connection = url.openConnection();
            connection.connect();

            try (InputStream input = new BufferedInputStream(url.openStream(), 8192)) {
                File file = new File(context.getFilesDir(), "questions.json");
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[8192]; // or other buffer size
                    int read;

                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                }
            }
            Log.w("debug", "downloaded json");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean unzipImg() {
        Log.w("debug", "unzip start");
        try {
            unzip(new File(context.getFilesDir(), "questions.zip"), new File(context.getFilesDir(), ""));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Log.w("debug", "unzip end");

        // supprime le .zip quand c'est terminé
        File fileDel = new File(context.getFilesDir(), "questions.zip");
        fileDel.delete();
        return true;
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        }
    }

    public void setFirstRunFlag() {
        Log.w("debug", "set first run flag done");
        SharedPreferences sharedPref = context.getSharedPreferences("UIPref" + String.valueOf(BuildConfig.VERSION_CODE), Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean("firstrun", false).commit();

    }
}
