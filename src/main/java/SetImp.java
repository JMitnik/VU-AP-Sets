import java.math.BigInteger;

public class SetImp<E extends Comparable> implements Set<E>{
    private ListInterface<E> elements;

    //TODO: General, can I insert methods like getElements in general, or is that cheating?

    SetImp() {
        //todo: Usage of List: do I instantiate it as a property, extend the List, or instantiate it in the program and give it to the constructor?
        // For now, will be instantiated as a property in here
        this.elements = new List<E>();
    }

    SetImp(ListInterface<E> elements) {
        this.elements = elements;
    }

    @Override
    public void addEl(E el) {
        if (!elements.find(el)) { //TODO: Assumes we can use the list implementation and find property?
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
        Set workingSet = this.copy();

        while (workingSet.cardinality() != 0) {
            E retrievedEl = (E) workingSet.remove(); //todo: again casting works, why?

            if (retrievedEl.equals(matchEl)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set intersection(Set set) {
        Set result = new SetImp(); //todo: do we find a way to instantiate a new 'Interface'
        Set workingSetLhs = this.copy();
        Set workingSetRhs = set.copy();

        while (workingSetLhs.cardinality() != 0) {
            E retrievedEl = (E) workingSetLhs.remove();

            if (workingSetRhs.find(retrievedEl)) {
                result.addEl(retrievedEl);
            }
        }

        return result;
    }


    @Override
    public Set union(Set set) {
        Set result = new SetImp();
        Set workingSetLhs = this.copy();
        Set workingSetRhs = set.copy();

        while (workingSetLhs.cardinality() != 0) {
            result.addEl(workingSetLhs.remove());
        }

        while (workingSetRhs.cardinality() != 0) {
            result.addEl(workingSetRhs.remove());
        }

        return result;
    }

    @Override
    public Set complement(Set set) {
        Set result = new SetImp();
        Set workingSetLhs = this.copy();
        Set workingSetRhs = set.copy();

        while (workingSetRhs.cardinality() != 0) {
            E el = (E) workingSetLhs.remove();

            if (!workingSetLhs.find(el)) {
                result.addEl(el);
            }
        }

        return result;
    }

    @Override
    public Set symmDifference(Set set) {
        Set leftCom = this.complement(set);
        Set rightCom = set.complement(this);

        return leftCom.union(rightCom);
    }

    @Override
    public Set<E> copy() {
        return new SetImp(elements.copy()); //todo: Copy with instance?
    }
}
