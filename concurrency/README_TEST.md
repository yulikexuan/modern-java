# Testing Concurrent Programs

## Overview

### Potential failures may be rare probabalistic occurrences 
  - rather than deterministic ones

### Safety: Nothing bad ever happens

### Liveness: Something good eventually happens

### Steps of Building Tests
- #### Find a typical usage scenario
- #### Write a program that executes that scenario many times, and timing it
- #### Watch out for a number of coding pitfalls 
    - that prevent performance tests from yielding meaningful results


## Testing for Correctness

### Basic Unit Tests

#### The Rule: Including a set of sequential tests in current correctness test
  - To disclose when a problem is not related to concurrency issues


###  Strategies of Testing Blocking Operations

- #### Requires introducing more than one thread
- #### Relay success or failure information back to the main test runner thread 
- #### Every test must wait until all the threads it created terminate
- #### Make sure whether the tests passed
- #### Make sure whether the failure information is reported somewhere for diagnosing
- #### If a method is supposed to block under certain conditions 
    - then a test for that behavior should succeed only if the thread does not 
      proceed
    - If the method returns normally, the test should be failed
- #### Once the method successfully blocks, there must be a way to unblock it
    - Wait until the thread blocks 
    - Interrupt it
    - Assert that the blocking operation completed
- #### Code Example: [CorrectnessTest](src/test/java/com/yulikexuan/concurrency/testing/correctness/CorrectnessTest.java)


### Strategies of Testing Safety 

- [SafetyTest](src/test/java/com/yulikexuan/concurrency/testing/correctness/SafetyTest.java)

- #### Set up Multiple Threads Performing Different Operations (Put / Take)
    - over some amount of time
- #### Checking the Test Property Should Not Require any Synchronization
    - ``` AtomicInteger putSum = new AtomicInteger(0) ```
    - ``` AtomicInteger takeSum = new AtomicInteger(0) ```
    - Compute checksums of the elements enqueued and dequeued using Checksum Func 
        - The Checksum Func should be insensitive to the order of elements
    - Make the checksums themselves not be guessable by the compiler
- #### Use a ``` CyclicBarrier ``` to Control the Threads
    - to make sure that all threads are up and running before any start working
    - Initialize the barrier with the number of worker threads plus one
    - Make the worker threads and the test driver wait at the barrier at the
      beginning and end of their run
- #### Use More Active Threads than CPUs
- #### Runs test on Multiprocessor System


### Testing Resource Management

