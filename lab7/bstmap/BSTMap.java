package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V>{
    private class BSTNode<K, V> {
        public K Key;
        public V Value;
        public BSTNode<K, V> LeftNode, RightNode;

        public BSTNode(K key, V value) {
            this.Key = key;
            this.Value = value;
            LeftNode = null;
            RightNode = null;
        }

        public void printNode() {
            System.out.print("(" + Key.toString() + ":" + Value.toString() + ")");
        }
    }

    private BSTNode<K, V> Root;

    public BSTMap() {
        Root = null;
    }

    @Override
    public void clear() {
        Root = null;
    }

    private BSTNode<K, V> getEndNode(BSTNode<K, V> node, K key) {
        if (node == null) {
            node = new BSTNode<>(null, null);
            return node;
        } else if (node.Key == key) {
            return node;
        } else if (node.Key.compareTo(key) < 0) {
            return getEndNode(node.LeftNode, key);
        } else {
            return getEndNode(node.RightNode, key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return getEndNode(Root, key) != null;
    }

    @Override
    public V get(K key) {
        BSTNode<K, V> endNode = getEndNode(this.Root, key);
        if (endNode == null) {
            return null;
        } else {
            return endNode.Value;
        }
    }

    private int sizeof(BSTNode<K, V> node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + sizeof(node.LeftNode) + sizeof(node.RightNode);
        }
    }

    @Override
    public int size() {
        return sizeof(Root);
    }


    @Override
    public void put(K key, V value) {
        BSTNode<K, V> endNode = getEndNode(Root, key);
        if (endNode == null) {
            endNode = new BSTNode<K, V>(key, value);
        } else {
            endNode.Value = value;
        }
    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private void printInOrder(BSTNode<K, V> node) {
        if (node == null) {
            return;
        }
        printInOrder(node.LeftNode);
        node.printNode();
        printInOrder(node.RightNode);
    }

    public void printInOrder() {
        printInOrder(Root);
    }
}
