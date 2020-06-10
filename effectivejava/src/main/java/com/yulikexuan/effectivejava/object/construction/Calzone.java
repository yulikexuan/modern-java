//: com.yulikexuan.effectivejava.object.construction.Calzone.java


package com.yulikexuan.effectivejava.object.construction;


import java.util.Set;


public class Calzone extends Pizza {

    private final boolean sauceInside;

    private Calzone(Set<Topping> toppings, boolean sauceInside) {
        super(toppings);
        this.sauceInside = sauceInside;
    }

    public boolean isSauceInside() {
        return this.sauceInside;
    }

    public static Builder builder(boolean sauceInside) {
        return new Builder(sauceInside);
    }

    public static class Builder extends Pizza.Builder<Builder> {

        private final boolean sauceInside;

        private Builder(boolean sauceInside) {
            this.sauceInside = sauceInside;
        }

        @Override
        Calzone build() {
            return new Calzone(this.toppings, sauceInside);
        }

        @Override
        protected Builder self() {
            return this;
        }

    }//: End of class Builder

}///:~