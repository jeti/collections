package io.jeti.collections;

/**
 * <p>
 * This class contains unmodifiable containers, similar to those in
 * {@link java.util.Collections}, such as
 * {@link java.util.Collections#unmodifiableList(java.util.List)}. The
 * fundamental distinction is that these containers simply do not offer methods
 * that would allow you to modify them (such as "add" and "remove"), while the
 * unmodifiable containers in {@link java.util.Collections} implement such
 * methods, but throw runtime errors if you try to use them.
 * </p>
 * <p>
 * The reason you should prefer these containers is because the
 * {@link java.util.Collections} package does not provide you with compile-time
 * guarantees that your code is correct, meaning that if you do have an error in
 * your code, you would only discover it through either extensive testing, or a
 * surprise exception that pops up during runtime. Thus this class removes the
 * need to litter your code with many unnecessary try/catch blocks, and helps
 * you write sound, correct code, because the classes contained herein simply
 * don't offer methods that would allow you to modify them, such as add and
 * remove. Therefore, you won't see any nasty runtime errors popping up.
 * </p>
 * <p>
 * The only exception to this rule is the {@link Iterator} class. To be able to
 * iterate over the containers we define, they must implement {@link Iterable},
 * and hence they must provide an {@link java.util.Iterator}. Unfortunately, the
 * {@link java.util.Iterator} interface explicitly declares a
 * {@link java.util.Iterator#remove()} method. One solution would be to throw a
 * {@link RuntimeException} if somebody tries to call the remove method.
 * However, we specifically do not want our classes to throw such exceptions
 * (otherwise we might as well use the classes in {@link java.util.Collections}
 * ). Hence calls to {@link Iterator#remove()} are simply ignored (that is, the
 * remove method is defined to be empty). You could say that calls to remove
 * will "fail silently". So the one steadfast rule to stick to in when using
 * these classes is to remember that you can still iterate over them, but the
 * iterator's "remove" method will do nothing, so you should never use it.
 * </p>
 * <p>
 * Note that if you are using this class for the first time, you should also
 * examine the documentation for the {@link Immutable} class which provides some
 * useful code examples.
 * </p>
 */
public class Unmodifiable {

    /*
     * ---------------------------------------------
     *
     * Iterator
     *
     * ---------------------------------------------
     */

    /**
     * An {@link java.util.Iterator} for which the {@link #remove()} method is
     * empty. That is, calling remove does nothing, and will fail silently. We
     * specifically do not want to throw an {@link Exception} because if we did,
     * we might as well be using the unmodifiable classes in
     * {@link java.util.Collections}.
     */
    public static class Iterator<T> implements java.util.Iterator<T> {

        private final java.util.Iterator<T> iterator;

        /**
         * Construct an {@link Iterator}.
         */
        public Iterator(java.util.Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            return iterator.next();
        }

        /**
         * <p>
         * THIS CODE DOES NOTHING.
         * </p>
         * <p>
         * IT WILL NOT REMOVE AN ELEMENT FROM YOUR ITERATOR.
         * </p>
         * <p>
         * IF YOU ARE CALLING THIS METHOD, THEN YOU WILL ALMOST SURELY GET
         * UNEXPECTED, INCORRECT RESULTS.
         * </p>
         * <p>
         * See the discussion in {@link Unmodifiable} as to why this iterator is
         * implemented like this..
         * </p>
         */
        @Override
        public void remove() {
        }
    }

    /*
     * ---------------------------------------------
     *
     * Collection
     *
     * ---------------------------------------------
     */

    /**
     * <p>
     * A "container" class which does not implement methods which would allow
     * one to change the container. For instance, whereas
     * {@link java.util.Collection} implements "add" and "remove" methods, this
     * container does not. The only method which could possibly change the
     * container is the iterator.remove() method. However, this class uses the
     * {@link Iterator} class, which will fail silently if the remove method is
     * called.
     * </p>
     * <p>
     * Note that this method also does not protect you from changes to the
     * underlying collection or its data. For instance,
     * </p>
     * 
     * <pre>
     * <code>
     * List&lt;String&gt; names = new ArrayList&lt;&gt;().
     * names.add("Bob");
     *
     * Collection&lt;String&gt; unmodifiable = new Collection(names);
     *
     * // At this point, names = {"Bob"} and unmodifiable = {"Bob"}
     * // However, unmodifiable does not expose methods such as "add" and "remove". Hence
     * //
     * // unmodifiable.add("Nancy"); // Does not compile
     * //
     * // Furthermore, the unmodifiable collection holds a reference to
     * // the underlying data, so if we change that, then our unmodifiable collection
     * // will also change:
     *
     *  names.add("Nancy");
     *
     * // Now, names = {"Bob","Nancy"} and unmodifiable = {"Bob","Nancy"}
     *
     * </code>
     * </pre>
     * <p>
     * If you want to protect yourselves from these types of changes, then you
     * should use one of the {@link io.jeti.collections.Immutable} classes,
     * which create a shallow copy of the data during construction.
     * </p>
     * <p>
     * Furthermore, it may be of note to some developers that this class is a
     * class, not an interface like {@link java.util.Collections}, because we
     * want to force all descendants to use the unmodifiable iterator class.
     * </p>
     */
    public static class Collection<T> implements Iterable<T> {

        private final java.util.Collection<T> data;

