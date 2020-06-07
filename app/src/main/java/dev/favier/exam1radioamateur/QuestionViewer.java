package dev.favier.exam1radioamateur;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

/**
 * Affiche une des question via {@link ExamenResults}
 */
public class QuestionViewer extends AppCompatActivity {

    TextView themeViewerTextView, commentViewTextView, questionIdViewTextView;
    ImageView imageViewerImageView;
    RadioButton respViewer1RadioButton, respViewer2RadioButton, respViewer3RadioButton, respViewer4RadioButton;
    Button coursViewerButton;

    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_viewer);
        setupControls();

        Bundle bundle = getIntent().getExtras();
        question = (Question) bundle.getSerializable("question");
        printQuestion();
    }

    /**
     * configuration des variable du layout
     */
    private void setupControls() {
        themeViewerTextView = findViewById(R.id.themeViewerTextView);
        questionIdViewTextView = findViewById(R.id.questionIdViewTextView);
        imageViewerImageView = findViewById(R.id.imageViewerImageView);
        respViewer1RadioButton = findViewById(R.id.respViewer1RadioButton);
        respViewer2RadioButton = findViewById(R.id.respViewer2RadioButton);
        respViewer3RadioButton = findViewById(R.id.respViewer3RadioButton);
        respViewer4RadioButton = findViewById(R.id.respViewer4RadioButton);
        commentViewTextView = findViewById(R.id.commentViewTextView);
        coursViewerButton = findViewById(R.id.coursViewerButton);
    }

    /**
     * Affichage de la question
     */
    private void printQuestion() {
        //set theme
        switch (question.getThemeID()) {
            case Examen.codeCouleurs:
                themeViewerTextView.setText(R.string.theme_resistancesCouleurs);
                break;
            case Examen.groupementsDeResistances:
                themeViewerTextView.setText(R.string.theme_resistancesGroupes);
                break;
            case Examen.diodesEtTransistors:
                themeViewerTextView.setText(R.string.theme_ampli);
                break;
            case Examen.synoptiques:
                themeViewerTextView.setText(R.string.theme_synoptiques);
                break;
            case Examen.etagesRF:
                themeViewerTextView.setText(R.string.theme_etagesRF);
                break;
            case Examen.electriciteDeBase:
                themeViewerTextView.setText(R.string.theme_electricite);
                break;
            case Examen.courantsAlternatifs:
                themeViewerTextView.setText(R.string.theme_alternatif);
                break;
            case Examen.condensateursetBobines:
                themeViewerTextView.setText(R.string.theme_condoBob);
                break;
            case Examen.transformateursAmpli:
                themeViewerTextView.setText(R.string.theme_transfo);
                break;
            case Examen.ligneDeTransmis:
                themeViewerTextView.setText(R.string.theme_lignes);
                break;

            case Examen.classesEmission:
                themeViewerTextView.setText(R.string.theme_emission);
                break;
            case Examen.indicatifs:
                themeViewerTextView.setText(R.string.theme_indicatifs);
                break;
            case Examen.codeQ:
                themeViewerTextView.setText(R.string.theme_codeQ);
                break;
            case Examen.epellation:
                themeViewerTextView.setText(R.string.theme_epellation);
                break;
            case Examen.questionsEntrainement:
                themeViewerTextView.setText(R.string.theme_entrainement);
                break;
            case Examen.sanctions:
                themeViewerTextView.setText(R.string.theme_sanctions);
                break;
            case Examen.exposition:
                themeViewerTextView.setText(R.string.theme_messages);
                break;
            case Examen.longueurOnde:
                themeViewerTextView.setText(R.string.theme_antennes);
                break;
            case Examen.adaptation:
                themeViewerTextView.setText(R.string.theme_adaptation);
                break;
            case Examen.cem:
                themeViewerTextView.setText(R.string.theme_cem);
                break;
        }
        // set id
        questionIdViewTextView.setText("# " + String.valueOf(question.getNumero()));
        //set image
        File file = new File(getFilesDir(), String.valueOf(question.getNumero()) + ".png");
        imageViewerImageView.setImageURI(Uri.fromFile(file));
        //imageViewerImageView.setImageResource(getResources().getIdentifier("q_" + String.valueOf(question.getNumero()), "drawable", getPackageName()));
        imageViewerImageView.setContentDescription(question.getQuestion());
        // set proposition
        ArrayList<String> propositions = question.getPropositions();
        respViewer1RadioButton.setText(propositions.get(0));
        respViewer2RadioButton.setText(propositions.get(1));
        respViewer3RadioButton.setText(propositions.get(2));
        respViewer4RadioButton.setText(propositions.get(3));
        //set user response
        switch (question.getUserReponse()) {
            case 0:
                respViewer1RadioButton.setChecked(true);
                break;
            case 1:
                respViewer2RadioButton.setChecked(true);
                break;
            case 2:
                respViewer3RadioButton.setChecked(true);
                break;
            case 3:
                respViewer4RadioButton.setChecked(true);
                break;
        }
        //set good response
        respViewer1RadioButton.setTextColor(Color.RED);
        respViewer2RadioButton.setTextColor(Color.RED);
        respViewer3RadioButton.setTextColor(Color.RED);
        respViewer4RadioButton.setTextColor(Color.RED);
        switch (question.getReponse()) {
            case 0:
                respViewer1RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
            case 1:
                respViewer2RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
            case 2:
                respViewer3RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
            case 3:
                respViewer4RadioButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
                break;
        }
        // set comment
        if (!question.getCommentaire().equals("null")) {
            commentViewTextView.setVisibility(View.VISIBLE);
            commentViewTextView.setText(question.getCommentaire());
        }
        // set btn click event
        coursViewerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(question.getCoursUrl()));
                startActivity(browserIntent);
            }
        });

    }
}
