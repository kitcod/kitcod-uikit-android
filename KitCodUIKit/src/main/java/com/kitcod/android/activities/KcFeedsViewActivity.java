package com.kitcod.android.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.FeedsViewFragment;

public class KcFeedsViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_create_group);
        FeedsViewFragment fragment = feedsViewFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    protected FeedsViewFragment feedsViewFragment() {
        final Intent intent = getIntent();
        FeedsViewFragment.Builder builder = new FeedsViewFragment.Builder()
                .setUseHeader(true)
                .setUseHeaderRightButton(false)
                .setUseHeaderLeftButton(false)
                .setHeaderTitle("News Feed")
                .setUseGroupsFeed(true)
                .setUseCreatePostFeed(true);

        return builder.build();
    }


}
