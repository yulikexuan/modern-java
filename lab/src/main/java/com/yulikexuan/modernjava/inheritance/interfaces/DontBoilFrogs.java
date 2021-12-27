//: com.yulikexuan.modernjava.inheritance.interfaces.DontBoilFrogs.java

package com.yulikexuan.modernjava.inheritance.interfaces;


interface FrogBoilable {

    static int getCtoF(int cTemp) {
        return (cTemp * 9 / 5) + 32;
    }

    default String hop() { return "hopping "; }
}

public class DontBoilFrogs implements FrogBoilable {

    public static void main(String[] args) {
        new DontBoilFrogs().go();
    }

    void go() {
        System.out.print(hop());
        System.out.println(FrogBoilable.getCtoF(100));
        System.out.println(FrogBoilable.getCtoF(100));
        DontBoilFrogs dbf = new DontBoilFrogs();
        System.out.println(FrogBoilable.getCtoF(100));
    }

}///:~