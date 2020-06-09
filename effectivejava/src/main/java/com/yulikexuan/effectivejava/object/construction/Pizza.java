//: com.yulikexuan.effectivejava.object.construction.Pizza.java


package com.yulikexuan.effectivejava.object.construction;


import com.google.common.collect.ImmutableSet;

import java.util.EnumSet;
import java.util.Set;


public abstract class Pizza {

    public enum Topping {
        HAM, MUSHROOM, ONION, PEPPER, SAUSAGE,
    }

    final Set<Topping> toppings;

    Pizza(Set<Topping> toppings) {
        this.toppings = ImmutableSet.copyOf(toppings);
    }

    public Set<Topping> getToppings() {
        return ImmutableSet.copyOf(this.toppings);
    }

    abstract static class Builder<T extends Builder<T>> {

        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            this.toppings.add(topping);
            return self();
        }

        abstract Pizza build();

        protected abstract T self();

    }//: End of class Builder

}///:~