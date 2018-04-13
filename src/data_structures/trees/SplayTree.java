package data_structures.trees;

import java.util.Map.Entry;

/**
 *
 * @author sebdeveloper6952
 */
public class SplayTree<K extends Comparable<K>, V> extends BinarySearchTree<K,V>
{
    public V put(K key, V value)
    {
        V old = super.put(key, value);
        splay(actionNode);
        return old;
    }
    
    public V remove(K key)
    {
        V old = super.remove(key);
        if(actionNode != null) splay(actionNode);
        return old;
    }
    
    public boolean contains(K key)
    {
        boolean b = super.contains(key);
        if(b) splay(actionNode);
        return b;
    }
    
    public V get(K key)
    {
        V v = super.get(key);
        if(v != null) splay(actionNode);
        return v;
    }
    
    /**
     * Performs a sequence of rotations until the provided node is the root of
     * the tree.
     * @param node Node to splay.
     * pre: node is not the root of the tree.
     * post: node is the root of the tree.
     */
    protected void splay(BinaryTree<Entry<K,V>> node)
    {
        if(node.equals(root) || node == null) return;
        // get the parent and grandparent of this node
        BinaryTree<Entry<K,V>> gp = null;
        BinaryTree<Entry<K,V>> p = null;
        boolean hasGp = false;
        while(node.parent() != null)
        {
            p = node.parent();
            if(p != null) gp = p.parent();
            hasGp = gp != null;
            // <editor-fold defaultstate="collapsed" desc="Node is left child">
            if (node.isLeftChild()) {
                if (hasGp) // node has a grandparent
                {
                    if (p.isLeftChild()) // left-left
                    {
                        gp.rotateRight();
                        p.rotateRight();
                    } else // left-right
                    {
                        p.rotateRight();
                        gp.rotateLeft();
                    }
                } else // left; node has only parent.
                {                    
                    p.rotateRight();
                }
            } // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Node is right child">
            else // node is right child
            {
                if (hasGp) // node has a grandparent
                {
                    if (p.isLeftChild()) // right-left
                    {
                        p.rotateLeft();
                        gp.rotateRight();
                    } else // right-right
                    {
                        gp.rotateLeft();
                        p.rotateLeft();
                    }
                } else // right; node has only parent.
                {                    
                    p.rotateLeft();
                }
            }

// </editor-fold>
        }
        // the splayed node is now the root of the tree
        root = node;
    }
}
