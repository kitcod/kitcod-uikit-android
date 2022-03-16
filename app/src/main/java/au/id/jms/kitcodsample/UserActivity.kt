package au.id.jms.kitcodsample

import com.kitcod.android.activities.KcUserProfileActivity
import com.kitcod.android.fragments.UserProfileFragment

class UserActivity : KcUserProfileActivity() {
    override fun userProfileFragment(): UserProfileFragment {
        return UserProfileFragment.Builder()
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(true)
            .setHeaderTitle("Profile").setButtonTitle("Follow").build();
    }
}