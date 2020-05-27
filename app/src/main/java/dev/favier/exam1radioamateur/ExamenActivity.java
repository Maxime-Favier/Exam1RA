package dev.favier.exam1radioamateur;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class ExamenActivity extends AppCompatActivity {

    Button delRespButton, coursQButton, reponseQButton, prevQButton, nextQButton;
    TextView themeQTextView, numQTextView, numberOfQtextView, commentTextView;
    RadioGroup propoRadioGroupe;
    RadioButton propo1RadioButton, propo2RadioButton, propo3RadioButton, propo4RadioButton;
    ImageView questionImageView;
    LinearLayout commentLinearLayout;

    Examen examen;
    int indexQuestion;
    int indexMaxQuestion;
    String coursUrl;
    boolean firstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Integer> themeList = bundle.getIntegerArrayList("ThemeList");
        //Log.w("themeList", themeList.toString());
        indexMaxQuestion = 20 - 1; //bundle.getInt("numberOfQuestions")
        int numberOfQuestionParTheme = (indexMaxQuestion + 1) / themeList.size();

        boolean malusEnable = bundle.getBoolean("malusEnable");
        firstRun = bundle.getBoolean("firstRun");

        examen = new Examen(this, themeList, numberOfQuestionParTheme, malusEnable);
        setupControls();

        indexQuestion = 0;
        // TODO: populate db on fist start
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.w("debug", "populate db");
                    examen.populateDbFromJson();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

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

        Log.w("debug", String.valueOf(currentQuestion.getReponse()));
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

    private void showQuestion() {
        Question currentQuestion = examen.getQuestion(indexQuestion);

        // set previous response
        switch (currentQuestion.getUserReponse()){
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
                break;
        }

        //set image
        questionImageView.setImageResource(getResources().getIdentifier("q_" + String.valueOf(currentQuestion.getNumero()), "drawable", getPackageName()));
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
        switch (currentQuestion.getThemeID()){
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


    public void gotoCours(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(coursUrl));
        startActivity(browserIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // setup top Menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.examen_menu, menu);
        return true;
    }

}
