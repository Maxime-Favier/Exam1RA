package dev.favier.exam1radioamateur;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class ExamenResults extends AppCompatActivity {

    TextView examStatusTextView, examConcluTextView, totalTextView, totalLegTextView,
            totalTechTextView, totalTempsTextView, totalCorrectTextView, totalIncorrectsTextView,
            totalSsRepTextView;
    PieChart pieChart;
    FlexboxLayout flexResults;

    ResultCalculator resultCalculator;
    long timeSpent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen_results);

        setupControls();

        Bundle bundle = getIntent().getExtras();
        resultCalculator = (ResultCalculator) bundle.getSerializable("exam");
        timeSpent = bundle.getLong("timeSpent");
        Log.w("debug", String.valueOf(timeSpent) + "time spent");

        printResults();
        printQuestions();
    }

    private void setupControls() {
        // setup widgets
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
        flexResults = findViewById(R.id.flexResult);

        // configure piechart
        pieChart.setUsePercentValues(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setRotationEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void printResults() {
        // examen bon ou raté
        if (resultCalculator.examGood()) {
            examStatusTextView.setText(R.string.examReussi);
            examStatusTextView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorOk));
        } else {
            examStatusTextView.setText(R.string.examRate);
            examStatusTextView.setTextColor(Color.RED);
        }
        examConcluTextView.setText(getResources().getString(R.string.exam_Conclu, resultCalculator.getNbrOfQuestions()));
        totalTextView.setText(getResources().getString(R.string.total, resultCalculator.pointCalculation(), resultCalculator.maxPoints()));

        String sec;
        if(timeSpent % 60 <= 9){
            sec = "0" + (timeSpent%60);
        }else {
            sec = String.valueOf(timeSpent % 60);
        }
        totalTempsTextView.setText(getResources().getString(R.string.temps, (int) (timeSpent / 60), sec));
        totalLegTextView.setText(getResources().getString(R.string.totalLeg, resultCalculator.pointsLegislation()));
        totalTechTextView.setText(getResources().getString(R.string.totalTech, resultCalculator.pointsTechnique()));
        totalCorrectTextView.setText(getResources().getString(R.string.corrects, resultCalculator.getNbrOfCorrect()));
        totalIncorrectsTextView.setText(getResources().getString(R.string.incorrects, resultCalculator.getNbrOfIncorrect()));
        totalSsRepTextView.setText(getResources().getString(R.string.ss_Rep, resultCalculator.getNbrSsRep()));

        //populate pie chart
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        ArrayList<Integer> colorArray = new ArrayList<>();
        if (resultCalculator.getNbrOfCorrect() > 0) {
            dataVals.add(new PieEntry(resultCalculator.getNbrOfCorrect(), "Correct"));
            colorArray.add(ContextCompat.getColor(getApplicationContext(), R.color.colorOk));
        }
        if (resultCalculator.getNbrOfIncorrect() > 0) {
            dataVals.add(new PieEntry(resultCalculator.getNbrOfIncorrect(), "Incorrect"));
            colorArray.add(ContextCompat.getColor(getApplicationContext(), R.color.colorBtnDanger));
        }
        if (resultCalculator.getNbrSsRep() > 0) {
            dataVals.add(new PieEntry(resultCalculator.getNbrSsRep(), "sans réponses"));
            colorArray.add(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        PieDataSet pieDataSet = new PieDataSet(dataVals, "");
        pieDataSet.setColors(colorArray);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getApplicationContext().getResources().getDisplayMetrics()));
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void printQuestions() {
        ArrayList<TextView> questionTextViews = new ArrayList<>();

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 36, getApplicationContext().getResources().getDisplayMetrics());
        int marginpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getApplicationContext().getResources().getDisplayMetrics());
        int marginTppx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, getApplicationContext().getResources().getDisplayMetrics());
        for (int i = 0; i < resultCalculator.getNbrOfQuestions(); i++) {
            final Question question = resultCalculator.getQuestion(i);

            TextView textView = new TextView(this);

            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(px, px);
            layoutParams.setMargins(0, marginTppx, 0, 0);
            layoutParams.setMarginStart(marginpx);
            layoutParams.setMarginEnd(marginpx);
            textView.setLayoutParams(layoutParams);
            textView.setText(String.valueOf(i + 1));
            textView.setTextColor(Color.WHITE);

            textView.setContentDescription(question.getQuestion());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), QuestionViewer.class);
                    intent.putExtra("question", question);
                    startActivity(intent);
                }
            });
            if(question.goodResponse() == Question.bonneReponse){
                textView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorOk));
            }else if(question.goodResponse() == Question.mauvaiseReponse){
                textView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorBtnDanger));
            }else {
                textView.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
            }

            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setBackgroundResource(R.drawable.circle_background);
            textView.setGravity(Gravity.CENTER);

            questionTextViews.add(textView);
            flexResults.addView(textView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.results_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gotoHomeItem:
                navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // empeche de retouner aux question d'examens et va à l'accueil
        navigateUpTo(new Intent(getBaseContext(), MainActivity.class));
        //super.onBackPressed();
    }
}
