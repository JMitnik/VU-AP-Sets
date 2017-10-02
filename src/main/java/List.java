public class List<E extends Comparable> implements ListInterface<E>{
    Node head;
    Node current;

    private class Node {

        E data;
        Node prior,
                next;

        public Node(E d) {
            this(d, null, null);
        }

        public Node(E data, Node prior, Node next) {
            //TODO:
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }
    }

    List() {
        Node head = null;
        Node current = head;
    }

    @Override
    public boolean isEmpty() {
        return ;
    }

    @Override
    public ListInterface<E> init() {
        return null;
    }

    @Override
    public int size() {
        return countNodes(head);
    }

    /**
     *
     */
    private int countNodes(Node currentNode) {
        if (currentNode == null) {
            return 0;
        }

        return 1 + countNodes(currentNode.next);
    }

    @Override
    public ListInterface<E> insert(E d) {
        Node currentNode = head;

        return null;
    }

    @Override
    public E retrieve() {
        return null;
    }

    @Override
    public ListInterface<E> remove() {
        return null;
    }

    @Override
    public boolean find(E d) {
        return false;
    }

    @Override
    public boolean goToFirst() {
        if (size() == 0) {
            return false;
        }

        while (current.next != null) {
            current = current.next;
        }

        return true;
    }

    @Override
    public boolean goToLast() {
        if (size() == 0) {
            return false;
        }

        while (current.next != null) {
            current = current.next;
        }

        return true;
    }

    @Override
    public boolean goToNext() {
        if (current.next != null) {
            current = current.next;
            return true;
        }

        return false;
    }

    @Override
    public boolean goToPrevious() {
        if (current.prior != null) {
            current = current.prior;
            return true;
        }

        return false;
    }

    @Override
    public ListInterface<E> copy() {
        return null;
    }
}
