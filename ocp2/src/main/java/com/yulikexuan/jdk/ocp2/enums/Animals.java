//: com.yulikexuan.jdk.ocp2.enums.Animals.java

package com.yulikexuan.jdk.ocp2.enums;


enum Animals {

    DOG("woof"), CAT("meow"), FISH("burble");

    String sound;

    Animals(String s) {
        sound = s;
    }

}///:~