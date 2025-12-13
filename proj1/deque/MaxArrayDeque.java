package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> Comp;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        Comp = c;
    }

    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T maxItem = get(0), current;
        for (int i = 0; i < size(); i++) {
            current = get(i);
            if (c.compare(maxItem, current) > 0) {
                maxItem = current;
            }
        }
        return maxItem;
    }

    public T max() {
        return max(Comp);
    }
}
