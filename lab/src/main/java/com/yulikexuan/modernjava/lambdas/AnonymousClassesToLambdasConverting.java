//: com.yulikexuan.modernjava.lambdas.AnonymousClassesToLambdasConverting.java


package com.yulikexuan.modernjava.lambdas;


public class AnonymousClassesToLambdasConverting {

    // JDK 7
    Runnable runnable_1 = new Runnable() {
        @Override
        public void run() {
            System.out.println("Hello Anonymous Runnable!");
        }
    };

    // JDK 8+
    Runnable runnable_2 = () -> System.out.println("Hello Lambda!");

}///:~