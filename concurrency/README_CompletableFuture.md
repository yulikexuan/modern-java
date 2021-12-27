# ``` CompletableFuture ``` Tutorial


## Limitations of ``` Future ```


### 1. ``` Future ``` CANNOT be Manually Completed

#### [The Scenario]
- A function to fetch the latest price of an e-commerce product from a remote API
- Since this API call is time-consuming 
- Running it in a separate thread and returning a Future 

#### [The Goal]
- If the remote API service is down, then complete the Future manually by the 
  last cached price of the product 

#### [Possible?]
- NO


### 2. CANNOT Perform Further Action on a ``` Future ```’s Result without Blocking

- It's not possible to attach a callback function to the Future and have it get 
  called automatically when the ``` Future ```’s result is available 


### 3. Multiple Futures CANNOT be Chained Together

#### [The Goal]

- Execute a long-running computation and when the computation is done, send its 
  result to another long-running computation, and so on

#### [Possible?]
- NO

### 4. CANNOT Combine Multiple Futures Together 

#### [The Scenario]
- With 10 different Futures running in parallel 

#### [The Goal]
- And then run some function afterall of them completes

#### [Possible?]
- NO

### 5. No Exception Handling


## Use ``` CompletableFuture ``` to Achieve All of The Above


### Introduction 

- ``` CompletableFuture ``` implements Future and CompletionStage interface

  ``` 
  public class CompletableFuture<T> 
          extends Object 
          implements Future<T>, CompletionStage<T>
  ```

- ``` CompletableFuture ``` provides a huge set of convenience methods for 
  - creating
  - chaining
  - combining multiple Futures
  - Also has a very comprehensive exception handling support 


### Geting Start - Creating an Instance of ``` CompletableFuture ```

1.  Create an instance of ``` CompletableFuture ``` with no-arg Constructor

    ``` 
    CompletableFuture<String> completableFuture = new CompletableFuture<String>();
    ```
   
    - Get the result from the created instance
      - ``` String result = completableFuture.get() ```
        - The get() method blocks until the Future is complete
        - So, the above call will block forever because the Future is never 
          completed

    - Manually complete the ``` CompletableFuture ``` instance
      - ``` completableFuture.complete("Manual Result") ```
      - All the clients waiting for this ``` CompletableFuture ``` instance 
        get the specified result "Manual Result"
      - And, subsequent calls to completableFuture.complete() will be ignored


2.  Running a Task Asynchronously using runAsync()
    
    ```
    public static CompletableFuture<Void> runAsync(
            Runnable runnable, Executor executor)
    ```
    
    - Returns a new CompletableFuture
    - Runs a task in the given executor asynchronously
    - The returned CompletableFuture will be completed after finishing the task
    - Do not return anything from the task


3.  Run a Task Asynchronously and Return the Result using supplyAsync()

    ```
    public static <U> CompletableFuture<U> supplyAsync(
            Supplier<U> supplier, Executor executor)
    ```

    - Returns a new CompletableFuture
    - Runs a task in the given executor asynchronously
    - The returned CompletableFuture will be completed after finishing the task
    - Return the from the task


### Transforming and Acting on an Instance of ``` CompletableFuture ``` - Single Channel


#### [The Goal of ``` CompletableFuture ```]

> Be able to attach a callback to the instance of ``` CompletableFuture ``` 
> which should automatically get called when the Future completes

  - That way, we won’t need to wait for the result 
  - We can write the logic in the callback that needs to be executed after the 
    completion of the the instance of ``` CompletableFuture ```

#### [How]
  - Attach a callback to the instance of ``` CompletableFuture ``` using 
    ``` CompletionStage ```'s methods
    - ``` thenApply() ```
    - ``` thenAccept() ``` 
    - ``` thenRun() ```


