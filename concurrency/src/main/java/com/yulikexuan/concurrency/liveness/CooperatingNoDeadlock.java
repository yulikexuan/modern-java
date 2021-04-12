//: com.yulikexuan.concurrency.liveness.CooperatingNoDeadlock.java

package com.yulikexuan.concurrency.liveness;


import com.google.common.collect.ImmutableSet;
import com.yulikexuan.concurrency.design.Point;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashSet;
import java.util.Set;


/**
 * CooperatingNoDeadlock
 * <p/>
 * Using open calls to avoiding deadlock between cooperating objects
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CooperatingNoDeadlock {

    @ThreadSafe
    class Taxi {

        @GuardedBy("this")
        private Point location, destination;

        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        /*
         * Acquire two locks:
         *   1.st - Lock this first
         *   2.rd - Release this Lock
         *   3.nd - Then lock Dispatcher
         *   4.rd - Release Dispatcher Lock
         */
        public void setLocation(final Point location) {

            boolean reachedDestination = false;

            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }

            if (reachedDestination) {
                this.dispatcher.notifyAvailable(this);
            }
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }

    }//: End of class Taxi

    @ThreadSafe
    class Dispatcher {

        @GuardedBy("this")
        private final Set<Taxi> taxis;

        @GuardedBy("this")
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            this.taxis = new HashSet<Taxi>();
            this.availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            this.availableTaxis.add(taxi);
        }

        /*
         * Acquire two locks:
         *   1.st - Lock this first
         *   2.rd - Release this Lock
         *   3.nd - Then lock Taxi
         *   4.rd - Release Taxi Lock
         */
        public Image getImage() {

            Set<Taxi> taxisCopy = null;

            synchronized (this) {
                taxisCopy = ImmutableSet.copyOf(this.taxis);
            }

            Image image = new Image();

            for (Taxi t : taxisCopy) {
                image.drawMarker(t.getLocation());
            }

            return image;
        }

    }//: End of class Dispatcher

    class Image {
        public void drawMarker(Point p) {
        }
    }

}///:~