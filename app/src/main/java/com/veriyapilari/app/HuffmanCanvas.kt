package com.veriyapilari.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp

@Composable
fun HuffmanCanvas(
    rootNode: HuffmanNode?,
    zoomLevel: Float,
    offsetX: Float,
    offsetY: Float,
    onDrag: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val surfaceColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(surfaceColor)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            }
    ) {
        if (rootNode == null) {
            Text(
                text = "Huffman ağacını görmek için metin girin",
                style = MaterialTheme.typography.bodyLarge,
                color = onSurfaceColor,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                clipRect {
                    drawHuffmanTree(
                        node = rootNode,
                        x = rootNode.x * zoomLevel + size.width / 2 + offsetX,
                        y = rootNode.y * zoomLevel + offsetY + 100f,
                        zoom = zoomLevel,
                        textMeasurer = textMeasurer,
                        primaryColor = primaryColor
                    )
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHuffmanTree(
    node: HuffmanNode,
    x: Float,
    y: Float,
    zoom: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer,
    primaryColor: Color
) {
    val radius = 60f * zoom
    val nodeColor = if (node.isLeaf()) {
        Color(0xFF10B981) // Yeşil - yaprak düğüm
    } else {
        Color(0xFF8B5CF6) // Mor - iç düğüm
    }

    // Çocuklara çizgileri çiz
    node.left?.let { left ->
        val childX = left.x * zoom + x - node.x * zoom
        val childY = left.y * zoom + y - node.y * zoom

        // Sol çizgi (mavi)
        drawLine(
            color = Color(0xFF3B82F6),
            start = Offset(x, y + radius),
            end = Offset(childX, childY - radius),
            strokeWidth = 4f * zoom
        )

        // "0" etiketi
        val labelLayout = textMeasurer.measure(
            "0",
            style = TextStyle(
                fontSize = (18 * zoom).sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF3B82F6)
            )
        )
        drawText(
            labelLayout,
            topLeft = Offset(
                (x + childX) / 2 - labelLayout.size.width / 2 - 15f * zoom,
                (y + childY) / 2 - labelLayout.size.height / 2
            )
        )

        drawHuffmanTree(left, childX, childY, zoom, textMeasurer, primaryColor)
    }

    node.right?.let { right ->
        val childX = right.x * zoom + x - node.x * zoom
        val childY = right.y * zoom + y - node.y * zoom

        // Sağ çizgi (kırmızı)
        drawLine(
            color = Color(0xFFEF4444),
            start = Offset(x, y + radius),
            end = Offset(childX, childY - radius),
            strokeWidth = 4f * zoom
        )

        // "1" etiketi
        val labelLayout = textMeasurer.measure(
            "1",
            style = TextStyle(
                fontSize = (18 * zoom).sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFEF4444)
            )
        )
        drawText(
            labelLayout,
            topLeft = Offset(
                (x + childX) / 2 - labelLayout.size.width / 2 + 15f * zoom,
                (y + childY) / 2 - labelLayout.size.height / 2
            )
        )

        drawHuffmanTree(right, childX, childY, zoom, textMeasurer, primaryColor)
    }

    // Düğümü çiz - shadow
    drawCircle(
        color = nodeColor.copy(alpha = 0.3f),
        radius = radius + 3f * zoom,
        center = Offset(x, y)
    )

    // Ana daire
    drawCircle(
        color = nodeColor,
        radius = radius,
        center = Offset(x, y)
    )

    // Border
    drawCircle(
        color = Color.White,
        radius = radius,
        center = Offset(x, y),
        style = Stroke(width = 3f * zoom)
    )

    // Düğüm içinde karakter (yaprak) veya frekans (iç düğüm)
    if (node.isLeaf() && node.char != null) {
        // Yaprak düğüm - sadece karakter göster
        val charLayout = textMeasurer.measure(
            "'${node.char}'",
            style = TextStyle(fontSize = (24 * zoom).sp, fontWeight = FontWeight.Bold, color = Color.White)
        )
        drawText(
            charLayout,
            topLeft = Offset(
                x - charLayout.size.width / 2,
                y - charLayout.size.height / 2
            )
        )
    } else {
        // İç düğüm - frekans göster
        val freqLayout = textMeasurer.measure(
            node.frequency.toString(),
            style = TextStyle(fontSize = (20 * zoom).sp, fontWeight = FontWeight.Bold, color = Color.White)
        )
        drawText(
            freqLayout,
            topLeft = Offset(
                x - freqLayout.size.width / 2,
                y - freqLayout.size.height / 2
            )
        )
    }
}
