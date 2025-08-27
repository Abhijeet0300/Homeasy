package io.homeasy.app.core.utils.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui.theme.AppTypography
import io.homeasy.app.core.utils.ui.theme.ColoredTextColor

@Preview(showBackground = true)
@Composable
fun WelcomeMessage() {
    Box(
        modifier = Modifier.fillMaxWidth().height(116.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.welcome),
                    style = AppTypography.titleLarge,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.to),
                    style = AppTypography.titleLarge
                )

                HomeasyText()
            }
        }
    }
}

@Composable
fun HomeasyText() {
    Text(
        text = stringResource(id = R.string.homeasy),
        style = AppTypography.titleLarge,
        color = ColoredTextColor
    )
}