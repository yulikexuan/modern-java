//: com.yulikexuan.effectivejava.generics.BoundedWildcardsStack.java


package com.yulikexuan.effectivejava.generics;


import java.util.Arrays;
import java.util.Collection;


/*
 * Generic stack with bulk methods using wildcard types (Pages 139-41)
 */
public class BoundedWildcardsStack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /*
     * The elements array will contain only E instances from push(E)
     * This is sufficient to ensure type safety, but the runtime type of the
     * array won't be E[]; it will always be Object[]
     */
    @SuppressWarnings("unchecked")
    public BoundedWildcardsStack() {
        elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public E pop() {

        if (size == 0) {
            throw new EmptyStackException();
        }

        E result = elements[--size];
        elements[size] = null; // Eliminate obsolete reference

        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }

    public void pushAll(Iterable<? extends E> src) {
        src.forEach(this::push);
    }

    public void popAll(Collection<? super E> dst) {
        while (!this.isEmpty()) {
            dst.add(this.pop());
        }
    }

}///:~