package au.id.jms.kitcodsample

import com.kitcod.android.activities.KcGroupListActivity
import com.kitcod.android.fragments.GroupListFragment

class GroupListActivity : KcGroupListActivity() {
    override fun grouplistFragment(): GroupListFragment {
        return GroupListFragment.Builder()
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(true)
            .setHeaderTitle("Discover Groups")
            .build()
    }
}