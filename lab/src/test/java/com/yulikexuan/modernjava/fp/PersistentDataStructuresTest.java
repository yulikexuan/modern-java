//: com.yulikexuan.modernjava.fp.PersistentDataStructuresTest.java


package com.yulikexuan.modernjava.fp;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Functional Persistent Data Structures Test - ")
class PersistentDataStructuresTest {

    private TrainJourney montrealToKingston;
    private TrainJourney kingstonToToronto;

    private TrainJourney montrealToToronto;

    @BeforeEach
    void setUp() {
        this.montrealToKingston = TrainJourney.builder()
                .price(70)
                .build();
        this.kingstonToToronto = TrainJourney.builder()
                .price(50)
                .build();
    }

    @Test
    @DisplayName("Test destructive data updating - ")
    void test_Destructive_Data_Updates() {

        // Given
        this.montrealToToronto = PersistentDataStructures.destructiveAppend(
                this.montrealToKingston, this.kingstonToToronto);

        // When
        int finalPriceOfMontrealToKingston =
                PersistentDataStructures.calculateFinalPrice(
                        this.montrealToKingston);

        int finalPriceOfMontrealToToronto =
                PersistentDataStructures.calculateFinalPrice(
                        this.montrealToToronto);

        boolean destructived = finalPriceOfMontrealToKingston >=
                finalPriceOfMontrealToToronto;

        System.out.println(finalPriceOfMontrealToKingston);
        System.out.println(finalPriceOfMontrealToToronto);

        // Then
        assertThat(destructived)
                .as("Kingston should be the terminal station of " +
                        "montrealToKingston")
                .isTrue();
    }

    @Test
    @DisplayName("Test functional persistent data updates - ")
    void test_Functional_Persistent_Data_Updates() {

        // Given
        TrainJourney quebecCityToMontreal = TrainJourney.builder()
                .price(50)
                .build();

        this.montrealToToronto = PersistentDataStructures
                .append(this.montrealToKingston, this.kingstonToToronto);

        TrainJourney torontoToHamilton = TrainJourney.builder()
                .price(10)
                .build();

        TrainJourney hamiltonToNiagaraFall = TrainJourney.builder()
                .price(20)
                .build();

        TrainJourney torontoToNiagaraFall = PersistentDataStructures
                .append(torontoToHamilton, hamiltonToNiagaraFall);

        TrainJourney quebecCityToToronto = PersistentDataStructures
                .append(quebecCityToMontreal, this.montrealToToronto);

        TrainJourney quebecCityToNiagaraFall = PersistentDataStructures
                .append(quebecCityToToronto, torontoToNiagaraFall);

        // Then
        assertThat(PersistentDataStructures.calculateFinalPrice(
                quebecCityToMontreal)).isEqualTo(50);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                montrealToKingston)).isEqualTo(70);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                kingstonToToronto)).isEqualTo(50);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                montrealToToronto)).isEqualTo(120);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                torontoToHamilton)).isEqualTo(10);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                hamiltonToNiagaraFall)).isEqualTo(20);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                torontoToNiagaraFall)).isEqualTo(30);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                quebecCityToToronto)).isEqualTo(170);

        assertThat(PersistentDataStructures.calculateFinalPrice(
                quebecCityToNiagaraFall)).isEqualTo(200);
    }

}///:~