package com.ruben.randomquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    padding: androidx.compose.ui.unit.Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.surface == Color(0xFF1E293B) // BackgroundDark check
    
    val backgroundColor = if (isDark) {
        Color.White.copy(alpha = 0.1f)
    } else {
        Color.White.copy(alpha = 0.4f)
    }
    
    val borderColor = Color.White.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(padding)
    ) {
        content()
    }
}
