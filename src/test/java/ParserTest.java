import org.junit.*;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Scanner;

import static org.junit.Assert.*;

public class ParserTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testPrint() throws APException {
        Parser parser = new Parser();

        // Test Regular print statement
        testParse(setInput("?Print"), parser);

        // Test print statement with invalid identifier
        testParse(setInput("?123123"), parser);

        // Test print statement with complex factor
        testParse(setInput("(1+3)"), parser);

        // Test print statement with set
        testParse(setInput("{1,3,4,5}"), parser);
    }

    private Scanner setInput(String input) {
        return new Scanner(input).useDelimiter("");
    }

    private void testParse(Scanner input, Parser parser) throws APException {
        try {
            while (input.hasNext()) {
                parser.parseStatement(input);
            }

//            parser.parseEndOfFile(input);
        } catch (APException e) {
            System.out.println(e);
        }
    }
}
