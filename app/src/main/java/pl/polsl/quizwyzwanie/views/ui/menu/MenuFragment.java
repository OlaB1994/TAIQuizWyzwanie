package pl.polsl.quizwyzwanie.views.ui.menu;

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
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.domain.model.Game;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;
import pl.polsl.quizwyzwanie.views.ui.game.GameFragment;

public class MenuFragment extends Fragment {

    @BindView(R.id.fragment_menu_username_tv)
    TextView usernameTv;
    @BindView(R.id.fragment_menu_games_rv)
    RecyclerView gamesRv;

    @OnClick(R.id.fragment_menu_new_game_btn)
    public void onNewGameClick() {
        ((MainActivity) getActivity()).switchToFragment(new GameFragment(),
                GameFragment.class.getName(), MenuFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        setupView();
        return view;
    }

    private void setupView() {
        List<Game> games = mockGames();
        Collections.sort(games);
        gamesRv.setLayoutManager(new LinearLayoutManager(getContext(),
                OrientationHelper.VERTICAL, false));
        gamesRv.setAdapter(new GamesAdapter((MainActivity) getActivity(), games));
    }

    private List<Game> mockGames() {
        List<Game> games = new ArrayList<>();
        games.add(new Game("mockName", 0, 2, Game.STATE_WAITING));
        games.add(new Game("mockName", 0, 2, Game.STATE_FINISHED));
        games.add(new Game("mockName", 0, 2, Game.STATE_FINISHED));
        games.add(new Game("mockName", 0, 2, Game.STATE_YOUR_TURN));
        games.add(new Game("mockName", 0, 2, Game.STATE_YOUR_TURN));
        return games;
    }

}
