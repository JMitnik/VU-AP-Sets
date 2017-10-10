import java.math.BigInteger;
public class List<E extends Comparable> implements ListInterface<E>{

    private class Node {

        E data;
        Node prior,
                next;

        public Node(E d) {
            this(d, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data;
            this.prior = prior;
            this.next = next;
        }

    }

    private int size;
    private Node current;

    public List() {
        this.size = 0;
        this.current = null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public ListInterface<E> init() {
        this.size = 0;
        this.current = null;
        return this;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ListInterface<E> insert(E d) {
        Node nd = new Node(d, null, null);
        if (this.isEmpty()) {
            size++;
            current = nd;
            return this;//break "loop"?
        } else if (this.current.next == null) {
            current.next = nd;
            nd.prior = current;
            nd.next = null;
        } else if (this.current.prior == null) {
            current.prior = nd;
            nd.prior = null;
            nd.next = current;
        } else {
            // element is inserted AFTER current element
            current.next.prior = nd;
            nd.prior = current;
            nd.next = current.next;
            current.next = nd;
        }
        size++;
        current = nd;
        return this;
    }

    @Override
    public E retrieve() {
        return this.current.data;
    }

    @Override
    public ListInterface<E> remove() {
        System.out.println(current.data);
        if(size == 1) {
            current = null;
        } else if (current.prior == null){
            current.next.prior = null;
            current = current.next;
        } else if (current.next == null) {
            current.prior.next = null;
            current = current.prior;
        } else {
            current.next.prior = current.prior;
            current.prior.next = current.next;
            current = current.next; // could also be prior...
        }
        size--;
        return this;
    }

    @Override
    public boolean find(E d) {
        if( this.isEmpty() ) {
            return false;
        }
        this.goToLast();
        return this.contains(current, d);
    }

    private boolean contains (Node l, E d) {
        if (l.prior == null) {
            return l.data.equals(d);
        }
        if(l.data.equals(d)){
            current = l;
        }
        return (l.data.equals(d) || contains(l.prior, d));
    }

    @Override
    public boolean goToFirst() {
        if ( this.isEmpty() ) {
            return false;
        }
        while (current.prior != null) {
            current = current.prior;
        }
        return true;
    }

    @Override
    public boolean goToLast() {
        if ( this.isEmpty() ) {
            return false;
        }
        while (current.next != null) {
            current = current.next;
        }
        return true;
    }

    public Boolean hasNext() {
        return current.next != null;
    }

    @Override
    public boolean goToNext() {
        if ( this.isEmpty() || current.next == null ) {
            return false;
        }
        current = current.next;
        return true;
    }

    @Override
    public boolean goToPrevious() {
        if ( this.isEmpty() || current.prior == null ) {
            return false;
        }
        current = current.prior;
        return true;
    }

    @Override
    public ListInterface<E> copy() {
        if ( this.isEmpty() ) {
            return new List<>();
        }
        this.goToFirst();
        ListInterface<E> tmp = new List<>();

        while (current.next != null) {
            tmp.insert( current.data );
            current = current.next;
        }
        tmp.insert(current.data);
        return tmp;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        } else {
            StringBuilder str = new StringBuilder("{ ");
            this.goToFirst();
            while (current.next != null) {
                str = str.append(current.data).append(" , ");
                current = current.next;
            }
            str.append(current.data).append(" }");
            return str.toString();
        }
    }


    public static void main(String[] argv) {
        List<BigInteger> list = new List<>();
        System.out.println( list.toString() );
        ListInterface<BigInteger> list3 = list.copy();
        System.out.println( list3.isEmpty() );
        list3.insert( BigInteger.valueOf(9) );
        list3.remove();
        list.insert( BigInteger.valueOf(4) ).insert( BigInteger.valueOf(5)).insert( BigInteger.valueOf(6)).insert( BigInteger.valueOf(7)).insert( BigInteger.valueOf(8)).insert( BigInteger.valueOf(9) );
        System.out.println( list.toString() );
        ListInterface<BigInteger> list2 = list.copy();
        System.out.println( list.find( BigInteger.valueOf(0)) );
        list.remove();
        list2.goToLast();
        list2.insert( BigInteger.valueOf(7) );
        list2.goToFirst();
        System.out.println( "There is a previous: " + list2.goToPrevious() );
        list2.insert( BigInteger.valueOf(8) );
        list2.goToNext();
        list2.insert( BigInteger.valueOf(100));
        System.out.println( list2.retrieve() );

    }
}