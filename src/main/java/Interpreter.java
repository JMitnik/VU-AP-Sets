import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Interpreter {

    private static HashMap<Identifier, Set> variables = new HashMap<>();

    public static void assign(Identifier id, Set set) throws APException {
        if (variables.containsKey(id)) {
            throw new APException("A set has already been assigned to " + id.getIdentifierName());
        }
        variables.put(id, set);
    }

    //TODO fix problems with Set and Set<E>
    public static Set readExpression(Scanner input) throws APException {
        Set result = readTerm(input);


        while( input.hasNext() ){
            if (Parser.nextCharIs(input, '|')) {
                Parser.readCharacter(input, '|');
                result = result.symmDifference( readTerm(input) );
            } else if (Parser.nextCharIs(input, '−')) {
                Parser.readCharacter(input, '-');
                result = result.complement( readTerm(input) );
            } else if (Parser.nextCharIs(input, '+')) {
                Parser.readCharacter(input, '+');
                result = result.union( readTerm(input) );
            } else {
                throw new APException("Expected an additive operator! Found: " + Parser.nextChar(input) );
            }
        }

        return result;
    }

    // term = factor { multiplicative_operator factor } ; A term is a factor,
    // followed by 0 or more factors. All factors are separated by a multiplicative-operator.
    public static Set readTerm(Scanner input) throws APException {
        Set result = readFactor(input);

        while(Parser.nextCharIs(input,'*')) {
            Parser.readCharacter(input, '*');
            result = result.intersection( readTerm(input) );
        }

        return result;
    }

    // factor = identifier | complex_factor | set ; A factor is an identifier,
    // a complex factor or a set.
    public static Set readFactor(Scanner input) throws APException {
        System.out.printf("in the readFactor method\n");
        System.out.println( Parser.nextCharIsLetter(input) );
        System.out.println( Parser.nextCharIs(input, '{'));
        System.out.println( Parser.nextCharIs(input, '('));
        Set result;

        if (Parser.nextCharIsLetter(input)) {
            System.out.println("in the readFactor method if 1");
            result = variables.get( Parser.readIdentifier(input) );
        } else if ( Parser.nextCharIs(input, '{') ) {
            System.out.println("in the readFactor method if 2");
            result = readSet(input);
            //complex_factor = '(' expression ')' ; A complex factor is an expression between round brackets.
        } else if ( Parser.nextCharIs(input, '(') ) {
            System.out.println("in the readFactor method if 3");
            Parser.readCharacter(input, '(');
            result = readExpression(input);
            Parser.readCharacter(input, ')');
        } else {
            System.out.println("in the readFactor method else");
            throw new APException("Expected a factor! Found: " + input.nextLine());
        }

        return result;
    }

    //set = '{' row_natural_numbers '}'
    //A set is a row of natural numbers between accolades.
    //row_natural_numbers = [ natural_number { ',' natural_number } ]
    //A row of natural numbers is empty or a summation of one or more natural numbers separated by commas.
    public static Set readSet(Scanner input) throws APException {
        Parser.readCharacter(input, '{');
        Set result = new SetImpl();

        if (Parser.nextCharIs(input, '}')) {
            return result;
        } else result.add( input.nextBigInteger() );

        while (Parser.nextCharIs(input, ',')) {
            Parser.readCharacter(input, ',');
            result.add( input.hasNextBigInteger() );
        }

        Parser.readCharacter(input, '}');

        return result;
    }

    public static void main(String[] argv) {
        try{
            System.out.println(readSet( new Scanner("{ 5, 99, 22, 44}")).toString() ) ;} catch (APException error) {};
        /*System.out.println("Set Calculator");
        System.out.print("Input:\n> ");

        Scanner in = new Scanner(System.in);
        while ( in.hasNext() ) {
            try {
                Parser.readStatement(in);
            } catch (APException error) {
                System.out.println("\u001B[31m" + error + "\u001B[0m");
                //throw new Error(error);
            }
            in.nextLine();
            System.out.printf("> ");
        } */

        // in.close(); // in.hasNext() is never false? since scanner never closes while in while loop??
    }


}
