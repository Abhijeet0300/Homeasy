package io.homeasy.app.feature_home.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui_components.AppTextField
import io.homeasy.app.core.utils.ui_components.LoginRegisterScreenTitle
import io.homeasy.app.core.utils.ui_components.RegularButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import io.homeasy.app.feature_home.presentation.viewmodel.HomeViewModel


@Composable
fun AddHome(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    var screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val homeName = remember {
        mutableStateOf("")
    }

    val geographicalName = remember {
        mutableStateOf("")
    }

    val roomList = listOf<String>(
        "Bedroom",
        "Kitchen",
        "Hall"
    )
    val context = LocalContext.current
    val homeBean by homeViewModel.homeBean.collectAsState()
    val homeCreationMessage by homeViewModel.homeCreationMessage.collectAsState()
    val isSuccessfullyCreated by homeViewModel.isSuccessfullyCreated.collectAsState()

    LaunchedEffect(isSuccessfullyCreated) {
        if(isSuccessfullyCreated){
            Toast.makeText(context, homeCreationMessage, Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.1f))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LoginRegisterScreenTitle()
        }

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        //Add home field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = homeName,
                label = stringResource(id = R.string.enter_home_name),
                placeholder = stringResource(id = R.string.enter_home_name),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        //geo name field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = geographicalName,
                label = stringResource(id = R.string.enter_geo_name),
                placeholder = stringResource(id = R.string.enter_geo_name),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        RegularButton(
            label = stringResource(id = R.string.save_text),
            onClick = {
                homeViewModel.createHome(
                    name = homeName.value,
                    geoName = geographicalName.value,
                    rooms = roomList
                )


            },
            enabled = if(homeName.value.isNotBlank() || homeName.value.isNotEmpty() || geographicalName.value.isNotBlank() || geographicalName.value.isNotEmpty()) {
                true
            } else {
                false
            }
        )
    }
}