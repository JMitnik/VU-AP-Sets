
import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import java.util.regex.Pattern;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;

public class Parser {
    private static final char MULTIPLICATIVE_OPERATOR = '*';

    // Additive operators
    private static final char SYMMETRICDIFFERENCE_OPERATOR = '|';
    private static final char UNION_OPERATOR = '+';
    private static final char COMPLEMENT_OPERATOR = '-';

    private static final char PRINT_STATEMENT_CHAR = '?';
    private static final char COMMENT_CHAR = '/';

    private HashMap<Identifier, Set<BigInteger>> variables = new HashMap<>();

    public Parser( Scanner in ) {
        in.useDelimiter("");

        while ( in.hasNextLine() ) {
            try {
                readStatement( in );
            } catch (APException error) {
                System.out.println(error.getMessage());
            }

            in.nextLine();
        }

        in.close();
    }

    public Parser( InputStream sc ) {
        Scanner in = new Scanner( sc ).useDelimiter("") ;

        System.out.println("Set Calculator");
        System.out.printf("> ");

        while ( in.hasNextLine() ) {
            try {
                readStatement( in );
            } catch (APException error) {
                System.out.println(error.getMessage());
            }
            System.out.printf("> ");
            in.nextLine();
        }

        in.close();
    }

    public Parser( String path ) {
        try {
            Scanner pr = new Scanner( new File( path ) ).useDelimiter("");
            readProgram(pr);
            pr.close();
        } catch (IOException error) {
            System.out.print(error.getMessage());
        }

    }

