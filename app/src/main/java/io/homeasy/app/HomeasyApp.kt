package io.homeasy.app

import android.app.Application
import com.thingclips.smart.home.sdk.ThingHomeSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HomeasyApp : Application() {
    @Inject
    lateinit var appProcessObserver: AppProcessObserver

    override fun onCreate() {
        super.onCreate()
        ThingHomeSdk.init(this, AppKeys.TUYA_APP_KEY, AppKeys.TUYA_APP_SECRET)
        appProcessObserver.intialize()
    }
}