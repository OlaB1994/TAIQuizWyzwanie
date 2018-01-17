package pl.polsl.quizwyzwanie.views.ui.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    String username = "";
    private List<Game> gamesList = new ArrayList<>();
    private GamesAdapter adapter;

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
        //Todo: set usernameTv
        Bundle bundle = this.getArguments();
        if(bundle != null){
            username = bundle.getString("username");
            usernameTv.setText(username);
        }
//        List<Game> games = getGamesFromFirebase();
//        Collections.sort(games);
//        gamesRv.setAdapter(new GamesAdapter((MainActivity) getActivity(), games));
        new GetData().execute();
    }


    private class GetData extends AsyncTask<Void, Void, List<Game>> {

        @Override
        protected void onPreExecute() {
            ((MainActivity)getActivity()).showDialog();
            gamesRv.setLayoutManager(new LinearLayoutManager(getContext(),
                    OrientationHelper.VERTICAL, false));
            adapter = new GamesAdapter((MainActivity) getActivity(), gamesList);
            gamesRv.setAdapter(adapter);
        }

        @Override
        protected List<Game> doInBackground(Void... voids) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference gamesRef = rootRef.child("games");
            gamesRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Game game = snapshot.getValue(Game.class);
                        gamesList.add(game);

                        Log.d("TMP", game.getOpponentUsername());
                    }
                    Collections.sort(gamesList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERROR", "ERROR");
                }
            });

            return gamesList;
        }

        @Override
        protected void onPostExecute(List<Game> gamesList) {
            ((MainActivity)getActivity()).dismissDialog();
        }
    }
}
