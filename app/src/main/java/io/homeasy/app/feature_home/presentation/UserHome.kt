package io.homeasy.app.feature_home.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import io.homeasy.app.core.utils.ui_components.RegularButton

@Composable
fun UserHome(
    navController: NavController,
    homeViewModel: HomeViewModel,
    userHomeViewModel: UserHomeViewModel,
    onBackPressed : @Composable () -> Unit
) {

    val selectedHome by homeViewModel.selectedHome.collectAsState()
    val isRoomAdded by homeViewModel.isRoomAdded.collectAsState()
    val roomAddedMessage by homeViewModel.roomAddedMessage.collectAsState()
    val roomList by homeViewModel.roomList.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.i("User home", "${selectedHome?.name}")
        homeViewModel.getRoomListOfSelectedHome()
        Log.i("User home", "${roomList}")
    }

    LazyColumn {
        item{
            RegularButton(
                label = "Add Home",
                onClick = {
                    homeViewModel.addRoom(selectedHome!!.homeId, "Bedroom")
                    homeViewModel.getRoomListOfSelectedHome()
                }
            )
        }

        item{
            if(roomList!!.isEmpty()) {
                Toast.makeText(context, "No room", Toast.LENGTH_SHORT).show()
            } else {
                roomList!!.forEach { roomBean ->
                    Text(
                        text = "${roomBean?.name}"
                    )
                }
            }
        }
    }
}