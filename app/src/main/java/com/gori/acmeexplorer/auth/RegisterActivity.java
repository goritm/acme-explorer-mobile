package com.gori.acmeexplorer.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.gori.acmeexplorer.R;

public class RegisterActivity extends AppCompatActivity {

    public static final String EMAIL_PARAM = "email_param";

    private AutoCompleteTextView register_email_et, register_password_et, register_password_confirmation_et;
    private TextInputLayout register_email, register_password, register_password_confirmation;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String emailParam = getIntent().getStringExtra(EMAIL_PARAM);

        register_email_et = findViewById(R.id.register_email_et);
        register_password_et = findViewById(R.id.register_password_et);
        register_password_confirmation_et = findViewById(R.id.register_password_confirmation_et);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        register_password_confirmation = findViewById(R.id.register_password_confirmation);
        registerButton = findViewById(R.id.register_button_mail);

        register_email_et.setText(emailParam);

        registerButton.setOnClickListener(l -> {
            if(register_email_et.getText().length() == 0) {
                register_email.setErrorEnabled(true);
                register_email.setError(getString(R.string.register_error_email));
            } else if(register_password_et.getText().length() == 0) {
                register_password.setErrorEnabled(true);
                register_password.setError(getString(R.string.register_error_password));
            } else if(register_password_et.getText().length() < 6) {
                register_password.setErrorEnabled(true);
                register_password.setError(getString(R.string.register_error_password_min_chars));
            } else if(register_password_confirmation_et.getText().length() == 0) {
                register_password_confirmation.setErrorEnabled(true);
                register_password_confirmation.setError(getString(R.string.register_error_password));
            }  else if(!register_password_et.getText().toString().equals(register_password_confirmation_et.getText().toString())) {
                register_password_confirmation.setErrorEnabled(true);
                register_password_confirmation.setError(getString(R.string.register_password_dont_match));
            } else {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(
                                register_email_et.getText().toString(),
                                register_password_et.getText().toString())
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                Toast.makeText(this, R.string.register_created, Toast.LENGTH_SHORT).show();
                                this.finish();
                            } else {
                                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}