package io.homeasy.app.feature_devices.data

import android.content.Context
import android.util.Log
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.builder.ActivatorBuilder
import com.thingclips.smart.sdk.api.IThingActivator
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import com.thingclips.smart.sdk.api.IThingSmartActivatorListener
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.enums.ActivatorModelEnum
import io.homeasy.app.feature_devices.domain.model.DeviceActivationResult
import io.homeasy.app.feature_devices.domain.repository.EZConnectRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class EZConnectRepositoryImpl @Inject constructor(
    private val context : Context
) : EZConnectRepository {
    override suspend fun getToken(homeId: Long): Result<String> {
        return suspendCancellableCoroutine { continuation ->
            ThingHomeSdk.getActivatorInstance()
                .getActivatorToken(homeId, object : IThingActivatorGetToken {
                    override fun onSuccess(token: String?) {
                        Log.i("EZConnectRepositoryImpl", "Token: $token")
                        continuation.resume(Result.success(token!!), null)

                    }

                    override fun onFailure(errorCode: String?, errorMsg: String?) {
                        Log.e("EZConnectRepositoryImpl", "Error getting token: $errorMsg")
                        continuation.resume(
                            Result.failure(Exception("$errorCode : $errorMsg")),
                            null
                        )
                    }
                })
        }
    }

    override fun ezPairing(
        ssid: String,
        password: String,
        timeOut: Int,
        token : String,
        homeId: Long
    ): Flow<DeviceActivationResult> = callbackFlow {
        val builder = ActivatorBuilder()
            .setContext(context)
            .setSsid(ssid)
            .setPassword(password)
            .setToken(token)
            .setActivatorModel(ActivatorModelEnum.THING_EZ)
            .setTimeOut(timeOut.toLong())
            .setListener(object : IThingSmartActivatorListener {
                override fun onError(errorCode: String?, errorMsg: String?) {
                    trySend(DeviceActivationResult.Failure(errorCode.toString(), errorMsg.toString()))
                }

                override fun onActiveSuccess(devResp: DeviceBean?) {
                    trySend(DeviceActivationResult.Success(devResp!!))
                }

                override fun onStep(step: String?, data: Any?) {
                    trySend(DeviceActivationResult.Step(step.toString(), data))
                }
            })

        val activator: IThingActivator = ThingHomeSdk.getActivatorInstance().newMultiActivator(builder)
        activator.start()
        Log.i("EZConnectRepositoryImpl", "Activator started")
        awaitClose {
            activator.stop()
            activator.onDestroy()
        }
    }
}