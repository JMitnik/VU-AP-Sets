import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
/**
 * Created by Sebastian on 08/08/15.
 */
public class ListTest {

    @Before
    public void setUp() {

        // Add any maintenance which is necessary to set up your tests.
    }

    @Test
    public void testIsEmpty() {
        // Test an empty list.
        List<Letter> list = new List<>();
        Assert.assertTrue("New list should be empty", list.isEmpty());

        list.insert(new Letter('a'));
        Assert.assertFalse("Adding one element should return false.", list.isEmpty());

        list.remove();
        Assert.assertTrue("Removing should make list empty again.", list.isEmpty());

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testInit() {
        List<Letter> list = new List<>();

        // Create an empty list with init.
        list.init();
        Assert.assertTrue("Init on empty list should return an empty list", list.isEmpty());

        // Add item, init should still be empty.
        list.insert(new Letter('a'));
        list.init();
        Assert.assertTrue("Init on non-empty list should return an empty list", list.isEmpty());

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testSize() {
        List<Letter> list = new List<>();

        Assert.assertEquals("Empty list should be size 0", 0, list.size());

        // Insert one item
        list.insert(new Letter('a'));
        Assert.assertEquals("List of one element should have size 1", 1, list.size());

        // Add 200 items to the list.
        for (int i = 0; i < 200; i++) {
            list.insert(new Letter('a'));
        }
        Assert.assertEquals("Adding many elements should result in a long list", 201, list.size());

        // Remove 1 item -> 200 items left
        list.remove();
        Assert.assertEquals("Removing one item should decrement the size", 200, list.size());

        // Init should empty the list.
        list.init();
        Assert.assertEquals("Init should set size to zero", 0, list.size());

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testInsert() {
        List<Letter> list = new List<>();

        /* Insert first item
         * Inserting into empty list is an edge case.
         */
        Letter c = new Letter('c');
        list.insert(c);
        Assert.assertEquals("Inserted element should be in list", c, list.retrieve());

        // Append second item
        Letter f = new Letter('f');
        list.insert(f);

        list.goToLast();
        Assert.assertEquals("Insert should order larger elements later in list", f, list.retrieve());

        list.goToFirst();
        Assert.assertEquals("Previous elements should still be in list", c, list.retrieve());


        // Insert in front of list
        // Test that the list is sorted correctly when inserting a smaller item.
        Letter a = new Letter('a');
        list.insert(a);
        list.goToFirst();
        Assert.assertEquals("Insert should order smaller elements earlier in list", a, list.retrieve());


        // Insert at the end.
        // Test that the list is sorted correctly when inserting a larger item.
        Letter k = new Letter('k');
        list.goToLast();
        list.insert(k);
        Assert.assertEquals(k, list.retrieve());


        // Insert between two items.
        // The order has to be preserved when inserting an item between existing items.
        //Letter b = new Letter('b');
        //list.insert(b);
        //list.goToFirst();
        //Assert.assertEquals(a, list.retrieve());
        //list.goToNext();
        //Assert.assertEquals(b, list.retrieve());
        //list.goToNext();
        //Assert.assertEquals(c, list.retrieve());

        // TODO: You can add more of your own tests. e.g.:
        // Insert duplicate item.
    }

    @Test
    public void testRetrieve() {

        List<Letter> list = new List<>();

        Letter z = new Letter('z');
        list.insert(z);

        Letter letter = list.retrieve();
        Assert.assertEquals("Retrieve should return an equal object", z, letter);

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testRemove() {
        List<Letter> list = new List<>();
        Letter a = new Letter('a');
        Letter b = new Letter('b');
        Letter c = new Letter('c');
        Letter d = new Letter('d');
        list.insert(a);
        list.insert(b);
        list.insert(c);
        list.insert(d);
        // Remove an element in the middle
        list.goToFirst();
        list.goToNext();
        list.remove();

        Assert.assertEquals(c, list.retrieve());

        // Remove last element in list
        list.goToLast();
        list.remove();
        Assert.assertEquals(c, list.retrieve());


        // Remove on list with size 1
        list.remove();
        list.remove();
        try {
            Assert.assertNull(list.retrieve()); // Inconsistent specification. Undefined behaviour for retrieve on empty list.
        } catch (NullPointerException e) {
        }

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testFind() {

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testgoToFirst() {

        List<Letter> list = new List<>();

        // Test on empty list
        Assert.assertFalse(list.goToFirst());

        Letter a = new Letter('a');
        Letter b = new Letter('b');
        Letter c = new Letter('c');
        Letter d = new Letter('d');
        list.insert(a);
        list.insert(b);
        list.insert(c);
        list.insert(d);

        // Test on list with some elements.
        Assert.assertTrue(list.goToFirst());

        Assert.assertEquals(a, list.retrieve());

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testgoToLast() {
        List<Letter> list = new List<>();

        // Test on empty list
        Assert.assertFalse(list.goToLast());

        Letter a = new Letter('a');
        Letter b = new Letter('b');
        Letter c = new Letter('c');
        Letter d = new Letter('d');
        list.insert(a);
        list.insert(b);
        list.insert(c);
        list.insert(d);

        // Test on list with some elements.
        Assert.assertTrue(list.goToLast());
        Assert.assertEquals(d, list.retrieve());

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testgoToNext() {
        List<Letter> list = new List<>();

        // Test on empty list
        Assert.assertFalse(list.goToNext());

        Letter a = new Letter('a');
        Letter b = new Letter('b');
        Letter c = new Letter('c');
        Letter d = new Letter('d');
        list.insert(a);
        list.insert(b);
        list.insert(c);
        list.insert(d);

        // Test on last element
        Assert.assertFalse(list.goToNext());
        Assert.assertEquals(d, list.retrieve());


        // Test on first
        list.goToFirst();
        Assert.assertTrue(list.goToNext());
        Assert.assertEquals(b, list.retrieve());

        // TODO: You can add more of your own tests.
    }

    @Test
    public void testgoToPrevious() {

        List<Letter> list = new List<>();

        // Test on empty list
        Assert.assertFalse(list.goToNext());

        Letter a = new Letter('a');
        Letter b = new Letter('b');
        Letter c = new Letter('c');
        Letter d = new Letter('d');
        list.insert(a);
        list.insert(b);
        list.insert(c);
        list.insert(d);

        // Test on last element
        list.goToLast();
        Assert.assertTrue(list.goToPrevious());
        Assert.assertEquals(c, list.retrieve());

        // Test on first
        list.goToFirst();
        Assert.assertFalse(list.goToPrevious());
        Assert.assertEquals(a, list.retrieve());

        // TODO: You can add more of your own tests.
    }


    /**
     * Represents a comparable and clonable Letter.
     *
     * This internal class is only used for testing.
     * It is independent of any of your Implementations.
     *
     * If you write your own tests you may also use your own
     * Implementations (i.e., of Identifier).
     */
    private class Letter implements Comparable<Letter>, Cloneable {

        private char letter;

        public Letter(char c){
            this.letter = c;
        }

        public int compareTo(Letter l) {
            return this.letter - l.letter;
        }

        public Letter clone() {
            return new Letter(this.letter);
        }

        /*
         * Tests whether o is the same as this Letter.
         *
         * For assertEquals() to work an equals() method is necessary.
         *
         * Adapt this method for any of your classes that you use in assertEquals(). 
         */
        public boolean equals(Object o){

            // First clause: Test whether o is null before treating it as an object.
            // Second clause: Test whether o is of correct type.
            //                Change this type when copying to another class.
            if(o != null && o.getClass() == getClass()) {

                // Now we know that o is not null and has the same type as this.

                // Do any calculation to determine whether o and this are the same Letter.
                // In this case, Letter implements Comparable, so we can use compareTo().
                return this.compareTo((Letter)o) == 0;
            }

            // Since o was null or not of type Letter, we can safely conclude
            // that o does not equal this.
            return false;
        }
    }
}