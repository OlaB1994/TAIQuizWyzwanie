package pl.polsl.quizwyzwanie.ui.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.domain.model.AppUser;
import pl.polsl.quizwyzwanie.domain.model.Game;
import pl.polsl.quizwyzwanie.domain.model.RoundResult;
import pl.polsl.quizwyzwanie.domain.model.StateOfLastThreeAnswers;
import pl.polsl.quizwyzwanie.ui.MainActivity;

import static pl.polsl.quizwyzwanie.domain.model.RoundResult.DEFAULT_ANSWER;


public class GameFragment extends Fragment {

    @BindView(R.id.fragment_game_my_username_tv)
    private
    TextView myUsernameTv;
    @BindView(R.id.fragment_game_opponent_username_tv)
    private
    TextView opponentUsernameTv;
    @BindView(R.id.fragment_game_result_tv)
    TextView resultTv;
    @BindView(R.id.fragment_game_round_results_rv)
    private
    RecyclerView resultsRv;

    @BindView(R.id.fragment_game_surrender_btn)
    private
    Button surrenderBtn;
    @BindView(R.id.fragment_game_play_btn)
    private
    Button playBtn;

    private Game game;
    private AppUser user;

    @OnClick(R.id.fragment_game_surrender_btn)
    public void onSurrenderClick(){
        //TODO: obsługa działania gry po kliknięciu przycisku surrender. Przemyśleć logikę. Na pewno do ustawienia isSurrender na Player oraz na Game isFinished
    }

    @OnClick(R.id.fragment_game_play_btn)
    public void onPlayClick(){
        if (game.getCurrentPlayer(user.getEmail()).getMyTurn()) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("user", user);
            arguments.putSerializable("game", game);
            CategoryFragment categoryFragment = new CategoryFragment();
            categoryFragment.setArguments(arguments);
            ((MainActivity) getActivity()).switchToFragment(categoryFragment, CategoryFragment.class.getName());
        } else Toast.makeText(getContext(), getString(R.string.waiting_for_opponent), Toast.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,view);
        setupGame();
        setupView();
        return view;
    }


    private void setupView() {
        myUsernameTv.setText(user.getDisplayName());
        opponentUsernameTv.setText(game.getOpponentUsername(user));

        if (game.isFinished()) {
            surrenderBtn.setVisibility(View.GONE);
            playBtn.setVisibility(View.GONE);
        }

        resultsRv.setLayoutManager(new LinearLayoutManager(getContext(),
                OrientationHelper.VERTICAL, false));
        resultsRv.setAdapter(new RoundsAdapter((MainActivity) getActivity(), mockRounds()));
    }

    private void setupGame() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (AppUser)bundle.getSerializable("user");
            game = (Game)bundle.getSerializable("game");
        }
    }

    private List<RoundResult> mockRounds() {

        List<RoundResult> results = new ArrayList<>();
        List<StateOfLastThreeAnswers> myAnswers = game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers();
        List<StateOfLastThreeAnswers> opponentAnswers = game.getOpponent(user).getStateOfLastThreeAnswers();

        List<int[]> myRound = new ArrayList<>();
        List<int[]> opponentRound = new ArrayList<>();

        if (myAnswers != null) {
            for (int i = 0; i < myAnswers.size() / 3; i++) {
                int[] round = new int[]{
                        myAnswers.get(i * 3).getState(),
                        myAnswers.get((i * 3) + 1).getState(),
                        myAnswers.get((i * 3) + 2).getState()
                };
                myRound.add(round);
            }
        }

        if (opponentAnswers != null) {
            for (int i = 0; i < opponentAnswers.size() / 3; i++) {
                int[] round = new int[]{
                        opponentAnswers.get(i * 3).getState(),
                        opponentAnswers.get((i * 3) + 1).getState(),
                        opponentAnswers.get((i * 3) + 2).getState()};
                opponentRound.add(round);
            }
        }

        for(int i = 0; i < RoundResult.ROUND_PER_GAME; i++){
            RoundResult roundResult;
                if (i < myRound.size() && i < opponentRound.size())
                    roundResult = new RoundResult("TMP"/*game.getCategoryRounds().get(i).getCategoryName()*/, myRound.get(i), opponentRound.get(i));
                else if (i == myRound.size()-1 && myRound.size() > 0)
                    roundResult = new RoundResult("TMP"/*game.getCategoryRounds().get(i).getCategoryName()*/, myRound.get(i), DEFAULT_ANSWER);
                else if (i == opponentRound.size()-1 && opponentRound.size() > 0)
                    roundResult = new RoundResult("TMP"/*game.getCategoryRounds().get(i).getCategoryName()*/, DEFAULT_ANSWER, opponentRound.get(i));
                else
                    roundResult = new RoundResult("NO_SELECTED", DEFAULT_ANSWER, DEFAULT_ANSWER);

            results.add(roundResult);
        }

        return results;
    }


}