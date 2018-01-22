package pl.polsl.quizwyzwanie.domain.model;

/**
 * Created by Mateusz on 19.01.2018.
 */

public class ChoosenQuestionId {

    private Long questionId;

    @SuppressWarnings("unused")
    public ChoosenQuestionId() {
    }

    @SuppressWarnings("unused")
    public ChoosenQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @SuppressWarnings("unused")
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
