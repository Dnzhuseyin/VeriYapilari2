package com.veriyapilari.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun HeapCanvas(
    rootNode: HeapNode?,
    zoomLevel: Float,
    offsetX: Float,
    offsetY: Float,
    onOffsetChange: (Float, Float) -> Unit,
    modifier: Modifier = Modifier,
    key: Int = 0 // Trigger recomposition when heap changes
) {
    val textMeasurer = rememberTextMeasurer()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    val onTertiaryColor = MaterialTheme.colorScheme.onTertiary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    onOffsetChange(dragAmount.x / zoomLevel, dragAmount.y / zoomLevel)
                }
            }
    ) {
        if (rootNode == null) {
            Text(
                text = "Heap is empty. Add a value to start.",
                style = MaterialTheme.typography.bodyLarge,
                color = onSurfaceColor,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Canvas(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                drawEdges(rootNode, centerX + offsetX, centerY + offsetY, zoomLevel)
                drawNodes(rootNode, centerX + offsetX, centerY + offsetY, zoomLevel, textMeasurer, tertiaryColor, onTertiaryColor)
            }
        }
    }
}

private fun DrawScope.drawEdges(
    node: HeapNode,
    offsetX: Float,
    offsetY: Float,
    zoom: Float
) {
    val nodeX = node.x * zoom + offsetX
    val nodeY = node.y * zoom + offsetY
    val nodeRadius = 30f

    node.left?.let { leftChild ->
        val childX = leftChild.x * zoom + offsetX
        val childY = leftChild.y * zoom + offsetY
        drawLine(
            color = Color.Gray,
            start = Offset(nodeX, nodeY + nodeRadius * zoom),
            end = Offset(childX, childY - nodeRadius * zoom),
            strokeWidth = 2f * zoom
        )
        drawEdges(leftChild, offsetX, offsetY, zoom)
    }
    node.right?.let { rightChild ->
        val childX = rightChild.x * zoom + offsetX
        val childY = rightChild.y * zoom + offsetY
        drawLine(
            color = Color.Gray,
            start = Offset(nodeX, nodeY + nodeRadius * zoom),
            end = Offset(childX, childY - nodeRadius * zoom),
            strokeWidth = 2f * zoom
        )
        drawEdges(rightChild, offsetX, offsetY, zoom)
    }
}

private fun DrawScope.drawNodes(
    node: HeapNode,
    offsetX: Float,
    offsetY: Float,
    zoom: Float,
    textMeasurer: TextMeasurer,
    tertiaryColor: Color,
    onTertiaryColor: Color
) {
    val nodeX = node.x * zoom + offsetX
    val nodeY = node.y * zoom + offsetY
    val nodeRadius = 30f

    drawCircle(
        color = tertiaryColor,
        radius = nodeRadius * zoom,
        center = Offset(nodeX, nodeY)
    )
    val textLayoutResult = textMeasurer.measure(node.key.toString())
    drawText(
        textLayoutResult,
        color = onTertiaryColor,
        topLeft = Offset(
            nodeX - textLayoutResult.size.width / 2,
            nodeY - textLayoutResult.size.height / 2
        )
    )

    node.left?.let { drawNodes(it, offsetX, offsetY, zoom, textMeasurer, tertiaryColor, onTertiaryColor) }
    node.right?.let { drawNodes(it, offsetX, offsetY, zoom, textMeasurer, tertiaryColor, onTertiaryColor) }
}
