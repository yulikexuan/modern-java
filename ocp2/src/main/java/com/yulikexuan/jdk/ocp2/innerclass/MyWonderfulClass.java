//: com.yulikexuan.jdk.ocp2.innerclass.MyWonderfulClass.java

package com.yulikexuan.jdk.ocp2.innerclass;


import lombok.extern.slf4j.Slf4j;


@Slf4j
final class MyWonderfulClass {

    void go() {

        Bar bar = new Bar();
        bar.doStuff(new Foo() {
            @Override
            public void foof() {
                log.info(">>> foofy");
            }
        });

    }

}

interface Foo {
    void foof();
}

final class Bar {
    void doStuff(Foo foo) {
        foo.foof();
    }
}

///:~