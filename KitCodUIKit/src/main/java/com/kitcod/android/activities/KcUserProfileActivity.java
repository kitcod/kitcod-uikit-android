package com.kitcod.android.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.UserProfileFragment;

public class KcUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_user_profile);
        UserProfileFragment fragment = userProfileFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    public UserProfileFragment userProfileFragment() {
        UserProfileFragment.Builder builder = new UserProfileFragment.Builder()
                .setUseHeader(true)
                .setHeaderTitle("User Profile");

        return builder.build();
    }
}