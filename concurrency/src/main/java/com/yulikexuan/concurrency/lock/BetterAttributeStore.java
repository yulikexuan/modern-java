//: com.yulikexuan.concurrency.lock.BetterAttributeStore.java

package com.yulikexuan.concurrency.lock;


import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * BetterAttributeStore
 * <p/>
 * Reducing lock duration
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BetterAttributeStore {

    @GuardedBy("this")
    private final Map<String, String> attributes = new HashMap<String, String>();

    /*
     * Because constructing the key string and processing the regular
     * expression do not access shared state, they need not be executed with
     * the lock held
     */
    public boolean userLocationMatches(String name, String regexp) {

        String key = "users." + name + ".location";

        String location = null;

        synchronized (this) {
            location = attributes.get(key);
        }

        if (location == null) {
            return false;
        } else {
            return Pattern.matches(regexp, location);
        }
    }

}///:~