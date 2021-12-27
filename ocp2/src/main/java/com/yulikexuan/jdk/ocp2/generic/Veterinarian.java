//: com.yulikexuan.jdk.ocp2.generic.Veterinarian.java

package com.yulikexuan.jdk.ocp2.generic;


import lombok.NonNull;

import java.util.List;


class Veterinarian {

    // method takes an array of any animal subtype
    public void checkAnimals(Animal[] animals) {
        for (Animal a : animals) {
            a.checkup();
        }
    }

    public void addAnimalButNotDog(Animal[] animals) {
        animals[0] = new Cat(); // no problem, any Animal works
    }

    public void addAnimalGeneric(@NonNull List<? super Animal> animals) {
        animals.add(new Cat());
    }

}///:~