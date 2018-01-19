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
import pl.polsl.quizwyzwanie.views.domain.model.AppUser;
import pl.polsl.quizwyzwanie.views.domain.model.Game;
import pl.polsl.quizwyzwanie.views.domain.model.Player;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;
import pl.polsl.quizwyzwanie.views.ui.game.GameFragment;

public class MenuFragment extends Fragment {

    @BindView(R.id.fragment_menu_username_tv)
    TextView usernameTv;
    @BindView(R.id.fragment_menu_games_rv)
    RecyclerView gamesRv;

    String username = "";
    String email = "";
    private List<Game> gamesList = new ArrayList<>();
    private GamesAdapter adapter;
    private Bundle bundle;
    private AppUser currentUser;

    @OnClick(R.id.fragment_menu_new_game_btn)
    public void onNewGameClick() {
        //todo tutaj trzeba stworzyć nową grę i pusha zrobić do bazy, potem te dane wcisnąć do bundla
        Game game = new Game(null, null, null, false,
                new Player(false, "email", false, username, null, true,0),
                new Player(false, "email", false, "oponentName", null, true, 0),
                username, "none");
        //todo ten game wyżej uzupełnić poprawnymi danymi.

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("games");

        GameFragment gameFragment = new GameFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("user", currentUser);
        arguments.putSerializable("game", game);

        gameFragment.setArguments(arguments);
        ((MainActivity) getActivity()).switchToFragment(gameFragment,
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
            ((MainActivity)getActivity()).showDialog();
        }

        @Override
        protected AppUser doInBackground(Void... voids) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child("users");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        AppUser user = snapshot.getValue(AppUser.class);
                        Log.e("getData", email);
                        if(user.getEmail().equals(email)) {
                            currentUser = user;
                            new GetData().execute();
                            return;
                        }
                    }
                    currentUser = createUser();
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
            ((MainActivity)getActivity()).dismissDialog();
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
            ((MainActivity)getActivity()).showDialog();
            gamesRv.setLayoutManager(new LinearLayoutManager(getContext(),
                    OrientationHelper.VERTICAL, false));
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
                        if(game.getUser1().getEmail().equals(currentUser.getEmail())
                                || game.getUser2().getEmail().equals(currentUser.getEmail())){
                            gamesList.add(game);
                        }

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
