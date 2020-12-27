//: com.yulikexuan.effectivejava.comparators.Employee.java

package com.yulikexuan.effectivejava.comparators;


import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor(staticName = "of")
class Employee implements Comparable<Employee> {

    private final String name;
    private final int salary;

    @Override
    public int compareTo(Employee o) {
        return this.salary > o.salary ? 1 : (this.salary == o.salary) ? 0 : -1 ;
    }

}///:~