package deque;

import java.util.Iterator;


public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class DequeNode {
        private T item;
        private DequeNode last;
        private DequeNode next;
        DequeNode(T i) {
            item = i;
        }
        DequeNode(T i, DequeNode l, DequeNode n) {
            item = i;
            last = l;
            next = n;
        }

        public void setNL(DequeNode l, DequeNode n) {
            last = l;
            next = n;
        }
    }

    private class LLDequeIterator implements Iterator<T> {
        private int pos;
        LLDequeIterator() {
            pos = 0;
        }
        @Override
        public boolean hasNext() {
            return pos != size;
        }

        @Override
        public T next() {
            pos++;
            return get(pos - 1);
        }
    }

    private DequeNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new DequeNode(null);
        sentinel.setNL(sentinel, sentinel);
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        DequeNode newNode = new DequeNode(item, sentinel, sentinel.next);
        sentinel.next.last = newNode;
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T item) {
        DequeNode newNode = new DequeNode(item, sentinel.last, sentinel);
        sentinel.last.next = newNode;
        sentinel.last = newNode;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        DequeNode currentNode = sentinel.next;
        while (currentNode.next != sentinel) {
            System.out.print(currentNode.item + " ");
            currentNode = currentNode.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        T removed = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.last = sentinel;
        if (size != 0) {
            size--;
        }
        return removed;
    }

    @Override
    public T removeLast() {
        T removed = sentinel.last.item;
        sentinel.last = sentinel.last.last;
        sentinel.last.next = sentinel;
        if (size != 0) {
            size--;
        }
        return removed;
    }

    @Override
    public T get(int index) {
        DequeNode currentNode = sentinel.next;
        if (index > size) {
            return null;
        } else if (index < size / 2) {
            int pos = 0;
            while (pos != index) {
                pos++;
                currentNode = currentNode.next;
            }
        } else if (index >= size / 2) {
            int pos = size + 1;
            while (pos != index) {
                pos--;
                currentNode = currentNode.last;
            }
        }
        return currentNode.item;
    }
    private T getLLRecursive(int index, DequeNode current) {
        if (index == 0) {
            return current.item;
        } else {
            return getLLRecursive(index - 1, current.next);
        }
    }

    public T getRecursive(int index) {
        return getLLRecursive(index, sentinel.next);
    }

    @Override
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Deque) {
            Deque<T> x = (Deque<T>) o;
            if (x.size() != size) {
                return false;
            } else {
                int pos = 0;
                for (T i : this) {
                    if (i != x.get(pos)) {
                        return false;
                    }
                    pos++;
                }
                return true;
            }
        }
        return false;
    }
}
