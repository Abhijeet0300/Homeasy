package io.homeasy.app.feature_connection.di

import android.content.Context
import com.thingclips.smart.android.ble.IThingBleOperator
import com.thingclips.smart.home.sdk.ThingHomeSdk
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.homeasy.app.feature_connection.data.ScanDevicesBleImpl
import io.homeasy.app.feature_connection.domain.repository.ScanDevicesBle
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScanModule {

    @Provides
    @Singleton
    fun provideBleOperator() : IThingBleOperator = ThingHomeSdk.getBleOperator()

    @Provides
    @Singleton
    fun provideScanDevicesBle(
        bleOperator : IThingBleOperator,
        @ApplicationContext context : Context
    ) : ScanDevicesBle = ScanDevicesBleImpl(bleOperator, context)

}