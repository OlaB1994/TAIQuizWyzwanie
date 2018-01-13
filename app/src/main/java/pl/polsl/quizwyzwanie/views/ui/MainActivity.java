package pl.polsl.quizwyzwanie.views.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.ui.game.GameFragment;
import pl.polsl.quizwyzwanie.views.ui.menu.MenuFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @BindView(R.id.activity_main_toolbar_ll)
    LinearLayout toolbarLl;

    @OnClick(R.id.activity_main_refresh_ib)
    public void onRefreshClick(){

    }

    @OnClick(R.id.actiity_main_stats_ib)
    public void onStatsClick(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        addFragment();
    }

    private void addFragment() {
        fragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container_fl, new MenuFragment(), MenuFragment.class.getName())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.activity_main_fragment_container_fl);
        if(currentFragment instanceof MenuFragment || currentFragment instanceof GameFragment)
            toolbarLl.setVisibility(View.VISIBLE);
        else toolbarLl.setVisibility(View.GONE);
    }

    public void switchToFragment(@NonNull final Fragment fragment, @NonNull final String tag, @NonNull final String backstack) {
        if (fragmentManager.findFragmentByTag(tag) != null) return;
        try {
            fragmentManager.beginTransaction()
                    .add(R.id.activity_main_fragment_container_fl, fragment, tag)
                    .addToBackStack(backstack)
                    .commit();
        } catch (Exception error) {
            Log.e(fragment.getActivity().getClass().getName(), error.getMessage());
        }
    }
}
