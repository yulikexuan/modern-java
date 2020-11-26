//: com.yulikexuan.effectivejava.stream.IterableAdaptor.java

package com.yulikexuan.effectivejava.stream;


import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class IterableAdaptor {

    /*
     * Adapter from  Stream<E> to Iterable<E>
     *
     * No cast is necessary
     * Java's type inference works properly in this context
     */
    public static <E> Iterable<E> iterableOf(Stream<E> stream) {
        return stream::iterator;
    }

    // Adapter from Iterable<E> to Stream<E>
    public static <E> Stream<E> streamOf(Iterable<E> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}///:~