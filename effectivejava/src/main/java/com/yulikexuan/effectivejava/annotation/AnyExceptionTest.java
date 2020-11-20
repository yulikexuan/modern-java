//: com.yulikexuan.effectivejava.annotation.AnyExceptionTest.java

package com.yulikexuan.effectivejava.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates that the annotated method is a test method that must throw any of
 * the designated exceptions to succeed
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyExceptionTest {
    Class<? extends Exception>[] value();
}///:~