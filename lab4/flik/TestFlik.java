package flik;
import org.junit.Test;
import static org.junit.Assert.*;
public class TestFlik {
    @Test
    public void testInts() {
        int i = 0;
        for (int j = 0; j < 500; j++) {
            assertTrue(Flik.isSameNumber(i, j));
            i++;
        }
    }
}
