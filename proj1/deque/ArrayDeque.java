package deque;

import java.util.Iterator;


public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private int front;
    private int back;
    private T[] items;

    private class ArrayDequeIterator implements Iterator<T> {
        private int currentPos;

        ArrayDequeIterator() {
            currentPos = 0;
        }

        @Override
        public boolean hasNext() {
            return currentPos != size;
        }

        @Override
        public T next() {
            currentPos++;
            return get(currentPos - 1);
        }
    }

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[8];
        front = 0;
        back = 0;
    }

    private int getIndex(int num) {
        return num - items.length * Math.floorDiv(num, items.length);
    }

    private boolean getCase() {
        return getIndex(back) >= getIndex(front); //两侧都有时为True
    }


    private int getBackSize() {
        if (getCase()) {
            return items.length - 1 - getIndex(back);
        } else {
            return 0;
        }
    }

    private void resize(int capacity) {
        T[] newArray = (T[]) new Object[capacity];
        if (getCase()) {
            System.arraycopy(items, getIndex(back + 1), newArray, 0, getBackSize());
            System.arraycopy(items, 0, newArray, getBackSize(), front);
        } else {
            System.arraycopy(items, getIndex(back + 1), newArray, 0, size);
        }
        front = size;
        back = -1;
        items = newArray;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length - 1) {
            resize(2 * size);
        }
        items[getIndex(back)] = item;
        size++;
        back--;
        if (size == 1) {
            front++;
        }
    }

    @Override
    public void addLast(T item) {
        if (size == items.length - 1) {
            resize(2 * size);
        }
        items[front] = item;
        size++;
        front++;
        if (size == 1) {
            back--;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        back++;
        size--;
        T removed = items[getIndex(back)];
        if (items.length >= 16 & (double) size / items.length < 0.25) {
            resize(2 * size);
        }
        if (size == 0) {
            front--;
        }
        return removed;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        front--;
        size--;
        T removed = items[getIndex(front)];
        if (items.length >= 16 & (double) size / items.length < 0.25) {
            resize(2 * size);
        }
        if (size == 0) {
            back++;
        }
        return removed;
    }

    @Override
    public T get(int index) {
        return items[getIndex(back + 1 + index)];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ArrayDeque) {
            ArrayDeque<T> x = (ArrayDeque<T>) o;
            if (x.size() != size) {
                return false;
            } else {
                int pos = 0;
                for (T i : this) {
                    pos++;
                    if (i != x.get(pos)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
