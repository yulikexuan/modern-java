//: com.yulikexuan.effectivejava.enums.enummap.PlantTest.java


package com.yulikexuan.effectivejava.enums.enummap;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.yulikexuan.effectivejava.enums.enummap.Plant.LifeCycle;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("Test EnumMap - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PlantTest {

    static final Plant BASIL = Plant.of("Basil", LifeCycle.ANNUAL);
    static final Plant CARROWAY = Plant.of("Carroway", LifeCycle.BIENNIAL);
    static final Plant DILL = Plant.of("Dill", LifeCycle.ANNUAL);
    static final Plant LAVENDAR = Plant.of("Lavendar", LifeCycle.PERENNIAL);
    static final Plant PARSLEY = Plant.of("Parsley", LifeCycle.BIENNIAL);
    static final Plant ROSEMARY = Plant.of("Rosemary", LifeCycle.PERENNIAL);

    static final List<Plant> MY_GARDEN = List.of(
            BASIL,
            CARROWAY,
            DILL,
            LAVENDAR,
            PARSLEY,
            ROSEMARY
    );

    static final List<Plant> SMALL_GARDEN = List.of(BASIL, CARROWAY, DILL, PARSLEY);

    private static StopWatch stopWatch;

    @BeforeAll
    static void beforeAll() {
        stopWatch = new StopWatch();
    }

    @BeforeEach
    void setUp() {
        stopWatch.reset();
    }

    @Test
    void test_Being_Able_To_Classify_A_Graden_By_Plant_Life_Style() {

        // Given & When
        stopWatch.start();
        Set<Plant>[] classifiedPlants = Plant.classifyPlantsOfTheGarden(MY_GARDEN);
        stopWatch.stop();

        log.debug(">>>>>>> Spent {} nano-seconds to classify with ordinal of enum.",
                stopWatch.getTime(TimeUnit.NANOSECONDS));

        Set<Plant> annualPlants = classifiedPlants[LifeCycle.ANNUAL.ordinal()];
        Set<Plant> biennialPlants = classifiedPlants[LifeCycle.BIENNIAL.ordinal()];
        Set<Plant> perennialPlants = classifiedPlants[LifeCycle.PERENNIAL.ordinal()];

        // Then
        assertThat(annualPlants).contains(BASIL, DILL);
        assertThat(biennialPlants).contains(CARROWAY, PARSLEY);
        assertThat(perennialPlants).contains(LAVENDAR, ROSEMARY);
    }

    @Test
    void test_Classify_A_Garden_Of_Plants_In_EnumMap_With_Stream() {

        // Given & When
        stopWatch.start();
        Map<LifeCycle, Set<Plant>> classifiedPlants =
                Plant.classifyPlantsInEnumMapWithStream(MY_GARDEN);
        stopWatch.stop();

        log.debug(">>>>>>> Spent {} nano-seconds to classify with EnumMap",
                stopWatch.getTime(TimeUnit.NANOSECONDS));

        // Then
        assertThat(classifiedPlants.size()).isEqualTo(LifeCycle.values().length);
        assertThat(classifiedPlants.get(LifeCycle.ANNUAL))
                .contains(BASIL, DILL);
        assertThat(classifiedPlants.get(LifeCycle.BIENNIAL))
                .contains(CARROWAY, PARSLEY);
        assertThat(classifiedPlants.get(LifeCycle.PERENNIAL))
                .contains(LAVENDAR, ROSEMARY);
    }

    @Test
    void test_Stream_Grouping_By_Not_Always_Produce_Full_Size_Map() {

        // Given & When
        Map<LifeCycle, Set<Plant>> classifiedPlants =
                Plant.classifyPlantsInEnumMapWithStream(SMALL_GARDEN);

        // Then
        assertThat(classifiedPlants.size()).isEqualTo(LifeCycle.values().length - 1);
    }

}///:~