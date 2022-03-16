package com.kitcod.android.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.CreateGroupFragment;

public class KcCreateGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_create_group);
        CreateGroupFragment fragment = createChannelFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    protected CreateGroupFragment createChannelFragment() {
        final Intent intent = getIntent();
        CreateGroupFragment.Builder builder = new CreateGroupFragment.Builder()
                .setUseHeader(true)
                .setUseHeaderRightButton(false)
                .setHeaderTitle("Create Group");

        return builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}