package dev.favier.exam1radioamateur;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Affiche l'interface des question de l'examen
 */
public class ExamenActivity extends AppCompatActivity {

    Button delRespButton, coursQButton, reponseQButton, prevQButton, nextQButton;
    TextView themeQTextView, numQTextView, numberOfQtextView, commentTextView, timerTextView;
    RadioGroup propoRadioGroupe;
    RadioButton propo1RadioButton, propo2RadioButton, propo3RadioButton, propo4RadioButton;
    ImageView questionImageView;
    LinearLayout commentLinearLayout;

    Examen examen;
    CountDownTimer countDownTimer;
    Timer timer;
    int indexQuestion;
    int indexMaxQuestion;
    String coursUrl;
    boolean examTimerEnable;
    boolean showResponces;
    int examTime;
    long timeLeft = 0;
    long timeSpent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Integer> themeList = bundle.getIntegerArrayList("ThemeList");
        indexMaxQuestion = bundle.getInt("numberOfQuestions") - 1;
        int numberOfQuestionParTheme = (indexMaxQuestion + 1) / themeList.size();

        //firstRun = bundle.getBoolean("firstRun");
        examTimerEnable = bundle.getBoolean("examTimerEnable");
        examTime = bundle.getInt("timer");
        showResponces = bundle.getBoolean("showResponces");

        examen = new Examen(this, themeList, numberOfQuestionParTheme);
        setupControls();

