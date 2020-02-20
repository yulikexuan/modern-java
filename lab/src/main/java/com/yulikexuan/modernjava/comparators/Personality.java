//: com.yulikexuan.modernjava.comparators.Personality.java


package com.yulikexuan.modernjava.comparators;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public final class Personality {

    String faith;
    String behavior;

}///:~