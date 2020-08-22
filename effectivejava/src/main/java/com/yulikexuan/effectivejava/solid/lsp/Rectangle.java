//: com.yulikexuan.effectivejava.solid.lsp.Rectangle.java


package com.yulikexuan.effectivejava.solid.lsp;


import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
class Rectangle {

    private int length;
    private int breadth;

    public long getArea() {
        return this.length * this.breadth;
    }

}///:~