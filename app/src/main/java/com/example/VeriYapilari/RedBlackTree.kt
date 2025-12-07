package com.veriyapilari.app

/**
 * Red-Black Tree veri yapısı implementasyonu
 * 
 * Red-Black Tree, self-balancing binary search tree'dir.
 * Her düğüm kırmızı veya siyah renkte olabilir ve ağaç
 * belirli kurallara göre dengelenir.
 * 
 * Özellikler:
 * 1. Her düğüm ya kırmızı ya da siyahtır
 * 2. Kök her zaman siyahtır
 * 3. Her yaprak (null) siyahtır
 * 4. Kırmızı bir düğümün çocukları siyah olmalıdır
 * 5. Her düğümden yapraklara giden yolda aynı sayıda siyah düğüm vardır
 */
class RedBlackTree {
    // Kök düğüm
    var root: RBNode? = null
        private set
    
    // Nil düğüm (yaprak düğümler için)
    private val nil = RBNode(0, false) // Siyah nil düğüm
    
    /**
     * Ağaca yeni bir düğüm ekler
     * 
     * @param key Eklenecek değer
     * @return Ekleme işlemi sırasında yapılan adımların listesi
     */
    fun insert(key: Int): List<String> {
        val steps = mutableListOf<String>()
        
        // Yeni düğüm oluştur (başlangıçta kırmızı)
        val newNode = RBNode(key, true)
        steps.add("Yeni düğüm oluşturuldu: $key (Kırmızı)")
        
        // Normal BST ekleme
        var current = root
        var parent: RBNode? = null
        
        while (current != null) {
            parent = current
            if (key < current.key) {
                current = current.left
            } else if (key > current.key) {
                current = current.right
            } else {
                steps.add("Düğüm zaten mevcut: $key")
                return steps
            }
        }
        
        newNode.parent = parent
        
        if (parent == null) {
            // Ağaç boş, yeni düğüm kök olur
            root = newNode
            steps.add("Ağaç boştu, $key kök düğüm olarak eklendi")
        } else if (key < parent.key) {
            parent.left = newNode
            steps.add("$key, ${parent.key}'nin sol çocuğu olarak eklendi")
        } else {
            parent.right = newNode
            steps.add("$key, ${parent.key}'nin sağ çocuğu olarak eklendi")
        }
        
        // Red-Black Tree düzeltmeleri
        steps.addAll(fixInsert(newNode))
        
        return steps
    }
    
