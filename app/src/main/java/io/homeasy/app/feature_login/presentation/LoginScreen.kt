package io.homeasy.app.feature_login.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.core.utils.ui_components.LoginRegisterScreenTitle
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui_components.AppTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import io.homeasy.app.core.utils.ui.theme.AppTypography
import io.homeasy.app.core.utils.ui.theme.Dark
import io.homeasy.app.core.utils.ui_components.HasAccount
import io.homeasy.app.core.utils.ui_components.Or
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.core.utils.ui_components.SocialMediaLogin
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import io.homeasy.app.feature_login.data.User


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    toRegisterScreen : () -> Unit = {},
    toHomeScreen : () -> Unit = {},
    userViewModel: UserViewModel = hiltViewModel()
) {

    var emailAddress = remember {
        mutableStateOf("")
    }

    var password = remember {
        mutableStateOf("")
    }

    var screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val user by viewModel.user.collectAsState()
    val loginMessage by viewModel.loginMessage.collectAsState()

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

        //Email field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = emailAddress,
                label = stringResource(id = R.string.email_address),
                placeholder = stringResource(id = R.string.enter_email_address),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        }

        Spacer(modifier = Modifier.height(15.dp ))

        //Password field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = password,
                label = stringResource(id = R.string.password),
                placeholder = stringResource(id = R.string.enter_password),
                isPasswordField = true
            )
        }

        //Forgot password
        Spacer(modifier = Modifier.height(5.dp))
        Row (
            modifier = Modifier.width(327.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = AppTypography.bodyMedium,
                color = Dark
            )
        }

        Spacer(modifier = Modifier.height(42.dp))

        RegularButton(
            label = stringResource(id = R.string.login),
            onClick = {
                viewModel.loginWithEmail(email = emailAddress.value, password = password.value)
                if(user != null) {
                    val currentUser = User(
                        id = user!!.uid,
                        email = user!!.email,
                        phone = user!!.mobile,
                        username = user!!.username
                    )
                }
                Log.i("LoginScreen", "User after login: $loginMessage")
            }
        )
        Spacer(modifier = Modifier.height(18.dp))

        //Or
        Or()

        Spacer(modifier = Modifier.height(20.dp))

        //facebook login
        SocialMediaLogin(
            logoId = R.drawable.facebook_logo,
            textId = R.string.login_with_facebook
        ) {}

        Spacer(modifier = Modifier.height(20.dp))

        //Google login
        SocialMediaLogin(
            logoId = R.drawable.google,
            textId = R.string.login_with_google
        ){}


        Spacer(modifier = Modifier.height(screenHeight * 0.2f))

        HasAccount(
            questionTextId = R.string.no_account,
            actionTextId = R.string.register,
            action = {
                toRegisterScreen()
            }
        )

        Spacer(modifier = Modifier.height(20.dp) )
    }
}