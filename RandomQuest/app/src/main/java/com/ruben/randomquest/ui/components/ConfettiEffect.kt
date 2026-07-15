package com.ruben.randomquest.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val color: Color,
    val size: Float,
    val speed: Float,
    val angle: Double
)

@Composable
fun ConfettiEffect(modifier: Modifier = Modifier) {
    val particles = remember {
        List(100) {
            Particle(
                x = Random.nextFloat(),
                y = -0.1f,
                color = Color(
                    Random.nextInt(256),
                    Random.nextInt(256),
                    Random.nextInt(256)
                ),
                size = Random.nextFloat() * 10f + 5f,
                speed = Random.nextFloat() * 0.02f + 0.01f,
                angle = Math.toRadians(Random.nextDouble(45.0, 135.0))
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = durationBasedAnimationSpec(),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        particles.forEach { particle ->
            val currentY = (particle.y + progress * particle.speed * 100) % 1.2f
            if (currentY < 1.1f) {
                drawCircle(
                    color = particle.color,
                    radius = particle.size,
                    center = Offset(
                        x = particle.x * width,
                        y = currentY * height
                    )
                )
            }
        }
    }
}

private fun durationBasedAnimationSpec(): DurationBasedAnimationSpec<Float> {
    return tween(durationMillis = 3000, easing = LinearEasing)
}
