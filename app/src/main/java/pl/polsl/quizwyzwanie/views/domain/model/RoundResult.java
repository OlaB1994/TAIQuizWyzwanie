package pl.polsl.quizwyzwanie.views.domain.model;

public class RoundResult {

    public static final int ROUND_PER_GAME = 6;
    public static final int ANSWER_UNDEFINED = 0;
    public static final int ANSWER_CORRECT = 1;
    public static final int ANSWER_WRONG = 2;


    private String categoryName;
    private int[] myQuestionsResults = {ANSWER_UNDEFINED,ANSWER_UNDEFINED,ANSWER_UNDEFINED};
    private int[] opponentQuestionsResults = {ANSWER_UNDEFINED,ANSWER_UNDEFINED,ANSWER_UNDEFINED};

    public RoundResult(String categoryName, int[] myQuestionsResults, int[] opponentQuestionsResults) {
        this.categoryName = categoryName;
        this.myQuestionsResults = myQuestionsResults;
        this.opponentQuestionsResults = opponentQuestionsResults;
    }

    public RoundResult() {
        categoryName = "TEST";
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int[] getMyQuestionsResults() {
        return myQuestionsResults;
    }

    public int[] getOpponentQuestionsResults() {
        return opponentQuestionsResults;
    }
}
