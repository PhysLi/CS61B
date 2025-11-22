package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> simple = new AListNoResizing<>();
        BuggyAList<Integer> Buggy = new BuggyAList<>();
        for (int i = 4; i < 7; i++) {
            simple.addLast(i);
            Buggy.addLast(i);
        }
        for (int i = 0; i < 3; i++) {
            assertEquals(simple.removeLast(),Buggy.removeLast());
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = B.size();
                assertEquals(size1, size2);
            } else if (L.size() > 0) {
                if (operationNumber == 2) {
                    // getLast
                    int last1 = L.getLast();
                    int last2 = B.getLast();
                    assertEquals(last1, last2);
                } else if (operationNumber == 3) {
                    int remove1 = L.removeLast();
                    int remove2 = B.removeLast();
                    assertEquals(remove1, remove2);
                }
            }
        }
    }
}
