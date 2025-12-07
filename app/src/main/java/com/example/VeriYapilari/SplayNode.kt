package com.veriyapilari.app

/**
 * Splay Tree Node sınıfı
 */
data class SplayNode(
    var key: Int,
    var left: SplayNode? = null,
    var right: SplayNode? = null,
    var parent: SplayNode? = null
) {
    var x: Float = 0f
    var y: Float = 0f
}




