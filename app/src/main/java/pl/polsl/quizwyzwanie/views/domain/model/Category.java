package pl.polsl.quizwyzwanie.views.domain.model;

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
    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
