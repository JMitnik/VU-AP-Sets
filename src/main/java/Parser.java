
//import java.util.regex.Pattern;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.HashMap;
import java.util.regex.*;

public class Parser {

    private static HashMap<Identifier, Set<BigInteger>> variables = new HashMap<>();

    // TODO fix this
    public static void readProgram(Scanner input) throws APException {
        readStatement(input);
        while( input.hasNextLine() ) {
            input.nextLine();
            readStatement(input);
        }
    }

    public static void readStatement(Scanner input) throws APException {
        terminateSpaces(input);

        if( nextCharIs(input, '/') ) {
            readCharacter(input, '/');

        } else if (nextCharIsLetter(input)) {
            readAssignment(input);

        } else if (nextCharIs(input, '?')) {
            readCharacter(input, '?');
            System.out.println( readExpression(input) );
            //readEol(input);

        } else {
            throw new APException("'/', '?', or a character (\"[a-zA-Z]\") was expected at the beginning of a statement!");
        }
    }

    public static void assign(Identifier id, Set<BigInteger> set) throws APException {
        if (variables.containsKey(id)) {
            // Does not allow override, maybe we should?
            throw new APException("A set has already been assigned to " + id.getIdentifierName());
        }
        variables.put(id, set);
    }

    public static Set<BigInteger> readExpression(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result = readTerm(input);


        while( input.hasNext("[|\\-+]") ){
            if (nextCharIs(input, '|')) {
                readCharacter(input, '|');
                terminateSpaces(input);
                result = result.symmDifference( readTerm(input) );

            } else if (nextCharIs(input, '-')) {
                readCharacter(input, '-');
                terminateSpaces(input);
                result = result.complement( readTerm(input) );

            } else if (nextCharIs(input, '+')) {
                readCharacter(input, '+');
                terminateSpaces(input);
                result = result.union( readTerm(input) );

            } else {
                throw new APException("Expected an additive operator! Found: '" + nextChar(input) + "'");
            }
        }

        return result;
    }

    // term = factor { multiplicative_operator factor } ; A term is a factor,
    // followed by 0 or more factors. All factors are separated by a multiplicative-operator.
    public static Set<BigInteger> readTerm(Scanner input) throws APException {
        terminateSpaces(input);
        Set<BigInteger> result = readFactor(input);

        while(nextCharIs(input,'*')) {
            readCharacter(input, '*');
            terminateSpaces(input);
            result = result.intersection( readTerm(input) );
        }

        return result;
    }

    // factor = identifier | complex_factor | set ; A factor is an identifier,
    // a complex factor or a set.
    public static Set<BigInteger> readFactor(Scanner input) throws APException {
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
    public static Set<BigInteger> readSet(Scanner input) throws APException {
        readCharacter(input, '{');
        terminateSpaces(input);

        if ( nextCharIs(input, '}') ) {
            readCharacter(input, '}');
            terminateSpaces(input);

            return new SetImpl<>();
        }

        StringBuilder val = new StringBuilder("");
        while(nextCharIsDigit(input)) {
            val.append( nextChar(input) );
        }

        Set<BigInteger> set = new SetImpl<>();
        set.add( new BigInteger(val.toString()) );

        return addValues(input, set);
    }

    private static Set<BigInteger> addValues(Scanner input, Set<BigInteger> set) throws APException {
        terminateSpaces(input);

        if ( nextCharIs(input, '}') ) {
            readCharacter(input, '}');
            return set;
        } else {
            readCharacter(input, ',');
            terminateSpaces(input);
            StringBuilder val = new StringBuilder("");

            while(nextCharIsDigit(input)) {
                val.append( nextChar(input) );
            }

            set.add( new BigInteger(val.toString()) );

            return addValues(input, set);
        }
    }


    public static void readAssignment(Scanner input) throws APException {
        Identifier id = readIdentifier(input);
        terminateSpaces(input);

        readCharacter(input, '=');

        terminateSpaces(input);
        Set<BigInteger> set = readExpression(input);
        terminateSpaces(input);
        readEol(input);

        assign(id, set);
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
            throw new APException("'" + ch + "'" + " was expected! Found '" +nextChar(input) + "'");
        }
        nextChar(input);
    }

    // checks if the line has ended. If there is more input, throws an APException.
    public  static void readEol(Scanner input) throws APException {
        terminateSpaces(input);


        //if ( input.hasNext() ) {
        //    throw new APException("Expected an <eol>, found: '" + nextChar(input) + "'");
        //}
    }

    // TODO implement this
    public static BigInteger readBigInteger(Scanner input) {
       StringBuilder val = new StringBuilder();

       while ( nextCharIsDigit(input) ) {
           val.append( nextChar(input) );
       }

       return new BigInteger( val.toString() );
    }

    public static void terminateSpaces(Scanner input) throws APException {
        while ( nextCharIs(input, ' ')) {
            readCharacter(input, ' ');
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
    public static boolean nextCharIsDigit (Scanner in) {
        return in.hasNext("[0-9]");
    }

    // Method to check if the next character to be read when
    // calling nextChar() is a letter.
    public static boolean nextCharIsLetter (Scanner in) {
        return in.hasNext("[A-Za-z]");
    }


    public static void main(String[] argv) {
        System.out.println("Set Calculator");
        System.out.print("Input:\n> ");

        Scanner in = new Scanner(System.in);
        in.useDelimiter("");

        while ( in.hasNextLine() ) {
            try {
                readStatement(in);
            } catch (APException error) {
                System.out.println("\u001B[31m" + error + "\u001B[0m");
                //throw new Error(error);
            }
            in.nextLine();

            System.out.printf("> ");
        }

        in.close(); // in.hasNext() is never false? since scanner never closes while in while loop??
    }
}
