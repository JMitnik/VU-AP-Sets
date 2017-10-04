/**
 *  @elements : -
 *	@structure : -
 *	@domain : 	-
 *	@constructor - Identifier();
 *	<dl>
 *		<dt><b>PRE</b><dd>		- The Identifier
 *		<dt><b>POST</b><dd> 	A new Identifier has been created with the given string from the constructor.
 * </dl>
 **/
interface Identifier {
    /**
     * @pre -
     * @post - the character has been added to Identifier
     */
    Identifier add(char c);

    /**
     * Get the String value of the Identifier's name
     * @pre -
     * @post - The name of the identifier has been returned as a String.
     */
    String getIdentifierName();
}