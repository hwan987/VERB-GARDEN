package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = GardenDarkPrimary,
    onPrimary = Color.Black,
    primaryContainer = GardenDarkContainer,
    onPrimaryContainer = Color.White,
    secondary = GardenSunAmber,
    onSecondary = Color.Black,
    secondaryContainer = GardenSunContainer,
    onSecondaryContainer = GardenSunOnContainer,
    tertiary = FlowerPink,
    onTertiary = Color.White,
    background = GardenDarkBackground,
    surface = GardenDarkSurface,
    surfaceVariant = GardenDarkSurfaceVariant,
    onBackground = Color.White,
    onSurface = Color.White,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = GardenGreenPrimary,
    onPrimary = GardenGreenOnPrimary,
    primaryContainer = GardenGreenContainer,
    onPrimaryContainer = GardenGreenOnContainer,
    secondary = GardenSunAmber,
    onSecondary = GardenSunAmberOn,
    secondaryContainer = GardenSunContainer,
    onSecondaryContainer = GardenSunOnContainer,
    tertiary = FlowerPink,
    onTertiary = FlowerPinkOn,
    tertiaryContainer = FlowerPinkContainer,
    onTertiaryContainer = FlowerPinkOnContainer,
    background = GardenBackground,
    surface = GardenSurface,
    surfaceVariant = GardenSurfaceVariant,
    outline = GardenOutline,
    onBackground = Color(0xFF1C281E),
    onSurface = Color(0xFF1C281E),
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use intentional garden branding by default
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

