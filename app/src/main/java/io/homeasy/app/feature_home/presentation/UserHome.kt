package io.homeasy.app.feature_home.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.homeasy.app.core.utils.ui_components.RegularButton

@Composable
fun UserHome(
    navController: NavController,
    homeViewModel: HomeViewModel
) {

    val selectedHome by homeViewModel.selectedHome.collectAsState()

    Text(
        text = "${selectedHome?.name}"
    )

    RegularButton(
        label = "Add home",
        onClick = {

        }
    )

}