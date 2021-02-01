//: com.yulikexuan.concurrency.design.BadListHelper.java

package com.yulikexuan.concurrency.design;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * BadListHelper
 *
 * <p/>
 * Examples of non-thread-safe implementations of
 * put-if-absent helper methods for List
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class BadListHelper<E> {

    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    /*
     * Synchronized on the wrong lock
     */
    @GuardedBy("this")
    public synchronized boolean putIfAbsent(E x) {

        boolean absent = !list.contains(x);

        if (absent) {
            list.add(x);
        }

        return absent;
    }

}///:~