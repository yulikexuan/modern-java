//: com.yulikexuan.effectivejava.concurrency.sync.ImprovedObservableSet.java

package com.yulikexuan.effectivejava.concurrency.sync;


import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class ImprovedObservableSet<E> extends ForwardingSet<E>
        implements IObservableSet<E> {

    private final List<SetObserver<E>> observers = new ArrayList<>();

    private ImprovedObservableSet(Set<E> set) {
        super(set);
    }

    public static <E> ImprovedObservableSet<E> of(@NonNull Set<E> set) {
        return new ImprovedObservableSet<>(set);
    }

    public void addObserver(SetObserver<E> observer) {
        synchronized(observers) {
            observers.add(observer);
        }
    }

    public boolean removeObserver(SetObserver<E> observer) {
        synchronized(observers) {
            return observers.remove(observer);
        }
    }

    /*
     * observer::added is a alien method from within a synchronized region
     * It's dangerous
     */
    private void notifyElementAdded(E element) {

        List<SetObserver<E>> snapshot = null;

        synchronized(observers) {
            snapshot = ImmutableList.copyOf(this.observers);
        }

        for (SetObserver<E> observer : snapshot)
            observer.added(this, element);
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