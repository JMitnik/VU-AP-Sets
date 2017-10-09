import java.util.Scanner;
import java.util.regex.Pattern;

public class Parser {
    Parser() {}

    //TODO Parser questions:
    //TODO: - Do we ignore whitespace, parse an unlimited amount of whitespace?
    //TODO: - During parsing, we store/build the factors. Do we return these somehow to the main HashTable, or do we instantiate HashTable here?
    //TODO: - How do we parse EoF?

    public void parseStatement(Scanner input) throws APException {
        if (nextCharIs(input, '?')) {
            parsePrintStatement(input);
        } else if (nextCharIsLetter(input)) {
            parseAssignment(input);
        } else if (nextCharIs(input, '/')) {
            parseComment(input);
        } else {
            throw new APException("Statement not recognized");
        }
    }

    private void parsePrintStatement(Scanner input) throws APException {
        nextChar(input);
        parseExpression(input);
    }

    private void parseExpression(Scanner input) throws APException {
        parseTerm(input);

        while (nextCharIsAdditive(input)) {
            nextChar(input); //todo: So I read the next character, then what?
            parseTerm(input);
        }
    }

    private void parseTerm(Scanner input) throws APException {
        parseFactor(input);

        while (nextCharIs(input, '*')) {
            nextChar(input);
            parseFactor(input);
        }
    }

    private void parseCharacter(Scanner input, char c) throws APException {
        if (!nextCharIs(input, c)) {
            throw new APException("The character is not proper");
        }

        nextChar(input);
    }

    private void parseFactor(Scanner input) throws APException {
        if (nextCharIsLetter(input)) {
            parseIdentifier(input);
        } else if (nextCharIs(input, '(')) {
            parseComplexFactor(input);
        } else if (nextCharIs(input, '{')) {
            parseSet(input);
        } else {
            throw new APException("An improper factor has been recognized");
        }
    }

    private void parseIdentifier(Scanner input) throws APException {
         if (nextCharIsLetter(input)) {
             nextChar(input);
         } else {
             throw new APException("Identifier not proper syntax");
         }

         while (nextCharIsLetter(input) || nextCharIsDigit(input)) {
             nextChar(input);
         }
    }

    private void parseComplexFactor(Scanner input) throws APException {
        parseCharacter(input, '(');
        parseExpression(input);
        parseCharacter(input, ')');
    }

    private void parseSet(Scanner input) throws APException {
        parseCharacter(input, '{');
        parseRowNaturalNumbers(input);
        parseCharacter(input, '}');
    }

    private void parseRowNaturalNumbers(Scanner input) throws APException {
        if (nextCharIsDigit(input) || !nextCharIs(input, ' ')) {
            nextChar(input);
        }

        while (nextCharIs(input, ',')) {
            parseNaturalNumber(input);
        }
    }

    private void parseNaturalNumber(Scanner input) throws APException {
        if (nextCharIsDigit(input)) {
            nextChar(input);
        } else {
            throw new APException("Non-natural number spotted in Set!");
        }
    }

    private void parseAssignment(Scanner input) throws APException {
        parseIdentifier(input);
        parseCharacter(input, '=');
        parseExpression(input);
//        parseEndOfLine(input);
    }

    private void parseComment(Scanner input) throws APException {
        parseCharacter(input, '/');

        while (input.hasNext()) {
            input.next();
        }

//        parseEndOfLine(input);
    }

    private void parseEndOfLine(Scanner input) {

    }

    private void parseEndOfFile(Scanner input) throws APException {

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
