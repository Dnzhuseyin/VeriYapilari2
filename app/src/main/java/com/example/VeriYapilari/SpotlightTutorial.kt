package com.veriyapilari.app

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.max
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius

data class SpotlightTarget(
    val elementId: String,
    val title: String,
    val description: String,
    val calloutPosition: HighlightPosition = HighlightPosition.BOTTOM_CENTER
)

/**
 * Enhanced tutorial component that highlights specific UI elements with callout bubbles
 */
@Composable
fun SpotlightTutorial(
    targets: List<SpotlightTarget>,
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    targetPositions: Map<String, Rect> = emptyMap()
) {
    var currentStep by remember { mutableStateOf(0) }
    val target = targets.getOrNull(currentStep)

    // Animasyon için
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    if (target != null) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val targetRect = target.elementId?.let { targetPositions[it] }

                // Semi-transparent overlay with spotlight
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Dark overlay
                    val darkPath = Path().apply {
                        addRect(Rect(Offset.Zero, size))
                    }

                    // Create spotlight cutout
                    targetRect?.let { rect ->
                        val expandedRect = Rect(
                            left = rect.left - 8.dp.toPx(),
                            top = rect.top - 8.dp.toPx(),
                            right = rect.right + 8.dp.toPx(),
                            bottom = rect.bottom + 8.dp.toPx()
                        )

                        val spotlightPath = Path().apply {
                            addRoundRect(
                                RoundRect(
                                    rect = expandedRect,
                                    cornerRadius = CornerRadius(12.dp.toPx())
                                )
                            )
                        }

                        // Subtract spotlight from dark overlay
                        darkPath.op(darkPath, spotlightPath, PathOperation.Difference)

                        // Draw pulsing border around spotlight
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.8f),
                            topLeft = expandedRect.topLeft,
                            size = Size(expandedRect.width, expandedRect.height),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                            style = Stroke(width = 3.dp.toPx() * pulseScale)
                        )

                        // Draw glow effect
                        drawRoundRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.3f),
                                    Color.Transparent
                                ),
                                center = expandedRect.center,
                                radius = max(expandedRect.width, expandedRect.height) * 0.8f
                            ),
                            topLeft = Offset(
                                expandedRect.left - expandedRect.width * 0.3f,
                                expandedRect.top - expandedRect.height * 0.3f
                            ),
                            size = Size(
                                expandedRect.width * 1.6f,
                                expandedRect.height * 1.6f
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                        )
                    }

                    drawPath(
                        path = darkPath,
                        color = Color.Black.copy(alpha = 0.85f)
                    )
                }

                // Callout bubble - show only if we have a valid target position
                if (targetRect != null) {
                    CalloutBubble(
                        title = target.title,
                        description = target.description,
                        targetRect = targetRect,
                        position = target.calloutPosition,
                        currentStep = currentStep + 1,
                        totalSteps = targets.size,
                        onNext = {
                            if (currentStep < targets.size - 1) {
                                currentStep++
                            } else {
                                onComplete()
                            }
                        },
                        onBack = if (currentStep > 0) {
                            { currentStep-- }
                        } else null,
                        onSkip = onSkip,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // If no target rect, show centered message
                    CenteredCallout(
                        title = target.title,
                        description = target.description,
                        currentStep = currentStep + 1,
                        totalSteps = targets.size,
                        onNext = {
                            if (currentStep < targets.size - 1) {
                                currentStep++
                            } else {
                                onComplete()
                            }
                        },
                        onBack = if (currentStep > 0) {
                            { currentStep-- }
                        } else null,
                        onSkip = onSkip
                    )
                }
            }
        }
    }
}

