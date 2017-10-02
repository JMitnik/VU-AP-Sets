import java.math.BigInteger;

/**
 * @structure - Linear
 * @elements - Elements of type E
 * @domain - Unique elements of Type E
 * @pre -
 * @post - A new empty Set has been created to be filled with elements E
 * @constructor - Set()
 */
interface Set<E extends Comparable<E>> {
    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> union(Set<E> set);

    /**
     * Adds an element 'E' to the set.
     * @param el
     * @pre -
     * @post - The element E has been added to the set at a random position.
     */
    void add(E el);

    /**
     * Removes a random element from the set.
     * @pre - The set is not empty.
     * @post - The set is returned without a random element.
     */
    void remove();

    /**
     * Gets random element E from the set.
     * @pre - The set is not empty.
     * @post - An element
     */
    E getRandomElement();

    /**
     * Returns the number of elements in the set.
     * @pre -
     * @post - The number of elements in the set has been returned.
     * @return
     */
    int cardinality();

    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> intersection(Set<E> set);

    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> complement(Set<E> set);

    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> symmDifference(Set<E> set);
}
