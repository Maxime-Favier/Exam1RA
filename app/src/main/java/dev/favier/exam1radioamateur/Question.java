package dev.favier.exam1radioamateur;

import java.util.ArrayList;

public class Question {
    /**
     * Gère toute les informations relatif à une question
     */
    private int numero; // le numéro de la question dans le xml (+ image de la question)
    private String question; // la question sous forme texte
    private ArrayList<String> propositions; // les quatres possibilités du QCM
    private int reponse; // le numéro de la réponse bonne
    private int themeID; // l'identifiant du theme
    private String commentaire; // le commentaire de la question
    private String coursUrl; // le lien vers la partie du cours de f6kgl
    private int userReponse; // le numéro de la réponse donné par l'utilisateur

    public final static int noReponse = 0;
    public final static int mauvaiseReponse = 1;
    public final static int bonneReponse = 2;

    public Question(int numero, String question, ArrayList<String> propositions, int reponse, int themeID, String commentaire, String coursUrl) {
        this.numero = numero;
        this.question = question;
        this.propositions = propositions;
        this.reponse = reponse;
        this.themeID = themeID;
        this.commentaire = commentaire;
        this.coursUrl = coursUrl;
        userReponse = -1;
    }

    public Question(){
        userReponse = -1;
    }

    /**
     * Retourne comment l'utilisateur à repondu
     * @return 0:pas de réponse, 1:réponse fausse, 2:réponse bonne
     */
    public int goodResponse(){
        if (userReponse != -1){
            if(userReponse == reponse) {
                return bonneReponse;
            }
            else {
                return mauvaiseReponse;
            }
        }else {
            return noReponse;
        }

    }

    // Getters and Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getPropositions() {
        return propositions;
    }

    public void setPropositions(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public void addProposition(String proposition){
        this.propositions.add(proposition);
    }

    public int getReponse() {
        return reponse;
    }

    public void setReponse(int reponse) {
        this.reponse = reponse;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getCoursUrl() {
        return coursUrl;
    }

    public void setCoursUrl(String coursUrl) {
        this.coursUrl = coursUrl;
    }

    public int getUserReponse() {
        return userReponse;
    }

    public void setUserReponse(int userReponse) {
        this.userReponse = userReponse;
    }
}
