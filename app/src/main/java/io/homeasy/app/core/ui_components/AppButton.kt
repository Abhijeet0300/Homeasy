package io.homeasy.app.core.ui_components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.homeasy.app.core.ui.theme.AppTypography
import io.homeasy.app.core.ui.theme.ButtonColor

@Preview(showBackground = true)
@Composable
fun RegularButton(
    label : String = "Next >",
    enabled : Boolean = true,
    onClick : () -> Unit = {},
    buttonWidth : Dp = 327.dp,
    buttonHeight : Dp = 47.dp
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.height(buttonHeight).width(buttonWidth),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColor
        ),
        shape = RoundedCornerShape(
            corner = CornerSize(12.dp)
        )
    ) {
        Text(
            text = label,
            style = AppTypography.bodyLarge
        )
    }
}