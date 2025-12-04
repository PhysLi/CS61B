package deque;

import java.util.Iterator;
import java.util.Objects;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class DequeNode {
        public T Item;
        public DequeNode last;
        public DequeNode next;
        public DequeNode(T i) {
            Item = i;
        }
        public DequeNode(T i, DequeNode l, DequeNode n) {
            Item = i;
            last = l;
            next = n;
        }

        public void setNL(DequeNode l, DequeNode n) {
            last = l;
            next = n;
        }
    }

    private class LLDequeIterator implements Iterator<T> {
        private int Pos;
        public LLDequeIterator() {
            Pos = 0;
        }
        @Override
        public boolean hasNext() {
            return Pos != size;
        }

        @Override
        public T next() {
            Pos++;
            return get(Pos - 1);
        }
    }

    private DequeNode Sentinel;
    private int size;

    public LinkedListDeque() {
        Sentinel = new DequeNode(null);
        Sentinel.setNL(Sentinel, Sentinel);
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        DequeNode NewNode = new DequeNode(item, Sentinel, Sentinel.next);
        Sentinel.next.last = NewNode;
        Sentinel.next = NewNode;
        size++;
    }

    @Override
    public void addLast(T item) {
        DequeNode NewNode = new DequeNode(item, Sentinel.last, Sentinel);
        Sentinel.last.next = NewNode;
        Sentinel.last = NewNode;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        DequeNode CurrentNode = Sentinel.next;
        while (CurrentNode.next != Sentinel) {
            System.out.print(CurrentNode.Item + " ");
            CurrentNode = CurrentNode.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        T removed = Sentinel.next.Item;
        Sentinel.next = Sentinel.next.next;
        Sentinel.next.last = Sentinel;
        if (size != 0) {
            size--;
        }
        return removed;
    }

    @Override
    public T removeLast() {
        T removed = Sentinel.last.Item;
        Sentinel.last = Sentinel.last.last;
        Sentinel.last.next = Sentinel;
        if (size != 0) {
            size--;
        }
        return removed;
    }

    @Override
    public T get(int index) {
        DequeNode CurrentNode = Sentinel;
        if (index > size) {
            return null;
        } else if (index < size / 2) {
            int Pos = 0;
            while (Pos != index) {
                Pos++;
                CurrentNode = CurrentNode.next;
            }
        } else if (index >= size / 2) {
            int Pos = size + 1;
            while (Pos != index) {
                Pos--;
                CurrentNode = CurrentNode.last;
            }
        }
        return CurrentNode.Item;
    }
    private T getLLRecursive(int index, DequeNode current) {
        if (index == 0) {
            return current.Item;
        } else {
            return getLLRecursive(index--, current.next);
        }
    }

    public T getRecursive(int index) {
        return getLLRecursive(index, Sentinel);
    }

    @Override
    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof LinkedListDeque) {
            LinkedListDeque<T> x = (LinkedListDeque<T>) o;
            if (x.size() != size) {
                return false;
            } else {
                int Pos = 0;
                for (T i : this) {
                    Pos++;
                    if (i != x.get(Pos)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
