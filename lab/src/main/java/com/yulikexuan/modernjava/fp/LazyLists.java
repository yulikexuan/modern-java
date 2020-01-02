//: com.yulikexuan.modernjava.fp.LazyLists.java


package com.yulikexuan.modernjava.fp;


import com.google.common.math.IntMath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


interface ILazyList<T> {

    T getHead();
    Supplier<ILazyList<T>> getTail();

    default ILazyList<T> filter(Predicate<T> predicate) {
        return this.isEmpty() ? this :
                predicate.test(this.getHead()) ?
                        LazyList.<T>builder()
                                .head(this.getHead())
                                .tail(() -> this.getTail().get()
                                        .filter(predicate))
                                .build() :
                        getTail().get().filter(predicate);
    }

    default boolean isEmpty() {
        return true;
    }

    static <T> ILazyList<T> numberLazyList(
            T seed, UnaryOperator<T> next, int tailLength) {

        ILazyList<T> lazyList = LazyList.<T>builder()
                .head(seed)
                .tail(() -> tailLength <= 0 ? new EmptyLazyList<T>() :
                        numberLazyList(next.apply(seed), next,
                                tailLength - 1))
                .build();

        return lazyList;
    }

    static <T> ILazyList<T> unlimitedNumberLazyList(
            T seed, UnaryOperator<T> next) {

        ILazyList<T> lazyList = LazyList.<T>builder()
                .head(seed)
                .tail(() -> unlimitedNumberLazyList(next.apply(seed), next))
                .build();

        return lazyList;
    }

    static <T> List<T> getAll(ILazyList<T> lazyList) {

        if (lazyList == null) {
            return List.of();
        }

        return accumulate(new ArrayList<>(), lazyList);
    }

    static <T> List<T> getHeads(ILazyList<T> lazyList, int size) {

        if (lazyList == null) {
            return List.of();
        }

        List<T> container = new ArrayList<>();

        return accumulate(new ArrayList<>(), lazyList, 10);
    }

    static <T> List<T> accumulate(List<T> container, ILazyList<T> lazyList,
                                  int size) {

        if ((lazyList instanceof EmptyLazyList) || (size == 0)) {
            return container;
        }

        container.add(lazyList.getHead());

        return accumulate(container, lazyList.getTail().get(), --size);
    }

    static <T> List<T> accumulate(List<T> container, ILazyList<T> lazyList) {
        if (lazyList instanceof EmptyLazyList) {
            return container;
        }
        container.add(lazyList.getHead());
        return accumulate(container, lazyList.getTail().get());
    }
}

@Getter
@Builder @AllArgsConstructor
class LazyList<T> implements ILazyList<T> {

    private final T head;
    private final Supplier<ILazyList<T>> tail;

    @Override
    public boolean isEmpty() {
        return false;
    }

}

class EmptyLazyList<T> implements ILazyList<T> {

    @Override
    public T getHead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Supplier<ILazyList<T>> getTail() {
        throw new UnsupportedOperationException();
    }

}

public class LazyLists {

    public static ILazyList<Integer> getPrimeList(int length) {

        if (length <= 0) {
            return new EmptyLazyList<>();
        }

        if (length == 1) {
            return LazyList.<Integer>builder()
                    .head(2)
                    .tail(() -> new EmptyLazyList<>())
                    .build();
        }

        ILazyList<Integer> numberList = ILazyList.unlimitedNumberLazyList(
                2, i -> ++i).filter(i -> IntMath.isPrime(i));

        return numberList;
    }

}///:~