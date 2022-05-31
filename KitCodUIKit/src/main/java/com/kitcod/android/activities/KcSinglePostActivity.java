package com.kitcod.android.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.GroupPostFragment;
import com.kitcod.android.utils.Util;

public class KcSinglePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_create_post);
        GroupPostFragment fragment = GroupPostFragment(getIntent().getStringExtra(Util.POST));
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    public GroupPostFragment GroupPostFragment(String groupPost) {
        GroupPostFragment.Builder builder = new GroupPostFragment.Builder(groupPost)
                .setUseHeader(true);


        return builder.build();
    }

}
