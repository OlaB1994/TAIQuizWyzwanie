package pl.polsl.quizwyzwanie.views.ui.game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
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

public class QuestionFragment extends Fragment {

    int counter=0;

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

    }

    @OnClick(R.id.fragment_question_answer_b_btn)
    public void onAnswerBClick(){

    }

    @OnClick(R.id.fragment_question_answer_c_btn)
    public void onAnswerCClick(){

    }

    @OnClick(R.id.fragment_question_answer_d_btn)
    public void onAnswerDClick(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
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

}