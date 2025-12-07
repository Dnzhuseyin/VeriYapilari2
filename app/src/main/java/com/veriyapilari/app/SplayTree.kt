package com.veriyapilari.app

/**
 * Splay Tree veri yapısı
 * Self-adjusting binary search tree
 */
class SplayTree {
    var root: SplayNode? = null
        private set
    
    fun insert(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Splay Tree'ye ekleniyor: $key")
        root = insertHelper(root, key, steps)
        root = splay(root, key, steps)
        return steps
    }
    
    private fun insertHelper(node: SplayNode?, key: Int, steps: MutableList<String>): SplayNode? {
        if (node == null) {
            steps.add("Yeni düğüm: $key")
            return SplayNode(key)
        }
        if (key < node.key) {
            node.left = insertHelper(node.left, key, steps)
            node.left?.parent = node
        } else if (key > node.key) {
            node.right = insertHelper(node.right, key, steps)
            node.right?.parent = node
        }
        return node
    }
    
    fun delete(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Splay Tree'den siliniyor: $key")
        root = splay(root, key, steps)
        if (root?.key != key) {
            steps.add("Düğüm bulunamadı")
            return steps
        }
        val left = root?.left
        val right = root?.right
        if (left == null) {
            root = right
        } else {
            left.parent = null
            root = splay(left, key, steps)
            root?.right = right
            right?.parent = root
        }
        return steps
    }
    
    private fun splay(node: SplayNode?, key: Int, steps: MutableList<String>): SplayNode? {
        if (node == null) return null
        if (node.key == key) return node
        
        if (key < node.key) {
            if (node.left == null) return node
            if (key < node.left!!.key) {
                node.left!!.left = splay(node.left!!.left, key, steps)
                node.left = rightRotate(node.left!!, steps)
            } else if (key > node.left!!.key) {
                node.left!!.right = splay(node.left!!.right, key, steps)
                if (node.left!!.right != null) {
                    node.left = leftRotate(node.left!!, steps)
                }
            }
            return if (node.left == null) node else rightRotate(node, steps)
        } else {
            if (node.right == null) return node
            if (key > node.right!!.key) {
                node.right!!.right = splay(node.right!!.right, key, steps)
                node.right = leftRotate(node.right!!, steps)
            } else if (key < node.right!!.key) {
                node.right!!.left = splay(node.right!!.left, key, steps)
                if (node.right!!.left != null) {
                    node.right = rightRotate(node.right!!, steps)
                }
            }
            return if (node.right == null) node else leftRotate(node, steps)
        }
    }
    
    private fun rightRotate(x: SplayNode, steps: MutableList<String>): SplayNode {
        steps.add("Right rotation: ${x.key}")
        val y = x.left!!
        x.left = y.right
        y.right = x
        return y
    }
    
    private fun leftRotate(x: SplayNode, steps: MutableList<String>): SplayNode {
        steps.add("Left rotation: ${x.key}")
        val y = x.right!!
        x.right = y.left
        y.left = x
        return y
    }
    
    fun clear() {
        root = null
    }
}




