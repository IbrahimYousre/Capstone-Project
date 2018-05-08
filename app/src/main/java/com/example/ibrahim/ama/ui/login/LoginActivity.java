package com.example.ibrahim.ama.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ibrahim.ama.MainActivity;
import com.example.ibrahim.ama.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private static final int RC_GOOGLE_SIGN_IN = 101;

    @BindView(R.id.email_txt)
    EditText emailEditText;

    @BindView(R.id.email_txt_layout)
    TextInputLayout emailLayout;

    @BindView(R.id.password_txt)
    EditText passwordEditText;

    @BindView(R.id.password_txt_layout)
    TextInputLayout passwordLayout;

    @BindView(R.id.login_btn)
    Button loginButton;

    @BindView(R.id.google_btn)
    SignInButton googleButton;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.my_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private boolean isValidInput(String email, String password) {
        boolean valid = true;
        if (email.isEmpty() || !email.matches(EMAIL_PATTERN)) {
            emailLayout.setError("Wrong Email Address!");
            valid = false;
        } else {
            emailLayout.setErrorEnabled(false);
        }
        if (password.isEmpty() || password.length() < 5) {
            passwordLayout.setError("Short Password!");
            valid = false;
        } else {
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick(R.id.login_btn)
    void onLoginClicked() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        if (isValidInput(email, password)) {
            hideKeyboard();
            loginButton.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, authCompleteListener);
        }
    }

    @OnClick(R.id.google_btn)
    void onGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, authCompleteListener);
    }

    private OnCompleteListener<AuthResult> authCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this,
                        R.string.authentication_error,
                        Toast.LENGTH_SHORT).show();
                loginButton.setEnabled(true);
            }
        }
    };
}
