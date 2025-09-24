package io.homeasy.app.feature_devices.domain.usecases

import com.thingclips.smart.android.ble.api.ScanDeviceBean
import io.homeasy.app.feature_devices.domain.repository.ScanDevicesBle
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanDevicesBleUseCase @Inject constructor(
    private val scanDevicesBle: ScanDevicesBle
) {
    operator fun invoke() : Flow<ScanDeviceBean> = scanDevicesBle.scanForDevices()
}