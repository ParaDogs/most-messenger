package com.most.messenger.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun MostTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (true) LightColors else DarkColors,
        content = content
    )
}
