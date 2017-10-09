import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {

    private void start() {         //todo: Does this throw APException?
        Scanner input = new Scanner(System.in);
        Parser parser = new Parser();

        try {
            while (input.hasNext()) {
                parser.parseStatement(input);
            }

//            parser.parseEndOfFile(input);
        } catch (APException e) {
            throw new Error("New error", e);
        }
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
