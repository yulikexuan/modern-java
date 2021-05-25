//: com.yulikexuan.concurrency.jmm.publication.SafeLazyInitialization.java

package com.yulikexuan.concurrency.jmm.publication;


import javax.annotation.concurrent.ThreadSafe;


/**
 * UnsafeLazyInitialization
 * <p/>
 * Unsafe lazy initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
final class SafeLazyInitialization {

    private static Resource resource;

    public synchronized static Resource getInstance() {

        if (resource == null) {
            resource = new Resource();
        }

        return resource;
    }

}///:~