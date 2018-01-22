package pl.polsl.quizwyzwanie.ui.game;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.domain.model.CategoryRounds;
import pl.polsl.quizwyzwanie.domain.model.ChoosenQuestionId;
import pl.polsl.quizwyzwanie.domain.model.Game;
import pl.polsl.quizwyzwanie.domain.model.Player;
import pl.polsl.quizwyzwanie.domain.model.Question;
import pl.polsl.quizwyzwanie.domain.model.StateOfLastThreeAnswers;

public class QuestionFragment extends Fragment {


    public static final String TAG = "QF";
    private static final int QUESTIONS_LIMIT = 3;
    private static final int MAX_TIME_IN_MILIS = 5000;
    private static final int ANSWER_WRONG = 0;
    private static final int ANSWER_CORRECT = 1;
    private final boolean[] questionAnswers = new boolean[4];
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
    private CountDownTimer countDownTimer;
    private int questionCounter;
    private boolean isAnswerSelected = false;
    private int counter = 5;

    private String currentCategory;
    private Long currentQuestionId;

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

            currentQuestionId = question.getId();
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
        if (countDownTimer == null) return;
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

    private void checkAnswer(Answer answer) {
        isAnswerSelected = true;
        boolean isAnswerCorrect = validateAnswer(answer);
        if (isAnswerCorrect) {
            //TODO: handle correct answer
            handleCorrectAnswer();
        } else {
            //TODO: handle invalid answer
            handleInvalidAnswer();
        }

        //set stateOfLastThreeAnswers list in Firebase
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Game game = (Game) bundle.getSerializable("game");

            Game.CurrentUser currentUser = game.getCurrentDBUser();
            Player currentPlayer;
            if (currentUser.equals(Game.CurrentUser.USER_1)) {
                currentPlayer = game.getUser1();
            } else {
                currentPlayer = game.getUser2();
            }

            List<StateOfLastThreeAnswers> stateOfLastThreeAnswers = new ArrayList<>();
            if (currentPlayer.getStateOfLastThreeAnswers() != null) {
                stateOfLastThreeAnswers = currentPlayer.getStateOfLastThreeAnswers();
            }

            final int CORRECT_ANSWER_IN_STATEOFLASTTHREEANSWERS_FIELD = 1;
            final int WRONG_ANSWER_IN_STATEOFLASTTHREEANSWERS_FIELD = -1;
            if (isAnswerCorrect) {
                stateOfLastThreeAnswers.add(new StateOfLastThreeAnswers(CORRECT_ANSWER_IN_STATEOFLASTTHREEANSWERS_FIELD));
            } else {
                stateOfLastThreeAnswers.add(new StateOfLastThreeAnswers(WRONG_ANSWER_IN_STATEOFLASTTHREEANSWERS_FIELD));
            }

            if (currentUser.equals(Game.CurrentUser.USER_1)) {
                game.getUser1().setStateOfLastThreeAnswers(stateOfLastThreeAnswers);
            } else {
                game.getUser2().setStateOfLastThreeAnswers(stateOfLastThreeAnswers);
            }


            List<CategoryRounds> categoryRounds = game.getCategoryRounds();
            CategoryRounds currentRound = categoryRounds.get(categoryRounds.size() - 1);

            List<ChoosenQuestionId> choosenQuestionIds = currentRound.getChoosenQusetionId();
            choosenQuestionIds.add(new ChoosenQuestionId(currentQuestionId));

            game.getCategoryRounds().get(categoryRounds.size() - 1).setChoosenQusetionId(choosenQuestionIds);


            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("games").child(game.getId()).setValue(game);
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
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }


    private enum Answer {
        A, B, C, D
    }

}