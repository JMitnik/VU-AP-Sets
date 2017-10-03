/**
 *  @elements : Characters
 *	@structure : Linear
 *	@domain : Letters and numbers, where the first character is a letter.
 *	@constructor - Identifier(char);
 *	<dl>
 *		<dt><b>PRE</b><dd>		- The Identifier
 *		<dt><b>POST</b><dd>  - A new empty Identifier has been created with the first char.
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
     * @post -
     */
    void addChar(char character);

    /**
     * Removes the element at index 'index' of the char[].
     * @pre -
     * @post -
     */
    void removeChar(int index);

    /**
     * @pre -
     * @post - An integer has been returned in the form of a hashCode.
     */
    int hashCode();

    /**
     * @pre -
     * @post - The identifier has been compared to another object to check if it
     * is the same.
     */
    boolean equals(Object o);
}
