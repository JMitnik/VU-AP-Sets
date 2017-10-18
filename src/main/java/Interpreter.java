import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {
    private HashMap<Identifier, Set> hashMap;

    Interpreter() {
        this.hashMap = new HashMap<>();
    }

    void readFile(Scanner input) {
        while (input.hasNextLine()) {
            readLine(new Scanner(input.nextLine()).useDelimiter(""));
        }
    }

    private void readLine(Scanner input) {
        try {
            readStatement(input);
        }
        catch (APException e) {
            System.out.println(e.toString());
        }
    }

    //todo: Insert whitespace ignorer in 'right' places
    private void readStatement(Scanner input) throws APException {
        ignoreWhiteSpace(input);

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

    private void readPrintStatement(Scanner input) throws APException {
        readCharacter(input, '?');
        ignoreWhiteSpace(input);
        Set<BigInteger> set = readExpression(input);
        readEndOfLine(input);
        printSet(set);
    }

    private void printSet(Set<BigInteger> set) {
        printElements(set);
    }

    private void printElements(Set<BigInteger> set) {
        Set<BigInteger> copySet = set.copy();
        if (copySet.cardinality() != 0) {
            System.out.printf("%s", copySet.remove().toString());
        }

        while (!(copySet.cardinality() == 0)) {
            System.out.print(' ');
            System.out.printf("%s", copySet.remove().toString());
        }

        System.out.printf("\n");
        System.out.printf(">: ");
    }

    private Set readExpression(Scanner input) throws APException {
        Set set = readTerm(input);
        ignoreWhiteSpace(input);

        //todo: Parse this properly
        while (nextCharIsAdditive(input)) {
            if (operatorIsUnion(input)) {
                nextChar(input);
                set = set.union(readTerm(input));
            } else if (operatorIsSymmDifference(input)) {
                nextChar(input);
                set = set.symmDifference(readTerm(input));
            } else if (operatorIsComplement(input)) {
                nextChar(input);
                set = set.complement(readTerm(input));
            } else {
                throw new APException("Operator is unknown");
            }
            ignoreWhiteSpace(input);
        }

        return set;
    }

    private boolean operatorIsUnion(Scanner input) throws APException {
        return nextCharIs(input, '+');
    }

    private boolean operatorIsSymmDifference(Scanner input) throws APException {
        return nextCharIs(input, '|');
    }

    private boolean operatorIsComplement(Scanner input) throws APException {
        return nextCharIs(input, '-');
    }

    private Set readTerm(Scanner input) throws APException {
        Set result = readFactor(input);
        ignoreWhiteSpace(input);

        while (nextCharIs(input, '*')) {
            nextChar(input);
            result = result.intersection(readFactor(input));
            ignoreWhiteSpace(input);
        }

        return result;
    }

    private void readCharacter(Scanner input, char c) throws APException {
        ignoreWhiteSpace(input);

        if (!nextCharIs(input, c)) {
            throw new APException("The character is not proper");
        }

        nextChar(input);
    }

    private Set readFactor(Scanner input) throws APException {
        ignoreWhiteSpace(input);

        if (nextCharIsLetter(input)) {
            return findIdentifier(readIdentifier(input));
        } else if (nextCharIs(input, '(')) {
            return readComplexFactor(input);
            //todo: return the set out of this
        } else if (nextCharIs(input, '{')) {
            return readSet(input);
        } else {
            throw new APException("An improper factor has been recognized");
        }
    }

    private Set findIdentifier(Identifier identifier) throws APException {
        if (hashMap.containsKey(identifier)) {
            return (Set) hashMap.get(identifier);
        } else {
            throw new APException("Can't find identifier");
        }
    }

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

    private Set readComplexFactor(Scanner input) throws APException {
        ignoreWhiteSpace(input);
        readCharacter(input, '(');
        Set set = readExpression(input);
        readCharacter(input, ')');
        return set;
    }

    private Set readSet(Scanner input) throws APException {
        ignoreWhiteSpace(input);
        readCharacter(input, '{');
        Set<BigInteger> set = readRowNaturalNumbers(input);
        readCharacter(input, '}');
        return set;
    }

    private Set readRowNaturalNumbers(Scanner input) throws APException {
        Set<BigInteger> set = new SetImp();
        ignoreWhiteSpace(input);

        if (nextCharIsDigit(input)) {
            set.addEl(readNaturalNumber(input));
        }

        ignoreWhiteSpace(input);

        if (nextCharIs(input, ',')) {
            checkFirstElement(set);
        }

        while (nextCharIs(input, ',')) {
            readCharacter(input, ',');
            ignoreWhiteSpace(input);
            set.addEl(readNaturalNumber(input));
            ignoreWhiteSpace(input);
        }

        return set;
    }

    private void checkFirstElement(Set<BigInteger> set) throws APException {
        if (set.cardinality() == 0) {
            throw new APException("First element of set is missing!");
        }
    }

    private BigInteger readNaturalNumber(Scanner input) throws APException {
        StringBuffer result = new StringBuffer();

        if (!nextCharIsDigit(input)) {
            throw new APException("Non-digit spotted!");
        } else {
            result.append(nextChar(input));
        }

        checkZeroPrefix(input, result.toString());

        while (nextCharIsDigit(input)) {
            result.append(nextChar(input));
        }

        return new BigInteger(result.toString());
    }

    private void checkZeroPrefix(Scanner input, String s) throws APException {
        if (s.equals("0") && nextCharIsDigit(input)) {
            throw new APException("Missing curly brackets: you can't prefix 0 to another number!");
        }
    }

    private void readAssignment(Scanner input) throws APException {
        Identifier identifier = readIdentifier(input);
        readCharacter(input, '=');
        Set set = readExpression(input);
        readEndOfLine(input);
        this.hashMap.put(identifier, set);
    }

    private void readComment(Scanner input) throws APException {
        readCharacter(input, '/');

        while (input.hasNext()) {
            input.next();
        }

        readEndOfLine(input);
    }

    private void readEndOfLine(Scanner input) throws APException {
        if (input.hasNext()) {
            throw new APException("End of line expected!");
        }
    }

    private char nextChar (Scanner in) throws APException {
        return in.next().charAt(0);
    }

    private boolean nextCharIs(Scanner in, char c) throws APException {
        return in.hasNext(Pattern.quote(c+""));
    }

    private boolean nextCharIsDigit (Scanner in) throws APException {
        return in.hasNext("[0-9]");
    }

    private void ignoreWhiteSpace (Scanner in) throws APException {
        while (in.hasNext("\\s")) {
            nextChar(in);
        }
    }

    private boolean nextCharIsAdditive (Scanner in) throws APException {
        return in.hasNext("[+|-]");
    }

    private boolean nextCharIsLetter (Scanner in) throws APException {
        return in.hasNext("[a-zA-Z]");
    }
}
