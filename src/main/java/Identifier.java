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
     * Get the String value of the Identifier's name
     * @pre -
     * @post - The name of the identifier has been returned as a String.
     */
    String getIdentifierName();

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
