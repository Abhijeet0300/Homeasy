package io.homeasy.app.feature_connection.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.builder.ThingCameraActivatorBuilder
import com.thingclips.smart.sdk.api.IThingActivator
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import com.thingclips.smart.sdk.api.IThingSmartCameraActivatorListener
import com.thingclips.smart.sdk.bean.DeviceBean
import dagger.hilt.android.qualifiers.ApplicationContext
import io.homeasy.app.feature_connection.domain.model.CameraDeviceActivationResult
import io.homeasy.app.feature_connection.domain.model.DeviceActivationResult
import io.homeasy.app.feature_connection.domain.repository.CameraPairingRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import java.util.Hashtable

class CameraPairingRepositoryImpl @Inject constructor(
    private val context : Context
) : CameraPairingRepository {
    override suspend fun getToken(homeId: Long): Result<String> {
       return suspendCancellableCoroutine { continuation ->
           ThingHomeSdk.getActivatorInstance().getActivatorToken(homeId, object : IThingActivatorGetToken {
               override fun onSuccess(token: String?) {
                   if (!token.isNullOrEmpty()) continuation.resume(Result.success(token), null)
                   else continuation.resume(Result.failure(Exception("Empty token")), null)
               }
               override fun onFailure(errorCode: String?, errorMsg: String?) {
                   continuation.resume(Result.failure(Exception("$errorCode: $errorMsg")), null)
               }
           })
        }
    }

    override suspend fun startCameraQrPairing(
        ssid: String,
        password: String,
        homeId: Long,
        timeOutSec: Long
    ) : Flow<CameraDeviceActivationResult> = callbackFlow {
        val job = kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val tokenResult = try {
                getToken(homeId)
            } catch (t: Throwable) {
                Result.failure<String>(t)
            }

            if (tokenResult.isFailure) {
                trySend(CameraDeviceActivationResult.Failure("TOKEN_ERROR", tokenResult.exceptionOrNull()?.message))
                close()
                return@launch
            }

            val token = tokenResult.getOrNull()!!

            // Build activator
            val builder = ThingCameraActivatorBuilder()
                .setContext(context)
                .setSsid(ssid)
                .setPassword(password)
                .setToken(token)
                .setTimeOut(timeOutSec)
                .setListener(object : IThingSmartCameraActivatorListener {
                    override fun onQRCodeSuccess(qrcodeUrl: String?) {
                        if (qrcodeUrl != null) {
                            try {
                                val bmp = createQRCode(qrcodeUrl, 600)
                                trySend(CameraDeviceActivationResult.QrCode(qrcodeUrl, bmp))
                            } catch (e: Exception) {
                                Log.e("CameraPairingRepo", "QR gen failed", e)
                                trySend(CameraDeviceActivationResult.Failure("QR_ERROR", e.message))
                            }
                        }
                    }

                    override fun onError(errorCode: String?, errorMsg: String?) {
                        trySend(CameraDeviceActivationResult.Failure(errorCode, errorMsg))
                    }

                    override fun onActiveSuccess(devResp: DeviceBean?) {
                        if (devResp != null) trySend(CameraDeviceActivationResult.Success(devResp))
                    }
                })

            // create activator and start
            val activator = ThingHomeSdk.getActivatorInstance().newCameraDevActivator(builder)
            activator.createQRCode() // generates the qrcodeUrl via listener
            activator.start()

            // awaitClose will be executed when collector cancels
            awaitClose {
                try {
                    activator.stop()
                    activator.onDestroy()
                } catch (t: Throwable) {
                    // ignore
                }
            }
        }

        // When collector cancels, cancel the job too
        awaitClose {
            job.cancel()
        }
    }

    @Throws(WriterException::class)
    private fun createQRCode(url: String, widthAndHeight: Int): Bitmap {
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.MARGIN] = 0
        val matrix = MultiFormatWriter().encode(
            url,
            BarcodeFormat.QR_CODE,
            widthAndHeight,
            widthAndHeight,
            hints
        )

        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (matrix.get(x, y)) {
                    Color.BLACK // black pixel
                } else {
                    Color.WHITE // background white
                }
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }
}