1.  ``` thenApply() ``` a Func as the callback

    ``` 
    <U> CompletionStage<U> thenApply(Function<? super T, ? extends U> fn)
    ```
    
    - Returns a second new CompletionStage immediately
    - Process and transform the result of the first ``` CompletionStage ``` 
      when it completes normally
    - Executed the supplied function with the first ``` CompletionStage ```'s 
      result as the argument
    - Type parameter
      - T - The type of the first ``` CompletionStage ```
      - U - the function's return type
    - Parameters 
      fn - the function to compute the value of the returned 
           ``` CompletionStage ``` from the previous ``` CompletionStage ```
    - Example: 
      ``` 
      CompletableFutureResultTransforming::
      test_Able_To_Transform_The_Result_Of_CompletableFuture_With_Applying_Funcs 
      ```

2.  Attaching a series of thenApply() callback methods to a ``` CompletionStage ```
    - The result of one thenApply() method is passed to the next in the series 
    - Example:
      ``` 
      CompletableFutureResultTransforming::
      test_Able_To_Transform_The_Result_Of_CompletableFuture_More_Than_Once_With_Different_Funcs 
      ```

3.  Consuming the result of the previous ``` CompletionStage ```

    ``` 
    public CompletableFuture<Void> thenAccept(Consumer<? super T> action)
    ```

    - Returns a second new CompletionStage immediately
    - Process the result of the first ``` CompletionStage ```
      when it completes normally
    - Executed the supplied Consumer with the first ``` CompletionStage ```'s
      result as the argument
    - Type parameter
        - T - The type of the first ``` CompletionStage ```
    - Parameters
      action - the action to perform before completing the final returned 
      ``` CompletionStage ```
    - Example:
      ``` 
      ResultConsuming::
      test_Able_To_Process_The_Result_Of_CompletableFuture_With_Accepting_Consumers 
      ```

4.  Run callback code after completing the previous ``` CompletionStage ```

    ``` 
    public CompletableFuture<Void> thenRun(Runnable action)
    ```

    - Returns a second new CompletionStage immediately
    - Run callback code without accessiong the result of the previous 
      ``` CompletionStage ``` when it completes normally
    - Executed the callback code without accessing the first 
      ``` CompletionStage ```'s result
    - Parameters
      action - the callback code to perform before completing the final returned
      ``` CompletionStage ```
    - Example:
      ``` 
      ResultConsuming::
      test_Able_To_Run_Callback_After_Completing_The_Previous_CompletableFuture_With_Runnable 
      ```


### Combining two CompletableFutures Together - Two Channels

1.  Combine Two Dependent CompletableFutures Using ``` thenCompose() ```

    ``` 
    <U> CompletionStage<U> thenCompose(
            Function<? super T, ? extends CompletionStage<U>> fn)
    ```

    - Given the API to get ``` User ```: 
      ``` 
      CompletableFuture<User> getUsersDetail(UUID userId) 
      ```

    - Given another API to get ``` UserCreditRating ``` of a ``` User ```: 
      ```
      CompletableFuture<UserCreditRating> getCreditRating(User user)
      ```

    - Compose the two APIs together to get the final credit rating
    
    ``` 
    UserCreditRating rating = this.userServices
            .getUsersDetail(this.userId) 
            // CompletableFuture<User> 
            .thenCompose(this.userServices::getCreditRating) 
            //CompletableFuture<UserCreditRating>
            .join();
    ```
    
    - Without using thenCompose, 

    ``` 
    UserCreditRating rating = this.userServices
            .getUsersDetail(this.userId) 
            // return CompletableFuture<User> 
            .thenApply(this.userServices::getCreditRating) 
            // return CompletableFuture<CompletableFuture<UserCreditRating>>
            .join() // return CompletableFuture<UserCreditRating>
            .join() // return UserCreditRating
    ```


