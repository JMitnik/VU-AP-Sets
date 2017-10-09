/**	@elements : objects of type E
 *	@structure : linear
 *	@domain : 	The elements in the list are sorted monotonically increasing.
 *				All rows of elements of type E are valid values for a list.
 *       		For every non-empty list the reference current is pointing to an
 *				element in the list.
 *	@constructor - List();
 *	<dl>
 *		<dt><b>PRE-conditie</b><dd>		-
 *		<dt><b>POST-conditie</b><dd> 	The new List-object is the empty list.
 * </dl>
 **/

public abstract interface ListInterface<E extends Comparable> {

    /**	@precondition -
     *  @postcondition - FALSE: list is not empty.
     *  				TRUE:  list is empty.
     **/
    public abstract boolean isEmpty();

    /** @precondition  -
     *	@postcondition - list-POST is empty and has been returned.
     **/
    public abstract ListInterface<E> init();

    /**	@precondition  -
     *	@postcondition - The number of elements has been returned.
     **/
    public abstract int size();

    /** @precondition  -
     *	@postcondition - Element d has been added to List-PRE.
     *    				current points to the newly added element.
     *   				list-POST has been returned.
     **/
    public abstract ListInterface<E> insert(E d);


    /** @precondition  - The list is not empty.
     *	@postcondition -The value of the current element has been returned.
     */
    public abstract E retrieve();


    /** @precondition  - The list is not empty.
     * 	@postcondition - The current element of list-PRE is not present in list-POST.
     * 	    			current-POST points to
     *    					- if list-POST is empty
     *   						null
     *   					- if list-POST is not empty
     *     						if current-PRE was the last element of list-PRE
     *     							the last element of list-POST
     *     						else
     *     							the element after current-PRE
     *  				list-POST has been returned.
     **/
    public abstract ListInterface<E> remove();


    /** @precondition  -
     *	@postcondition - TRUE:  The list contains the element d.
     *	     			current-POST points to the first element in list that
     *	     			contains the element d.
     *     				FALSE: list does not contain the element d.
     *	     			current-POST points to
     *	      				- if list-POST is empty
     *                    		null
     *	      				- if the first element in list > d:
     *                    		the first element in list
     *        				else
     *	    					the last element in list with value < d
     **/
    public abstract boolean find(E d);


    /** @precondition  -
     *	@postcondition - FALSE: list is empty
     *    				TRUE:  current points to the first element
     **/
    public abstract boolean goToFirst();


    /**	@precondition  -
     *	@postcondition - FALSE: list is empty
     *     				TRUE:  current points to the last element
     */
    public abstract boolean goToLast();


    /** @precondition  -
     *	@postcondition - FALSE: list is empty or current points to the last element
     *     				TRUE:  current-POST points to the next element of current-PRE
     */
    public abstract boolean goToNext();


    /** @precondition  -
     *	@postcondition - FALSE: list is empty of current points to the first element
     *     				TRUE:  current-POST points to the prior element of current-PRE
     */
    public abstract boolean goToPrevious();

    /**
     * @precondition -
     * @postcondition A deep copy of the list has been returned.
     */
    public abstract ListInterface<E> copy();
}
