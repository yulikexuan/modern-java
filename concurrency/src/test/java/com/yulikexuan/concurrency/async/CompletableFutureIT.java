//: com.yulikexuan.concurrency.async.CompletableFutureIT.java

package com.yulikexuan.concurrency.async;


import com.yulikexuan.concurrency.util.ExecutorServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.Awaitility.with;


@Slf4j
@DisplayName("Test CompletableFuture - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CompletableFutureIT {

    static final int DEFAULT_THREAD_POOL_SIZE = 7;
    static final long EXPECTED_BG_TASK_MILLIS = 1000;

    private static ExecutorService executor;
    private static StopWatch stopWatch;

    @BeforeAll
    static void beforeAll() throws Exception {
        executor = ExecutorServiceFactory.createFixedPoolSizeExecutor(
                DEFAULT_THREAD_POOL_SIZE);
        stopWatch = StopWatch.create();
    }

    @BeforeEach
    void setUp() throws Exception {
        stopWatch.reset();
    }

    @Nested
    @DisplayName("Getting Start - Creating new CompletableFuture Instances - ")
    class CreatingAndRuningCompletableFuture {

        @Test
        void test_Get_Method_Of_CompletableFuture_Is_A_Blocking_Method()
                throws Exception {

            // Given
            CompletableFuture<String> secretFuture = new CompletableFuture<>();

            // When & Then
            assertThatThrownBy(() -> await().atMost(Duration.ofMillis(300))
                    .until(() -> secretFuture.get() != null))
                    .isInstanceOf(ConditionTimeoutException.class);
        }

        @Test
        void test_Complete_Method_Is_Able_To_Manually_Complete_A_Future() throws Exception {

            // Given
            int defaultSecretLength = 17;

            String defaultResult = RandomStringUtils.randomAlphanumeric(
                    defaultSecretLength);
            String ignoredResult = RandomStringUtils.randomAlphanumeric(
                    defaultSecretLength + 2);

            CompletableFuture<String> secretFuture = new CompletableFuture<>();

            // Manually complete
            secretFuture.complete(defaultResult);

            // When
            secretFuture.complete(ignoredResult);
            secretFuture.complete(ignoredResult);

            with().pollDelay(Duration.ofMillis(3)).await()
                    .until(() -> secretFuture.get().equals(defaultResult));

            // Then
            assertThat(secretFuture.get()).isEqualTo(defaultResult);
        }

        /**
         * Returns a new CompletableFuture
         * Runs a DupeBackgroundTask in the given executor asynchronously
         * The returned CompletableFuture will be completed after finishing the task
         * Do not return anything from the task
         */
        @Test
        void test_Able_To_Run_A_Task_Asynchronously_In_A_Given_Executor() {

            // Given
            stopWatch.start();

            DupeBackgroundTask runnableTask = DupeBackgroundTask.of(
                    Duration.ofMillis(EXPECTED_BG_TASK_MILLIS));

            CompletableFuture<Void> future = CompletableFuture.runAsync(
                    runnableTask, executor);

            stopWatch.split();
            log.info(">>>>>>> The time of future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            future.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(future).isNotNull();
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);
            stopWatch.stop();
        }

        /**
         * Returns a new CompletableFuture
         * Runs a task in the given executor asynchronously
         * The returned CompletableFuture will be completed after finishing the task
         * Return the from the task
         */
        @Test
        void test_Able_To_Run_A_Task_Asynchronously_In_A_Given_Executor_And_Supply_A_Result() {

            // Given
            stopWatch.start();

            Supplier<String> secretSupplier = DupeSecretBackgroundTask.of(
                    Duration.ofMillis(EXPECTED_BG_TASK_MILLIS));

            CompletableFuture<String> future = CompletableFuture.supplyAsync(
                    secretSupplier, executor);

            stopWatch.split();
            log.info(">>>>>>> The time of future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            String newSecret = future.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(newSecret).hasSize(DupeSecretBackgroundTask.SECRET_LENGTH);
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);
            stopWatch.stop();
        }

    }//: End of class CreatingAndRuningCompletableFuture

    @Nested
    @DisplayName("Transforming CompletableFuture Result - ")
    class ResultTransforming {

        @Test
        void test_Able_To_Transform_The_Result_Of_CompletableFuture_With_Applying_Funcs() {

            // Given
            stopWatch.start();

            CompletableFuture<byte[]> future = CompletableFuture
                    .supplyAsync(DupeSecretBackgroundTask.of(
                            Duration.ofMillis(EXPECTED_BG_TASK_MILLIS)), executor)
                    .thenApply(String::getBytes);

            stopWatch.split();
            log.info(">>>>>>> The time of future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            byte[] newSecretBytes = future.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(newSecretBytes).hasSize(DupeSecretBackgroundTask.SECRET_LENGTH);
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);

            stopWatch.stop();
        }

        @Test
        void test_Able_To_Transform_The_Result_Of_CompletableFuture_More_Than_Once_With_Different_Funcs() {

            // Given
            stopWatch.start();
            DupeSecretBackgroundTask secretService = DupeSecretBackgroundTask.of(
                    Duration.ofMillis(EXPECTED_BG_TASK_MILLIS));

            InformationService informationService = InformationService.of(
                    Duration.ofMillis(EXPECTED_BG_TASK_MILLIS));

            // At least more than 2 seconds totally.
            CompletableFuture<String> future = CompletableFuture
                    .supplyAsync(secretService, executor) // at least 1 sec.
                    .thenApply(String::getBytes)
                    .thenApply(informationService); // at least 1 sec.

            stopWatch.split();
            log.info(">>>>>>> The time of future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            String secretInfo = future.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(secretInfo).hasSizeGreaterThan(
                    DupeSecretBackgroundTask.SECRET_LENGTH);
            assertThat(actualBgTaskMillis).isBetween(2000L, 2100L);

            stopWatch.stop();
        }

    }//: End of class ResultTransforming

    @Nested
    @DisplayName("Transforming CompletableFuture Result - ")
    class ResultConsuming {

        @Test
        void test_Able_To_Process_The_Result_Of_CompletableFuture_With_Accepting_Consumers() {

            // Given
            stopWatch.start();

            Supplier<String> secretSupplier = DupeSecretBackgroundTask.of(
                    Duration.ofMillis(EXPECTED_BG_TASK_MILLIS));

            CompletableFuture<Void> future = CompletableFuture
                    .supplyAsync(secretSupplier, executor)
                    .thenAccept(secret -> log.info(
                            ">>>>>>> I know the secret is {}", secret));

            stopWatch.split();
            log.info(">>>>>>> The time of future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            future.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);

            stopWatch.stop();
        }

        @Test
        void test_Able_To_Run_Callback_After_Completing_The_Previous_CompletableFuture_With_Runnable() {

            // Given
            stopWatch.start();

            Runnable bgTask1 = DupeBackgroundTask.of(
                    Duration.ofMillis(EXPECTED_BG_TASK_MILLIS));

            CompletableFuture<Void> future = CompletableFuture
                    .runAsync(bgTask1, executor)
                    .thenRun(() -> log.info(">>>>>>> The first task is done."));

            stopWatch.split();
            log.info(">>>>>>> The time of future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            future.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);

            stopWatch.stop();
        }

    }//: End of class ResultConsuming


    @Nested
    @DisplayName("Composing CompletableFutures Test - ")
    class DependentCompletableFuturesComposingTest {

        private UUID userId;
        private IUserServices userServices;

        @BeforeEach
        void setUp() throws Exception {
            this.userId = UUID.randomUUID();
            this.userServices = UserServices.of(ExecutorServiceFactory
                    .createFixedPoolSizeExecutor(7));
        }

        @Test
        void able_To_Flat_Embeded_CompletableFutures_With_Composing_Method() {

            // Given
            stopWatch.start();
            CompletableFuture<UserCreditRating> futureRating = this.userServices
                    .getUsersDetail(this.userId)
                    .thenCompose(this.userServices::getCreditRating);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            UserCreditRating rating = futureRating.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(2000L, 2100L);
            assertThat(rating).isNotNull();
            stopWatch.stop();
        }

    }//: End of class DependentCompletableFuturesComposingTest

    @Nested
    @DisplayName("Composing CompletableFutures Test - ")
    class IndependentCompletableFuturesCombiningTest {

        private UUID userId;
        private IUserServices userServices;

        @BeforeEach
        void setUp() throws Exception {
            this.userId = UUID.randomUUID();
            this.userServices = UserServices.of(ExecutorServiceFactory
                    .createFixedPoolSizeExecutor(7));
        }

        @Test
        void able_To_Combine_Results_From_Two_Independent_CompletableFutures() {

            // Given
            stopWatch.start();
            CompletableFuture<TaxReport> futureRating = this.userServices
                    .getUsersDetail(this.userId)
                    .thenCombine(this.userServices.getTaxInfo(this.userId),
                            TaxReport::of);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            TaxReport taxReport = futureRating.join();
            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);
            assertThat(taxReport).isNotNull();
            stopWatch.stop();
        }

    }//: End of class DependentCompletableFuturesComposingTest

    @Nested
    @DisplayName("Combining Multi-CompletableFutures Test")
    class CombiningMultiCompletableFuturesTest {

        private IImageService imageService;

        @BeforeEach
        void setUp() {
            this.imageService = ImageService.of(executor);
        }

        @Test
        void able_To_Combine_List_Of_CompletableFutures() {

            // Given
            List<String> uriInfo = List.of(
                    RandomStringUtils.randomAlphanumeric(17),
                    RandomStringUtils.randomAlphanumeric(17),
                    RandomStringUtils.randomAlphanumeric(17),
                    RandomStringUtils.randomAlphanumeric(17));

            stopWatch.start();

            CompletableFuture[] futureImages = uriInfo.stream()
                    .map(URI::create)
                    .map(this.imageService::downloadImage)
                    .toArray(CompletableFuture[]::new);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            CompletableFuture.allOf(futureImages).join();

            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);
            stopWatch.stop();
        }

        @Test
        void able_To_Pick_Up_The_First_Completed_CompletableFuture() {

            // Given
            RandomAdSupplierService supplierService =
                    RandomAdSupplierService.of(executor);

            stopWatch.start();

            CompletableFuture[] futures = IntStream.range(0, 3)
                    .mapToObj(i -> supplierService.getFutureSupplier(
                            (i + 1) * 500))
                    .toArray(CompletableFuture[]::new);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            Object ad = CompletableFuture.anyOf(futures).join();

            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(ad).isInstanceOf(Ad.class);
            assertThat(actualBgTaskMillis).isBetween(500L, 600L);
            stopWatch.stop();
        }

        @Test
        void able_To_Applly_Either_Result_Which_Completed_First_Among_Two_Futures() {

            // Given
            String fastAdName = "Fast Ad";
            String slowAdName = "Slow Ad";

            long fastTime = 1000L;
            long slowTime = 2000L;

            RandomAdSupplierService adSupplierService =
                    RandomAdSupplierService.of(executor);

            stopWatch.start();

            CompletableFuture<Ad> futureAdFast =
                    adSupplierService.getFutureSupplier(fastTime, fastAdName);

            CompletableFuture<Ad> futureAdSlow =
                    adSupplierService.getFutureSupplier(slowTime, slowAdName);

            CompletableFuture<Duration> futurePlayTime =
                    futureAdFast.applyToEither(futureAdSlow, Ad::getPlayTime);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            Duration playTime = futurePlayTime.join();

            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(playTime).isEqualTo(Duration.ofMillis(fastTime));
            assertThat(actualBgTaskMillis).isBetween(fastTime, fastTime + 100);
        }

        @Test
        void able_To_Accept_Either_Result_Which_Completed_First_Among_Two_Futures() {

            // Given
            String fastAdName = "Fast Ad";
            String slowAdName = "Slow Ad";

            long fastTime = 1000L;
            long slowTime = 2000L;

            RandomAdSupplierService adSupplierService =
                    RandomAdSupplierService.of(executor);

            stopWatch.start();

            CompletableFuture<Ad> futureAdFast =
                    adSupplierService.getFutureSupplier(fastTime, fastAdName);

            CompletableFuture<Ad> futureAdSlow =
                    adSupplierService.getFutureSupplier(slowTime, slowAdName);

            CompletableFuture<Void> futurePlayTime =
                    futureAdFast.acceptEither(futureAdSlow, Ad::play);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            futurePlayTime.join();

            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(fastTime, fastTime + 100);
        }

    }//: End of class CombiningMultiCompletableFuturesTogetherTest

    @Nested
    @DisplayName("Test the machanism of Exception Handling of CompletableFuture - ")
    class ExceptionHandlingTest {

        private IRandomSupplierService<Ad> adService;

        @BeforeEach
        void setUp() {
            this.adService = RandomAdSupplierService.of(executor);
        }

        @Test
        void test_The_Propagation_Of_Errors() {

            // Given
            CompletableFuture<Void> cFuture = ExceptionalCallbackChain
                    .getCallbackChainWithErrorInTheCore(executor);

            // When & Then
            assertThatThrownBy(() -> cFuture.join())
                    .isInstanceOf(CompletionException.class)
                    .hasMessageContaining(ExceptionalCallbackChain
                            .ERROR_MSG_ORIGINAL)
                    .hasCauseInstanceOf(RuntimeException.class);
        }

        @Test
        void able_To_Supply_Default_Value_When_Completed_With_Exception() {

            // Given
            CompletableFuture<Ad> adFuture =
                    adService.getUnstableFutureSupplier(3000L);

            // When
            Ad ad = adFuture.join();

            // Then
            assertThat(ad).isEqualTo(Ad.of(RandomAdSupplierService.DEFAULT_LONG_AD));
        }

        @Test
        void able_To_Compose_A_Back_Up_CompletableFuture_When_The_Previous_One_Completed_Exceptionally() {

            // Given
            stopWatch.start();

            CompletableFuture<Ad> backUpFutureAd =
                    adService.getBackupFutureSupplier(3000L);

            stopWatch.split();
            log.info(">>>>>>> The time of the final future returned: {} millis",
                    stopWatch.getSplitTime());
            stopWatch.unsplit();

            // When
            Ad backUpAd = backUpFutureAd.join();

            long actualBgTaskMillis = stopWatch.getTime();
            log.info(">>>>>>> The time after join: {} millis", actualBgTaskMillis);

            // Then
            assertThat(actualBgTaskMillis).isBetween(1000L, 1100L);
            assertThat(backUpAd).isEqualTo(Ad.of(
                    RandomAdSupplierService.BACK_UP_AD));
            stopWatch.stop();
        }

        @Test
        void able_To_Handle_Exception_Genericly_With_Null_Exception() {

            // Given
            CompletableFuture<Optional<Ad>> genericFutureAd =
                    this.adService.getGenericFutureSupplier(500L);

            // When
            Ad selectedAd = genericFutureAd.join().orElse(null);

            // Then
            assertThat(selectedAd).isEqualTo(Ad.of("Long Ad"));
        }

        @Test
        void able_To_Handle_Exception_Genericly_With_Exception() {

            // Given
            CompletableFuture<Optional<Ad>> genericFutureAd =
                    this.adService.getGenericFutureSupplier(3500L);

            // When
            Ad selectedAd = genericFutureAd.join().orElse(null);

            // Then
            assertThat(selectedAd).isNull();
        }

    }//: End of class ExceptionHandlingTest


}///:~