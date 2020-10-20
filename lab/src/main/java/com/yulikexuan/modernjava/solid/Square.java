//: com.yulikexuan.modernjava.solid.Square.java


package com.yulikexuan.modernjava.solid;


import lombok.*;


/**
 * Square class; Square inherits from Rectangle;
 * Represents ISA relationship - Square is a Rectangle
 * @author Ajitesh Shukla
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Square extends Rectangle {

    public void setLength(int length) {
        super.setLength(length);
        super.setBreadth(length);
    }

    public void setBreadth(int breadth) {
        super.setLength(breadth);
        super.setBreadth(breadth);
    }

}///:~