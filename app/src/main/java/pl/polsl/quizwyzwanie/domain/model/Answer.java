package pl.polsl.quizwyzwanie.domain.model;

import java.io.Serializable;

public class Answer implements Serializable {

    private boolean isCorrect;
    private String tresc;

    @SuppressWarnings("unused")
    public Answer() {
    }

    @SuppressWarnings("unused")
    public Answer(boolean isCorrect, String tresc) {
        this.isCorrect = isCorrect;
        this.tresc = tresc;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }
    @SuppressWarnings("unused")
    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getTresc() {
        return tresc;
    }
    @SuppressWarnings("unused")
    public void setTresc(String tresc) {
        this.tresc = tresc;
    }
}
