//: com.yulikexuan.concurrency.lock.ServerStatusBeforeSplit.java

package com.yulikexuan.concurrency.lock;


import lombok.AllArgsConstructor;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;


/**
 * ServerStatusBeforeSplit
 * <p/>
 * Candidate for lock splitting
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
@AllArgsConstructor(staticName = "of")
public class ServerStatusBeforeSplit {

    @GuardedBy("this")
    public final Set<String> users;

    @GuardedBy("this")
    public final Set<String> queries;

    public synchronized void addUser(String u) {
        this.users.add(u);
    }

    public synchronized void addQuery(String q) {
        this.queries.add(q);
    }

    public synchronized void removeUser(String u) {
        this.users.remove(u);
    }

    public synchronized void removeQuery(String q) {
        this.queries.remove(q);
    }

}///:~