    /**
     * Ekleme sonrası Red-Black Tree özelliklerini düzeltir
     *
     * @param node Eklene düğüm
     * @return Yapılan düzeltme adımları
     */
    private fun fixInsert(node: RBNode): List<String> {
        val steps = mutableListOf<String>()
        var current = node

        // Case 1: Eğer node kök ise, siyah yap
        if (current.parent == null) {
            current.setBlack()
            steps.add("Case 1: Düğüm ${current.key} kök olduğu için Özellik 2'ye göre siyah yapıldı (Kök her zaman siyah olmalıdır)")
            return steps
        }

        // Case 2: Eğer parent siyah ise, hiçbir şey yapma
        if (current.parent!!.isBlack()) {
            steps.add("Case 2: Parent ${current.parent!!.key} zaten siyah, Red-Black özellikleri korunuyor, düzeltme gerekmez")
            return steps
        }

        // Case 3: Parent kırmızı
        while (current.parent != null && current.parent!!.isRed()) {
            val parent = current.parent!!
            val grandparent = parent.parent

            if (grandparent == null) {
                // Parent kök ise, siyah yap
                parent.setBlack()
                steps.add("Case 3: Parent ${parent.key} kök olduğu için Özellik 2'ye göre siyah yapıldı")
                break
            }

            val uncle = current.getUncle()

            // Case 3a: Uncle kırmızı
            if (uncle != null && uncle.isRed()) {
                steps.add("Case 3a: Parent (${parent.key}) ve Current (${current.key}) her ikisi de kırmızı → Özellik 4 ihlali (Kırmızı düğümün kırmızı çocuğu olamaz)")
                steps.add("Uncle (${uncle.key}) kırmızı olduğundan, renk değiştirme yöntemi kullanılıyor")
                parent.setBlack()
                uncle.setBlack()
                grandparent.setRed()
                steps.add("→ Parent (${parent.key}) ve Uncle (${uncle.key}) siyah, Grandparent (${grandparent.key}) kırmızı yapıldı")
                steps.add("→ Siyah yüksekliği korundu, ancak Grandparent'ta ihlal olabilir, kontrol yukarı taşınıyor")
                current = grandparent
            } else {
                // Case 3b: Uncle siyah veya null
                steps.add("Case 3b: Parent (${parent.key}) ve Current (${current.key}) kırmızı ancak Uncle siyah/null → Rotasyon gerekiyor")

                // Case 3b.1: Left-Left case
                if (parent == grandparent.left && current == parent.left) {
                    steps.add("Case 3b.1: Left-Left yapısı tespit edildi (${current.key} ← ${parent.key} ← ${grandparent.key})")
                    steps.add("→ Grandparent (${grandparent.key}) etrafında sağa rotasyon yapılıyor, böylece Parent yukarı çıkıp dengeli yapı oluşuyor")
                    rightRotate(grandparent)
                    parent.setBlack()
                    grandparent.setRed()
                    steps.add("→ Rotasyon sonrası Parent (${parent.key}) siyah, eski Grandparent (${grandparent.key}) kırmızı yapıldı → Özellik 4 ve 5 sağlandı")
                }
                // Case 3b.2: Left-Right case
                else if (parent == grandparent.left && current == parent.right) {
                    steps.add("Case 3b.2: Left-Right yapısı tespit edildi (${parent.key} ← ${grandparent.key}, ${current.key} → ${parent.key})")
                    steps.add("→ Önce Parent (${parent.key}) etrafında sola rotasyon yapılıyor, Left-Left case'e dönüştürülüyor")
                    leftRotate(parent)
                    steps.add("→ Şimdi Grandparent (${grandparent.key}) etrafında sağa rotasyon yapılıyor")
                    rightRotate(grandparent)
                    current.setBlack()
                    grandparent.setRed()
                    steps.add("→ Çift rotasyon sonrası Current (${current.key}) siyah, eski Grandparent (${grandparent.key}) kırmızı yapıldı → Denge sağlandı")
                }
                // Case 3b.3: Right-Right case
                else if (parent == grandparent.right && current == parent.right) {
                    steps.add("Case 3b.3: Right-Right yapısı tespit edildi (${grandparent.key} → ${parent.key} → ${current.key})")
                    steps.add("→ Grandparent (${grandparent.key}) etrafında sola rotasyon yapılıyor, böylece Parent yukarı çıkıp dengeli yapı oluşuyor")
                    leftRotate(grandparent)
                    parent.setBlack()
                    grandparent.setRed()
                    steps.add("→ Rotasyon sonrası Parent (${parent.key}) siyah, eski Grandparent (${grandparent.key}) kırmızı yapıldı → Özellik 4 ve 5 sağlandı")
                }
                // Case 3b.4: Right-Left case
                else if (parent == grandparent.right && current == parent.left) {
                    steps.add("Case 3b.4: Right-Left yapısı tespit edildi (${grandparent.key} → ${parent.key}, ${parent.key} ← ${current.key})")
                    steps.add("→ Önce Parent (${parent.key}) etrafında sağa rotasyon yapılıyor, Right-Right case'e dönüştürülüyor")
                    rightRotate(parent)
                    steps.add("→ Şimdi Grandparent (${grandparent.key}) etrafında sola rotasyon yapılıyor")
                    leftRotate(grandparent)
                    current.setBlack()
                    grandparent.setRed()
                    steps.add("→ Çift rotasyon sonrası Current (${current.key}) siyah, eski Grandparent (${grandparent.key}) kırmızı yapıldı → Denge sağlandı")
                }

                break
            }
        }

        // Kökü siyah yap
        root?.setBlack()
        if (root != null && root!!.isRed()) {
            steps.add("Son kontrol: Kök düğüm ${root!!.key} Özellik 2'ye göre siyah yapıldı (Kök her zaman siyah)")
        }

        return steps
    }
    
