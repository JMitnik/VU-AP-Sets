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
    }

    @Test
    public void testUnion() {
        Set<BigInteger> set1 = new SetImp();
        set1.addEl(BigInteger.valueOf(1));
        set1.addEl(BigInteger.valueOf(3));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(9));
        set1.addEl(BigInteger.valueOf(95));
        set1.addEl(BigInteger.valueOf(5));

        Set<BigInteger> set2 = new SetImp();
        set2.addEl(BigInteger.valueOf(1));
        set2.addEl(BigInteger.valueOf(3));
        set2.addEl(BigInteger.valueOf(6));
        set2.addEl(BigInteger.valueOf(77));
        set2.addEl(BigInteger.valueOf(43));

        Set<BigInteger> setTest = set1.union(set2);

        int i = 1;
    }

    @Test
    public void testComplement() {
        Set<BigInteger> set1 = new SetImp();
        set1.addEl(BigInteger.valueOf(1));
        set1.addEl(BigInteger.valueOf(3));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(9));
        set1.addEl(BigInteger.valueOf(95));
        set1.addEl(BigInteger.valueOf(5));

        Set<BigInteger> set2 = new SetImp();
        set2.addEl(BigInteger.valueOf(1));
        set2.addEl(BigInteger.valueOf(3));
        set2.addEl(BigInteger.valueOf(6));
        set2.addEl(BigInteger.valueOf(77));
        set2.addEl(BigInteger.valueOf(43));

        Set<BigInteger> setTest = set1.complement(set2);

        int i = 1;
    }

    @Test
    public void testSymm() {
        Set<BigInteger> set1 = new SetImp();
        set1.addEl(BigInteger.valueOf(1));
        set1.addEl(BigInteger.valueOf(3));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(4));
        set1.addEl(BigInteger.valueOf(9));
        set1.addEl(BigInteger.valueOf(95));
        set1.addEl(BigInteger.valueOf(5));

        Set<BigInteger> set2 = new SetImp();
        set2.addEl(BigInteger.valueOf(1));
        set2.addEl(BigInteger.valueOf(3));
        set2.addEl(BigInteger.valueOf(6));
        set2.addEl(BigInteger.valueOf(77));
        set2.addEl(BigInteger.valueOf(43));

        Set<BigInteger> setTest = set1.symmDifference(set2);

        int i = 1;
    }
}
