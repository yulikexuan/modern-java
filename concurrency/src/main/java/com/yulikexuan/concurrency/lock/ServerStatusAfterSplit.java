//: com.yulikexuan.concurrency.lock.ServerStatusAfterSplit.java

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
public class ServerStatusAfterSplit {

    @GuardedBy("users")
    public final Set<String> users;

    @GuardedBy("queries")
    public final Set<String> queries;

    public void addUser(String u) {
        synchronized (this.users) {
            this.users.add(u);
        }
    }

    public void addQuery(String q) {
        synchronized (this.queries) {
            this.queries.add(q);
        }
    }

    public void removeUser(String u) {
        synchronized (this.users) {
            this.users.remove(u);
        }
    }

    public void removeQuery(String q) {
        synchronized (this.queries) {
            this.queries.remove(q);
        }
    }

}///:~