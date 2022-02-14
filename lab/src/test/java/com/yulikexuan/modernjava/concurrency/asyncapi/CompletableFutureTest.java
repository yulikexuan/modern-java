//: com.yulikexuan.modernjava.concurrency.asyncapi.CompletableFutureTest.java


package com.yulikexuan.modernjava.concurrency.asyncapi;


import com.google.common.collect.ImmutableList;
import com.yulikexuan.modernjava.annotations.Person;
import com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceConfig;
import com.yulikexuan.modernjava.concurrency.executors.ExecutorServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


/*
 * https://community.oracle.com/docs/DOC-995305
 *
 * 1.  CompletableFuture is a CompletionStage
 *       - A CompletionStage is a model that carries a task
 *           - A task can be an instance of Runnable, Consumer, or Function
 *           - The task is an element of a chain
 *       - CompletionStage elements are linked together in different ways along
 *         the chain
 *       - An "upstream" element is a CompletionStage that is executed before
 *         the element we are considering
 *       - Consequently, a "downstream" element is a CompletionStage that is
 *         executed after the element we are considering
 *       - The execution of a CompletionStage is triggered upon the completion
 *         of one or more upstream  CompletionStages
 *       - Those CompletionStages might return values, and these values can be
 *         fed to this CompletionStage
 *       - The completion of this CompletionStage can also produce a result and
 *         trigger other downstream CompletionStages
 *       - So a CompletionStage is an element of a chain
 *       - The CompletionStage interface has an implementation called
 *         CompletableFuture
 *       - CompletableFuture is also an implementation of the Future interface
 *       - CompletionStage does not extend Future
 *
 * 2.  A task has a state
 *       - It might be running
 *       - It might be completed normally and might have produced a result
 *       - It might be completed exceptionally and might have produced an
 *         exception
 *
 * 3.  CompletableFuture is also a Future
 *       - Future has 5 methods in 3 categories
 *           - cancel()
 *           - isCanceled() & isDone()
 *           - get() & get(timeout, timeunit)
 *       - CompletableFuture adds 6 Future-like methods
 *           - join()
 *           - getNow(valueIfAbsent) Does not force the CompletableFuture to
 *             complete
 *           - complete(value) Completes the CompletableFuture if it has not
 *             been completed, and it sets its value to the passed value
 *               - If the CompletableFuture has already completed, its return value
 *                 is not changed
 *           - obtrudeValue(value) Change the value of the CompletableFuture,
 *             even if it has already completed
 *               - Should be used with care and only in error recovery situations
 *           - completeExceptionally(throwable) Throws an unchecked exception
 *             if the CompletableFuture has not completed
 *           - obtrudeException(Throwable ex) Forces the CompletableFuture to
 *             change its state
 *
 * 4. How to Create a CompletableFuture
 *      - CompletableFuture::completedFuture Creating a Completed CompletableFuture
 *      - Creating a CompletableFuture from a Task
 *          - A CompletableFuture can be built on two kinds of tasks
 *              - A Runnable, which does not take any argument and does not
 *                return anything
 *              - A Supplier, which also takes no argument and which produces
 *                an object
 *          - In both cases, it is possible to pass an Executor to set the pool
 *            of threads that will execute this task
 *          - There are two patterns for each task
 *              - CompletableFuture<Void> cf1 = CompletableFuture.runAsync(
 *                        Runnable runnable);
 *              - CompletableFuture<T> cf2 = CompletableFuture.supplyAsync(
 *                        Supplier<T> supplier);
 *          - If no ExecutorService is supplied, the tasks will be executed in
 *            the common fork/join pool, the same pool that is used for the
 *            parallel execution of streams
 *
 * 5. Building a CompletableFuture Chain
 *      - Tasks for an Element of the Chain
 *          - The first task is modeled by a Runnable or a Supplier, two
 *            functional interfaces (you could say functions), which do not
 *            take any argument and which might or might not return something
 *          - The second element of the chain and the further ones could take
 *            the result of the previous element, if there is one
 *          - So we need different functions on which to build those elements
 *              - The previous element of the chain might or might not produce a
 *                result
 *              - So our functions should take one object or no object
 *              - This element might or might not produce a result
 *              - So our functions should return one object or no object
 *              - That makes four possible cases
 *              - Among those four possible functions, the one that does not
 *                take any result and produces a value should be discarded,
 *                because it is not an element of a chain; it is the starting
 *                point of a chain, which we already saw in the previous section
 *              - Four possible functions:
 *                [Takes a param or not]      [Return NO Value]     [Return Value R]
 *                ---------------------------------------------------------------------------
 *                Takes T                     Consumer<T>           Function<T, R>
 *                Takes NOTHING               Runnable              Not an element of a Chain
 *     - Types of Chaining - What chaining means
 *         - The basic chaining:
 *             - The chaining is about triggering a task on the result of
 *               another task
 *             - The chaining is also about passing the result of the first one
 *               as a parameter to the second one
 *         - Compose:
 *             - We can also compose the elements instead of chaining them
 *             - This makes sense only for tasks that take the result of the
 *               previous task and provide an object wrapped in another
 *               CompletableFuture
 *             - This is once more a one-to-one relation (not chaining, because
 *               this is composition)
 *         -  A tree-like structure: a task is triggered on two upstream tasks
 *            instead of one
 *              - We can imagine a combination of the two provided results, or
 *                a situation in which the current element is triggered on the
 *                first upstream element, which can provide a result ???????????
 *     - Choosing an ExecutorService
 *         - One of our tasks might be updating a graphical user interface
 *           In that case, we want it to be run in the human-machine interface
 *           (HMI) thread. This is the case in Swing, JavaFX, and Android
 *         - We might have I/O tasks or computation tasks that need to be
 *           executed in specialized pools of threads
 *         - We might have visibility issues in our variables that need a
 *           further task to be executed in the same thread as the first one
 *         - We might want to execute this task asynchronously, in the default
 *           fork/join pool
 *     - Lots of Methods to Implement
 *         - Three types of tasks,
 *             - Times four types of chaining and composition,
 *             - Times three ways of specifying which ExecutorService we want
 *               this task to be run
 *         - We have 36 methods to chain tasks
 *         - This is probably one of the elements that make this class complex:
 *             - The high number of available methods
 *
 * 6.  Selection of Patterns
 *
 *       - One-to-One Patterns:
 *         In this case, from the first CompletableFuture, we create a
 *         CompletableFuture that will execute its task when the first one is
 *         completed
 *
 *       - Two-to-One Combining Patterns
 *           - Combining patterns are patterns in which the task we write takes
 *             the results of two upstream tasks
 *           - Two functions can be used in this case: BiFunction and BiConsumer
 *               - It is also possible to execute a Runnable in these patterns
 *           - Three base methods for two-to-one combining patterns
 *               - <U, R> CompletionStage<R> thenCombine(
 *                                                   CompletionStage<U> other,
 *                                                   BiFunction<T, U, R> action)
 *                 Combines the result of this and other in one, using a BiFunction
 *
 *               - <U> CompletionStage<Void> thenAcceptBoth(
 *                                                      CompletionStage<U> other,
 *                                                      BiConsumer<T, U> action)
 *                 Consumes the result of this and other, using a BiConsumer
 *
 *               - CompletableFuture<Void> runAfterBoth(CompletionStage<?> other,
 *                                                      Runnable action)
 *                 Triggers the execution of a Runnable on the completion of
 *                 this and other
 *       - Two-to-One Selecting Patterns
 *           - Instead of executing the downstream element once the two upstream
 *             elements are completed, the downstream element is executed when
 *             one of the two upstream elements is completed
 *           -  This might prove very useful when we want to resolve a domain
 *              name, for instance, instead of querying only one domain name
 *              server, we might find it more efficient to query a group of
 *              domain name servers. We do not expect to have different results
 *              from the different servers, so we do not need more answers than
 *              the first we get. All the other queries can be safely canceled
 *           - This time, the patterns are built on one result from the upstream
 *             element, because we do not need more.
 *           - These methods have the either key word in their names.
 *           - The combined elements should produce the same types of result,
 *             because only one of them will be selected
 *
 *           - <U> CompletionStage<U> applyToEither(
 *                     CompletionStage<? extends T> other,
 *                     Function<? super T, U> fn)
 *             Selects the first available result from this and other, and
 *             applies the function to it
 *
 *           - CompletableFuture<Void> acceptEither(
 *                     CompletionStage<? extends T> other,
 *                     Consumer<? super T> action)
 *             Selects the first available result from this and other, and
 *             passes it to the consumer
 *
 *           - CompletableFuture<Void> runAfterEither(
 *                     CompletionStage<?> other, Runnable action)
 *             Runs the provided action after the first result from this and
 *             other have been made available
 *
 */
