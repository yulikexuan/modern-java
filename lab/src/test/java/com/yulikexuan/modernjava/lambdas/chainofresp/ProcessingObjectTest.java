//: com.yulikexuan.modernjava.lambdas.chainofresp.ProcessingObjectTest.java


package com.yulikexuan.modernjava.lambdas.chainofresp;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;


class ProcessingObjectTest {

    static final String INPUT = "Aren't Labdas really sexy?!!";
    static final String EXPECTED_OUTPUT = "From Yul, Nic and Rene: " +
            "Aren't Lambdas really sexy?!!";

    @BeforeEach
    public void setUp() {
    }

    @Test
    void test_Given_Two_Processing_Objects_Then_Processing_Them_Together_As_A_Chain() {

        // Given
        ProcessingObject<String> headerProcessing = new HeaderTextProcessing();
        ProcessingObject<String> spellChecking = new SpellCheckerProcessing();

        headerProcessing.setSuccessor(spellChecking);

        // When
        String actualOutput = headerProcessing.handle(INPUT);

        // Then
        assertThat(actualOutput).isEqualTo(EXPECTED_OUTPUT);
    }

    @Test
    void test_Given_Unary_String_Functions_Then_Make_Chain_Of_Resp_By_Composing() {

        // Given
        UnaryOperator<String> headerProcessing =
                input -> "From Yul, Nic and Rene: " + input;

        UnaryOperator<String> spellChecking =
                input -> StringUtils.replaceIgnoreCase(input,
                        "labda", "Lambda");

        Function<String, String> processingPipeline =
                headerProcessing.andThen(spellChecking);

        // When
        String actualOutput = processingPipeline.apply(INPUT);

        // Then
        assertThat(actualOutput).isEqualTo(EXPECTED_OUTPUT);
    }

}///:~