//: com.yulikexuan.modernjava.algorithms.powerset.ListIterator.java

package com.yulikexuan.modernjava.algorithms.powerset;


import java.util.Iterator;
import java.util.Set;


public abstract class ListIterator<E> implements Iterator<E> {

    protected int position = 0;

    // Math.pow(2, n)
    private int size;

    public ListIterator(int size) {
        this.size = size;
    }

    @Override
    public boolean hasNext() {
        return position < size;
    }

}///:~