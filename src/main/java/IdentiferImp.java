public class IdentiferImp implements Identifier {

    private StringBuffer identifierCharacters;

    public IdentiferImp() {
        this.identifierCharacters = new StringBuffer();
    }

    @Override
    public String getIdentifier() {
        return identifierCharacters.toString();
    }

    @Override
    public void addChar(char character) {
        identifierCharacters.append(character);
    }

    @Override
    public void removeChar(int index) {
        identifierCharacters.deleteCharAt(index);
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    public boolean equals(Object ob) {
        if (ob.getClass() == this.getClass() ) {
            Identifier id = (Identifier) ob;
            return this.getIdentifier().equals( id.getIdentifier() );
        }

        return false;
    }
}
