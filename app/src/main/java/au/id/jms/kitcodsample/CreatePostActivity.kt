package au.id.jms.kitcodsample

import android.content.Intent
import com.kitcod.android.activities.KcCreatePostActivity
import com.kitcod.android.fragments.CreatePostFragment

class CreatePostActivity : KcCreatePostActivity() {
    override fun createPostFragment(): CreatePostFragment {
        return CreatePostFragment.Builder()
            .setUseHeader(true)
            .setUseHeaderRightButton(false)
            .setUseHeaderLeftButton(true)
            .setButtonTitle("Post")
            .setVideo(false)
            .setPostButtonListener {
            startActivity(Intent(this,GroupHomeActivity::class.java))

            }
            .setHeaderTitle("Create Post").build();
    }

}