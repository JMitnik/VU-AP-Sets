import java.math.BigInteger;

/**
 * @structure - Unstructured
 * @elements - Elements of type E
 * @domain - Unique elements of Type E
 * @pre -
 * @post - A new empty Set has been created to be filled with elements E
 * @constructor - Set()
 *              - Copy constructor: Set(Set<E>)
 *	                <dl>
 *		                <dt><b>PRE-conditie</b><dd>		-
 *		                <dt><b>POST-conditie</b><dd> 	- A new Set<E> has been created.
 *                  </dl>
 */
interface Set<E extends Comparable> {
    /**
     * Determines the union with the given Set<E> set
     * @param set
     * @pre - The given 'set' consists of the elements of type E or is empty.
     * @post - The union with the given set has been returned as a Set<E>.
     */
    Set<E> union(Set<E> set);

    /**
     * Adds an element of type 'E' to the set.
     * @param el
     * @pre -
     * @post - The element E has been added to the set at a random position.
     */
    void addEl(E el);

    /**
     * Removes a random element from the set and returns it.
     * @pre - The set is not empty.
     * @post - The set is returned without a random element.
     */
    E remove();

    /**
     * Returns the number of elements in the set.
     * @pre -
     * @post - The number of elements in the set has been returned.
     * @return
     */
    int cardinality();

    /**
     * Determines the union with the given Set<E> set.
     * @param set
     * @pre - The given 'set' consists of the elements of type E or is empty.
     * @post - The intersection with the given set has been returned as a Set<E>.
     */
    Set<E> intersection(Set<E> set);

    /**
     * Determines the union with the given Set<E> set.
     * @param set
     * @pre - The given 'set' consists of the elements of type E or is empty.
     * @post - The relative complement with the given set been returned as a Set<E>.
     */
    Set<E> complement(Set<E> set);

    /**
     * Determines the symmetric difference with the given Set<E> set.
     * @param set
     * @pre - The given 'set' consists of the elements of type E or is empty.
     * @post - The symmetric difference with the given set been returned as a Set<E>.
     */
    Set<E> symmDifference(Set<E> set);

    /**
     * Returns a deep copy of this set.
     * @pre -
     * @post - A deep copy of 'this' set been returned as a Set<E>.
     */
    Set<E> copy();

    /**
     * Looks to see element 'el' can be found
     * @param el - Element to find in the set.
     * @pre -
     * @post - True: Element 'el' has been found in the set.
     *       - False: Element 'el' has not been found in the set.
     */
    boolean find(E el);
}
