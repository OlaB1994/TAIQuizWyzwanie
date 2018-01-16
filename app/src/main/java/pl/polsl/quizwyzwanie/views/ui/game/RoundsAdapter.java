package pl.polsl.quizwyzwanie.views.ui.game;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.domain.model.RoundResult;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;

import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.ANSWER_CORRECT;
import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.ANSWER_DO_NOT_SHOW;
import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.ANSWER_UNDEFINED;
import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.ANSWER_WRONG;
import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.DEFAULT_ANSWER;

public class RoundsAdapter extends RecyclerView.Adapter<RoundsAdapter.ViewHolder> {

    private List<RoundResult> rounds;
    private MainActivity activity;

    public RoundsAdapter(MainActivity activity, List<RoundResult> rounds) {
        this.activity = activity;
        this.rounds = rounds;
    }

    @Override
    public RoundsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.entry_rounds_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RoundsAdapter.ViewHolder holder, final int position) {
        setupAnswers(holder, rounds.get(position).getMyQuestionsResults(), rounds.get(position).getOpponentQuestionsResults());
    }

    private void setupAnswers(ViewHolder holder, int[] myQuestionsResults, int[] opponentQuestionsResults) {
        if (myQuestionsResults == DEFAULT_ANSWER && opponentQuestionsResults != DEFAULT_ANSWER)
            doNotShowIcons(holder);
        else {
            setupIcon(holder.myFirstQuestionIv, myQuestionsResults[0]);
            setupIcon(holder.mySecondQuestionIv, myQuestionsResults[1]);
            setupIcon(holder.myThirdQuestionIv, myQuestionsResults[2]);
            setupIcon(holder.opponentFirstQuestionIv, opponentQuestionsResults[0]);
            setupIcon(holder.opponentSecondQuestionIv, opponentQuestionsResults[1]);
            setupIcon(holder.opponentThirdQuestionIv, opponentQuestionsResults[2]);
        }
    }

    private void doNotShowIcons(ViewHolder holder) {
        setupIcon(holder.myFirstQuestionIv, ANSWER_UNDEFINED);
        setupIcon(holder.mySecondQuestionIv, ANSWER_UNDEFINED);
        setupIcon(holder.myThirdQuestionIv, ANSWER_UNDEFINED);
        setupIcon(holder.opponentFirstQuestionIv, ANSWER_DO_NOT_SHOW);
        setupIcon(holder.opponentSecondQuestionIv, ANSWER_DO_NOT_SHOW);
        setupIcon(holder.opponentThirdQuestionIv, ANSWER_DO_NOT_SHOW);
    }


    private void setupIcon(ImageView imageView, int answer) {
        if (answer == ANSWER_CORRECT)
            imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.entry_games_correct));
        else if (answer == ANSWER_UNDEFINED)
            imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.entry_games_undefined));
        else if (answer == ANSWER_WRONG)
            imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.entry_games_wrong));
        else if (answer == ANSWER_DO_NOT_SHOW)
            imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.entry_games_do_not_show));
    }

    @Override
    public int getItemCount() {
        return rounds != null ? rounds.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.entry_rounds_my_first_question_iv)
        ImageView myFirstQuestionIv;
        @BindView(R.id.entry_rounds_my_second_question_iv)
        ImageView mySecondQuestionIv;
        @BindView(R.id.entry_rounds_my_third_question_iv)
        ImageView myThirdQuestionIv;
        @BindView(R.id.entry_rounds_opponent_first_question_iv)
        ImageView opponentFirstQuestionIv;
        @BindView(R.id.entry_rounds_opponent_second_question_iv)
        ImageView opponentSecondQuestionIv;
        @BindView(R.id.entry_rounds_opponent_third_question_iv)
        ImageView opponentThirdQuestionIv;

        public ViewHolder(View rowView) {
            super(rowView);
            ButterKnife.bind(this, rowView);
        }
    }
}
