package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }


    @Test
    public void testSquarePrimesIdenticalPrime() {
        IntList lst = IntList.of(3, 3, 3, 3, 3, 3);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 9 -> 9 -> 9 -> 9 -> 9", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesFirstPrime() {
        IntList lst = IntList.of(7, 2, 5, 13, 15);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("49 -> 4 -> 25 -> 169 -> 15", lst.toString());
        assertTrue(changed);
    }
}
