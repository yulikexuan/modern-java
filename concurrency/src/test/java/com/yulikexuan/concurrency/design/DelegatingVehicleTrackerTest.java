//: com.yulikexuan.concurrency.design.DelegatingVehicleTrackerTest.java

package com.yulikexuan.concurrency.design;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Unmodifiable but live view of Map - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DelegatingVehicleTrackerTest {

    static final int LIMIT = 1000;

    private ThreadLocalRandom random;
    private DelegatingVehicleTracker vehicleTracker;

    private List<String> vehicleIds;
    private Map<String, Point> view;
    private Point newLocation;

    @BeforeEach
    void setUp() {

        this.random = ThreadLocalRandom.current();

        vehicleIds = List.of(
                RandomStringUtils.randomAlphanumeric(7),
                RandomStringUtils.randomAlphanumeric(8),
                RandomStringUtils.randomAlphanumeric(6));

        Map<String, Point> fleet = Map.of(
                this.vehicleIds.get(0),
                Point.of(random.nextInt(LIMIT), random.nextInt(LIMIT)),
                this.vehicleIds.get(1),
                Point.of(random.nextInt(LIMIT), random.nextInt(LIMIT)),
                this.vehicleIds.get(2),
                Point.of(random.nextInt(LIMIT), random.nextInt(LIMIT)));

        this.vehicleTracker = DelegatingVehicleTracker.of(fleet);

        this.view = this.vehicleTracker.getLocations();

        this.newLocation = Point.of(0, 0);
    }

    @Test
    void test_The_Fleed_View_Should_Be_Live() {

        // Given
        this.vehicleTracker.setLocation(this.vehicleIds.get(1),
                newLocation.getX(), newLocation.getY());

        // When
        Point location = this.view.get(this.vehicleIds.get(1));

        // Then
        assertThat(location).isEqualTo(this.newLocation);
    }

    @Test
    void test_The_Static_Fleed_View_Should_Be_A_Snapshot() {

        // Given
        Map<String, Point> snapshot = this.vehicleTracker.getLocationsAsStatic();
        int index = 2;
        this.vehicleTracker.setLocation(this.vehicleIds.get(index),
                newLocation.getX(), newLocation.getY());

        // When
        Point location = snapshot.get(this.vehicleIds.get(index));

        // Then
        assertThat(location).isNotEqualTo(this.newLocation);
    }

}///:~