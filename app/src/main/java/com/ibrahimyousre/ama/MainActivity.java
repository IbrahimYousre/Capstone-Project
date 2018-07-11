package com.ibrahimyousre.ama;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ibrahimyousre.ama.ui.auth.LoginActivity;
import com.ibrahimyousre.ama.ui.main.feed.FeedFragment;
import com.ibrahimyousre.ama.ui.main.notification.NotificationsFragment;
import com.ibrahimyousre.ama.ui.main.profile.ProfileFragment;
import com.ibrahimyousre.ama.ui.main.topics.TopicsFragment;
import com.ibrahimyousre.ama.util.BottomNavigationViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ProfileFragment.OnSignoutClickListender {

    private static final String STATE_SELECTED_ITEM = "selected_item";
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private int selectedItem = R.id.feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setupBottomNavigationView();
        if (savedInstanceState == null)
            bottomNavigationView.setSelectedItemId(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_ITEM, selectedItem);
    }

    Fragment mfeedFragment;
    Fragment mTopicsFragment;
    Fragment mNotificationsFragment;
    Fragment mProfileFragment;

    private void setupBottomNavigationView() {
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectedItem = item.getItemId();
                switch (selectedItem) {
                    case R.id.feed:
                        if (mfeedFragment == null) mfeedFragment = new FeedFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, mfeedFragment)
                                .commit();
                        break;
                    case R.id.topics:
                        if (mTopicsFragment == null) mTopicsFragment = new TopicsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, mTopicsFragment)
                                .commit();
                        break;
                    case R.id.notifications:
                        if (mNotificationsFragment == null)
                            mNotificationsFragment = new NotificationsFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, mNotificationsFragment)
                                .commit();
                        break;
                    case R.id.profile:
                        if (mProfileFragment == null)
                            mProfileFragment = ProfileFragment.getInstance(mAuth.getUid());
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, mProfileFragment)
                                .commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void signout() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
