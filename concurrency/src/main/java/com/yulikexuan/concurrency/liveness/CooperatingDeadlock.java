//: com.yulikexuan.concurrency.liveness.CooperatingDeadlock.java

package com.yulikexuan.concurrency.liveness;


import com.yulikexuan.concurrency.design.Point;

import javax.annotation.concurrent.GuardedBy;
import java.util.*;


/**
 * CooperatingDeadlock
 * <p/>
 * Lock-ordering deadlock between cooperating objects
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CooperatingDeadlock {

    // Warning: Deadlock-Prone!
    class Taxi {

        @GuardedBy("this")
        private Point location, destination;

        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return this.location;
        }

        /*
         * Acquire two locks:
         *   1.st - Lock this first
         *   2.nd - Then lock Dispatcher
         *   3.rd - Release Dispatcher Lock
         *   4.rd - Release this Lock
         */
        public synchronized void setLocation(final Point location) {
            this.location = location;
            if (this.location.equals(this.destination)) {
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized Point getDestination() {
            return this.destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }

    }//: End of class Taxi

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
         *   2.nd - Then lock Taxi
         *   3.rd - Release Taxi Lock
         *   4.rd - Release this Lock
         */
        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : this.taxis) {
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