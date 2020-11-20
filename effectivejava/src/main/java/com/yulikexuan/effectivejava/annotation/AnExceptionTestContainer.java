//: com.yulikexuan.effectivejava.annotation.AnExceptionTestContainer.java

package com.yulikexuan.effectivejava.annotation;


import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnExceptionTestContainer {
    AnExceptionTest[] value();
}///:~