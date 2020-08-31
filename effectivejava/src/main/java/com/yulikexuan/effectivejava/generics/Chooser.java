//: com.yulikexuan.effectivejava.generics.Chooser.java


package com.yulikexuan.effectivejava.generics;


import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Chooser<T> {

    private final Random random;

    private final List<T> choiceList;

    Chooser(Collection<T> choices) {
        this.choiceList = Lists.newArrayList(choices);
        this.random = ThreadLocalRandom.current();
    }

    Object choice() {
        return this.choiceList.get(this.random.nextInt(this.choiceList.size()));
    }

}///:~