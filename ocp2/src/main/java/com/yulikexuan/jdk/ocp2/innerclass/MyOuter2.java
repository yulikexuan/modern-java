//: com.yulikexuan.jdk.ocp2.innerclass.MyOuter2.java

package com.yulikexuan.jdk.ocp2.innerclass;


import lombok.extern.slf4j.Slf4j;


@Slf4j
class MyOuter2 {

    private String x = "Outer2";

    void doStuff() {

        final String z = "local variable";

        class MyInner {

            public void seeOuter() {
                log.info(">>> Outer x is '{}'", x);
                log.info(">>> Local var z is '{}'", z);
                // z = "changing the local variable"; // Won't compile!
            }

        }//: End of class MyInner

        x = "Changing Outer2";
        MyInner mi = new MyInner();
        mi.seeOuter();

    }// End of doStuff()

    public static void main(String args[]) {
        MyOuter2 mo2 = new MyOuter2();
        mo2.doStuff();
    }

}//: End of class MyOuter2