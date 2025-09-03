package io.homeasy.app.feature_login.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.core.utils.ui_components.LoginRegisterScreenTitle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.homeasy.app.R
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui_components.AppTextField
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.feature_login.data.User

@Composable
fun UserDetails(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel(navController.getBackStackEntry(AppRoutes.REGISTER_SCREEN)),
    userViewModel: UserViewModel = hiltViewModel(),
    toHomeScreen : () -> Unit = {}
) {

    var screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.1f))

        var username = remember {
            mutableStateOf("")
        }

        var mobile = remember {
            mutableStateOf("")
        }

        val user by registerViewModel.user.collectAsState()


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LoginRegisterScreenTitle()
        }

        Spacer(
            modifier = Modifier.height(50.dp)
        )

        //Username field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = username,
                label = stringResource(id = R.string.username),
                placeholder = stringResource(id = R.string.enter_username),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        //Mobile field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = mobile,
                label = stringResource(id = R.string.mobile),
                placeholder = stringResource(id = R.string.enter_mobile),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        RegularButton(
            label = stringResource(id = R.string.save_text),
            onClick = {
                user?.let {
                    it.username = username.value
                    it.mobile = mobile.value

                    val newUser = User(
                        id = it.sid,
                        username = it.username,
                        email = it.email,
                        phone = it.mobile
                    )

                    userViewModel.setCurrentUser(newUser)
                    toHomeScreen()
                }
            },
            enabled = if(username.value.isNotBlank() || username.value.isNotEmpty() || mobile.value.isNotBlank() || mobile.value.isNotEmpty()) {
                true
            } else {
                false
            }
        )
    }
}