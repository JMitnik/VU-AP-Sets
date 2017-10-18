/**
 *  @elements : Characters
 *	@structure : Linear
 *	@domain : Letters and numbers, where the first character is a letter.
 *	@constructor - Identifier();
 *	<dl>
 *		<dt><b>PRE</b><dd>   -
 *		<dt><b>POST</b><dd>  - A new empty Identifier has been created.
 * </dl>
 **/
interface Identifier {
    /**
     * Get the full value of the Identifier's name
     * @pre -
     * @post - The full character representation of the identifier has been returned as String.
     */
    String getIdentifier();

    /**
     * Adds a character to the identifier.
     * @pre -
     * @post - A character has been added to the end of the identifier.
     */
    void addChar(char character);

    /**
     * Removes the element at index 'index' of the char[].
     * @pre -
     * @post - The element at index 'index' has been removed
     */
    void removeChar(int index);

    /**
     * Returns the hashcode of the identifier.
     * @pre -
     * @post - An integer has been returned in the form of a hashCode.
     */
    int hashCode();

    /**
     * Checks if the identifier is equal to another object.
     * @pre -
     * @post - The identifier has been compared to another object to check if it
     * is the same.
     */
    boolean equals(Object o);
}
