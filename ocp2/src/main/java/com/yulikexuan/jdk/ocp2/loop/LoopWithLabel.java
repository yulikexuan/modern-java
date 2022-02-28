//: com.yulikexuan.jdk.ocp2.loop.LoopWithLabel.java

package com.yulikexuan.jdk.ocp2.loop;

class LoopWithLabel {

    public static void main(String[] args) {
        var race = "";
        loop:
        do {
            race += "x";
            break loop;
        } while (true);
        System.out.println(">>> race is " + race);
    }

}///:~