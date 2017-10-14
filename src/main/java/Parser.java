
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;

public class Parser {
    private static final char MULTIPLICATIVE_OPERATOR = '*';

    // Additive operators
    private static final char SYMMETRICDIFFERENCE_OPERATOR = '|';
    private static final char UNION_OPERATOR = '+';
    private static final char COMPLEMENT_OPERATOR = '-';

    private static HashMap<Identifier, Set<BigInteger>> variables = new HashMap<>();

    // Removes all stored variables
    private static void init() {
        variables.clear();
    }

    // Reads a program followed by an <Eof>, <Eof> when hasNextLine() is false
    private static void readProgram(Scanner input) {
        try {readStatement(input);}
        catch (APException error) {
            System.out.println("ERROR: " + error.getMessage());
        }

        while( input.hasNextLine() ) {
            input.nextLine();
            try {readStatement(input);}
            catch (APException error) {
                System.out.println("ERROR: " + error.getMessage());
            }
        }
    }

    // Reads a Statement and performs an action accordingly. Empty statements ("") are ignored
    private static void readStatement(Scanner input) throws APException {
        //Ignore completely blank lines
        if ( !input.hasNext(".") ) {
            return;
        }

        // Could introduce terminateSpaces(input); here but think we do not want this.
        if( nextCharIs(input, '/') ) {
            // ignore line

        } else if (nextCharIsLetter(input)) {
            readAssignment(input);
            readEol(input);

        } else if (nextCharIs(input, '?')) {
            readCharacter(input, '?');
            System.out.println( readExpression(input) );
            readEol(input);

        } else {
            throw new APException("'/', '?', or a character (\"[a-zA-Z]\") was expected at the beginning of a statement!");
        }
    }

    // Reads an Expression and returns the resulting set.
    private static Set<BigInteger> readExpression(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result = readTerm(input);


        while( input.hasNext("[|\\-+]") ){
            if (nextCharIs(input, SYMMETRICDIFFERENCE_OPERATOR)) {
                readCharacter(input, SYMMETRICDIFFERENCE_OPERATOR);
                terminateSpaces(input);
                result = result.symmDifference( readTerm(input) );

            } else if (nextCharIs(input, COMPLEMENT_OPERATOR)) {
                readCharacter(input, COMPLEMENT_OPERATOR);
                terminateSpaces(input);
                result = result.complement( readTerm(input) );

            } else if (nextCharIs(input, UNION_OPERATOR)) {
                readCharacter(input, UNION_OPERATOR);
                terminateSpaces(input);
                result = result.union( readTerm(input) );

            } else {
                throw new APException("Expected an additive operator! Found: '" + nextChar(input) + "'");
            }
        }

        return result;
    }

    // Reads an Assignment statement and assigns the Identifier to the set that is the result of the Expression.
    private static void readAssignment(Scanner input) throws APException {
        Identifier id = readIdentifier(input);

        terminateSpaces(input);
        readCharacter(input, '=');
        terminateSpaces(input);

        Set<BigInteger> set = readExpression(input);
        terminateSpaces(input);

        readEol(input);

        assign(id, set);
    }

    // Assigns a set to an Identifier.
    private static void assign(Identifier id, Set<BigInteger> set) throws APException {
        if (variables.containsKey(id)) {
            throw new APException("A set has already been assigned to " + id.getIdentifierName());
        }
        variables.put(id, set);
    }

    // Reads a term consisting of a factor followed by 0 or more factors. All factors are separated by
    // a MULTIPLICATIVE_OPERATOR.
    private static Set<BigInteger> readTerm(Scanner input) throws APException {
        Set<BigInteger> result = readFactor(input);

        while ( nextCharIs(input, MULTIPLICATIVE_OPERATOR) ) {
            readCharacter(input, MULTIPLICATIVE_OPERATOR);
            result = result.intersection( readFactor(input) );
        }

        return result;
    }

