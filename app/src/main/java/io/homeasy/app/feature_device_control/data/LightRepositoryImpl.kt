package io.homeasy.app.feature_device_control.data

import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IResultCallback
import io.homeasy.app.feature_device_control.domain.repository.LightRepository

class LightRepositoryImpl : LightRepository {
    override suspend fun toggleLights(devId: String, turnOn: Boolean) {
        val dps = mapOf<String, String>(
            "20" to "$turnOn"
        )
        ThingHomeSdk.newDeviceInstance(devId).publishDps(JSONObject(dps).toString(), object : IResultCallback{
            override fun onError(code: String?, error: String?) {
                Log.e("LightRepository", "Error toggling lights: $error")
            }

            override fun onSuccess() {
                Log.i("LightRepository", "Lights toggled successfully")
            }
        })
    }

    override suspend fun setBrightness(devId: String, brightness: Int) {
        TODO("Not yet implemented")
    }
}