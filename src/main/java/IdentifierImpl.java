public class IdentifierImpl implements Identifier {
    private char[] characters;
    private int numberOfElements;
    private static final int INITIAL_SIZE = 10;

    public IdentifierImpl() {
        characters = new char[ INITIAL_SIZE ];
        numberOfElements = 0;
    }

    public IdentifierImpl add (char ch) {
        if( characters.length == numberOfElements) {
            this.extendArray();
        }
        characters [ numberOfElements++ ] = ch;
        return this;
    }

    private void extendArray() {
        char[] tmp = new char[ 2* characters.length ];
        for(int i = 0; i < characters.length; i++) {
            tmp[i] = characters[i];
        }
        characters = tmp;
    }

    public String getIdentifierName(){
        return String.valueOf(characters);
    }

    @Override
    public boolean equals(Object ob) {
        if (ob.getClass() == this.getClass() ) {
            Identifier id = (Identifier) ob;
            return this.getIdentifierName().equals( id.getIdentifierName() );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.valueOf(characters).hashCode();
    }

    public static void main(String[] argv) {
        Identifier Id = new IdentifierImpl();
        Identifier a = new IdentifierImpl();
        a.add('l').add('p').add('k');
        Id.add('l').add('p').add('k');
        System.out.println( Id.getIdentifierName() );
        System.out.println( Id.equals(a) );
    }
}