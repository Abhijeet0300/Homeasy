package io.homeasy.app.core.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.R
import io.homeasy.app.core.ui.theme.AppTypography
import io.homeasy.app.core.ui.theme.Black
import io.homeasy.app.core.ui.theme.ColoredTextColor
import io.homeasy.app.core.ui.theme.Dark

@Preview(showBackground = true)
@Composable
fun LoginRegisterScreenTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.homeasy),
            style = AppTypography.titleLarge,
            color = ColoredTextColor
        )
        Text(
            text = stringResource(id = R.string.smart_living),
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordEye() {
    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.eye),
            contentDescription = stringResource(id = R.string.password_toggle)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Or() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier.width(140.dp).height(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.faded_line1),
                contentDescription = null
            )
        }

        Text(
            text = stringResource(id = R.string.or),
            style = AppTypography.bodyLarge,
            color = Dark
        )

        Box(
            modifier = Modifier.width(140.dp).height(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.faded_line2),
                contentDescription = null
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SocialMediaLogin(
    logoId : Int = R.drawable.facebook_logo,
    textId : Int = R.string.login_with_facebook,
    onClick : () -> Unit = {}
){
    Box(
        modifier = Modifier
            .width(327.dp)
            .height(47.dp)
            .border(
                width = 1.dp,
                color = Dark,
                shape = RoundedCornerShape(14.dp)
            )
            .combinedClickable(
                onClick = {
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = logoId),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(id = textId),
                style = AppTypography.bodyLarge,
                color = Dark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HasAccount(
    questionTextId : Int = R.string.no_account,
    actionTextId : Int = R.string.register
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = questionTextId),
            style = AppTypography.bodyLarge,
            color = Black
        )

        Text(
            modifier = Modifier.combinedClickable(
                onClick = {}
            ),
            text = stringResource(id = actionTextId),
            style = AppTypography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = ColoredTextColor
        )
    }
}