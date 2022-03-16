package com.kitcod.android.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.CreatePostFragment;

public class KcCreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_create_post);
        CreatePostFragment fragment = createPostFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    public CreatePostFragment createPostFragment() {
        CreatePostFragment.Builder builder = new CreatePostFragment.Builder()
                .setUseHeader(true);


        return builder.build();
    }
}