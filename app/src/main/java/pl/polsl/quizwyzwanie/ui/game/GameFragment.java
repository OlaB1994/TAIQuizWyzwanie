package pl.polsl.quizwyzwanie.ui.game;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.domain.model.AppUser;
import pl.polsl.quizwyzwanie.domain.model.Category;
import pl.polsl.quizwyzwanie.domain.model.Game;
import pl.polsl.quizwyzwanie.domain.model.RoundResult;
import pl.polsl.quizwyzwanie.domain.model.StateOfLastThreeAnswers;
import pl.polsl.quizwyzwanie.ui.MainActivity;

import static pl.polsl.quizwyzwanie.domain.model.RoundResult.DEFAULT_ANSWER;


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

    private Game game;
    private AppUser user;
    private List<Category> categoryList = new ArrayList<>();
    private int currentPlayerPoints = 0, opponentPoints = 0;

    @OnClick(R.id.fragment_game_surrender_btn)
    public void onSurrenderClick() {
        //TODO: obsługa działania gry po kliknięciu przycisku surrender. Przemyśleć logikę. Na pewno do ustawienia isSurrender na Player oraz na Game isFinished
    }

    @OnClick(R.id.fragment_game_play_btn)
    public void onPlayClick() {
        if (game.getCurrentPlayer(user.getEmail()).getMyTurn()) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("user", user);
            arguments.putSerializable("game", game);
            CategoryFragment categoryFragment = new CategoryFragment();
            categoryFragment.setArguments(arguments);
            if (game.getWhoChoosedCategoryLast() == null) {
                ((MainActivity) getActivity()).switchToFragment(categoryFragment, CategoryFragment.class.getName());
            } else if (game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers().size() ==
                    game.getOpponent(user).getStateOfLastThreeAnswers().size() &&
                    !game.getWhoChoosedCategoryLast().equals(user.getEmail())) {
                ((MainActivity) getActivity()).switchToFragment(categoryFragment, CategoryFragment.class.getName());
            } else {

                Category category = null;
                for(Category c : categoryList){
                    if (c.getName().equals(game.getActualCategoryName())) {
                        category = c;
                        break;
                    }
                }

                arguments.putSerializable("categoryAndQuestions", category);
                QuestionFragment questionFragment = new QuestionFragment();
                questionFragment.setArguments(arguments);
                ((MainActivity) getActivity()).switchToFragment(questionFragment, QuestionFragment.class.getName());
            }
        } else
            Toast.makeText(getContext(), getString(R.string.waiting_for_opponent), Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        setupGame();
        setupView();
        return view;
    }

    private void setupView() {
        myUsernameTv.setText(user.getDisplayName());
        opponentUsernameTv.setText(game.getOpponentUsername(user));
        String result = currentPlayerPoints + " - " + opponentPoints;
        resultTv.setText(result);

        new GetCategories().execute();

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
            user = (AppUser) bundle.getSerializable("user");
            game = (Game) bundle.getSerializable("game");
        }


        for( StateOfLastThreeAnswers state : game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers()){
            if (state.getState() > 0) currentPlayerPoints++;
        }

        if (game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers().size() <
                game.getOpponent(user).getStateOfLastThreeAnswers().size()) {
            for (int state = 0; state < game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers().size(); state++) {
                if (game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers().get(state).getState() > 0) opponentPoints++;
            }
        } else {
            for( StateOfLastThreeAnswers state : game.getOpponent(user).getStateOfLastThreeAnswers()){
                if (state.getState() > 0) opponentPoints++;
            }
        }
    }

    private List<RoundResult> mockRounds() {

        List<RoundResult> results = new ArrayList<>();
        List<StateOfLastThreeAnswers> myAnswers = game.getCurrentPlayer(user.getEmail()).getStateOfLastThreeAnswers();
        List<StateOfLastThreeAnswers> opponentAnswers = game.getOpponent(user).getStateOfLastThreeAnswers();

        List<int[]> myRound = new ArrayList<>();
        List<int[]> opponentRound = new ArrayList<>();

        if (myAnswers != null) {
            handleAnswers(myAnswers, myRound);
        }
        if (opponentAnswers != null) {
            handleAnswers(opponentAnswers, opponentRound);
        }

        for (int i = 0; i < RoundResult.ROUND_PER_GAME; i++) {
            RoundResult roundResult;
            roundResult = handleRoundResult(myRound, opponentRound, i);
            results.add(roundResult);
        }
        return results;
    }

    @NonNull
    private RoundResult handleRoundResult(List<int[]> myRound, List<int[]> opponentRound, int i) {
        RoundResult roundResult;
        if (i < myRound.size() && i < opponentRound.size())
            roundResult = new RoundResult(game.getCategoryRounds().get(i).getCategoryName(), myRound.get(i), opponentRound.get(i));
        else if (i == myRound.size() - 1 && myRound.size() > 0)
            roundResult = new RoundResult(game.getCategoryRounds().get(i).getCategoryName(), myRound.get(i), DEFAULT_ANSWER);
        else if (i == opponentRound.size() - 1 && opponentRound.size() > 0)
            roundResult = new RoundResult(game.getCategoryRounds().get(i).getCategoryName(), DEFAULT_ANSWER, opponentRound.get(i));
        else
            roundResult = new RoundResult("NO_SELECTED", DEFAULT_ANSWER, DEFAULT_ANSWER);
        return roundResult;
    }

    private void handleAnswers(List<StateOfLastThreeAnswers> myAnswers, List<int[]> myRound) {
        for (int i = 0; i < myAnswers.size() / 3; i++) {
            int[] round = new int[]{
                    myAnswers.get(i * 3).getState(),
                    myAnswers.get((i * 3) + 1).getState(),
                    myAnswers.get((i * 3) + 2).getState()
            };
            myRound.add(round);
        }
    }

    private class GetCategories extends AsyncTask<Void, Void, List<Category>> {

        @Override
        protected void onPreExecute() {
            ((MainActivity) getActivity()).showDialog();
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference categoryRef = rootRef.child("categories");
            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Category category = snapshot.getValue(Category.class);
                        categoryList.add(category);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("ERROR", "ERROR");
                }
            });
            return categoryList;
        }

        @Override
        protected void onPostExecute(List<Category> categoryList) {
            ((MainActivity) getActivity()).dismissDialog();
        }
    }

}