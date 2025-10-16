package io.homeasy.app.feature_device_control.camera_new.data

import android.content.Context
import android.os.Environment
import com.thingclips.smart.android.camera.sdk.api.IThingIPCCore
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.AbsP2pCameraListener
import com.thingclips.smart.camera.camerasdk.thingplayer.callback.OperationDelegateCallBack
import com.thingclips.smart.camera.middleware.p2p.IThingSmartCameraP2P
import dagger.hilt.android.qualifiers.ApplicationContext
import io.homeasy.app.feature_device_control.camera_new.domain.repository.CameraRepo
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CameraRepoImpl @Inject constructor(
    private val cameraInstance: IThingIPCCore,
    @ApplicationContext private val context: Context
): CameraRepo {

    private var cameraP2P : IThingSmartCameraP2P<Any?>? = null
    private var listener : AbsP2pCameraListener? = null
    override fun createCameraP2P(devId: String) : IThingSmartCameraP2P<Any?>? {
        cameraP2P = cameraInstance.createCameraP2P(devId)
        return cameraP2P
    }

    override suspend fun startRecording(
        folderPath: String,
        context: Context,
        callback: OperationDelegateCallBack
    ): Result<Unit> {
        return suspendCancellableCoroutine { continuation->
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//            val dir = Environment.getExternalStorageDirectory().absolutePath + folderPath
            val file = File(dir, folderPath)
            if(!file.exists()) {
                file.mkdirs()
            }
            val outputFilePath = "${file.absolutePath}/record_$timeStamp.mp4"

            cameraP2P?.startRecordLocalMp4(outputFilePath, context, object : OperationDelegateCallBack{
                override fun onSuccess(
                    sessionId: Int,
                    requestId: Int,
                    data: String?
                ) {
                    callback.onSuccess(sessionId, requestId, data)
                    continuation.resume(Result.success(Unit), null)
                }

                override fun onFailure(
                    sessionId: Int,
                    requestId: Int,
                    errCode: Int
                ) {
                    callback.onFailure(sessionId, requestId, errCode)
                    continuation.resume(Result.failure(Exception("Recording failed, code=$errCode")), null)
                }
            })
        }
    }

    override suspend fun stopRecording(callback: OperationDelegateCallBack): Result<Unit> {
        return suspendCancellableCoroutine { cont->
            cameraP2P?.stopRecordLocalMp4(object : OperationDelegateCallBack {
                override fun onSuccess(sessionId: Int, requestId: Int, data: String?) {
                    callback.onSuccess(sessionId, requestId, data)
                    cont.resume(Result.success(Unit), null)
                }
                override fun onFailure(sessionId: Int, requestId: Int, errCode: Int) {
                    callback.onFailure(sessionId, requestId, errCode)
                    cont.resume(Result.failure(Exception("Stop recording failed, code=$errCode")), null)
                }
            })
        }
    }

    override fun generateCameraView(view: Any) {
        cameraP2P?.generateCameraView(view)
    }

    override fun registerListener(listener: AbsP2pCameraListener) {
        this.listener = listener
        cameraP2P?.registerP2PCameraListener(this.listener)
    }

    override suspend fun connect(devId: String): Result<Boolean> {
        return suspendCancellableCoroutine { continuation ->
            cameraP2P?.connect(devId, 0, object : OperationDelegateCallBack{
                override fun onSuccess(
                    sessionId: Int,
                    requestId: Int,
                    data: String?
                ) {
                    continuation.resume(Result.success(true), null)
                }

                override fun onFailure(
                    sessionId: Int,
                    requestId: Int,
                    errCode: Int
                ) {
                    continuation.resume(Result.failure(Exception("Connect failed, code=$errCode")), null)
                }
            }) ?: continuation.resume(Result.failure(Exception("Camera not initialized")), null)
        }
    }

    override suspend fun startPreview(): Result<String> {
        return suspendCancellableCoroutine { continuation->
            cameraP2P?.startPreview(2, object : OperationDelegateCallBack{
                override fun onSuccess(
                    sessionId: Int,
                    requestId: Int,
                    data: String?
                ) {
                    continuation.resume(Result.success("Preview started"), null)
                }

                override fun onFailure(
                    sessionId: Int,
                    requestId: Int,
                    errCode: Int
                ) {
                    continuation.resume(Result.failure(Exception("Preview failed, code=$errCode")), null)
                }

            })
        }
    }

    override suspend fun stopPreview(): Result<String> {
        return suspendCancellableCoroutine { continuation->
            cameraP2P?.stopPreview(object : OperationDelegateCallBack{
                override fun onSuccess(
                    sessionId: Int,
                    requestId: Int,
                    data: String?
                ) {
                    continuation.resume(Result.success("Preview stopped"), null)
                }

                override fun onFailure(
                    sessionId: Int,
                    requestId: Int,
                    errCode: Int
                ) {
                    continuation.resume(Result.failure(Exception("Preview failed, code=$errCode")), null)
                }

            })
        }
    }

    override fun destroy() {
        listener?.let { cameraP2P?.removeOnP2PCameraListener(it) }
        cameraP2P?.destroyP2P()
        cameraP2P = null
    }
}