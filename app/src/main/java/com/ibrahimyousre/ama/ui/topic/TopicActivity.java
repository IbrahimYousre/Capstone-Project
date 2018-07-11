package com.ibrahimyousre.ama.ui.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ibrahimyousre.ama.R;
import com.ibrahimyousre.ama.data.model.Topic;
import com.ibrahimyousre.ama.ui.ask.AskActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ibrahimyousre.ama.util.Constants.EXTRA_TOPIC;

public class TopicActivity extends AppCompatActivity {

    private static final String STATE_CURRENT_ITEM = "current_item";
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        ButterKnife.bind(this);

        topic = (Topic) getIntent().getSerializableExtra(EXTRA_TOPIC);

        getSupportActionBar().setTitle(topic.getName());
        getSupportActionBar().setElevation(0);

        MyFragmentPagerAdapter pagerAdapter =
                new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ask, menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(STATE_CURRENT_ITEM));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_ITEM, viewPager.getCurrentItem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ask) {
            Intent intent = new Intent(this, AskActivity.class);
            intent.putExtra(EXTRA_TOPIC, topic);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return ReadTopicFragment.getInstance(topic.getUid());
            } else if (position == 1) {
                return AnswerTopicFragment.getInstance(topic.getUid());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.read);
                case 1:
                    return getString(R.string.answer);
            }
            return null;
        }

    }
}
