//: com.yulikexuan.concurrency.thread.confinement.Animals.java

package com.yulikexuan.concurrency.thread.confinement;


import java.util.*;

public class Animals {

    Ark ark;
    Species species;
    Gender gender;

    public int loadTheArk(Collection<Animal> candidates) {

        /*
         * Maintaining stack confinement for object references requires a
         * little more assistance from the programmer to ensure that the
         * referent does not escape
         *
         * However, if we were to publish a reference to the Set (or any of its
         * internals), the confinement would be violated and the animals would
         * escape
         *
         * Using a non-thread-safe object in a within-thread context is still
         * thread-safe;
         * However, be careful: the design requirement that the object be
         * confined to the executing thread, or the awareness that the confined
         * object is not thread-safe, often exists only in the head of the
         * developer when the code is written;
         * If the assumption of within-thread usage is not clearly documented,
         * future maintainers might mistakenly allow the object to escape
         */
        SortedSet<Animal> animals = null;

        /*
         * It's not possible to violate stack confinement for numPairs
         *
         * The language semantics ensure that primitive local variables are
         * always stack confined.
         */
        int numPairs = 0;
        Animal candidate = null;

        // animals confined to method, don't let them escape!
        animals = new TreeSet<Animal>(new SpeciesGenderComparator());
        animals.addAll(candidates);

        for (Animal a : animals) {
            if (candidate == null || !candidate.isPotentialMate(a)) {
                candidate = a;
            } else {
                ark.load(new AnimalPair(candidate, a));
                ++numPairs;
                candidate = null;
            }
        }

        return numPairs;
    }

    class Animal {

        Species species;
        Gender gender;

        public boolean isPotentialMate(Animal other) {
            return species == other.species && gender != other.gender;
        }
    }

    enum Species {
        AARDVARK, BENGAL_TIGER, CARIBOU, DINGO, ELEPHANT, FROG, GNU, HYENA,
        IGUANA, JAGUAR, KIWI, LEOPARD, MASTADON, NEWT, OCTOPUS,
        PIRANHA, QUETZAL, RHINOCEROS, SALAMANDER, THREE_TOED_SLOTH,
        UNICORN, VIPER, WEREWOLF, XANTHUS_HUMMINBIRD, YAK, ZEBRA
    }

    enum Gender {
        MALE, FEMALE
    }

    class AnimalPair {

        private final Animal one, two;

        public AnimalPair(Animal one, Animal two) {
            this.one = one;
            this.two = two;
        }
    }

    class SpeciesGenderComparator implements Comparator<Animal> {

        public int compare(Animal one, Animal two) {
            int speciesCompare = one.species.compareTo(two.species);
            return (speciesCompare != 0)
                    ? speciesCompare
                    : one.gender.compareTo(two.gender);
        }
    }

    class Ark {
        private final Set<AnimalPair> loadedAnimals = new HashSet<AnimalPair>();
        public void load(AnimalPair pair) {
            loadedAnimals.add(pair);
        }
    }

}///:~