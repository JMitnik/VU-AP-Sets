import org.junit.*;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Scanner;

import static org.junit.Assert.*;

public class InterpreterTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testPrint() throws APException {
        Hashtable<IdentiferImp, SetImp> hashTable = new Hashtable<>();

        Interpreter interpreter = new Interpreter(hashTable);

        // Test Regular print statement
        testParse(setInput("? Print"), interpreter);

        // Test print statement with invalid identifier
        try {
            testParse(setInput("?123123"), interpreter);
        } catch (APException e) {
            assertEquals("Error not right", "APException: An improper factor has been recognized", e.toString());
        }

        // Test print statement with complex factor
        testParse(setInput("?({1}+{3})"), interpreter);

        // Test print statement with set
        testParse(setInput("?{1,3,4,5}"), interpreter);
    }

    @Test
    public void testAssignment() throws APException {
        Hashtable<IdentiferImp, SetImp> hashTable = new Hashtable<>();
        Interpreter interpreter = new Interpreter(hashTable);

        // Test Empty Set Assignment
        testParse(setInput("Aap={}"), interpreter);

        // Test Set Operation
        testParse(setInput("Full={  1  , 2, 3 }*{3 ,  4  ,  5 }"), interpreter);

        // Test Whitespace
//        testParse(setInput("Vat = Worm - Xorn - Yeti - Zeven"), interpreter);

        // Test priorities
        testParse(setInput("Beer ={1, 2, 3, 4}*{4, 5, 6}*{3, 4  }"), interpreter);

        // Test print statement with set
        testParse(setInput("?{1,3,4,5}"), interpreter);
    }

    private Scanner setInput(String input) {
        return new Scanner(input).useDelimiter("");
    }

    private void testParse(Scanner input, Interpreter interpreter) throws APException {
        try {
            while (input.hasNext()) {
                interpreter.readStatement(input);
            }

        } catch (APException e) {
            throw e;
        }
    }
}
