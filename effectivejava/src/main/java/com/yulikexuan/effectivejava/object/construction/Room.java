//: com.yulikexuan.effectivejava.object.construction.Room.java


package com.yulikexuan.effectivejava.object.construction;


import lombok.extern.slf4j.Slf4j;

import java.lang.ref.Cleaner;
import java.util.Objects;


@Slf4j
public class Room implements AutoCloseable {

    private static final Cleaner CLEANER = Cleaner.create();

    // The state of this room, shared with our cleanable
    private final State state;

    // Our cleanable. Cleans the room when it’s eligible for gc
    private final Cleaner.Cleanable cleanable;

    private Room(State state) {
        this.state = Objects.requireNonNull(state);
        cleanable = CLEANER.register(this, state);
    }

    boolean isDirty() {
        return this.state.isDirty();
    }

    static Room of(State state) {
        return new Room(state);
    }

    @Override
    public void close() throws Exception {
        cleanable.clean();
    }

    static class State implements Runnable {

        int numJunkPiles; // Number of junk piles in this room

        private State(int numJunkPiles) {
            this.numJunkPiles = numJunkPiles;
        }

        boolean isDirty() {
            return this.numJunkPiles > 0;
        }

        static State of(int numJunkPiles) {
            return new State(numJunkPiles);
        }

        @Override
        public void run() {
            log.info(">>>>>>> Cleaning Room ... ... ");
            numJunkPiles = 0;
        }
    }

}///:~