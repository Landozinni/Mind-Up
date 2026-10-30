package com.mindup.mindup.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.MaterialTheme

@Composable
fun MindUpBackgroundBrush(): Brush = Brush.verticalGradient(
    listOf(
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.surface
    )
)
