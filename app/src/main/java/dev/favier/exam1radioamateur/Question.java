package dev.favier.exam1radioamateur;

import android.util.Log;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

@Entity(tableName = "Questions")
public class Question implements Serializable {
    /**
     * Gère toute les informations relatif à une question
     */
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "imgNum")
    private int numero; // le numéro de la question dans le xml (+ image de la question)

    @ColumnInfo(name = "question")
    private String question; // la question sous forme texte

    @ColumnInfo(name = "propositionsJson")
    private String propositionsJson;  // les posibilités au format json
    //private ArrayList<String> propositions; // les quatres possibilités du QCM

    @ColumnInfo(name = "reponse")
    private int reponse; // le numéro de la réponse bonne

    @ColumnInfo(name = "themeId")
    private int themeID; // l'identifiant du theme

    @ColumnInfo(name = "commentaire")
    private String commentaire; // le commentaire de la question

    @ColumnInfo(name = "coursUrl")
    private String coursUrl; // le lien vers la partie du cours de f6kgl

    private int userReponse; // le numéro de la réponse donné par l'utilisateur
    private boolean reponseAsked; // etat de la demande de la réponse

    public final static int noReponse = 0;
    public final static int mauvaiseReponse = 1;
    public final static int bonneReponse = 2;

    @Ignore
    public Question(int numero, String question, String propositionsJson, int reponse, int themeID, String commentaire, String coursUrl) {
        this.numero = numero;
        this.question = question;
        this.propositionsJson = propositionsJson;
        this.reponse = reponse;
        this.themeID = themeID;
        this.commentaire = commentaire;
        this.coursUrl = coursUrl;
        userReponse = -1;
        reponseAsked = false;
    }

    public Question() {
        userReponse = -1;
        reponseAsked = false;
    }

    /**
     * Retourne comment l'utilisateur à repondu
     *
     * @return 0:pas de réponse, 1:réponse fausse, 2:réponse bonne
     */
    public int goodResponse() {
        if (userReponse != -1) {
            if (userReponse == reponse) {
                return bonneReponse;
            } else {
                return mauvaiseReponse;
            }
        } else {
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

    /*
    public ArrayList<String> getPropositions() {
        return propositions;
    }

    public void setPropositions(ArrayList<String> propositions) {
        this.propositions = propositions;
    }

    public void addProposition(String proposition){
        this.propositions.add(proposition);
    }*/

    public ArrayList<String> getPropositions() {
        Gson gson = new Gson();
        Type arrayListType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(this.propositionsJson, arrayListType);
    }

    public void setPropositions(ArrayList<String> propositions) {
        Gson gson = new Gson();
        this.propositionsJson = gson.toJson(propositions);
    }

    public void addProposition(String proposition) {
        ArrayList<String> adder = getPropositions() == null ? new ArrayList<String>() : getPropositions();
        adder.add(proposition);
        setPropositions(adder);
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


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPropositionsJson() {
        return propositionsJson;
    }

    public void setPropositionsJson(String propositionsJson) {
        this.propositionsJson = propositionsJson;
    }

    /*public void demo() {
        Log.w("debug", numero + "-" + question);
    }*/

    public void setReponseAsked(boolean reponseAsked) {
        this.reponseAsked = reponseAsked;
    }

    public boolean isReponseAsked() {
        return reponseAsked;
    }
}
