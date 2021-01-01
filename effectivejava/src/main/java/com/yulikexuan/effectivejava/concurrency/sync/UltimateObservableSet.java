//: com.yulikexuan.effectivejava.concurrency.sync.UltimateObservableSet.java

package com.yulikexuan.effectivejava.concurrency.sync;


import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;


public class UltimateObservableSet<E> extends ForwardingSet<E>
        implements IObservableSet<E> {

    private final List<SetObserver<E>> observers = new CopyOnWriteArrayList<>();

    private UltimateObservableSet(Set<E> set) {
        super(set);
    }

    public static <E> UltimateObservableSet<E> of(@NonNull Set<E> set) {
        return new UltimateObservableSet<>(set);
    }

    public void addObserver(SetObserver<E> observer) {
        this.observers.add(observer);
    }

    public boolean removeObserver(SetObserver<E> observer) {
        return this.observers.remove(observer);
    }

    /*
     * observer::added is a alien method from within a synchronized region
     * It's dangerous
     */
    private void notifyElementAdded(E element) {
        for (SetObserver<E> observer : this.observers) {
            observer.added(this, element);
        }
    }

    @Override
    public boolean add(E element) {
        boolean added = super.add(element);
        if (added) {
            notifyElementAdded(element);
        }
        return added;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E element : c) {
            result |= add(element); // Calls notifyElementAdded
        }
        return result;
    }

}///:~