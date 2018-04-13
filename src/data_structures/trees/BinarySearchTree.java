package data_structures.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author sebdeveloper6952
 */
public class BinarySearchTree<K extends Comparable<K>,V>
{
    protected BinaryTree<Entry<K,V>> root;
    protected BinaryTree<Entry<K,V>> actionNode;
    protected int size;
    
    public BinarySearchTree()
    {
        root = new BinaryTree<>();
        size = 0;
    }
    
    public boolean isEmpty() 
    {
        return root.isEmpty();
    }

    public void clear() 
    {
        root = new BinaryTree<>();
        size = 0;
    }
    
    public int size() 
    {
       return size;
    }
    
    /**
     * Add provided key-value pair to tree. If key was already present, it
     * updates the value associated with it.
     * @param key
     * @param value
     * @return Old value associated with key, or null if key was not in tree.
     */
    public V put(K key, V value)
    {
        // create new entry to be inserted
        Entry<K,V> e = new SEntry(key, value);
        V old = null;
        if (root.isEmpty())
        {
            root = new BinaryTree<>(e);
            actionNode = root;
            size++;
        }
        else
        {
          // find node where provided key should go
          BinaryTree<Entry<K,V>> foundNode = findNode(key, root);
          // save found node as action-node for use in other algorithms
          actionNode = foundNode;
          // key already in tree, update value
          if(!foundNode.isEmpty())
          {
              old = foundNode.value().getValue();
              foundNode.setValue(e);
          }
          else
          {
              foundNode.setValue(e);
              // add empty children nodes if needed
              if (foundNode.left()== null)
                  foundNode.setLeft(new BinaryTree<>());
              if (foundNode.right()== null)
                  foundNode.setRight(new BinaryTree<>());
              size++;
          }
        }
        return old;
    }
    
    /**
     * Returns true if provided key is found on tree.
     * @param key Key to search in tree.
     * @return true if key found, false otherwise.
     */
    public boolean contains(K key)
    {
        return get(key) == null;
    }
    
    /**
     * Gets the value associated with the provided key.
     * @param key Key to search in tree.
     * @return Value associated with key if it is on the tree, null otherwise.
     */
    public V get(K key)
    {
        if(key == null || root.isEmpty()) return null;
        BinaryTree<Entry<K,V>> node = findNode(key, root);
        if(node.isEmpty()) return null;
        actionNode = node;
        return node.value().getValue();
    }

    /**
     * Removes key - value pair associated with provided key, if it is found
     * on the tree.
     * @param key Key to remove from tree.
     * @return Value associated with key if found, null otherwise.
     */
    public V remove(K key) 
    {
        if(key == null) return null;
        BinaryTree<Entry<K,V>> foundNode = findNode(key, root);
        // key is not in tree
        if(foundNode.isEmpty()) return null;
        V temp = foundNode.value().getValue();
        // handle removing the last node
        if(size == 1)
        {
            clear();
            return temp;
        }
        else
        {
            if(foundNode.left().isEmpty()) 
                removeExternal(foundNode.left());
            else if(foundNode.right().isEmpty()) 
                removeExternal(foundNode.right());
            else
            {
                // find replacement node
                BinaryTree<Entry<K,V>> rep = foundNode.right();
                while(rep.left().isInternal()) rep = rep.left();
                // move rep value to foundNode
                foundNode.setValue(rep.value());
                // remove external node that is left child of replacement node
                removeExternal(rep.left());
                // update action-node to point to foundNode parent
                actionNode = foundNode.parent();
            }
        }
        size--;
        return temp;
    }

    public List<V> valuesInOrder()
    {
        List<V> list = new ArrayList<>();
        if(isEmpty()) return list;
        inOrderRecursive(root, list);
        return list;
    }
    
    /**
     * Helper method used by add, get and remove methods.
     * @param node
     * @param value
     * @return 
     */
    protected BinaryTree<Entry<K,V>> findNode(K key, BinaryTree<Entry<K,V>> node)
    {
        if(node.isEmpty()) return node;
        else
        {
           // compare current node key with provided key
           int c =  node.value().getKey().compareTo(key);
           if (c == 0 )return node; // we found the node
           else if (c > 0) // node key > key
           {
               // search left
               return findNode(key, node.left());
           }
           else 
           {
               // search right
              return findNode(key, node.right()); 
           }
        }
    }
    
    /**
     * Remove external node v and its parent p, and replace p with v's sibling.
     * @param v 
     */
    protected void removeExternal(BinaryTree<Entry<K,V>> v)
    {
        // node is not external
        if(!v.isEmpty()) return;
        // grab v's sibling
        BinaryTree<Entry<K,V>> s = null;
        // grab v's parent
        BinaryTree<Entry<K,V>> p = v.parent();
        if(v.isLeftChild()) s = p.right();
        else s = p.left();
        if(p.parent() != null)
        {
            if(p.isLeftChild()) p.parent().setLeft(s);
            else p.parent().setRight(s);
        }
        else
        {
            root = s;
            root.setParent(null);
        }
    }
    
    protected void inOrderRecursive(BinaryTree<Entry<K,V>> node, List<V> list)
    {
        if(node.left() != null && !node.left().isEmpty()) 
            inOrderRecursive(node.left(), list);
        list.add(node.value().getValue());
        if(node.right() != null && !node.right().isEmpty()) 
            inOrderRecursive(node.right(), list);
    }
    
    /**
     * Clase utilizada internamente por el arbol binario de busqueda.
     * @param <K>
     * @param <V> 
     */
    protected static class SEntry<K,V> implements Entry<K,V>
    {
        protected K key;
        protected V value;
        
        public SEntry(K k, V v)
        {
            key = k;
            value = v;
        }
        
        @Override
        public K getKey() 
        {
            return key;
        }

        @Override
        public V getValue() 
        {
            return value;
        }

        @Override
        public V setValue(V value) 
        {
            V temp = value;
            this.value = value;
            return temp;
        }
        
    }
}