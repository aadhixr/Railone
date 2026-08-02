package app.railonexr.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF005AC1),
    secondary = Color(0xFF535F70),
    tertiary = Color(0xFF6B5778)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF005AC1),
    secondary = Color(0xFF535F70),
    tertiary = Color(0xFF6B5778),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
)

@Composable
fun RailOneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
