package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private class BSTNode {
        public K Key;
        public V Value;
        public BSTNode LeftNode, RightNode;

        public BSTNode(K key, V value) {
            this.Key = key;
            this.Value = value;
        }

        public void printNode() {
            System.out.print("(" + Key.toString() + ":" + Value.toString() + ")");
        }
    }

    private BSTNode Root;

    public BSTMap() {
        Root = null;
    }

    @Override
    public void clear() {
        Root = null;
    }

    private BSTNode getEndNode(BSTNode node, K key) {
        if (node == null) {
            return null;
        } else if (node.Key.compareTo(key) == 0) {
            return node;
        } else if (node.Key.compareTo(key) > 0) {
            return getEndNode(node.LeftNode, key);
        } else {
            return getEndNode(node.RightNode, key);
        }
    }

    @Override
    public boolean containsKey(K key) {
        return getEndNode(this.Root, key) != null;
    }

    @Override
    public V get(K key) {
        BSTNode endNode = getEndNode(this.Root, key);
        if (endNode == null) {
            return null;
        } else {
            return endNode.Value;
        }
    }

    private int sizeof(BSTNode node) {
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

    private BSTNode putEndNode(BSTNode node, K key, V value) {
        if (node == null) {
            return new BSTNode(key, value);
        }

        // 2. 比较并递归
        int cmp = key.compareTo(node.Key);
        if (cmp < 0) {
            // key 小于当前节点，插入到左边，并更新左子树的引用
            node.LeftNode = putEndNode(node.LeftNode, key, value);
        } else if (cmp > 0) {
            // key 大于当前节点，插入到右边，并更新右子树的引用
            node.RightNode = putEndNode(node.RightNode, key, value);
        } else {
            // 3. Key 已存在，更新 Value
            node.Value = value;
        }

        // 4. 返回当前节点，保持树的结构连接
        return node;
    }

    @Override
    public void put(K key, V value) {
        Root = putEndNode(Root, key, value);
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    private void printInOrder(BSTNode node) {
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
