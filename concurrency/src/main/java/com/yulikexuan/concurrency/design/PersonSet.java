//: com.yulikexuan.concurrency.design.PersonSet.java

package com.yulikexuan.concurrency.design;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashSet;
import java.util.Set;


@ThreadSafe
public class PersonSet {

    /*
     * The state of PersonSet is managed by a HashSet, which is not thread-safe
     *
     * this.persons is private and not allowed to escape, the HashSet is
     * confined to the PersonSet
     *
     * The only code paths that can access persons are addPerson and
     * containsPerson, and each of these acquires the lock on the PersonSet
     *
     * All its state is guarded by its intrinsic lock, making PersonSet
     * thread-safe
     */
    @GuardedBy("this")
    private final Set<Person> persons = new HashSet<Person>();

    public synchronized void addPerson(Person p) {
        persons.add(p);
    }

    public synchronized boolean containsPerson(Person p) {
        return persons.contains(p);
    }

}

interface Person {
}

///:~