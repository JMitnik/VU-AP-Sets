public class SetImpl<E extends  Comparable> implements Set<E> {
    // maybe extend Set with the List class
    // class SetImpl<E> extends List<> implements Set<E> {
    private List<E> list = new List<>();

    public SetImpl( List<E> list ) {
        this.list = list;
    }

    public SetImpl() {
        this( new List<>() );
    }

    public void add(E e) {
        if (list.find(e)) {
            return;
        }
        list.insert(e);
    }

    public int cardinality() {
        return list.size();
    }

    public void remove(E e) {
        list.find(e);
        list.remove();
    }

    public boolean contains(E e) {
        return list.find(e);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public List<E> retrieve() {
        return this.list;
    }

    // TODO why doesnt ListInterface work??
    public Set<E> copy() {
        return new SetImpl<>( (List<E>) this.list.copy() );
    }

    public String toString() {
        return this.retrieve().toString();
    }

    public Set<E> union(Set<E> set) {
        if ( list.isEmpty() ) {
            return set.copy();
        } else if ( set.isEmpty() ) {
            return this.copy();

        } else {
            List<E> comlist = set.retrieve();
            comlist.goToFirst();
            Set<E> union = this.copy();
            int i = 0;
            while( i < comlist.size() ) {
                union.add( comlist.retrieve() );
                comlist.goToNext();
                i++;
            }
            return union;
        }
    }

    public Set<E> intersection(Set<E> set) {
        if ( set.isEmpty() || this.isEmpty() ) {
            return new SetImpl<>();
        }

        Set<E> intersection = new SetImpl<>();

        this.retrieve().goToFirst();
        while ( this.retrieve().hasNext() ) {
            if ( set.contains( this.retrieve().retrieve() ) ) {
                intersection.add(this.retrieve().retrieve() );
            }
            this.retrieve().goToNext();
        }

        if (set.contains( this.retrieve().retrieve() ) ) {
            // have to check the last element because the while loop stops before checking the last element.
            intersection.add(this.retrieve().retrieve() );
        }

        return intersection;
    }

    public Set<E> complement(Set<E> set) {
        if (set.isEmpty()) {
            return this.copy();
        } else if (this.isEmpty()) {
            return new SetImpl<>();

        } else {
            Set<E> complement = new SetImpl<>();
            ListInterface<E> tlist = this.list.copy();
            tlist.goToFirst();

            while (tlist.hasNext()) {
                if (!(set.contains( tlist.retrieve() ))) {
                    complement.add( tlist.retrieve() );
                }
                tlist.goToNext();
            }

            // have to check the last element because the while loop stops before checking the last element.
            if (!(set.contains( tlist.retrieve() ) )) {
                complement.add( tlist.retrieve() );
            }

            return complement;
        }

    }

    public Set<E> symmDifference(Set<E> set) {
        return this.union(set).complement( this.intersection(set) );
    }

    public static void main(String[] argv) {
    }
}
