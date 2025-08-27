package io.homeasy.app.core.ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import io.homeasy.app.R

class FontProvider {
    private val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    private val fontName = GoogleFont("Inter")
    private val fontFamily = FontFamily(
        Font(
            googleFont = fontName,
            fontProvider = provider
        ),
        // Bold (W700)
        Font(
            googleFont = fontName,
            fontProvider = provider,
            weight = FontWeight.Bold
        ),
        // Black (W900)
        Font(
            googleFont = fontName,
            fontProvider = provider,
            weight = FontWeight.W900
        )
    )

    fun getInterFont() = fontFamily
}