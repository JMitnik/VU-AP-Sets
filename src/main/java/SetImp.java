import java.math.BigInteger;

public class SetImp<E extends Comparable> implements Set<E>{
    private ListInterface<E> elements;

    SetImp() {
        this.elements = new List<E>();
    }

    SetImp(SetImp<E> set) {
        this.elements = set.elements.copy();
    }

    @Override
    public void addEl(E el) {
        if (!find(el)) {
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
    public boolean find(E matchEl) {
        return this.elements.find(matchEl);
    }

    @Override
    public Set intersection(Set set) {
        Set<E> result = new SetImp();
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
        return new SetImp<>(this);
    }
}
