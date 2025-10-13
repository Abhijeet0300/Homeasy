package io.homeasy.app.feature_device_control.camera.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.thingclips.smart.android.camera.sdk.ThingIPCSdk
import com.thingclips.smart.android.camera.sdk.api.IThingIPCCore
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack
import com.thingclips.smart.camera.middleware.p2p.IThingSmartCameraP2P
import dagger.hilt.android.qualifiers.ApplicationContext
import io.homeasy.app.feature_device_control.camera.domain.repository.CameraP2PRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class CameraP2PRepositoryImpl @Inject constructor(
    @ApplicationContext private val context : Context
) : CameraP2PRepository {

    private var cameraInstance : IThingIPCCore? = null
    private var cameraP2P: IThingSmartCameraP2P<Any?>? = null
    private var p2pListener: AbsP2pCameraListener? = null

    override fun createP2P(devId: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        if (devId.isBlank()) {
            return false
        }
        cameraInstance = ThingIPCSdk.getCameraInstance()
        cameraP2P = cameraInstance?.createCameraP2P(devId) as IThingSmartCameraP2P<Any?>?
        return cameraP2P != null
    }

    override fun generateCameraView(renderedView: Any) {
        cameraP2P?.generateCameraView(renderedView)
    }

    override fun registerP2PListener(listener: AbsP2pCameraListener) {
        p2pListener = listener
        cameraP2P?.registerP2PCameraListener(p2pListener)
    }

    override suspend fun connect(
        devId: String,
        mode: Int
    ): Result<Int> {
        return suspendCancellableCoroutine { cont ->
            val p2p = cameraP2P
            if (p2p == null) {
                cont.resume(Result.failure(Exception("P2P not initialized")), null)
                return@suspendCancellableCoroutine
            }
            p2p.connect(devId, mode, object : OperationDelegateCallBack {
                override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                    cont.resume(Result.success(sessionId), null)
                }
                override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                    cont.resume(Result.failure(Exception("connect failed code=$errCode")), null)
                }
            })
        }
    }

    override suspend fun startPreview(clarity: Int): Result<Unit> {
        return suspendCancellableCoroutine { cont ->
            val p2p = cameraP2P
            if (p2p == null) {
                cont.resume(Result.failure(Exception("P2P not initialized")), null)
                return@suspendCancellableCoroutine
            }
            p2p.startPreview(clarity, object : OperationDelegateCallBack {
                override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                    cont.resume(Result.success(Unit), null)
                }

                override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                    cont.resume(
                        Result.failure(Exception("startPreview failed code=$errCode")),
                        null
                    )
                }
            })
        }
    }

    override suspend fun stopPreview() : Result<Unit>{
        return suspendCancellableCoroutine { cont ->
            val p2p = cameraP2P
            if (p2p == null) {
                cont.resume(Result.failure(Exception("P2P not initialized")), null)
                return@suspendCancellableCoroutine
            }
            try {
                p2p.stopPreview(@SuppressLint("ImplicitSamInstance")
                object : OperationDelegateCallBack {
                    override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                        cont.resume(Result.success(Unit), null)
                    }

                    override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                        cont.resume(Result.failure(Exception("Stop preview failed: code=$errCode")), null)
                    }
                })
            } catch (e: Exception) {
                cont.resume(Result.failure(Exception("Stop preview error: ${e.message}")), null)
            }
        }
    }

    override suspend fun disconnect(devId: String): Result<Unit> {
        return suspendCancellableCoroutine { cont ->
            val p2p = cameraP2P
            if (p2p == null) {
                cont.resume(Result.failure(Exception("P2P not initialized")), null)
                return@suspendCancellableCoroutine
            }
            p2p.disconnect(true, object : OperationDelegateCallBack {
                override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                    cont.resume(Result.success(Unit), null)
                }
                override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                    cont.resume(Result.failure(Exception("disconnect failed code=$errCode")), null)
                }
            })
        }
    }

    override fun destroy() {
        try {
            p2pListener?.let { cameraP2P?.removeOnP2PCameraListener(it) }
            cameraP2P?.destroyP2P()
            cameraP2P = null
            cameraInstance = null
            p2pListener = null
        } catch (e: Exception) {
            // Log silently
        }
    }
}