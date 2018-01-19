package pl.polsl.quizwyzwanie.views.domain.model;

/**
 * Created by Mateusz on 19.01.2018.
 */

public class ChoosenQuestionId {

    private Long questionId;

    public ChoosenQuestionId() {
    }

    public ChoosenQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionId() {
        return questionId;
    }
}
