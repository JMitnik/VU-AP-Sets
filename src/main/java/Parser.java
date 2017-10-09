
//import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.regex.*;

public class Parser {

    // TODO fix this
    public static void readProgram(Scanner input) throws APException {
        readStatement(input);
        while( input.hasNextLine() ) {
            input.nextLine();
            readStatement(input);
        }
        // TODO? readEof(input);
    }

    public static void readStatement(Scanner input) throws APException {
        if( nextCharIs(input, '/') ) {
            readCharacter(input, '/');
            // IGNORE do nothing
        } else if (nextCharIsLetter(input)) {
            readAssignment(input);
        } else if (nextCharIs(input, '?')) {
            System.out.println("made it in the if loop");
            readCharacter(input, '?');
            input.next();
            System.out.println("read the character!");
            System.out.println( Interpreter.readExpression(input) );
        } else {
            throw new APException("'/', '?', or a character (\"[a-zA-Z]\") was expected at the beginning of a statement!");
        }
    }

    public static void readAssignment(Scanner input) throws APException {
        Identifier id = readIdentifier(input);
        readCharacter(input, '=');
        Set set = Interpreter.readExpression(input);
        readEol(input);
        Interpreter.assign(id, set);
    }

    public static Identifier readIdentifier(Scanner input) throws APException {
        Identifier result = new IdentifierImpl();
        if (nextCharIsLetter(input)) {
            result.add(nextChar(input));
        } else {
            throw new APException("Identifier has to start with a letter, found: " + nextChar(input));
        }

        while ( nextCharIsLetter(input) || nextCharIsDigit(input) ) {
            result.add(nextChar(input));
        }

        return result;
    }


    public static void readCharacter(Scanner input, char ch) throws APException {
        if (!( nextCharIs(input, ch) )) {
            throw new APException("'" + ch + "'" + " was expected");
        }
        nextChar(input);
    }

    // TODO checks if this works well when reading a program of multiple lines.
    // checks if the line has ended. If there is more input, it throws an APException.
    public  static void readEol(Scanner input) throws APException {
        if ( input.hasNextLine() ) {
            throw new APException("Expected an <eol>, found: " + input.nextLine());
        }
    }
    // Method to read 1 character.
    public static char nextChar (Scanner in) {
        return in.next().charAt(0);
    }

    // Method to check if the next character to be read when
    // calling nextChar() is equal to the provided character.
    public static boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c+""));
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a digit.
    // TODO
    public static boolean nextCharIsDigit (Scanner in) {
        return in.hasNext("[0-9]");
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a letter.
    // TODO
    public static boolean nextCharIsLetter (Scanner in) {
        return in.hasNext("[A-Za-z]");
    }

    public static void main(String[] argv) {
        Scanner sc = new Scanner("? {9, 8 , 6}");
        //sc.useDelimiter("");
        System.out.println( nextCharIs(sc,'?'));
        System.out.println( nextCharIs(sc,'('));
        System.out.println( nextCharIsLetter(sc) );
        // System.out.println(nextCharIsLetter(sc));
        /*try {
            readProgram(sc);
        } catch (APException error) {
            System.out.println(error);
        }*/
    }
}
