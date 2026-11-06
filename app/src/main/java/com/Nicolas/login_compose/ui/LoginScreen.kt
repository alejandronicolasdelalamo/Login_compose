package com.Nicolas.login_compose.ui

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.Nicolas.login_compose.R
import com.Nicolas.login_compose.ui.theme.Login_composeTheme
import kotlinx.coroutines.delay

// ---------- DATOS DE PRUEBA (hardcodeados) ----------
// En una app real estos datos vendrían de un servidor o de una base de datos.
private const val CORREO_CORRECTO = "alejandro@gmail.com"
private const val CLAVE_CORRECTA = "1234"
private const val NOMBRE_USUARIO = "Alejandro"

// ---------- REGLAS DE SEGURIDAD (extra de validación) ----------
private const val MAX_INTENTOS = 3          // fallos permitidos antes de bloquear el botón
private const val SEGUNDOS_BLOQUEO = 10     // lo que dura el bloqueo temporal

/*
 * PANTALLA 1: LOGIN
 *
 * Idea clave de Compose: la pantalla es una FUNCIÓN que dibuja la interfaz
 * a partir de un ESTADO. Cuando el estado cambia (por ejemplo, al escribir),
 * Compose vuelve a ejecutar la función y la pantalla se actualiza sola.
 *
 * Esta pantalla NO sabe cómo se abre la siguiente. Solo recibe una función
 * ("onLoginCorrecto") y la llama cuando el usuario acierta, entregándole el
 * nombre. Quien la use (MainActivity) decide qué hacer con él.
 */
