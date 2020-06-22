package dev.favier.exam1radioamateur;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;

import java.io.IOException;

public class QuestionsDownload extends AppCompatActivity {

    TextView downloadStateTextView, errrorInfotextView;
    boolean allowBackQuit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_download);

        setupControls();

        try {
            downloaderTasker();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupControls() {
        downloadStateTextView = findViewById(R.id.downloadStateTextView);
        errrorInfotextView = findViewById(R.id.errrorInfotextView);
    }

    private void downloaderTasker() throws IOException, JSONException {
        final DbPopulator dbPopulator = new DbPopulator(getApplicationContext());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final boolean a, b, c, d;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStateTextView.setText(R.string.downloadImg);
                    }
                });
                a = dbPopulator.downloadZipImg();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStateTextView.setText(R.string.downloadQuestion);
                    }
                });
                b = dbPopulator.downloadJson();
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downloadStateTextView.setText(R.string.bddGen);
                        }
                    });

                    dbPopulator.populateDbFromJson();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStateTextView.setText(R.string.unzipProcess);
                    }
                });
                c = dbPopulator.unzipImg();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStateTextView.setText(R.string.ajust);
                    }
                });
                // en attente du https sur le site de f6kgl
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStateTextView.setText(R.string.downloadCours);
                    }
                });
                d = dbPopulator.downloadCoursHtml();*/

                if (a && b && c /*&& d*/) {
                    dbPopulator.setFirstRunFlag();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // en attente du https sur le site de f6kgl
                            downloadStateTextView.setText(getResources().getString(R.string.errorCode, a, b, c/*, d*/));
                            allowBackQuit = true;
                            if (!a && !b) {
                                errrorInfotextView.setText(R.string.pbConnect);
                            } else {
                                errrorInfotextView.setText(R.string.pbRestart);
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!allowBackQuit) {
            Toast.makeText(this, "Veuillez attendre la fin du téléchargement", Toast.LENGTH_SHORT).show();
        } else {
            finishAffinity();
            System.exit(0);
        }

        //super.onBackPressed();
    }
}
