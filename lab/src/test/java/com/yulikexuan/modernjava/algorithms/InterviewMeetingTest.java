//: com.yulikexuan.modernjava.algorithms.InterviewMeetingTest.java

package com.yulikexuan.modernjava.algorithms;


import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DisplayName("Test InterviewMeetingTest - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InterviewMeetingTest {

    private InterviewMeeting meeting;


    @BeforeEach
    void setUp() {
        this.meeting = new InterviewMeeting();
    }


    @Test
    void able_To_Know_If_Being_Banlance_Or_Not() {

        // Given
        String testString = "()";

        // When
        boolean balance = this.meeting.isBalanced(testString);

        // Then
        assertThat(balance).isTrue();
    }

}///:~