@Composable
fun LoginScreen(
    onLoginCorrecto: (nombre: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // BoxWithConstraints nos dice cuánto espacio hay para elegir el diseño.
    BoxWithConstraints(modifier = modifier.fillMaxSize().imePadding()) {
        val alto = maxHeight

        if (maxWidth > 900.dp) {
            // Tablet: texto a la izquierda y tarjeta a la derecha
            Row(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = alto)
                    .padding(horizontal = 80.dp, vertical = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(64.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Intro(
                    estiloTitulo = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.weight(1f),
                )
                TarjetaLogin(onLoginCorrecto = onLoginCorrecto, modifier = Modifier.width(440.dp))
            }
        } else {
            // Móvil: texto arriba y tarjeta debajo
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = alto)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Intro(estiloTitulo = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(24.dp))
                TarjetaLogin(onLoginCorrecto = onLoginCorrecto, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

/** Logo de polígonos y saludo con los datos para entrar. */
@Composable
private fun Intro(estiloTitulo: TextStyle, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Logo hecho con polígonos (res/drawable/logo_poligonos.xml). Es decorativo.
        Image(
            painter = painterResource(R.drawable.logo_poligono),
            contentDescription = null,
            modifier = Modifier.size(72.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Hola, usuario. ¿Cómo estás?",
            style = estiloTitulo,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Por favor, inicia sesión con $CORREO_CORRECTO y la contraseña $CLAVE_CORRECTA.",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/** Tarjeta transparente con el formulario. Aquí está la lógica del login. */
@Composable
private fun TarjetaLogin(
    onLoginCorrecto: (nombre: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // ---------- ESTADO ----------
    // "rememberSaveable" guarda el valor mientras la pantalla está viva (y aunque
    // se gire el móvil); "mutableStateOf" avisa a Compose cuando cambia para que redibuje.
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var hayError by rememberSaveable { mutableStateOf(false) }   // ¿mostramos el aviso de error?
    var correoInvalido by rememberSaveable { mutableStateOf(false) }   // ¿el correo no tiene formato de correo?
    var intentosFallidos by rememberSaveable { mutableIntStateOf(0) }  // contador de intentos
    var segundosBloqueo by rememberSaveable { mutableIntStateOf(0) }   // > 0 significa "bloqueado"

    // CUENTA ATRÁS del bloqueo: LaunchedEffect lanza una corrutina que, mientras queden
    // segundos, espera 1 s y resta 1. Al cambiar la clave (bloqueado sí/no) se reinicia.
    LaunchedEffect(segundosBloqueo > 0) {
        while (segundosBloqueo > 0) {
            delay(1000)
            segundosBloqueo--
        }
    }
    val bloqueado = segundosBloqueo > 0

    val focusManager = LocalFocusManager.current
    val forma = RoundedCornerShape(28.dp)

    // ---------- DISEÑO ----------
    Column(
        modifier = modifier
            .clip(forma)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
            .padding(28.dp),
    ) {
        // --- Cabecera: logo + título + subtítulo ---
        LogoCircular(icono = R.drawable.ic_cv, tamanoIcono = 48.dp)
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))

        // --- Campo de correo ---
        CampoTexto(
            etiqueta = "Correo electrónico",
            valor = email,
            alCambiar = {
                email = it           // guardamos lo escrito en el estado
                hayError = false     // y quitamos los errores mientras el usuario corrige
                correoInvalido = false
            },
            hayError = hayError || correoInvalido,
            // Texto de ayuda bajo el campo: solo si el formato del correo es incorrecto.
            textoAyuda = if (correoInvalido) "Escribe un correo válido, por ejemplo nombre@dominio.com" else null,
            teclado = KeyboardType.Email,
            accionTeclado = ImeAction.Next,   // la tecla del teclado salta al campo siguiente
        )
        Spacer(Modifier.height(16.dp))

        // --- Campo de contraseña ---
        CampoTexto(
            etiqueta = "Contraseña",
            valor = password,
            alCambiar = {
                password = it
                hayError = false
            },
            hayError = hayError,
            teclado = KeyboardType.Password,
            accionTeclado = ImeAction.Done,   // la tecla del teclado lo cierra
            // Si la contraseña es visible mostramos el texto tal cual; si no, puntos.
            transformacion = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            // Icono del ojo a la derecha: al pulsarlo cambia el estado.
            iconoFinal = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                        ),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    )
                }
            },
        )

        // --- Mensaje de error: solo aparece si hay error ---
        if (hayError) {
            Spacer(Modifier.height(8.dp))
            Text(
                // Si está bloqueado avisamos de la espera; si no, de los intentos que quedan.
                text = if (bloqueado) {
                    "Demasiados intentos. Espera $segundosBloqueo s para volver a probar."
                } else {
                    "Correo o contraseña incorrectos. Intentos restantes: ${MAX_INTENTOS - intentosFallidos}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }

        Spacer(Modifier.height(24.dp))

        // --- Botón principal: AQUÍ ESTÁ LA LÓGICA DEL LOGIN ---
        Button(
            onClick = {
                focusManager.clearFocus()   // cierra el teclado para que no tape el mensaje de error
                // 1) Validación de formato: si no parece un correo, avisamos y no gastamos intento.
                if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                    correoInvalido = true
                // 2) Comparamos con los datos de prueba (trim() quita espacios sobrantes del correo).
                } else if (email.trim() == CORREO_CORRECTO && password == CLAVE_CORRECTA) {
                    onLoginCorrecto(NOMBRE_USUARIO)   // ¡acertó! Avisamos y enviamos el nombre
                } else {
                    // 3) Falló: contamos el intento y, si se acaban, bloqueamos unos segundos.
                    hayError = true
                    intentosFallidos++
                    if (intentosFallidos >= MAX_INTENTOS) {
                        segundosBloqueo = SEGUNDOS_BLOQUEO
                        intentosFallidos = 0
                    }
                }
            },
            enabled = !bloqueado,   // mientras dura el bloqueo el botón no se puede pulsar
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
            shape = RoundedCornerShape(14.dp),
        ) {
            Text(if (bloqueado) "Espera $segundosBloqueo s" else "Iniciar sesión")
        }
    }
}

/** Campo de texto con su etiqueta encima. Lo usan el correo y la contraseña. */
@Composable
private fun CampoTexto(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit,
    hayError: Boolean,
    teclado: KeyboardType,
    accionTeclado: ImeAction,
    textoAyuda: String? = null,
    transformacion: VisualTransformation = VisualTransformation.None,
    iconoFinal: @Composable (() -> Unit)? = null,
) {
    // Fondo semitransparente: es el mismo color del texto con menos opacidad.
    val fondoCampo = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f)

    Text(
        text = etiqueta,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
    Spacer(Modifier.height(6.dp))
    // OutlinedTextField no guarda nada: muestra "value" y avisa con "onValueChange".
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = hayError,
        supportingText = textoAyuda?.let { { Text(it) } },   // texto pequeño bajo el campo
        visualTransformation = transformacion,
        trailingIcon = iconoFinal,
        keyboardOptions = KeyboardOptions(keyboardType = teclado, imeAction = accionTeclado),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = fondoCampo,
            unfocusedContainerColor = fondoCampo,
            errorContainerColor = fondoCampo,
        ),
    )
}

/** Fondo de las dos pantallas: una foto según el modo y un velo encima para que se lea el texto. */
@Composable
fun FondoPantalla(contenido: @Composable BoxScope.() -> Unit) {
    val modoOscuro = isSystemInDarkTheme()
    val colorVelo = MaterialTheme.colorScheme.background

    val foto = if (modoOscuro) R.drawable.edificios else R.drawable.backdrop

    val velo = if (modoOscuro) {
        Brush.horizontalGradient(
            colorStops = arrayOf(
                0f to colorVelo.copy(alpha = 0.55f),
                0.6f to colorVelo.copy(alpha = 0.15f),
                1f to colorVelo.copy(alpha = 0.35f),
            )
        )
    } else {
        // La foto de modo claro es muy oscura: un velo blanco permite leer las letras negras.
        SolidColor(colorVelo.copy(alpha = 0.7f))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(foto),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(modifier = Modifier.fillMaxSize().background(velo))
        contenido()
    }
}

// Vista previa en Android Studio, con el tamaño exacto del Samsung Galaxy S24 (360 x 780 dp).
@Preview(name = "Galaxy S24", showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun LoginScreenPreview() {
    Login_composeTheme {
        // El fondo ya no está dentro de la pantalla (lo pone MainActivity), así que lo añadimos aquí.
        FondoPantalla { LoginScreen(onLoginCorrecto = {}) }
    }
}
