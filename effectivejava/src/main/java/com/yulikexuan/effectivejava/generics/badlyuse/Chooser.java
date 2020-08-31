//: com.yulikexuan.effectivejava.generics.badlyuse.Chooser.java


package com.yulikexuan.effectivejava.generics.badlyuse;


import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


class Chooser {

    private Random random;

    private final Object[] choiceArray;

    Chooser(Collection choices) {
        this.choiceArray = choices.toArray();
        this.random = ThreadLocalRandom.current();
    }

    Object choice() {
        return this.choiceArray[this.random.nextInt(this.choiceArray.length)];
    }

}///:~