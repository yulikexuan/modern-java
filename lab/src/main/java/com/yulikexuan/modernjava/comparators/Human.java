//: com.yulikexuan.modernjava.comparators.Human.java


package com.yulikexuan.modernjava.comparators;


import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@Builder @AllArgsConstructor
public final class Human implements Comparable<Human> {

    private final String name;
    private final int age;
    private final Personality personality;

    @Override
    public int compareTo(@NonNull Human human) {
        return this.name.compareTo(human.getName());
    }

}///:~