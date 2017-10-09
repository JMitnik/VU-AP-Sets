import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {
    Interpreter() {}

    //TODO Parser questions:
    //TODO: - Do we ignore whitespace, read an unlimited amount of whitespace?
    //TODO: - How do we read EoF?

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

    private void readExpression(Scanner input) throws APException {
        readTerm(input);

        while (nextCharIsAdditive(input)) {
            nextChar(input); //todo: So I read the next character, then what?
            readTerm(input);
        }
    }

    private void readTerm(Scanner input) throws APException {
        readFactor(input);

        while (nextCharIs(input, '*')) {
            nextChar(input);
            readFactor(input);
        }
    }

    private void readCharacter(Scanner input, char c) throws APException {
        if (!nextCharIs(input, c)) {
            throw new APException("The character is not proper");
        }

        nextChar(input);
    }

    //returns Set
    private void readFactor(Scanner input) throws APException {
        if (nextCharIsLetter(input)) {
            readIdentifier(input);
            //todo: checkifexists
            //todo: hashmap->get(identifier);
        } else if (nextCharIs(input, '(')) {
            readComplexFactor(input);
            //todo: return the set out of this
        } else if (nextCharIs(input, '{')) {
            readSet(input);
            //todo: return this set
        } else {
            throw new APException("An improper factor has been recognized");
        }
    }

    private void readIdentifier(Scanner input) throws APException {
         if (nextCharIsLetter(input)) {
             nextChar(input);
         } else {
             throw new APException("Identifier not proper syntax");
         }

         while (nextCharIsLetter(input) || nextCharIsDigit(input)) {
             nextChar(input);
         }

         //return new Identifier
    }

    private void readComplexFactor(Scanner input) throws APException {
        readCharacter(input, '(');
        readExpression(input);
        readCharacter(input, ')');
    }

    private void readSet(Scanner input) throws APException {
        readCharacter(input, '{');
        readRowNaturalNumbers(input);
        readCharacter(input, '}');
    }

    private void readRowNaturalNumbers(Scanner input) throws APException {
        if (nextCharIsDigit(input) || !nextCharIs(input, ' ')) {
            nextChar(input);
        }

        while (nextCharIs(input, ',')) {
            readCharacter(input, ',');
            readNaturalNumber(input);
        }
    }

    private void readNaturalNumber(Scanner input) throws APException {
        if (nextCharIsDigit(input)) {
            nextChar(input);
        } else {
            throw new APException("Non-natural number spotted in Set!");
        }
    }

    private void readAssignment(Scanner input) throws APException {
        readIdentifier(input);
        readCharacter(input, '=');
        readExpression(input);
//        readEndOfLine(input);
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

    private char nextChar (Scanner in) {
        return in.next().charAt(0);
    }

    private boolean nextCharIs(Scanner in, char c) {
        return in.hasNext(Pattern.quote(c+""));
    }

    private boolean nextCharIsDigit (Scanner in) {
        return in.hasNext("[0-9]");
    }

    private boolean nextCharIsAdditive (Scanner in) {
        return in.hasNext("[+|-]");
    }

    private boolean nextCharIsLetter (Scanner in) {
        return in.hasNext("[a-zA-Z]");
    }
}
