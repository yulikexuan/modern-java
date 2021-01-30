//: com.yulikexuan.concurrency.design.DelegatingVehicleTracker.java

package com.yulikexuan.concurrency.design;


import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * DelegatingVehicleTracker
 * <p/>
 * Delegating thread safety to a ConcurrentHashMap
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class DelegatingVehicleTracker {

    private final ConcurrentMap<String, Point> locations;

    private DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<String, Point>(points);
    }

    public static DelegatingVehicleTracker of(Map<String, Point> points) {
        return new DelegatingVehicleTracker(points);
    }

    /**
     * Returns an unmodifiable but “live” view of the vehicle locations
     *
     * This means that if thread A calls getLocations and thread B later
     * modifies the location of some of the points, those changes are reflected
     * in the Map returned to thread A
     *
     * This can be a benefit (more up-to-date data) or a liability (potentially
     * inconsistent view of the fleet), depending on the requirements
     *
     * @return an unmodifiable but “live” view of the vehicle locations
     */
    public Map<String, Point> getLocations() {
        return Collections.unmodifiableMap(locations);
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, Point.of(x, y)) == null) {
            throw new IllegalArgumentException("Invalid vehicle name: " + id);
        }
    }

    // Alternate version of getLocations (Listing 4.8)
    public Map<String, Point> getLocationsAsStatic() {
        return ImmutableMap.copyOf(this.locations);
    }

}///:~