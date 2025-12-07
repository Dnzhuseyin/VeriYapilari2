package com.veriyapilari.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Huffman Coding görselleştirme ViewModel
 */
class HuffmanViewModel : ViewModel() {

    private val huffman = HuffmanCoding()

    var rootNode: HuffmanNode? by mutableStateOf(null)
        private set

    var explanations: List<String> by mutableStateOf(emptyList())
        private set

    var encodedText: String by mutableStateOf("")
        private set

    var decodedText: String by mutableStateOf("")
        private set

    var huffmanCodes: Map<Char, String> by mutableStateOf(emptyMap())
        private set

    var frequencyMap: Map<Char, Int> by mutableStateOf(emptyMap())
        private set

    var zoomLevel: Float by mutableStateOf(1.0f)
        private set

    var offsetX: Float by mutableStateOf(0f)
        private set

    var offsetY: Float by mutableStateOf(0f)
        private set

    /**
     * Metni analiz edip Huffman ağacı oluşturur
     */
    fun buildTree(text: String) {
        val steps = huffman.buildTree(text)
        calculateCoordinates()
        rootNode = huffman.getRoot()
        explanations = steps
        huffmanCodes = huffman.getCodes()
        encodedText = if (text.isNotEmpty()) huffman.encode(text) else ""
        decodedText = ""

        // Frekans haritasını hesapla
        val freqMap = mutableMapOf<Char, Int>()
        for (char in text) {
            freqMap[char] = freqMap.getOrDefault(char, 0) + 1
        }
        frequencyMap = freqMap
    }

    /**
     * Kodlanmış metni çözer
     */
    fun decode() {
        decodedText = huffman.decode(encodedText)
    }

    /**
     * Ağaç koordinatlarını hesaplar
     */
    private fun calculateCoordinates() {
        val root = huffman.getRoot() ?: return
        val canvasWidth = 1200f
        calculateTreeLayout(root, canvasWidth / 2, 100f, canvasWidth / 2, 0)
    }

    private fun calculateTreeLayout(
        node: HuffmanNode?,
        x: Float,
        y: Float,
        levelWidth: Float,
        level: Int
    ) {
        if (node == null) return

        node.x = x
        node.y = y

        val nextLevelWidth = levelWidth / 2

        node.left?.let {
            calculateTreeLayout(
                it,
                x - nextLevelWidth / 2,
                y + 200f,
                nextLevelWidth,
                level + 1
            )
        }

        node.right?.let {
            calculateTreeLayout(
                it,
                x + nextLevelWidth / 2,
                y + 200f,
                nextLevelWidth,
                level + 1
            )
        }
    }

    fun zoomIn() {
        zoomLevel = (zoomLevel * 1.2f).coerceAtMost(3.0f)
    }

    fun zoomOut() {
        zoomLevel = (zoomLevel / 1.2f).coerceAtLeast(0.5f)
    }

    fun resetZoom() {
        zoomLevel = 1.0f
        offsetX = 0f
        offsetY = 0f
    }

    fun updateOffset(deltaX: Float, deltaY: Float) {
        offsetX += deltaX
        offsetY += deltaY
    }

    fun clear() {
        rootNode = null
        explanations = emptyList()
        encodedText = ""
        decodedText = ""
        huffmanCodes = emptyMap()
        frequencyMap = emptyMap()
        resetZoom()
    }
}
