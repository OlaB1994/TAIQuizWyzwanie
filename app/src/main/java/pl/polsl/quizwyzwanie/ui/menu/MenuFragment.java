package pl.polsl.quizwyzwanie.ui.menu;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.domain.model.AppUser;
import pl.polsl.quizwyzwanie.domain.model.Game;
import pl.polsl.quizwyzwanie.domain.model.Player;
import pl.polsl.quizwyzwanie.ui.MainActivity;
import pl.polsl.quizwyzwanie.ui.game.GameFragment;

public class MenuFragment extends Fragment {

    @BindView(R.id.fragment_menu_username_tv)
    TextView usernameTv;
    @BindView(R.id.fragment_menu_games_rv)
    RecyclerView gamesRv;

    private String username = "";
    private String email = "";
    private List<Game> gamesList = new ArrayList<>();
    private final List<AppUser> opponentList = new ArrayList<>();
    private GamesAdapter adapter;
    private Bundle bundle;
    private boolean isButtonActive = false;
    private AppUser currentUser = null;

    @OnClick(R.id.fragment_menu_new_game_btn)
    public void onNewGameClick() {
        if (isButtonActive) {
            startNewGame();
        } else {
            Toast.makeText(getContext(), getString(R.string.waiting_initialize), Toast.LENGTH_LONG).show();
        }
    }

    private void startNewGame() {
        AppUser opponent = opponentList.get(new Random().nextInt(opponentList.size()));
        Boolean myTurn = new Random().nextBoolean();

        Game newGame = new Game(null, null, null, false,
                new Player(false, currentUser.getEmail(), false, currentUser.getDisplayName(),
                        null, myTurn,0),
                new Player(false, opponent.getEmail(), false, opponent.getDisplayName(),
                        null, !myTurn, 0),
                null, "none");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("games");
        DatabaseReference newGameRef = ref.push();
        newGame.setId(newGameRef.getKey());
        newGameRef.setValue(newGame);

        GameFragment gameFragment = new GameFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("user", currentUser);
        arguments.putSerializable("game", newGame);

        gameFragment.setArguments(arguments);
        ((MainActivity) getActivity()).switchToFragmentWithBackStack(gameFragment,
                GameFragment.class.getName(), MenuFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        setupFields();
        setupView();
        return view;
    }

    private void setupFields() {
        bundle = this.getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
            email = bundle.getString("email");
        }
    }

    private void setupView() {
        usernameTv.setText(username);
        new GetUser().execute();
    }


    private class GetUser extends AsyncTask<Void, Void, AppUser> {

        @Override
        protected void onPreExecute() {
            ((MainActivity) getActivity()).showDialog();
        }

        @Override
        protected AppUser doInBackground(Void... voids) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child("users");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        AppUser user = snapshot.getValue(AppUser.class);
                        if (user.getEmail().equals(email)) {
                            currentUser = user;
                        } else {
                            opponentList.add(user);
                        }
                    }
                    if (currentUser == null) currentUser = createUser();
                    new GetData().execute();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return currentUser;
        }


        @Override
        protected void onPostExecute(AppUser user) {
        }
    }

    private AppUser createUser() {
        AppUser newUser = new AppUser(username, email);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("users");
        DatabaseReference newUserRef = userRef.push();
        newUser.setId(newUserRef.getKey());
        newUserRef.setValue(newUser);
        return newUser;
    }

    private class GetData extends AsyncTask<Void, Void, List<Game>> {

        @Override
        protected void onPreExecute() {
            if(getActivity()!=null)
            ((MainActivity) getActivity()).showDialog();
            gamesList = new ArrayList<>();
            gamesRv.setLayoutManager(new LinearLayoutManager(getContext(),
                    OrientationHelper.VERTICAL, false));
            bundle.putSerializable("user", currentUser);
            adapter = new GamesAdapter((MainActivity) getActivity(), gamesList, bundle);
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
                        if(game.getUser1().getEmail().equals(currentUser.getEmail())){
                            game.setCurrentUser(Game.CurrentUser.USER_1);
                            gamesList.add(game);
                        }
                        if (game.getUser2().getEmail().equals(currentUser.getEmail())){
                            game.setCurrentUser(Game.CurrentUser.USER_2);
                            gamesList.add(game);
                        }

                        Log.d("TMP", game.getOpponentUsername(currentUser));
                    }
                    Collections.sort(gamesList);
                    isButtonActive = true;
                    ((MainActivity) getActivity()).dismissDialog();
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
