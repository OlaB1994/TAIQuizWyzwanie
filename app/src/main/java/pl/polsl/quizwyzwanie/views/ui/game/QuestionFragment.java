package pl.polsl.quizwyzwanie.views.ui.game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;
import pl.polsl.quizwyzwanie.views.ui.menu.MenuFragment;

public class QuestionFragment extends Fragment {

    private static final int QUESTIONS_LIMIT = 3;
    private enum Answer {
        A, B, C, D
    }
    private int questionCounter;

    int counter=0;
    String currentCategory;

    @BindView(R.id.fragment_question_first_indicator_iv)
    ImageView firstIndicatorIv;
    @BindView(R.id.fragment_question_second_indicator_iv)
    ImageView secondIndicatorIv;
    @BindView(R.id.fragment_question_third_indicator_iv)
    ImageView thirdIndicatorIv;
    @BindView(R.id.fragment_question_question_tv)
    TextView questionTv;
    @BindView(R.id.fragment_question_answer_a_btn)
    Button answerABtn;
    @BindView(R.id.fragment_question_answer_b_btn)
    Button answerBBtn;
    @BindView(R.id.fragment_question_answer_c_btn)
    Button answerCBtn;
    @BindView(R.id.fragment_question_answer_d_btn)
    Button answerDBtn;
    @BindView(R.id.fragment_question_timer_pb)
    ProgressBar timerPb;

    @OnClick(R.id.fragment_question_answer_a_btn)
    public void onAnswerAClick(){
        handleAnswer(Answer.A);
    }

    @OnClick(R.id.fragment_question_answer_b_btn)
    public void onAnswerBClick(){
        handleAnswer(Answer.B);
    }

    @OnClick(R.id.fragment_question_answer_c_btn)
    public void onAnswerCClick(){
        handleAnswer(Answer.C);
    }

    @OnClick(R.id.fragment_question_answer_d_btn)
    public void onAnswerDClick(){
        handleAnswer(Answer.D);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            currentCategory = bundle.getString("category");
        }
        setupTimer();
        return view;
    }

    private void setupTimer() {
        CountDownTimer countDownTimer=new CountDownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
                timerPb.setProgress(counter*100/(5000/1000));

            }

            @Override
            public void onFinish() {
                //Do what you want
                counter--;
                timerPb.setProgress(0);
            }
        };
        countDownTimer.start();
    }

    private void handleAnswer(Answer answer) {

        questionCounter++;
        if(validateAnswer(answer)) {
            //TODO: handle correct answer
            Log.d("handleAnswer", "Answer correct!");
        } else {
            //TODO: handle invalid answer
            Log.d("handleAnswer", "Answer invalid!");
        }

        if(questionCounter >= QUESTIONS_LIMIT) {
            Log.d("handleAnswer", "Question limit reached!");
            navigateToMenu();
        }
    }

    private boolean validateAnswer(Answer answer) {
        Log.d("validateAnswer", "Checking if answer is valid!");
        return true;
    }

    private void navigateToMenu() {
        Log.d("navigateToMenu", "Moving back to menu!");
        //todo: return to menu -how?
        getActivity().getSupportFragmentManager().popBackStack();
    }

}