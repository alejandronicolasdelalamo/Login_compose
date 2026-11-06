package com.Nicolas.login_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.unit.IntOffset
import com.Nicolas.login_compose.ui.FondoPantalla
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.Nicolas.login_compose.ui.LoginScreen
import com.Nicolas.login_compose.ui.WelcomeScreen
import com.Nicolas.login_compose.ui.theme.Login_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La app se dibuja bajo la barra de estado y la de navegación (pantalla completa).
        enableEdgeToEdge()
        // setContent: aquí empieza Compose. Todo lo de dentro es interfaz declarativa.
        setContent {
            Login_composeTheme {
                App()
            }
        }
    }
}

/**
 * Decide QUÉ pantalla se ve, usando un único estado: el nombre del usuario.
 *
 *   nombreUsuario == null   ->  nadie ha iniciado sesión  ->  mostramos el LOGIN
 *   nombreUsuario != null   ->  sesión iniciada           ->  mostramos la BIENVENIDA
 *
 * Cuando el login avisa de que el usuario acertó, guardamos su nombre en el
 * estado; Compose redibuja y cambia de pantalla solo. Al cerrar sesión hacemos
 * lo contrario: volvemos a poner el estado en null. No hace falta ninguna
 * librería de navegación para este ejemplo.
 *
 * ¿Por qué el formulario vuelve LIMPIO? Porque al cambiar a la bienvenida la
 * pantalla de login desaparece de la composición y su estado (lo que se había
 * escrito) se descarta. Al volver, Compose la crea de cero y los campos están vacíos.
 */
@Composable
private fun App() {
    var nombreUsuario by rememberSaveable { mutableStateOf<String?>(null) }

    // Scaffold nos da el fondo del tema y calcula "innerPadding":
    // el espacio que ocupan las barras del sistema, para no taparnos con ellas.
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        // ANIMACIÓN: AnimatedContent vigila el estado (nombreUsuario). Cuando cambia,
        // la pantalla vieja se desvanece rápido (300 ms) y, justo después, la nueva entra
        // (700 ms) con tres efectos a la vez: fundido, desplazamiento lateral y zoom.
        // El fondo (foto) NO se anima: está fuera, así que el cambio se nota en la tarjeta.
        FondoPantalla {
            AnimatedContent(
                targetState = nombreUsuario,
                transitionSpec = {
                    // Al iniciar sesión entra desde la derecha; al cerrarla, desde la izquierda.
                    val sentido = if (targetState != null) 1 else -1
                    val entrada = tween<Float>(700, delayMillis = 250, easing = FastOutSlowInEasing)
                    val entradaDesplazamiento = tween<IntOffset>(700, delayMillis = 250, easing = FastOutSlowInEasing)
                    (fadeIn(entrada) +
                        slideInHorizontally(entradaDesplazamiento) { it / 3 * sentido } +
                        scaleIn(entrada, initialScale = 0.85f))
                        .togetherWith(fadeOut(tween(300)))
                },
                label = "transicionPantallas",
            ) { nombre ->
                if (nombre == null) {
                    // onLoginCorrecto se ejecuta cuando el login acierta; "it" es el nombre recibido.
                    LoginScreen(
                        onLoginCorrecto = { nombreUsuario = it },
                        modifier = modifier,
                    )
                } else {
                    // Pasamos el nombre como parámetro a la segunda pantalla.
                    // onCerrarSesion se ejecuta al pulsar el botón: borramos el nombre y
                    // AnimatedContent vuelve a mostrar el login.
                    WelcomeScreen(
                        nombre = nombre,
                        onCerrarSesion = { nombreUsuario = null },
                        modifier = modifier,
                    )
                }
            }
        }
    }
}
