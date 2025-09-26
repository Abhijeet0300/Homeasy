package io.homeasy.app.feature_room.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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

@Composable
fun Room(
    navController : NavController,
    roomViewModel: RoomViewModel,
    homeViewModel: HomeViewModel
) {
    val roomBean by roomViewModel.selectedRoom.collectAsState()
    val context = LocalContext.current
    val isDeviceAdded by roomViewModel.isDeviceAdded.collectAsState()

    LaunchedEffect(Unit) {
        Log.i("Room View Model", "Home Id : ${homeViewModel.selectedHome.value!!.homeId}, roomId : ${roomBean?.roomId}")
        roomViewModel.getRoomDetails(homeId = homeViewModel.selectedHome.value!!.homeId, roomId = roomBean?.roomId ?: 0)
    }

    LaunchedEffect(isDeviceAdded) {
        if(isDeviceAdded == true) {
            Toast.makeText(context, "Device added", Toast.LENGTH_SHORT).show()
        }
    }


    LazyColumn {
        // room devices
        item{
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(roomBean?.deviceList?.size ?: 0) {
                    RoomDevices()
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
fun RoomDevices() {
    Card(
        modifier = Modifier.size(100.dp),
        shape = RoundedCornerShape(size = 12.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = White,
        )
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Default.Home,
                contentDescription = null
            )
        }
    }
}
