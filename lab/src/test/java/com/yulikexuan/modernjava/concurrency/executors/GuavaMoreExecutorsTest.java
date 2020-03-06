//: com.yulikexuan.modernjava.concurrency.executors.GuavaMoreExecutorsTest.java


package com.yulikexuan.modernjava.concurrency.executors;


import com.google.common.util.concurrent.MoreExecutors;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test Guava's implementations of ExecutorService - ")
public class GuavaMoreExecutorsTest {

    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        ExecutorServiceConfiguration.terminateExecutorServece(executorService);
    }

    /*
     * The instance returned by the MoreExecutors.directExecutor() method is
     * actually a static singleton, so using this method does not provide any
     * overhead on object creation at all
     *
     * MoreExecutors::directExecutor returns an Executor that runs each task
     * in the thread that invokes execute, as in ThreadPoolExecutor.CallerRunsPolicy.
     *
     * The instance returned by the directExecutor() method is actually a static
     * singleton, so using this method does not provide any overhead on object
     * creation at all
     *
     * Should prefer this method to the MoreExecutors.newDirectExecutorService(),
     * because that API creates a full-fledged executor service implementation
     * on every call
     */
    @Test
    void test_Given_Direct_Executor_When_Running_Task_Then_Running_In_Current_Thread() {

        // Given
        executorService = MoreExecutors.newDirectExecutorService();

        final LongAdder longAdder = new LongAdder();

        long curId = Thread.currentThread().getId();
        String mainThreadName = Thread.currentThread().getName();

        // When
        executorService.execute(() -> {
            try {
                Thread.sleep(100L);
                // Then
                assertThat(Thread.currentThread().getName())
                        .isEqualTo(mainThreadName);
                assertThat(curId).isEqualTo(Thread.currentThread().getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            longAdder.increment();
        });
    }

    // Given_Preconditions_When_StateUnderTest_Then_ExpectedBehavior
    // Given_UserIsAuthenticated_When_InvalidAccountNumberIsUsedToWithdrawMoney_Then_TransactionsWillFail
    /*
     * The common problem is shutting down the virtual machine while a thread
     * pool is still running its tasks
     * Even with a cancellation mechanism in place, there is no guarantee that
     * the tasks will behave nicely and stop their work when the executor
     * service shuts down
     * This may cause JVM to hang indefinitely while the tasks keep doing their
     * work
     *
     * Guava introduces a family of exiting executor services
     * They are based on daemon threads which terminate together with the JVM
     *
     * These services also add a shutdown hook with the
     * Runtime.getRuntime().addShutdownHook() method and prevent the VM from
     * terminating for a configured amount of time before giving up on hung tasks
     *
     * In this test, submitting the task that contains an infinite loop,
     * but using an exiting executor service with a configured time of 100
     * milliseconds to wait for the tasks upon VM termination
     *
     * Without the exitingExecutorService in place, this task would cause the
     * VM to hang indefinitely
     *
     * MoreExecutors.getExitingExecutorService(ThreadPoolExecutor)
     *   - Converts the given ThreadPoolExecutor into an ExecutorService that
     *     exits when the application is complete
     *   - It does so by using daemon threads and adding a shutdown hook to wait
     *     for their completion
     *   - This method waits 120 seconds before continuing with JVM termination
     *     even if the executor has not finished its work
     *   - This is mainly for fixed thread pools
     *
     * MoreExecutors.getExitingExecutorService(ThreadPoolExecutor, Duration)
     *   - Specify how long to wait for the executor to finish before
     *     terminating the JVM
     */
    @Test
    @Disabled
    void test_Guava_Exiting_Executor_Service() {

        // Given
        ThreadPoolExecutor executor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(
                        ExecutorServiceConfiguration.NUMBER_OF_THREADS);

        executorService = MoreExecutors.getExitingExecutorService(
                executor, Duration.ofSeconds(4));

        String echo = ">>>>>>> Hello! I am running in an ExitingExecutorService!";

        // When
        this.executorService.submit(
                () -> {
                    while (true) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            System.out.println("------- I am interrupted.");
                        }
                        System.out.println(echo);
                    }
                });
    }

}///:~