package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    private class CompareIntGreat implements Comparator<Integer> {

        @Override
        public int compare(Integer integer, Integer t1) {
            return integer - t1;
        }
    }
    private class CompareIntLess implements Comparator<Integer> {

        @Override
        public int compare(Integer integer, Integer t1) {
            return t1 - integer;
        }
    }

    private class CompareStringVal implements Comparator<String> {

        @Override
        public int compare(String s, String t1) {
            return s.compareTo(t1);
        }
    }

    private class CompareStringLen implements Comparator<String> {

        @Override
        public int compare(String s, String t1) {
            return s.length() - t1.length();
        }
    }

    @Test
    public void testInt() {
        MaxArrayDeque<Integer> testD = new MaxArrayDeque<>(new CompareIntGreat());

        assertEquals(null, testD.max());
        assertEquals(null, testD.max(new CompareIntLess()));
        int N = 10;
        for (int i = 0; i < N; i++) {
            testD.addFirst(i);
        }
        assertEquals(0, (int) testD.max());
        assertEquals(N - 1, (int) testD.max(new CompareIntLess()));
    }

    public void testString() {
        MaxArrayDeque<String> testD = new MaxArrayDeque<>(new CompareStringVal());

        assertEquals(null, testD.max());
        assertEquals(null, testD.max(new CompareStringLen()));
        int N = 4;
        String[] strs = {"aaaa", "bbb", "ccc","ddd"};
        for (int i = 0; i < N; i++) {
            testD.addFirst(strs[i]);
        }
        assertEquals("ddd", (String) testD.max());
        assertEquals("aaaa", (String) testD.max(new CompareStringLen()));
    }
}
