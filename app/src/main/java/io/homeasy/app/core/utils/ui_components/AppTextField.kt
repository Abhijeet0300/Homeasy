package io.homeasy.app.core.utils.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.core.utils.ui.theme.AppTypography
import io.homeasy.app.core.utils.ui.theme.Black
import io.homeasy.app.core.utils.ui.theme.Grey
import io.homeasy.app.core.utils.ui.theme.White

@Preview(showBackground = true)
@Composable
fun  AppTextField(
    value : MutableState<String> = mutableStateOf(""),
    label : String = "Label",
    placeholder :String = "abc@gmail.com",
    height : Int = 47,
    width : Int = 327,
    isPasswordField : Boolean = false,
    isSingleLine : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    imeAction : ImeAction = ImeAction.Done
) {

    val passwordVisible = remember {
        mutableStateOf(false)
    }

    val focusManager = LocalFocusManager.current

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = label,
            style = AppTypography.bodyLarge,
            color = Black
        )
        OutlinedTextField(
            modifier = Modifier.height(height.dp).width(width.dp),
            value = value.value,
            onValueChange = {value.value = it},
            placeholder = {
                Text(
                    text = placeholder,
                    style = AppTypography.bodyMedium,
                    color = Black
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                focusedBorderColor = Grey,
                unfocusedBorderColor = Grey
            ),
            shape = RoundedCornerShape(corner = CornerSize(14.dp)),
            visualTransformation = if(isPasswordField) {
                if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation()
            } else VisualTransformation.None,
            trailingIcon = {
                if(isPasswordField) {
                    IconButton(onClick = {passwordVisible.value = !passwordVisible.value}) {
                        PasswordEye()
                    }
                }
            },
            textStyle = AppTypography.bodyMedium,
            singleLine = isSingleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }
}