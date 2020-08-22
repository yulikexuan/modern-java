//: com.yulikexuan.effectivejava.solid.lsp.Square.java


package com.yulikexuan.effectivejava.solid.lsp;


import lombok.*;

/**
 * Square class; Square inherits from Rectangle;
 * Represents ISA relationship - Square is a Rectangle
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
class Square extends Rectangle {

    @Override
    public void setLength(int length) {
        super.setLength(length);
        super.setBreadth(length);
    }

    @Override
    public void setBreadth(int breadth) {
        super.setBreadth(breadth);
        super.setLength(breadth);
    }

}///:~