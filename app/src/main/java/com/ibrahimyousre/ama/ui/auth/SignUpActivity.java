package com.ibrahimyousre.ama.ui.auth;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.Repository;
import com.ibrahimyousre.ama.data.model.User;
import com.ibrahimyousre.ama.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ibrahimyousre.ama.util.Constants.EMAIL_PATTERN;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.user_name_txt)
    EditText userNameEditText;

    @BindView(R.id.user_name_txt_layout)
    TextInputLayout userNameLayout;

    @BindView(R.id.email_txt)
    EditText emailEditText;

    @BindView(R.id.email_txt_layout)
    TextInputLayout emailLayout;

    @BindView(R.id.password_txt)
    EditText passwordEditText;

    @BindView(R.id.password_txt_layout)
    TextInputLayout passwordLayout;

    @BindView(R.id.signup_btn)
    Button signUpButton;

    private FirebaseAuth mAuth;

    private boolean workInProgress = false;
    private String name;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        // TODO: Signup show "already have account login"
    }

    @Override
    public void onBackPressed() {
        if (!workInProgress) super.onBackPressed();
    }

    private boolean isValidInput(String name, String email, String password) {
        boolean valid = true;
        if (name.isEmpty()) {
            userNameLayout.setError(getString(R.string.empty_name_error));
            valid = false;
        } else {
            userNameLayout.setErrorEnabled(false);
        }
        if (email.isEmpty() || !email.matches(EMAIL_PATTERN)) {
            emailLayout.setError(getString(R.string.wrong_email_error));
            valid = false;
        } else {
            emailLayout.setErrorEnabled(false);
        }
        if (password.length() < 5) {
            passwordLayout.setError(getString(R.string.short_password_error));
            valid = false;
        } else {
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }

    @OnClick(R.id.signup_btn)
    void signUpClicked() {
        name = userNameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString();
        if (isValidInput(name, email, password)) {
            ActivityUtils.hideKeyboard(this);
            signUpButton.setEnabled(false);
            workInProgress = true;
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, authCompleteListener);
        }
    }

    private Observer<User> userObserver = new Observer<User>() {
        boolean isRenaming = false;

        @Override
        public void onChanged(@Nullable User user) {
            if (user != null && !isRenaming) {
                isRenaming = true;
                user.setName(name);
                Repository.getInstance().updateUser(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        }
    };

    private final OnCompleteListener<AuthResult> authCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Repository.getInstance().getUserById(mAuth.getUid()).observe(
                        SignUpActivity.this, userObserver);
            } else {
                Toast.makeText(SignUpActivity.this,
                        R.string.account_creation_error,
                        Toast.LENGTH_SHORT).show();
                signUpButton.setEnabled(true);
                workInProgress = false;
            }
        }
    };
}
