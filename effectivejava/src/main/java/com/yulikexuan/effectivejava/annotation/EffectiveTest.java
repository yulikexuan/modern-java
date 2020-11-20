//: com.yulikexuan.effectivejava.annotation.EffectiveTest.java

package com.yulikexuan.effectivejava.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates the annotated method is a test method
 *
 * Should be only used on parameterless static mehtods;
 *   - The compiler is not able to enforce this
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EffectiveTest {

}///:~