@Composable
private fun CalloutBubble(
    title: String,
    description: String,
    targetRect: Rect,
    position: HighlightPosition,
    currentStep: Int,
    totalSteps: Int,
    onNext: () -> Unit,
    onBack: (() -> Unit)?,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        // Get screen dimensions
        val screenWidth = with(density) { maxWidth.toPx() }
        val screenHeight = with(density) { maxHeight.toPx() }
        val calloutWidth = with(density) { 300.dp.toPx() }
        val calloutHeightEstimate = with(density) { 250.dp.toPx() } // Tahmini yükseklik
        val padding = with(density) { 16.dp.toPx() }

        // Calculate callout position based on target and position preference
        val calloutModifier = when (position) {
            HighlightPosition.TOP_CENTER -> {
                val x = ((screenWidth - calloutWidth) / 2f).coerceIn(padding, screenWidth - calloutWidth - padding)
                val y = (targetRect.top - calloutHeightEstimate - with(density) { 24.dp.toPx() }).coerceIn(padding, screenHeight - calloutHeightEstimate - padding)

                Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(density) { x.toDp() },
                        y = with(density) { y.toDp() }.coerceAtLeast(16.dp)
                    )
            }

            HighlightPosition.BOTTOM_CENTER -> {
                val centerX = targetRect.left + targetRect.width / 2f - calloutWidth / 2f
                val x = centerX.coerceIn(padding, screenWidth - calloutWidth - padding)
                val y = (targetRect.bottom + with(density) { 24.dp.toPx() }).coerceAtMost(screenHeight - calloutHeightEstimate - padding)

                Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(density) { x.toDp() },
                        y = with(density) { y.toDp() }
                    )
            }

            HighlightPosition.CENTER_RIGHT -> {
                val x = (targetRect.right + with(density) { 16.dp.toPx() }).coerceAtMost(screenWidth - calloutWidth - padding)
                val y = targetRect.top.coerceIn(padding, screenHeight - calloutHeightEstimate - padding)

                Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(density) { x.toDp() },
                        y = with(density) { y.toDp() }
                    )
            }

            HighlightPosition.CENTER_LEFT -> {
                val x = (targetRect.left - calloutWidth - with(density) { 16.dp.toPx() }).coerceAtLeast(padding)
                val y = targetRect.top.coerceIn(padding, screenHeight - calloutHeightEstimate - padding)

                Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(density) { x.toDp() },
                        y = with(density) { y.toDp() }
                    )
            }

            HighlightPosition.BOTTOM_LEFT -> {
                val x = targetRect.left.coerceIn(padding, screenWidth - calloutWidth - padding)
                val y = (targetRect.bottom + with(density) { 24.dp.toPx() }).coerceAtMost(screenHeight - calloutHeightEstimate - padding)

                Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = with(density) { x.toDp() },
                        y = with(density) { y.toDp() }
                    )
            }

            HighlightPosition.CENTER -> Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)

            else -> Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
        }

        Card(
            modifier = calloutModifier
                .width(300.dp)
                .shadow(24.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$currentStep/$totalSteps",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onSkip, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Geç",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Progress
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )

                // Content
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (onBack != null) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Geri")
                        }
                    }

                    Button(
                        onClick = onNext,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (currentStep < totalSteps) "Sonraki" else "Başla")
                        if (currentStep < totalSteps) {
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Arrow pointing to target
        Canvas(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = with(density) { targetRect.center.x.toDp() },
                    y = with(density) { targetRect.center.y.toDp() }
                )
                .size(24.dp)
        ) {
            drawCircle(
                color = Color.White,
                radius = 8.dp.toPx(),
                style = Stroke(width = 3.dp.toPx())
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = 12.dp.toPx()
            )
        }
    }
}

@Composable
private fun CenteredCallout(
    title: String,
    description: String,
    currentStep: Int,
    totalSteps: Int,
    onNext: () -> Unit,
    onBack: (() -> Unit)?,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$currentStep/$totalSteps",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onSkip) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Geç",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (onBack != null) {
                        OutlinedButton(
                            onClick = onBack,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Geri")
                        }
                    }

                    Button(
                        onClick = onNext,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (currentStep < totalSteps) "Sonraki" else "Başla")
                        if (currentStep < totalSteps) {
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Modifier extension to track element position for spotlight
@Composable
fun Modifier.tutorialTarget(
    id: String,
    onPositionChanged: (String, Rect) -> Unit
): Modifier {
    val density = LocalDensity.current
    return this.onGloballyPositioned { coordinates ->
        val position = coordinates.positionInRoot()
        val size = coordinates.size
        val rect = Rect(
            left = position.x,
            top = position.y,
            right = position.x + size.width,
            bottom = position.y + size.height
        )
        onPositionChanged(id, rect)
    }
}
