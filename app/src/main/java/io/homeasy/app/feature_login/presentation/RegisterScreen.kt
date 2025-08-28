package io.homeasy.app.feature_login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui_components.AppTextField
import io.homeasy.app.core.utils.ui_components.HasAccount
import io.homeasy.app.core.utils.ui_components.LoginRegisterScreenTitle
import io.homeasy.app.core.utils.ui_components.Or
import io.homeasy.app.core.utils.ui_components.RegularButton
import io.homeasy.app.core.utils.ui_components.SocialMediaLogin


@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel : RegisterViewModel = hiltViewModel()
) {
    var emailAddress = remember {
        mutableStateOf("")
    }

    var password = remember {
        mutableStateOf("")
    }

    var confirmPassword = remember {
        mutableStateOf("")
    }
    var screenHeight = LocalConfiguration.current.screenHeightDp.dp

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

        Spacer(modifier = Modifier.height(15.dp ))

        //Confirm Password field
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            AppTextField(
                value = confirmPassword,
                label = stringResource(id = R.string.confirm_password),
                placeholder = stringResource(id = R.string.enter_confirm_password),
                isPasswordField = true
            )
        }
        
        //Spacer
        Spacer(modifier = Modifier.height(15.dp ))

        // terms and conditions
        HasAccount(
            questionTextId = R.string.i_agree,
            actionTextId = R.string.terms_and_conditions
        )

        Spacer(modifier = Modifier.height(15.dp ))

        RegularButton(
            label = stringResource(id = R.string.register),
            onClick = {
                viewModel.sendVerificationCode(email = emailAddress.value, countryCode = "91")
            }
        )

        Spacer(modifier = Modifier.height(15.dp ))

        Or()

        Spacer(modifier = Modifier.height(15.dp ))

        SocialMediaLogin(
            logoId = R.drawable.facebook_logo,
            textId = R.string.login_with_facebook
        ) {}

        Spacer(modifier = Modifier.height(15.dp))

        //Google login
        SocialMediaLogin(
            logoId = R.drawable.google,
            textId = R.string.login_with_google
        ){}

        Spacer(modifier = Modifier.height(screenHeight * 0.1f))

        HasAccount(
            questionTextId = R.string.already_have_an_account,
            actionTextId = R.string.login
        )

        Spacer(modifier = Modifier.height(20.dp) )
    }
}