- [Verbose Garbage Collection in Java](https://www.baeldung.com/java-verbose-gc)
- [GCeasy](https://gceasy.io/gc-index.jsp#features)

#### The Goal: Detect & Avoid Storate Leaks

#### Forces a garbage collection 

#### Records information about the heap size and memory usage


### Using Callbacks

#### Callbacks to client-provided code can be helpful in constructing test cases

#### Callbacks are often made at known points in an object’s lifecycle 
  - that are good opportunities to assert invariants

#### Example 1.: Count how many thread created in a thread pool
- [TestingThreadFactory](src/main/java/com/yulikexuan/concurrency/testing/correctness/TestingThreadFactory.java)
- [ThreadPoolTest](src/test/java/com/yulikexuan/concurrency/testing/correctness/ThreadPoolTest.java)


### Generating More Interleavings

#### Yielding in the Middle of an Operation 
- May activate timing-sensitive bugs in code that does not use adequate 
  synchronization to access state
- Tech Note: The inconvenience of adding these calls for testing and removing them for
  production can be reduced by adding them using aspect-oriented programming
  (AOP) tools

#### Using ``` Thread.yeild ```

#### Using a short but nonzero sleep would be slower but more reliable


## Testing for Performance

### The Goal of Performance
- #### Seek to measure end-to-end performance metrics for representative use cases
- #### Select sizings empirically for 
    - Various bounds 
    - Numbers of threads 
    - Buffer capacities 
    - etc.


### Measure Performance
- #### Throughput: the rate at which a set of concurrent tasks is completed
- #### Responsiveness: the delay between a request for and completion of some action 
    - also called latency 
- #### Scalability: the improvement in throughput (or lack thereof) 
    -  as more resources usually CPUs) are made available


### Steps for Performance
- #### Find a typical usage scenario
- #### Write a program that executes that scenario many times, and time it
- #### Watch out for a number of coding pitfalls 
    - that prevent performance tests from yielding meaningful results


### Tech Notes
- #### Always include some basic functionality testing within performance tests 
    - to ensure not testing the performance of broken code


### Measure the Time Taken for a Run

#### How to get a more accurate measure 
- Timing the entire run and dividing by the number of operations to get a 
  per-operation time


### Plan the Performance Test
- The throughput of teh producer-consumer handoff operation for various
  combinations of parameters 
- How the bounded buffer scales with different numbers of threads (pairs)
- How to select the bound size (capacities)
- Answering questions above requires running the test for various combinations 
  of parameters


### Comparing Multiple Algorithms

#### ``` LinkedBlockingQueue ``` scales better than ``` ArrayBlockingQueue ```


### Measuring Responsiveness

#### It's important to know how long an individual action might take to complete
- Measure the variance of service time 
- Estimate the answers to quality-of-service questions
    - What percentage of operations will succeed in under 100 milliseconds?
- Histograms of task completion times are normally the best way to visualize
  variance in service time

#### For Example 
- Unfair semaphores provide much better throughput 
- Fair semaphores provide lower variance 


## Avoiding Performance Testing Pitfalls

### Overview

#### In practice, developers have to watch out for a number of coding pitfalls 
- that prevent performance tests from yielding meaningful results


### Garbage Collection

- [JVM Garbage Collectors](https://www.baeldung.com/jvm-garbage-collectors)
- [Verbose Garbage Collection in Java](https://www.baeldung.com/java-verbose-gc)
- [Understanding Java Garbage Collection Logging](https://sematext.com/blog/java-garbage-collection-logs/)

#### The timing of garbage collection is unpredictable
- There is always the possibility that the garbage collector will run during 
  a measured test run

#### Two Strategies for preventing garbage collection from biasing the test results
1. Ensure that the GC does not run at all during the test
     - Invoke the JVM with ``` -verbose:gc ``` to find out
2. Make sure that the GC runs a number of times during the run so that the test 
   program adequately reflects the cost of ongoing allocation and garbage collection 
     - Requires a longer test and is more likely to reflect real-world performance
     - For example, most producer-consumer applications involve a fair amount of 
       allocation and garbage collection
         - producers allocate new objects that are used and discarded by consumers
     - Running the bounded buffer test for long enough to incur multiple garbage
       collections yields more accurate results


### Dynamic Compilation

#### Java uses a combination of Bytecode Interpretation and Dynamic Compilation
- When a class is first loaded, the JVM executes it by interpreting the bytecode 
- At some point, if a method is run often enough, the dynamic compiler kicks in 
  and converts it to machine code 
    - When compilation completes, it switches from interpretation to direct execution
- Code may also be decompiled (reverting to interpreted execution) and recompiled 
  for various reasons

#### The timing tests should run only after all code has been compiled
- as the timing of compilation is unpredictable

#### Approaches
- Run test program for a long time (at least several minutes) so that 
    - compilation and interpreted execution represent a small fraction of the 
      total run time
- Use an unmeasured “warm-up” run, in which your code is executed enough to be 
  fully compiled when you actually start timing
- Running the test program with ``` -XX:+PrintCompilation ```
    - to print out a message when dynamic compilation runs, so can verify that 
      this is prior to, rather than during, measured test runs
- Running the same test several times in the same JVM instance
    - The first group of results should be discarded as warm-up
    - Seeing inconsistent results in the remaining groups suggests that the test 
      should be examined further to determine why the timing results are not 
      repeatable


### The JVM uses various background threads for housekeeping tasks

#### Approaches 
- When measuring multiple unrelated computationally intensive activities in a 
  single run, place explicit pauses between the measured trials to give the JVM
  a chance to catch up with background tasks with minimal interference from 
  measured tasks


### Unrealistic Sampling of Code Paths

#### Runtime compilers use profiling information to help optimize the code being compiled

#### The JVM is permitted to use information specific to the execution in order to
- Produce better code: means that compiling method M in one program may generate 
  different code than compiling M in another 
- In some cases, the JVM may make optimizations based on assumptions that may
  only be true temporarily, and later back them out by invalidating the compiled
  code if they become untrue

#### The test program should 
- not only adequately approximate the usage patterns of a typical application
- but also approximate the set of code paths used by such an application 


### Unrealistic Degrees of Contention

#### Concurrent applications tend to interleave two very different sorts of work
- Accessing shared data (fetching the next task from a shared work queue)
- Thread Local Computation (executing the task that does not access shared data)

#### The application will experience 
- Different levels of contention
- Exhibit different performance and scaling behaviors

#### No Contention
- N threads are fetching tasks from a shared work queue and executing them
- The tasks are compute-intensive and long-running (do not access shared date)
- Throughput is dominated by the availability of CPU resources

#### A Lot of Contention
- N threads are fetching tasks from a shared work queue and executing them
- The tasks are very short-lived
- There will be a lot of contention for the work queue
- Throughput is dominated by the cost of synchronization

#### The Contention Level to Offer Good Performance
- An app did a significant amount of thread-local computation for each time
  it accesses the shared data structure
- The contention level might be low enough to offer good performance


### Dead Code Elimination

#### Dead Code: code that has no effect on the outcome

#### Why Eliminate Dead Code
- Optimizing compilers are adept at (good at) spotting and eliminating dead code
- For a benchmark, this is a big problem because then we are measuring less
  execution than we think

#### Running with HotSpot’s ``` -server ``` Compiler other than ``` -client ```
- Many microbenchmarks perform much “better” when run with HotSpot’s ``` -server ``` 
  compiler than with ``` -client ```
    - not just because the server compiler can produce more efficient code, 
    - but also because it is more adept at optimizing dead code
- Prefer ``` -server ``` to ``` -client ``` for both production and testing on 
  multiprocessor systems

#### Writing Effective Performance Tests Requires Tricking the Optimizer
- into NOT Optimizing Away the Benchmark as Dead Code

#### Approches

- Compute the ``` hashCode ``` of the field of some derived object
    - compare it to an arbitrary value such as the current value of ``` System.nanoTime ```
    - and print a useless and ignorable message if they happen to match
    - the comparison will rarely succeed, and if it does, its only effect will
      be to insert a harmless space character into the output
    - The ``` System.out.print ``` method buffers output until
      ``` System.out.println ``` is called, so in the rare case that ``` hashCode ```
      and ``` System.nanoTime ``` are equal, no I/O is actually performed

      ``` 
        if (foo.x.hashCode() == System.nanoTime()) {
            System.out.print(" ");
        }
      ```

- Computed Result should also be Unguessable
    - otherwise, a smart dynamic optimizing compiler is allowed to replace 
      actions with precomputed results

- Any test program whose input is static data is vulnerable to the optimization