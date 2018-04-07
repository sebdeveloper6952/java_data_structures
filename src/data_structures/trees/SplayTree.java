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
    
    protected void splay(BinaryTree<Entry<K,V>> node)
    {
        BinaryTree<Entry<K,V>> parent = null;
    }
}
