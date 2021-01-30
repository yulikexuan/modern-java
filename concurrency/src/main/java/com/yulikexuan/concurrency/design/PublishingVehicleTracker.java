//: com.yulikexuan.concurrency.design.PublishingVehicleTracker.java

package com.yulikexuan.concurrency.design;


import lombok.NonNull;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * PublishingVehicleTracker
 *
 * Callers cannot add or remove vehicles, but could change the location of one
 * of the vehicles by mutating the SafePoint values in the returned Map
 *
 * If it needed to be able to “veto” changes to vehicle locations, or to take
 * action when a location changes, the approach taken by PublishingVehicleTracker
 * would not be appropriate
 *
 * <p/>
 * Vehicle tracker that safely publishes underlying state
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class PublishingVehicleTracker {

    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    private PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<String, SafePoint>(locations);
        this.unmodifiableMap = Collections.unmodifiableMap(this.locations);
    }

    public static PublishingVehicleTracker of(
            @NonNull Map<String, SafePoint> locations) {
        return new PublishingVehicleTracker(locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiableMap;
    }

    public SafePoint getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(@NonNull String id, int x, int y) {
        if (!locations.containsKey(id)) {
            throw new IllegalArgumentException("invalid vehicle name: " + id);
        }
        locations.get(id).set(x, y);
    }

}///:~