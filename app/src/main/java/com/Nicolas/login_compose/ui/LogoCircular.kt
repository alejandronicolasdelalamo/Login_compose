package com.Nicolas.login_compose.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Logo: un círculo de color con un icono en el centro.
 *
 * Lo usan las DOS pantallas (candado cerrado en el login, candado abierto
 * en la bienvenida). Crear un composable una vez y reutilizarlo evita
 * repetir código: esa es una de las grandes ventajas de Compose.
 */
@Composable
fun LogoCircular(
    @DrawableRes icono: Int,   // qué icono dibujar (cambia según la pantalla)
    tamanoIcono: Dp,           // tamaño del icono dentro del círculo
) {
    // Box apila elementos uno encima de otro; aquí centramos el icono en el círculo.
    Box(
        modifier = Modifier
            .size(72.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icono),
            contentDescription = null,   // es decorativo: los lectores de pantalla lo ignoran
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(tamanoIcono),
        )
    }
}
