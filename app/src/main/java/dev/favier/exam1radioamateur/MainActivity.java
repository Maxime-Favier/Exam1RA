package dev.favier.exam1radioamateur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    TableRow legislationRow, techniqueRow;
    TextView legislationTextView, techniqueTextView;
    Button allThemeButton, noThemeButton, startButton;
    CheckBox codeQCheckBox, emissionCheckBox, adaptationCheckBox, epellationCheckBox, cemCheckBox, antennesCheckBox,
            sanctionsCheckBox, messagesCheckBox, indicatifsCheckBox, entrainementCheckBox;
    CheckBox lignesCheckBox, etagesRFCheckBox, resistancesGroupesCheckBox, ampliCheckBox, transfoCheckBox,
            alternatifCheckBox, synoptiquesCheckBox, resistancesCouleursCheckBox, electriciteCheckBox, condoBobCheckBox;
    EditText questionEditText, tempsEditText;
    Switch showRespSwitch, commentsSwitch, nextQSwitch, malusSwitch, timerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupControls();
    }

    private void setupControls() {
        // register widgets
        legislationRow = findViewById(R.id.legislationRow);
        techniqueRow = findViewById(R.id.techniqueRow);
        legislationTextView = findViewById(R.id.legislationTextView);
        techniqueTextView = findViewById(R.id.techniqueTextView);
        allThemeButton = findViewById(R.id.allThemeButton);
        noThemeButton = findViewById(R.id.noThemeButton);
        startButton = findViewById(R.id.startButton);
        // setting edutText
        questionEditText = findViewById(R.id.questionEditText);
        tempsEditText = findViewById(R.id.tempsEditText);
        // setting switch
        showRespSwitch = findViewById(R.id.showRespSwitch);
        commentsSwitch = findViewById(R.id.commentsSwitch);
        nextQSwitch = findViewById(R.id.nextQSwitch);
        malusSwitch = findViewById(R.id.malusSwitch);
        timerSwitch = findViewById(R.id.timerSwitch);
        // register theme checkbox
        codeQCheckBox = findViewById(R.id.codeQCheckBox);
        emissionCheckBox = findViewById(R.id.emissionCheckBox);
        adaptationCheckBox = findViewById(R.id.adaptationCheckBox);
        epellationCheckBox = findViewById(R.id.epellationCheckBox);
        cemCheckBox = findViewById(R.id.cemCheckBox);
        antennesCheckBox = findViewById(R.id.antennesCheckBox);
        sanctionsCheckBox = findViewById(R.id.sanctionsCheckBox);
        messagesCheckBox = findViewById(R.id.messagesCheckBox);
        indicatifsCheckBox = findViewById(R.id.indicatifsCheckBox);
        entrainementCheckBox = findViewById(R.id.entrainementCheckBox);
        lignesCheckBox = findViewById(R.id.lignesCheckBox);
        etagesRFCheckBox = findViewById(R.id.etagesRFCheckBox);
        resistancesGroupesCheckBox = findViewById(R.id.resistancesGroupesCheckBox);
        ampliCheckBox = findViewById(R.id.ampliCheckBox);
        transfoCheckBox = findViewById(R.id.transfoCheckBox);
        alternatifCheckBox = findViewById(R.id.alternatifCheckBox);
        synoptiquesCheckBox = findViewById(R.id.synoptiquesCheckBox);
        resistancesCouleursCheckBox = findViewById(R.id.resistancesCouleursCheckBox);
        electriciteCheckBox = findViewById(R.id.electriciteCheckBox);
        condoBobCheckBox = findViewById(R.id.condoBobCheckBox);

        // legislation + technique drop up/down logic
        legislationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch visibility state of the legislation drop down
                if (legislationRow.getVisibility() == View.GONE) {
                    legislationRow.setVisibility(View.VISIBLE);
                    legislationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_24dp, 0, R.drawable.ic_book_24dp, 0);

                } else {
                    legislationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_24dp, 0, R.drawable.ic_book_24dp, 0);
                    legislationRow.setVisibility(View.GONE);
                }

            }

        });
        // same for the techique drop down
        techniqueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (techniqueRow.getVisibility() == View.GONE) {
                    techniqueRow.setVisibility(View.VISIBLE);
                    techniqueTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_24dp, 0, R.drawable.ic_build_24dp, 0);
                } else {
                    techniqueRow.setVisibility(View.GONE);
                    techniqueTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_24dp, 0, R.drawable.ic_build_24dp, 0);
                }
            }
        });

    }
}
