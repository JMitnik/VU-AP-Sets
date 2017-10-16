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
