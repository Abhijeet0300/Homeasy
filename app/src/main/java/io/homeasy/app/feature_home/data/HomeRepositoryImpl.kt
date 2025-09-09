package io.homeasy.app.feature_home.data

import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeChangeListener
import com.thingclips.smart.home.sdk.api.IThingHomeManager
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.GroupBean
import io.homeasy.app.feature_home.domain.model.HomeChangeEvent
import io.homeasy.app.feature_home.domain.repository.HomeRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeManagerInstance : IThingHomeManager
) : HomeRepository {
    override suspend fun createHome(
        name: String,
        lon: Double,
        lat: Double,
        geoName: String,
        rooms: List<String>
    ): Result<HomeBean?> {
        return suspendCancellableCoroutine { continuation ->
            homeManagerInstance.createHome(
                name,
                lon,
                lat,
                geoName,
                rooms,
                object : IThingHomeResultCallback{
                    override fun onSuccess(bean: HomeBean?) {
                        continuation.resume(Result.success(bean), null)
                    }

                    override fun onError(errorCode: String?, errorMsg: String?) {
                        continuation.resume(Result.failure(Exception("Error code: $errorCode, error: $errorMsg")), null)
                    }

                }
            )
        }
    }

    override suspend fun queryHomeList(): Result<List<HomeBean?>> {
        return suspendCancellableCoroutine { continuation ->
            homeManagerInstance.queryHomeList(object : IThingGetHomeListCallback{
                override fun onSuccess(homeBeans: List<HomeBean?>?) {
                    continuation.resume(
                        Result.success(homeBeans ?: emptyList()),
                        null
                    )
                }

                override fun onError(errorCode: String?, error: String?) {
                    continuation.resume(
                        Result.failure(Exception("Error code: $errorCode, error: $error")),
                        null
                    )
                }
            })
        }
    }

    override suspend fun observeHomeChanges(): Flow<HomeChangeEvent> = callbackFlow {
        val listener = object : IThingHomeChangeListener {
            override fun onHomeAdded(homeId: Long) {
                trySend(HomeChangeEvent.HomeAdded(homeId))
            }

            override fun onHomeInvite(homeId: Long, homeName: String?) {
                trySend(HomeChangeEvent.HomeInvite(homeId, homeName!!))
            }

            override fun onHomeRemoved(homeId: Long) {
                trySend(HomeChangeEvent.HomeRemoved(homeId))
            }

            override fun onHomeInfoChanged(homeId: Long) {
                trySend(HomeChangeEvent.HomeInfoChanged(homeId))
            }

            override fun onSharedDeviceList(sharedDeviceList: List<DeviceBean?>?) {
                trySend(HomeChangeEvent.SharedDeviceList(sharedDeviceList))
            }

            override fun onSharedGroupList(sharedGroupList: List<GroupBean?>?) {
                trySend(HomeChangeEvent.SharedGroupList(sharedGroupList))
            }

            override fun onServerConnectSuccess() {
                trySend(HomeChangeEvent.ServerConnectSuccess)
            }
        }
        homeManagerInstance.registerThingHomeChangeListener(listener)
        awaitClose { homeManagerInstance.unRegisterThingHomeChangeListener(listener) }
    }
}