package io.homeasy.app.feature_device_control.light.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.homeasy.app.core.utils.ui.theme.White
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.homeasy.app.core.utils.ui.theme.ColoredTextColor
import io.homeasy.app.core.utils.ui.theme.Grey
import io.homeasy.app.feature_device_control.light.presentation.viewmodel.LightScreenViewModel
import io.homeasy.app.feature_room.presentation.RoomViewModel

//@Preview(showBackground = true)
@Composable
fun LightScreen(
    roomViewModel: RoomViewModel,
    viewModel : LightScreenViewModel
) {
    val colorController = rememberColorPickerController()
    val selectedDevice by roomViewModel.selectedDevice.collectAsState()

    var hsv by remember {
        mutableStateOf("")
    }

    var switch by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Card(
            modifier = Modifier.size(350.dp),
            shape = RoundedCornerShape(size = 12.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = switch,
                        onCheckedChange = { value ->
                            if(selectedDevice != null) {
                                viewModel.toggleLights(id = selectedDevice!!.devId, turnOn = value)
                            }
                            switch = value
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            uncheckedThumbColor = White,
                            checkedTrackColor = ColoredTextColor,
                            uncheckedTrackColor = Grey
                        )
                    )
                }

                HsvColorPicker(
                    modifier = Modifier.height(350.dp).width(250.dp),
                    controller = colorController,
                    onColorChanged = {colorEnvelope : ColorEnvelope ->
                        hsv = argbToHsv(colorEnvelope.color)
                    }
                )
            }
        }
    }
}


fun argbToHsv(color : Color) : String {
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(color.toArgb(), hsv)
    val hueScaled = hsv[0].toInt()
    val saturationScaled = (hsv[1] * 1000).toInt()
    val valueScaled = (hsv[2] * 1000).toInt()
    val hueHex = String.format("%04X", hueScaled)
    val saturationHex = String.format("%04X", saturationScaled)
    val valueHex = String.format("%04X", valueScaled)
    return "$hueHex$saturationHex$valueHex"
}