package dev.favier.exam1radioamateur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
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

        // shared preferences load settings
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("UIPref", Context.MODE_PRIVATE);

        // dropdown prefs
        boolean legislationShow = sharedPref.getBoolean("legislationShow", true);
        legislationRow.setVisibility(legislationShow ? View.VISIBLE: View.GONE);
        boolean techniqueShow = sharedPref.getBoolean("techniqueShow", true);
        techniqueRow.setVisibility(techniqueShow ? View.VISIBLE: View.GONE);
        // exam settings prefs
        int numberOfQuestions = sharedPref.getInt("numberOfQuestions", 20);
        questionEditText.setText(String.valueOf(numberOfQuestions));
        showRespSwitch.setChecked(sharedPref.getBoolean("showResponces", false));
        commentsSwitch.setChecked(sharedPref.getBoolean("showComments", false));
        nextQSwitch.setChecked(sharedPref.getBoolean("autoNextQ", false));
        malusSwitch.setChecked(sharedPref.getBoolean("malusEnable", true));
        timerSwitch.setChecked(sharedPref.getBoolean("timerEnable", false));
        int examTime = sharedPref.getInt("examTime", 20);
        tempsEditText.setText(String.valueOf(examTime));
        // themes preferences
        //  legislation
        codeQCheckBox.setChecked(sharedPref.getBoolean("codeQTheme", false));
        emissionCheckBox.setChecked(sharedPref.getBoolean("emissionTheme", false));
        adaptationCheckBox.setChecked(sharedPref.getBoolean("adaptationTheme", false));
        epellationCheckBox.setChecked(sharedPref.getBoolean("epellationTheme", false));
        cemCheckBox.setChecked(sharedPref.getBoolean("cemTheme", false));
        antennesCheckBox.setChecked(sharedPref.getBoolean("antennesTheme", false));
        sanctionsCheckBox.setChecked(sharedPref.getBoolean("sanctionsTheme", false));
        messagesCheckBox.setChecked(sharedPref.getBoolean("messagesTheme", false));
        indicatifsCheckBox.setChecked(sharedPref.getBoolean("indicatifsTheme", false));
        entrainementCheckBox.setChecked(sharedPref.getBoolean("entrainementTheme", false));
        //  technique
        lignesCheckBox.setChecked(sharedPref.getBoolean("lignesTheme", false));
        etagesRFCheckBox.setChecked(sharedPref.getBoolean("etagesRFTheme", false));
        resistancesGroupesCheckBox.setChecked(sharedPref.getBoolean("resistancesGroupesTheme", false));
        ampliCheckBox.setChecked(sharedPref.getBoolean("ampliTheme", false));
        transfoCheckBox.setChecked(sharedPref.getBoolean("transfoTheme", false));
        alternatifCheckBox.setChecked(sharedPref.getBoolean("alternatifTheme", false));
        synoptiquesCheckBox.setChecked(sharedPref.getBoolean("synoptiquesTheme", false));
        resistancesCouleursCheckBox.setChecked(sharedPref.getBoolean("resistancesCouleursTheme", false));
        electriciteCheckBox.setChecked(sharedPref.getBoolean("electriciteTheme", false));
        condoBobCheckBox.setChecked(sharedPref.getBoolean("condoBobTheme", false));


        // shared preference writer
        final SharedPreferences.Editor sharedEditor = sharedPref.edit();

        // legislation + technique drop up/down logic
        legislationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch visibility state of the legislation drop down
                if (legislationRow.getVisibility() == View.GONE) {
                    legislationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_24dp, 0, R.drawable.ic_book_24dp, 0);
                    legislationRow.setVisibility(View.VISIBLE);
                    sharedEditor.putBoolean("legislationShow", true);

                } else {
                    legislationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_24dp, 0, R.drawable.ic_book_24dp, 0);
                    legislationRow.setVisibility(View.GONE);
                    sharedEditor.putBoolean("legislationShow", false);
                }
                // set show dropdown preferences
                sharedEditor.apply();

            }

        });
        // same for the techique drop down
        techniqueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (techniqueRow.getVisibility() == View.GONE) {
                    techniqueTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_24dp, 0, R.drawable.ic_build_24dp, 0);
                    techniqueRow.setVisibility(View.VISIBLE);
                    sharedEditor.putBoolean("techniqueShow", true);
                } else {
                    techniqueTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_24dp, 0, R.drawable.ic_build_24dp, 0);
                    techniqueRow.setVisibility(View.GONE);
                    sharedEditor.putBoolean("techniqueShow", false);
                }
                sharedEditor.apply();
            }
        });

        // select all themes button
        allThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // legislation theme
                codeQCheckBox.setChecked(true);
                emissionCheckBox.setChecked(true);
                adaptationCheckBox.setChecked(true);
                epellationCheckBox.setChecked(true);
                cemCheckBox.setChecked(true);
                antennesCheckBox.setChecked(true);
                sanctionsCheckBox.setChecked(true);
                messagesCheckBox.setChecked(true);
                indicatifsCheckBox.setChecked(true);
                entrainementCheckBox.setChecked(true);
                // techniques themes
                lignesCheckBox.setChecked(true);
                etagesRFCheckBox.setChecked(true);
                resistancesGroupesCheckBox.setChecked(true);
                ampliCheckBox.setChecked(true);
                transfoCheckBox.setChecked(true);
                alternatifCheckBox.setChecked(true);
                synoptiquesCheckBox.setChecked(true);
                resistancesCouleursCheckBox.setChecked(true);
                electriciteCheckBox.setChecked(true);
                condoBobCheckBox.setChecked(true);
            }
        });

        // deselect all themes button
        noThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // legislation theme
                codeQCheckBox.setChecked(false);
                emissionCheckBox.setChecked(false);
                adaptationCheckBox.setChecked(false);
                epellationCheckBox.setChecked(false);
                cemCheckBox.setChecked(false);
                antennesCheckBox.setChecked(false);
                sanctionsCheckBox.setChecked(false);
                messagesCheckBox.setChecked(false);
                indicatifsCheckBox.setChecked(false);
                entrainementCheckBox.setChecked(false);
                // techniques themes
                lignesCheckBox.setChecked(false);
                etagesRFCheckBox.setChecked(false);
                resistancesGroupesCheckBox.setChecked(false);
                ampliCheckBox.setChecked(false);
                transfoCheckBox.setChecked(false);
                alternatifCheckBox.setChecked(false);
                synoptiquesCheckBox.setChecked(false);
                resistancesCouleursCheckBox.setChecked(false);
                electriciteCheckBox.setChecked(false);
                condoBobCheckBox.setChecked(false);
            }
        });

        // start exam button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set exams setting prefs for the next exam
                int numberOfQuestions = Integer.parseInt(questionEditText.getText().toString());
                sharedEditor.putInt("numberOfQuestions", numberOfQuestions);
                boolean showResponces = showRespSwitch.isChecked();
                sharedEditor.putBoolean("showResponces", showResponces);
                boolean showComments = commentsSwitch.isChecked();
                sharedEditor.putBoolean("showComments", showComments);
                boolean autoNextQ = nextQSwitch.isChecked();
                sharedEditor.putBoolean("autoNextQ", autoNextQ);
                boolean malusEnable = malusSwitch.isChecked();
                sharedEditor.putBoolean("malusEnable", malusEnable);
                boolean timerEnable = timerSwitch.isChecked();
                sharedEditor.putBoolean("timerEnable", timerEnable);
                int examTime = Integer.parseInt(tempsEditText.getText().toString());
                sharedEditor.putInt("examTime", examTime);
                //  set exams themes prefs for the next exam
                //  legislation
                boolean codeQTheme = codeQCheckBox.isChecked();
                sharedEditor.putBoolean("codeQTheme", codeQTheme);
                boolean emissionTheme = emissionCheckBox.isChecked();
                sharedEditor.putBoolean("emissionTheme", emissionTheme);
                boolean adaptationTheme = adaptationCheckBox.isChecked();
                sharedEditor.putBoolean("adaptationTheme", adaptationTheme);
                boolean epellationTheme = epellationCheckBox.isChecked();
                sharedEditor.putBoolean("epellationTheme", epellationTheme);
                boolean cemTheme = cemCheckBox.isChecked();
                sharedEditor.putBoolean("cemTheme", cemTheme);
                boolean antennesTheme = antennesCheckBox.isChecked();
                sharedEditor.putBoolean("antennesTheme", antennesTheme);
                boolean sanctionsTheme = sanctionsCheckBox.isChecked();
                sharedEditor.putBoolean("sanctionsTheme", sanctionsTheme);
                boolean messagesTheme = messagesCheckBox.isChecked();
                sharedEditor.putBoolean("messagesTheme", messagesTheme);
                boolean indicatifsTheme = indicatifsCheckBox.isChecked();
                sharedEditor.putBoolean("indicatifsTheme", indicatifsTheme);
                boolean entrainementTheme = entrainementCheckBox.isChecked();
                sharedEditor.putBoolean("entrainementTheme", entrainementTheme);
                //  technique
                boolean lignesTheme = lignesCheckBox.isChecked();
                sharedEditor.putBoolean("lignesTheme", lignesTheme);
                boolean etagesRFTheme = etagesRFCheckBox.isChecked();
                sharedEditor.putBoolean("etagesRFTheme", etagesRFTheme);
                boolean resistancesGroupesTheme = resistancesGroupesCheckBox.isChecked();
                sharedEditor.putBoolean("resistancesGroupesTheme", resistancesGroupesTheme);
                boolean ampliTheme = ampliCheckBox.isChecked();
                sharedEditor.putBoolean("ampliTheme",ampliTheme);
                boolean transfoTheme = transfoCheckBox.isChecked();
                sharedEditor.putBoolean("transfoTheme", transfoTheme);
                boolean alternatifTheme = alternatifCheckBox.isChecked();
                sharedEditor.putBoolean("alternatifTheme", alternatifTheme);
                boolean synoptiquesTheme = synoptiquesCheckBox.isChecked();
                sharedEditor.putBoolean("synoptiquesTheme", synoptiquesTheme);
                boolean resistancesCouleursTheme = resistancesCouleursCheckBox.isChecked();
                sharedEditor.putBoolean("resistancesCouleursTheme", resistancesCouleursTheme);
                boolean electriciteTheme = electriciteCheckBox.isChecked();
                sharedEditor.putBoolean("electriciteTheme", electriciteTheme);
                boolean condoBobTheme = condoBobCheckBox.isChecked();
                sharedEditor.putBoolean("condoBobTheme", condoBobTheme);

                // apply preferences
                sharedEditor.apply();

                // start new intent examen
                Intent intent = new Intent(getBaseContext(), ExamenActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.remerciementItem:
                Intent intent = new Intent(getBaseContext(), Remerciements.class);
                startActivity(intent);
                return true;
            case R.id.opensourceItem:
                Intent intent1 = new Intent(getBaseContext(), OpenSource.class);
                startActivity(intent1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
