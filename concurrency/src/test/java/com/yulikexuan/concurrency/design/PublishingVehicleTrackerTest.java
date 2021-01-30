//: com.yulikexuan.concurrency.design.PublishingVehicleTrackerTest.java

package com.yulikexuan.concurrency.design;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test - Car's location can be changed by other thread ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PublishingVehicleTrackerTest {

    static final int LIMIT = 1000;

    private ThreadLocalRandom random;
    private PublishingVehicleTracker vehicleTracker;

    private List<String> vehicleIds;
    private Map<String, SafePoint> view;
    private SafePoint newLocation;

    @BeforeEach
    void setUp() {
        this.random = ThreadLocalRandom.current();

        this.vehicleIds = List.of(
                RandomStringUtils.randomAlphanumeric(7),
                RandomStringUtils.randomAlphanumeric(8),
                RandomStringUtils.randomAlphanumeric(6));

        Map<String, SafePoint> fleet = Map.of(
                this.vehicleIds.get(0),
                SafePoint.of(this.random.nextInt(LIMIT), this.random.nextInt(LIMIT)),
                this.vehicleIds.get(1),
                SafePoint.of(this.random.nextInt(LIMIT), this.random.nextInt(LIMIT)),
                this.vehicleIds.get(2),
                SafePoint.of(this.random.nextInt(LIMIT), this.random.nextInt(LIMIT)));

        this.vehicleTracker = PublishingVehicleTracker.of(fleet);

        this.view = this.vehicleTracker.getLocations();

        this.newLocation = SafePoint.of(
                this.random.nextInt(LIMIT), this.random.nextInt(LIMIT));
    }

    @Test
    void test_Vehicle_Location_Can_Be_Changed_Through_Fleet_View() {

        // Given
        String carId = this.vehicleIds.get(0);
        SafePoint location = this.view.get(carId);
        int[] newCoordinate = this.newLocation.get();

        // When
        location.set(newCoordinate[0], newCoordinate[1]);
        int[] actualLocation = this.vehicleTracker.getLocation(carId).get();

        // Then
        assertThat(actualLocation).isEqualTo(newCoordinate);
    }

}///:~