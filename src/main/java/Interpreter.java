import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {
    private HashMap<Identifier, Set<BigInteger>> variables;

    private static final char OPERATOR_UNION = '+';
    private static final char OPERATOR_INTERSECT = '*';
    private static final char OPERATOR_SYMMDIFFENCE = '|';
    private static final char OPERATOR_COMPLEMENT = '-';

    /**
     * Creates and reads the input of the source.
     * @param src - Source of the input.
     */
    Interpreter(InputStream src) {
        this.variables = new HashMap<>();
        Scanner in = new Scanner(src) ;
        readInput(in);
    }

    /**
     * Reads the full given input
     * @param input - The input given to the scanner
     */
    private void readInput(Scanner input) {
        while (input.hasNextLine()) {
            readLine(new Scanner(input.nextLine()).useDelimiter(""));
        }

        input.close();
    }

    /**
     * Reads a single line of the input.
     * @param input - The input given to the scanner as a line of the input.
     */
    private void readLine(Scanner input) {
        try {
            readStatement(input);
            input.close();
        }
        catch (APException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reads a line as a statement.
     * @param input - The Scanner portion of the input.
     */
    private void readStatement(Scanner input) throws APException {
        if (nextCharIs(input, '?')) {
            readPrintStatement(input);
        } else if (nextCharIsLetter(input)) {
            readAssignment(input);
        } else if (nextCharIs(input, '/')) {
            readComment(input);
        } else {
            throw new APException("Statement not recognized");
        }
    }

    /**
     * Reads a print statement.
     * @param input - The Scanner portion of the input.
     */
    private void readPrintStatement(Scanner input) throws APException {
        readCharacter(input, '?');
        Set<BigInteger> set = readExpression(input);
        readEndOfLine(input);

        printSet(set);
    }

    /**
     * Assigns a 'set' to an 'identifier' and stores them inside the interpreter's 'variables' property.
     * @param input - The Scanner portion of the input.
     */
    private void readAssignment(Scanner input) throws APException {
        Identifier identifier = readIdentifier(input);
        readCharacter(input, '=');
        Set<BigInteger> set = readExpression(input);
        readEndOfLine(input);

        variables.put(identifier, set);
    }

    /**
     * Interprets and ignores a comment's content.
     * @param input - The Scanner portion of the input.
     */
    private void readComment(Scanner input) throws APException {
        // read '/' then do nothing thus ignoring it.
        readCharacter(input, '/');

       // while (input.hasNext()) {
       //     input.next();
       // }

       //readEndOfLine(input);
    }

    /**
     * Reads an expression of at least one set and additive operations with 0 or more sets.
     * @param input - The Scanner portion of the input.
     * @return Set<BigInteger> - The value of the expression.
     */
    private Set<BigInteger> readExpression(Scanner input) throws APException {
        Set<BigInteger> set = readTerm(input);

        while (nextCharIsAdditive(input)) {
            set = getAdditiveSet(input, set);
        }

        return set;
    }

    /**
     * Prints all elements of a given set 'set'.
     * @param set - A set with elements BigInteger.
     */
    private void printSet(Set<BigInteger> set) {
        System.out.println(set.toString());
        /*Set<BigInteger> copySet = set.copy();

        if (copySet.cardinality() != 0) {
            System.out.printf("%s", copySet.remove().toString());
        }

        while (!(copySet.cardinality() == 0)) {
            System.out.print(' ');
            System.out.printf("%s", copySet.remove().toString());
        }

        System.out.printf("\n");*/
    }

    /**
     * Performs the proper additive operation with the given set and the next operation.
     * @param input - The scanner portion of an expression after reading at least one term.
     * @param set - The first set of the term.
     * @return Set<BigInteger> - The value of the additive operation.
     */
    private Set<BigInteger> getAdditiveSet(Scanner input, Set<BigInteger> set) throws APException {
        if (operatorIsUnion(input)) {
            readCharacter(input, OPERATOR_UNION);
            return set.union(readTerm(input));

        } else if (operatorIsSymmDifference(input)) {
            readCharacter(input, OPERATOR_SYMMDIFFENCE);
            return set.symmDifference(readTerm(input));

        } else if (operatorIsComplement(input)) {
            readCharacter(input, OPERATOR_COMPLEMENT);
            return set.complement(readTerm(input));

        } else {
            throw new APException("Operator is unknown");
        }
    }

    /**
     * Reads a term of at least one set and multiplicative operations with 0 or more sets.
     * @param input - The Scanner portion of the input.
     * @return Set<BigInteger> - The value of the term evaluation.
     */
    private Set<BigInteger> readTerm(Scanner input) throws APException {
        Set<BigInteger> result = readFactor(input);

        while (nextCharIs(input, OPERATOR_INTERSECT)) {
            readCharacter(input, OPERATOR_INTERSECT);
            result = result.intersection(readFactor(input));
        }

        return result;
    }

    /**
     * Reads the factor as a set, complex factor, or set of an identifier.
     * @param input - The Scanner portion of the input.
     * @return Set<BigInteger> - Returns the next found factor as a Set.
     */
    private Set<BigInteger> readFactor(Scanner input) throws APException {
        if (nextCharIsLetter(input)) {
            return findSet(readIdentifier(input));

        } else if (nextCharIs(input, '(')) {
            return readComplexFactor(input);

        } else if (nextCharIs(input, '{')) {
            return readSet(input);

        } else {
            throw new APException("An improper factor has been recognized");
        }
    }

    /**
     * Reads the complex factor and returns the set resulting from it.
     * @param input - The Scanner portion of the input.
     * @return Set<BigInteger> - The complex factor as a set.
     */
    private Set<BigInteger> readComplexFactor(Scanner input) throws APException {
        readCharacter(input, '(');
        Set<BigInteger> set = readExpression(input);
        readCharacter(input, ')');
        return set;
    }

    /**
     * Reads the next encountered identifier in the input.
     * @param input - The Scanner portion of the input.
     * @return Identifier - The encountered identifier.
     */
    private Identifier readIdentifier(Scanner input) throws APException {
        Identifier result = new IdentiferImp();

        if (nextCharIsLetter(input)) {
            result.addChar(nextChar(input));
        } else {
            throw new APException("Identifier not proper syntax");
        }

        while (nextCharIsLetter(input) || nextCharIsDigit(input)) {
            result.addChar(nextChar(input));
        }

        return result;
    }

    /**
     * Assumes and checks that the identifier has been stored as a variable, and returns the matching set.
     * @param identifier - Identifier to be found in the 'variables' property.
     * @return Set<BigInteger> - Returns the set belonging to the identifier.
     */
    private Set<BigInteger> findSet(Identifier identifier) throws APException {
        if (variables.containsKey(identifier)) {
            return variables.get(identifier);
        } else {
            throw new APException("No set assigned to variable '" + identifier.getIdentifier() + "'.");
        }
    }

    /**
     * Reads the encountered set from the input and returns it as a Set<BigInteger>.
     * @param input - The Scanner portion of the input.
     * @return Set<BigInteger> - The read set.
     */
    private Set<BigInteger> readSet(Scanner input) throws APException {
        readCharacter(input, '{');
        Set<BigInteger> set = readRowNaturalNumbers(input);
        readCharacter(input, '}');
        return set;
    }

    /**
     * Reads a row of characters and returns this row as a Set<BigInteger>.
     * @param input - The Scanner portion of the input.
     * @return Set<BigInteger> - The read set.
     */
    private Set<BigInteger> readRowNaturalNumbers(Scanner input) throws APException {
        Set<BigInteger> set = new SetImp<>();

        if (nextCharIs(input, '}')) {
            return set;
        }

        //if (nextCharIsDigit(input)) {
            set.addEl(readNaturalNumber(input));
        //}

        //if (nextCharIs(input, ',')) {
        //    checkFirstElement(set);
        //}

        while (nextCharIs(input, ',')) {
            readCharacter(input, ',');
            set.addEl(readNaturalNumber(input));
            //ignoreWhiteSpace(input);
        }

        return set;
    }

    /**
     * Checks on index > 0 of an input of the set if the set already has a member added.
     * @param set - The set to check.
     */
    private void checkFirstElement(Set<BigInteger> set) throws APException {
        if (set.cardinality() == 0) {
            throw new APException("First element of set is missing!");
        }
    }

    /**
     * Reads the next encountered selection of subsequent digits as one single natural number,
     * checks if the number is not prefixed with a 0.
     * @param input - The Scanner portion of the input.
     * @return BigInteger - The natural number encountered in the input.
     */
    private BigInteger readNaturalNumber(Scanner input) throws APException {
        StringBuffer result = new StringBuffer();

       if (nextCharIs(input, '-')) {
           throw new APException("Only natural numbers allowed!");
       } else if (!nextCharIsDigit(input)) {
           throw new APException("Non-digit found!");

       } else if (nextCharIs(input, '0')) {
           readCharacter(input, '0');
           return new BigInteger("0");
       } else
           result.append(nextChar(input));

        //checkZeroPrefix(input, result.toString());

        while (nextCharIsDigit(input)) {
            result.append(nextChar(input));
        }

        return new BigInteger(result.toString());
    }

    /**
     * Checks after building the first element of a string if it will be a prefix to a natural number.
     * @param input - The Scanner portion of the input.
     * @param s - The string built with the first digit of a natural number.
     */
    private void checkZeroPrefix(Scanner input, String s) throws APException {
        if (s.equals("0") && nextCharIsDigit(input)) {
            throw new APException("Missing curly brackets: no 0-prefix allowed!");
        }
    }

    /**
     * Continues checking the input when called until another element has been encountered.
     * @param in - The Scanner portion of the input.
     */
    private void ignoreWhiteSpace (Scanner in) throws APException {
        while (in.hasNext("\\s")) {
            nextChar(in);
        }
    }

    /**
     * Checks if the end of the line has anything else.
     * @param input - The Scanner portion of the input.
     */
    private void readEndOfLine(Scanner input) throws APException {
        if (input.hasNext()) {
            throw new APException("End of line expected!");
        }
    }

    // One character reading/checking methods
    /**
     * Returns the next character of the input.
     * @param in - The Scanner portion of the input.
     * @return char - The next character of the input.
     */
    private char nextChar (Scanner in) throws APException {
        return in.next().charAt(0);
    }

    /**
     * Checks if the next character is a digit.
     * @param in - The Scanner portion of the input.
     * @return boolean - True if the next character is a digit, else false.
     */
    private boolean nextCharIsDigit (Scanner in) throws APException {
        return in.hasNext("[0-9]");
    }

    /**
     * Checks if the next character is an additive operator[+, |, -].
     * @param in - The Scanner portion of the input.
     * @return boolean - True if the next character is an additive operator, else false.
     */
    private boolean nextCharIsAdditive (Scanner in) throws APException {
        ignoreWhiteSpace(in);
        return in.hasNext("[" + OPERATOR_UNION + OPERATOR_SYMMDIFFENCE + OPERATOR_COMPLEMENT + "]");
    }

    /**
     * Checks if the next character is a letter.
     * @param in - The Scanner portion of the input.
     * @return boolean - True if the next character is a letter, else false.
     */
    private boolean nextCharIsLetter (Scanner in) throws APException {
        return in.hasNext("[a-zA-Z]");
    }

    /**
     * Reads and assumes the given character and the next character of the scanner to be the same.
     * @param input - The Scanner portion of the input.
     * @param c - The character which should be read.
     */
    private void readCharacter(Scanner input, char c) throws APException {
        ignoreWhiteSpace(input);

        if (!nextCharIs(input, c)) {
            throw new APException("Unexpected character!");
        }

        nextChar(input);
        ignoreWhiteSpace(input);
    }

    /**
     * Checks if the next element of the input is given character 'c'.
     * @param in - The Scanner portion of the input.
     * @param c - The character to compare and check.
     * @return boolean - True if c matches next character of the input, else false.
     */
    private boolean nextCharIs(Scanner in, char c) throws APException {
        ignoreWhiteSpace(in);
        return in.hasNext(Pattern.quote(c+""));
    }

    /**
     * Checks if the next element of the input is a union operator.
     * @param input - The scanner portion of a presuming additive operator.
     * @return boolean - True if operator is union, else false.
     */
    private boolean operatorIsUnion(Scanner input) throws APException {
        return nextCharIs(input, OPERATOR_UNION);
    }

    /**
     * Checks if the next element of the input is a symmetric-difference operator.
     * @param input - The scanner portion of a presuming symmetric-difference operator.
     * @return boolean - True if operator is symmetric-difference, else false.
     */
    private boolean operatorIsSymmDifference(Scanner input) throws APException {
        return nextCharIs(input, OPERATOR_SYMMDIFFENCE);
    }

    /**
     * Checks if the next element of the input is a complement operator.
     * @param input - The scanner portion of a presuming complement operator.
     * @return boolean - True if operator is complement, else false.
     */
    private boolean operatorIsComplement(Scanner input) throws APException {
        return nextCharIs(input, OPERATOR_COMPLEMENT);
    }
}
