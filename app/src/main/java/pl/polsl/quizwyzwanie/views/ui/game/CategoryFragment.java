package pl.polsl.quizwyzwanie.views.ui.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;

public class CategoryFragment extends Fragment {

    @BindView(R.id.fragment_category_first_btn)
    Button firstBtn;
    @BindView(R.id.fragment_category_second_btn)
    Button secondBtn;
    @BindView(R.id.fragment_category_third_btn)
    Button thirdBtn;

    @OnClick(R.id.fragment_category_first_btn)
    public void onFirstCategoryClick() {
        ((MainActivity) getActivity()).switchToFragment(
                createQuestionFragmentForCategory(firstBtn.getText().toString().toLowerCase()),
                QuestionFragment.class.getName(), CategoryFragment.class.getName());
    }

    @OnClick(R.id.fragment_category_second_btn)
    public void onSecondCategoryClick() {
        ((MainActivity) getActivity()).switchToFragment(
                createQuestionFragmentForCategory(secondBtn.getText().toString().toLowerCase()),
                QuestionFragment.class.getName(), CategoryFragment.class.getName());
    }

    @OnClick(R.id.fragment_category_third_btn)
    public void onThirdCategoryClick() {
        ((MainActivity) getActivity()).switchToFragment(
                createQuestionFragmentForCategory(thirdBtn.getText().toString().toLowerCase()),
                QuestionFragment.class.getName(), CategoryFragment.class.getName());
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
        Bundle arguments = new Bundle();
        arguments.putString("category", category);

        QuestionFragment questionFragment = new QuestionFragment();
        questionFragment.setArguments(arguments);
        return questionFragment;
    }

    private void setupView() {
        //todo setup category names
    }

}