//: com.yulikexuan.modernjava.inheritance.rules.HybridCar.java


package com.yulikexuan.modernjava.inheritance.rules;


import lombok.Getter;


@Getter
final class HybridCar extends Car {

    // Properties Rule – Class Invariants
    // invariant: charge >= 0;
    private int charge;

    // Properties Rule – History Constraint
    // Allowed to be set once at the time of creation.
    // Value can only increment thereafter.
    // Value cannot be reset.
    protected int mileage;

    HybridCar(int limit) {
        super(limit);
    }

    @Override
    protected void accelerate() {
        if (this.charge > 0) {
            int newSpeed = this.speed + (this.speed * 10 / 100);
            if (newSpeed < limit) {
                this.speed = newSpeed;
            }
        }
    }

}///:~