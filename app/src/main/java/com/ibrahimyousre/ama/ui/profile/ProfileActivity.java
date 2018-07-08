package com.ibrahimyousre.ama.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.ui.main.profile.ProfileFragment;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_USER_ID;

public class ProfileActivity extends AppCompatActivity
        implements ProfileFragment.OnNavigateBackClickListender {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String userId = getIntent().getStringExtra(EXTRA_USER_ID);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,
                        ProfileFragment.getInstance(userId))
                .commit();
    }

    @Override
    public void onNavigateBack() {
        finish();
    }
}
