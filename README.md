# Login_compose

App Android de ejemplo con **Jetpack Compose** y **Material 3**: una pantalla de login y una pantalla de bienvenida, con diseño adaptable a móvil y tablet, modo claro y oscuro.

## Capturas

| Modo claro | Modo oscuro |
|:---:|:---:|
| ![Login en modo claro](screenshots/login-claro.png) | ![Login en modo oscuro](screenshots/login-oscuro.png) |

## Características

- **Login** con correo y contraseña (datos de prueba hardcodeados) y mensaje de error si fallan.
- **Pantalla de bienvenida** con el nombre del usuario, fuegos artificiales dibujados con `Canvas` y botón de cerrar sesión.
- Botón para **mostrar u ocultar** la contraseña.
- Diseño **adaptable**: en tablet el texto va a la izquierda y la tarjeta a la derecha; en móvil se apilan.
- **Modo claro y oscuro**, con un fondo distinto para cada uno.
- Pantalla completa (edge-to-edge) y el teclado no tapa el formulario.
- Navegación entre pantallas con un único estado, sin librerías extra.

## Datos de prueba

| Campo | Valor |
|---|---|
| Correo | `alejandro@gmail.com` |
| Contraseña | `1234` |

## Estructura

```
app/src/main/java/com/Nicolas/login_compose/
├── MainActivity.kt        # Decide qué pantalla se muestra
└── ui/
    ├── LoginScreen.kt     # Pantalla de login y fondo
    ├── WelcomeScreen.kt   # Pantalla de bienvenida y fuegos artificiales
    ├── LogoCircular.kt    # Logo reutilizable (círculo + icono)
    └── theme/             # Colores, tipografía y tema
```

## Requisitos

- Android Studio (versión reciente)
- JDK 11 o superior
- Android 8.0 (API 26) o superior

## Cómo ejecutarlo

1. Clona el repositorio:
   ```bash
   git clone https://github.com/alejandronicolasdelalamo/Login_compose.git
   ```
2. Ábrelo con Android Studio y espera a que termine la sincronización de Gradle.
3. Pulsa **Run** en un emulador o dispositivo, o desde la terminal:
   ```bash
   ./gradlew installDebug
   ```

## Tecnologías

Kotlin · Jetpack Compose · Material 3 · Gradle (Kotlin DSL)
