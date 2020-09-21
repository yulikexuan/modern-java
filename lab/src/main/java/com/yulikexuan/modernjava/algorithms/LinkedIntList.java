//: com.yulikexuan.modernjava.algorithms.LinkedIntList.java


package com.yulikexuan.modernjava.algorithms;


import lombok.Getter;

import java.util.Objects;


@Getter
class LinkedIntList {

    static final int SIZE = 8;

    private Node head;

    private LinkedIntList(Node head) {
        this.head = head;
    }

    static LinkedIntList of() {
        return new LinkedIntList(initialize(0));
    }

    private static Node initialize(int value) {

        if (value >= SIZE) {
            return null;
        }

        return Node.of(value, initialize(value + 1));
    }

    void print() {
        Node node = head;
        while (Objects.nonNull(node)) {
            System.out.print(node.getValue());
            System.out.print(" ");
            node = node.getNext();
        }
        System.out.println();
    }

    void reverse() {

        Node previous = null;
        Node current = this.head;
        Node next = null;

        if (Objects.isNull(current) || Objects.isNull(current.next)) {
            return;
        }

        next = current.getNext();

        while (Objects.nonNull(next)) {
            previous = current;
            current = next;
            next = next.getNext();
            current.setNext(previous);
        }

        this.head.setNext(null);

        this.head = current;
    }

    @Getter
    static class Node {

        private final int value;

        private Node next;

        private Node(int value) {
            this.value = value;
        }

        private Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }

        static Node of(int value, Node next) {
            return new Node(value, next);
        }

        static Node of(int value) {
            return new Node(value);
        }

        void setNext(Node next) {
            this.next = next;
        }

    } //: End of class Node

}///:~