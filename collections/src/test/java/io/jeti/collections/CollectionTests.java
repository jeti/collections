package io.jeti.collections;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Test;

public class CollectionTests {

    private java.util.Collection<String>    collection;
    private java.util.Collection<String>    collectionCopy;

    private Unmodifiable.Collection<String> ucollection;

    /** This is an Immutable Collection, but descends from unmodifiable */
    private Unmodifiable.Collection<String> icollection;

    @Test
    public void sets() throws Exception {
        runAll(new HashSet<>());
    }

    @Test
    public void lists() throws Exception {
        runAll(new ArrayList<>());
    }

    /**
     * Run all of the available tests on the specified type of container.
     */
    private void runAll(java.util.Collection<String> container) throws Exception {
        reset(container);
        contains();
        reset(container);
        isEmpty();
        reset(container);
        size();
        reset(container);
        toCollection();
    }

    /**
     * Reset the test parameters for tests on the specified type of containers.
     */
    public void reset(java.util.Collection<String> container) {

        if (container instanceof java.util.Set) {

            collection = new HashSet<>();
            fill(collection);
            collectionCopy = new HashSet<>(collection);
            ucollection = new Unmodifiable.Set<>((java.util.Set<String>) collection);
            icollection = new Immutable.Set<>(collection);

        } else if (container instanceof java.util.List) {

            collection = new ArrayList<>();
            fill(collection);
            collectionCopy = new ArrayList<>(collection);
            ucollection = new Unmodifiable.List<>((java.util.List<String>) collection);
            icollection = new Immutable.List<>(collection);

        }
    }

    /**
     * Fill the specified container with the test data.
     */
    private void fill(java.util.Collection<String> container) {
        container.add("Bob");
        container.add("Nancy");
        container.add("Tim");
    }

    public void contains() throws Exception {
        for (String item : collection) {
            assertTrue(ucollection.contains(item));
            assertTrue(icollection.contains(item));
        }
        collection.clear();
        for (String item : collectionCopy) {
            assertFalse(ucollection.contains(item));
            assertTrue(icollection.contains(item));
        }
    }

    public void isEmpty() throws Exception {
        assertFalse(ucollection.isEmpty());
        assertFalse(icollection.isEmpty());
        collection.clear();
        assertTrue(ucollection.isEmpty());
        assertFalse(icollection.isEmpty());
    }

    public void size() throws Exception {
        assertTrue(collection.size() == ucollection.size());
        assertTrue(collection.size() == icollection.size());
        collection.remove("Bob");
        assertTrue(collection.size() == ucollection.size());
        assertTrue(collection.size() == icollection.size() - 1);
        collection.clear();
        assertTrue(collection.size() == ucollection.size());
        assertTrue(collectionCopy.size() == icollection.size());
    }

    /**
     * Test the methods which are supposed to return shallow copies of the
     * underyling data.
     */
    public void toCollection() {
        java.util.Collection<String> u = null;
        java.util.Collection<String> i = null;
        if (ucollection instanceof Unmodifiable.Set) {
            u = ((Unmodifiable.Set) ucollection).toSet();
            i = ((Immutable.Set) icollection).toSet();
        } else if (collection instanceof java.util.List) {
            u = ((Unmodifiable.List) ucollection).toList();
            i = ((Immutable.List) icollection).toList();
        }
        assertTrue(ucollection.containsAll(u));
        assertTrue(icollection.containsAll(i));
        u.add("Suzy");
        i.add("Suzy");
        assertFalse(ucollection.containsAll(u));
        assertFalse(icollection.containsAll(i));
    }
}