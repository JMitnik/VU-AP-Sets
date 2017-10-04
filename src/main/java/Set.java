import java.math.BigInteger;

/**
 * @structure - no structure
 * @elements - Elements of type E
 * @domain -
 * @pre -
 * @post - A new empty Set has been created to be filled with elements E
 * @constructor - Set()
 */
interface Set<E extends Comparable> {

    /**
     * @pre -
     * @post - true has been returned if the set is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * @pre -
     * @post - a copy of the set has been returned.
     */
    Set<E> copy();


    /**
     * @param
     * @pre -
     * @post - the set contains element e.
     */
    void add(E e);

    /**
     * @param
     * @pre -
     * @post - the set does not contain element e.
     */
    void remove(E e);

    /**
     * @param
     * @pre -
     * @post - true has been returned if the set contains element e, false otherwise.
     */
    boolean contains(E e);

    /**
     * @pre -
     * @post - an int has been returned representing the size of the set.
     */
    int cardinality();

    /**
     * @pre -
     * @post - an int has been returned representing the size of the set.
     */
    List<E> retrieve();

    /**
     * @param
     * @pre -
     * @post - The union of this and set has been returned
     */
    Set<E> union(Set<E> set);

    /**
     * @param
     * @pre -
     * @post - The intersection of this and set has been returned
     */
    Set<E> intersection(Set<E> set);

    /**
     * @param
     * @pre -
     * @post - The complement of this and set has been returned.
     */
    Set<E> complement(Set<E> set);

    /**
     * @param
     * @pre -
     * @post - The symmetric difference of this and set has been returned
     */
    Set<E> symmDifference(Set<E> set);
}
