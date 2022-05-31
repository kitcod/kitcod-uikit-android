package com.kitcod.android.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.GroupListFragment;

public class KcGroupListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_create_group);
        GroupListFragment fragment = grouplistFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    protected GroupListFragment grouplistFragment() {
        GroupListFragment.Builder builder = new GroupListFragment.Builder()
                .setUseHeader(true)
                .setUseHeaderRightButton(true)
                .setUseHeaderLeftButton(false)
                .setHeaderTitle("Discover Groups");

        return builder.build();
    }


}
