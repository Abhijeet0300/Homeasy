package io.homeasy.app.feature_devices.domain.repository

import com.thingclips.smart.android.ble.api.ScanDeviceBean
import kotlinx.coroutines.flow.Flow

interface ScanDevicesBle {
    fun scanForDevices(timeout : Long = 1000L) : Flow<ScanDeviceBean>
}