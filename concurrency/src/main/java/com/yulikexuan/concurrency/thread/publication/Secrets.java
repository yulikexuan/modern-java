//: com.yulikexuan.concurrency.thread.publication.Secrets.java

package com.yulikexuan.concurrency.thread.publication;


import java.util.HashSet;
import java.util.Set;


/**
 * Secrets
 *
 * Publishing an object
 *
 * This is the most blatant form of publication storing a reference in a public
 * static field, where any class and thread could see it
 * 
 * The initialize method instantiates a new HashSet and publishes it by storing
 * a reference to it into knownSecrets
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Secrets {

    public static Set<Secret> knownSecrets;

    public void initialize() {
        knownSecrets = new HashSet<Secret>();
    }

}

class Secret {
}

///:~