2.  Combine Two Independent Futures using ``` thenCombine() ```

    ``` 
    <U, V> CompletionStage<V> thenCombine(
            CompletionStage<? extends U> other, 
            BiFunction<? super T, ? super U, ? extends V> fn)
    ```
    
    ``` 
    <U, V> CompletionStage<V> thenCombineAsync(
            CompletionStage<? extends U> other, 
            BiFunction<? super T, ? super U, ? extends V> fn, 
            Executor executor)
    ```
    
    -  Two Futures to run independently and do something after both are complete

    - Given the API of ``` IUserServices ``` to get ``` User ``` with an user id, 
      an ``` UUID ```:
      ```
      CompletableFuture<User> getUsersDetail(UUID userId)
      ```

    - Given another API of ``` IUserServices ``` to get ``` TaxInfo ``` with 
      the same user id and ``` BiFunction ```: 
      ``` 
      CompletableFuture<TaxInfo> getTaxInfo(UUID userId);
      BiFunction<User, TaxInfo, TaxReport> taxReportBiFunc = 
              TaxReport.of(User user, TaxInfo taxInfo)
      ```

    - Accesses the tow APIs in the same timw, and combines the two APIs result 
      together to get the final ``` TaxReport ```
      ``` 
      CompletableFuture<User> futureUser = 
              this.userServices.getUsersDetail(this.userId);
      
      CompletableFuture<TaxInfo> futureTaxInfo = 
              this.userServices.getTaxInfo(this.userId);
      
      BiFunction<User, TaxInfo, TaxReport> taxReportBiFunc = 
              TaxReport.of(User user, TaxInfo taxInfo)
      
      CompletableFuture<TaxReport> futureRating = futureUser.thenCombine(
              futureTaxInfo, taxReportBiFunc);
      ```


3.  Combining Multiple ``` CompletableFutures ``` Together

    ``` 
    public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
    ```

    - Returns a new ``` CompletableFuture ``` that is completed when all of the 
      given ``` CompletableFuture ``` complete
      - If any of the given ``` CompletableFuture ``` complete exceptionally, 
        then the returned ``` CompletableFuture ``` also does so, with a 
        ``` CompletionException ``` holding this exception as its cause
      - Otherwise, the results, if any, of the given ``` CompletableFuture ``` 
        are not reflected in the returned ``` CompletableFuture ```, but may be 
        obtained by inspecting them individually
      - If no ``` CompletableFuture ``` are provided, returns a 
        ``` CompletableFuture ``` completed with the value null 

    - Among the applications of this method is to await completion of a set of 
      independent ``` CompletableFuture ```s before continuing a program, 
      as in: ``` CompletableFuture.allOf(c1, c2, c3).join() ```

    - ``` CompletableFuture.allOf ``` is used in scenarios when you have a List 
      of independent futures that you want to run in parallel and do something 
      after all of them are complete

    - Given an ``` Array ``` of ``` CompletableFutures ```
    
    ``` 
    CompletableFuture[] futureImages = uriInfo.stream()
        .map(URI::create)
        .map(this.imageService::downloadThenRenderImage)
        .toArray(CompletableFuture[]::new);
    ```
    
    - Run them with ``` allOf ``` in the same time together
    
    - Wait for them be completed with ``` join ```
    ``` 
    CompletableFuture.allOf(futureImages).join();
    ```


4.  Pick up the first completed ``` CompletableFuture ``` from an array of 
    ``` CompletableFuture ```s

    ``` 
    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
    ```
    
    - Returns a new CompletableFuture that is completed when any of the given
      ``` CompletableFuture ``` complete, with the same result
      - Otherwise, if it completed exceptionally, the returned 
        ``` CompletableFuture ``` also does so, with a 
        ``` CompletionException ``` holding this exception as its cause
      - If no ``` CompletableFuture ```s are provided, returns an incomplete
        ``` CompletableFuture ```

    - Given an array of CompletableFuture<Ad>, each of them fetch different 
      ``` Ad ``` information from different resource in different duration

      ``` 
      CompletableFuture[] futures = IntStream.range(0, 3)
        .mapToObj(i -> supplierService.getFutureSupplier(
                (i + 1) * 500))
        .toArray(CompletableFuture[]::new);
      ```

    - To use the fastest completed one, 

      ``` 
      Object ad = CompletableFuture.anyOf(futures).join();
      ```


### Exception Handling

