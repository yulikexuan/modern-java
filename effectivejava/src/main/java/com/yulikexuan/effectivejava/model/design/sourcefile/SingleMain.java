//: com.yulikexuan.effectivejava.model.design.sourcefile.SingleMain.java


package com.yulikexuan.effectivejava.model.design.sourcefile;


/*
 * Static member classes instead of multiple top-level classes (Page 116)
 */
public class SingleMain {

    public static void main(String[] args) {
        System.out.println(String.join
                ("", Utensil.NAME + Dessert.NAME));
    }

    private static class Utensil {
        static final String NAME = "pan";
    }

    private static class Dessert {
        static final String NAME = "cake";
    }

}///:~