package com.example.datastructer

/**
 * Red-Black Tree Node sınıfı
 * 
 * Her düğüm bir key (değer), color (renk), left (sol çocuk), 
 * right (sağ çocuk) ve parent (ebeveyn) referanslarını tutar.
 * 
 * @param key Düğümün değeri (Int)
 * @param color Düğümün rengi (true = kırmızı, false = siyah)
 * @param left Sol çocuk düğüm
 * @param right Sağ çocuk düğüm
 * @param parent Ebeveyn düğüm
 */
data class RBNode(
    var key: Int,
    var color: Boolean = true, // true = RED, false = BLACK
    var left: RBNode? = null,
    var right: RBNode? = null,
    var parent: RBNode? = null
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
     * Düğümün kırmızı olup olmadığını kontrol eder
     */
    fun isRed(): Boolean = color
    
    /**
     * Düğümün siyah olup olmadığını kontrol eder
     */
    fun isBlack(): Boolean = !color
    
    /**
     * Düğümü kırmızı yapar
     */
    fun setRed() {
        color = true
    }
    
    /**
     * Düğümü siyah yapar
     */
    fun setBlack() {
        color = false
    }
    
    /**
     * Düğümün amcasını (parent'ın kardeşi) bulur
     */
    fun getUncle(): RBNode? {
        val parent = this.parent ?: return null
        val grandparent = parent.parent ?: return null
        
        return if (parent == grandparent.left) {
            grandparent.right
        } else {
            grandparent.left
        }
    }
    
    /**
     * Düğümün kardeşini bulur
     */
    fun getSibling(): RBNode? {
        val parent = this.parent ?: return null
        return if (this == parent.left) {
            parent.right
        } else {
            parent.left
        }
    }
    
    /**
     * Düğümün sol çocuğu var mı kontrol eder
     */
    fun hasLeftChild(): Boolean = left != null
    
    /**
     * Düğümün sağ çocuğu var mı kontrol eder
     */
    fun hasRightChild(): Boolean = right != null
    
    /**
     * Düğümün çocuğu var mı kontrol eder
     */
    fun hasChildren(): Boolean = hasLeftChild() || hasRightChild()
    
    /**
     * Düğümün yaprak (leaf) olup olmadığını kontrol eder
     */
    fun isLeaf(): Boolean = !hasChildren()
}




