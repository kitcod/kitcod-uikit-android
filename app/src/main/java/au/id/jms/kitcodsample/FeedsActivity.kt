package au.id.jms.kitcodsample

import android.content.Intent
import com.google.gson.Gson
import com.kitcod.android.activities.KcCreatePostActivity
import com.kitcod.android.activities.KcFeedsViewActivity
import com.kitcod.android.fragments.FeedsViewFragment
import com.kitcod.android.model.Post
import com.kitcod.android.utils.Util

class FeedsActivity : KcFeedsViewActivity() {

    override fun feedsViewFragment(): FeedsViewFragment {
        return FeedsViewFragment.Builder()
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(false)
            .setHeaderTitle("News Feed")
            .setUseGroupsFeed(true)
            .setUseCreatePostFeed(true)
            .setItemClickListener({ view, i, post -> showCustomChannelActivity(post) })
            .setUserClickListener { view, position, data -> showCustomChannelActivity(data) }
            .setCreatePostItemClickListener { view, position, data ->
                showCustomChannelActivity()
            }
            .build();
    }

    private fun showCustomChannelActivity() {
        val intent = Intent(this, CreatePostActivity::class.java)
        startActivity(intent)
    }

    private fun showCustomChannelActivity(post: Post) {
        val intent = Intent(this, SinglePostActivity::class.java)
        intent.putExtra(Util.POST, Gson().toJson(post))
        startActivity(intent)
    }

    private fun showCustomChannelActivity(post: String) {
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra(Util.USERID, post)
        startActivity(intent)
    }

}