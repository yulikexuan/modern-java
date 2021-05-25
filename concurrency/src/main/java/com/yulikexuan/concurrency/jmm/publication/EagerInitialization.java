//: com.yulikexuan.concurrency.jmm.publication.EagerInitialization.java

package com.yulikexuan.concurrency.jmm.publication;


import javax.annotation.concurrent.ThreadSafe;


/**
 * EagerInitialization
 * <p/>
 * Eager initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class EagerInitialization {

    private static Resource resource = new Resource();

    public static Resource getResource() {
        return resource;
    }

}///:~