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

public class QuestionFragment extends Fragment {

    private static final int QUESTIONS_LIMIT = 3;
    public static final int MAX_TIME_IN_MILIS = 5000;

    private enum Answer {
        A, B, C, D
    }

    private static final int ANSWER_WRONG = 0;
    private static final int ANSWER_CORRECT = 1;

    private int questionCounter;

    int counter = 5;
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
        prepareAndShowNextQuestion();
        setupTimer();
        return view;
    }

    private void prepareAndShowNextQuestion() {
        Question question = new Question();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentCategory = bundle.getString("category");
            question = (Question) bundle.getSerializable("question" + questionCounter);
        }

        if (question != null) {
            questionTv.setText(question.getTresc());
            answerABtn.setText(question.getAnswers().get(0).getTresc());
            answerBBtn.setText(question.getAnswers().get(1).getTresc());
            answerCBtn.setText(question.getAnswers().get(2).getTresc());
            answerDBtn.setText(question.getAnswers().get(3).getTresc());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        setupTimer();
    }

    private void setupTimer() {
        timerPb.setProgress(MAX_TIME_IN_MILIS);
        CountDownTimer countDownTimer = new CountDownTimer(MAX_TIME_IN_MILIS, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
                timerPb.setProgress((int) millisUntilFinished);
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
            setupAnswerIndicator(ANSWER_CORRECT);
            prepareAndShowNextQuestion();
        } else {
            //TODO: handle invalid answer
            Log.d("handleAnswer", "Answer invalid!");
            setupAnswerIndicator(ANSWER_WRONG);
            prepareAndShowNextQuestion();
        }

        if (questionCounter >= QUESTIONS_LIMIT) {
            Log.d("handleAnswer", "Question limit reached!");
            navigateToMenu();
        }
    }

    private void setupAnswerIndicator(int answer) {
        if (questionCounter == 1) {
            setupAnswerColor(firstIndicatorIv, answer);
        } else if (questionCounter == 2) {
            setupAnswerColor(secondIndicatorIv, answer);
        } else if (questionCounter == 3) {
            setupAnswerColor(thirdIndicatorIv, answer);
        }
    }

    private void setupAnswerColor(ImageView imageView, int answer) {
        if (answer == ANSWER_CORRECT) {
            imageView.setColorFilter(getContext().getResources().getColor(R.color.question_correct));
        } else if (answer == ANSWER_WRONG) {
            imageView.setColorFilter(getContext().getResources().getColor(R.color.question_wrong));
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