0.  How errors are propagated in a callback chain

    - Given a ``` CompletableFuture ``` callback chain like: 
      ``` 
        static CompletableFuture<Void> getCallbackChainWithErrorInTheCore(
                final ExecutorService executor) {
    
            return CompletableFuture.supplyAsync(
                    () -> {
                        throw new RuntimeException(ERROR_MSG_ORIGINAL);
                    }, executor).thenApply(s -> {
                        return THE_FIRST_FUNC_INFO;
                    }).thenApply(s -> {
                        return THE_SECOND_FUNC_INFO;
                    }).thenAccept(s -> log.info(THE_FINAL_INFO));
        }
      ```

    - ``` 
        @Test
        void test_The_Propagation_Of_Errors() {

            // Given
            CompletableFuture<Void> cFuture = ExceptionalCallbackChain
                    .getCallbackChainWithErrorInTheCore(executor);

            // When & Then
            assertThatThrownBy(() -> cFuture.join())
                    .isInstanceOf(CompletionException.class)
                    .hasMessageContaining(
                            ExceptionalCallbackChain.ERROR_MSG_ORIGINAL)
                    .hasCauseInstanceOf(RuntimeException.class);
        }
      ```

    - None of the thenApply() callbacks will be called and future will be 
      resolved with the exception occurred

2.  Handling exceptions with ``` exceptionally() ``` callback and support
    a Default Value of the returned ``` CompletionStage ```

    ``` 
    CompletionStage<T> exceptionally(Function<Throwable, ? extends T> fn)
    ```
    
    - Returns a new ``` CompletionStage``` that, when this stage 
      completes exceptionally, is executed with this stage's exception 
      as the argument to the supplied function
      - Otherwise, if this stage completes normally, then the returned 
        stage also completes normally with the same value 

    - Parameters:
      ``` fn ``` - the function to use to compute the value of the 
      returned CompletionStage if this CompletionStage completed 
      exceptionally

    - Usage: The ``` exceptionally() ``` callback gives you a chance to 
      recover from errors generated from the original Future
      - A chance to log the exception here
      - Return a default value 


3.  Handling exceptions with ``` exceptionallyCompose ```

    ``` 
    CompletionStage<T> exceptionallyCompose(
            Function<Throwable, ? extends CompletionStage<T>> fn)
    
    CompletionStage<T> exceptionallyComposeAsync(
            Function<Throwable, ? extends CompletionStage<T>> fn, 
            Executor executor)
    ```

    - Returns a new CompletionStage that, when this stage completes 
      exceptionally, is composed using the results of the supplied 
      function applied to this stage's exception

    - Given 
    
    ``` 
    public CompletableFuture<Ad> getBackupFutureSupplier(long millis) {

        CompletableFuture<Ad> backUpAdFuture = getFutureSupplier(1000L);

        return CompletableFuture.supplyAsync(() -> {
            if (millis > SERVICE_TIME_OUT_MILLIS) {
                throw new AdNotFoundException();
            }
            return Ad.of("Long Ad");
        }).exceptionallyComposeAsync(e -> backUpAdFuture, executor);
    }
    ```

4.  Handle exceptions using the generic ``` handle() ``` method

    ``` 
    CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn)
    ```
    
    - This API provides a more generic method - ``` handle() ``` to recover from 
      exceptions
      - It is called whether or not an exception occurs 

    - Returns a new ``` CompletionStage ``` that, when this stage completes 
      either normally or exceptionally, is executed with this stage's result 
      and exception as arguments to the supplied function 

    - When this stage is complete, the given function is invoked with the result 
      (or null if none) and the exception (or null if none) of this stage as 
      arguments, and the function's result is used to complete the returned 
      stage 

    - Type Parameters: ``` U ``` - the function's return type

    - Parameters: ``` fn ``` 
      - the function to use to compute the value of the returned CompletionStage

    - Returns: the new ``` CompletionStage ```

    - Given a handler: 
    
    ``` 
    BiFunction<Ad, Throwable, Optional<Ad>> exceptionHandler =
            (ad, exception) -> Objects.isNull(exception) ?
                    Optional.ofNullable(ad) : Optional.empty();
    ```

    - Create CompletableFuture with the given handler

    ``` 
    @Override
    public CompletableFuture<Optional<Ad>> getGenericFutureSupplier(long millis) {
        return CompletableFuture.supplyAsync(() -> {
            if (millis > SERVICE_TIME_OUT_MILLIS) {
                throw new AdNotFoundException();
            }
            return Ad.of("Long Ad");
        }).handle(exceptionHandler);
    }
    ```


