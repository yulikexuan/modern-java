//: com.yulikexuan.effectivejava.object.construction.NyPizza.java


package com.yulikexuan.effectivejava.object.construction;


import java.util.Objects;
import java.util.Set;


public class NyPizza extends Pizza {

    public enum Size {
        SMALL, MEDIUM, LARGE
    }

    private final Size size;

    private NyPizza(Set<Topping> toppings, Size size) {
        super(toppings);
        this.size = size;
    }

    public Size getSize() {
        return this.size;
    }

    public static Builder builder(Size size) {
        return new Builder(size);
    }

    public static class Builder extends Pizza.Builder<Builder> {

        private final Size size;

        private Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public Pizza build() {
            return new NyPizza(this.toppings, this.size);
        }

        @Override
        protected Builder self() {
            return this;
        }

    }//: End of class Builder

}///:~