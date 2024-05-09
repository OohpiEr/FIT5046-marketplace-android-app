package com.example.marketplace.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val LightColors = lightColorScheme(
    primary = marketplace_light_primary,
    onPrimary = marketplace_light_onPrimary,
    primaryContainer = marketplace_light_primaryContainer,
    onPrimaryContainer = marketplace_light_onPrimaryContainer,
    secondary = marketplace_light_secondary,
    onSecondary = marketplace_light_onSecondary,
    secondaryContainer = marketplace_light_secondaryContainer,
    onSecondaryContainer = marketplace_light_onSecondaryContainer,
    tertiary = marketplace_light_tertiary,
    onTertiary = marketplace_light_onTertiary,
    tertiaryContainer = marketplace_light_tertiaryContainer,
    onTertiaryContainer = marketplace_light_onTertiaryContainer,
    error = marketplace_light_error,
    errorContainer = marketplace_light_errorContainer,
    onError = marketplace_light_onError,
    onErrorContainer = marketplace_light_onActive,
    background = marketplace_light_background,
    onBackground = marketplace_light_onBackground,
    surface = marketplace_light_surface,
    onSurface = marketplace_light_onSurface,
    surfaceVariant = marketplace_light_surfaceVariant,
    onSurfaceVariant = marketplace_light_onSurfaceVariant,
    outline = marketplace_light_outline,
    inverseOnSurface = marketplace_light_inverseOnSurface,
    inverseSurface = marketplace_light_inverseSurface,
    inversePrimary = marketplace_light_inversePrimary,
    surfaceTint = marketplace_light_surfaceTint,
    outlineVariant = marketplace_light_outlineVariant,
    scrim = marketplace_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = marketplace_dark_primary,
    onPrimary = marketplace_dark_onPrimary,
    primaryContainer = marketplace_dark_primaryContainer,
    onPrimaryContainer = marketplace_dark_onPrimaryContainer,
    secondary = marketplace_dark_secondary,
    onSecondary = marketplace_dark_onSecondary,
    secondaryContainer = marketplace_dark_secondaryContainer,
    onSecondaryContainer = marketplace_dark_onSecondaryContainer,
    tertiary = marketplace_dark_tertiary,
    onTertiary = marketplace_dark_onTertiary,
    tertiaryContainer = marketplace_dark_tertiaryContainer,
    onTertiaryContainer = marketplace_dark_onTertiaryContainer,
    error = marketplace_dark_error,
    errorContainer = marketplace_dark_errorContainer,
    onError = marketplace_dark_onError,
    onErrorContainer = marketplace_dark_onErrorContainer,
    background = marketplace_dark_background,
    onBackground = marketplace_dark_onBackground,
    surface = marketplace_dark_surface,
    onSurface = marketplace_dark_onSurface,
    surfaceVariant = marketplace_dark_surfaceVariant,
    onSurfaceVariant = marketplace_dark_onSurfaceVariant,
    outline = marketplace_dark_outline,
    inverseOnSurface = marketplace_dark_inverseOnSurface,
    inverseSurface = marketplace_dark_inverseSurface,
    inversePrimary = marketplace_dark_inversePrimary,
    surfaceTint = marketplace_dark_surfaceTint,
    outlineVariant = marketplace_dark_outlineVariant,
    scrim = marketplace_dark_scrim,
)

@Composable
fun MarketplaceTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkColors
  }

  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}

