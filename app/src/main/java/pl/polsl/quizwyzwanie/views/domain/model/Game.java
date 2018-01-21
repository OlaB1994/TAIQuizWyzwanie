package pl.polsl.quizwyzwanie.views.domain.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.List;

public class Game implements Comparable<Game>, Serializable {

    public static final int STATE_FINISHED = 0;
    public static final int STATE_WAITING = 1;
    public static final int STATE_YOUR_TURN = 2;

    public static final int ROUND_PER_GAME = 6;
    public static final int ANSWER_UNDEFINED = 0;
    public static final int ANSWER_CORRECT = 1;
    public static final int ANSWER_WRONG = 2;
    public static final int ANSWER_DO_NOT_SHOW = 3;
    public static int[] DEFAULT_ANSWER = new int[]{ANSWER_UNDEFINED, ANSWER_UNDEFINED, ANSWER_UNDEFINED};

    public enum CurrentUser {USER_1, USER_2};
    private CurrentUser currentUser;


    private String actualCategoryName;
    private List<CategoryRounds> categoryRounds;
    private String id;
    private boolean isFinished;
    private Player user1;
    private Player user2;
    private String whoChoosedCategoryLast;
    private String whoWinGame;



    public Game() {    }

    public Game(String actualCategoryName, List<CategoryRounds> categoryRounds, String id, Boolean isFinished, Player user1, Player user2, String whoChoosedCategoryLast, String whoWinGame) {
        this.actualCategoryName = actualCategoryName;
        this.categoryRounds = categoryRounds;
        this.id = id;
        this.isFinished = isFinished;
        this.user1 = user1;
        this.user2 = user2;
        this.whoChoosedCategoryLast = whoChoosedCategoryLast;
        this.whoWinGame = whoWinGame;
    }

    public String getActualCategoryName() {
        return actualCategoryName;
    }

    public List<CategoryRounds> getCategoryRounds() {
        return categoryRounds;
    }

    public String getId() {
        return id;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Player getUser1() {
        return user1;
    }

    public Player getUser2() {
        return user2;
    }

    public String getWhoChoosedCategoryLast() {
        return whoChoosedCategoryLast;
    }

    public String getWhoWinGame() {
        return whoWinGame;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCurrentUser(CurrentUser currentUser){
        this.currentUser = currentUser;
    }

    @Exclude
    public Integer getState(){
        if (this.isFinished) return STATE_FINISHED;
        else if ((currentUser.equals(CurrentUser.USER_1) && user1.getMyTurn())
            || (currentUser.equals(CurrentUser.USER_2) && user2.getMyTurn()))
            return STATE_YOUR_TURN;
        else return STATE_WAITING;
    }

    public Player getCurrentPlayer(String email){
        if (user1.getEmail().equals(email)) return user1;
        else return user2;
    }

    public String getOpponentUsername(AppUser loggedUser){

        if(user1.getEmail().equals(loggedUser.getEmail())) {
            if (user2.getDisplayName() != null ) {
                if (!user2.getDisplayName().equals("")) return user2.getDisplayName();
                else return user2.getEmail();
            }
            else return user2.getEmail();
        } else {
            if (user1.getDisplayName() != null ) {
                if (!user1.getDisplayName().equals("")) return user1.getDisplayName();
                else return user1.getEmail();
            }
            else return user1.getEmail();
        }
    }

    @Override
    public int compareTo(@NonNull Game o) {
        if (this.getState() < o.getState()) return 1;
        if (this.getState() > o.getState()) return -1;
        return 0;
    }

}
