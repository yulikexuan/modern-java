//: com.yulikexuan.modernjava.fp.LinkedLists.java


package com.yulikexuan.modernjava.fp;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

interface IList<T> {
    T getHead();
    IList<T> getTail();
    default boolean isEmpty() {
        return true;
    }
}

@Getter
@ToString
@Builder @AllArgsConstructor
class LinkedList<T> implements IList<T> {

    private final T head;
    private final IList<T> tail;

    @Override
    public boolean isEmpty() {
        return false;
    }
}

class EmptyList<T> implements IList<T> {

    private EmptyList() {}

    public static EmptyList newEmptyList() {
        return new EmptyList<>();
    }

    @Override
    public T getHead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IList<T> getTail() {
        throw new UnsupportedOperationException();
    }
}

public class LinkedLists {

    public static List<Integer> getNumbers(IList<Integer> numList) {

        List<Integer> container = new ArrayList<>();
        retrieveNumbers(container, numList);

        return ImmutableList.copyOf(container);
    }

    static void retrieveNumbers(List<Integer> container,
                                IList<Integer> numList) {
        if (numList.isEmpty()) {
            return;
        }
        container.add(numList.getHead());
        retrieveNumbers(container, numList.getTail());
    }

}///:~