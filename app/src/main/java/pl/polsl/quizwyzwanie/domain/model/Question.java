package pl.polsl.quizwyzwanie.domain.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    private long id;
    private String tresc;
    private List<Answer> answers;

    public Question() {
    }

    @SuppressWarnings("unused")
    public Question(long id, String tresc, List<Answer> answers) {
        this.id = id;
        this.tresc = tresc;
        this.answers = answers;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }
    @SuppressWarnings("unused")
    public void setId(long id) {
        this.id = id;
    }

    public String getTresc() {
        return tresc;
    }
    @SuppressWarnings("unused")
    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
    @SuppressWarnings("unused")
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
