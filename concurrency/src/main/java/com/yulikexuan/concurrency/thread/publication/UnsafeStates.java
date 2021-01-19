//: com.yulikexuan.concurrency.thread.publication.UnsafeStates.java

package com.yulikexuan.concurrency.thread.publication;


/**
 * UnsafeStates
 * <p/>
 * Allowing internal mutable state to escape
 *
 * Publishing states in this way is problematic because any caller can modify
 * its contents
 *
 * In this case, the states array has escaped its intended scope, because what
 * was supposed to be private state has been effectively made public
 *
 * @author Brian Goetz and Tim Peierls
 */
public class UnsafeStates {

    private String[] states = new String[]{
            "AK", "AL" /*...*/
    };

    public String[] getStates() {
        return states;
    }

}///:~