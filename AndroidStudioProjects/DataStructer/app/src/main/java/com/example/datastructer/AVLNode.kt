package com.example.datastructer

/**
 * AVL Tree Node sınıfı
 * 
 * AVL Tree, self-balancing binary search tree'dir.
 * Her düğüm bir height (yükseklik) değeri tutar.
 * 
 * @param key Düğümün değeri (Int)
 * @param left Sol çocuk düğüm
 * @param right Sağ çocuk düğüm
 * @param parent Ebeveyn düğüm
 */
data class AVLNode(
    var key: Int,
    var left: AVLNode? = null,
    var right: AVLNode? = null,
    var parent: AVLNode? = null,
    var height: Int = 1
) {
    /**
     * Düğümün görselleştirme için x koordinatı
     */
    var x: Float = 0f
    
    /**
     * Düğümün görselleştirme için y koordinatı
     */
    var y: Float = 0f
    
    /**
     * Balance factor hesaplar (sağ - sol yükseklik)
     */
    fun getBalanceFactor(): Int {
        val leftHeight = left?.height ?: 0
        val rightHeight = right?.height ?: 0
        return rightHeight - leftHeight
    }
    
    /**
     * Yüksekliği günceller
     */
    fun updateHeight() {
        val leftHeight = left?.height ?: 0
        val rightHeight = right?.height ?: 0
        height = maxOf(leftHeight, rightHeight) + 1
    }
}




