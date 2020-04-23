//: com.yulikexuan.modernjava.lambdas.chainofresp.HeaderTextProcessing.java


package com.yulikexuan.modernjava.lambdas.chainofresp;


public class HeaderTextProcessing extends ProcessingObject<String> {

    @Override
    String handleWork(String input) {
        return "From Yul, Nic and Rene: " + input;
    }

}///:~