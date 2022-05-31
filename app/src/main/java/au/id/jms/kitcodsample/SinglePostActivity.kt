package au.id.jms.kitcodsample

import com.kitcod.android.activities.KcSinglePostActivity
import com.kitcod.android.fragments.GroupPostFragment

class SinglePostActivity : KcSinglePostActivity() {

    override fun GroupPostFragment(groupPost: String?): GroupPostFragment {
        return GroupPostFragment.Builder(groupPost)
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(false)
            .setHeaderTitle("Feed")
            .build();
    }
}