//: com.yulikexuan.effectivejava.concurrency.sync.ObservableSet.java

package com.yulikexuan.effectivejava.concurrency.sync;


import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/*
 * observers is accessed from a few synchronized methods or blocks
 * synchronized methods can be called from outside
 * notifyElementAdded method allows SetObserver::added be called in
 * a synchronozed block, this can cause deadlock or
 * ConcurrentModificationException
 */
public class ObservableSet<E> extends ForwardingSet<E>
        implements IObservableSet<E> {

    private final List<SetObserver<E>> observers = new ArrayList<>();

    private ObservableSet(Set<E> set) {
        super(set);
    }

    public static <E> ObservableSet<E> of(@NonNull Set<E> set) {
        return new ObservableSet<>(set);
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
        synchronized(observers) {
            for (SetObserver<E> observer : observers)
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