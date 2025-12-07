package com.veriyapilari.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AVLViewModel : ViewModel() {
    private val tree = AVLTree()
    var rootNode: AVLNode? by mutableStateOf(null)
        private set
    var visualizationVersion by mutableStateOf(0)
        private set
    var explanations: List<String> by mutableStateOf(emptyList())
        private set
    var zoomLevel: Float by mutableStateOf(1.0f)
        private set
    var offsetX: Float by mutableStateOf(0f)
        private set
    var offsetY: Float by mutableStateOf(0f)
        private set
    
    fun insert(key: Int) {
        val steps = tree.insert(key)
        calculateCoordinates()
        rootNode = tree.root
        explanations = steps
        visualizationVersion++
        resetZoom() // Ağacı ortala
    }

    fun delete(key: Int) {
        val steps = tree.delete(key)
        calculateCoordinates()
        rootNode = tree.root
        explanations = steps
        visualizationVersion++
        resetZoom() // Ağacı ortala
    }
    
    fun zoomIn() { zoomLevel = (zoomLevel * 1.2f).coerceAtMost(3.0f) }
    fun zoomOut() { zoomLevel = (zoomLevel / 1.2f).coerceAtLeast(0.5f) }
    fun resetZoom() { zoomLevel = 1.0f; offsetX = 0f; offsetY = 0f }
    fun updateOffset(deltaX: Float, deltaY: Float) { offsetX += deltaX; offsetY += deltaY }
    
    private fun calculateCoordinates() {
        val root = tree.root ?: return
        // Önce ağacı 0,0 noktasından başlayarak hesapla
        calculateTreeLayout(root, 0f, 100f, 800f, 0)
        
        // Ağacın minimum ve maksimum x değerlerini bul
        val bounds = findTreeBounds(root)
        
        // Ağacı merkeze almak için offset hesapla
        val treeWidth = bounds.second - bounds.first
        val centerOffset = -treeWidth / 2 - bounds.first
        
        // Tüm düğümleri merkeze kaydır
        applyOffset(root, centerOffset)
    }
    
    private fun findTreeBounds(node: AVLNode?): Pair<Float, Float> {
        if (node == null) return Pair(Float.MAX_VALUE, Float.MIN_VALUE)
        val leftBounds = findTreeBounds(node.left)
        val rightBounds = findTreeBounds(node.right)
        val minX = minOf(node.x, leftBounds.first, rightBounds.first)
        val maxX = maxOf(node.x, leftBounds.second, rightBounds.second)
        return Pair(
            if (minX == Float.MAX_VALUE) node.x else minX,
            if (maxX == Float.MIN_VALUE) node.x else maxX
        )
    }
    
    private fun applyOffset(node: AVLNode?, offset: Float) {
        if (node == null) return
        node.x += offset
        applyOffset(node.left, offset)
        applyOffset(node.right, offset)
    }
    
    private fun calculateTreeLayout(node: AVLNode?, x: Float, y: Float, levelWidth: Float, level: Int) {
        if (node == null) return
        node.x = x
        node.y = y
        val nextLevelWidth = levelWidth / 2
        node.left?.let { calculateTreeLayout(it, x - nextLevelWidth / 2, y + 120f, nextLevelWidth, level + 1) }
        node.right?.let { calculateTreeLayout(it, x + nextLevelWidth / 2, y + 120f, nextLevelWidth, level + 1) }
    }
    
    fun clear() { tree.clear(); rootNode = null; explanations = emptyList(); resetZoom() }
}


