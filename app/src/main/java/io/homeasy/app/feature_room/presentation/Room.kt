package io.homeasy.app.feature_room.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui.theme.White
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.thingclips.smart.sdk.bean.DeviceBean
import io.homeasy.app.core.utils.ui.theme.Orange
import io.homeasy.app.R
import io.homeasy.app.feature_connection.domain.model.DeviceType
import io.homeasy.app.feature_connection.domain.model.toDeviceType

@Composable
fun Room(
    navController : NavController,
    roomViewModel: RoomViewModel,
    homeViewModel: HomeViewModel
) {
    val roomBean by roomViewModel.selectedRoom.collectAsState()
    val context = LocalContext.current
    val isDeviceAdded by roomViewModel.isDeviceAdded.collectAsState()
    var deviceList = emptyList<DeviceBean>()

    LaunchedEffect(Unit) {
        Log.i("Room View Model", "Home Id : ${homeViewModel.selectedHome.value!!.homeId}, roomId : ${roomBean?.roomId}")
        roomViewModel.getRoomDetails(homeId = homeViewModel.selectedHome.value!!.homeId, roomId = roomBean?.roomId ?: 0)
        deviceList = roomBean?.deviceList ?: emptyList()
    }

    LaunchedEffect(isDeviceAdded) {
        if(isDeviceAdded == true) {
            Toast.makeText(context, "Device added", Toast.LENGTH_SHORT).show()
        }
    }


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // room devices
        item{
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(roomBean?.deviceList?.size ?: 0) { index ->
                    val deviceBean = roomBean?.deviceList!!.get(index)
                    val deviceType = deviceBean.toDeviceType()
                    RegularButton(
                        label = "${deviceBean.name}",
                        onClick = {
                            when (deviceType) {
                                DeviceType.LIGHT -> {
                                    roomViewModel.setSelectedDevice(deviceBean)
                                    navController.navigate(route = AppRoutes.LIGHT_SCREEN)
                                }
                                DeviceType.CAMERA -> {
                                    roomViewModel.setSelectedDevice(deviceBean)
                                    navController.navigate(route = AppRoutes.C_CONTROL)
                                }
                                else -> {
                                    Toast.makeText(context, "Unknown device type", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    )
//                    RoomDevices(
//                        deviceBean = roomBean?.deviceList!!.get(index),
//                        navController = navController,
//                        roomViewModel = roomViewModel
//                    )
                }
            }
        }

        //
        item{
            RegularButton(
                label = "Add Devices",
                onClick = {
                    navController.navigate(route= AppRoutes.CHECKING_PERMISSIONS)
                }
            )
        }
    }
}

@Composable
fun RoomDevices(
    deviceBean : DeviceBean,
    navController: NavController,
    roomViewModel : RoomViewModel
) {
    Card(
        modifier = Modifier.size(150.dp).combinedClickable(
            onClick = {
                roomViewModel.setSelectedDevice(deviceBean)
                navController.navigate(route = AppRoutes.LIGHT_SCREEN)
            }
        ),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Orange,
        )
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.bulb),
                    contentDescription = null
                )

                Text(
                    text = "${deviceBean.name}"
                )
            }
        }
    }
}
