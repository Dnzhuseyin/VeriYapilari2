package com.example.datastructer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Red-Black Tree görselleştirme uygulaması için ViewModel
 * 
 * MVVM mimarisine uygun olarak, UI'dan bağımsız iş mantığını yönetir.
 * Red-Black Tree işlemlerini ve görselleştirme durumunu yönetir.
 */
class TreeViewModel : ViewModel() {
    
    /**
     * Red-Black Tree veri yapısı
     */
    private val tree = RedBlackTree()
    
    /**
     * Ağacın kök düğümü (UI için)
     */
    var rootNode: RBNode? by mutableStateOf(null)
        private set

    /**
     * Görselleştirme versiyonu - her değişiklikte artar, recomposition tetikler
     */
    var visualizationVersion by mutableStateOf(0)
        private set

    /**
     * Adım adım açıklamalar listesi
     */
    var explanations: List<String> by mutableStateOf(emptyList())
        private set
    
    /**
     * Zoom seviyesi (1.0 = normal)
     */
    var zoomLevel: Float by mutableStateOf(1.0f)
        private set
    
    /**
     * Canvas offset X (pan için)
     */
    var offsetX: Float by mutableStateOf(0f)
        private set
    
    /**
     * Canvas offset Y (pan için)
     */
    var offsetY: Float by mutableStateOf(0f)
        private set
    
    /**
     * Ağaca yeni bir değer ekler
     *
     * @param key Eklenecek değer
     */
    fun insert(key: Int) {
        // Ekleme işlemini yap ve adımları al
        val steps = tree.insert(key)

        // Koordinatları hesapla
        calculateCoordinates()

        // Kök düğümü güncelle
        rootNode = tree.root

        // Açıklamaları güncelle
        explanations = steps

        // Görselleştirme versiyonunu artır - recomposition tetikler
        visualizationVersion++
    }
    
    /**
     * Ağaçtan bir değer siler
     *
     * @param key Silinecek değer
     */
    fun delete(key: Int) {
        // Silme işlemini yap ve adımları al
        val steps = tree.delete(key)

        // Koordinatları hesapla
        calculateCoordinates()

        // Kök düğümü güncelle
        rootNode = tree.root

        // Açıklamaları güncelle
        explanations = steps

        // Görselleştirme versiyonunu artır - recomposition tetikler
        visualizationVersion++
    }
    
    /**
     * Zoom seviyesini artırır
     */
    fun zoomIn() {
        zoomLevel = (zoomLevel * 1.2f).coerceAtMost(3.0f)
    }
    
    /**
     * Zoom seviyesini azaltır
     */
    fun zoomOut() {
        zoomLevel = (zoomLevel / 1.2f).coerceAtLeast(0.5f)
    }
    
    /**
     * Zoom seviyesini sıfırlar
     */
    fun resetZoom() {
        zoomLevel = 1.0f
        offsetX = 0f
        offsetY = 0f
    }
    
    /**
     * Canvas offset'ini günceller (pan için)
     * 
     * @param deltaX X ekseni değişimi
     * @param deltaY Y ekseni değişimi
     */
    fun updateOffset(deltaX: Float, deltaY: Float) {
        offsetX += deltaX
        offsetY += deltaY
    }
    
    /**
     * Tüm düğümlerin koordinatlarını hesaplar
     * Ağaç görselleştirmesi için gerekli x, y pozisyonlarını belirler
     */
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
    
    private fun findTreeBounds(node: RBNode?): Pair<Float, Float> {
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
    
    private fun applyOffset(node: RBNode?, offset: Float) {
        if (node == null) return
        node.x += offset
        applyOffset(node.left, offset)
        applyOffset(node.right, offset)
    }
    
    /**
     * Ağaç düğümlerinin koordinatlarını recursive olarak hesaplar
     * 
     * @param node Düğüm
     * @param x X pozisyonu
     * @param y Y pozisyonu
     * @param levelWidth Seviye genişliği
     * @param level Mevcut seviye
     */
    private fun calculateTreeLayout(
        node: RBNode?,
        x: Float,
        y: Float,
        levelWidth: Float,
        level: Int
    ) {
        if (node == null) return
        
        // Mevcut düğümün pozisyonunu ayarla
        node.x = x
        node.y = y
        
        // Alt seviye genişliği (her seviyede yarıya iner)
        val nextLevelWidth = levelWidth / 2
        
        // Sol çocuk pozisyonu (merkezin solunda)
        node.left?.let {
            calculateTreeLayout(
                it,
                x - nextLevelWidth / 2,
                y + 120f, // Dikey mesafe
                nextLevelWidth,
                level + 1
            )
        }
        
        // Sağ çocuk pozisyonu (merkezin sağında)
        node.right?.let {
            calculateTreeLayout(
                it,
                x + nextLevelWidth / 2,
                y + 120f, // Dikey mesafe
                nextLevelWidth,
                level + 1
            )
        }
    }
    
    /**
     * Ağacı temizler
     */
    fun clearTree() {
        tree.clear()
        rootNode = null
        explanations = emptyList()
        resetZoom()
    }
}

