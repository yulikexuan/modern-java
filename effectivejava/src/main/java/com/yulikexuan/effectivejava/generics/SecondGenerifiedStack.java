//: com.yulikexuan.effectivejava.generics.SecondGenerifiedStack.java


package com.yulikexuan.effectivejava.generics;


import java.util.Arrays;
import java.util.EmptyStackException;


public class SecondGenerifiedStack<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Object[] elements;
    private int size = 0;

    private SecondGenerifiedStack(int capacity) {
        this.elements = new Object[capacity];
    }

    static SecondGenerifiedStack of() {
        return new SecondGenerifiedStack(DEFAULT_INITIAL_CAPACITY);
    }

    static SecondGenerifiedStack of(int capacity) {
        if (capacity <= 0) {
            throw new EmptyStackException();
        }
        return new SecondGenerifiedStack(capacity);
    }

    void push(E e) {
        this.ensureCapacity();
        this.elements[size++] = e;
    }

    E pop() {
        if (this.size <= 0) {
            throw new EmptyStackException();
        }

        // push requires elements to be of type E, so cast is correct
        @SuppressWarnings("unchecked")
        E result = (E) this.elements[--size];

        // Eliminate obsolete reference
        this.elements[size] = null;

        return result;
    }

    boolean isEmpty() {
        return this.size <= 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

}///:~