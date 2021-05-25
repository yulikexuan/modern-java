//: com.yulikexuan.concurrency.jmm.publication.DoubleCheckedLocking.java

package com.yulikexuan.concurrency.jmm.publication;


import javax.annotation.concurrent.NotThreadSafe;


/**
 * DoubleCheckedLocking
 * <p/>
 * Double-checked-locking antipattern
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class DoubleCheckedLocking {

    private static Resource resource;

    public static Resource getInstance() {

        if (resource == null) {
            synchronized (DoubleCheckedLocking.class) {
                if (resource == null) {
                    resource = new Resource();
                }
            }
        }

        return resource;
    }

}///:~