//: com.yulikexuan.effectivejava.generics.badlyuse.GenericArrayChooser.java


package com.yulikexuan.effectivejava.generics.badlyuse;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class GenericArrayChooser<T> {

    private Random random;

    private final T[] choiceArray;

    GenericArrayChooser(Collection<T> choices) {
        // Have Waring Here
        this.choiceArray = (T[]) choices.toArray();
        this.random = ThreadLocalRandom.current();
    }

    T choice() {
        return this.choiceArray[this.random.nextInt(this.choiceArray.length)];
    }

}///:~