package io.homeasy.app.core.utils.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.homeasy.app.R

@Preview(showBackground = true)
@Composable
fun OnBoardingPicture(
    location : Int = R.drawable.first_onboarding
) {

    Box(
        modifier = Modifier.height(400.dp).height(400.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = location),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null
        )
    }
}