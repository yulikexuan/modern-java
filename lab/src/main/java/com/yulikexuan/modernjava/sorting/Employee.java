//: com.yulikexuan.modernjava.sorting.Employee.java


package com.yulikexuan.modernjava.sorting;


import lombok.*;
import org.jetbrains.annotations.NotNull;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder @AllArgsConstructor
public class Employee implements Comparable<Employee> {

    private final String name;
    private final int age;
    private final int salary;

    @Override
    public int compareTo(@NotNull Employee o) {
        return this.name.compareTo(o.getName());
    }

}///:~