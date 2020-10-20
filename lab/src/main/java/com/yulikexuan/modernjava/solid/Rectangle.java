//: com.yulikexuan.modernjava.solid.Rectangle.java


package com.yulikexuan.modernjava.solid;


import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Rectangle {

    private int length;
    private int breadth;

    public int getArea() {
        return this.length * this.breadth;
    }

}///:~