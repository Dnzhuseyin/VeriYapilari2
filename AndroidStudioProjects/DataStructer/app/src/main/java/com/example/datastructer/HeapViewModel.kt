package com.example.datastructer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Max Heap görselleştirme uygulaması için ViewModel
 * 
 * MVVM mimarisine uygun olarak, UI'dan bağımsız iş mantığını yönetir.
 * Max Heap işlemlerini ve görselleştirme durumunu yönetir.
 */
class HeapViewModel : ViewModel() {
    
    /**
     * Max Heap veri yapısı
     */
    private val heap = MaxHeap()
    
    /**
     * Ağacın kök düğümü (UI için)
     */
    var rootNode: HeapNode? by mutableStateOf(null)
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
     * Heap'e yeni bir değer ekler
     *
     * @param key Eklenecek değer
     */
    fun insert(key: Int) {
        val steps = heap.insert(key)
        calculateCoordinates()
        rootNode = heap.root
        explanations = steps
        visualizationVersion++
    }

    fun extractMax() {
        val steps = heap.extractMax()
        calculateCoordinates()
        rootNode = heap.root
        explanations = steps
        visualizationVersion++
    }

    fun delete(key: Int) {
        val steps = heap.delete(key)
        calculateCoordinates()
        rootNode = heap.root
        explanations = steps
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
     * Heap görselleştirmesi için gerekli x, y pozisyonlarını belirler
     */
    private fun calculateCoordinates() {
        val root = heap.root ?: return
        
        // Canvas genişliği (varsayılan)
        val canvasWidth = 1200f
        
        // Koordinatları hesapla (merkezden başlayarak)
        calculateTreeLayout(root, canvasWidth / 2, 100f, canvasWidth / 2, 0)
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
        node: HeapNode?,
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
     * Heap'i temizler
     */
    fun clearHeap() {
        heap.clear()
        rootNode = null
        explanations = emptyList()
        resetZoom()
    }
}




