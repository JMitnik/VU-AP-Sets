import java.math.BigInteger;

public class SetImp<E extends Comparable> implements Set<E>{
    ListInterface<E> elements;

    SetImp() {
        //todo: Usage of List: do I instantiate it as a property, extend the List, or instantiate it in the program and give it to the constructor?
        // For now, will be instantiated as a property in here
        this.elements = new List<E>();
    }

    @Override
    public void addEl(E el) {
        if (!elements.find(el)) {
            elements.insert(el);
        }
    }

    @Override
    public void remove() {
        this.elements.retrieve();
        this.elements = this.elements.remove();
    }

    @Override
    public int cardinality() {
        return this.elements.size();
    }

    @Override
    public Set intersection(Set set) {
        Set result = new SetImp();

        for (int i = 0; i < set.cardinality(); i++) {

        }

        return result;
    }

    @Override
    public Set union(Set set) {
        return null;
    }

    @Override
    public Set complement(Set set) {
        return null;
    }

    @Override
    public Set symmDifference(Set set) {
        return null;
    }
}
