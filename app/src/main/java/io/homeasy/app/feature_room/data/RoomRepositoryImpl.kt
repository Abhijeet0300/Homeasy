package io.homeasy.app.feature_room.data

import android.util.Log
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.bean.RoomBean
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.home.sdk.callback.IThingRoomResultCallback
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import io.homeasy.app.feature_room.domain.repository.RoomRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class RoomRepositoryImpl @Inject constructor() : RoomRepository {
    override suspend fun addRoom(homeId: Long, name : String): Result<RoomBean?> {
        return suspendCancellableCoroutine { continuation->
            ThingHomeSdk.newHomeInstance(homeId).addRoom(name, object : IThingRoomResultCallback{
                override fun onSuccess(bean: RoomBean?) {
                    continuation.resume(Result.success(bean), null)
                }

                override fun onError(errorCode: String?, errorMsg: String?) {
                    continuation.resumeWithException(Exception("$errorCode $errorMsg"))
                }

            })
        }


    }

    override suspend fun addDevice(roomId: Long, deviceId: String) : Result<Unit> {
        return suspendCancellableCoroutine { continuation->
            ThingHomeSdk.newRoomInstance(roomId).addDevice(deviceId, object : IResultCallback {
                override fun onError(code: String?, error: String?) {
                    Log.e("RoomRepositoryImpl", "Failed to add device: $code $error")
                    continuation.resume(Result.failure(Exception("$code : $error")), null)
                }

                override fun onSuccess() {
                    Log.i("RoomRepositoryImpl", "Device added successfully to room $roomId")
                    continuation.resume(Result.success(Unit), null)
                }

            })
        }
    }

    override suspend fun getRoomDetails(
        homeId: Long,
        roomId: Long
    ): Result<RoomBean> {
        return suspendCancellableCoroutine { continuation ->
            val home = ThingHomeSdk.newHomeInstance(homeId)
            home.getHomeDetail(object : IThingHomeResultCallback {
                override fun onSuccess(bean: HomeBean?) {
                    val room = bean?.rooms?.find { it.roomId == roomId }
                    if (room != null) {
                        continuation.resume(Result.success(room), null)
                    } else {
                        continuation.resume(Result.failure(Exception("Room not found")), null)
                    }
                }

                override fun onError(errorCode: String?, errorMsg: String?) {
                    continuation.resume(Result.failure(Exception("$errorCode $errorMsg")), null)
                }
            })

        }
    }


}