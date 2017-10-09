public class List<E extends Comparable> implements ListInterface<E>{

    //TODO: List
    // TODO: - Do we return 'this' when it asks for ListInterface<E>?

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
     *
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
     * @pre - List is not empty
     * @return
     */
    private boolean isAtStart() {
        return current.prior == null;
    }

    /**
     * @pre - List is not empty
     * @return
     */
    private boolean isAtEnd() {
        return current.next == null;
    }

    /**
     *
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

        //todo: what do I return for this?
        return this;
    }

    private void insertInOrder(E d) {
        if (d.compareTo(current.data) > 0 ) {
            insertLargerEl(d);
        } else {
            insertSmallerEqualEl(d);
        }
    }

    private void insertLargerEl(E d) {
        if (!isAtEnd()) {
            current.next = current.next.prior = new Node(d, current, current.next);
        } else {
            current.next = new Node(d, current, null);
        }

        goToNext();
    }

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

        return false;
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
        List result = new List();

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
