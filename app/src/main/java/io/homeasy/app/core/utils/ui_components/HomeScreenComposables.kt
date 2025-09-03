package io.homeasy.app.core.utils.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.homeasy.app.R
import io.homeasy.app.core.utils.ui.theme.AppTypography
import io.homeasy.app.core.utils.ui.theme.ColoredTextColor
import io.homeasy.app.core.utils.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeasyAppBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Column(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.good_evening) + "",
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ColoredTextColor,
            titleContentColor = White
        )
    )
}