    // Reads a program followed by an <Eof>, <Eof> when hasNextLine() is false
    private void readProgram(Scanner input) {
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
    private void readStatement(Scanner input) throws APException {
        terminateSpaces(input);
        if( nextCharIs(input, COMMENT_CHAR ) || !input.hasNext("."))  {
            // ignore completely empty lines and lines preceded by COMMENT.

        } else if (nextCharIsLetter(input)) { // Identifier
            readAssignment(input);
            readEol(input);

        } else if (nextCharIs(input, PRINT_STATEMENT_CHAR)) { // Print statement
            readCharacter(input, PRINT_STATEMENT_CHAR);
            Set<BigInteger> set = readExpression(input);
            readEol(input);
            System.out.println( set );

        } else {
            throw new APException("'" + COMMENT_CHAR + "', '" + PRINT_STATEMENT_CHAR +
                    "', or a character was expected at the beginning of a statement!");
        }
    }

    // Reads an Expression and returns the resulting set.
    private Set<BigInteger> readExpression(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result = readTerm(input);
        terminateSpaces(input);

        while( input.hasNext("[|\\-+]") ){
            if (nextCharIs(input, SYMMETRICDIFFERENCE_OPERATOR)) {
                readCharacter(input, SYMMETRICDIFFERENCE_OPERATOR);
                terminateSpaces(input);
                result = result.symmDifference( readTerm(input) );
                terminateSpaces(input);

            } else if (nextCharIs(input, COMPLEMENT_OPERATOR)) {
                readCharacter(input, COMPLEMENT_OPERATOR);
                terminateSpaces(input);
                result = result.complement( readTerm(input) );
                terminateSpaces(input);

            } else if (nextCharIs(input, UNION_OPERATOR)) {
                readCharacter(input, UNION_OPERATOR);
                terminateSpaces(input);
                result = result.union( readTerm(input) );
                terminateSpaces(input);

            } else {
                throw new APException("Expected an additive operator!");
            }
        }

        return result;
    }

    // Reads an Assignment statement and
    // assigns the Identifier to the set that is the result of the Expression.
    private void readAssignment(Scanner input) throws APException {
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
    private void assign(Identifier id, Set<BigInteger> set) throws APException {
        //if (variables.containsKey(id)) {
        //    throw new APException("A set has already been assigned to " + id.getIdentifierName());
        //}
        variables.put(id, set);
    }

    // Reads a term consisting of a factor followed by 0 or more factors.
    // All factors are separated by a MULTIPLICATIVE_OPERATOR.
    private Set<BigInteger> readTerm(Scanner input) throws APException {
        Set<BigInteger> result = readFactor(input);
        terminateSpaces(input);

        while ( nextCharIs(input, MULTIPLICATIVE_OPERATOR) ) {
            readCharacter(input, MULTIPLICATIVE_OPERATOR);
            terminateSpaces(input);
            result = result.intersection( readFactor(input) );
            terminateSpaces(input);
        }

        return result;
    }

    // Reads a factor, and returns the resulting Set.
    // A factor is an identifier, a complex factor or a set.
    private Set<BigInteger> readFactor(Scanner input) throws APException {
        Set<BigInteger> result;

        if ( nextCharIsLetter(input) ) { // factor is an identifier
            Identifier id = readIdentifier(input);
            result = retrieveSet(id);
            terminateSpaces(input);

        } else if ( nextCharIs(input, '{') ) { // factor is a set
            result = readSet(input);

        } else if ( nextCharIs(input, '(') ) { // factor is a complex factor
            readCharacter(input, '(');
            result = readExpression(input);
            terminateSpaces(input);
            readCharacter(input, ')');

        } else {
            throw new APException("Expected a factor!" );
        }

        return result;
    }

    // Retrieves a set assigned to an Identifier
    private Set<BigInteger> retrieveSet(Identifier id) throws APException {
        if (variables.containsKey(id)) {
            return variables.get(id);
        } else {
            throw new APException("No set assigned to '" + id.getIdentifierName() + "'!");
        }
    }

    // Takes a Scanner, reads the set that is in it and returns a set.
    private Set<BigInteger> readSet(Scanner input) throws APException {
        Set<BigInteger> set = new SetImpl<>();
        readCharacter(input, '{');
        terminateSpaces(input);

        while (!nextCharIs(input, '}')) {
            set.add(readBigInteger(input));
            terminateSpaces(input);
            if ( nextCharIs(input, '}') ) {
                break;
            }
            readCharacter(input, ',');
            terminateSpaces(input);
        }

        readCharacter(input, '}');

        return set;
    }

    // Reads an identifier.
    private Identifier readIdentifier(Scanner input) throws APException {
        Identifier result = new IdentifierImpl();

        while ( nextCharIsLetter(input) || nextCharIsDigit(input) ) {
            result.add( nextChar(input) );
        }

        return result;
    }

    // The function converts a BigInteger given
    private BigInteger readBigInteger(Scanner input) throws APException {
        if ( !nextCharIsDigit(input) ) {
            throw new APException("Expected an integer!");
        }

        StringBuilder val = new StringBuilder();

        while ( nextCharIsDigit(input) ) {
            val.append( nextChar(input) );
        }

        return new BigInteger( val.toString() );
    }

    // Reads next character if it equals thee given character
    private void readCharacter(Scanner input, char ch) throws APException {
        if (!( nextCharIs(input, ch) )) {
            throw new APException("'" + ch + "'" + " was expected!");
        }
        nextChar(input);
    }

    // Removes all subsequent whitespace
    private void terminateSpaces(Scanner input) throws APException {
        while ( nextCharIs(input, ' ') ) {
            readCharacter(input, ' ');
        }
    }

    // Checks to see if there are any tokens left in the line
    private void readEol(Scanner in) throws APException {
        terminateSpaces(in);
        if ( in.findInLine("") != null ){
            //throw new APException("An <EoL> was expected");
            throw new APException("No <Eol>! Found '" + in.next() + "'");
        }
    }

    // Method to read 1 character.
    private char nextChar (Scanner in) {
        return in.next().charAt(0);
    }

    // Method to check if the next character to be read when
    // calling nextChar() is equal to the provided character.
    private boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c+""));
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a digit.
    private boolean nextCharIsDigit (Scanner in) {
        return in.hasNext("[0-9]");
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a letter.
    private boolean nextCharIsLetter (Scanner in) {
        return in.hasNext("[A-Za-z]");
    }

    public static void main(String[] args) throws IOException {

    }

}
