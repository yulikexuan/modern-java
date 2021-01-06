//: com.yulikexuan.concurrency.thread.safety.atomicity.LazyInitRace.java

package com.yulikexuan.concurrency.thread.safety.atomicity;


import javax.annotation.concurrent.NotThreadSafe;


/**
 * LazyInitRace
 *
 * Race condition in lazy initialization
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class LazyInitRace {

    private ExpensiveObject instance;

    public ExpensiveObject getInstance() {
        if (instance == null) {
            instance = new ExpensiveObject();
        }
        return instance;
    }
}

class ExpensiveObject { }

///:~