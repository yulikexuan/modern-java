//: com.yulikexuan.effectivejava.annotation.AnExceptionTest.java

package com.yulikexuan.effectivejava.annotation;


import java.lang.annotation.*;


/**
 * Indicates that the annotated method is a test method that must throw the
 * designated exception to succeed
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AnExceptionTestContainer.class)
public @interface AnExceptionTest {
    Class<? extends Throwable> value();
}///:~