    /**
     * Ağaçtan bir düğüm siler
     * 
     * @param key Silinecek değer
     * @return Silme işlemi sırasında yapılan adımların listesi
     */
    fun delete(key: Int): List<String> {
        val steps = mutableListOf<String>()
        steps.add("Silme işlemi başlatıldı: $key")
        
        // Silinecek düğümü bul
        var nodeToDelete = search(key)
        if (nodeToDelete == null) {
            steps.add("Düğüm bulunamadı: $key")
            return steps
        }
        
        steps.add("Düğüm bulundu: $key")
        
        var nodeToFix: RBNode?
        var originalColor = nodeToDelete.color
        
        // Silinecek düğümün çocuk sayısına göre işlem yap
        if (nodeToDelete.left == null) {
            // Sol çocuk yok
            nodeToFix = nodeToDelete.right
            steps.add("Sol çocuk yok, sağ çocuk ile değiştiriliyor")
            transplant(nodeToDelete, nodeToDelete.right)
        } else if (nodeToDelete.right == null) {
            // Sağ çocuk yok
            nodeToFix = nodeToDelete.left
            steps.add("Sağ çocuk yok, sol çocuk ile değiştiriliyor")
            transplant(nodeToDelete, nodeToDelete.left)
        } else {
            // İki çocuk var, successor bul
            val successor = minimum(nodeToDelete.right!!)
            originalColor = successor.color
            nodeToFix = successor.right
            
            if (successor.parent == nodeToDelete) {
                nodeToFix?.parent = successor
            } else {
                transplant(successor, successor.right)
                successor.right = nodeToDelete.right
                successor.right?.parent = successor
            }
            
            transplant(nodeToDelete, successor)
            successor.left = nodeToDelete.left
            successor.left?.parent = successor
            successor.color = nodeToDelete.color
            
            steps.add("Successor bulundu: ${successor.key}, düğüm ile değiştirildi")
        }
        
        // Eğer silinen düğüm siyah ise, düzeltme yap
        if (!originalColor) {
            steps.add("Silinen düğüm siyahtı, düzeltme yapılıyor")
            if (nodeToFix != null) {
                steps.addAll(fixDelete(nodeToFix))
            }
        } else {
            steps.add("Silinen düğüm kırmızıydı, düzeltme gerekmez")
        }
        
        return steps
    }
    
