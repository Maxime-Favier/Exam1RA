package dev.favier.exam1radioamateur;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Examen {
    /**
     * Gere toute la logique relatif à l'examen
     */
    private ArrayList<Question> questions; // la liste de questions posés à l'examen
    private ArrayList<Integer> themesList; // la liste des themes
    private Context context;

    // les settings de l'examen
    private boolean malusEnable; // active le malus de -1 à chaque question fausse
    private int nbrQuestionParTheme; // le nombre de question de l'examen pour chaque theme
    private boolean showReponse; // active l'affichage des réponses
    private boolean showCommentaires; // active l'affichage des commentaires
    private boolean timerEnable; // active le compte à rebous
    private int timer; // le temps en seconde restant

    // les themes de l'examen
    public static final int codeCouleurs = 201;
    public static final int groupementsDeResistances = 202;
    public static final int diodesEtTransistors = 203;
    public static final int synoptiques = 204;
    public static final int etagesRF = 205;
    public static final int electriciteDeBase = 206;
    public static final int courantsAlternatifs = 207;
    public static final int condensateursetBobines = 208;
    public static final int transformateursAmpli = 209;
    public static final int ligneDeTransmis = 210;
    public static final int classesEmission = 301;
    public static final int indicatifs = 302;
    public static final int codeQ = 303;
    public static final int epellation = 304;
    public static final int questionsEntrainement = 305;
    public static final int sanctions = 306;
    public static final int exposition = 307;
    public static final int longueurOnde = 308;
    public static final int adaptation = 309;
    public static final int cem = 310;

    public Examen(Context context) {
        this.context = context;
    }

    /**
     * retourne le nombre de points de l'examen
     *
     * @return nombre de point à l'examen
     */
    public int pointCalculation() {
        int points = 0;
        for (Question question : questions) {
            if (question.goodResponse() == Question.mauvaiseReponse) {
                // cas mauvaise reponse
                if (malusEnable) {
                    // enleve -1 point
                    points -= 1;
                }
            } else if (question.goodResponse() == Question.bonneReponse) {
                // ajoute 3 points
                points += 3;
            }
        }
        return points;
    }

    private void questionGenerator() {
        ArrayList<Question> AllQuestions = new ArrayList<>();

        int codeCouleursNumber, groupementsDeResistancesNumber, diodesEtTransistorsNumber, synoptiquesNumber,
                etagesRFNumber, electriciteDeBaseNumber, courantsAlternatifsNumber, condensateursetBobinesNumber,
                transformateursAmpliNumber, ligneDeTransmisNumber, classesEmissionNumber, indicatifsNumber, codeQNumber,
                epellationNumber, questionsEntrainementNumber, sanctionsNumber, expositionNumber, longueurOndeNumber,
                adaptationNumber, cemNumber;
        codeCouleursNumber = groupementsDeResistancesNumber = diodesEtTransistorsNumber = synoptiquesNumber =
                etagesRFNumber = electriciteDeBaseNumber = courantsAlternatifsNumber = condensateursetBobinesNumber =
                        transformateursAmpliNumber = ligneDeTransmisNumber = classesEmissionNumber = indicatifsNumber = codeQNumber =
                                epellationNumber = questionsEntrainementNumber = sanctionsNumber = expositionNumber = longueurOndeNumber =
                                        adaptationNumber = cemNumber = 0;
        // load all questions from the xml
        try {
            XmlResourceParser xpp = context.getResources().getXml(R.xml.out);
            xpp.next();
            int eventType = xpp.getEventType();
            String elemtext = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String elemName = xpp.getName();
                    if (elemName.equals("question")) {
                        AllQuestions.add(new Question());
                    }
                    if (elemName.equals("num")) {
                        elemtext = "num";
                    }
                    if (elemName.equals("question")) {
                        elemtext = "question";
                    }
                    if (elemName.equals("proposition")) {
                        elemtext = "proposition";
                    }
                    if (elemName.equals("reponse")) {
                        elemtext = "reponse";
                    }
                    if (elemName.equals("themeNum")) {
                        elemtext = "themeNum";
                    }
                    if (elemName.equals("commentaire")) {
                        elemtext = "commentaire";
                    }
                    if (elemName.equals("cours")) {
                        elemtext = "cours";
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    Question currentQuestion = AllQuestions.get(AllQuestions.size() - 1);
                    if (elemtext.equals("num")) {
                        currentQuestion.setNumero(Integer.parseInt(xpp.getText()));
                    }
                    if (elemtext.equals("question")) {
                        currentQuestion.setQuestion(xpp.getText());
                    }
                    if (elemtext.equals("proposition")) {
                        currentQuestion.addProposition(xpp.getText());
                    }
                    if (elemtext.equals("reponse")) {
                        currentQuestion.setReponse(Integer.parseInt(xpp.getText()));
                    }
                    if (elemtext.equals("themeNum")) {
                        switch (Integer.parseInt(xpp.getText())) {
                            case codeCouleurs:
                                codeCouleursNumber++;
                                break;
                            case groupementsDeResistances:
                                groupementsDeResistancesNumber++;
                                break;
                            case diodesEtTransistors:
                                diodesEtTransistorsNumber++;
                                break;
                            case synoptiques:
                                synoptiquesNumber++;
                                break;
                            case etagesRF:
                                etagesRFNumber++;
                                break;
                            case electriciteDeBase:
                                electriciteDeBaseNumber++;
                                break;
                            case courantsAlternatifs:
                                courantsAlternatifsNumber++;
                                break;
                            case condensateursetBobines:
                                condensateursetBobinesNumber++;
                                break;
                            case transformateursAmpli:
                                transformateursAmpliNumber++;
                                break;
                            case ligneDeTransmis:
                                ligneDeTransmisNumber++;
                                break;
                            case classesEmission:
                                classesEmissionNumber++;
                                break;
                            case indicatifs:
                                indicatifsNumber++;
                                break;
                            case codeQ:
                                codeQNumber++;
                                break;
                            case epellation:
                                epellationNumber++;
                                break;
                            case questionsEntrainement:
                                questionsEntrainementNumber++;
                                break;
                            case sanctions:
                                sanctionsNumber++;
                                break;
                            case exposition:
                                expositionNumber++;
                                break;
                            case longueurOnde:
                                longueurOndeNumber++;
                                break;
                            case adaptation:
                                adaptationNumber++;
                                break;
                            case cem:
                                cemNumber++;
                                break;
                        }
                        currentQuestion.setThemeID(Integer.parseInt(xpp.getText()));
                    }
                    if (elemtext.equals("commentaire")) {
                        currentQuestion.setCommentaire(xpp.getText());
                    }
                    if (elemtext.equals("cours")) {
                        currentQuestion.setCoursUrl(xpp.getText());
                    }
                    AllQuestions.set(AllQuestions.size() - 1, currentQuestion);
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            Log.w("Error", "Error parsing xml");
            e.printStackTrace();
        } catch (IOException e) {
            Log.w("Error", "Error loading xml");
            e.printStackTrace();
        }
        for(int theme:themesList){
            ThreadLocalRandom.current().nextInt(0, 10 );
        }

    }


}
