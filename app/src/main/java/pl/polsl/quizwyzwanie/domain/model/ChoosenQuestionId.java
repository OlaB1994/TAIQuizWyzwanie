package pl.polsl.quizwyzwanie.domain.model;

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

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}
