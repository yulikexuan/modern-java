//: com.yulikexuan.concurrency.design.GoodListHelper.java

package com.yulikexuan.concurrency.design;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * GoodListHelper
 *
 * <p/>
 * Examples of thread-safe implementations of
 * put-if-absent helper methods for List
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class GoodListHelper <E> {

    public List<E> list = Collections.synchronizedList(new ArrayList<E>());

    /*
     * Synchronized on the wrong lock
     */
    @GuardedBy("this.list")
    public boolean putIfAbsent(E x) {

        synchronized (this.list) {

            boolean absent = !list.contains(x);

            if (absent) {
                list.add(x);
            }

            return absent;
        }
    }

}///:~