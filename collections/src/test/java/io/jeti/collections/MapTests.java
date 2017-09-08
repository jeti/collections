package io.jeti.collections;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.util.HashMap;
import org.junit.Test;

/**
 * These tests ensure TODO
 */
public class MapTests {

    private java.util.Map<String, Integer>    map;
    private java.util.Map<String, Integer>    mapCopy;

    /** Unmodifiable Map */
    private Unmodifiable.Map<String, Integer> umap;

    /** This is an Immutable Map, but descends from unmodifiable */
    private Unmodifiable.Map<String, Integer> imap;

    @Test
    public void maps() throws Exception {
        runAll(new HashMap<>());
    }

    /**
     * Run all of the available tests on the specified type of container.
     */
    private void runAll(java.util.Map<String, Integer> map) throws Exception {
        reset(map);
        contains();
        reset(map);
        isEmpty();
        reset(map);
        size();
    }

    /**
     * Reset the test parameters for tests on the specified type of containers.
     */
    public void reset(java.util.Map<String, Integer> map) {

        if (map instanceof java.util.Map) {

            this.map = new HashMap<>();
            fill(this.map);
            mapCopy = new HashMap<>(this.map);
            umap = new Unmodifiable.Map<>(this.map);
            imap = new Immutable.Map<>(this.map);
        }
    }

    /**
     * Fill the specified container with the test data.
     */
    private void fill(java.util.Map<String, Integer> map) {
        map.put("Bob", 1);
        map.put("Nancy", 2);
        map.put("Tim", 3);
    }

    public void contains() throws Exception {
        for (String key : map.keySet()) {
            assertTrue(umap.containsKey(key));
            assertTrue(imap.containsKey(key));
        }
        for (Integer value : map.values()) {
            assertTrue(umap.containsValue(value));
            assertTrue(imap.containsValue(value));
        }
        map.clear();
        for (String key : map.keySet()) {
            assertFalse(umap.containsKey(key));
            assertTrue(imap.containsKey(key));
        }
        for (Integer value : map.values()) {
            assertFalse(umap.containsValue(value));
            assertTrue(imap.containsValue(value));
        }
    }

    public void isEmpty() throws Exception {
        assertFalse(umap.isEmpty());
        assertFalse(imap.isEmpty());
        map.clear();
        assertTrue(umap.isEmpty());
        assertFalse(imap.isEmpty());
    }

    public void size() throws Exception {
        assertTrue(map.size() == umap.size());
        assertTrue(map.size() == imap.size());
        map.remove("Bob");
        assertTrue(map.size() == umap.size());
        assertTrue(map.size() == imap.size() - 1);
        map.clear();
        assertTrue(map.size() == umap.size());
        assertTrue(mapCopy.size() == imap.size());
    }
}