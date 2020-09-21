//: com.yulikexuan.modernjava.algorithms.StackQueue.java


package com.yulikexuan.modernjava.algorithms;


import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;


public class StackQueue<T> extends AbstractQueue<T> {

    private final Stack<T> left;
    private final Stack<T> right;

    StackQueue() {
        this.left = new Stack();
        this.right = new Stack();
    }

    @Override
    public Iterator<T> iterator() {
        return this.left.iterator();
    }

    @Override
    public int size() {
        return this.left.size();
    }

    @Override
    public boolean offer(T t) {

        if (Objects.isNull(t)) {
            return false;
        }

        while (!this.left.empty()) {
            T element = this.left.peek();
            this.left.pop();
            this.right.push(element);
        }

        this.left.push(t);

        while (!this.right.empty()) {
            T element = this.right.peek();
            this.right.pop();
            this.left.push(element);
        }

        return true;
    }

    @Override
    public T poll() {

        if (this.left.empty()) {
            return null;
        }

        T element = this.left.peek();
        this.left.pop();

        return element;
    }

    @Override
    public T peek() {

        if (this.left.empty()) {
            return null;
        }

        return this.left.peek();
    }

}///:~