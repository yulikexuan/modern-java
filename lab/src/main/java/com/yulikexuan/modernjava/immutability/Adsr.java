//: com.yulikexuan.modernjava.immutability.Adsr.java


package com.yulikexuan.modernjava.immutability;


import lombok.Builder;
import lombok.Getter;


/*
 * ADSR envelope (Attack Decay Sustain Release), a common type of music envelope
 *
 * A list of things to do to create an immutable Class:
 *   1. Mark the class final so that it cannot be extended
 *   2. Mark it's variables private and final
 *   3. If the constructor takes any mutable objects as arguments, make new
 *      copies of those objects in the constructor
 *      [DEFENSIVE COPYING]
 *   4. DO NOT PROVIDE ANY SETTER METHODS
 *   5. If any of the getter methods return a mutable object reference,
 *      make a copy of the actual object, and then return a reference to that
 *      copy
 */
@Getter
public final class Adsr {

    private final StringBuilder name;
    private final int attack;
    private final int decay;

    @Builder
    public Adsr(StringBuilder name, int attack, int decay) {
        this.name = new StringBuilder(name);
        this.attack = attack;
        this.decay = decay;
    }

    public StringBuilder getName() {
        return new StringBuilder(this.name);
    }

    public Adsr getAdsr() {
        return this;
    }

}///:~