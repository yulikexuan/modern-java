//: com.yulikexuan.concurrency.noblocking.LinkedQueue.java

package com.yulikexuan.concurrency.noblocking;


import lombok.NonNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicReference;


/**
 * LinkedQueue
 * <p/>
 * Insertion in the Michael-Scott nonblocking queue algorithm
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class LinkedQueue <E> {

    private final Node<E> dummy = new Node<E>(null, null);

    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(
            dummy);

    private final AtomicReference<Node<E>> tail = new AtomicReference<Node<E>>(
            dummy);

    public boolean put(final @NonNull E item) {

        Node<E> newNode = new Node<E>(item, null);

        while (true) {

            Node<E> curTail = this.tail.get();
            Node<E> tailNext = curTail.next.get();

            if (curTail == this.tail.get()) {

                if (tailNext != null) {
                    // Queue in intermediate state, advance tail
                    this.tail.compareAndSet(curTail, tailNext);
                } else {
                    // In quiescent state, try inserting new node
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // Insertion succeeded, try advancing tail
                        this.tail.compareAndSet(curTail, newNode);
                        return true;
                    }
                }
            }
        }
    }

    static class Node <E> {

        final E item;
        final AtomicReference<Node<E>> next;

        public Node(E item, LinkedQueue.Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<LinkedQueue.Node<E>>(next);
        }
    }

}///:~