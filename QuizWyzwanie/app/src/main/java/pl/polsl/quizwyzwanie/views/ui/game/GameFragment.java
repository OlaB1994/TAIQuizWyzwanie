package pl.polsl.quizwyzwanie.views.ui.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.domain.model.RoundResult;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;

public class GameFragment extends Fragment {

    @BindView(R.id.fragment_game_my_username_tv)
    TextView myUsernameTv;
    @BindView(R.id.fragment_game_opponent_username_tv)
    TextView opponentUsernameTv;
    @BindView(R.id.fragment_game_result_tv)
    TextView resultTv;
    @BindView(R.id.fragment_game_round_results_rv)
    RecyclerView resultsRv;

    @OnClick(R.id.fragment_game_surrender_btn)
    public void onSurrenderClick(){

    }

    @OnClick(R.id.fragment_game_play_btn)
    public void onPlayClick(){
        ((MainActivity)getActivity()).switchToFragment(new CategoryFragment(),
                CategoryFragment.class.getName(), GameFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this,view);
        setupView();
        return view;
    }

    private void setupView() {
        //todo: setup usernames and results
        resultsRv.setLayoutManager(new LinearLayoutManager(getContext(),
                OrientationHelper.VERTICAL, false));
        resultsRv.setAdapter(new RoundsAdapter((MainActivity) getActivity(), mockRounds()));
    }

    private List<RoundResult> mockRounds() {
        List<RoundResult> results = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            results.add(new RoundResult());
        return results;
    }

}