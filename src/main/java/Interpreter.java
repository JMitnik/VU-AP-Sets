import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {
    private Hashtable hashTable;

    Interpreter(Hashtable hashTable) {
        this.hashTable = hashTable;
    }
    //todo: Insert whitespace ignorer in 'right' places
    public void readStatement(Scanner input) throws APException {
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
        readExpression(input);
    }

    private Set readExpression(Scanner input) throws APException {
        Set set = readTerm(input);

        while (nextCharIsAdditive(input)) {
            nextChar(input);
            readTerm(input);
        }

        return set;
    }

    private Set readTerm(Scanner input) throws APException {
        Set result = readFactor(input);

        while (nextCharIs(input, '*')) {
            nextChar(input);
            result = result.intersection(readFactor(input));
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
        if (hashTable.containsKey(identifier)) {
            return (Set) hashTable.get(identifier);
        } else {
            throw new APException("Can't find Identifier");
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
        readCharacter(input, '(');
        Set set = readExpression(input);
        readCharacter(input, ')');
        return set;
    }

    private Set readSet(Scanner input) throws APException {
        readCharacter(input, '{');
        Set<BigInteger> set = readRowNaturalNumbers(input); //todo should I not add the set after all this is over?
        readCharacter(input, '}');
        return set;
    }

    private Set readRowNaturalNumbers(Scanner input) throws APException {
        Set<BigInteger> set = new SetImp();

        if (nextCharIsDigit(input)) {
            set.addEl(BigInteger.valueOf(readNaturalNumber(input)));
        }

        while (nextCharIs(input, ',')) {
            readCharacter(input, ',');
            set.addEl(BigInteger.valueOf(readNaturalNumber(input)));
        }

        return set;
    }

    private Integer readNaturalNumber(Scanner input) throws APException {
        if (nextCharIsDigit(input)) {
            return Character.getNumericValue(nextChar(input));
        } else {
            throw new APException("Non-natural number spotted in Set!");
        }
    }

    private void readAssignment(Scanner input) throws APException {
        Identifier identifier = readIdentifier(input);
        readCharacter(input, '=');
        Set set = readExpression(input);
//        readEndOfLine(input);
        this.hashTable.put(identifier, set);
    }

    private void readComment(Scanner input) throws APException {
        readCharacter(input, '/');

        while (input.hasNext()) {
            input.next();
        }

//        readEndOfLine(input);
    }

    private void readEndOfLine(Scanner input) {

    }

    private void readEndOfFile(Scanner input) throws APException {

    }

    private char nextChar (Scanner in) throws APException {
        return in.next().charAt(0);
    }

    private boolean nextCharIs(Scanner in, char c) throws APException {
        ignoreWhiteSpace(in);
        return in.hasNext(Pattern.quote(c+""));
    }

    private boolean nextCharIsDigit (Scanner in) throws APException {
        ignoreWhiteSpace(in);
        return in.hasNext("[0-9]");
    }

    private void ignoreWhiteSpace (Scanner in) throws APException {
        while (in.hasNext("\\s")) {
            nextChar(in);
        }
    }

    private boolean nextCharIsAdditive (Scanner in) throws APException {
        ignoreWhiteSpace(in);
        return in.hasNext("[+|-]");
    }

    private boolean nextCharIsLetter (Scanner in) throws APException {
        ignoreWhiteSpace(in);
        return in.hasNext("[a-zA-Z]");
    }
}
