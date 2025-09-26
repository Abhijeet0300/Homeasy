package io.homeasy.app.core.utils.ui_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui.theme.AppTypography
import io.homeasy.app.core.utils.ui.theme.ColoredTextColor
import io.homeasy.app.core.utils.ui.theme.White
import io.homeasy.app.feature_login_register.presentation.UserViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.thingclips.smart.home.sdk.bean.RoomBean
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui.theme.Black
import io.homeasy.app.feature_room.presentation.RoomViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenAppBar(
    userViewModel: UserViewModel
) {
    val user by userViewModel.currentUser.collectAsState()
    val username  = user?.username
    val greetingsMap = mapOf<String, Int>(
        "Good Evening, $username" to R.drawable.sun_image,
        "Friday, August 8" to R.drawable.location_icon
    )

    val tempAndPlace = mapOf<Int, String>(
        R.drawable.sun_image to "24° C",
        R.drawable.location_icon to "Washington"
    )

    TopAppBar(
        modifier = Modifier.fillMaxWidth().height(150.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                greetingsMap.forEach { (message1, icon) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message1,
                            style = AppTypography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(50.dp))

                        Box(
                            modifier = Modifier.size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = icon),
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = tempAndPlace[icon].toString(),
                            style = AppTypography.bodyLarge
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ColoredTextColor,
            titleContentColor = White
        )
    )
}


@Composable
fun showRoomNames(roomBean: RoomBean, navController: NavController, roomViewModel: RoomViewModel) {
    Card(
        modifier = Modifier.height(30.dp).width(100.dp).combinedClickable(
            onClick = {
                roomViewModel.setSelectedRoom(roomBean = roomBean)
                navController.navigate(route = AppRoutes.ROOM) {
                    launchSingleTop = true
                }
            }
        ),
        shape = RoundedCornerShape(size = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${roomBean.name}",
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}