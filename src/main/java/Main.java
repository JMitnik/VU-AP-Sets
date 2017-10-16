import java.util.HashMap;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    private void start() {
        Scanner input = new Scanner(System.in);
        Interpreter interpreter = new Interpreter();
        interpreter.readFile(input);
        //todo: Put while loop around
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
