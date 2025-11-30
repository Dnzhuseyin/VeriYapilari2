package com.example.datastructer

/**
 * Heap Tree Node sınıfı
 * 
 * Her düğüm bir key (değer), left (sol çocuk), 
 * right (sağ çocuk) ve parent (ebeveyn) referanslarını tutar.
 * 
 * @param key Düğümün değeri (Int)
 * @param left Sol çocuk düğüm
 * @param right Sağ çocuk düğüm
 * @param parent Ebeveyn düğüm
 */
data class HeapNode(
    var key: Int,
    var left: HeapNode? = null,
    var right: HeapNode? = null,
    var parent: HeapNode? = null
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

    /**
     * Override equals to prevent StackOverflow from circular references
     * Only compares the key value, ignoring parent/child references
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HeapNode) return false
        return key == other.key
    }

    /**
     * Override hashCode to match equals implementation
     */
    override fun hashCode(): Int {
        return key.hashCode()
    }
}




