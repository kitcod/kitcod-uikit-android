package au.id.jms.kitcodsample

import com.kitcod.android.activities.KcFeedsViewActivity
import com.kitcod.android.fragments.FeedsViewFragment

class FeedsActivity : KcFeedsViewActivity() {

    override fun feedsViewFragment(): FeedsViewFragment {
        return FeedsViewFragment.Builder()
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(false)
            .setHeaderTitle("News Feed")
            .setUseGroupsFeed(true)
            .setUseCreatePostFeed(true).build();
    }
}