        indexQuestion = 0;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                examen.genrateQuestions();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showQuestion();
                    }
                });
            }
        });

    }

    /**
     * register layout variable and buttons event
     */
    private void setupControls() {
        delRespButton = findViewById(R.id.delRespButton);
        coursQButton = findViewById(R.id.coursQButton);
        reponseQButton = findViewById(R.id.reponseQButton);
        prevQButton = findViewById(R.id.prevQButton);
        nextQButton = findViewById(R.id.nextQButton);
        themeQTextView = findViewById(R.id.themeQTextView);
        numberOfQtextView = findViewById(R.id.numberOfQtextView);
        numQTextView = findViewById(R.id.numQTextView);
        questionImageView = findViewById(R.id.questionImageView);
        propoRadioGroupe = findViewById(R.id.propoRadioGroupe);
        propo1RadioButton = findViewById(R.id.propo1RadioButton);
        propo2RadioButton = findViewById(R.id.propo2RadioButton);
        propo3RadioButton = findViewById(R.id.propo3RadioButton);
        propo4RadioButton = findViewById(R.id.propo4RadioButton);
        commentLinearLayout = findViewById(R.id.commentLinearLayout);
        commentTextView = findViewById(R.id.commentTextView);
        timerTextView = findViewById(R.id.timerTextView);

        // desactivate showResponces fx
        if (!showResponces) {
            reponseQButton.setEnabled(false);
        }
        // setup event time limit
        if (examTimerEnable) {
            //Log.w("debug", String.valueOf(timerInMillis) + "timer time");
            // countdown exam
            countDownTimer = new CountDownTimer((examTime * 60000), 1000) {
                //examTime * 60000, 1000
                @Override
                public void onTick(long millisUntilFinished) {
                    // calculate min, sec from millis
                    int min = (int) (millisUntilFinished / 1000) / 60;
                    int sec = (int) (millisUntilFinished / 1000) % 60;
                    if (min == 1 && sec == 0) {
                        timerTextView.setTextColor(Color.WHITE);
                        timerTextView.setBackgroundColor(Color.RED);
                    }
                    String secStr;
                    if (sec <= 9) {
                        secStr = "0" + sec;
                    } else {
                        secStr = String.valueOf(sec);
                    }
                    // set time left
                    timerTextView.setText(String.valueOf(min) + "'" + secStr + '"');
                    timeLeft = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    stopExam(true);//crash ici
                }
            };
            countDownTimer.start();
        }
        // setup timer of exam
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeSpent++;
            }
        }, 0, 1000);

        // uncheck proposition
        delRespButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propoRadioGroupe.clearCheck();
                examen.setReponse(indexQuestion, -1);
            }
        });

        //next Question
        nextQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexQuestion <= indexMaxQuestion) {
                    indexQuestion++;
                    showQuestion();
                    checkEnableBtn();
                }
            }
        });
        // prev Question
        prevQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexQuestion--;
                showQuestion();
                checkEnableBtn();
            }
        });

        //set proposition
        propoRadioGroupe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.propo1RadioButton:
                        //Log.w("debug", String.valueOf(0) + " set reposne");
                        examen.setReponse(indexQuestion, 0);
                        break;
                    case R.id.propo2RadioButton:
                        //Log.w("debug", String.valueOf(1) + " set reposne");
                        examen.setReponse(indexQuestion, 1);
                        break;
                    case R.id.propo3RadioButton:
                        //Log.w("debug", String.valueOf(2) + " set reposne");
                        examen.setReponse(indexQuestion, 2);
                        break;
                    case R.id.propo4RadioButton:
                        //Log.w("debug", String.valueOf(3) + " set reposne");
                        examen.setReponse(indexQuestion, 3);
                        break;
                }
            }
        });

        //response btn
        reponseQButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printResponse();
            }
        });

        checkEnableBtn();
    }

    /**
     * active/désactive les btn dans le cas de la première/derniere question
     */
    private void checkEnableBtn() {
        // enable/disable btn
        if (indexQuestion == indexMaxQuestion) {
            nextQButton.setEnabled(false);
        } else {
            nextQButton.setEnabled(true);
        }
        if (indexQuestion == 0) {
            prevQButton.setEnabled(false);
        } else {
            prevQButton.setEnabled(true);
        }
    }

    /**
     * Affiche le commentaire et la réponse à la question
     */
    private void printResponse() {
        Question currentQuestion = examen.getQuestion(indexQuestion);
        if (!currentQuestion.getCommentaire().equals("null")) {
            commentLinearLayout.setVisibility(View.VISIBLE);
            commentTextView.setText(currentQuestion.getCommentaire());
        }
        propo1RadioButton.setEnabled(false);
        propo2RadioButton.setEnabled(false);
        propo3RadioButton.setEnabled(false);
        propo4RadioButton.setEnabled(false);

        propo1RadioButton.setTextColor(Color.RED);
        propo2RadioButton.setTextColor(Color.RED);
        propo3RadioButton.setTextColor(Color.RED);
        propo4RadioButton.setTextColor(Color.RED);

        //Log.w("debug", String.valueOf(currentQuestion.getReponse()));
        switch (currentQuestion.getReponse()) {
            case 0:
                propo1RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
            case 1:
                propo2RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
            case 2:
                propo3RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
            case 3:
                propo4RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
        }

        examen.setReponseAsked(indexQuestion);
    }

    /**
     * Affiche la question
     */
    private void showQuestion() {
        Question currentQuestion = examen.getQuestion(indexQuestion);

        // set previous response
        switch (currentQuestion.getUserReponse()) {
            case 0:
                propo1RadioButton.setChecked(true);
                break;
            case 1:
                propo2RadioButton.setChecked(true);
                break;
            case 2:
                propo3RadioButton.setChecked(true);
                break;
            case 3:
                propo4RadioButton.setChecked(true);
                break;
            case -1:
                propoRadioGroupe.clearCheck();
                examen.setReponse(indexQuestion, -1);
                break;
        }

        //set image
        //questionImageView.setImageResource(getResources().getIdentifier("q_" + String.valueOf(currentQuestion.getNumero()), "drawable", getPackageName()));
        File file = new File(getFilesDir(), String.valueOf(currentQuestion.getNumero()) + ".png");
        questionImageView.setImageURI(Uri.fromFile(file));
        questionImageView.setContentDescription(currentQuestion.getQuestion());
        //set proposition
        ArrayList<String> propositions = currentQuestion.getPropositions();
        propo1RadioButton.setText(propositions.get(0));
        propo2RadioButton.setText(propositions.get(1));
        propo3RadioButton.setText(propositions.get(2));
        propo4RadioButton.setText(propositions.get(3));
        //set question id
        numQTextView.setText("# " + String.valueOf(currentQuestion.getNumero()));
        numberOfQtextView.setText(String.valueOf(indexQuestion + 1) + " / " + String.valueOf(indexMaxQuestion + 1));
        //set cours url
        coursUrl = currentQuestion.getCoursUrl();
        //set theme
        switch (currentQuestion.getThemeID()) {
            case Examen.codeCouleurs:
                themeQTextView.setText(R.string.theme_resistancesCouleurs);
                break;
            case Examen.groupementsDeResistances:
                themeQTextView.setText(R.string.theme_resistancesGroupes);
                break;
            case Examen.diodesEtTransistors:
                themeQTextView.setText(R.string.theme_ampli);
                break;
            case Examen.synoptiques:
                themeQTextView.setText(R.string.theme_synoptiques);
                break;
            case Examen.etagesRF:
                themeQTextView.setText(R.string.theme_etagesRF);
                break;
            case Examen.electriciteDeBase:
                themeQTextView.setText(R.string.theme_electricite);
                break;
            case Examen.courantsAlternatifs:
                themeQTextView.setText(R.string.theme_alternatif);
                break;
            case Examen.condensateursetBobines:
                themeQTextView.setText(R.string.theme_condoBob);
                break;
            case Examen.transformateursAmpli:
                themeQTextView.setText(R.string.theme_transfo);
                break;
            case Examen.ligneDeTransmis:
                themeQTextView.setText(R.string.theme_lignes);
                break;

            case Examen.classesEmission:
                themeQTextView.setText(R.string.theme_emission);
                break;
            case Examen.indicatifs:
                themeQTextView.setText(R.string.theme_indicatifs);
                break;
            case Examen.codeQ:
                themeQTextView.setText(R.string.theme_codeQ);
                break;
            case Examen.epellation:
                themeQTextView.setText(R.string.theme_epellation);
                break;
            case Examen.questionsEntrainement:
                themeQTextView.setText(R.string.theme_entrainement);
                break;
            case Examen.sanctions:
                themeQTextView.setText(R.string.theme_sanctions);
                break;
            case Examen.exposition:
                themeQTextView.setText(R.string.theme_messages);
                break;
            case Examen.longueurOnde:
                themeQTextView.setText(R.string.theme_antennes);
                break;
            case Examen.adaptation:
                themeQTextView.setText(R.string.theme_adaptation);
                break;
            case Examen.cem:
                themeQTextView.setText(R.string.theme_cem);
                break;

        }

        //enable propositions
        propo1RadioButton.setEnabled(true);
        propo2RadioButton.setEnabled(true);
        propo3RadioButton.setEnabled(true);
        propo4RadioButton.setEnabled(true);
        //set default color
        propo1RadioButton.setTextColor(Color.BLACK);
        propo2RadioButton.setTextColor(Color.BLACK);
        propo3RadioButton.setTextColor(Color.BLACK);
        propo4RadioButton.setTextColor(Color.BLACK);
        //set default comment visibility
        commentLinearLayout.setVisibility(View.GONE);
        // set response if user already ask for it
        if (currentQuestion.isReponseAsked()) {
            printResponse();
        }
    }

    /**
     * boutton vers le cours sur internet de f6kgl
     *
     * @param view
     */
    public void gotoCours(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(coursUrl));
        startActivity(browserIntent);
    }

    /**
     * ajoute le menu top bar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // setup top Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.examen_menu, menu);
        return true;
    }

    /**
     * envenment top bar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.terminerItem:
                stopExam(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * fini l'examen et envois vers la page des résultats
     */
    public void stopExam(boolean force) {
        if(!force){
            // confimation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(R.string.terminer);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Log.w("debug", "examen terminé");
                    dialog.dismiss();
                    if (examTimerEnable) {
                        countDownTimer.cancel();
                    }
                    Intent intent = new Intent(getBaseContext(), ExamenResults.class);
                    intent.putExtra("exam", examen.getResults());
                    intent.putExtra("timeSpent", timeSpent);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            if(!isFinishing()){
                alertDialog.show();//ici
            }

        }else {
            //Log.w("debug", "examen terminé");
            if (examTimerEnable) {
                countDownTimer.cancel();
            }
            Intent intent = new Intent(getBaseContext(), ExamenResults.class);
            intent.putExtra("exam", examen.getResults());
            intent.putExtra("timeSpent", timeSpent);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(R.string.stopExam);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(examTimerEnable){
                    countDownTimer.cancel();
                }
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        if(!isFinishing()){
            alertDialog.show();
        }

    }
}
