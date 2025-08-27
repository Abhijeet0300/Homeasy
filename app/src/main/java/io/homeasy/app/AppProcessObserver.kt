package io.homeasy.app

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.thingclips.smart.home.sdk.ThingHomeSdk
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppProcessObserver @Inject constructor() {
    fun intialize() {
        val processLifecycleScope = ProcessLifecycleOwner.get().lifecycleScope
        processLifecycleScope.launch {
            try {
                kotlinx.coroutines.delay(Long.MAX_VALUE)
            } finally {
                ThingHomeSdk.onDestroy()
            }
        }
    }
}