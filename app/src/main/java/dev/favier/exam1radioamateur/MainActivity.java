package dev.favier.exam1radioamateur;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.util.Log;
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
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TableRow legislationRow, techniqueRow;
    TextView legislationTextView, techniqueTextView;
    Button allThemeButton, noThemeButton, startButton;
    CheckBox codeQCheckBox, emissionCheckBox, adaptationCheckBox, epellationCheckBox, cemCheckBox, antennesCheckBox,
            sanctionsCheckBox, messagesCheckBox, indicatifsCheckBox, entrainementCheckBox;
    CheckBox lignesCheckBox, etagesRFCheckBox, resistancesGroupesCheckBox, ampliCheckBox, transfoCheckBox,
            alternatifCheckBox, synoptiquesCheckBox, resistancesCouleursCheckBox, electriciteCheckBox, condoBobCheckBox;
    CheckBox techniqueCheckBox, legislationCheckBox;
    EditText tempsEditText;
    Spinner nbrQSpinner;
    Switch showRespSwitch, malusSwitch, timerSwitch;

    ArrayList<Integer> ThemeList;
    SharedPreferences sharedPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // shared preferences load settings
        Context context = getApplicationContext();

        sharedPref = context.getSharedPreferences("UIPref" + String.valueOf(BuildConfig.VERSION_CODE), Context.MODE_PRIVATE);

        setupControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPref.getBoolean("firstrun", true)) {
            // first run
            Log.w("debug", "first run!");
            Intent intent = new Intent(getBaseContext(), QuestionsDownload.class);
            startActivity(intent);
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
        //questionEditText = findViewById(R.id.questionEditText);
        nbrQSpinner = findViewById(R.id.nbrQSpinner);
        tempsEditText = findViewById(R.id.tempsEditText);
        // setting switch
        showRespSwitch = findViewById(R.id.showRespSwitch);
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
        techniqueCheckBox = findViewById(R.id.techniqueCheckBox);
        legislationCheckBox = findViewById(R.id.legislationCheckBox);
        Log.w("debug", "setup controls");

        // set minimum countdown
        tempsEditText.setFilters(new InputFilter[]{new InputFilterMinMax("1", "90")});
        // dropdown prefs
        boolean legislationShow = sharedPref.getBoolean("legislationShow", true);
        legislationRow.setVisibility(legislationShow ? View.VISIBLE : View.GONE);
        boolean techniqueShow = sharedPref.getBoolean("techniqueShow", true);
        techniqueRow.setVisibility(techniqueShow ? View.VISIBLE : View.GONE);
        // exam settings prefs
        //int numberOfQuestions = sharedPref.getInt("numberOfQuestions", 20);
        //questionEditText.setText(String.valueOf(numberOfQuestions));
        showRespSwitch.setChecked(sharedPref.getBoolean("showResponces", false));
        malusSwitch.setChecked(sharedPref.getBoolean("malusEnable", true));
        timerSwitch.setChecked(sharedPref.getBoolean("timerEnable", false));
        int examTime = sharedPref.getInt("examTime", 20);
        tempsEditText.setText(String.valueOf(examTime));
        // themes preferences

        String Themejson = sharedPref.getString("ThemeJson", "[]");
        ThemeList = new ArrayList<>();
        ThemeList = new Gson().fromJson(Themejson, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        for (int theme : ThemeList) {
            switch (theme) {
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
                case Examen.groupementsDeResistances:
                    resistancesGroupesCheckBox.setChecked(true);
                    break;
                case Examen.diodesEtTransistors:
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
        updateNbrofQSpinner();


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
                updateNbrofQSpinner();
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
                updateNbrofQSpinner();
            }
        });


        // select all tech/legisl
        legislationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                codeQCheckBox.setChecked(isChecked);
                emissionCheckBox.setChecked(isChecked);
                adaptationCheckBox.setChecked(isChecked);
                epellationCheckBox.setChecked(isChecked);
                cemCheckBox.setChecked(isChecked);
                antennesCheckBox.setChecked(isChecked);
                sanctionsCheckBox.setChecked(isChecked);
                messagesCheckBox.setChecked(isChecked);
                indicatifsCheckBox.setChecked(isChecked);
                entrainementCheckBox.setChecked(isChecked);
                tempsEditText.setText("15");
                updateNbrofQSpinner();
            }
        });
        techniqueCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lignesCheckBox.setChecked(isChecked);
                etagesRFCheckBox.setChecked(isChecked);
                resistancesGroupesCheckBox.setChecked(isChecked);
                ampliCheckBox.setChecked(isChecked);
                transfoCheckBox.setChecked(isChecked);
                alternatifCheckBox.setChecked(isChecked);
                synoptiquesCheckBox.setChecked(isChecked);
                resistancesCouleursCheckBox.setChecked(isChecked);
                electriciteCheckBox.setChecked(isChecked);
                condoBobCheckBox.setChecked(isChecked);
                tempsEditText.setText("30");
                updateNbrofQSpinner();
            }
        });

        // start exam button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set exams setting prefs for the next exam
                //int numberOfQuestions = Integer.parseInt(questionEditText.getText().toString());
                //sharedEditor.putInt("numberOfQuestions", numberOfQuestions);
                Log.w("debug", String.valueOf(tempsEditText.getText()));
                boolean showResponces = showRespSwitch.isChecked();
                sharedEditor.putBoolean("showResponces", showResponces);
                boolean malusEnable = malusSwitch.isChecked();
                sharedEditor.putBoolean("malusEnable", malusEnable);
                boolean timerEnable = timerSwitch.isChecked();
                sharedEditor.putBoolean("timerEnable", timerEnable);


                //  set exams themes prefs for the next exam
                ThemeList = new ArrayList<>();
                if (codeQCheckBox.isChecked()) {
                    ThemeList.add(Examen.codeQ);
                }
                if (emissionCheckBox.isChecked()) {
                    ThemeList.add(Examen.classesEmission);
                }
                if (adaptationCheckBox.isChecked()) {
                    ThemeList.add(Examen.adaptation);
                }
                if (epellationCheckBox.isChecked()) {
                    ThemeList.add(Examen.epellation);
                }
                if (cemCheckBox.isChecked()) {
                    ThemeList.add(Examen.cem);
                }
                if (antennesCheckBox.isChecked()) {
                    ThemeList.add(Examen.longueurOnde);
                }
                if (sanctionsCheckBox.isChecked()) {
                    ThemeList.add(Examen.sanctions);
                }
                if (messagesCheckBox.isChecked()) {
                    ThemeList.add(Examen.exposition);
                }
                if (indicatifsCheckBox.isChecked()) {
                    ThemeList.add(Examen.indicatifs);
                }
                if (entrainementCheckBox.isChecked()) {
                    ThemeList.add(Examen.questionsEntrainement);
                }

                if (lignesCheckBox.isChecked()) {
                    ThemeList.add(Examen.ligneDeTransmis);
                }
                if (etagesRFCheckBox.isChecked()) {
                    ThemeList.add(Examen.etagesRF);
                }
                if (resistancesGroupesCheckBox.isChecked()) {
                    ThemeList.add(Examen.groupementsDeResistances);
                }
                if (ampliCheckBox.isChecked()) {
                    ThemeList.add(Examen.diodesEtTransistors);
                }
                if (transfoCheckBox.isChecked()) {
                    ThemeList.add(Examen.transformateursAmpli);
                }
                if (alternatifCheckBox.isChecked()) {
                    ThemeList.add(Examen.courantsAlternatifs);
                }
                if (synoptiquesCheckBox.isChecked()) {
                    ThemeList.add(Examen.synoptiques);
                }
                if (resistancesCouleursCheckBox.isChecked()) {
                    ThemeList.add(Examen.codeCouleurs);
                }
                if (electriciteCheckBox.isChecked()) {
                    ThemeList.add(Examen.electriciteDeBase);
                }
                if (condoBobCheckBox.isChecked()) {
                    ThemeList.add(Examen.condensateursetBobines);
                }

                sharedEditor.putString("ThemeJson", new Gson().toJson(ThemeList));
                // apply preferences
                sharedEditor.apply();

                if (ThemeList.size() >= 1 && !tempsEditText.getText().toString().equals("")) {
                    int examTime = Integer.parseInt(tempsEditText.getText().toString());
                    sharedEditor.putInt("examTime", examTime);
                    // start new intent examen
                    Intent intent = new Intent(getBaseContext(), ExamenActivity.class);
                    intent.putIntegerArrayListExtra("ThemeList", ThemeList);
                    intent.putExtra("malusEnable", malusEnable);
                    intent.putExtra("showResponces", showResponces);
                    intent.putExtra("numberOfQuestions", Integer.parseInt(nbrQSpinner.getSelectedItem().toString()));
                    intent.putExtra("examTimerEnable", timerEnable);
                    intent.putExtra("timer", examTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Selectionnez un theme et un temps valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateNbrofQSpinner(View view) {

        int themeRegistered = 0;
        if (codeQCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (emissionCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (adaptationCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (epellationCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (cemCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (antennesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (sanctionsCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (messagesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (indicatifsCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (entrainementCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (lignesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (etagesRFCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (resistancesGroupesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (ampliCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (transfoCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (alternatifCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (synoptiquesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (resistancesCouleursCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (electriciteCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (condoBobCheckBox.isChecked()) {
            themeRegistered++;
        }
        legislationCheckBox.setChecked(codeQCheckBox.isChecked() && emissionCheckBox.isChecked() && adaptationCheckBox.isChecked() && epellationCheckBox.isChecked() && cemCheckBox.isChecked()
                && antennesCheckBox.isChecked() && sanctionsCheckBox.isChecked() && messagesCheckBox.isChecked() && indicatifsCheckBox.isChecked() && entrainementCheckBox.isChecked());
        techniqueCheckBox.setChecked(lignesCheckBox.isChecked() && etagesRFCheckBox.isChecked() && resistancesGroupesCheckBox.isChecked() && ampliCheckBox.isChecked() && transfoCheckBox.isChecked()
                && alternatifCheckBox.isChecked() && synoptiquesCheckBox.isChecked() && resistancesCouleursCheckBox.isChecked() && electriciteCheckBox.isChecked() && condoBobCheckBox.isChecked());

        //Log.w("debug", String.valueOf(themeRegistered) + " themes sont cochés");
        if (themeRegistered == 3) {
            themeRegistered = 6;
        }
        if (themeRegistered < 4) {
            themeRegistered = 4;
        }
        List<String> spinnerArray = new ArrayList<>();
        int nearTwentyPos = 1;
        int delta = 100;
        for (int i = 1; i < 6; i++) {
            spinnerArray.add(String.valueOf(i * themeRegistered));
            if (delta > Math.abs(20 - (i * themeRegistered))) {
                nearTwentyPos = i - 1;
                delta = Math.abs(20 - (i * themeRegistered));
                //Log.w("debug", String.valueOf(nearTwentyPos) + "set position");
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nbrQSpinner.setAdapter(adapter);
        //Log.w("DEBUG", String.valueOf(nearTwentyPos) + "position set");
        nbrQSpinner.setSelection(nearTwentyPos);
    }

    public void updateNbrofQSpinner() {

        int themeRegistered = 0;
        if (codeQCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (emissionCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (adaptationCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (epellationCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (cemCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (antennesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (sanctionsCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (messagesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (indicatifsCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (entrainementCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (lignesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (etagesRFCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (resistancesGroupesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (ampliCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (transfoCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (alternatifCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (synoptiquesCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (resistancesCouleursCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (electriciteCheckBox.isChecked()) {
            themeRegistered++;
        }
        if (condoBobCheckBox.isChecked()) {
            themeRegistered++;
        }
        legislationCheckBox.setChecked(codeQCheckBox.isChecked() && emissionCheckBox.isChecked() && adaptationCheckBox.isChecked() && epellationCheckBox.isChecked() && cemCheckBox.isChecked()
                && antennesCheckBox.isChecked() && sanctionsCheckBox.isChecked() && messagesCheckBox.isChecked() && indicatifsCheckBox.isChecked() && entrainementCheckBox.isChecked());
        techniqueCheckBox.setChecked(lignesCheckBox.isChecked() && etagesRFCheckBox.isChecked() && resistancesGroupesCheckBox.isChecked() && ampliCheckBox.isChecked() && transfoCheckBox.isChecked()
                && alternatifCheckBox.isChecked() && synoptiquesCheckBox.isChecked() && resistancesCouleursCheckBox.isChecked() && electriciteCheckBox.isChecked() && condoBobCheckBox.isChecked());

        //Log.w("debug", String.valueOf(themeRegistered) + " themes sont cochés");
        if (themeRegistered == 3) {
            themeRegistered = 6;
        }
        if (themeRegistered < 4) {
            themeRegistered = 4;
        }
        List<String> spinnerArray = new ArrayList<>();
        int nearTwentyPos = 1;
        int delta = 100;
        for (int i = 1; i < 6; i++) {
            spinnerArray.add(String.valueOf(i * themeRegistered));
            if (delta > Math.abs(20 - (i * themeRegistered))) {
                nearTwentyPos = i - 1;
                delta = Math.abs(20 - (i * themeRegistered));
                //Log.w("debug", String.valueOf(nearTwentyPos) + "set position");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nbrQSpinner.setAdapter(adapter);

        //Log.w("DEBUG", String.valueOf(nearTwentyPos) + "position set");
        nbrQSpinner.setSelection(nearTwentyPos);
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
            case R.id.sycroItem:
                Intent intent2 = new Intent(getBaseContext(), QuestionsDownload.class);
                startActivity(intent2);
                return true;
            case R.id.gotoCoursItem:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://f6kgl-f5kff.fr/formationf6gpx"));
                startActivity(browserIntent);
                /*File file = new File(getBaseContext().getFilesDir(), "COURS.html");
                Intent intent4 = new Intent(ACTION_VIEW);
                intent4.setDataAndType(Uri.fromFile(file), "text/html");*/
                return true;
            case R.id.gotoVideoItem:
                Intent coursIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/F6KGL"));
                startActivity(coursIntent3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
