package com.example.datastructer

/**
 * Huffman ağacı için düğüm sınıfı
 *
 * @param char Karakter (null ise iç düğüm)
 * @param frequency Frekans değeri
 * @param left Sol çocuk
 * @param right Sağ çocuk
 */
data class HuffmanNode(
    val char: Char?,
    val frequency: Int,
    var left: HuffmanNode? = null,
    var right: HuffmanNode? = null
) : Comparable<HuffmanNode> {
    var x: Float = 0f
    var y: Float = 0f

    /**
     * Yaprak düğüm mü kontrol eder
     */
    fun isLeaf(): Boolean = left == null && right == null

    /**
     * Priority Queue için karşılaştırma (frekansa göre)
     */
    override fun compareTo(other: HuffmanNode): Int {
        return this.frequency - other.frequency
    }

    /**
     * StackOverflow'dan kaçınmak için equals override
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HuffmanNode) return false
        return char == other.char && frequency == other.frequency
    }

    override fun hashCode(): Int {
        var result = char?.hashCode() ?: 0
        result = 31 * result + frequency
        return result
    }
}
