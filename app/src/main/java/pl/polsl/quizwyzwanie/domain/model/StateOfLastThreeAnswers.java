package pl.polsl.quizwyzwanie.domain.model;

/**
 * Created by Mateusz on 18.01.2018.
 */

public class StateOfLastThreeAnswers {

    private int state;


    public StateOfLastThreeAnswers() {
    }

    public StateOfLastThreeAnswers(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }


    public void setState(int state) {
        this.state = state;
    }
}
