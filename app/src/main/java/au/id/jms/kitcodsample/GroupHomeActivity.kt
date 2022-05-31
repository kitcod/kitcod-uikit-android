package au.id.jms.kitcodsample

import com.kitcod.android.activities.KcGroupHomeActivity
import com.kitcod.android.fragments.GroupHomeFragment

class GroupHomeActivity : KcGroupHomeActivity() {
    override fun groupHomeFragment(stringExtra: String): GroupHomeFragment? {
        return GroupHomeFragment.Builder()
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(true)
            .setButtonTitle("Join")
            .setHeaderTitle("Group").build();
    }

}