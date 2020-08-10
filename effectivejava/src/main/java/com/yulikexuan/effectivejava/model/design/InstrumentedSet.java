//: com.yulikexuan.effectivejava.model.design.InstrumentedSet.java


package com.yulikexuan.effectivejava.model.design;


import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;


public class InstrumentedSet<E> extends ForwardingSet<E> {

    private int addingCount;

    private InstrumentedSet(Set<E> set) {
        super(set);
    }

    public static <E> InstrumentedSet<E> of(Set<E> set) {
        return new InstrumentedSet<>(set);
    }

    @Override
    public boolean add(E e) {
        this.addingCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        this.addingCount += Objects.requireNonNull(c).size();
        return super.addAll(c);
    }

    public int getAddingCount() {
        return this.addingCount;
    }

}///:~