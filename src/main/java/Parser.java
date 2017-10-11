
//import java.util.regex.Pattern;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.*;

public class Parser {
    private static final char MULTIPLICATIVE_OPERATOR = '*';

    // Additive operators
    private static final char SYMMETRICDIFFERENCE_OPERATOR = '|';
    private static final char UNION_OPERATOR = '+';
    private static final char COMPLEMENT_OPERATOR = '-';

    private static HashMap<Identifier, Set<BigInteger>> variables = new HashMap<>();

    // reads a program followed by an <Eof>, <Eof> when hasNextLine() is false
    private static void readProgram(Scanner input) throws APException {
        readStatement(input);

        while( input.hasNextLine() ) {
            input.nextLine();
            readStatement(input);
        }
    }

    private static void readStatement(Scanner input) throws APException {
        terminateSpaces(input);

        if( nextCharIs(input, '/') ) {
            readCharacter(input, '/');

        } else if (nextCharIsLetter(input)) {
            readAssignment(input);
            //readEol(input);

        } else if (nextCharIs(input, '?')) {
            readCharacter(input, '?');
            System.out.println( readExpression(input) );
            //readEol(input);

        } else {
            throw new APException("'/', '?', or a character (\"[a-zA-Z]\") was expected at the beginning of a statement!");
        }
    }

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

    private static void assign(Identifier id, Set<BigInteger> set) throws APException {
        if (variables.containsKey(id)) {
            // Does not allow override, maybe we should?
            throw new APException("A set has already been assigned to " + id.getIdentifierName());
        }
        variables.put(id, set);
    }

    // Reads a term consisting of a factor followed by 0 or more factors. All factors are separated by
    // a MULTIPLICATIVE_OPERATOR, preceded and followed by 0 or more whitespaces.
    private static Set<BigInteger> readTerm(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result = readFactor(input);
        terminateSpaces(input);

        // Recursion condition
        if ( nextCharIs(input, MULTIPLICATIVE_OPERATOR) ) {
            readCharacter(input, MULTIPLICATIVE_OPERATOR);
            result = result.intersection( readTerm(input) );
        }

        return result;
    }

    // factor = identifier | complex_factor | set ; A factor is an identifier,
    // a complex factor or a set.
    private static Set<BigInteger> readFactor(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result;

        if ( nextCharIsLetter(input) ) {
            Identifier id = readIdentifier(input);

            if (variables.containsKey(id)) {
                result = variables.get(id);
            } else {
                throw new APException("No variable assigned to '" + id.getIdentifierName() + "'!");
            }

        } else if ( nextCharIs(input, '{') ) {
            result = readSet(input);

        } else if ( nextCharIs(input, '(') ) {
            readCharacter(input, '(');
            terminateSpaces(input);

            result = readExpression(input);

            terminateSpaces(input);
            readCharacter(input, ')');

        } else {
            throw new APException("Expected a factor! Found: '" + input.next() + "'" );
        }

        return result;
    }

    //set = '{' row_natural_numbers '}'
    //A set is a row of natural numbers between accolades.
    //row_natural_numbers = [ natural_number { ',' natural_number } ]
    //A row of natural numbers is empty or a summation of one or more natural numbers separated by commas.
    private static Set<BigInteger> readSet(Scanner input) throws APException {
        readCharacter(input, '{');
        terminateSpaces(input);

        if ( nextCharIs(input, '}') ) {
            readCharacter(input, '}');
            return new SetImpl<>();
        }

        Set<BigInteger> set = new SetImpl<>();
        set.add( readBigInteger(input) );

        return addValues(input, set);
    }

    private static Set<BigInteger> addValues(Scanner input, Set<BigInteger> set) throws APException {
        terminateSpaces(input);

        // Termination condition
        if ( nextCharIs(input, '}') ) {
            readCharacter(input, '}');
            return set;
        }

        readCharacter(input, ',');
        terminateSpaces(input);

        set.add( readBigInteger(input) );

        return addValues(input, set);

    }

    private static Identifier readIdentifier(Scanner input) throws APException {
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



    private static BigInteger readBigInteger(Scanner input) {
        StringBuilder val = new StringBuilder();

        while ( nextCharIsDigit(input) ) {
            val.append( nextChar(input) );
        }

        return new BigInteger( val.toString() );
    }

    private static void readCharacter(Scanner input, char ch) throws APException {
        if (!( nextCharIs(input, ch) )) {
            throw new APException("'" + ch + "'" + " was expected! Found '" +nextChar(input) + "'");
        }
        nextChar(input);
    }

    // checks if the line has ended. If there is more input, throws an APException.
    private  static void readEol(Scanner input) throws APException {
        terminateSpaces(input);

        //if ( input.hasNext() ) {
        //    throw new APException("Expected an <eol>, found: '" + nextChar(input) + "'");
        //}
    }

    private static void terminateSpaces(Scanner input) throws APException {
        while ( nextCharIs(input, ' ') ) {
            readCharacter(input, ' ');
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


    public static void main(String[] argv) {
        System.out.println("Set Calculator");
        Scanner in = new Scanner(System.in).useDelimiter("") ;

        while ( in.hasNextLine() ) {
            try {
                readStatement( in );
            } catch (APException error) {
                System.out.println("\u001B[31m" + error.getMessage() + "\u001B[0m");
            }
            in.nextLine();
        }

        in.close();
    }
}
