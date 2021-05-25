//: com.yulikexuan.concurrency.jmm.publication.ResourceFactory.java

package com.yulikexuan.concurrency.jmm.publication;


import javax.annotation.concurrent.ThreadSafe;


/**
 * ResourceFactory
 * <p/>
 * Lazy Initialization Holder Class Idiom
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ResourceFactory {

    /*
     * The JVM defers initializing the ResourceHolder class until it is actually
     * used, and because the Resource is initialized with a static initializer,
     * no additional synchronization is needed
     */
    private static class ResourceHolder {
        public static Resource resource = new Resource();
    }

    /*
     * The first call to getResource by any thread causes ResourceHolder to be
     * loaded and initialized, at which time the initialization of the Resource
     * happens through the static initializer
     */
    public static Resource getResource() {
        return ResourceFactory.ResourceHolder.resource;
    }

}///:~