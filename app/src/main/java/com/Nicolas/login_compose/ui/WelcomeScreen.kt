package com.Nicolas.login_compose.ui


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.Nicolas.login_compose.R
import com.Nicolas.login_compose.ui.theme.Login_composeTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/*
 * PANTALLA 2: BIENVENIDA
 *
 * Recibe DOS cosas desde fuera:
 *   - "nombre": el nombre del usuario, que solo mostramos (parámetro de DATOS).
 *   - "onCerrarSesion": una función que llamamos al pulsar el botón (parámetro de ACCIÓN).
 * La pantalla no sabe qué pasa después de cerrar sesión: eso lo decide quien la usa.
 */
@Composable
fun WelcomeScreen(
    nombre: String,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val forma = RoundedCornerShape(28.dp)

    // Box centra la tarjeta en pantallas anchas, como las tablets.
    Box(modifier = modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        // Column con "horizontalAlignment" centra todos sus hijos en horizontal.
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 440.dp)
                .clip(forma)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Mismo logo que el login, pero con el candado ABIERTO.
            LogoCircular(icono = R.drawable.ic_lock_open, tamanoIcono = 40.dp)

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = nombre,   // <- el parámetro que nos envió la pantalla de login
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(24.dp))

            // weight(1f): los fuegos artificiales ocupan TODO el espacio libre que sobra,
            // y así el botón de abajo queda siempre pegado al final de la tarjeta.
            Fireworks(modifier = Modifier.weight(1f))

            Spacer(Modifier.height(24.dp))

            OutlinedButton(onClick = onCerrarSesion) {
                Text("Cerrar sesión")
            }
        }
    }
}

/**
 * Dibujo de fuegos artificiales hecho con Canvas.
 *
 * Canvas es un "lienzo": dentro de él dibujamos con funciones como
 * drawLine, drawCircle o drawPath. Las coordenadas están pensadas para
 * un lienzo de 312 x 400 (las medidas del diseño en Figma) y se escalan
 * con "u" para que el dibujo quepa en el espacio disponible de cualquier pantalla.
 */
@Composable
private fun Fireworks(modifier: Modifier = Modifier) {
    // Los colores se leen del tema ANTES de entrar en el Canvas.
    val verde = MaterialTheme.colorScheme.primaryContainer
    val ambar = MaterialTheme.colorScheme.secondary
    val naranja = MaterialTheme.colorScheme.tertiary

    // ANIMACIÓN: "progreso" va de 0 a 1 en 1,4 s al aparecer la pantalla. Como es un
    // estado, Compose redibuja el Canvas en cada fotograma y el dibujo "crece".
    // En la vista previa de Android Studio no hay animación: mostramos el dibujo ya completo.
    val enPreview = LocalInspectionMode.current
    val progreso = remember { Animatable(if (enPreview) 1f else 0f) }
    LaunchedEffect(Unit) { progreso.animateTo(1f, tween(durationMillis = 1400, delayMillis = 300, easing = FastOutSlowInEasing)) }

    Canvas(modifier = modifier.fillMaxWidth()) {
        // Unidad de escala "u": el dibujo (312 x 400) se agranda o se encoge para
        // caber en el lienzo SIN deformarse (usamos el lado más limitante).
        val u = minOf(size.width / 312f, size.height / 400f)

        // translate() desplaza todo lo que se dibuja dentro: lo usamos para
        // centrar el dibujo en horizontal si sobra ancho.
        // scale() agranda todo lo de dentro desde el centro del lienzo (pivot), de 0 a 1.
        scale(progreso.value, pivot = center) {
        translate(left = (size.width - 312f * u) / 2f) {
            // Tres explosiones: (centro x, centro y, radio, nº de rayos, grosor, color)
            drawBurst(Offset(156 * u, 120 * u), 96 * u, 14, 5 * u, verde)
            drawBurst(Offset(64 * u, 268 * u), 52 * u, 10, 4 * u, ambar)
            drawBurst(Offset(256 * u, 306 * u), 48 * u, 9, 4 * u, naranja)

            // Destellos pequeños de cuatro puntas: (centro, tamaño, color)
            drawSparkle(Offset(40 * u, 40 * u), 11 * u, ambar)
            drawSparkle(Offset(280 * u, 34 * u), 13 * u, verde)
            drawSparkle(Offset(288 * u, 160 * u), 8 * u, naranja)
            drawSparkle(Offset(24 * u, 150 * u), 9 * u, verde)
            drawSparkle(Offset(150 * u, 250 * u), 10 * u, naranja)
            drawSparkle(Offset(160 * u, 380 * u), 7 * u, ambar)
        }
        }
    }
}

/** Una explosión: rayos que salen del centro, alternando largos y cortos, con un punto en cada punta. */
private fun DrawScope.drawBurst(centro: Offset, radio: Float, rayos: Int, grosor: Float, color: Color) {
    val radioPunto = grosor * 0.9f

    for (i in 0 until rayos) {
        // Cada rayo sale en un ángulo distinto: repartimos la vuelta completa (2π) entre todos.
        val angulo = 2 * PI * i / rayos - PI / 2          // "- PI / 2" hace que el primero apunte arriba
        val direccion = Offset(cos(angulo).toFloat(), sin(angulo).toFloat())
        val largo = if (i % 2 == 0) radio else radio * 0.72f   // pares: largos, impares: cortos

        drawLine(
            color = color,
            start = centro + direccion * (radio * 0.38f),   // empieza un poco separado del centro
            end = centro + direccion * largo,
            strokeWidth = grosor,
            cap = StrokeCap.Round,                          // puntas redondeadas
        )
        drawCircle(color, radioPunto, centro + direccion * (largo + radioPunto * 3.2f))  // punto en la punta
    }
    drawCircle(color, radioPunto * 1.4f, centro)            // punto en el centro
}

/** Destello: una estrella de cuatro puntas dibujada uniendo 8 puntos con líneas rectas. */
private fun DrawScope.drawSparkle(centro: Offset, medida: Float, color: Color) {
    val c = medida * 0.2f   // distancia de las "esquinas interiores" al centro
    val estrella = Path().apply {
        moveTo(centro.x, centro.y - medida)       // punta de arriba
        lineTo(centro.x + c, centro.y - c)
        lineTo(centro.x + medida, centro.y)       // punta de la derecha
        lineTo(centro.x + c, centro.y + c)
        lineTo(centro.x, centro.y + medida)       // punta de abajo
        lineTo(centro.x - c, centro.y + c)
        lineTo(centro.x - medida, centro.y)       // punta de la izquierda
        lineTo(centro.x - c, centro.y - c)
        close()
    }
    drawPath(estrella, color)
}

// Vista previa con el tamaño del Samsung Galaxy S24. Aquí el nombre es solo de ejemplo.
@Preview(name = "Galaxy S24", showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun WelcomeScreenPreview() {
    Login_composeTheme {
        FondoPantalla { WelcomeScreen(nombre = "Alejandro", onCerrarSesion = {}) }
    }
}
