package io.homeasy.app.feature_home.data

import android.content.Context
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeManager
import com.thingclips.smart.home.sdk.builder.ActivatorBuilder
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import com.thingclips.smart.sdk.api.IThingSmartActivatorListener
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.enums.ActivatorModelEnum
import io.homeasy.app.feature_home.domain.model.ActivatorParams
import io.homeasy.app.feature_home.domain.model.ActivatorResult
import io.homeasy.app.feature_home.domain.repository.DevicePairingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class DevicePairingRepositoryImpl @Inject constructor(
    private val homeManagerInstance : IThingHomeManager,
    private val context : Context
) : DevicePairingRepository {
    override suspend fun getActivatorToken(homeId: Long): Result<String> {
        return suspendCancellableCoroutine { continuation->
            ThingHomeSdk.getActivatorInstance().getActivatorToken(homeId, object : IThingActivatorGetToken {
                override fun onSuccess(token: String?) {
                    continuation.resume(Result.success(token ?: "")){}
                }

                override fun onFailure(errorCode: String?, errorMsg: String?) {
                    continuation.resume(Result.failure(Exception("$errorCode: $errorMsg"))) {}
                }
            })
        }
    }

    override fun startEZPairing(params: ActivatorParams): Flow<ActivatorResult> = callbackFlow {
        val builder = ActivatorBuilder()
            .setContext(context)
            .setSsid(params.ssid)
            .setPassword(params.password)
            .setActivatorModel(ActivatorModelEnum.THING_EZ)
            .setTimeOut(params.timeout)
            .setToken("")  // will set after token obtained
            .setListener(object : IThingSmartActivatorListener {
                override fun onError(errorCode: String, errorMsg: String) {
                    trySend(ActivatorResult.Failure(errorCode, errorMsg))
                    // maybe close the flow?
                }
                override fun onActiveSuccess(devResp: DeviceBean) {
                    trySend(ActivatorResult.Success(devResp))
                }
                override fun onStep(step: String, data: Any?) {
                    // optionally send step updates if you want
                }
            })

        val activator = ThingHomeSdk.getActivatorInstance().newMultiActivator(builder)

        activator.start()

        awaitClose {
            activator.stop()
            activator.onDestroy()
        }
    }
}