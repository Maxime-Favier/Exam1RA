package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuestionsDownload extends AppCompatActivity {
    private static final String TAG = "QuestionsDownload";
    private TextView downloadStateTextView, errrorInfotextView;
    private LinearProgressIndicator progressBar;
    private MaterialButton retryButton;
    private boolean allowBackQuit = false;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_download);
        setupControls();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!allowBackQuit) {
                    Toast.makeText(QuestionsDownload.this, "Veuillez attendre la fin du téléchargement", Toast.LENGTH_SHORT).show();
                } else {
                    finishAffinity();
                }
            }
        });
        downloaderTasker();
    }

    private void setupControls() {
        downloadStateTextView = findViewById(R.id.downloadStateTextView);
        errrorInfotextView = findViewById(R.id.errrorInfotextView);
        progressBar = findViewById(R.id.progressBar2);
        retryButton = findViewById(R.id.retryButton);

        retryButton.setOnClickListener(v -> retryDownload());
    }

    private void updateProgressUi(int progress) {
        runOnUiThread(() -> progressBar.setProgressCompat(progress, true));
    }

    private void updateStateUi(int stringResId, boolean isIndeterminate) {
        runOnUiThread(() -> {
            downloadStateTextView.setText(stringResId);
            progressBar.setIndeterminate(isIndeterminate);
            if (!isIndeterminate) {
                progressBar.setProgress(0);
            }
        });
    }

    private void showErrorUi(String errorMsg) {
        runOnUiThread(() -> {
            progressBar.setIndeterminate(false);
            progressBar.setProgressCompat(100, true);
            allowBackQuit = true;
            errrorInfotextView.setText(errorMsg);
            downloadStateTextView.setText(R.string.downloadFailed);
            retryButton.setVisibility(View.VISIBLE);
        });
    }

    private void retryDownload() {
        retryButton.setVisibility(View.GONE);
        errrorInfotextView.setText("");
        allowBackQuit = false;
        downloaderTasker();
    }

    private void downloaderTasker() {
        try {
            final DbPopulator dbPopulator = new DbPopulator(getApplicationContext());

            executorService.execute(() -> {
                try {
                    // Images
                    updateStateUi(R.string.downloadImg, false);
                    String errorImg = dbPopulator.downloadZipImg(this::updateProgressUi);
                    if (errorImg != null) {
                        showErrorUi("Erreur images : " + errorImg);
                        return;
                    }

                    // Questions JSON
                    updateStateUi(R.string.downloadQuestion, false);
                    String errorJson = dbPopulator.downloadJson(this::updateProgressUi);
                    if (errorJson != null) {
                        showErrorUi("Erreur JSON : " + errorJson);
                        return;
                    }

                    // Base de données
                    SharedPreferences sharedPref;
                    Context context = getApplicationContext();
                    sharedPref = context.getSharedPreferences("UIPref" + String.valueOf(BuildConfig.VERSION_CODE), Context.MODE_PRIVATE);
                    sharedPref.edit().putBoolean("firstrun", true).apply();
                    updateStateUi(R.string.bddGen, true);
                    dbPopulator.populateDbFromJson();

                    // Extraction ZIP
                    updateStateUi(R.string.unzipProcess, true);
                    boolean isUnzipped = dbPopulator.unzipImg();
                    if (!isUnzipped) {
                        showErrorUi("Erreur lors de la décompression des images.");
                        return;
                    }

                    // 5. Finalisation
                    updateStateUi(R.string.ajust, true);
                    dbPopulator.setFirstRunFlag();

                    runOnUiThread(this::finish);

                } catch (Exception e) {
                    Log.e(TAG, "Erreur pendant le téléchargement", e);
                    showErrorUi("Erreur inattendue : " + e.getLocalizedMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du lancement de la tâche", e);
            showErrorUi("Erreur d'initialisation : " + e.getLocalizedMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
    }
}