    /**
     * Silme sonrası Red-Black Tree özelliklerini düzeltir
     *
     * @param node Düzeltilecek düğüm
     * @return Yapılan düzeltme adımları
     */
    private fun fixDelete(node: RBNode): List<String> {
        val steps = mutableListOf<String>()
        var current = node

        while (current != root && current.isBlack()) {
            val parent = current.parent ?: break
            val sibling = current.getSibling()

            if (sibling == null) {
                break
            }

            // Case 1: Sibling kırmızı
            if (sibling.isRed()) {
                steps.add("Case 1: Sibling (${sibling.key}) kırmızı → Siyah sibling elde etmek için rotasyon yapılıyor")
                steps.add("→ Sibling siyah ve Parent (${parent.key}) kırmızı yapılıyor")
                sibling.setBlack()
                parent.setRed()

                if (current == parent.left) {
                    steps.add("→ Parent (${parent.key}) etrafında sola rotasyon yapılıyor, böylece sibling'in çocuğu yeni sibling oluyor")
                    leftRotate(parent)
                } else {
                    steps.add("→ Parent (${parent.key}) etrafında sağa rotasyon yapılıyor, böylece sibling'in çocuğu yeni sibling oluyor")
                    rightRotate(parent)
                }

                // Yeni sibling'i güncelle
                val newSibling = current.getSibling()
                if (newSibling != null) {
                    steps.add("→ Yeni sibling: ${newSibling.key} (siyah)")
                }
            } else {
                // Case 2: Sibling siyah ve her iki çocuğu da siyah
                val siblingLeft = sibling.left
                val siblingRight = sibling.right

                if ((siblingLeft == null || siblingLeft.isBlack()) &&
                    (siblingRight == null || siblingRight.isBlack())) {
                    steps.add("Case 2: Sibling (${sibling.key}) siyah ve her iki çocuğu da siyah → Siyah yüksekliği dengelemek için renk değişimi")
                    sibling.setRed()
                    current = parent
                    steps.add("→ Sibling (${sibling.key}) kırmızı yapıldı, siyah eksiklik Parent'a taşındı: ${current.key}")
                    steps.add("→ Kontrol yukarı doğru devam ediyor")
                } else {
                    // Case 3: Sibling siyah, yakın çocuk kırmızı, uzak çocuk siyah
                    if (current == parent.left) {
                        if (siblingRight == null || siblingRight.isBlack()) {
                            steps.add("Case 3: Sibling (${sibling.key}) siyah, yakın çocuk kırmızı ama uzak çocuk siyah")
                            steps.add("→ Sibling (${sibling.key}) etrafında sağa rotasyon yapılıyor, Case 4'e dönüştürülüyor")
                            siblingLeft?.setBlack()
                            sibling.setRed()
                            rightRotate(sibling)
                            val newSibling = current.getSibling()
                            if (newSibling != null) {
                                steps.add("→ Yeni sibling: ${newSibling.key}")
                            }
                        }

                        // Case 4: Sibling siyah, uzak çocuk kırmızı
                        val finalSibling = current.getSibling()
                        if (finalSibling != null) {
                            steps.add("Case 4: Uzak çocuk kırmızı → Parent (${parent.key}) etrafında rotasyon yapılarak siyah yüksekliği dengeleniyor")
                            finalSibling.color = parent.color
                            parent.setBlack()
                            finalSibling.right?.setBlack()
                            steps.add("→ Sibling (${finalSibling.key}) parent rengini alıyor, Parent (${parent.key}) ve uzak çocuk siyah yapılıyor")
                            steps.add("→ Parent (${parent.key}) etrafında sola rotasyon yapılıyor → Özellik 5 (siyah yüksekliği) restore edildi")
                            leftRotate(parent)
                        }
                    } else {
                        if (siblingLeft == null || siblingLeft.isBlack()) {
                            steps.add("Case 3: Sibling (${sibling.key}) siyah, yakın çocuk kırmızı ama uzak çocuk siyah")
                            steps.add("→ Sibling (${sibling.key}) etrafında sola rotasyon yapılıyor, Case 4'e dönüştürülüyor")
                            siblingRight?.setBlack()
                            sibling.setRed()
                            leftRotate(sibling)
                            val newSibling = current.getSibling()
                            if (newSibling != null) {
                                steps.add("→ Yeni sibling: ${newSibling.key}")
                            }
                        }

                        // Case 4: Sibling siyah, uzak çocuk kırmızı
                        val finalSibling = current.getSibling()
                        if (finalSibling != null) {
                            steps.add("Case 4: Uzak çocuk kırmızı → Parent (${parent.key}) etrafında rotasyon yapılarak siyah yüksekliği dengeleniyor")
                            finalSibling.color = parent.color
                            parent.setBlack()
                            finalSibling.left?.setBlack()
                            steps.add("→ Sibling (${finalSibling.key}) parent rengini alıyor, Parent (${parent.key}) ve uzak çocuk siyah yapılıyor")
                            steps.add("→ Parent (${parent.key}) etrafında sağa rotasyon yapılıyor → Özellik 5 (siyah yüksekliği) restore edildi")
                            rightRotate(parent)
                        }
                    }
                    break
                }
            }
        }

        current.setBlack()
        if (current != root) {
            steps.add("Son kontrol: Current düğüm ${current.key} siyah yapıldı → Tüm Red-Black özellikleri sağlandı")
        }

        return steps
    }
    
