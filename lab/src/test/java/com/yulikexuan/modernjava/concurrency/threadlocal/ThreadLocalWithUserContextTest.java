//: com.yulikexuan.modernjava.concurrency.threadlocal.ThreadLocalWithUserContextTest.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


@DisplayName("Test Thread Safty without Using ThreadLocal - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ThreadLocalWithUserContextTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userRepository = new UserRepository();
    }

    @Test
    void test_Loading_User_Contexts_Concurrently_With_Using_ThreadLocal() {

        // Given
        ThreadLocalWithUserContext userContext_1 =
                ThreadLocalWithUserContext.of(1, userRepository);
        ThreadLocalWithUserContext userContext_2 =
                ThreadLocalWithUserContext.of(2, userRepository);

        // When
        new Thread(userContext_1).start();
        new Thread(userContext_2).start();

        // Then
        await().untilAsserted(() -> {
            assertThat(userContext_1.getUserName()).isEqualTo(
                    userRepository.getUsernameForUserId(1));
            assertThat(userContext_2.getUserName()).isEqualTo(
                    userRepository.getUsernameForUserId(2));
        });
    }

}///:~