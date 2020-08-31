//: com.yulikexuan.effectivejava.generics.badlyuse.ObjectStack.java


package com.yulikexuan.effectivejava.generics.badlyuse;


import java.util.Arrays;
import java.util.EmptyStackException;


class ObjectStack {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Object[] elements;
    private int size = 0;

    private ObjectStack(int capacity) {
        this.elements = new Object[capacity];
    }

    static ObjectStack of() {
        return new ObjectStack(DEFAULT_INITIAL_CAPACITY);
    }

    static ObjectStack of(int capacity) {
        if (capacity <= 0) {
            throw new EmptyStackException();
        }
        return new ObjectStack(capacity);
    }

    void push(Object e) {
        this.ensureCapacity();
        this.elements[size++] = e;
    }

    Object pop() {
        if (this.size <= 0) {
            throw new EmptyStackException();
        }
        Object result = this.elements[--size];
        // Eliminate obsolete reference
        this.elements[size] = null;
        return result;
    }

    boolean isEmpty() {
        return this.size <= 0;
    }

    private void ensureCapacity() {

        if (elements.length == size) {
            /*
             * Copies the specified array, truncating or padding with nulls
             * (if necessary) so the copy has the specified length
             *
             * For all indices that are valid in both the original array and
             * the copy, the two arrays will contain identical values
             *
             * For any indices that are valid in the copy but not the original,
             * the copy will contain null
             *   - Such indices will exist if and only if the specified length
             *     is greater than that of the original array.
             *
             * The resulting array is of exactly the same class as the original array.
             */
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

}///:~