    /**
     * Sol rotasyon yapar
     * 
     * @param node Rotasyon yapılacak düğüm
     */
    private fun leftRotate(node: RBNode) {
        val rightChild = node.right ?: return
        node.right = rightChild.left
        
        if (rightChild.left != null) {
            rightChild.left!!.parent = node
        }
        
        rightChild.parent = node.parent
        
        if (node.parent == null) {
            root = rightChild
        } else if (node == node.parent!!.left) {
            node.parent!!.left = rightChild
        } else {
            node.parent!!.right = rightChild
        }
        
        rightChild.left = node
        node.parent = rightChild
    }
    
    /**
     * Sağ rotasyon yapar
     * 
     * @param node Rotasyon yapılacak düğüm
     */
    private fun rightRotate(node: RBNode) {
        val leftChild = node.left ?: return
        node.left = leftChild.right
        
        if (leftChild.right != null) {
            leftChild.right!!.parent = node
        }
        
        leftChild.parent = node.parent
        
        if (node.parent == null) {
            root = leftChild
        } else if (node == node.parent!!.left) {
            node.parent!!.left = leftChild
        } else {
            node.parent!!.right = leftChild
        }
        
        leftChild.right = node
        node.parent = leftChild
    }
    
    /**
     * İki düğümü yer değiştirir (transplant)
     * 
     * @param u Eski düğüm
     * @param v Yeni düğüm
     */
    private fun transplant(u: RBNode, v: RBNode?) {
        if (u.parent == null) {
            root = v
        } else if (u == u.parent!!.left) {
            u.parent!!.left = v
        } else {
            u.parent!!.right = v
        }
        
        v?.parent = u.parent
    }
    
    /**
     * Verilen düğümün alt ağacındaki minimum değeri bulur
     * 
     * @param node Başlangıç düğümü
     * @return Minimum değerli düğüm
     */
    private fun minimum(node: RBNode): RBNode {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }
    
    /**
     * Ağaçta bir değer arar
     * 
     * @param key Aranacak değer
     * @return Bulunan düğüm veya null
     */
    fun search(key: Int): RBNode? {
        var current = root
        while (current != null) {
            when {
                key < current.key -> current = current.left
                key > current.key -> current = current.right
                else -> return current
            }
        }
        return null
    }
    
    /**
     * Ağacın yüksekliğini hesaplar
     * 
     * @return Ağacın yüksekliği
     */
    fun height(): Int {
        return heightHelper(root)
    }
    
    /**
     * Yükseklik hesaplama yardımcı fonksiyonu
     * 
     * @param node Düğüm
     * @return Düğümün yüksekliği
     */
    private fun heightHelper(node: RBNode?): Int {
        if (node == null) return 0
        return 1 + maxOf(heightHelper(node.left), heightHelper(node.right))
    }
    
    /**
     * Ağacın tüm düğümlerini inorder sırada döndürür
     * 
     * @return Düğüm listesi
     */
    fun inorder(): List<RBNode> {
        val result = mutableListOf<RBNode>()
        inorderHelper(root, result)
        return result
    }
    
    /**
     * Inorder traversal yardımcı fonksiyonu
     * 
     * @param node Düğüm
     * @param result Sonuç listesi
     */
    private fun inorderHelper(node: RBNode?, result: MutableList<RBNode>) {
        if (node != null) {
            inorderHelper(node.left, result)
            result.add(node)
            inorderHelper(node.right, result)
        }
    }
    
    /**
     * Ağacı temizler (tüm düğümleri siler)
     */
    fun clear() {
        root = null
    }
}

