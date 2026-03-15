package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private int bucketNum;
    private double maxLoad;
    private Set<K> keyset;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.bucketNum = initialSize;
        this.buckets = createTable(initialSize);
        this.maxLoad = maxLoad;
        this.size = 0;
        this.keyset = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] hashTable = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = createBucket();
        }
        return hashTable;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        this.bucketNum = 16;
        this.buckets = createTable(16);
        this.maxLoad = 0.75;
        this.size = 0;
        this.keyset = new HashSet<>();
    }

    private Node getHelper(K key) {
        int index = Math.floorMod(key.hashCode(), bucketNum);
        for (Node n : buckets[index]) {
            if (n.key.equals(key)) {
                return n;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (getHelper(key) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {
        Node n = getHelper(key);
        if (n == null) {
            return null;
        } else {
            return n.value;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private void resize() {
        int num = bucketNum;
        Collection<Node>[] buk = this.buckets;
        this.bucketNum = 2 * num;
        this.buckets = createTable(bucketNum);
        this.size = 0;

        for (int i = 0; i < num; i++) {
            for (Node n : buk[i]) {
                put(n.key, n.value);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        Node n = getHelper(key);
        if (n != null) {
            n.value = value;
        } else {
            double loadfactor = (double) (size + 1) / bucketNum;
            if (loadfactor > maxLoad) {
                resize();
            }
            int index = Math.floorMod(key.hashCode(), bucketNum);
            buckets[index].add(createNode(key, value));
            keyset.add(key);
            size++;
        }
    }

    @Override
    public Set<K> keySet() {
        return this.keyset;
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
        return keyset.iterator();
    }

}
