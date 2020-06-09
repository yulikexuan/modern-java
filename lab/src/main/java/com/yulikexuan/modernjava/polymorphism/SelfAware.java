//: com.yulikexuan.modernjava.polymorphism.SelfAware.java


package com.yulikexuan.modernjava.polymorphism;

public interface SelfAware<T extends SelfAware> {
    T self();
}///:~