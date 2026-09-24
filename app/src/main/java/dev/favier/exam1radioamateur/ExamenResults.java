package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;

public class ExamenResults extends AppCompatActivity {

    private TextView examStatusTextView, examConcluTextView, totalTextView, totalLegTextView,
            totalTechTextView, totalTempsTextView, totalCorrectTextView, totalIncorrectsTextView,
            totalSsRepTextView;
    private PieChart pieChart;
    private androidx.constraintlayout.helper.widget.Flow flowResult;
    private androidx.constraintlayout.widget.ConstraintLayout mainConstraintLayout;

    private ResultCalculator resultCalculator;
    private long timeSpent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen_results);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateUpTo(new Intent(ExamenResults.this, MainActivity.class));
            }
        });
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);

        // Gérer les clics sur les items du menu
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.gotoHomeItem) {
                // Votre logique de retour à l'accueil
                Intent intent = new Intent(ExamenResults.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });

        topAppBar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        setupControls();
        extractIntentData();

        if (resultCalculator != null) {
            printResults();
            printQuestions();
        }
    }

    private void extractIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+)
                resultCalculator = bundle.getSerializable("exam", ResultCalculator.class);
            } else {
                // Versions antérieures
                @SuppressWarnings("deprecation")
                ResultCalculator legacyCalculator = (ResultCalculator) bundle.getSerializable("exam");
                resultCalculator = legacyCalculator;
            }

            timeSpent = bundle.getLong("timeSpent", 0);
            Log.d("ExamenResults", "Time spent: " + timeSpent + "s");
        }
    }
    private void setupControls() {
        examStatusTextView = findViewById(R.id.examStatusTextView);
        examConcluTextView = findViewById(R.id.examConcluTextView);
        totalTextView = findViewById(R.id.totalTextView);
        totalLegTextView = findViewById(R.id.totalLegTextView);
        totalTechTextView = findViewById(R.id.totalTechTextView);
        totalTempsTextView = findViewById(R.id.totalTempsTextView);
        totalCorrectTextView = findViewById(R.id.totalCorrectTextView);
        totalIncorrectsTextView = findViewById(R.id.totalIncorrectsTextView);
        totalSsRepTextView = findViewById(R.id.totalSsRepTextView);
        pieChart = findViewById(R.id.pieChart);
        flowResult = findViewById(R.id.flowResult);
        mainConstraintLayout = findViewById(R.id.mainConstraintLayout);

        // Configuration du PieChart
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setRotationEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);
        l.setForm(Legend.LegendForm.CIRCLE);
        // Adaptation de la couleur de la légende au thème sombre/clair
        l.setTextColor(MaterialColors.getColor(this, R.attr.colorOnSurface, Color.BLACK));
    }

    private void printResults() {
        // Statut de l'examen
        if (resultCalculator.examGood()) {
            examStatusTextView.setText(R.string.examReussi);
            examStatusTextView.setTextColor(MaterialColors.getColor(this, R.attr.colorPrimary, Color.GREEN));
        } else {
            examStatusTextView.setText(R.string.examRate);
            examStatusTextView.setTextColor(MaterialColors.getColor(this, R.attr.colorError, Color.RED));
        }

        examConcluTextView.setText(getString(R.string.exam_Conclu, resultCalculator.getNbrOfQuestions()));
        totalTextView.setText(getString(R.string.total, resultCalculator.pointCalculation(), resultCalculator.maxPoints()));

        // Formatage du temps (mm:ss)
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", timeSpent / 60, timeSpent % 60);
        totalTempsTextView.setText(getString(R.string.temps, timeFormatted));

        totalLegTextView.setText(getString(R.string.totalLeg, resultCalculator.pointsLegislation()));
        totalTechTextView.setText(getString(R.string.totalTech, resultCalculator.pointsTechnique()));
        totalCorrectTextView.setText(getString(R.string.corrects, resultCalculator.getNbrOfCorrect()));
        totalIncorrectsTextView.setText(getString(R.string.incorrects, resultCalculator.getNbrOfIncorrect()));
        totalSsRepTextView.setText(getString(R.string.ss_Rep, resultCalculator.getNbrSsRep()));

        setupPieChartData();
    }

    private void setupPieChartData() {
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        ArrayList<Integer> colorArray = new ArrayList<>();

        if (resultCalculator.getNbrOfCorrect() > 0) {
            dataVals.add(new PieEntry(
                    resultCalculator.getNbrOfCorrect(),
                    getString(R.string.corrects, resultCalculator.getNbrOfCorrect())
            ));
            colorArray.add(MaterialColors.getColor(this, R.attr.colorPrimary, Color.GREEN));
        }
        if (resultCalculator.getNbrOfIncorrect() > 0) {
            dataVals.add(new PieEntry(
                    resultCalculator.getNbrOfIncorrect(),
                    getString(R.string.incorrects, resultCalculator.getNbrOfIncorrect())
            ));
            colorArray.add(MaterialColors.getColor(this, R.attr.colorError, Color.RED));
        }
        if (resultCalculator.getNbrSsRep() > 0) {
            dataVals.add(new PieEntry(
                    resultCalculator.getNbrSsRep(),
                    getString(R.string.ss_Rep, resultCalculator.getNbrSsRep())
            ));
            colorArray.add(MaterialColors.getColor(this, R.attr.colorTertiaryContainer, Color.LTGRAY));
        }

        PieDataSet pieDataSet = new PieDataSet(dataVals, "");
        pieDataSet.setColors(colorArray);
        pieDataSet.setSliceSpace(3f); // Espacement entre les parts
        pieDataSet.setSelectionShift(5f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void printQuestions() {
        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        int sizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        // Tableau pour stocker les IDs des vues générées
        int[] referencedIds = new int[resultCalculator.getNbrOfQuestions()];

        for (int i = 0; i < resultCalculator.getNbrOfQuestions(); i++) {
            final Question question = resultCalculator.getQuestion(i);

            TextView textView = new TextView(this);
            textView.setId(View.generateViewId());

            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams layoutParams =
                    new androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(sizePx, sizePx);
            textView.setLayoutParams(layoutParams);

            textView.setText(String.valueOf(i + 1));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setContentDescription(question.getQuestion());

            // Rendre le composant cliquable avec un ripple effect standard
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true);
            textView.setForeground(ContextCompat.getDrawable(this, typedValue.resourceId));
            textView.setClickable(true);
            textView.setFocusable(true);

            textView.setOnClickListener(v -> {
                Intent intent = new Intent(ExamenResults.this, QuestionViewer.class);
                intent.putExtra("question", question);
                startActivity(intent);
            });

            int backgroundColor;
            int textColor;

            // Définition des couleurs
            if (question.goodResponse() == Question.bonneReponse) {
                backgroundColor = MaterialColors.getColor(this, R.attr.colorPrimaryContainer, Color.BLUE);
                textColor = MaterialColors.getColor(this, R.attr.colorOnPrimaryContainer, Color.BLACK);
            } else if (question.goodResponse() == Question.mauvaiseReponse) {
                backgroundColor = MaterialColors.getColor(this, R.attr.colorErrorContainer, Color.RED);
                textColor = MaterialColors.getColor(this, R.attr.colorOnErrorContainer, Color.WHITE);
            } else {
                backgroundColor = MaterialColors.getColor(this, R.attr.colorSurfaceVariant, Color.LTGRAY);
                textColor = MaterialColors.getColor(this, R.attr.colorOnSurfaceVariant, Color.BLACK);
            }

            textView.setTextColor(textColor);

            // Création d'un fond circulaire
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(backgroundColor);
            textView.setBackground(shape);

            // Ajouter la vue au ConstraintLayout
            mainConstraintLayout.addView(textView);
            referencedIds[i] = textView.getId();
        }
        flowResult.setReferencedIds(referencedIds);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.results_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.gotoHomeItem) {
            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clear glide cache & storage
        Glide.get(getApplicationContext()).clearMemory();
        Executors.newSingleThreadExecutor().execute(() -> {
            Glide.get(getApplicationContext()).clearDiskCache();
        });
    }
}