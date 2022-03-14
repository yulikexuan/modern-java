//: com.yulikexuan.jdk.ocp2.switches.Weather.java

package com.yulikexuan.jdk.ocp2.switches;

enum Season { SPRING, SUMMER, WINTER }

class Weather {
    public int getAverageTemperate(Season s) {
        switch (s) {
            default:
            case WINTER: return 30;
        }
    }
}///:~