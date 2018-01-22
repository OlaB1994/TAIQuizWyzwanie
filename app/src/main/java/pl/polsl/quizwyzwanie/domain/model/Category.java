package pl.polsl.quizwyzwanie.domain.model;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    private String name;
    private List<Question> questions;

    public Category() {
    }

    public Category(String name, List<Question> questions) {
        this.name = name;
        this.questions = questions;
    }

    public String getName() {
        return name;
    }
    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    @SuppressWarnings("unused")
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