    // Reads a factor, and returns the resulting Set.
    // A factor is an identifier, a complex factor or a set.
    private static Set<BigInteger> readFactor(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result;

        if ( nextCharIsLetter(input) ) { // factor is an identifier
            Identifier id = readIdentifier(input);
            result = getSet(id);

        } else if ( nextCharIs(input, '{') ) { // factor is a set
            result = readSet(input);

        } else if ( nextCharIs(input, '(') ) { // factor is a complex factor
            readCharacter(input, '(');
            result = readExpression(input);
            readCharacter(input, ')');

        } else {
            throw new APException("Expected a factor!" );
        }

        return result;
    }

    // Retrieves a set assigned to an Identifier
    private static Set<BigInteger> getSet(Identifier id) throws APException {
        if (variables.containsKey(id)) {
            return variables.get(id);
        } else {
            throw new APException("No set assigned to '" + id.getIdentifierName() + "'!");
        }
    }

    // Takes a Scanner, reads the set that is in it and returns a set.
    private static Set<BigInteger> readSet(Scanner input) throws APException {
        Set<BigInteger> set = new SetImpl<>();
        readCharacter(input, '{');
        terminateSpaces(input);

        if ( nextCharIs(input, '}') ) {
            readCharacter(input, '}');
            return new SetImpl<>();
        }

        set.add( readBigInteger(input) );
        terminateSpaces(input);

        while( nextCharIs(input, ',') ) {
            readCharacter(input, ',');
            terminateSpaces(input);
            set.add( readBigInteger(input) );
            terminateSpaces(input);
        }

        readCharacter(input, '}');

        return set;
    }

    // Reads an identifier.
    private static Identifier readIdentifier(Scanner input) throws APException {
        Identifier result = new IdentifierImpl();

        while ( nextCharIsLetter(input) || nextCharIsDigit(input) ) {
            result.add( nextChar(input) );
        }

        return result;
    }

    // The function converts a BigInteger given
    private static BigInteger readBigInteger(Scanner input) throws APException {
        if ( !nextCharIsDigit(input) ) {
            throw new APException("Expected an integer! Found '" + input.next() + "'");
        }

        StringBuilder val = new StringBuilder();

        while ( nextCharIsDigit(input) ) {
            val.append( nextChar(input) );
        }

        return new BigInteger( val.toString() );
    }

    // Reads next character if it equals thee given character
    private static void readCharacter(Scanner input, char ch) throws APException {
        if (!( nextCharIs(input, ch) )) {
            throw new APException("'" + ch + "'" + " was expected!");
        }
        nextChar(input);
    }

    // Removes all subsequent whitespace
    private static void terminateSpaces(Scanner input) throws APException {
        while ( nextCharIs(input, ' ') ) {
            readCharacter(input, ' ');
        }
    }

    // Checks to see if there are any tokens left in the line
    private static void readEol(Scanner in) throws APException {
        terminateSpaces(in);
        if ( in.findInLine("") != null ){
            throw new APException("An <EoL> was expected");
            //throw new APException("No <Eol>! Found '" + in.next() + "'");
        }
    }

    // Method to read 1 character.
    private static char nextChar (Scanner in) {
        return in.next().charAt(0);
    }

    // Method to check if the next character to be read when
    // calling nextChar() is equal to the provided character.
    private static boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c+""));
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a digit.
    private static boolean nextCharIsDigit (Scanner in) {
        return in.hasNext("[0-9]");
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a letter.
    private static boolean nextCharIsLetter (Scanner in) {
        return in.hasNext("[A-Za-z]");
    }

    // First reads a program from a file (from filepath), then processes user input.
    public static void main(String[] args) throws FileNotFoundException {
        File text = new File("C:\\Users\\wille\\Documents\\Set.txt");
        Scanner pr = new Scanner( text ).useDelimiter("");
        readProgram(pr);
        pr.close();

        String ANSI_RED = "\u001B[31m";
        String ANSI_YELLOW = "\u001B[32m";
        String ANSI_RESET = "\u001B[0m";

        Scanner in = new Scanner(System.in).useDelimiter("") ;
        System.out.printf( ANSI_YELLOW + "\nSet Calculator" + ANSI_RESET + "\n> ");

        init();
        while ( in.hasNextLine() ) {
            try {
                readStatement( in );
            } catch (APException error) {
                System.out.println(ANSI_RED + error.getMessage() + ANSI_RESET);
            }
            System.out.printf("> ");
            in.nextLine();
        }

        in.close();
    }

}
