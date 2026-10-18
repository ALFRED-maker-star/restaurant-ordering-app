package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TastyGoLightColorScheme = lightColorScheme(
    primary = WarmRedPrimary,
    onPrimary = Color.White,
    primaryContainer = WarmRedContainer,
    onPrimaryContainer = OnWarmRedContainer,
    secondary = GoldenYellowAccent,
    onSecondary = Color.White,
    secondaryContainer = GoldenYellowContainer,
    onSecondaryContainer = OnGoldenYellowContainer,
    tertiary = GoldenYellowDark,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = CreamSurface,
    onSurface = TextPrimary,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = WarmBorder,
    outlineVariant = Color(0xFFE5E0D8)
)

private val TastyGoDarkColorScheme = darkColorScheme(
    primary = WarmRedLight,
    onPrimary = Color(0xFF450A0A),
    primaryContainer = WarmRedDark,
    onPrimaryContainer = Color(0xFFFEE2E2),
    secondary = GoldenYellowAccent,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF1C1917),
    onBackground = Color(0xFFF5F5F4),
    surface = Color(0xFF292524),
    onSurface = Color(0xFFF5F5F4),
    surfaceVariant = Color(0xFF44403C),
    onSurfaceVariant = Color(0xFFD6D3D1),
    outline = Color(0xFF57534E)
)

@Composable
fun TastyGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TastyGoDarkColorScheme else TastyGoLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
