package dev.favier.exam1radioamateur;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Affiche une des questions via {@link ExamenResults} en réutilisant l'adaptateur principal.
 */
public class QuestionViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_viewer);

        // 1. Configuration de la TopAppBar
        MaterialToolbar toolbar = findViewById(R.id.topToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // 2. Initialisation du RecyclerView
        RecyclerView recyclerView = findViewById(R.id.questionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Récupération des données et liaison avec l'Adapter
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Question question;

            // Pour Android 13 et supérieur (API 33+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                question = bundle.getSerializable("question", Question.class);
            } else {
                // Pour les anciennes versions
                @SuppressWarnings("deprecation")
                Question legacyQuestion = (Question) bundle.getSerializable("question");
                question = legacyQuestion;
            }

            if (question != null) {
                question.setReponseAsked(true);

                // Création d'un Examen "jetable" pour satisfaire le constructeur de QuestionAdapter
                // et éviter tout crash si un listener interne tente de sauvegarder un état.
                ArrayList<Integer> dummyThemeList = new ArrayList<>();
                dummyThemeList.add(question.getThemeID());
                Examen dummyExamen = new Examen(this, dummyThemeList, 1);

                // Instanciation en passant une liste contenant notre unique question
                QuestionAdapter adapter = new QuestionAdapter(
                        this,
                        Collections.singletonList(question),
                        dummyExamen,
                        true, // showResponces activé
                        true
                );

                recyclerView.setAdapter(adapter);
            }
        }
    }
}