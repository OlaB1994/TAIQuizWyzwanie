package pl.polsl.quizwyzwanie.ui.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import pl.polsl.quizwyzwanie.domain.model.Category;
import pl.polsl.quizwyzwanie.domain.model.Game;
import pl.polsl.quizwyzwanie.domain.model.Question;
import pl.polsl.quizwyzwanie.ui.MainActivity;

public class CategoryFragment extends Fragment {

    @BindView(R.id.fragment_category_first_btn)
    Button firstBtn;
    @BindView(R.id.fragment_category_second_btn)
    Button secondBtn;
    @BindView(R.id.fragment_category_third_btn)
    Button thirdBtn;

    private final List<Category> categoriesList = new ArrayList<>();
    private List<Category> threeRandomCategoriesList = new ArrayList<>();

    private List<Question> questions = new ArrayList<>();

    @OnClick(R.id.fragment_category_first_btn)
    public void onFirstCategoryClick() {
        ((MainActivity) getActivity()).switchToFragment(
                createQuestionFragmentForCategory(firstBtn.getText().toString()),
                QuestionFragment.class.getName());
    }

    @OnClick(R.id.fragment_category_second_btn)
    public void onSecondCategoryClick() {
        ((MainActivity) getActivity()).switchToFragment(
                createQuestionFragmentForCategory(secondBtn.getText().toString()),
                QuestionFragment.class.getName());
    }

    @OnClick(R.id.fragment_category_third_btn)
    public void onThirdCategoryClick() {
        ((MainActivity) getActivity()).switchToFragment(
                createQuestionFragmentForCategory(thirdBtn.getText().toString()),
                QuestionFragment.class.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        setupView();
        return view;
    }

    private QuestionFragment createQuestionFragmentForCategory(String category) {


        for (Category categoryObject : threeRandomCategoriesList) {
            if (categoryObject.getName().equals(category)) {
                questions = getQuestionsInCategoryForRound(categoryObject);
            }
        }

        QuestionFragment questionFragment = new QuestionFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable("question0", questions.get(0));
        arguments.putSerializable("question1", questions.get(1));
        arguments.putSerializable("question2", questions.get(2));
        arguments.putString("category", category);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Game game = (Game) bundle.getSerializable("game");
            game.setActualCategoryName(category);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("games").child(game.getId()).setValue(game);

            arguments.putSerializable("game", game);
        }

        questionFragment.setArguments(arguments);
        return questionFragment;
    }

    private void setupView() {
        ((MainActivity)getActivity()).showDialog();
        //TODO: extract this to separated thread and refactor
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref = mDatabase.child("categories");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    System.out.println("Categories read OK: " + snapshot.getValue());
                    Category cat = snapshot.getValue(Category.class);
                    categoriesList.add(cat);
                    System.out.println("Categories read OK2: " + snapshot.getValue(Category.class));
                }

                threeRandomCategoriesList = getThreeRandomCategories(categoriesList);

                //TODO: what if there is less than 3 categories?
                firstBtn.setText(threeRandomCategoriesList.get(0).getName());
                secondBtn.setText(threeRandomCategoriesList.get(1).getName());
                thirdBtn.setText(threeRandomCategoriesList.get(2).getName());
                ((MainActivity)getActivity()).dismissDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Categories read failed: " + databaseError.getCode());
            }

        });
    }

    private List<Category> getThreeRandomCategories(List<Category> allCategories) {
        List<Category> randomCategoriesList = new ArrayList<>(allCategories);
        Collections.shuffle(randomCategoriesList);

        //TODO: what if there is less than 3 elements in allCategories? :P
        final int HOW_MANY_CATEGORIES = 3;
        return randomCategoriesList.subList(0, HOW_MANY_CATEGORIES);

    }


    private List<Question> getQuestionsInCategoryForRound(Category category) {
        List<Question> randomQuestionsList = new ArrayList<>(category.getQuestions());
        Collections.shuffle(randomQuestionsList);

        //TODO: what if there is less than 3 questions in category? xD
        final int HOW_MANY_QUESTIONS = 3;
        return randomQuestionsList.subList(0, HOW_MANY_QUESTIONS);
    }

}