//: com.yulikexuan.concurrency.thread.publication.ThisEscape.java

package com.yulikexuan.concurrency.thread.publication;


/**
 * ThisEscape
 * <p/>
 * Implicitly allowing the <code>this</code> reference to escape
 * Don’t do this
 *
 * This class shows a mechanism by which an object or its internal state can be
 * published is to publish an inner class instance
 *
 * When the inner EventListener instance is published, so is the enclosing
 * ThisEscape instance
 *
 * Publishing an object from within its constructor can publish an incompletely
 * constructed object
 *
 * If the <code>this</code> reference escapes during construction, the object
 * is considered not properly constructed
 *
 * Do not allow the this reference to escape during construction
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ThisEscape {

    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        });
    }

    void doSomething(Event e) {
    }


    interface EventSource {
        void registerListener(EventListener e);
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {
    }

}///:~