package pl.polsl.quizwyzwanie.views.ui.game;

import android.annotation.SuppressLint;
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
import pl.polsl.quizwyzwanie.views.domain.model.Question;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;

public class QuestionFragment extends Fragment {

    private static final int QUESTIONS_LIMIT = 3;
    public static final int MAX_TIME_IN_MILIS = 5000;

    private enum Answer {
        A, B, C, D
    }

    private int questionCounter;

    int counter=5;
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
    public void onAnswerAClick() {
        handleAnswer(Answer.A);
    }

    @OnClick(R.id.fragment_question_answer_b_btn)
    public void onAnswerBClick() {
        handleAnswer(Answer.B);
    }

    @OnClick(R.id.fragment_question_answer_c_btn)
    public void onAnswerCClick() {
        handleAnswer(Answer.C);
    }

    @OnClick(R.id.fragment_question_answer_d_btn)
    public void onAnswerDClick() {
        handleAnswer(Answer.D);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);


        Question question = new Question();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentCategory = bundle.getString("category");
            question = (Question) bundle.getSerializable("question" + questionCounter);
            questionCounter = bundle.getInt("questionCounter", 0);
        }

        timerPb.setMax(MAX_TIME_IN_MILIS);


        if (question != null) {
            questionTv.setText(question.getTresc());
            answerABtn.setText(question.getAnswers().get(0).getTresc());
            answerBBtn.setText(question.getAnswers().get(1).getTresc());
            answerCBtn.setText(question.getAnswers().get(2).getTresc());
            answerDBtn.setText(question.getAnswers().get(3).getTresc());
        }


        setupTimer();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupTimer();
    }

    private void setupTimer() {
        timerPb.setProgress(MAX_TIME_IN_MILIS);
        CountDownTimer countDownTimer=new CountDownTimer(MAX_TIME_IN_MILIS,500) {

            @Override
            public void onTick(long millisUntilFinished) {
                counter--;

                timerPb.setProgress((int)millisUntilFinished);
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
        if (validateAnswer(answer)) {
            //TODO: handle correct answer
            Log.d("handleAnswer", "Answer correct!");
            navigateToNextQuestion();
        } else {
            //TODO: handle invalid answer
            Log.d("handleAnswer", "Answer invalid!");
            navigateToNextQuestion();
        }

        if (questionCounter >= QUESTIONS_LIMIT) {
            Log.d("handleAnswer", "Question limit reached!");
            navigateToMenu();
        }
    }

    private boolean validateAnswer(Answer answer) {
        Log.d("validateAnswer", "Checking if answer is valid!");
        return true;
    }

    private void navigateToNextQuestion() {
        QuestionFragment questionFragment = new QuestionFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("questionCounter", questionCounter);
        ((MainActivity) getActivity()).switchToFragment(questionFragment, QuestionFragment.class.getName(), QuestionFragment.class.getName());
    }

    private void navigateToMenu() {
        Log.d("navigateToMenu", "Moving back to menu!");
        //todo: return to menu -how?
        getActivity().getSupportFragmentManager().popBackStack();
    }

}