        /**
         * Construct an {@link Collection}.
         */
        public Collection(java.util.Collection<T> data) {
            this.data = data;
        }

        /**
         * See {@link java.util.Collection#contains(Object)}.
         */
        public boolean contains(T o) {
            return data.contains(o);
        }

        /**
         * See {@link java.util.Collection#containsAll(java.util.Collection)}.
         */
        public boolean containsAll(java.util.Collection<?> arg0) {
            return data.containsAll(arg0);
        }

        /**
         * See {@link java.util.Collection#isEmpty()}.
         */
        public boolean isEmpty() {
            return data.isEmpty();
        }

        /**
         * @return an {@link Iterator}.
         */
        @Override
        public java.util.Iterator<T> iterator() {
            return new Iterator<>(data.iterator());
        }

        /**
         * See {@link java.util.Collection#size()}.
         */
        public int size() {
            return data.size();
        }

        /**
         * See {@link java.util.Collection#toArray()}.
         */
        public Object[] toArray() {
            return data.toArray();
        }

        /**
         * See {@link java.util.Collection#toArray(Object[])}.
         */
        public T[] toArray(T[] a) {
            return data.toArray(a);
        }
    }

    /*
     * ---------------------------------------------
     *
     * Set
     *
     * ---------------------------------------------
     */

    /**
     * A "set" class which does not implement methods which would allow one to
     * change the set. For instance, whereas both {@link java.util.Set} and
     * {@link java.util.Collections#unmodifiableSet(java.util.Set)} implement
     * "add" and remove methods, this container does not. The only method which
     * could possibly change the container is the iterator.remove() method.
     * However, this class uses the {@link Iterator} class, which will fail
     * silently if the remove method is called.
     */
    public static class Set<T> extends Collection<T> {

        /**
         * Construct an {@link Set}.
         */
        public Set(java.util.Set<T> data) {
            super(data);
        }

        /**
         * @return a shallow copy of this container as a
         *         {@link java.util.HashSet}.
         */
        public java.util.Set<T> toSet() {
            return new java.util.HashSet<>(super.data);
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
     * A "list" class which does not implement methods which would allow one to
     * change the list. For instance, whereas both {@link java.util.List} and
     * {@link java.util.Collections#unmodifiableList(java.util.List)} implement
     * "add" and "remove" methods, this container does not. The only method
     * which could possibly change the container is the iterator.remove()
     * method. However, this class uses the {@link Iterator} class, which will
     * fail silently if the remove method is called.
     */
    public static class List<T> extends Collection<T> {

        private final java.util.List<T> list;

        /**
         * Construct an {@link List}.
         */
        public List(java.util.List<T> data) {
            super(data);
            this.list = (java.util.List<T>) super.data;
        }

        /**
         * See {@link java.util.List#get(int)}.
         */
        public T get(int index) {
            return list.get(index);
        }

        /**
         * See {@link java.util.List#indexOf(Object)}.
         */
        public int indexOf(T o) {
            return list.indexOf(o);
        }

        /**
         * See {@link java.util.List#lastIndexOf(Object)}.
         */
        public int lastIndexOf(T o) {
            return list.lastIndexOf(o);
        }

        /**
         * @return a shallow copy of this container as a
         *         {@link java.util.ArrayList}.
         */
        public java.util.List<T> toList() {
            return new java.util.ArrayList<>(super.data);
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
     * A "map" class which does not implement methods which would allow one to
     * change the map. For instance, whereas both {@link java.util.Map} and
     * {@link java.util.Collections#unmodifiableMap(java.util.Map)} implement
     * "put" methods, this container does not. The only method which could
     * possibly change the container is through the {@link Map#keySet()} or
     * {@link Map#values()}, and hence they are returned as {@link Set}s and
     * {@link Collection}s, respectively.
     */
    public static class Map<K, V> {

        private final java.util.Map<K, V> data;
        private final Set<K>              keys;
        private final Collection<V>       values;

        /**
         * Construct an {@link Map}.
         */
        public Map(java.util.Map<K, V> data) {
            this.data = data;
            this.keys = new Set<>(data.keySet());
            this.values = new Collection<>(data.values());
        }

        /**
         * See {@link java.util.Map#containsKey(Object)}.
         */
        public boolean containsKey(K key) {
            return data.containsKey(key);
        }

        /**
         * See {@link java.util.Map#containsValue(Object)}.
         */
        public boolean containsValue(V value) {
            return data.containsValue(value);
        }

        /**
         * See {@link java.util.Map#get(Object)}.
         */
        public V get(K key) {
            return data.get(key);
        }

        /**
         * See {@link java.util.Map#isEmpty()}.
         */
        public boolean isEmpty() {
            return data.isEmpty();
        }

        /**
         * @return an {@link Set} of keys in the {@link Map}.
         */
        public Set<K> keySet() {
            return keys;
        }

        /**
         * @return an {@link Collection} of values in the {@link Map} .
         */
        public Collection<V> values() {
            return values;
        }

        /**
         * See {@link java.util.Map#size()}.
         */
        public int size() {
            return data.size();
        }

        /**
         * @return a shallow copy of this container as a
         *         {@link java.util.HashMap}.
         */
        public java.util.Map<K, V> toMap() {
            return new java.util.HashMap<>(data);
        }
    }
}
