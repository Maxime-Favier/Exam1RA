package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.radiobutton.MaterialRadioButton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final Context context;
    private final List<Question> questionList;
    private final Examen examen;
    private final boolean showResponces;

    public QuestionAdapter(Context context, List<Question> questionList, Examen examen, boolean showResponces) {
        this.context = context;
        this.questionList = questionList;
        this.examen = examen;
        this.showResponces = showResponces;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);

        // 1. Initialiser les Textes (Numéro, Thème)
        holder.numQChip.setText("# " + question.getNumero());
        holder.themeQTextView.setText(getThemeName(question.getThemeID()));

        // 2. Initialiser l'image
        File file = new File(context.getFilesDir(), question.getNumero() + ".png");
        if (file.exists()) {
            holder.questionImageView.setImageURI(Uri.fromFile(file));
            holder.questionImageView.setContentDescription(question.getQuestion());
            holder.questionImageView.setVisibility(View.VISIBLE);
        } else {
            holder.questionImageView.setVisibility(View.GONE);
        }

        // 3. Définir les textes des propositions
        ArrayList<String> propositions = question.getPropositions();
        holder.propo1.setText(propositions.get(0));
        holder.propo2.setText(propositions.get(1));
        holder.propo3.setText(propositions.get(2));
        holder.propo4.setText(propositions.get(3));

        // IMPORTANT : Retirer temporairement le listener pour ne pas fausser les données lors du recyclage de la vue
        holder.propoRadioGroupe.setOnCheckedChangeListener(null);
        holder.propoRadioGroupe.clearCheck();

        // 4. Restaurer la réponse de l'utilisateur si existante
        switch (question.getUserReponse()) {
            case 0: holder.propo1.setChecked(true); break;
            case 1: holder.propo2.setChecked(true); break;
            case 2: holder.propo3.setChecked(true); break;
            case 3: holder.propo4.setChecked(true); break;
        }

        // Remettre le listener après avoir restauré l'état
        holder.propoRadioGroupe.setOnCheckedChangeListener((group, checkedId) -> {
            int answer = -1;
            if (checkedId == holder.propo1.getId()) answer = 0;
            else if (checkedId == holder.propo2.getId()) answer = 1;
            else if (checkedId == holder.propo3.getId()) answer = 2;
            else if (checkedId == holder.propo4.getId()) answer = 3;
            examen.setReponse(position, answer);
            question.setUserReponse(answer); // Mise à jour locale pour le RecyclerView
        });

        // 5. Bouton d'effacement de réponse
        holder.delRespButton.setOnClickListener(v -> {
            holder.propoRadioGroupe.clearCheck();
            examen.setReponse(position, -1);
            question.setUserReponse(-1);
        });

        // 6. Bouton Cours
        holder.coursQButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(question.getCoursUrl()));
            context.startActivity(browserIntent);
        });

        // 7. Bouton et Logique de Réponse
        holder.reponseQButton.setEnabled(showResponces);

        // Nettoyage des couleurs (Nécessaire lors du recyclage des vues)
        resetColorsAndState(holder);

        if (question.isReponseAsked()) {
            showSolutionUI(holder, question);
        } else {
            holder.reponseQButton.setOnClickListener(v -> {
                question.setReponseAsked(true);
                examen.setReponseAsked(position);
                showSolutionUI(holder, question);
            });
        }
    }

    private void showSolutionUI(QuestionViewHolder holder, Question question) {
        // Afficher le commentaire
        if (question.getCommentaire() != null && !question.getCommentaire().equals("null")) {
            holder.commentCardView.setVisibility(View.VISIBLE);
            holder.commentTextView.setText(question.getCommentaire());
        }

        // Désactiver les clics
        for (int i = 0; i < holder.propoRadioGroupe.getChildCount(); i++) {
            holder.propoRadioGroupe.getChildAt(i).setEnabled(false);
        }

        // Appliquer les couleurs de correction (Rouge = Faux, Tertiaire/Primary = Vrai)
        int colorError = MaterialColors.getColor(holder.itemView, R.attr.colorError);
        int colorCorrect = MaterialColors.getColor(holder.itemView, R.attr.colorPrimary);

        holder.propo1.setTextColor(colorError);
        holder.propo2.setTextColor(colorError);
        holder.propo3.setTextColor(colorError);
        holder.propo4.setTextColor(colorError);

        switch (question.getReponse()) {
            case 0: holder.propo1.setTextColor(colorCorrect); break;
            case 1: holder.propo2.setTextColor(colorCorrect); break;
            case 2: holder.propo3.setTextColor(colorCorrect); break;
            case 3: holder.propo4.setTextColor(colorCorrect); break;
        }
    }

    private void resetColorsAndState(QuestionViewHolder holder) {
        int defaultColor = MaterialColors.getColor(holder.itemView, com.google.android.material.R.attr.colorOnSurface);
        holder.propo1.setTextColor(defaultColor);
        holder.propo2.setTextColor(defaultColor);
        holder.propo3.setTextColor(defaultColor);
        holder.propo4.setTextColor(defaultColor);

        for (int i = 0; i < holder.propoRadioGroupe.getChildCount(); i++) {
            holder.propoRadioGroupe.getChildAt(i).setEnabled(true);
        }

        holder.commentCardView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    private int getThemeName(int themeId) {
        switch (themeId) {
            case Examen.codeCouleurs: return R.string.theme_resistancesCouleurs;
            case Examen.groupementsDeResistances: return R.string.theme_resistancesGroupes;
            case Examen.diodesEtTransistors: return R.string.theme_ampli;
            case Examen.synoptiques: return R.string.theme_synoptiques;
            case Examen.etagesRF: return R.string.theme_etagesRF;
            case Examen.electriciteDeBase: return R.string.theme_electricite;
            case Examen.courantsAlternatifs: return R.string.theme_alternatif;
            case Examen.condensateursetBobines: return R.string.theme_condoBob;
            case Examen.transformateursAmpli: return R.string.theme_transfo;
            case Examen.ligneDeTransmis: return R.string.theme_lignes;

            case Examen.classesEmission: return R.string.theme_emission;
            case Examen.indicatifs: return R.string.theme_indicatifs;
            case Examen.codeQ: return R.string.theme_codeQ;
            case Examen.epellation: return R.string.theme_epellation;
            //case Examen.questionsEntrainement: return R.string.theme_entrainement;
            case Examen.sanctions: return R.string.theme_sanctions;
            case Examen.exposition: return R.string.theme_messages;
            case Examen.longueurOnde: return R.string.theme_antennes;
            case Examen.adaptation: return R.string.theme_adaptation;
            case Examen.cem: return R.string.theme_cem;
            default: return R.string.theme_entrainement;
        }
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView themeQTextView, commentTextView;
        Chip numQChip;
        ImageView questionImageView;
        RadioGroup propoRadioGroupe;
        MaterialRadioButton propo1, propo2, propo3, propo4;
        MaterialCardView commentCardView;
        MaterialButton delRespButton, coursQButton, reponseQButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            themeQTextView = itemView.findViewById(R.id.themeQTextView);
            numQChip = itemView.findViewById(R.id.numQChip);
            questionImageView = itemView.findViewById(R.id.questionImageView);
            propoRadioGroupe = itemView.findViewById(R.id.propoRadioGroupe);
            propo1 = itemView.findViewById(R.id.propo1RadioButton);
            propo2 = itemView.findViewById(R.id.propo2RadioButton);
            propo3 = itemView.findViewById(R.id.propo3RadioButton);
            propo4 = itemView.findViewById(R.id.propo4RadioButton);
            commentCardView = itemView.findViewById(R.id.commentCardView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            delRespButton = itemView.findViewById(R.id.delRespButton);
            coursQButton = itemView.findViewById(R.id.coursQButton);
            reponseQButton = itemView.findViewById(R.id.reponseQButton);
        }
    }
}