## Usage Patterns

[CompletableFuture for Asynchronous Programming in Java 8](https://community.oracle.com/tech/developers/discussion/4418058/completablefuture-for-asynchronous-programming-in-java-8)


### One-to-One Patterns

1.  ``` thenApply ```

    ``` 
    <U> CompletionStage<U> thenApply(Function<? super T, ? extends U> fn)
    
    <U> CompletionStage<U> thenApplyAsync(
            Function<? super T, ? extends U> fn, 
            Executor executor)
    ```

2.  ``` thenAccept ```

    ``` 
    CompletionStage<Void> thenAccept(Consumer<? super T> action)
    
    CompletionStage<Void> thenAcceptAsync(
            Consumer<? super T> action, 
            Executor executor)
    ```

3. ``` thenRun ```

   ``` 
   CompletionStage<Void> thenRun(Runnable action) 
   
   CompletionStage<Void> thenRunAsync(Runnable action, Executor executor)
   ```


### Two-to-One Combining Patterns

1.  ``` thenCombine ```

    ``` 
    <U, V> CompletionStage<V> thenCombine(
            CompletionStage<? extends U> other, 
            BiFunction<? super T, ? super U, ? extends V> fn)

    <U, V> CompletionStage<V> thenCombineAsync(
            CompletionStage<? extends U> other, 
            BiFunction<? super T, ? super U, ? extends V> fn, 
            Executor executor)
    ```

2.  ``` thenAcceptBoth ```

    ``` 
    <U> CompletionStage<Void> thenAcceptBoth(
            CompletionStage<? extends U> other, 
            BiConsumer<? super T, ? super U> action)

    <U> CompletionStage<Void> thenAcceptBothAsync(
            CompletionStage<? extends U> other, 
            BiConsumer<? super T, ? super U> action, 
            Executor executor)
    ```

3.  ``` runAfterBoth ```

    ``` 
    CompletionStage<Void> runAfterBoth(
            CompletionStage<?> other, Runnable action)
    
    CompletionStage<Void> runAfterBothAsync(
            CompletionStage<?> other, Runnable action,  Executor executor)
    ```


### Two-to-One Selecting Patterns

0.  Overview
    - Instead of executing the downstream element once the two upstream elements 
      are completed, the downstream element is executed when one of the two 
      upstream elements is completed

    - Usage Example: Resolve a domain name
      - Instead of querying only one domain name server, we might find it more 
        efficient to query a group of domain name servers
        - We do not expect to have different results from the different servers, 
          so we do not need more answers than the first we get, then all the 
          other queries can be safely canceled

    - Thess patterns are built on one result from the upstream element, because 
      we do not need more
      - These methods have the either key word in their names
      - The combined elements should produce the same types of result, because 
        only one of them will be selected

1.  ``` applyToEither ``` 

    ``` 
    <U> CompletionStage<U> applyToEither(
            CompletionStage<? extends T> other, Function<? super T, U> fn)
    
    <U> CompletionStage<U> applyToEitherAsync(
            CompletionStage<? extends T> other, 
            Function<? super T, U> fn, 
            Executor executor)
    ```
    
2.  ``` acceptEither ```

    ``` 
    CompletionStage<Void> acceptEither(
            CompletionStage<? extends T> other, 
            Consumer<? super T> action)
    
    CompletionStage<Void> acceptEitherAsync(
           CompletionStage<? extends T> other, 
           Consumer<? super T> action, 
           Executor executor)
    ```
    
3.  ``` runAfterEither ```

    ``` 
    CompletionStage<Void> runAfterEither(
            CompletionStage<?> other, Runnable action)
    
    CompletionStage<Void> runAfterEitherAsync(
            CompletionStage<?> other, Runnable action, Executor executor)
    ```
    

    - Also ``` anyOf ```

    ``` 
    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
    ```