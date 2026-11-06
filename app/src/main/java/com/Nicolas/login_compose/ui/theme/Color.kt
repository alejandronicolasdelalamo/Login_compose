package com.Nicolas.login_compose.ui.theme

import androidx.compose.ui.graphics.Color

// Colores de la app. Cada uno se asigna a su ROL de Material 3 en Theme.kt.
// En los colores de 8 cifras, las dos primeras indican la transparencia.

// ---------- Modo claro: letras y bordes negros ----------
val PrimaryClaro = Color(0xFF000000)            // botón principal
val OnPrimaryClaro = Color(0xFFFFFFFF)          // texto sobre el botón principal
val BackgroundClaro = Color(0xFFFFFFFF)         // velo blanco sobre la foto
val OnSurfaceClaro = Color(0xFF000000)          // texto principal
val OnSurfaceVariantClaro = Color(0xDB000000)   // texto secundario
val OutlineClaro = Color(0x99000000)            // borde de campos y botón con contorno
val OutlineVariantClaro = Color(0x57000000)     // borde de la tarjeta
val ErrorClaro = Color(0xFF9F1239)              // borde de los campos con error

// ---------- Modo oscuro: letras y bordes blancos ----------
val PrimaryOscuro = Color(0xFFFFFFFF)
val OnPrimaryOscuro = Color(0xFF000000)
val BackgroundOscuro = Color(0xFF0A0828)        // velo oscuro sobre la foto
val OnSurfaceOscuro = Color(0xFFFFFFFF)
val OnSurfaceVariantOscuro = Color(0xDBFFFFFF)
val OutlineOscuro = Color(0x99FFFFFF)
val OutlineVariantOscuro = Color(0x57FFFFFF)
val ErrorOscuro = Color(0xFFFDA4AF)

// ---------- Iguales en los dos modos ----------
val Verde = Color(0xFF059669)                   // círculo del logo y fuegos artificiales
val Blanco = Color(0xFFFFFFFF)                  // icono del candado
val Ambar = Color(0xFFF59E0B)                   // fuegos artificiales
val Naranja = Color(0xFFEA580C)                 // fuegos artificiales
val ErrorContainer = Color(0xFF9F1239)          // fondo del mensaje de error
val OnErrorContainer = Color(0xFFFFE4E6)        // texto del mensaje de error
