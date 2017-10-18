public class List<E extends Comparable<E>> implements ListInterface<E>{

    private Node current;

    private class Node {

        E data;
        Node prior,
                next;

        public Node(E d) {
            this(d, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }
    }

    List() {
        this.current = null;
    }

    @Override
    public boolean isEmpty() {
        return current == null;
    }

    @Override
    public ListInterface<E> init() {
        current = null;
        return this;
    }

    @Override
    public int size() {
        Node currentHolder = current;
        goToFirst();
        int length = countNodes();
        current = currentHolder;
        return length;
    }

    /**
     * Counts the number of nodes in the list.
     * @return int
     */
    private int countNodes() {
        if (current == null) {
            return 0;
        } else if (current.next == null) {
            return 1;
        } else {
            goToNext();
            return 1 + countNodes();
        }
    }

    /**
     * Checks if 'current' is the first element of the List.
     * @return boolean - True if first element, else false.
     */
    private boolean isAtStart() {
        return current.prior == null;
    }

    /**
     * Checks if 'current' is the last element of the List.
     * @return boolean - True if last element, else false.
     */
    private boolean isAtEnd() {
        return current.next == null;
    }

    /**
     * Moves 'current' to the appropriated sorted position for value 'd' in the list.
     * @param d - Element to be a
     */
    private void goToOrderIndex(E d) {
        goToFirst();

        while (d.compareTo(current.data) > 0 && !isAtEnd()) {
            goToNext();
        }
    }

    @Override
    public ListInterface<E> insert(E d) {
        if (isEmpty()) {
            current = new Node(d, null, null);
        } else {
            goToOrderIndex(d);
            insertInOrder(d);
        }

        return this;
    }

    /**
     * Inserts value 'd' before or after 'current', based on the value of 'current.data' and 'd'.
     * @param d
     */
    private void insertInOrder(E d) {
        if (d.compareTo(current.data) > 0 ) {
            insertLargerEl(d);
        } else {
            insertSmallerEqualEl(d);
        }
    }

    /**
     * Creates a new node for element 'd' after 'current'.
     * @param d
     */
    private void insertLargerEl(E d) {
        if (!isAtEnd()) {
            current.next = current.next.prior = new Node(d, current, current.next);
        } else {
            current.next = new Node(d, current, null);
        }

        goToNext();
    }

    /**
     * Creates a new node for element 'd' before 'current'.
     * @param d
     */
    private void insertSmallerEqualEl(E d) {
        if (!isAtStart()) {
            current.prior = current.prior.next = new Node(d, current.prior, current);
        } else {
            current.prior = new Node(d, null, current);
        }

        goToPrevious();
    }

    @Override
    public E retrieve() {
        return current.data;
    }

    @Override
    public ListInterface<E> remove() {
        if (size() == 1) {
            current = null;
        } else {
            if (isAtStart()) {
                goToNext();
                current.prior = null;
            }
            else if (isAtEnd()) {
                goToPrevious();
                current.next = null;
            } else {
                current.prior.next = current.next;
                current.next.prior = current.prior;
                goToNext();
            }
        }

        return this;
    }

    @Override
    public boolean find(E d) {
        if (isEmpty()) {
            return false;
        }

        goToOrderIndex(d);
        return current.data.equals(d);
    }

    @Override
    public boolean goToFirst() {
        if (isEmpty()) {
            return false;
        }

        while (!isAtStart()) {
            current = current.prior;
        }

        return true;
    }

    @Override
    public boolean goToLast() {
        if (isEmpty()) {
            return false;
        }

        while (!isAtEnd()) {
            current = current.next;
        }

        return true;
    }

    @Override
    public boolean goToNext() {
        if (isEmpty()) {
            return false;
        }

        if (current.next != null) {
            current = current.next;
            return true;
        }

        return false;
    }

    @Override
    public boolean goToPrevious() {
        if (isEmpty()) {
            return false;
        }

        if (current.prior != null) {
            current = current.prior;
            return true;
        }

        return false;
    }

    @Override
    public ListInterface<E> copy() {
        List<E> result = new List<>();

        if (!isEmpty()) {
            goToFirst();

            while (!isAtEnd()) {
                result.insert(retrieve());
                goToNext();
            }

            result.insert(retrieve());
            return result;
        } else {
            return result.init();
        }
    }
}
