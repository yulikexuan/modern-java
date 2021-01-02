//: com.yulikexuan.effectivejava.concurrency.Intern.java

package com.yulikexuan.effectivejava.concurrency;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Concurrent canonicalizing map atop ConcurrentMap - Pages 273-274
 */
public class Intern {

    // Concurrent canonicalizing map atop ConcurrentMap - not optimal
    private static final ConcurrentMap<String, String> MAP =
            new ConcurrentHashMap<>();

//    public static String intern(String s) {
//        String previousValue = MAP.putIfAbsent(s, s);
//        return previousValue == null ? s : previousValue;
//    }

    /*
     * Concurrent canonicalizing map atop ConcurrentMap - faster!
     *
     * In fact, you can do even better.
     *
     * ConcurrentHashMap is optimized for retrieval operations, such as get.
     *
     * Therefore, it is worth invoking get initially and calling putIfAbsent
     * only if get indicates that it is necessary:
     */
    public static String intern(String s) {

        String result = MAP.get(s);

        if (result == null) {
            result = MAP.putIfAbsent(s, s);
            if (result == null)
                result = s;
        }

        return result;
    }

}///:~