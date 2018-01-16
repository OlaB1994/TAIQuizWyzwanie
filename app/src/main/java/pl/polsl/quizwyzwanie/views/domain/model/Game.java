package pl.polsl.quizwyzwanie.views.domain.model;

import android.support.annotation.NonNull;

import java.util.Objects;

public class Game implements Comparable<Game>{

    public static final int STATE_FINISHED = 0;
    public static final int STATE_WAITING = 1;
    public static final int STATE_YOUR_TURN = 2;

    private String opponentUsername;
    private int myPoints;
    private int opponentPoints;
    private int state;

    public Game(String opponentUsername, int myPoints, int opponentPoints, int state) {
        this.opponentUsername = opponentUsername;
        this.myPoints = myPoints;
        this.opponentPoints = opponentPoints;
        this.state = state;
    }

    @Override
    public int compareTo(@NonNull Game o) {
        if(state>o.getState()) return 1;
        if(state<o.getState()) return -1;
        return 0;
    }

    public int getState() {
        return state;
    }
}
