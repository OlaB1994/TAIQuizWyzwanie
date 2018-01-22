package pl.polsl.quizwyzwanie.domain.model;

import java.io.Serializable;

public class Answer implements Serializable {

    private boolean isCorrect;
    private String tresc;

    public Answer() {
    }

    public Answer(boolean isCorrect, String tresc) {
        this.isCorrect = isCorrect;
        this.tresc = tresc;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }


    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getTresc() {
        return tresc;
    }


    public void setTresc(String tresc) {
        this.tresc = tresc;
    }
}
