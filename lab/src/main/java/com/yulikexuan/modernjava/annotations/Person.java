//: com.yulikexuan.modernjava.annotations.Person.java


package com.yulikexuan.modernjava.annotations;


import org.checkerframework.checker.nullness.qual.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor(staticName = "of")
public class Person {

    private final String name;

    /*
     * Type Annotations are annotations that can be placed anywhere using a type
     * This includes the new operator, type casts, implements clauses, etc.
     */
    @Override
    public String toString() {
        @NonNull String name = this.getName();
        return "Person's name is " + name;
    }
}///:~