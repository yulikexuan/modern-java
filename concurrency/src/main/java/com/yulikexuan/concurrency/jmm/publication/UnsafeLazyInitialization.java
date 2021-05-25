//: com.yulikexuan.concurrency.jmm.publication.UnsafeLazyInitialization.java

package com.yulikexuan.concurrency.jmm.publication;


import javax.annotation.concurrent.NotThreadSafe;


/**
 * UnsafeLazyInitialization
 * <p/>
 * Unsafe lazy initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class UnsafeLazyInitialization {

    private static Resource resource;

    public static Resource getInstance() {

        if (resource == null) {
            resource = new Resource(); // unsafe publication
        }

        return resource;
    }

}///:~