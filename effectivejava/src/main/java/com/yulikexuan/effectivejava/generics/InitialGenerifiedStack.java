//: com.yulikexuan.effectivejava.generics.InitialGenerifiedStack.java


package com.yulikexuan.effectivejava.generics;


import java.util.Arrays;
import java.util.EmptyStackException;


public class InitialGenerifiedStack<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private E[] elements;
    private int size = 0;

    /*
     * This solution is more readable
     * This solution is also more concise: only cast once
     * But Cause Heap pollution
     *   - Heap pollution: implies that we have bad data in our heap memory.
     *     In Java language, heap pollution is a situation that occurs when a
     *     variable of parameterized type points to an object that is not of
     *     that parameterized type
     *
     * The elements array will contain only E instances from push(E).
     * This is sufficient to ensure type safety, but the runtime type of the
     * array won't be E[]; it will always be Object[]!
     */
    @SuppressWarnings("unchecked")
    private InitialGenerifiedStack(int capacity) {
        /*
         * Warning! It's legal but not type-safe
         */
        this.elements = (E[]) new Object[capacity];
    }

    static InitialGenerifiedStack of() {
        return new InitialGenerifiedStack(DEFAULT_INITIAL_CAPACITY);
    }

    static InitialGenerifiedStack of(int capacity) {
        if (capacity <= 0) {
            throw new EmptyStackException();
        }
        return new InitialGenerifiedStack(capacity);
    }

    void push(E e) {
        this.ensureCapacity();
        this.elements[size++] = e;
    }

    E pop() {
        if (this.size <= 0) {
            throw new EmptyStackException();
        }
        E result = this.elements[--size];
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