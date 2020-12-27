//: com.yulikexuan.effectivejava.concurrency.threadlocal.SessionManagerTest.java

package com.yulikexuan.effectivejava.concurrency.threadlocal;


import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test ThreadLocal variables - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SessionManagerTest {

    @Test
    void when_Call_Session_Then_Get_Username_Back_In_Future() {

        // When
        List<String> usernames = SessionManager.startSessions();
        Collection<String> expectUsernames =
                UserRepository.INSTANCE.getAllUsernames();

        // Then
        assertThat(usernames).containsAll(expectUsernames);
    }

}///:~