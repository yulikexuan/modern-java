//: com.yulikexuan.effectivejava.model.design.InstrumentedHashSet.java


package com.yulikexuan.effectivejava.model.design;


import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Collection;
import java.util.HashSet;


@NoArgsConstructor
public class InstrumentedHashSet<E> extends HashSet<E> {

    // The number of attempted element insertions
    private int addingCount;

    public InstrumentedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    @Override
    public boolean add(E e) {
        this.addingCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(@NonNull final Collection<? extends E> c) {
        this.addingCount += c.size();
        return super.addAll(c);
    }

    public int getAddingCount() {
        return this.addingCount;
    }

}///:~