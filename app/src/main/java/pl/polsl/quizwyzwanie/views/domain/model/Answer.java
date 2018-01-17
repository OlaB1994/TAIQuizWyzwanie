package pl.polsl.quizwyzwanie.views.domain.model;

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

    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getTresc() {
        return tresc;
    }
    public void setTresc(String tresc) {
        this.tresc = tresc;
    }
}
