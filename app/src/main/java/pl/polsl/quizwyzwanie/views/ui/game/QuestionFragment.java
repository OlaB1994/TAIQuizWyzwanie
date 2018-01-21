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
    private CountDownTimer countDownTimer;

    private enum Answer {
        A, B, C, D
    }

    private static final int ANSWER_WRONG = 0;
    private static final int ANSWER_CORRECT = 1;

    private int questionCounter;
    private boolean isAnswerSelected = false;
    private boolean[] questionAnswers = new boolean[4];

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
        initializeTimer();
        questionCounter = 0;
        prepareAndShowNextQuestion();
        return view;
    }

    private void initializeTimer() {
        countDownTimer = new CountDownTimer(MAX_TIME_IN_MILIS, 500) {

            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
                timerPb.setProgress((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                counter--;
                timerPb.setProgress(0);
                goToNextQuestion();
            }
        };
    }

    private void prepareAndShowNextQuestion() {
        Question question = new Question();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentCategory = bundle.getString("category");
            question = (Question) bundle.getSerializable("question" + questionCounter);
        }
        timerPb.setMax(MAX_TIME_IN_MILIS);
        if (question != null) {
            questionTv.setText(question.getTresc());
            answerABtn.setText(question.getAnswers().get(0).getTresc());
            answerBBtn.setText(question.getAnswers().get(1).getTresc());
            answerCBtn.setText(question.getAnswers().get(2).getTresc());
            answerDBtn.setText(question.getAnswers().get(3).getTresc());

            questionAnswers[0] = question.getAnswers().get(0).isIsCorrect();
            questionAnswers[1] = question.getAnswers().get(1).isIsCorrect();
            questionAnswers[2] = question.getAnswers().get(2).isIsCorrect();
            questionAnswers[3] = question.getAnswers().get(3).isIsCorrect();
        }
        setupTimer();
    }

    private void setupTimer() {
        timerPb.setProgress(MAX_TIME_IN_MILIS);
        countDownTimer.start();
    }

    private void goToNextQuestion() {
        if (!isAnswerSelected) {
            //todo send info about wrong answer to database - time is up
            handleInvalidAnswer();
        }
        handleNextQuestion();
    }

    private void handleAnswer(Answer answer) {
        checkAnswer(answer);
        if (countDownTimer == null)
            goToNextQuestion();
        else {
            resetTimer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        resetTimer();
    }

    private void resetTimer() {
        Log.i(this.getClass().getName(), "timer reset");
        if(countDownTimer == null) return;
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

    private void checkAnswer(Answer answer) {
        isAnswerSelected = true;
        if (validateAnswer(answer)) {
            //TODO: handle correct answer
            handleCorrectAnswer();
        } else {
            //TODO: handle invalid answer
            handleInvalidAnswer();
        }
    }

    private void handleCorrectAnswer() {
        Log.d("handleAnswer", "Answer correct!");
        setupAnswerIndicator(ANSWER_CORRECT);
    }

    private void handleInvalidAnswer() {
        Log.d("handleAnswer", "Answer invalid!");
        setupAnswerIndicator(ANSWER_WRONG);
    }

    private void handleNextQuestion() {
        questionCounter++;
        isAnswerSelected = false;
        if (questionCounter >= QUESTIONS_LIMIT) {
            Log.d("handleAnswer", "Question limit reached!");
            navigateToMenu();
        } else {
            prepareAndShowNextQuestion();
        }
    }

    private void setupAnswerIndicator(int answer) {
        if (questionCounter == 0) {
            Log.d("setupAnswerIndicator", "First question answered!");
            setupAnswerColor(firstIndicatorIv, answer);
        } else if (questionCounter == 1) {
            Log.d("setupAnswerIndicator", "Second question answered!");
            setupAnswerColor(secondIndicatorIv, answer);
        } else if (questionCounter == 2) {
            Log.d("setupAnswerIndicator", "Third question answered!");
            setupAnswerColor(thirdIndicatorIv, answer);
        }
    }

    private void setupAnswerColor(ImageView imageView, int answer) {
        if (answer == ANSWER_CORRECT) {
            Log.d("setupAnswerColor", "Answer is correct: green color is set");
            imageView.setColorFilter(getContext().getResources().getColor(R.color.question_correct));
        } else if (answer == ANSWER_WRONG) {
            Log.d("setupAnswerColor", "Answer is wrong: red color is set");
            imageView.setColorFilter(getContext().getResources().getColor(R.color.question_wrong));
        }
    }

    private boolean validateAnswer(Answer answer) {
        Log.d("validateAnswer", "Checking if answer is valid!");
        int whichAnswer = answer.ordinal();
        return questionAnswers[whichAnswer];
    }


    private void navigateToMenu() {
        Log.d("navigateToMenu", "Moving back to menu!");
        getActivity().getSupportFragmentManager().popBackStack();
    }

}