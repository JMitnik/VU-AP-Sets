import java.math.BigInteger;

public class SetImp<E extends Comparable> implements Set<E>{
    private ListInterface<E> elements;

    //TODO: Sets
    //TODO - Can we copy a set by using an extra constructor?

    SetImp() {
        //todo: Usage of List: do I instantiate it as a property, extend the List, or instantiate it in the program and give it to the constructor?
        this.elements = new List<E>();
    }

    SetImp(SetImp<E> set) {
        this.elements = set.elements.copy();
    }

    @Override
    public void addEl(E el) {
        if (!find(el)) { //TODO: Assumes we can use the list implementation and find property?
            elements.insert(el);
        }
    }

    @Override
    public E remove() {
        E el = this.elements.retrieve();
        this.elements = this.elements.remove();

        return el;
    }

    @Override
    public int cardinality() {
        return this.elements.size();
    }

    @Override
    public boolean find(E matchEl) { //todo: If we can't use the find method of List, we do it this way.
        Set<E> workingSet = this.copy();

        while (workingSet.cardinality() != 0) {
            E retrievedEl = workingSet.remove(); //todo: again casting works, why?

            if (retrievedEl.equals(matchEl)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set intersection(Set set) {
        Set<E> result = new SetImp(); //todo: do we find a way to instantiate a new 'Interface'
        Set<E> workingSetLhs = this.copy();
        Set<E> workingSetRhs = set.copy();

        while (workingSetLhs.cardinality() != 0) {
            E retrievedEl = workingSetLhs.remove();

            if (workingSetRhs.find(retrievedEl)) {
                result.addEl(retrievedEl);
            }
        }

        return result;
    }


    @Override
    public Set<E> union(Set set) {
        Set<E> result = new SetImp();
        Set<E> workingSetLhs = this.copy();
        Set<E> workingSetRhs = set.copy();

        while (workingSetLhs.cardinality() != 0) {
            result.addEl(workingSetLhs.remove());
        }

        while (workingSetRhs.cardinality() != 0) {
            result.addEl(workingSetRhs.remove());
        }

        return result;
    }

    @Override
    public Set<E> complement(Set set) {
        Set<E> result = new SetImp();
        Set<E> workingSetLhs = this.copy();
        Set<E> workingSetRhs = set.copy();

        while (workingSetRhs.cardinality() != 0) {
            E el = workingSetRhs.remove();

            if (!workingSetLhs.find(el)) {
                result.addEl(el);
            }
        }

        return result;
    }

    @Override
    public Set<E> symmDifference(Set set) {
        Set<E> leftCom = this.complement(set);
        Set<E> rightCom = set.complement(this);

        return leftCom.union(rightCom);
    }

    @Override
    public Set<E> copy() {
        return new SetImp(elements.copy()); //todo: Copy with instance?
    }
}
