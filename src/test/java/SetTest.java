import org.junit.*;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class SetTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testIntersection() {
        Set<BigInteger> set1 = new SetImp();
        set1.addEl(BigInteger.valueOf(1));
        set1.addEl(BigInteger.valueOf(3));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(5));

        Set<BigInteger> set2 = new SetImp();
        set2.addEl(BigInteger.valueOf(1));
        set2.addEl(BigInteger.valueOf(3));
        set2.addEl(BigInteger.valueOf(6));

        Set<BigInteger> setTest = set1.intersection(set2);

        Set<BigInteger> setCheck = new SetImp();

        setCheck.addEl(BigInteger.valueOf(1));
        setCheck.addEl(BigInteger.valueOf(3));

        int i = 1;
    }
}
