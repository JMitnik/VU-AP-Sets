import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {

    private void start() {
        Scanner input = new Scanner(System.in);
        Hashtable<IdentiferImp, SetImp> hashTable = new Hashtable<>();
        Interpreter interpreter = new Interpreter(hashTable);

        //todo: Put while loop around
        while (input.hasNext()) {
            try {
                interpreter.readStatement(input);
            }
            catch (APException e) {
                //todo: print out the error
                throw new Error("New error", e);
            }
//            interpreter.readEndOfFile(input);
        }
    }

    public static void main(String[] argv) {
        new Main().start();
    }
}
