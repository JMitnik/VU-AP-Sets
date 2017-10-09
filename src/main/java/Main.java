import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {

    private void start() {
        Scanner input = new Scanner(System.in);
        Interpreter interpreter = new Interpreter();

        try {
            while (input.hasNext()) {
                interpreter.readStatement(input);
            }

//            interpreter.readEndOfFile(input);
        } catch (APException e) {
            throw new Error("New error", e);
        }
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
