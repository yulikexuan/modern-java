//: com.yulikexuan.concurrency.noblocking.ConcurrentStack.java

package com.yulikexuan.concurrency.noblocking;


import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;


/**
 * ConcurrentStack
 * <p/>
 * Nonblocking stack using Treiber's algorithm
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ConcurrentStack<E> {

    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E item) {
        Node<E> newHead = Node.of(item);
        Node<E> oldHead = null;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop() {

        Node<E> oldHead = null;
        Node<E> newHead = null;

        do {
            oldHead = top.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));

        return oldHead.item;
    }

    private static class Node <E> {

        public final E item;
        public Node<E> next;

        private Node(E item) {
            this.item = item;
        }

        static <E> Node<E> of(E item) {
            return new Node(item);
        }
    }

}///:~