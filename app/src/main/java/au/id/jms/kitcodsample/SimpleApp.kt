package au.id.jms.kitcodsample

import android.app.Application
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.AmityRegionalEndpoint

class SimpleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AmityCoreClient.setup(
            apiKey = "b3bee90c39d9a5644831d84e5a0d1688d100ddebef3c6e78",
            httpEndpoint = AmityRegionalEndpoint.SG,  // optional param, defaulted as SG region
            socketEndpoint = AmityRegionalEndpoint.SG // optional param, defaulted as SG region
        )

    }
}