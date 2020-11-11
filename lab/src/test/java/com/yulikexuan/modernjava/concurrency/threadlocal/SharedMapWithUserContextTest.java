//: com.yulikexuan.modernjava.concurrency.threadlocal.SharedMapWithUserContextTest.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.*;


@DisplayName("Test Thread Safty without Using ThreadLocal - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SharedMapWithUserContextTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = new UserRepository();
    }

    @Test
    void test_Loading_User_Contexts_Concurrently_Without_Using_ThreadLocal() {

        // Given
        SharedMapWithUserContext user_1 = SharedMapWithUserContext.of(
                1, this.userRepository);
        SharedMapWithUserContext user_2 = SharedMapWithUserContext.of(
                2, this.userRepository);

        // When
        new Thread(user_1).start();
        new Thread(user_2).start();

        // Then
        await().untilAsserted(() -> assertThat(
                SharedMapWithUserContext.USER_CONTEXT_PER_USERID.size())
                .isEqualTo(2));
    }

}///:~