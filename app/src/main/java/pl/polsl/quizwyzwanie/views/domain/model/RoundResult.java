package pl.polsl.quizwyzwanie.views.domain.model;

public class RoundResult {

    private String categoryName;
    private boolean[] myQuestionsResults = new boolean[3];
    private boolean[] opponentQuestionsResults = new boolean[3];

    public RoundResult(String categoryName, boolean[] myQuestionsResults, boolean[] opponentQuestionsResults) {
        this.categoryName = categoryName;
        this.myQuestionsResults = myQuestionsResults;
        this.opponentQuestionsResults = opponentQuestionsResults;
    }

    public RoundResult() {
        categoryName = "TEST";
        myQuestionsResults = new boolean[]{false, true, false};
        opponentQuestionsResults = new boolean[]{true, true, false};
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean[] getMyQuestionsResults() {
        return myQuestionsResults;
    }

    public boolean[] getOpponentQuestionsResults() {
        return opponentQuestionsResults;
    }
}
