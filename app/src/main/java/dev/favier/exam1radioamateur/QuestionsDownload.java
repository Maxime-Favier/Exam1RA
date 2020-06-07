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
                boolean a, b, c;
                downloadStateTextView.setText(R.string.downloadImg);
                a = dbPopulator.downloadZipImg();
                downloadStateTextView.setText(R.string.downloadQuestion);
                b = dbPopulator.downloadJson();
                try {
                    downloadStateTextView.setText(R.string.bddGen);
                    dbPopulator.populateDbFromJson();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                downloadStateTextView.setText(R.string.unzipProcess);
                c = dbPopulator.unzipImg();
                downloadStateTextView.setText(R.string.ajust);
                if (a && b && c) {
                    dbPopulator.setFirstRunFlag();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    downloadStateTextView.setText(getResources().getString(R.string.errorCode, a, b, c));
                    allowBackQuit = true;
                    if(!a && !b){
                        errrorInfotextView.setText(R.string.pbConnect);
                    }else {
                        errrorInfotextView.setText(R.string.pbRestart);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!allowBackQuit){
            Toast.makeText(this, "Veuillez attendre la fin du téléchargement", Toast.LENGTH_SHORT).show();
        }else {
            finishAffinity();
            System.exit(0);
        }

        //super.onBackPressed();
    }
}
