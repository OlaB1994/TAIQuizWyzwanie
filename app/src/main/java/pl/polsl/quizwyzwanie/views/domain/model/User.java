package pl.polsl.quizwyzwanie.views.domain.model;

import java.util.List;

/**
 * Created by Mateusz on 17.01.2018.
 */

public class User {

    private boolean isSurrender;
    private String email;
    private boolean isFinished;
    private String displayName;
    private List<Object> stateOfLastThreeAnswers;
    private boolean myTurn;
    private Integer points;

    public User() {    }

    public User(Boolean isSurrender, String email, Boolean isFinished, String displayName, List<Object> stateOfLastThreeAnswers, Boolean myTurn, Integer points) {
        this.isSurrender = isSurrender;
        this.email = email;
        this.isFinished = isFinished;
        this.displayName = displayName;
        this.stateOfLastThreeAnswers = stateOfLastThreeAnswers;
        this.myTurn = myTurn;
        this.points = points;
    }

    public Boolean getSurrender() {
        return isSurrender;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<Object> getStateOfLastThreeAnswers() {
        return stateOfLastThreeAnswers;
    }

    public Boolean getMyTurn() {
        return myTurn;
    }

    public Integer getPoints() {
        return points;
    }
}


