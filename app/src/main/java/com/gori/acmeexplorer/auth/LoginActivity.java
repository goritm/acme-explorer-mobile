package com.gori.acmeexplorer.auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gori.acmeexplorer.R;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signInButtonGoogle, signInButtonMail, registerButton;
    private ProgressBar progressBar;
    private TextInputLayout loginEmailParent, loginPasswordParent;
    private AutoCompleteTextView loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.login_progress);
        loginEmail = findViewById(R.id.login_email_et);
        loginPassword = findViewById(R.id.login_password_et);
        loginEmailParent = findViewById(R.id.login_email);
        loginPasswordParent = findViewById(R.id.login_password);
        signInButtonGoogle = findViewById(R.id.login_button_google);
        signInButtonMail = findViewById(R.id.login_button_mail);
        registerButton = findViewById(R.id.login_button_register);

        signInButtonMail.setOnClickListener(l -> attemptLoginEmail());
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
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        if (mAuth != null) {
            mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString()).addOnCompleteListener(this, task -> {
                if (!task.isSuccessful() || task.getResult().getUser() == null) {
                    showErrorDialogMail();
                } else if (!task.getResult().getUser().isEmailVerified()) {
                    showErrorEmailVerified(task.getResult().getUser());
                } else {
                    checkUserDatabaseLogin(task.getResult().getUser());
                }
            });
        } else {
            showGooglePlayServicesError();
        }
    }

    private void showGooglePlayServicesError() {
        Snackbar.make(registerButton, R.string.login_google_play_services_error, Snackbar.LENGTH_LONG).setAction(R.string.login_download_gps, view -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gps_download_url))));
            } catch (Exception ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_download_url))));
            }
        });
    }

    private void checkUserDatabaseLogin(FirebaseUser user) {
        // TODO: complete this function
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

    private void showErrorDialogMail() {
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
            progressBar.setVisibility(View.VISIBLE);
            signInButtonMail.setVisibility(View.GONE);
            signInButtonGoogle.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
            loginEmailParent.setEnabled(false);
            loginPasswordParent.setEnabled(false);
        } else {
            TransitionManager.beginDelayedTransition(findViewById(R.id.login_main_layout), transitionSet);
            progressBar.setVisibility(View.GONE);
            signInButtonMail.setVisibility(View.VISIBLE);
            signInButtonGoogle.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
            loginEmailParent.setEnabled(true);
            loginPasswordParent.setEnabled(true);
        }
    }
}