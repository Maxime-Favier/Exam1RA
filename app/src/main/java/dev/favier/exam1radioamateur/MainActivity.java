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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout legislationRow, techniqueRow;
    TextView legislationTextView, techniqueTextView;
    Button allThemeButton, noThemeButton, startButton;
    private CheckBox[] legislationCheckBoxes;
    private CheckBox[] techniqueCheckBoxes;
    private final Map<Integer, CheckBox> themeMap = new HashMap<>();
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
        legislationCheckBoxes = new CheckBox[]{codeQCheckBox, emissionCheckBox, adaptationCheckBox, epellationCheckBox, cemCheckBox, antennesCheckBox, sanctionsCheckBox,
                messagesCheckBox, indicatifsCheckBox, entrainementCheckBox};
        legislationCheckBox = findViewById(R.id.legislationCheckBox);

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
        techniqueCheckBoxes = new CheckBox[]{lignesCheckBox, etagesRFCheckBox, resistancesGroupesCheckBox, ampliCheckBox, transfoCheckBox, alternatifCheckBox, synoptiquesCheckBox,
                resistancesCouleursCheckBox, electriciteCheckBox, condoBobCheckBox};
        techniqueCheckBox = findViewById(R.id.techniqueCheckBox);

        tempsEditText.setFilters(new InputFilter[]{new InputFilterMinMax("1", "90")});


        showRespSwitch.setChecked(sharedPref.getBoolean("showResponces", false));
        timerSwitch.setChecked(sharedPref.getBoolean("timerEnable", false));

        int examTime = sharedPref.getInt("examTime", 20);
        tempsEditText.setText(String.valueOf(examTime));

        // Restauration des thèmes sauvegardés
        setupThemeMap();
        String themeJson = sharedPref.getString("ThemeJson", "[]");
        List<Integer> themeList = new Gson().fromJson(themeJson, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        if (themeList != null) {
            for (int themeId : themeList) {
                CheckBox checkBox = themeMap.get(themeId);
                if (checkBox != null) {
                    checkBox.setChecked(true);
                }
            }
        }

        CompoundButton.OnCheckedChangeListener themeChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateNbrofQSpinner();
            }
        };

        for (CheckBox cb : legislationCheckBoxes) {
            cb.setOnCheckedChangeListener(themeChangeListener);
        }
        for (CheckBox cb : techniqueCheckBoxes) {
            cb.setOnCheckedChangeListener(themeChangeListener);
        }

        updateNbrofQSpinner();

        //gestion du repli des catégories
        final SharedPreferences.Editor sharedEditor = sharedPref.edit();
        boolean isLegislationShow = sharedPref.getBoolean("legislationShow", true);
        boolean isTechniqueShow = sharedPref.getBoolean("techniqueShow", true);
        updateSectionVisibility(legislationTextView, legislationRow, isLegislationShow, R.drawable.ic_book_24dp);
        updateSectionVisibility(techniqueTextView, techniqueRow, isTechniqueShow, R.drawable.ic_build_24dp);
        legislationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newVisibility = legislationRow.getVisibility() == View.GONE;

                updateSectionVisibility(legislationTextView, legislationRow, newVisibility, R.drawable.ic_book_24dp);
                sharedEditor.putBoolean("legislationShow", newVisibility).apply();
            }
        });
        techniqueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean newVisibility = techniqueRow.getVisibility() == View.GONE;

                updateSectionVisibility(techniqueTextView, techniqueRow, newVisibility, R.drawable.ic_build_24dp);
                sharedEditor.putBoolean("techniqueShow", newVisibility).apply();
            }
        });

        // Boutons de sélection rapide
        allThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox cb : legislationCheckBoxes) {
                    cb.setChecked(true);
                }
                for (CheckBox cb : techniqueCheckBoxes) {
                    cb.setChecked(true);
                }
                updateNbrofQSpinner();
            }
        });
        noThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckBox cb : legislationCheckBoxes) {
                    cb.setChecked(false);
                }
                for (CheckBox cb : techniqueCheckBoxes) {
                    cb.setChecked(false);
                }
                updateNbrofQSpinner();
            }
        });


        // CheckBoxes de groupes
        legislationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    for (CheckBox cb : legislationCheckBoxes) {
                        cb.setChecked(isChecked);
                    }
                    //tempsEditText.setText("15");
                    updateNbrofQSpinner();
                }
            }
        });


        techniqueCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    for (CheckBox cb : techniqueCheckBoxes) {
                        cb.setChecked(isChecked);
                    }
                    //tempsEditText.setText("30");
                    updateNbrofQSpinner();
                }
            }
        });


        // Bouton Démarrer
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean showResponces = showRespSwitch != null && showRespSwitch.isChecked();
                sharedEditor.putBoolean("showResponces", showResponces);
                boolean timerEnable = timerSwitch != null && timerSwitch.isChecked();
                sharedEditor.putBoolean("timerEnable", timerEnable);

                ArrayList<Integer> themeListfrmBtn = new ArrayList<>();
                for (Map.Entry<Integer, CheckBox> entry : themeMap.entrySet()) {
                    CheckBox checkBox = entry.getValue();
                    if (checkBox != null && checkBox.isChecked()) {
                        themeListfrmBtn.add(entry.getKey());
                    }
                }

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

                if (themeListfrmBtn.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Sélectionnez au moins un thème", Toast.LENGTH_SHORT).show();
                    return;
                }

                int examTime = sharedPref.getInt("examTime", 20);

                if (timerEnable) {
                    String tempsStr = tempsEditText.getText().toString().trim();
                    if (tempsStr.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Sélectionnez un temps valide", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        try {
                            examTime = Integer.parseInt(tempsStr);
                        } catch (NumberFormatException e) {
                            Toast.makeText(MainActivity.this, "Sélectionnez un temps valide", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // Le temps doit être supérieur à 0
                    if (examTime <= 0) {
                        Toast.makeText(MainActivity.this, "Le temps doit être supérieur à 0 min", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Sauvegarde groupée unique dans SharedPreferences
                sharedEditor.putString("ThemeJson", new Gson().toJson(themeListfrmBtn));
                sharedEditor.putInt("examTime", examTime);
                sharedEditor.apply();

                // Lancement unique de l'Activity
                Intent intent = new Intent(MainActivity.this, ExamenActivity.class);
                intent.putIntegerArrayListExtra("ThemeList", themeListfrmBtn);
                intent.putExtra("showResponces", showResponces);
                intent.putExtra("numberOfQuestions", selectedNbrQ);
                intent.putExtra("examTimerEnable", timerEnable);
                intent.putExtra("timer", examTime);
                startActivity(intent);
            }
        });

    }

    private void updateSectionVisibility(TextView textView, View rowView, boolean isVisible, int rightIconRes) {
        int arrowIcon = isVisible ? R.drawable.ic_arrow_drop_up_24dp : R.drawable.ic_arrow_drop_down_24dp;
        textView.setCompoundDrawablesWithIntrinsicBounds(arrowIcon, 0, rightIconRes, 0);
        rowView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setupThemeMap() {
        themeMap.put(Examen.codeQ, codeQCheckBox);
        themeMap.put(Examen.classesEmission, emissionCheckBox);
        themeMap.put(Examen.adaptation, adaptationCheckBox);
        themeMap.put(Examen.epellation, epellationCheckBox);
        themeMap.put(Examen.cem, cemCheckBox);
        themeMap.put(Examen.longueurOnde, antennesCheckBox);
        themeMap.put(Examen.sanctions, sanctionsCheckBox);
        themeMap.put(Examen.exposition, messagesCheckBox);
        themeMap.put(Examen.indicatifs, indicatifsCheckBox);
        themeMap.put(Examen.questionsEntrainement, entrainementCheckBox);
        themeMap.put(Examen.ligneDeTransmis, lignesCheckBox);
        themeMap.put(Examen.etagesRF, etagesRFCheckBox);
        themeMap.put(Examen.groupementsDeResistances, resistancesGroupesCheckBox);
        themeMap.put(Examen.diodesEtTransistors, ampliCheckBox);
        themeMap.put(Examen.transformateursAmpli, transfoCheckBox);
        themeMap.put(Examen.courantsAlternatifs, alternatifCheckBox);
        themeMap.put(Examen.synoptiques, synoptiquesCheckBox);
        themeMap.put(Examen.codeCouleurs, resistancesCouleursCheckBox);
        themeMap.put(Examen.electriciteDeBase, electriciteCheckBox);
        themeMap.put(Examen.condensateursetBobines, condoBobCheckBox);
    }

    private boolean areAllChecked(CheckBox[] checkBoxes) {
        for (CheckBox cb : checkBoxes) {
            if (!cb.isChecked()) return false;
        }
        return true;
    }

    public void updateNbrofQSpinner(View view) {
        updateNbrofQSpinner();
    }

    public void updateNbrofQSpinner() {
        //if (nbrQSpinner == null) return;

        int themeRegistered = 0;
        for (CheckBox cb : legislationCheckBoxes) {
            if (cb.isChecked()) {
                themeRegistered++;
            }
        }
        for (CheckBox cb : techniqueCheckBoxes) {
            if (cb.isChecked()) {
                themeRegistered++;
            }
        }

        legislationCheckBox.setChecked(areAllChecked(legislationCheckBoxes));
        techniqueCheckBox.setChecked(areAllChecked(techniqueCheckBoxes));

        Log.w("debug", String.valueOf(themeRegistered) + " themes sont cochés");
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
        nbrQSpinner.clearFocus();
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