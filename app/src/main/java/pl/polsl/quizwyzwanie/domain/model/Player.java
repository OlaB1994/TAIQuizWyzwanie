package pl.polsl.quizwyzwanie.domain.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {

    private boolean isSurrender;
    private String email;
    private boolean isFinished;
    private String displayName;
    private List<StateOfLastThreeAnswers> stateOfLastThreeAnswers;
    private boolean myTurn;
    private Integer points;

    public Player() {
    }

    public Player(boolean isSurrender, String email, boolean isFinished, String displayName, List<StateOfLastThreeAnswers> stateOfLastThreeAnswers, boolean myTurn, Integer points) {
        this.isSurrender = isSurrender;
        this.email = email;
        this.isFinished = isFinished;
        this.displayName = displayName;
        this.stateOfLastThreeAnswers = stateOfLastThreeAnswers;
        this.myTurn = myTurn;
        this.points = points;
    }

    @PropertyName("isSurrender")
    public Boolean isSurrender() {
        return isSurrender;
    }

    public String getEmail() {
        return email;
    }

    @PropertyName("isFinished")
    public Boolean isFinished() {
        return isFinished;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<StateOfLastThreeAnswers> getStateOfLastThreeAnswers() {
        return stateOfLastThreeAnswers;
    }

    public void setStateOfLastThreeAnswers(List<StateOfLastThreeAnswers> stateOfLastThreeAnswers) {
        this.stateOfLastThreeAnswers = stateOfLastThreeAnswers;
    }

    public Boolean getMyTurn() {
        return myTurn;
    }


    public Integer getPoints() {
        return points;
    }
}


