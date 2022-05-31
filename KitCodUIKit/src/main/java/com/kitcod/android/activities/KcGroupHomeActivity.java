package com.kitcod.android.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kitcod.android.R;
import com.kitcod.android.fragments.GroupHomeFragment;
import com.kitcod.android.utils.Util;

public class KcGroupHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.KitCod);
        setContentView(R.layout.activity_user_profile);
        GroupHomeFragment fragment = groupHomeFragment(getIntent().getStringExtra(Util.GROUP));
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
        manager.beginTransaction()
                .replace(R.id.sb_fragment_container, fragment)
                .commit();
    }

    public GroupHomeFragment groupHomeFragment(String group) {
        GroupHomeFragment.Builder builder = new GroupHomeFragment.Builder(group)
                .setUseHeader(true)
                .setButtonTitle("Join")
                .setHeaderTitle("Group Home")
                .setUseHeaderRightButton(false);

        return builder.build();
    }
}
