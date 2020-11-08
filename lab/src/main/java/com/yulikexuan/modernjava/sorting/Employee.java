//: com.yulikexuan.modernjava.sorting.Employee.java


package com.yulikexuan.modernjava.sorting;


import lombok.*;


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
    public int compareTo(@NonNull Employee o) {
        return this.name.compareTo(o.getName());
    }

}///:~