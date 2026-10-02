package com.example.janakiprepacademy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val JanakiLightColorScheme = lightColorScheme(
    primary = JanakiOrange,
    onPrimary = Color.White,
    primaryContainer = LightOrangeTint,
    onPrimaryContainer = JanakiOrangeDark,
    secondary = JanakiMaroon,
    onSecondary = Color.White,
    secondaryContainer = LightMaroonTint,
    onSecondaryContainer = JanakiMaroonDark,
    tertiary = JanakiGold,
    onTertiary = Color.Black,
    tertiaryContainer = LightGoldTint,
    onTertiaryContainer = JanakiGoldDark,
    background = CreamWhite,
    onBackground = Color(0xFF1C1B1F),
    surface = PureWhite,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = WarmGray,
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    error = IncorrectRed,
    onError = Color.White
)

private val JanakiDarkColorScheme = darkColorScheme(
    primary = JanakiOrangeLight,
    onPrimary = JanakiOrangeDark,
    primaryContainer = JanakiOrange,
    onPrimaryContainer = LightOrangeTint,
    secondary = JanakiMaroonLight,
    onSecondary = JanakiMaroonDark,
    secondaryContainer = JanakiMaroon,
    onSecondaryContainer = LightMaroonTint,
    tertiary = JanakiGoldLight,
    onTertiary = JanakiGoldDark,
    tertiaryContainer = JanakiGold,
    onTertiaryContainer = LightGoldTint,
    background = DarkBackground,
    onBackground = Color(0xFFE6E1E5),
    surface = DarkSurface,
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF601410)
)

@Composable
fun JanakiPrepAcademyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled — we want consistent Janaki brand colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> JanakiDarkColorScheme
        else -> JanakiLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}