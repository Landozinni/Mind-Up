package com.mindup.mindup.ui.theme

import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink40,
    background = androidx.compose.ui.graphics.Color(0xFF121018),
    surface = androidx.compose.ui.graphics.Color(0xFF1E1B24),
    onBackground = androidx.compose.ui.graphics.Color(0xFFE8E0F0),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE8E0F0),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFCAC3D3)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = DarkColor

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MindUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val targetColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val context = LocalContext.current
    val animationsEnabled = remember {
        runCatching {
            Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0f
        }.getOrDefault(true)
    }
    val colorScheme = if (animationsEnabled) {
        rememberAnimatedColorScheme(targetColorScheme)
    } else targetColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
private fun rememberAnimatedColorScheme(target: androidx.compose.material3.ColorScheme): androidx.compose.material3.ColorScheme {
    @Composable
    fun color(value: Color) = animateColorAsState(value, tween(durationMillis = 280), label = "theme-color").value

    return target.copy(
        primary = color(target.primary), onPrimary = color(target.onPrimary), primaryContainer = color(target.primaryContainer), onPrimaryContainer = color(target.onPrimaryContainer),
        secondary = color(target.secondary), onSecondary = color(target.onSecondary), secondaryContainer = color(target.secondaryContainer), onSecondaryContainer = color(target.onSecondaryContainer),
        tertiary = color(target.tertiary), onTertiary = color(target.onTertiary), tertiaryContainer = color(target.tertiaryContainer), onTertiaryContainer = color(target.onTertiaryContainer),
        background = color(target.background), onBackground = color(target.onBackground), surface = color(target.surface), onSurface = color(target.onSurface),
        surfaceBright = color(target.surfaceBright), surfaceDim = color(target.surfaceDim), surfaceContainer = color(target.surfaceContainer),
        surfaceContainerLowest = color(target.surfaceContainerLowest), surfaceContainerLow = color(target.surfaceContainerLow),
        surfaceContainerHigh = color(target.surfaceContainerHigh), surfaceContainerHighest = color(target.surfaceContainerHighest),
        surfaceVariant = color(target.surfaceVariant), onSurfaceVariant = color(target.onSurfaceVariant), surfaceTint = color(target.surfaceTint),
        inverseSurface = color(target.inverseSurface), inverseOnSurface = color(target.inverseOnSurface), inversePrimary = color(target.inversePrimary),
        error = color(target.error), onError = color(target.onError), errorContainer = color(target.errorContainer), onErrorContainer = color(target.onErrorContainer),
        outline = color(target.outline), outlineVariant = color(target.outlineVariant), scrim = color(target.scrim)
    )
}
