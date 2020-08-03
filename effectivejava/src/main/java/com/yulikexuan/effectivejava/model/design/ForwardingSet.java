//: com.yulikexuan.effectivejava.model.design.ForwardingSet.java


package com.yulikexuan.effectivejava.model.design;


import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


/*
 * Reusable Forwarding Class
 */
@Slf4j
public class ForwardingSet<E> implements Set<E> {

    private final Set<E> set;

    private ForwardingSet(Set<E> set) {
        this.set = set;
    }

    public static <E> ForwardingSet<E> of(Set<E> set) {
        return new ForwardingSet(Objects.requireNonNull(set));
    }

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.set.contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return this.set.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }

    @NotNull
    @Override
    public <E> E[] toArray(@NotNull E[] a) {
        return this.set.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return this.set.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return this.set.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return this.set.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return this.set.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return this.set.removeAll(c);
    }

    @Override
    public void clear() {
        this.set.clear();
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.set.equals(obj);
    }

    @Override
    public String toString() {
        return this.set.toString();
    }
    
}///:~