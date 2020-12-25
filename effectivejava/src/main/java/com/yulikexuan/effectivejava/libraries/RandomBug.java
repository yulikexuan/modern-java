//: com.yulikexuan.effectivejava.libraries.RandomBug.java

package com.yulikexuan.effectivejava.libraries;


import java.util.Random;


class RandomBug {

    // Common but deeply flawed!
    static Random rnd = new Random();

    static int random(int n) {
        return Math.abs(rnd.nextInt()) % n;
    }

}///:~