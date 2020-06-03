package dev.favier.exam1radioamateur;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Calcule les stats et résultats de l'examen
 */
public class ResultCalculator implements Serializable {
    private ArrayList<Question> questions; // la liste de questions posés à l'examen
    private ArrayList<Integer> themesList; // la liste des themes
    private int nbrQuestionParTheme;
    private boolean malusEnable;

    /**
     * retoune le nombre de questions
     * @return nombre de question
     */
    public int getNumberOfQuestion(){
        return questions.size();
    }

    /**
     * retoune la question de la liste à l'index
     * @param index index de la question
     * @return obj {@link Question}
     */
    public Question getQuestion(int index){
        return questions.get(index);
    }

    /**
     * retourne le nombre de points de l'examen
     *
     * @return nombre de point à l'examen
     */
    public int pointCalculation() {
        int points = 0;
        points += (getNbrOfCorrect() * 3);
        if(malusEnable){
            points -= getNbrOfIncorrect();
        }
        return points;
    }

    /**
     * calcule le nombre de points pour les questions techniques
     * @return nbr de pts partie technique
     */
    public int pointsTechnique(){
        int pts = 0;
        for(Question question: questions){
            if(question.getThemeID() <= Examen.ligneDeTransmis){
                if(question.goodResponse() == Question.bonneReponse){
                    pts += 3;
                }
                else if(question.goodResponse() == Question.mauvaiseReponse){
                    if(malusEnable){
                        pts--;
                    }
                }
            }
        }
        return pts;
    }

    /**
     * calcule le nombre de points pour les questions legislation
     * @return nbr de points partie legislation
     */
    public int pointsLegislation(){
        int pts = 0;
        for(Question question: questions){
            if(question.getThemeID() >= Examen.classesEmission){
                if(question.goodResponse() == Question.bonneReponse){
                    pts += 3;
                }
                else if(question.goodResponse() == Question.mauvaiseReponse){
                    if(malusEnable){
                        pts--;
                    }
                }
            }
        }
        return pts;
    }

    /**
     * retourne le nombre maximal de points de l'exam
     *
     * @return nbr de points max
     */
    public int maxPoints() {
        return nbrQuestionParTheme * themesList.size() * 3;
    }

    /**
     * retourne true si l'examen est réussi
     *
     * @return examen réussi state
     */
    public boolean examGood() {
        //Log.w("debug", String.valueOf(pointCalculation()) + "nbr de poinst");
        //Log.w("debug", String.valueOf(maxPoints()) + "max points");
        double d = ((double) pointCalculation()) / maxPoints();

        //Log.w("debug", String.valueOf(d));
        return d >= 0.5;
    }

    /**
     * retourne le nombre de questions dans l'exam
     *
     * @return nombre de questions de l'exam
     */
    public int getNbrOfQuestions() {
        return nbrQuestionParTheme * themesList.size();
    }

    /**
     * retourne le nombre de bonne réponse
     *
     * @return nombre de bonnes réponses
     */
    public int getNbrOfCorrect() {
        int i = 0;
        for (Question question : questions) {
            if (question.goodResponse() == Question.bonneReponse) {
                i++;
            }
        }
        return i;
    }

    /**
     * retourne le nombre de questions incorrectes
     *
     * @return nbr de questions fausses
     */
    public int getNbrOfIncorrect() {
        int j = 0;
        for (Question question : questions) {
            if (question.goodResponse() == Question.mauvaiseReponse) {
                j++;
            }
        }
        return j;
    }

    /**
     * retourne le nombre de questions sans réponse
     *
     * @return nbr de question sans réponses
     */
    public int getNbrSsRep() {
        int k = 0;
        for (Question question:questions) {
            if(question.goodResponse() == Question.noReponse){
                k++;
            }
        }
        return k;
    }

    /**
     * Contructeur de {@link ResultCalculator}
     * @param questions la liste de question posée
     * @param themesList la liste des themes
     * @param nbrQuestionParTheme le nombre de questions par themes
     */
    public ResultCalculator(ArrayList<Question> questions, ArrayList<Integer> themesList, int nbrQuestionParTheme, boolean malusEnable) {
        this.questions = questions;
        this.themesList = themesList;
        this.nbrQuestionParTheme = nbrQuestionParTheme;
        this.malusEnable = malusEnable;
    }

}
