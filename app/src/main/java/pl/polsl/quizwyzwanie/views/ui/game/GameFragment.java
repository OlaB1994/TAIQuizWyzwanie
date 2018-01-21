package pl.polsl.quizwyzwanie.views.ui.game;

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
import pl.polsl.quizwyzwanie.views.domain.model.AppUser;
import pl.polsl.quizwyzwanie.views.domain.model.Game;
import pl.polsl.quizwyzwanie.views.domain.model.RoundResult;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;

import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.ANSWER_CORRECT;
import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.ANSWER_WRONG;
import static pl.polsl.quizwyzwanie.views.domain.model.RoundResult.DEFAULT_ANSWER;


public class GameFragment extends Fragment {

    @BindView(R.id.fragment_game_my_username_tv)
    TextView myUsernameTv;
    @BindView(R.id.fragment_game_opponent_username_tv)
    TextView opponentUsernameTv;
    @BindView(R.id.fragment_game_result_tv)
    TextView resultTv;
    @BindView(R.id.fragment_game_round_results_rv)
    RecyclerView resultsRv;

    @BindView(R.id.fragment_game_surrender_btn)
    Button surrenderBtn;
    @BindView(R.id.fragment_game_play_btn)
    Button playBtn;

    Game game;
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
        resultsRv.setAdapter(new RoundsAdapter((MainActivity) getActivity(), mockRounds(), game));
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
        results.add(new RoundResult("CAT1", new int[]{ANSWER_CORRECT, ANSWER_WRONG, ANSWER_CORRECT},
                new int[]{ANSWER_WRONG, ANSWER_CORRECT, ANSWER_WRONG}));
        results.add(new RoundResult("CAT1", DEFAULT_ANSWER,
                new int[]{ANSWER_WRONG, ANSWER_CORRECT, ANSWER_WRONG}));
        results.add(new RoundResult());
        results.add(new RoundResult());
        results.add(new RoundResult());
        results.add(new RoundResult());
        results.add(new RoundResult());
        return results;
    }


}