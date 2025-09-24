package io.homeasy.app.feature_devices.data

import android.content.Context
import com.thingclips.smart.android.ble.IThingBleOperator
import com.thingclips.smart.android.ble.api.LeScanSetting
import com.thingclips.smart.android.ble.api.ScanDeviceBean
import com.thingclips.smart.android.ble.api.ScanType
import dagger.hilt.android.qualifiers.ApplicationContext
import io.homeasy.app.feature_devices.domain.repository.ScanDevicesBle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ScanDevicesBleImpl @Inject constructor(
    private val bleOperator : IThingBleOperator,
    @ApplicationContext private val context : Context
) : ScanDevicesBle {
    override fun scanForDevices(timeout: Long): Flow<ScanDeviceBean> = callbackFlow {
        val scanSetting = LeScanSetting.Builder()
            .setTimeout(timeout)
            .addScanType(ScanType.SINGLE)
            .setRepeatFilter(false)
            .build()

        bleOperator.startLeScan(scanSetting) { bean : ScanDeviceBean ->
            bean?.let { trySend(it) }
        }

        awaitClose{
            bleOperator.stopLeScan()
        }
    }

}