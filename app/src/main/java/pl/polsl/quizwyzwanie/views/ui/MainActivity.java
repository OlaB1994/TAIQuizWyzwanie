package pl.polsl.quizwyzwanie.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.ui.game.GameFragment;
import pl.polsl.quizwyzwanie.views.ui.menu.MenuFragment;
import pl.polsl.quizwyzwanie.views.ui.signin.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String username = "";
    private String photoUrl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }else{
            username = firebaseUser.getDisplayName();
            if(firebaseUser.getPhotoUrl() != null){
                photoUrl = firebaseUser.getPhotoUrl().toString();
            }
        }
        fragmentManager = getSupportFragmentManager();
        addFragment();
    }

    private void addFragment() {
        MenuFragment fragment = new MenuFragment();
        Bundle arguments = new Bundle();
        arguments.putString("username", username);
        fragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container_fl, fragment, MenuFragment.class.getName())
                .commit();
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
