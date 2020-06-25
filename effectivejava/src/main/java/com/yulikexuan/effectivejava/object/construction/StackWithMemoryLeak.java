//: com.yulikexuan.effectivejava.object.construction.StackWithMemoryLeak.java


package com.yulikexuan.effectivejava.object.construction;


import java.util.Arrays;


public class StackWithMemoryLeak {

    static final int DEFAULT_INITIAL_CAPACITY = 16;

    private Object[] elements;
    private int size = 0;

    public StackWithMemoryLeak() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

//    public Object pop() {
//        if (size == 0) throw new EmptyStackException();
//        return elements[--size];
//    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }

    // Corrected version of pop method (Page 27)
    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // Eliminate obsolete reference
        return result;
    }

    int getSize() {
        return this.size;
    }

    Object getPoppedElement() {
        return this.elements[this.size];
    }

//    public static void main(String[] args) {
//        StackWithMemoryLeak stack = new StackWithMemoryLeak();
//        for (String arg : args) stack.push(arg);
//
//        while (true)
//            System.err.println(stack.pop());
//    }

}///:~