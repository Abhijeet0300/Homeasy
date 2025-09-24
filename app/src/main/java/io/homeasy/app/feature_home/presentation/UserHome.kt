package io.homeasy.app.feature_home.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui.theme.White
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.core.utils.ui_components.showRoomNames
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel
import io.homeasy.app.feature_home.presentation.viewmodel.UserHomeViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

@Composable
fun UserHome(
    navController: NavController,
    homeViewModel: HomeViewModel,
    roomViewModel: RoomViewModel,
    onBackPressed : @Composable () -> Unit
) {

    val selectedHome by homeViewModel.selectedHome.collectAsState()
    val isRoomAdded by homeViewModel.isRoomAdded.collectAsState()
    val roomAddedMessage by homeViewModel.roomAddedMessage.collectAsState()
    val roomList by homeViewModel.roomList.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.i("User home", "${selectedHome?.name}")
        homeViewModel.roomListOfSelectedHome(selectedHome?.homeId!!)
        Log.i("User home", "${roomList?.size}")
    }

    LaunchedEffect(isRoomAdded) {
        if (isRoomAdded != null) {
            if(isRoomAdded!!) {
                Toast.makeText(context, roomAddedMessage, Toast.LENGTH_SHORT).show()
                homeViewModel.updateRoomListOfSelectedHome()
                homeViewModel.setIsRoomAddedToNull()
                navController.popBackStack()
            } else {
                Toast.makeText(context, roomAddedMessage, Toast.LENGTH_SHORT).show()
                homeViewModel.setIsRoomAddedToNull()
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(White),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
//        item{
//            RegularButton(
//                label = "Add Room",
//                onClick = {
//                    homeViewModel.addRoom(selectedHome!!.homeId, "Bedroom")
//                    homeViewModel.updateRoomListOfSelectedHome()
//                }
//            )
//        }

        item{
            Spacer(modifier = Modifier.fillMaxWidth().height(0.dp))
        }

        item {
            if(roomList!!.isEmpty()) {
                Toast.makeText(context, "No room", Toast.LENGTH_SHORT).show()
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(roomList!!.size) { index ->
                        showRoomNames(
                            roomBean = roomList!![index]!!,
                            navController = navController,
                            roomViewModel = roomViewModel
                        )
                    }
                }
            }
        }

        item{
            Button(
                onClick = {
                    navController.navigate(route= AppRoutes.CHECKING_PERMISSIONS)
                }
            ) {
                Text(
                    text = "Add Devices"
                )
            }
        }
    }
}