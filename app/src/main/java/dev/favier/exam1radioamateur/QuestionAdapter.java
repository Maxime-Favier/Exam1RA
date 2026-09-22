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
import com.bumptech.glide.Glide;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final Context context;
    private final List<Question> questionList;
    private final Examen examen;
    private final boolean showResponces;
    private final boolean isViewerMode;

    public QuestionAdapter(Context context, List<Question> questionList, Examen examen, boolean showResponces, boolean isViewerMode) {
        this.context = context;
        this.questionList = questionList;
        this.examen = examen;
        this.showResponces = showResponces;
        this.isViewerMode = isViewerMode;
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
        holder.numQChip.setText(String.format("# %s", question.getNumero()));
        holder.themeQTextView.setText(getThemeName(question.getThemeID()));

        // 2. Gestion de la question (Texte vs Description d'image)
        String rawQuestionText = question.getQuestion();
        // TODO : en attendant l'évolution de Valentin pour savoir si c'est une question texte
        boolean isImageDescription = true; //rawQuestionText != null && rawQuestionText.trim().startsWith("-");

        if (isImageDescription || rawQuestionText == null || rawQuestionText.isEmpty()) {
            holder.questionTextView.setVisibility(View.GONE);
        } else {
            holder.questionTextView.setVisibility(View.VISIBLE);
            holder.questionTextView.setText(rawQuestionText);
        }

        // 3. Gestion de l'image
        File file = new File(context.getFilesDir(), question.getNumero() + ".png");
        if (file.exists()) {
            holder.questionImageView.setVisibility(View.VISIBLE);
            // Chargement optimisé de l'image avec Glide
            Glide.with(context)
                    .load(file)
                    .into(holder.questionImageView);
            if (isImageDescription) {
                // On retire le tiret initial pour la lecture audio
                holder.questionImageView.setContentDescription(rawQuestionText.replaceFirst("^-?\\s*", ""));
            } else {
                holder.questionImageView.setContentDescription(rawQuestionText);
            }
        } else {
            holder.questionImageView.setVisibility(View.GONE);
            // Libérer l'image précédente recyclée si le fichier n'existe pas
            Glide.with(context).clear(holder.questionImageView);
        }

        // 4. Définir les textes des propositions
        ArrayList<String> propositions = question.getPropositions();
        holder.propo1.setText(propositions.get(0));
        holder.propo2.setText(propositions.get(1));
        holder.propo3.setText(propositions.get(2));
        holder.propo4.setText(propositions.get(3));

        // Retirer le listener pour ne pas fausser les données lors du recyclage
        holder.propoRadioGroupe.setOnCheckedChangeListener(null);
        holder.propoRadioGroupe.clearCheck();

        // 5. Restaurer la réponse de l'utilisateur si existante
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

            // On vérifie que on écrit pas la réponse en dehors des champs
            if (examen != null && examen.getQuestions() != null && position < examen.getQuestions().size()) {
                examen.setReponse(position, answer);
            }
            question.setUserReponse(answer);
        });

        // 6. Bouton d'effacement de réponse
        holder.delRespButton.setOnClickListener(v -> {
            // L'appel à clearCheck() va mettre l'ID à -1 et déclencher le listener ci-dessus.
            // Le listener s'occupera tout seul, et en toute sécurité, de mettre les réponses à -1.
            holder.propoRadioGroupe.clearCheck();
        });

        //holder.delRespButton.setVisibility(View.GONE);

        // On fige les boutons radio pour empêcher la modification de la réponse
        for (int i = 0; i < holder.propoRadioGroupe.getChildCount(); i++) {
            holder.propoRadioGroupe.getChildAt(i).setEnabled(false);
        }

        // 7. Bouton Cours
        holder.coursQButton.setOnClickListener(v -> {
            if (question.getCoursUrl() != null && !question.getCoursUrl().isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(question.getCoursUrl()));
                context.startActivity(browserIntent);
            }
        });

        // 8. Bouton et Logique de Réponse (Correction)
        holder.reponseQButton.setEnabled(showResponces);


        // Nettoyage des couleurs systématique
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
        // Afficher le commentaire s'il existe et est valide
        String commentaire = question.getCommentaire();
        if (commentaire != null && !commentaire.trim().isEmpty() && !commentaire.equalsIgnoreCase("null")) {
            holder.commentCardView.setVisibility(View.VISIBLE);
            holder.commentTextView.setText(commentaire.replaceAll("\n+$", ""));
        }

        // Désactiver les clics sur le RadioGroup
        for (int i = 0; i < holder.propoRadioGroupe.getChildCount(); i++) {
            holder.propoRadioGroupe.getChildAt(i).setEnabled(false);
        }

        // Appliquer les couleurs de correction
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
        int defaultColor = MaterialColors.getColor(holder.itemView, R.attr.colorOnSurface);

        holder.propo1.setTextColor(defaultColor);
        holder.propo2.setTextColor(defaultColor);
        holder.propo3.setTextColor(defaultColor);
        holder.propo4.setTextColor(defaultColor);

        for (int i = 0; i < holder.propoRadioGroupe.getChildCount(); i++) {
            holder.propoRadioGroupe.getChildAt(i).setEnabled(true);
        }
        // 1. On réaffiche le bouton effacer par défaut
        holder.commentCardView.setVisibility(View.GONE);
        if (isViewerMode) {
            holder.delRespButton.setVisibility(View.GONE);
        } else {
            holder.delRespButton.setVisibility(View.VISIBLE);
        }
        holder.commentCardView.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return questionList != null ? questionList.size() : 0;
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
            case Examen.sanctions: return R.string.theme_sanctions;
            case Examen.exposition: return R.string.theme_messages;
            case Examen.longueurOnde: return R.string.theme_antennes;
            case Examen.adaptation: return R.string.theme_adaptation;
            case Examen.cem: return R.string.theme_cem;
            default: return R.string.theme_entrainement;
        }
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView themeQTextView, commentTextView, questionTextView;
        Chip numQChip;
        ImageView questionImageView;
        RadioGroup propoRadioGroupe;
        MaterialRadioButton propo1, propo2, propo3, propo4;
        MaterialCardView commentCardView;
        MaterialButton delRespButton, coursQButton, reponseQButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            themeQTextView = itemView.findViewById(R.id.themeQTextView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
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