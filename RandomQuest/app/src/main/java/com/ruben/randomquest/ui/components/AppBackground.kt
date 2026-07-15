package com.ruben.randomquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.ruben.randomquest.R

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image using Coil for memory management
        AsyncImage(
            model = R.drawable.background,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay for readability
        val isDark = MaterialTheme.colorScheme.surface == Color(0xFF1E293B)
        val overlayColor = if (isDark) {
            Color.Black.copy(alpha = 0.6f)
        } else {
            Color.White.copy(alpha = 0.6f)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
        )

        content()
    }
}
