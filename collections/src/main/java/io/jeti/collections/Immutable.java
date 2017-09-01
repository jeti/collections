package io.jeti.collections;

/**
 * <p>
 * This class contains our immutable containers. Specifically, these
 * containers are very similar to the {@link Unmodifiable} containers, with the
 * primary distinction that these containers create a shallow copy of the
 * container during construction, which should ensure that the containers are immutable. (If you are using this class
 * for the first time, I suggest that you also look at the {@link Unmodifiable} documentation so that you aren't surprised by any "gotchas").
 * Note however, that we cannot enforce that the individual entries of the containers
 * to be immutable, as demonstrated in the following example. Hence if you are using this class, you
 * should almost always use immutable Class types in the containers, such as Strings, Integers, or other Immutable containers.
 * You should never use the usual java lists internally because they are immutable, which can lead to surprising results.
 * </p>
 * <pre>
 * <code>
 * java.util.List&lt;String&gt;names = new ArrayList&lt;&gt;();
 * names.add("Bob");
 *
 * Unmodifiable.List&lt;String&gt;unmodifiable = new Unmodifiable.List&lt;&gt;(names);
 * Immutable.List&lt;String&gt;immutable = new Immutable.List&lt;&gt;(names);
 *
 * // At this point, names = {"Bob"}, unmodifiable = {"Bob"}, immutable = {"Bob"}
 * //
 * // However, "names" can be directly modified, but "unmodifiable" and "immutable" cannot.
 * // For instance,
 * //
 * // unmodifiable.add("Nancy"); // Does not compile
 * // immutable.add("Nancy"); // Does not compile
 *
 * // But we can add or remove something from "names":
 * names.add("Nancy");
 *
 * // Furthermore, the {@link Unmodifiable} containers contain a direct reference to the object from which
 * // they were constructed, while the {@link Immutable} containers create a shallow copy. Therefore, since the
 * // underlying object "names" changed, this will be immediately reflected in the unmodifiable instance, but not
 * // in the immutable instance. Specifically, at this point, we have that
 * //
 * // names = {"Bob","Nancy"}, unmodifiable = {"Bob","Nancy"}, immutable = {"Bob"}
 *
 * // Finally, note that if the underlying type was mutable (String is immutable), then changes to the original object
 * // would also be reflected in the immutable class.
 * // For instance, let's suppose we want to create an immutable matrix of integers.
 * // Our first attempt (which definitely is not immutable) might look like this:
 * java.util.List&lt;java.util.List&lt;Integer&gt;&gt; matrix = new ArrayList&lt;&gt;();
 * int rows = 2;
 * int cols = 3;
 * for (int r = 0 ; r &lt; rows ; r++ ){
 *      // Generate this row of data
 *      java.util.List&lt;Integer&gt; row = new ArrayList&lt;&gt;();
 *      for (int c = 0 ; c &lt; cols; c++ ){
 *          row.add(1+c+r*cols);
 *      }
 *      // Add the row to the matrix
 *      matrix.add(row);
 * }
 *
 * // This is definitely not immutable because we can easily add rows to our matrix no problem, thanks to the fact that
 * // the java.util.List class exposes "add" and "remove" methods. For instance, we could even add a row with a
 * // different number of entries (in this case a row with 0 entries):
 * matrix.add(new ArrayList&lt;Integer&gt;() );
 *
 * // So at this point, matrix = {{1,2,3},{4,5,6},{}} // Note the empty array as the last entry
 *
 * // Now, let's remove that empty row, and see if we can do better....
 * matrix.remove(matrix.size()-1);
 *
 * // At this point, matrix = {{1,2,3},{4,5,6}} // Note we removed the last entry
 *
 * // To protect ourselves from somebody adding rows to our matrix, our first attempt might be to make the
 * // matrix class have an immutable class:
 * Immutable.List&lt;java.util.List&lt;Integer&gt;&gt; immutableMatrix0 = new Immutable.List&lt;&gt;(matrix);
 *
 * // At this point, we now have that matrix = {{1,2,3},{4,5,6}}, immutableMatrix0 = {{1,2,3},{4,5,6}}
 * // Furthermore, if somebody tries to add rows to our matrix, we are now protected because the immutable matrix
 * // created a shallow copy of the matrix during construction. For example, let's try adding an empty row again
 * // (note that you can't add an empty row to immutableMatrix0 because that method simply doesn't exist).
 * matrix.add(new ArrayList&lt;Integer&gt;() );
 *
 * // At this point, matrix = {{1,2,3},{4,5,6},{}}, but the immutable matrix is unchanged, that is, immutableMatrix0 = {{1,2,3},{4,5,6}}
 *
 * // But we still have a problem because the elements of the immutable matrix (which we declared to be java.util.List) are
 * // themselves mutable. So we can, for instance, directly change them. For instance, we can remove an element from a row:
 * immutableMatrix0.get(0).remove(0); // Remove the first entry of the first row.
 *
 * // At this point, matrix = {{1,2,3},{4,5,6},{}}, immutableMatrix0 = {{2,3},{4,5,6}}
 *
 * // So if you want truly immutable objects, you need to make sure that the generic type is also immutable. For instance, a
 * // better way of constructing the matrix would be like this
 * java.util.List&lt;Immutable.List&lt;Integer&gt;&gt; matrix2 = new ArrayList&lt;&gt;();
 * for (int r = 0 ; r &lt; rows ; r++ ){
 *      // Generate this row of data
 *      java.util.List&lt;Integer&gt; row = new ArrayList&lt;&gt;();
 *      for (int c = 0 ; c &lt; cols; c++ ){
 *          row.add(1+c+r*cols);
 *      }
 *      // Add the row to the matrix
 *      matrix2.add(new Immutable.List&lt;&gt;(row));
 * }
 * Immutable.List&lt;Immutable.List&lt;Integer&gt;&gt; immutableMatrix = new Immutable.List&lt;&gt;(matrix);
 *
 * // This "immutableMatrix" will be truly and completely immutable, no matter what you do to the mutable "matrix" instance.
 * </code>
 * </pre>
 */
