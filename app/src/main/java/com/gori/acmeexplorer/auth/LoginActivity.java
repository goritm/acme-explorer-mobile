package com.gori.acmeexplorer.auth;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gori.acmeexplorer.MainMenuActivity;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.utils.BetterActivityResult;

public class LoginActivity extends AppCompatActivity {
    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);

    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth mAuth;
    private Button signInButtonGoogle, signInButtonMail, registerButton;
    private TextInputLayout loginEmailParent, loginPasswordParent;
    private AutoCompleteTextView loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.login_email_et);
        loginPassword = findViewById(R.id.login_password_et);
        loginEmailParent = findViewById(R.id.login_email);
        loginPasswordParent = findViewById(R.id.login_password);
        signInButtonGoogle = findViewById(R.id.login_button_google);
        signInButtonMail = findViewById(R.id.login_button_mail);
        registerButton = findViewById(R.id.login_button_register);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signInButtonGoogle.setOnClickListener(l -> attemptLoginGoogle());
        signInButtonMail.setOnClickListener(l -> attemptLoginEmail());
        registerButton.setOnClickListener(l -> redirectToRegisterActivity());

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

    private void attemptLoginGoogle() {
        hideLoginButton(true);
        Intent signInIntent = googleSignInClient.getSignInIntent();

        activityLauncher.launch(signInIntent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Task<GoogleSignInAccount> taskSignIn = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                try {
                    GoogleSignInAccount account = taskSignIn.getResult(ApiException.class);

                    if (account != null) {
                        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                        if (mAuth == null) {
                            mAuth = FirebaseAuth.getInstance();
                        }
                        mAuth.signInWithCredential(credential)
                                .addOnCompleteListener(this, task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = task.getResult().getUser();
                                        assert user != null;
                                        checkUserDatabaseLogin(user);
                                    } else {
                                        showErrorDialog();
                                    }
                                });
                    } else {
                        showErrorDialog();
                    }
                } catch (ApiException e) {
                    showErrorDialog();
                }
            }
        });
    }

    private void attemptLoginEmail() {
        loginEmailParent.setError(null);
        loginPasswordParent.setError(null);

        if (loginEmail.getText().length() == 0) {
            loginEmailParent.setErrorEnabled(true);
            loginEmailParent.setError(getString(R.string.login_email_error_1));
        } else if (loginPassword.getText().length() == 0) {
            loginPasswordParent.setErrorEnabled(true);
            loginPasswordParent.setError(getString(R.string.login_email_error_2));
        } else {
            signInEmail();
        }
    }

    private void signInEmail() {
        hideLoginButton(true);

        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user = task.getResult().getUser();
                        assert user != null;
                        if(!user.isEmailVerified()) showErrorEmailVerified(user);
                        checkUserDatabaseLogin(user);
                    } else {
                        showErrorDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(LoginActivity.this, "This User Not Found , Create A New Account", Toast.LENGTH_SHORT).show();
                    }
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(LoginActivity.this, "The Password Is Invalid, Please Try Valid Password", Toast.LENGTH_SHORT).show();
                    }
                    if (e instanceof FirebaseNetworkException) {
                        Toast.makeText(LoginActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void checkUserDatabaseLogin(FirebaseUser user) {
        Toast.makeText(this, String.format(getString(R.string.login_completed), user.getEmail()), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainMenuActivity.class));
    }


    private void redirectToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.EMAIL_PARAM, loginEmail.getText().toString());
        startActivity(intent);
    }

    private void showErrorEmailVerified(FirebaseUser user) {
        hideLoginButton(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.login_verified_mail_error).setPositiveButton(R.string.login_verified_mail_ok, ((dialog, which) -> {
            user.sendEmailVerification().addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    Snackbar.make(loginEmail, R.string.login_verified_mail_error_sent, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(loginEmail, R.string.login_verified_mail_error_no_sent, Snackbar.LENGTH_SHORT).show();
                }
            });
        })).setNegativeButton(R.string.login_verified_mail_error_cancel, (dialog, which) -> {
        }).show();
    }

    private void showErrorDialog() {
        hideLoginButton(false);
        Snackbar.make(signInButtonMail, getString(R.string.login_mail_access_error), Snackbar.LENGTH_SHORT).show();
    }


    private void hideLoginButton(boolean hide) {
        TransitionSet transitionSet = new TransitionSet();
        Transition layoutFade = new AutoTransition();
        layoutFade.setDuration(1000);
        transitionSet.addTransition(layoutFade);

        if (hide) {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            signInButtonMail.setVisibility(View.GONE);
            signInButtonGoogle.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            loginEmailParent.setEnabled(false);
            loginPasswordParent.setEnabled(false);
        } else {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            signInButtonMail.setVisibility(View.VISIBLE);
            signInButtonGoogle.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            loginEmailParent.setEnabled(true);
            loginPasswordParent.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
    }
}