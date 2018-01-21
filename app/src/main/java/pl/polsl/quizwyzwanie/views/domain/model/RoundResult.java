package pl.polsl.quizwyzwanie.views.domain.model;

public class RoundResult {


    public static final int ROUND_PER_GAME = 6;
    public static final int ANSWER_UNDEFINED = 0;
    public static final int ANSWER_CORRECT = 1;
    public static final int ANSWER_WRONG = -1;
    public static final int ANSWER_DO_NOT_SHOW = 3;
    public static int[] DEFAULT_ANSWER = new int[]{ANSWER_UNDEFINED, ANSWER_UNDEFINED, ANSWER_UNDEFINED};

    private String categoryName;
    private int[] myQuestionsResults = DEFAULT_ANSWER;
    private int[] opponentQuestionsResults = DEFAULT_ANSWER;

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
