package io.homeasy.app.feature_login.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.homeasy.app.core.utils.ui_components.LoginRegisterScreenTitle
import io.homeasy.app.R
import io.homeasy.app.core.navigation.AppRoutes
import io.homeasy.app.core.utils.ui.theme.AppTypography
import io.homeasy.app.core.utils.ui.theme.Black
import io.homeasy.app.core.utils.ui_components.RegularButton
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onOtpVerified: () -> Unit = {},
    registerViewModel: RegisterViewModel = hiltViewModel(navController.getBackStackEntry(AppRoutes.REGISTER_SCREEN))
) {
    var screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var otpValue by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val registrationMessage by registerViewModel.registrationMessage.collectAsState()
    val isValidOtp by registerViewModel.isValidOtp.collectAsState()
    val otpVerificationMessage by registerViewModel.otpVerificationMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // A small delay can sometimes help ensure the UI is fully ready
        delay(100)
        focusRequester.requestFocus()
        keyboardController?.show()
        Log.i("OtpScreen", email)
    }

    LaunchedEffect(isValidOtp) {
        if (isValidOtp == true) {
            Log.i("OtpScreen", otpVerificationMessage ?: "OTP is valid")
            registerViewModel.registerWithEmail(
                email = email,
                password = password,
                verificationCode = otpValue
            )
            Toast.makeText(context, "$registrationMessage", Toast.LENGTH_SHORT).show()
            onOtpVerified()
        } else if (isValidOtp == false) {
            Log.e("OtpScreen", otpVerificationMessage ?: "OTP is invalid")
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

        Spacer(modifier = Modifier.height(40.dp))

        //Code sent message
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.verify_your_phone),
                style = AppTypography.bodyLarge,
                color = Black
            )
            Text(
                text = stringResource(id = R.string.sent_6_digit_code),
                style = AppTypography.bodyMedium,
                color = Black
            )
            Text(
                text = stringResource(id = R.string.to_email),
                style = AppTypography.bodyLarge,
                color = Black
            )
        }

        //Otp Text Fields
        Spacer(
            modifier = Modifier.height(91.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) { index ->
                    val char = otpValue.getOrNull(index)?.toString() ?: ""
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable(
                                onClick = {
                                    keyboardController?.show()
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = AppTypography.bodyLarge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Hidden text field to capture OTP input
            BasicTextField(
                modifier = Modifier.focusRequester(focusRequester),
                value = otpValue,
                onValueChange = { newValue ->
                    if(newValue.length <= 6 && newValue.all { ch -> ch.isDigit() }) {
                        otpValue = newValue
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                decorationBox = {}
            )

        }



        Spacer(modifier = Modifier.height(40.dp))

        //verify button

        RegularButton(
            label  = stringResource(id = R.string.verify_code),
            onClick = {
                Log.i("OtpScreen", "Verify button clicked with OTP: $otpValue" )
                registerViewModel.verifyCode(
                    email = email,
                    verificationCode = otpValue
                )
            }
        )

        //resend verification code
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            modifier = Modifier.clickable(
                onClick = {
                    registerViewModel.sendVerificationCode()
                }
            ),
            text = stringResource(id = R.string.resend_verification_code),
            style = AppTypography.bodyMedium,
            color = Black
        )
    }
}