import java.math.BigInteger;

/**
 * @structure - Linear
 * @elements - Elements of type E
 * @domain - All natural numbers
 * @pre -
 * @post - A new empty Set has been created to be filled with elements E
 * @constructor - Set()
 */
interface Set<E> {
    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> union(Set set);

    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> intersection(Set set);

    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> complement(Set set);

    /**
     * @param set
     * @pre -
     * @post -
     */
    Set<E> symmDifference(Set set);
}
