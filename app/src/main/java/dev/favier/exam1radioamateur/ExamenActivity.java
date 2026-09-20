package dev.favier.exam1radioamateur;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Affiche l'interface des questions de l'examen
 */
public class ExamenActivity extends AppCompatActivity {

    private TextView timerTextView;
    private RecyclerView questionsRecyclerView;
    private QuestionAdapter adapter;

    private Examen examen;
    private CountDownTimer countDownTimer;
    private Timer timer;
    private int indexMaxQuestion;
    private boolean examTimerEnable;
    private boolean showResponces;
    private int examTime;
    private long timeLeft = 0;
    private long timeSpent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);

        timerTextView = findViewById(R.id.timerTextView);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);

        // Optimisation des performances du RecyclerView
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setHasFixedSize(true);

        // 1. Configuration de la Toolbar pour y attacher le menu (la coche)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);

        timerTextView = findViewById(R.id.timerTextView);
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);

        // Récupération de la vue de la carte entière du minuteur
        //View timerCardView = findViewById(R.id.timerCardView);
        View timerContainer = findViewById(R.id.timerContainer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<Integer> themeList = bundle.getIntegerArrayList("ThemeList");
            indexMaxQuestion = bundle.getInt("numberOfQuestions") - 1;
            int numberOfQuestionParTheme = (indexMaxQuestion + 1) / themeList.size();

            examTimerEnable = bundle.getBoolean("examTimerEnable");
            examTime = bundle.getInt("timer");
            showResponces = bundle.getBoolean("showResponces");

            examen = new Examen(this, themeList, numberOfQuestionParTheme);
            if (!examTimerEnable) {
                timerContainer.setVisibility(View.GONE);
            } else {
                timerContainer.setVisibility(View.VISIBLE);
            }
            setupTimer();

            // Génération asynchrone des questions
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        examen.genrateQuestions();

                        final List<Question> questionList = new ArrayList<>();
                        for (int i = 0; i <= indexMaxQuestion; i++) {
                            questionList.add(examen.getQuestion(i));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new QuestionAdapter(ExamenActivity.this, questionList, examen, showResponces);
                                questionsRecyclerView.setAdapter(adapter);
                            }
                        });
                    } finally {
                        executor.shutdown();
                    }
                }
            });
        }

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamenActivity.this);
                builder.setCancelable(true);
                builder.setTitle(R.string.stopExam);
                builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                    if (examTimerEnable && countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    if (timer != null) timer.cancel();
                    finish();
                });
                builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());

                if (!isFinishing()) {
                    builder.show();
                }
            }
        });
    }

    private void setupTimer() {
        if (examTimerEnable) {
            countDownTimer = new CountDownTimer((examTime * 60000L), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int min = (int) (millisUntilFinished / 1000) / 60;
                    int sec = (int) (millisUntilFinished / 1000) % 60;

                    // Avertissement de la dernière minute (couleurs Material 3)
                    if (min == 1 && sec == 0) {
                        int errorColor = MaterialColors.getColor(timerTextView, R.attr.colorError, Color.RED);
                        int onErrorColor = MaterialColors.getColor(timerTextView, R.attr.colorOnError, Color.WHITE);
                        timerTextView.setTextColor(onErrorColor);
                        timerTextView.setBackgroundColor(errorColor);
                    }

                    String secStr = (sec <= 9) ? "0" + sec : String.valueOf(sec);
                    timerTextView.setText(min + "'" + secStr + '"');
                    timeLeft = millisUntilFinished;
                }

                @Override
                public void onFinish() {
                    stopExam(true);
                }
            };
            countDownTimer.start();
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeSpent++;
            }
        }, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.examen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.terminerItem) {
            stopExam(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void stopExam(boolean force) {
        if (!force) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle(R.string.terminer);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    executeStop();
                }
            });
            builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel());
            if (!isFinishing()) {
                builder.show();
            }
        } else {
            executeStop();
        }
    }

    private void executeStop() {
        if (examTimerEnable && countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (timer != null) timer.cancel();

        Intent intent = new Intent(getBaseContext(), ExamenResults.class);
        intent.putExtra("exam", examen.getResults());
        intent.putExtra("timeSpent", timeSpent);
        startActivity(intent);
        finish();
    }

}

