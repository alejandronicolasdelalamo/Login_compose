package com.Nicolas.login_compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Un "ColorScheme" asigna cada color a su ROL dentro de Material 3.
// Las pantallas leen estos roles, así que cambian solas al cambiar de modo.
private val LightColorScheme = lightColorScheme(
    primary = PrimaryClaro,
    onPrimary = OnPrimaryClaro,
    secondary = Ambar,
    tertiary = Naranja,
    primaryContainer = Verde,
    onPrimaryContainer = Blanco,
    background = BackgroundClaro,
    onBackground = OnSurfaceClaro,
    surface = BackgroundClaro,
    onSurface = OnSurfaceClaro,
    onSurfaceVariant = OnSurfaceVariantClaro,
    outline = OutlineClaro,
    outlineVariant = OutlineVariantClaro,
    error = ErrorClaro,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)

// Mejora opcional del tutorial: esquema de colores para el modo oscuro.
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryOscuro,
    onPrimary = OnPrimaryOscuro,
    secondary = Ambar,
    tertiary = Naranja,
    primaryContainer = Verde,
    onPrimaryContainer = Blanco,
    background = BackgroundOscuro,
    onBackground = OnSurfaceOscuro,
    surface = BackgroundOscuro,
    onSurface = OnSurfaceOscuro,
    onSurfaceVariant = OnSurfaceVariantOscuro,
    outline = OutlineOscuro,
    outlineVariant = OutlineVariantOscuro,
    error = ErrorOscuro,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)

@Composable
fun Login_composeTheme(content: @Composable () -> Unit) {
    // isSystemInDarkTheme() es true si el móvil o la tablet está en modo oscuro.
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
