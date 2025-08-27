package io.homeasy.app.feature_onboarding.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.homeasy.app.core.ui_components.OnBoardingPicture
import io.homeasy.app.core.ui_components.WelcomeMessage
import io.homeasy.app.R
import io.homeasy.app.core.ui.theme.AppTypography
import io.homeasy.app.core.ui_components.RegularButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import io.homeasy.app.core.ui.theme.Dark

@Composable
fun FirstOnBoardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnBoardingScreenViewModel = hiltViewModel()
) {
    val index by viewModel.currentIndex.collectAsState()
    var item by remember {
        mutableStateOf(viewModel.currentItem      )
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(screenHeight * 0.05f))

        // Skip button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = stringResource(id = R.string.skip),
                modifier = Modifier.clickable { /* handle skip */ },
                style = AppTypography.bodyLarge,
                color = Dark
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Welcome message
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WelcomeMessage()
            Text(
                text = stringResource(id = item.message),
                style = AppTypography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Onboarding picture
        OnBoardingPicture(location = item.picture)

        Spacer(modifier = Modifier.height(screenHeight * 0.1f))

        // Next button
        RegularButton(
            label = stringResource(id = R.string.next),
            buttonWidth = screenWidth * 0.7f,
            onClick = {
                viewModel.next()
                item = viewModel.currentItem
            }
        )

        Spacer(modifier = Modifier.height(screenHeight * 0.05f))
    }
}