@Slf4j
@DisplayName("CompletableFuture Test - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CompletableFutureTest {

    @BeforeEach
    void setUp() {
    }

    private static void takeMilliSecs(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nested
    @DisplayName("Oracle Tutorial Tests - ")
    class OracleTutorial {

        static final long RUNNING_TIME_MILLI = 200;

        @BeforeEach
        void setUp() {
        }

        final Supplier<List<Person>> allPersonsSupplier = () -> {
            takeMilliSecs(RUNNING_TIME_MILLI);
            return List.of(Person.of("Bill Gates", 72),
                    Person.of("Trump", 76),
                    Person.of("Miles", 51));
        };

        @Test
        void test_Creating_A_Completed_CompletableFuture_For_Testing() {

            // Given
            CompletableFuture<String> completedFuture =
                    CompletableFuture.completedFuture("I am done.");

            // When
            boolean done = completedFuture.isDone();
            // completedFuture.join();

            // Then
            assertThat(done).isTrue();
        }

        @Nested
        @DisplayName("The One to One using Patterns of CompletableFuture Tests - ")
        class OneToOnePatternTest {

            /*
             * thenApply​(Function<? super T,​? extends U> fn)
             * thenApplyAsync​(Function<? super T,​? extends U> fn)
             * thenApplyAsync​(Function<? super T,​? extends U> fn, Executor executor)
             */
            @Test
            void test_Given_An_Upstream_Element_Then_Apply_Its_Result_And_Produce_New_Result()
                    throws Exception {

                // Givne
                ExecutorService executorService = ExecutorServiceFactory
                        .createFixedPoolSizeExecutor(1);

                // When
                CompletableFuture.supplyAsync(allPersonsSupplier, executorService)
                        .thenApply(persons -> persons.toString())
                        .thenAccept(System.out::println);

                // Then
                ExecutorServiceConfig.terminateExecutorServeceAfter(executorService,
                        Duration.ofMillis(RUNNING_TIME_MILLI + 100));
            }

            /*
             * thenAccept(Consumer<? super T> action)
             * thenAcceptAsync(Consumer<? super T> action)
             * thenAcceptAsync(Consumer<? super T> action, Executor executor)
             */
            @Test
            void test_Given_An_Upstream_Element_Then_Consume_Its_Result() throws Exception {

                // Given
                ExecutorService executorService = ExecutorServiceFactory
                        .createFixedPoolSizeExecutor(1);

                CompletableFuture.supplyAsync(allPersonsSupplier, executorService)
                        .thenAccept(System.out::println);

                // Then
                ExecutorServiceConfig.terminateExecutorServeceAfter(executorService,
                        Duration.ofMillis(RUNNING_TIME_MILLI * 2));
            }

            /*
             * thenRun(Runnable action)
             * thenRunAsync(Runnable action)
             * thenRunAsync(Runnable action, Executor executor)
             */
            @Test
            void test_Given_An_Upstream_Element_Then_Run_Something_Without_Consuming_And_Producing_Nothing()
                    throws Exception {

                // Given
                final StopWatch stopWatch = StopWatch.create();

                final int numOfLoop = 2;

                Runnable task_0 = () -> {
                    IntStream.range(0, numOfLoop)
                            .forEach(i -> {
                                System.out.println("Running ... ...");
                                takeMilliSecs(RUNNING_TIME_MILLI);
                            });
                };

                Runnable task_1 = () -> {
                    IntStream.range(0, 1)
                            .forEach(i -> {
                                System.out.println("Running ... ...");
                                takeMilliSecs(300);
                            });
                };

                ExecutorService executorService = ExecutorServiceFactory
                        .createFixedPoolSizeExecutor(2);

                // When
                stopWatch.start();
                CompletableFuture<Void> cfVoid_0 = CompletableFuture.runAsync(
                        task_0, executorService);
                CompletableFuture<Void> cfVoid_1 = CompletableFuture.runAsync(
                        task_1, executorService);

                cfVoid_0.join();
                cfVoid_1.join();

                stopWatch.stop();
                long timeElapsed = stopWatch.getTime(TimeUnit.MILLISECONDS);
                assertThat(timeElapsed).isLessThan(450L);
                System.out.printf("%n>>> I am done. Time Elapsed: %d ms%n%n",
                        timeElapsed);

                // Then
                ExecutorServiceConfig.terminateExecutorServeceAfter(executorService,
                        Duration.ofMillis(RUNNING_TIME_MILLI));
            }

        }//: End of class OneToOnePatternTest

        /*
         * Different ways of Handling Exceptions in CompletableFutures
         *   - CompletionStage::handle
         *   - CompletionStage::exceptionally   (Suggested)
         *   - CompletionStage::whenComplete    (Suggested)
         */
        @Nested
        @DisplayName("Test Completing a Computation Exceptionally - ")
        class CompletableExceptionalFutureTest {

            private ExecutorService executor;
            private String expectedResultMsg;
            private Executor delayedExecutor;
            private String finalResultWhenComplete;

            @BeforeEach
            void setUp() {
                this.expectedResultMsg = "Message upon cancel! ";
                this.executor = Executors.newSingleThreadExecutor();
                this.delayedExecutor = CompletableFuture.delayedExecutor(
                        100, TimeUnit.MILLISECONDS, this.executor);
            }

            @AfterEach
            void tearDown() throws Exception {
                ExecutorServiceConfig.terminateExecutorServeceAfter(executor,
                        Duration.ofMillis(RUNNING_TIME_MILLI));
            }

            private String decodeMessage(String msg) {
                throw new RuntimeException(String.format(
                        "Failed to decode message %s", msg));
            }

            private String decodeMessageWell(String msg) {
                if (StringUtils.isAllBlank(msg)) {
                    throw new RuntimeException("Failed to decode blank message.");
                }
                return StringUtils.capitalize(msg);
            }

            /*
             * Using
             */
            @ParameterizedTest
            @ValueSource(strings = {"", "  ", "The quick brown fox jumps over a lazy dog."})
            void test_Given_CompoletableFuture_Completing_Exceptionally_Then_Using_Handle_Method_To_Handle(
                    String testMessage) throws Exception {

                // Given
                CompletableFuture<String> exceptionalFuture = CompletableFuture
                        .completedFuture(testMessage)
                        .thenApplyAsync(this::decodeMessageWell, this.delayedExecutor)
                        .handle((msg, exception) -> {
                            if (exception != null) {
                                log.error(">>>>>>> Decoding Exception - {}",
                                        exception.getMessage());
                                return exception.getMessage();
                            } else {
                                return msg;
                            }
                        });

                // When
                String decodedMsg = exceptionalFuture.join();

                // Then
                assertThat(decodedMsg).isNotNull();
            }

            @ParameterizedTest
            @ValueSource(strings = {"", "  ", "The quick brown fox jumps over a lazy dog."})
            void test_Given_CompoletableFuture_Completing_Exceptionally_Then_Using_Exceptionally_Method_To_Handle(
                    String testMessage) {

                // Given
                CompletableFuture<String> completableFuture = CompletableFuture
                        .completedFuture(testMessage)
                        .thenApplyAsync(this::decodeMessageWell, this.delayedExecutor)
                        .exceptionally(Throwable::getMessage);

                // When
                String decodedMsg = completableFuture.join();

                // Then
                assertThat(decodedMsg).isNotNull();
            }

            @ParameterizedTest
            @ValueSource(strings = {"", "  ", "The quick brown fox jumps over a lazy dog."})
            void test_Given_CompletableFuture_May_Complete_Exceptionally_Then_Using_whenComplete_To_Handle(
                    String testMessage) {

                // Given
                CompletableFuture<String> completableFuture = CompletableFuture
                        .completedFuture(testMessage)
                        .thenApplyAsync(this::decodeMessageWell, this.delayedExecutor)
                        .whenComplete((result, throwable) -> {
                            this.finalResultWhenComplete =
                                    Objects.isNull(throwable) ?
                                            result : throwable.getMessage();
                        });

                // When
                await().untilAsserted(() -> assertThat(this.finalResultWhenComplete).isNotNull());

                // Then
               log.info(this.finalResultWhenComplete);
            }

        }//: End of class CompletableExceptionalFutureTest

        @Nested
        @DisplayName("Test Two-to-One Combining & Selecting Patterns - ")
        class TwoToOnePatternsTest {

            static final String MSG_IN_CHINESE = "你好 GTV!";
            static final String MSG_IN_JAPANESE = "こんにちはGTV!";
            static final String EXPECTED_MSG = "こんにちはGTV! / 你好 GTV!";
            static final long EXPECTED_MAX_ELAPSED_TIME = 225L;

            private final Duration maxProcessingTime = Duration.ofMillis(200);
            private final Duration minProcessingTime = Duration.ofMillis(100);

            private String originalMsg;
            private String expectedTranslation;

            private Function<String, String> transToChineseFunc;
            private Function<String, String> transToJapaneseFunc;

            private BiFunction<String, String, String> combinedFunc;
            private Function<String, String> eitherFunc;

            @Mock
            private BiConsumer<String, String> msgConsumer;

            @Mock
            private Consumer<String> consumer;

            @Mock
            private Runnable action;

            private Executor shortDelayedExecutor;
            private Executor longDelayedExecutor;

            private CompletableFuture<String> cf_1;

            private StopWatch stopWatch;

            @BeforeEach
            void setUp() {
                originalMsg = "Hello GTV!";
                transToChineseFunc = msg -> MSG_IN_CHINESE;
                transToJapaneseFunc = msg -> MSG_IN_JAPANESE;
                expectedTranslation = EXPECTED_MSG;
                combinedFunc = (trans1, trans2) -> String.join(" / ",
                        trans1, trans2);
                eitherFunc = trans -> String.join(" : ",
                        "Translation", trans);
                shortDelayedExecutor =
                        CompletableFuture.delayedExecutor(
                                this.minProcessingTime.toMillis(),
                                TimeUnit.MILLISECONDS,
                                Executors.newSingleThreadExecutor());
                longDelayedExecutor =
                        CompletableFuture.delayedExecutor(
                                this.maxProcessingTime.toMillis(),
                                TimeUnit.MILLISECONDS,
                                Executors.newSingleThreadExecutor());
                cf_1 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToChineseFunc,
                                longDelayedExecutor);
                stopWatch = StopWatch.createStarted();
            }

            @AfterEach
            void tearDown() {
                if (!this.stopWatch.isStopped()) {
                    stopWatch.stop();
                }
            }

            @Test // CompletableFuture::thenCombine
            void test_Given_Two_Stages_Then_Combining_With_BiFunc_To_Generate_New_Value() {

                // Given
                CompletableFuture<String> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                shortDelayedExecutor)
                        .thenCombine(cf_1, combinedFunc);

                // When
                String resultMsg = cf_2.join();
                stopWatch.stop();
                long elapsedTime = stopWatch.getTime(TimeUnit.MILLISECONDS);

                // Then
                assertThat(resultMsg).isEqualTo(expectedTranslation);
                assertThat(elapsedTime).isLessThan(EXPECTED_MAX_ELAPSED_TIME);
            }

            @Test // CompletableFuture::thenAcceptBoth
            void test_Given_Two_Stages_Then_Combining_With_BiConsumer() {

                // Given
                CompletableFuture<Void> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                this.shortDelayedExecutor)
                        .thenAcceptBoth(this.cf_1, this.msgConsumer);

                // When
                cf_2.join();
                stopWatch.stop();
                long elapsedTime = stopWatch.getTime(TimeUnit.MILLISECONDS);

                // Then
                then(this.msgConsumer)
                        .should(times(1))
                        .accept(MSG_IN_JAPANESE, MSG_IN_CHINESE);

                assertThat(elapsedTime).isLessThan(EXPECTED_MAX_ELAPSED_TIME);
            }

            @Test // CompletableFuture::runAfterBoth
            void test_Given_Two_Stages_Then_Triggering_Runnable_After_Both_Completed() {

                // Given
                CompletableFuture<Void> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                this.shortDelayedExecutor)
                        .runAfterBoth(this.cf_1, this.action);

                // When
                cf_2.join();
                stopWatch.stop();
                long elapsedTime = stopWatch.getTime(TimeUnit.MILLISECONDS);

                // Then
                then(this.action)
                        .should(times(1))
                        .run();

                assertThat(elapsedTime).isLessThan(EXPECTED_MAX_ELAPSED_TIME);
            }

            @Test // CompletableFuture::applyToEither
            void test_Given_Two_Stages_Then_Apply_Func_On_The_First_Completed_One() {

                // Given
                CompletableFuture<String> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                this.shortDelayedExecutor)
                        .applyToEither(cf_1, this.eitherFunc);

                // When
                String finalTrans = cf_2.join();
                stopWatch.stop();
                long elapsed = this.stopWatch.getTime(TimeUnit.MILLISECONDS);

                // Then
                assertThat(finalTrans).endsWith(MSG_IN_JAPANESE);
                assertThat(elapsed).isLessThan(minProcessingTime.toMillis() + 30);
            }

            @Test // CompletableFuture::applyToEither
            void test_Given_Two_Stages_Then_Consume_The_First_Completed_One() {

                // Given
                CompletableFuture<Void> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                this.shortDelayedExecutor)
                        .acceptEither(cf_1, this.consumer);

                // When
                cf_2.join();
                stopWatch.stop();
                long elapsed = this.stopWatch.getTime(TimeUnit.MILLISECONDS);

                // Then
                assertThat(elapsed).isLessThan(minProcessingTime.toMillis() + 30);
                then(this.consumer).should(times(1))
                        .accept(MSG_IN_JAPANESE);
            }

            @Test // CompletableFuture::runAfterBoth
            void test_Given_Two_Stages_Then_Triggering_Runnable_After_The_First_Completed() {

                // Given
                CompletableFuture<Void> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                this.shortDelayedExecutor)
                        .runAfterEither(cf_1, this.action);

                // When
                cf_2.join();
                stopWatch.stop();
                long elapsed = this.stopWatch.getTime(TimeUnit.MILLISECONDS);

                // Then
                assertThat(elapsed).isLessThan(minProcessingTime.toMillis() + 30);
                then(this.action).should(times(1)).run();
            }

            @Test // CompletableFuture::thenCompose
            void test_Given_One_Stage_Then_Composing_Other_One() {

                // Given
                CompletableFuture<String> cf_2 = CompletableFuture
                        .completedFuture(originalMsg)
                        .thenApplyAsync(this.transToJapaneseFunc,
                                this.shortDelayedExecutor)
                        .thenCompose(japaneseMsg -> cf_1.thenApply(
                                chineseMsg -> String.join(" : ",
                                        chineseMsg, japaneseMsg)));

                // When
                String finalMsg = cf_2.join();

                // Then
                assertThat(finalMsg).isEqualTo("你好 GTV! : こんにちはGTV!");
            }

        }//: End of class TwoToOnePatternsTest

        @Nested
        @DisplayName("Many to One Patterns Test - ")
        class ManyToOnePatternsTest {

            private static final long DELAY = 100L;
            private static final int SIZE = 10;

            private Executor executor;
            private ThreadLocalRandom random;
            private StopWatch stopWatch;

            private double ave;
            private List<CompletableFuture<Double>> numFutures;

            @Captor
            ArgumentCaptor<Double> argCaptorDouble;

            @Mock
            DoubleConsumer dconsumser;

            @BeforeEach
            void setUp() throws Exception {

                this.random = ThreadLocalRandom.current();

                this.executor = CompletableFuture.delayedExecutor(
                        DELAY, TimeUnit.MILLISECONDS,
                        ExecutorServiceFactory.createFixedPoolSizeExecutor(SIZE));

                this.stopWatch = StopWatch.createStarted();

                this.numFutures = IntStream.range(0, SIZE)
                        .mapToObj(i -> CompletableFuture.supplyAsync(() ->
                                        this.random.nextDouble(0, 1),
                                this.executor))
                        .collect(ImmutableList.toImmutableList());
            }

            @AfterEach
            void tearDown() {
                if (!Objects.isNull(this.stopWatch) &&
                        !this.stopWatch.isStopped()) {
                    this.stopWatch.stop();
                }
            }

            @Test
            void test_Given_A_Collection_Of_CompletableFutures_Then_Calculate_After_All_Compoleted() {

                // Given
                CompletableFuture.allOf(numFutures.toArray(CompletableFuture[]::new))
                        .whenComplete((value, throwable) -> {
                            ave = numFutures.stream()
                                    .mapToDouble(f -> f.join())
                                    .average()
                                    .orElse(0);
                        }).join();

                // When
                this.stopWatch.stop();
                long slapsed = this.stopWatch.getTime();

                // Then
                assertThat(ave).isBetween(0D, 1D);
                assertThat(slapsed).isBetween(DELAY, DELAY + 50);
            }

            @Test
            void test_Given_A_Collection_Of_CompletableFutures_Then_Calculate_After_Any_Compoleted() {

                // Given
                CompletableFuture.anyOf(numFutures.toArray(CompletableFuture[]::new))
                        .whenComplete((value, throwable) -> {
                            dconsumser.accept((Double)value);
                        }).join();

                // When
                this.stopWatch.stop();
                long slapsed = this.stopWatch.getTime();

                // Then
                then(dconsumser).should(times(1))
                        .accept((Double)argCaptorDouble.capture());
                assertThat(this.argCaptorDouble.getValue()).isBetween(0D, 1D);
                assertThat(slapsed).isBetween(DELAY, DELAY + 50);
            }

        } //: End of class ManyToOnePatternsTest

    }//: End of class OracleTutorial

    @Nested
    @DisplayName("Test Using CompletableFuture as a Simple Future - ")
    class UsingAsASimpleFutureTest {

        @Test
        void test_Create_CompletableFuture_With_Known_Result() throws Exception {

            // Given
            String result = "Hello Asynchronous Programming!";
            CompletableFuture<String> completableFuture =
                    CompletableFuture.completedFuture(result);

            // When
            String actualResult = completableFuture.get();

            // Then
            assertThat(actualResult).isEqualTo(result);
        }

        @Test
        void test_Cancelling_CompletableFuture() throws Exception {

            // Given
            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            Executors.newFixedThreadPool(1).submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                completableFuture.cancel(false);
                return null;
            });

            // When
            assertThatThrownBy(() -> completableFuture.get()).isInstanceOf(
                    CancellationException.class);
        }

    }//: End of class UsingAsASimpleFutureTest

    @Nested
    @DisplayName("Test Using CompletableFuture with Encapsulaed Computation Logic - ")
    class UsingWithEncapsulatedComputationLogicTest {

        /*
         * Skip the boilerplate and simply execute some code asynchronously
         * with CompletableFuture::runAsync which returns a new CompletableFuture
         * that is asynchronously completed by a task running in the
         * ForkJoinPool.commonPool() after it runs the given action
         */
        @Test
        void test_Encapsulated_Computation_Logic_runAsync() {

            // Given
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(
                    () -> {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

            // When & Then
            assertTimeout(Duration.ofMillis(600), () -> completableFuture.join());
        }

        /*
         * CompletableFuture::supplyAsync Returns a new CompletableFuture that is
         * asynchronously completed by a task running in the ForkJoinPool.commonPool()
         * with the value obtained by calling the given Supplier.
         */
        @Test
        void test_Encapsulated_Computation_Logic_supplyAsync() {

            // Given
            long duration = 500;
            long timeout = duration + 100;
            long start = 10000;
            long ceiling = 100000;
            CompletableFuture<Long> completableFuture =
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(duration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ThreadLocalRandom.current()
                                .nextLong(start, ceiling);
                    });

            // When & Then
            assertTimeout(Duration.ofMillis(timeout), () ->
                    assertThat(completableFuture.join()).isBetween(start, ceiling));
        }

    }//: End of class UsingWithEncapsulatedComputationLogicTest

    @Nested
    @DisplayName("Test Processing Results of Asynchronous Computations - ")
    class ProcessingResultsOfAsyncComputationsTest {

        private long theFirstFutureThreadId;
        private long theSecondFutureThreadId;

        private boolean isRunning;

        void setUp() {
            this.isRunning = false;
            this.theFirstFutureThreadId = 0;
            this.theSecondFutureThreadId = 0;
        }

        /*
         * CompletionStage::thenApply Returns a new CompletionStage that,
         * when this stage completes normally, is executed with this stage's result
         * as the argument to the supplied function
         * This method is analogous to Optional.map and Stream.map.
         *
         * CompletionStage::thenApplyAsync Returns a new CompletionStage that,
         * when this stage completes normally, is executed using this stage's
         * default asynchronous execution facility, with this stage's result as the
         * argument to the supplied functionn
         */
        @Test
        void test_Processing_Results_With_thenApply_Function() {

            // Given
            CompletableFuture<String> secretKeyFuture =
                    CompletableFuture.supplyAsync(() -> {
                            theFirstFutureThreadId =
                                    Thread.currentThread().getId();
                            return RandomStringUtils.randomAlphanumeric(10);
                    });

            CompletableFuture<String> finalKeyFuture =
                    secretKeyFuture.thenApply(key -> {
                            theSecondFutureThreadId =
                                    Thread.currentThread().getId();
                            takeMilliSecs(200);
                            return StringUtils.upperCase(key);
                    });

            // When
            /*
             * The CompletableFuture.join() method is similar to the get method,
             * but it throws an unchecked exception in case the Future does not
             * complete normally.
             * This makes it possible to use it as a method reference in the
             * Stream.map() method.
             */
            String fullSecretKey = secretKeyFuture.join();

            // Then
            assertThat(fullSecretKey.length()).isEqualTo(10);
            assertThat(StringUtils.isAlphanumeric(fullSecretKey)).isTrue();
        }

        @Test
        void test_Processing_Results_With_thenApplyAsync_Function() {

            // Given
            CompletableFuture<String> secretKeyFuture =
                    CompletableFuture.supplyAsync(() -> {
                        theFirstFutureThreadId =
                                Thread.currentThread().getId();
                        return RandomStringUtils.randomAlphanumeric(10);
                    });

            CompletableFuture<String> finalKeyFuture =
                    secretKeyFuture.thenApplyAsync(key -> {
                        theSecondFutureThreadId =
                                Thread.currentThread().getId();
                        takeMilliSecs(200);
                        return StringUtils.upperCase(key);
                    });

            // When & Then
            await().untilAsserted(() -> assertThat(
                    this.theSecondFutureThreadId).isGreaterThan(0));
        }

        @Test
        void test_Processing_Results_With_thenAcceptAsync_Consumer() {

            // Given
            CompletableFuture<Void> secretKeyPrintFuture =
                    CompletableFuture.supplyAsync(
                            () -> RandomStringUtils.randomAlphanumeric(10))
                            .thenAcceptAsync(key -> {
                                takeMilliSecs(200);
                            });

            // When
            secretKeyPrintFuture.join();
        }

        @Test
        void test_Processing_Results_With_thenRun_Runnable() {

            // Given
            CompletableFuture<Void> secretKeyPrintFuture =
                    CompletableFuture.supplyAsync(
                            () -> RandomStringUtils.randomAlphanumeric(10))
                            .thenRun(() -> this.isRunning = true);

            // When
            secretKeyPrintFuture.join();

            // Then
            assertThat(this.isRunning).isTrue();
        }

    }//: End of class ProcessingResultsOfAsyncComputationsTest

    @Nested
    @DisplayName("Test Processing Results of Asynchronous Computations - ")
    class CombiningFuturesTest {

        private long theMainThreadId;
        private long theFirstFutureThreadId;
        private long theSecondFutureThreadId;
        private long theFinalThreadId;

        /*
         * CompletableFuture::thenCompose is like Stream::flatMap
         */
        @Test
        void test_FlatMap_Method_For_Stream() {

            // Given
            String[] testData = {
                    "The difference between thenApply and thenCompose.",
                    "CompletableFuture is the core of asynchronous programming."
            };

            // When
            List<String> units = Stream.of(testData)
                    .map(data -> data.split(""))
                    .flatMap(Arrays::stream)
                    .distinct()
                    .collect(ImmutableList.toImmutableList());

            // Then
        }

        /*
         * The method CompletableFuture::thenCompose takes a function that
         * returns a CompletableFuture instance
         *
         * The argument of this function is the result of the previous
         * computation step allowing to use this value inside the next
         * CompletableFuture‘s lambda
         *
         * The thenCompose method together with thenApply implement basic
         * building blocks of the monadic pattern
         *
         * They closely relate to the map and flatMap methods of Stream and
         * Optional classes also available in Java 8
         *
         * Both methods receive a function and apply it to the computation
         * result, but the thenCompose (flatMap) method receives a function
         * that returns another object of CompletableFuture
         *
         * This functional structure allows composing the instances of these
         * classes as building blocks
         */
        @Test
        void test_Combining_Futures_With_thenCompose() {

            // Given
            this.theMainThreadId = Thread.currentThread().getId();

            CompletableFuture<String> completableFuture_1 =
                    CompletableFuture.supplyAsync(() -> {
                            this.theFirstFutureThreadId =
                                    Thread.currentThread().getId();
                            return "Hello";
                    });

            Function<String, CompletableFuture<String>> composeFunc =
                    data -> CompletableFuture.supplyAsync(() -> {
                            this.theSecondFutureThreadId =
                                    Thread.currentThread().getId();
                            return data + " Asynchronous Programming!";
                    });

            CompletableFuture<String> completableFuture_2 =
                    completableFuture_1.thenCompose(composeFunc);

            // When
            String result = completableFuture_2.join();

            // Then
            assertThat(result).isEqualTo("Hello Asynchronous Programming!");
            assertThat(this.theFirstFutureThreadId).isNotEqualTo(
                    this.theMainThreadId);
            assertThat(this.theSecondFutureThreadId).isNotEqualTo(
                    this.theMainThreadId);
        }

        @Test
        void test_Combining_Two_Independent_Futures_With_thenCombine() {

            // Given
            this.theMainThreadId = Thread.currentThread().getId();

            CompletableFuture<String> completableFuture_1 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theFirstFutureThreadId =
                                Thread.currentThread().getId();
                        return "Hello";
                    });

            CompletableFuture<String> completableFuture_2 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theSecondFutureThreadId =
                                Thread.currentThread().getId();
                        return " Asynchronous Programming!";
                    });

            CompletableFuture<String> completableFuture_3 =
                    completableFuture_1.thenCombine(completableFuture_2,
                            (s1, s2) -> {
                                    this.theFinalThreadId =
                                            Thread.currentThread().getId();
                                    return String.join("", s1, s2);
                            });

            // When
            String result = completableFuture_3.join();

            // Then
            assertThat(this.theMainThreadId).isEqualTo(this.theFinalThreadId);
        }

        @Test
        void test_Combining_Two_Independent_Futures_With_thenAcceptBoth() {

            // Given
            this.theMainThreadId = Thread.currentThread().getId();

            CompletableFuture<String> completableFuture_1 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theFirstFutureThreadId =
                                Thread.currentThread().getId();
                        return "Hello";
                    });

            CompletableFuture<String> completableFuture_2 =
                    CompletableFuture.supplyAsync(() -> {
                        this.theSecondFutureThreadId =
                                Thread.currentThread().getId();
                        return " Asynchronous Programming!";
                    });

            CompletableFuture<Void> completableFuture_3 =
                    completableFuture_1.thenAcceptBoth(completableFuture_2,
                            (s1, s2) -> {
                                this.theFinalThreadId =
                                        Thread.currentThread().getId();
                            });

            // When
            await().untilAsserted(() -> assertThat(this.theFinalThreadId)
                    .isGreaterThan(0L));

            // Then
            assertThat(this.theMainThreadId).isEqualTo(this.theFinalThreadId);
        }

    }//: End of class CombiningFuturesTest

    @Nested
    @DisplayName("Handling Errors Tests - ")
    class HandlingErrorsTest {

        private Throwable innerException;

        @Test
        void test_Handling_Error_With_Handle_Method() {

            // Given
            String name = null;

            CompletableFuture<String> completableFuture =
                    CompletableFuture.supplyAsync(() -> {
                        if (Objects.isNull(name)) {
                            throw new RuntimeException("Computation Error!");
                        }
                        return "Hello, " + name;
                    });

            CompletableFuture<String> exceptioanlFuture =
                    completableFuture.handle((greeting, error) -> {
                            innerException = error;
                            return Objects.isNull(greeting) ?
                                    "Hello, Stranger!" : greeting;
                    });

            // When
            String finalGreeting = exceptioanlFuture.join();

            // Then
            assertThat(finalGreeting).isEqualTo("Hello, Stranger!");

            assertThat(this.innerException)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Computation Error!");
        }

    }//: End of class HandlingErrorsTest

    @Nested
    @DisplayName("JDK 9+ Features Test - ")
    class Jdk9PlusTest {

        static final int CODE_SIZE = 10;
        static final long DELAYED_MS = 1000;

        private String getRandomString() {
            return RandomStringUtils.randomAlphanumeric(10);
        }

        private String getRandomStringWithDelay() {
            try {
                Thread.sleep(DELAYED_MS / 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomStringUtils.randomAlphanumeric(10);
        }

        @Test
        void test_Given_Supplier_And_Delayed_Executor_When_completeAsync_Then_Executing_After_Delay() {

            // Given
            CompletableFuture<String> completableFuture = new CompletableFuture();

            completableFuture.completeAsync(() -> getRandomString(),
                    CompletableFuture.delayedExecutor(DELAYED_MS,
                            TimeUnit.MILLISECONDS));

            // When
            long start = System.nanoTime();
            String processedString = completableFuture.join();

            Duration duration = Duration.ofMillis(
                    (System.nanoTime() - start) / 1000_000);

            // Then
            assertThat(processedString).hasSize(CODE_SIZE);
            assertThat(duration.toMillis()).isBetween(DELAYED_MS, DELAYED_MS + 10);
        }

        @Test
        void test_Given_Input_Value_With_Timeout_Then_Complete_On_Timeout() {

            // Given
            CompletableFuture<String> completableFuture = new CompletableFuture<>();

            StopWatch stopWatch = StopWatch.createStarted();

            completableFuture.completeOnTimeout(getRandomStringWithDelay(),
                    DELAYED_MS / 2, TimeUnit.MILLISECONDS);

            // When
            String code = completableFuture.join();

            stopWatch.stop();

            // Then
            assertThat(code).hasSize(CODE_SIZE);
            assertThat(stopWatch.getTime(TimeUnit.MILLISECONDS)).isBetween(
                    DELAYED_MS, DELAYED_MS + 100);
        }

    }//: End of class Jdk9PlusTest

}///:~