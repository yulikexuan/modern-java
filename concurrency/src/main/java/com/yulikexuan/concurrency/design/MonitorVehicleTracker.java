//: com.yulikexuan.concurrency.design.MonitorVehicleTracker.java

package com.yulikexuan.concurrency.design;


import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashMap;
import java.util.Map;


@ThreadSafe
public class MonitorVehicleTracker {

    @GuardedBy("this")
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : MutablePoint.of(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {

        MutablePoint loc = locations.get(id);

        if (loc == null) {
            throw new IllegalArgumentException("No such ID: " + id);
        }

        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(
            Map<String, MutablePoint> m) {

        Map<String, MutablePoint> result = new HashMap<>();

        for (String id : m.keySet()) {
            result.put(id, MutablePoint.of(m.get(id)));
        }

        return ImmutableMap.copyOf(result);
    }

}///:~