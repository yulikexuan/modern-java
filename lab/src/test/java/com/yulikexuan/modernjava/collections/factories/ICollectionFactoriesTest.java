//: com.yulikexuan.modernjava.collections.factories.ICollectionFactoriesTest.java


package com.yulikexuan.modernjava.collections.factories;


public interface ICollectionFactoriesTest {

    String NEW_NAME_1 = "Mike";

    static String[] getNames() {
        return new String[] {"Raphael", "Olivia", "Thibaut"};
    }

}///:~