public class Immutable {

    /*
     * ---------------------------------------------
     *
     * Set
     *
     * ---------------------------------------------
     */

    /**
     * A "set" wrapper extended from the {@link Unmodifiable.Set} class which
     * makes a shallow copy of the underlying set during construction, and saves the data internally in a {@link java.util.HashSet}.
     */
    public static class Set<T> extends Unmodifiable.Set<T> {

        /**
         * Construct a {@link Set}.
         */
        public Set(java.util.Collection<T> data) {
            super(new java.util.HashSet<>(data));
        }
    }

    /*
     * ---------------------------------------------
     *
     * List
     *
     * ---------------------------------------------
     */

    /**
     * A "list" wrapper extended from the {@link Unmodifiable.List} class which
     * makes a shallow copy of the underlying list during construction, and saves the data internally in a {@link java.util.ArrayList}.
     */
    public static class List<T> extends Unmodifiable.List<T> {

        /**
         * Construct a {@link List}.
         */
        public List(java.util.Collection<T> data) {
            super(new java.util.ArrayList<>(data));
        }
    }

    /*
     * ---------------------------------------------
     *
     * Map
     *
     * ---------------------------------------------
     */

    /**
     * A "map" wrapper extended from the {@link Unmodifiable.Map} class which
     * makes a shallow copy of the underlying map during construction, and saves the data internally in a {@link java.util.HashMap}.
     */
    public static class Map<K, V> extends Unmodifiable.Map<K, V> {

        /**
         * Construct an {@link Map}.
         */
        public Map(java.util.Map<K, V> data) {
            super(new java.util.HashMap<>(data));
        }
    }
}
