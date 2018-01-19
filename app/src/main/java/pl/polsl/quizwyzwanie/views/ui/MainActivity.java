package pl.polsl.quizwyzwanie.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.ButterKnife;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.domain.model.AppUser;
import pl.polsl.quizwyzwanie.views.ui.dialogs.LoadingDialog;
import pl.polsl.quizwyzwanie.views.ui.menu.MenuFragment;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String username = "";
    private GoogleApiClient googleApiClient;
    private LoadingDialog loadingDialog;
    private String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        initializeFirebase();
        handleLogin();
        initializeDialog();
    }

    private void initializeDialog() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setupDialog();
    }

    private void handleLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        boolean userWasTaken = getUserDataFromAuth();
        if (!userWasTaken) {
            signIn();
        }else{
            addFragment();
        }
    }

    private boolean getUserDataFromAuth() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            username = firebaseUser.getDisplayName();
            email = firebaseUser.getEmail();
            return true;
        }else{
            return false;
        }
    }

    private void initializeFirebase() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void addFragment() {
        MenuFragment fragment = new MenuFragment();
        Bundle arguments = new Bundle();
        arguments.putString("username", username);
        arguments.putString("email", email);
        fragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .add(R.id.activity_main_fragment_container_fl, fragment, MenuFragment.class.getName())
                .commit();
    }

    public void switchToFragmentWithBackStack(@NonNull final Fragment fragment, @NonNull final String tag, @NonNull final String backstack) {
        try {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_fragment_container_fl, fragment, tag)
                    .addToBackStack(backstack)
                    .commit();
        } catch (Exception error) {
            Log.e(fragment.getActivity().getClass().getName(), error.getMessage());
        }
    }

    public void switchToFragment(@NonNull final Fragment fragment, @NonNull final String tag) {
        try {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main_fragment_container_fl, fragment, tag)
                    .commit();
        } catch (Exception error) {
            Log.e(fragment.getActivity().getClass().getName(), error.getMessage());
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        signInError();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed." + result.getStatus());
                signInError();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            signInError();
                        }
                        // now menu can be displayed
                        getUserDataFromAuth();
                        addFragment();
                    }
                });
    }

    private void signInError() {
        Toast.makeText(MainActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    public void showDialog(){
        if(!loadingDialog.isShowing() && !isFinishing())
            loadingDialog.show();
    }

    public void dismissDialog(){
        if(loadingDialog.isShowing() && !isFinishing())
            loadingDialog.dismiss();
    }
}
