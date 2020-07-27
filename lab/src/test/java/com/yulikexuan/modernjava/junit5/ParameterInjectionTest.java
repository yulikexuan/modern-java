//: com.yulikexuan.modernjava.junit5.ParameterInjectionTest.java


package com.yulikexuan.modernjava.junit5;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@DisplayName("JUnit 5 Parameter Injection Test - ")
// @ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ParameterInjectionTest {

    @RepeatedTest(5)
    public void test_Test_Repetition_Info(
            TestInfo testInfo, TestReporter testReporter,
            RepetitionInfo repetitionInfo) {

        String testMethodName = testInfo.getTestMethod().get().getName();
        int curRepetition = repetitionInfo.getCurrentRepetition();
        int totalRepetition = repetitionInfo.getTotalRepetitions();

        testReporter.publishEntry("secretMessage", "JUnit 5");

        assertThat(testMethodName).isEqualTo("test_Test_Repetition_Info");
        assertThat(totalRepetition).isEqualTo(5);
        assertThat(curRepetition).isLessThanOrEqualTo(totalRepetition);
    }

}///:~