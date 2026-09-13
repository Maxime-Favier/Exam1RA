package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout legislationRow, techniqueRow;
    TextView legislationTextView, techniqueTextView;
    Button allThemeButton, noThemeButton, startButton;
    CheckBox codeQCheckBox, emissionCheckBox, adaptationCheckBox, epellationCheckBox, cemCheckBox, antennesCheckBox,
            sanctionsCheckBox, messagesCheckBox, indicatifsCheckBox, entrainementCheckBox;
    CheckBox lignesCheckBox, etagesRFCheckBox, resistancesGroupesCheckBox, ampliCheckBox, transfoCheckBox,
            alternatifCheckBox, synoptiquesCheckBox, resistancesCouleursCheckBox, electriciteCheckBox, condoBobCheckBox;
    CheckBox techniqueCheckBox, legislationCheckBox;
    EditText tempsEditText;
    MaterialAutoCompleteTextView nbrQSpinner;
    MaterialSwitch showRespSwitch, timerSwitch;

    ArrayList<Integer> ThemeList;
    SharedPreferences sharedPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        sharedPref = context.getSharedPreferences("UIPref" + String.valueOf(BuildConfig.VERSION_CODE), Context.MODE_PRIVATE);

        setupControls();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPref.getBoolean("firstrun", true)) {
            Log.w("debug", "first run!");
            Intent intent = new Intent(getBaseContext(), QuestionsDownload.class);
            startActivity(intent);
        }
    }

    private void setupControls() {
        // Enregistrement des conteneurs et boutons
        legislationRow = findViewById(R.id.legislationRow);
        techniqueRow = findViewById(R.id.techniqueRow);
        legislationTextView = findViewById(R.id.legislationTextView);
        techniqueTextView = findViewById(R.id.techniqueTextView);
        allThemeButton = findViewById(R.id.allThemeButton);
        noThemeButton = findViewById(R.id.noThemeButton);
        startButton = findViewById(R.id.startButton);
        nbrQSpinner = findViewById(R.id.nbrQSpinner);
        tempsEditText = findViewById(R.id.tempsEditText);
        showRespSwitch = findViewById(R.id.showRespSwitch);
        timerSwitch = findViewById(R.id.timerSwitch);

        // CheckBoxes Législation
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

        // CheckBoxes Technique
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

        tempsEditText.setFilters(new InputFilter[]{new InputFilterMinMax("1", "90")});

        // Visibilité initiale
        legislationRow.setVisibility(sharedPref.getBoolean("legislationShow", true) ? View.VISIBLE : View.GONE);
        techniqueRow.setVisibility(sharedPref.getBoolean("techniqueShow", true) ? View.VISIBLE : View.GONE);

        showRespSwitch.setChecked(sharedPref.getBoolean("showResponces", false));
        timerSwitch.setChecked(sharedPref.getBoolean("timerEnable", false));

        int examTime = sharedPref.getInt("examTime", 20);
        tempsEditText.setText(String.valueOf(examTime));

        // Restauration des thèmes sauvegardés
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

        // Tableau sécurisé des CheckBoxes de thèmes
        CheckBox[] allCheckBoxes = {
                codeQCheckBox, emissionCheckBox, adaptationCheckBox, epellationCheckBox, cemCheckBox,
                antennesCheckBox, sanctionsCheckBox, messagesCheckBox, indicatifsCheckBox, entrainementCheckBox,
                lignesCheckBox, etagesRFCheckBox, resistancesGroupesCheckBox, ampliCheckBox, transfoCheckBox,
                alternatifCheckBox, synoptiquesCheckBox, resistancesCouleursCheckBox, electriciteCheckBox, condoBobCheckBox
        };

        CompoundButton.OnCheckedChangeListener themeChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateNbrofQSpinner();
            }
        };

        for (CheckBox cb : allCheckBoxes) {
            cb.setOnCheckedChangeListener(themeChangeListener);
        }

        updateNbrofQSpinner();

        final SharedPreferences.Editor sharedEditor = sharedPref.edit();

        // Gestion du repli de la section Législation
        if (legislationTextView != null) {
            legislationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (legislationRow != null) {
                        if (legislationRow.getVisibility() == View.GONE) {
                            legislationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up_24dp, 0, R.drawable.ic_book_24dp, 0);
                            legislationRow.setVisibility(View.VISIBLE);
                            sharedEditor.putBoolean("legislationShow", true);
                        } else {
                            legislationTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down_24dp, 0, R.drawable.ic_book_24dp, 0);
                            legislationRow.setVisibility(View.GONE);
                            sharedEditor.putBoolean("legislationShow", false);
                        }
                        sharedEditor.apply();
                    }
                }
            });
        }

        // Gestion du repli de la section Technique
        if (techniqueTextView != null) {
            techniqueTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (techniqueRow != null) {
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
                }
            });
        }

        // Boutons de sélection rapide
        if (allThemeButton != null) {
            allThemeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CheckBox cb : allCheckBoxes) {
                        cb.setChecked(true);
                    }
                    updateNbrofQSpinner();
                }
            });
        }

        if (noThemeButton != null) {
            noThemeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CheckBox cb : allCheckBoxes) {
                        cb.setChecked(false);
                    }
                    updateNbrofQSpinner();
                }
            });
        }

        // CheckBoxes de groupes
        if (legislationCheckBox != null) {
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
        }

        if (techniqueCheckBox != null) {
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
        }

        // Bouton Démarrer

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean showResponces = showRespSwitch != null && showRespSwitch.isChecked();
                sharedEditor.putBoolean("showResponces", showResponces);
                boolean timerEnable = timerSwitch != null && timerSwitch.isChecked();
                sharedEditor.putBoolean("timerEnable", timerEnable);

                ThemeList = new ArrayList<>();
                if (codeQCheckBox != null && codeQCheckBox.isChecked()) ThemeList.add(Examen.codeQ);
                if (emissionCheckBox != null && emissionCheckBox.isChecked()) ThemeList.add(Examen.classesEmission);
                if (adaptationCheckBox != null && adaptationCheckBox.isChecked()) ThemeList.add(Examen.adaptation);
                if (epellationCheckBox != null && epellationCheckBox.isChecked()) ThemeList.add(Examen.epellation);
                if (cemCheckBox != null && cemCheckBox.isChecked()) ThemeList.add(Examen.cem);
                if (antennesCheckBox != null && antennesCheckBox.isChecked()) ThemeList.add(Examen.longueurOnde);
                if (sanctionsCheckBox != null && sanctionsCheckBox.isChecked()) ThemeList.add(Examen.sanctions);
                if (messagesCheckBox != null && messagesCheckBox.isChecked()) ThemeList.add(Examen.exposition);
                if (indicatifsCheckBox != null && indicatifsCheckBox.isChecked()) ThemeList.add(Examen.indicatifs);
                if (entrainementCheckBox != null && entrainementCheckBox.isChecked())
                    ThemeList.add(Examen.questionsEntrainement);
                if (lignesCheckBox != null && lignesCheckBox.isChecked()) ThemeList.add(Examen.ligneDeTransmis);
                if (etagesRFCheckBox != null && etagesRFCheckBox.isChecked()) ThemeList.add(Examen.etagesRF);
                if (resistancesGroupesCheckBox != null && resistancesGroupesCheckBox.isChecked())
                    ThemeList.add(Examen.groupementsDeResistances);
                if (ampliCheckBox != null && ampliCheckBox.isChecked()) ThemeList.add(Examen.diodesEtTransistors);
                if (transfoCheckBox != null && transfoCheckBox.isChecked()) ThemeList.add(Examen.transformateursAmpli);
                if (alternatifCheckBox != null && alternatifCheckBox.isChecked())
                    ThemeList.add(Examen.courantsAlternatifs);
                if (synoptiquesCheckBox != null && synoptiquesCheckBox.isChecked()) ThemeList.add(Examen.synoptiques);
                if (resistancesCouleursCheckBox != null && resistancesCouleursCheckBox.isChecked())
                    ThemeList.add(Examen.codeCouleurs);
                if (electriciteCheckBox != null && electriciteCheckBox.isChecked())
                    ThemeList.add(Examen.electriciteDeBase);
                if (condoBobCheckBox != null && condoBobCheckBox.isChecked())
                    ThemeList.add(Examen.condensateursetBobines);

                sharedEditor.putString("ThemeJson", new Gson().toJson(ThemeList));

                int selectedNbrQ = 20;
                if (nbrQSpinner != null && nbrQSpinner.getText() != null) {
                    String selectedValue = nbrQSpinner.getText().toString();
                    if (!selectedValue.isEmpty()) {
                        try {
                            selectedNbrQ = Integer.parseInt(selectedValue);
                        } catch (NumberFormatException e) {
                            selectedNbrQ = 20;
                        }
                    }
                }
                sharedEditor.putInt("numberOfQuestions", selectedNbrQ);
                sharedEditor.apply();

                if (ThemeList.size() >= 1 && !tempsEditText.getText().toString().equals("")) {
                    int examTime = Integer.parseInt(tempsEditText.getText().toString());
                    sharedEditor.putInt("examTime", examTime);
                    sharedEditor.apply();

                    Intent intent = new Intent(getBaseContext(), ExamenActivity.class);
                    intent.putIntegerArrayListExtra("ThemeList", ThemeList);
                    intent.putExtra("showResponces", showResponces);
                    intent.putExtra("numberOfQuestions", selectedNbrQ);
                    intent.putExtra("examTimerEnable", timerEnable);
                    intent.putExtra("timer", examTime);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Sélectionnez un thème et un temps valide", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateNbrofQSpinner(View view) {
        updateNbrofQSpinner();
    }

    public void updateNbrofQSpinner() {
        //if (nbrQSpinner == null) return;

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
        int nearTwentyPos = 0;
        int delta = 100;
        int savedNumQ = sharedPref != null ? sharedPref.getInt("numberOfQuestions", 20) : 20;
        int savedMatchPos = -1;

        for (int i = 1; i <= 5; i++) {
            int val = i * themeRegistered;
            spinnerArray.add(String.valueOf(val));

            if (val == savedNumQ) {
                savedMatchPos = i - 1;
            }

            if (delta > Math.abs(20 - val)) {
                nearTwentyPos = i - 1;
                delta = Math.abs(20 - val);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, spinnerArray);
        nbrQSpinner.setAdapter(adapter);

        if (savedMatchPos != -1 && savedMatchPos < spinnerArray.size()) {
            nbrQSpinner.setText(spinnerArray.get(savedMatchPos), false);
        } else if (nearTwentyPos < spinnerArray.size()) {
            nbrQSpinner.setText(spinnerArray.get(nearTwentyPos), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.remerciementItem) {
            startActivity(new Intent(getBaseContext(), Remerciements.class));
            return true;
        } else if (itemId == R.id.opensourceItem) {
            startActivity(new Intent(getBaseContext(), OpenSource.class));
            return true;
        } else if (itemId == R.id.sycroItem) {
            startActivity(new Intent(getBaseContext(), QuestionsDownload.class));
            return true;
        } else if (itemId == R.id.gotoCoursItem) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://f6kgl-f5kff.fr/formationf6gpx")));
            return true;
        } else if (itemId == R.id.gotoVideoItem) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/user/F6KGL")));
            return true;
        } else if (itemId == R.id.gotoCoursHTMLItem) {
            startActivity(new Intent(getBaseContext(), CoursViewer.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}