package au.id.jms.kitcodsample

import android.content.Intent
import com.kitcod.android.activities.KcCreateGroupActivity
import com.kitcod.android.fragments.CreateGroupFragment

class MainActivity : KcCreateGroupActivity() {
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startActivity(Intent(this,CreateGroupActivity::class.java))
*/
/*
        if (savedInstanceState == null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = createCommunityHomeFragment()
            fragmentTransaction.replace(R.id.fragmentContainer, fragment)
            fragmentTransaction.commit()
        }
*//*

    }
*/

    override fun createChannelFragment(): CreateGroupFragment {
        return CreateGroupFragment.Builder(R.style.CustomAppBarStyle)
            .setUseHeader(true)
            .setButtonTitle("Create Group")
            .setUseHeaderRightButton(false)
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}