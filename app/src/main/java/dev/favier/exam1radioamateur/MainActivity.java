package dev.favier.exam1radioamateur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TableRow legislationRow, techniqueRow;
    TextView legislationTextView, techniqueTextView;
    Button allThemeButton, noThemeButton, startButton;
    CheckBox codeQCheckBox, emissionCheckBox, adaptationCheckBox, epellationCheckBox, cemCheckBox, antennesCheckBox,
            sanctionsCheckBox, messagesCheckBox, indicatifsCheckBox, entrainementCheckBox;
    CheckBox lignesCheckBox, etagesRFCheckBox, resistancesGroupesCheckBox, ampliCheckBox, transfoCheckBox,
            alternatifCheckBox, synoptiquesCheckBox, resistancesCouleursCheckBox, electriciteCheckBox, condoBobCheckBox;
    EditText questionEditText, tempsEditText;
    Switch showRespSwitch, nextQSwitch, malusSwitch, timerSwitch;

    ArrayList<Integer> ThemeList;
    SharedPreferences sharedPref = null;
    boolean firstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // shared preferences load settings
        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences("UIPref", Context.MODE_PRIVATE);
        setupControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPref.getBoolean("firstrun", true)) {
            // first run
            firstRun = true;
            sharedPref.edit().putBoolean("firstrun", false).commit();
        }
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


        // dropdown prefs
        boolean legislationShow = sharedPref.getBoolean("legislationShow", true);
        legislationRow.setVisibility(legislationShow ? View.VISIBLE : View.GONE);
        boolean techniqueShow = sharedPref.getBoolean("techniqueShow", true);
        techniqueRow.setVisibility(techniqueShow ? View.VISIBLE : View.GONE);
        // exam settings prefs
        int numberOfQuestions = sharedPref.getInt("numberOfQuestions", 20);
        questionEditText.setText(String.valueOf(numberOfQuestions));
        showRespSwitch.setChecked(sharedPref.getBoolean("showResponces", false));
        nextQSwitch.setChecked(sharedPref.getBoolean("autoNextQ", false));
        malusSwitch.setChecked(sharedPref.getBoolean("malusEnable", true));
        timerSwitch.setChecked(sharedPref.getBoolean("timerEnable", false));
        int examTime = sharedPref.getInt("examTime", 20);
        tempsEditText.setText(String.valueOf(examTime));
        // themes preferences

        String Themejson = sharedPref.getString("ThemeJson", "[]");
        ThemeList = new ArrayList<>();
        ThemeList = new Gson().fromJson(Themejson, new TypeToken<ArrayList<Integer>>(){}.getType());
        for(int theme: ThemeList){
            switch (theme){
                case Examen.codeQ:
                    codeQCheckBox.setChecked(true);
                    break;
                case Examen.classesEmission:
                    emissionCheckBox.setChecked(true);
                    break;
                case Examen.adaptation:
                    adaptationCheckBox.setChecked(true);
                    break;
                case Examen.epellation:
                    epellationCheckBox.setChecked(true);
                    break;
                case Examen.cem:
                    cemCheckBox.setChecked(true);
                    break;
                case Examen.longueurOnde:
                    antennesCheckBox.setChecked(true);
                    break;
                case Examen.sanctions:
                    sanctionsCheckBox.setChecked(true);
                    break;
                case Examen.exposition:
                    messagesCheckBox.setChecked(true);
                    break;
                case Examen.indicatifs:
                    indicatifsCheckBox.setChecked(true);
                    break;
                case Examen.questionsEntrainement:
                    entrainementCheckBox.setChecked(true);
                    break;

                case Examen.ligneDeTransmis:
                    lignesCheckBox.setChecked(true);
                    break;
                case Examen.etagesRF:
                    etagesRFCheckBox.setChecked(true);
                    break;
                case  Examen.groupementsDeResistances:
                    resistancesGroupesCheckBox.setChecked(true);
                    break;
                case  Examen.diodesEtTransistors:
                    ampliCheckBox.setChecked(true);
                    break;
                case Examen.transformateursAmpli:
                    transfoCheckBox.setChecked(true);
                    break;
                case Examen.courantsAlternatifs:
                    alternatifCheckBox.setChecked(true);
                    break;
                case Examen.synoptiques:
                    synoptiquesCheckBox.setChecked(true);
                    break;
                case Examen.codeCouleurs:
                    resistancesCouleursCheckBox.setChecked(true);
                    break;
                case Examen.electriciteDeBase:
                    electriciteCheckBox.setChecked(true);
                    break;
                case Examen.condensateursetBobines:
                    condoBobCheckBox.setChecked(true);
                    break;
            }
        }


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
                boolean autoNextQ = nextQSwitch.isChecked();
                sharedEditor.putBoolean("autoNextQ", autoNextQ);
                boolean malusEnable = malusSwitch.isChecked();
                sharedEditor.putBoolean("malusEnable", malusEnable);
                boolean timerEnable = timerSwitch.isChecked();
                sharedEditor.putBoolean("timerEnable", timerEnable);
                int examTime = Integer.parseInt(tempsEditText.getText().toString());
                sharedEditor.putInt("examTime", examTime);


                //  set exams themes prefs for the next exam
                ThemeList = new ArrayList<>();
                if (codeQCheckBox.isChecked()) {
                    ThemeList.add(Examen.codeQ);
                }
                if(emissionCheckBox.isChecked()){
                    ThemeList.add(Examen.classesEmission);
                }
                if(adaptationCheckBox.isChecked()){
                    ThemeList.add(Examen.adaptation);
                }
                if(epellationCheckBox.isChecked()){
                    ThemeList.add(Examen.epellation);
                }
                if(cemCheckBox.isChecked()){
                    ThemeList.add(Examen.cem);
                }
                if(antennesCheckBox.isChecked()){
                    ThemeList.add(Examen.longueurOnde);
                }
                if(sanctionsCheckBox.isChecked()){
                    ThemeList.add(Examen.sanctions);
                }
                if(messagesCheckBox.isChecked()){
                    ThemeList.add(Examen.exposition);
                }
                if(indicatifsCheckBox.isChecked()){
                    ThemeList.add(Examen.indicatifs);
                }
                if(entrainementCheckBox.isChecked()){
                    ThemeList.add(Examen.questionsEntrainement);
                }

                if(lignesCheckBox.isChecked()){
                    ThemeList.add(Examen.ligneDeTransmis);
                }
                if(etagesRFCheckBox.isChecked()){
                    ThemeList.add(Examen.etagesRF);
                }
                if(resistancesGroupesCheckBox.isChecked()){
                    ThemeList.add(Examen.groupementsDeResistances);
                }
                if(ampliCheckBox.isChecked()){
                    ThemeList.add(Examen.diodesEtTransistors);
                }
                if(transfoCheckBox.isChecked()){
                    ThemeList.add(Examen.transformateursAmpli);
                }
                if(alternatifCheckBox.isChecked()){
                    ThemeList.add(Examen.courantsAlternatifs);
                }
                if(synoptiquesCheckBox.isChecked()){
                    ThemeList.add(Examen.synoptiques);
                }
                if(resistancesCouleursCheckBox.isChecked()){
                    ThemeList.add(Examen.codeCouleurs);
                }
                if(electriciteCheckBox.isChecked()){
                    ThemeList.add(Examen.electriciteDeBase);
                }
                if(condoBobCheckBox.isChecked()){
                    ThemeList.add(Examen.condensateursetBobines);
                }

                sharedEditor.putString("ThemeJson", new Gson().toJson(ThemeList));
                // apply preferences
                sharedEditor.apply();

                if(ThemeList.size() >= 1) {
                    // start new intent examen
                    Intent intent = new Intent(getBaseContext(), ExamenActivity.class);
                    intent.putIntegerArrayListExtra("ThemeList", ThemeList);
                    intent.putExtra("malusEnable", malusEnable);
                    intent.putExtra("numberOfQuestions", numberOfQuestions);
                    intent.putExtra("firstRun", firstRun);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Selectionnez un theme", Toast.LENGTH_SHORT).show();
                }
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
        switch (item.getItemId()) {
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
