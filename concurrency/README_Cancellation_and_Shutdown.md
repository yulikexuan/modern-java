# Task Cancellation and Shutdown

- [CompletableFuture](README_CompletableFuture.md)
- [Applying Thread Pools](README_Applying_Thread_Pools.md)

## Executing Tasks in Threads


### Overview

#### Identifying Sensible Task Boundaries
- coupled with a sensible task execution policy


### Executing Tasks in Threads

#### Disadvantages of Unbounded Thread Creation

- Thread Lifecycle Overhead 
    - Thread creation and teardown are not free
    - Introducing latency into request processing
    - Requires some processing activity by the JVM and OS
    - Creating a new thread for each request can consume Significant computing 
      resources

- Resource Consumption
    - Active threads consume system resources, especially memory
    - When there are more runnable threads than available processors, threads 
      sit idle 
    - Having many idle threads can 
        - tie up a lot of memory
        - putting pressure on the garbage collector 
        - and having many threads competing for the CPUs can impose other 
          performance costs as well
    - If having enough threads to keep all the CPUs busy, creating more threads 
      won’t help and may even hurt

- Stability 
    - There is a limit on how many threads can be created
    - The limit varies by platform
    - The limit is affected by factors including 
        - JVM invocation parameters
        - the requested stack size in the Thread constructor 
        - and limits on threads placed by the underlying OS 
        - When hitting this limit, the most likely result is an OutOfMemoryError
        - Trying to recover from such an error is very risky
        - It is far easier to structure the program to avoid hitting this limit

#### Place some bound on how many threads your application creates

#### Test the application thoroughly to ensure that 
- even when the bound is reached, it does not run out of resources 

#### Problems surfacing When the app is deployed and under heavy load 


## The Executor Framework

### Overview
- The Executor Framework provides a standard means of decoupling task 
  submission from task execution
- The Executor implementations also provide 
    - lifecycle support 
    - hooks for adding statistics gathering 
    - app management 
    - app monitoring
- Executor is based on the producer-consumer pattern
    - where activities that submit tasks are the producers (producing units 
      of work to be done) 
    - and the threads that execute tasks are the consumers (consuming those 
      units of work) 


### Execution Policies

#### What is Execution Policy 
- An execution policy specifies the “what, where, when, and how” of task 
  execution, including
    - In what thread will tasks be executed?
    - In what order should tasks be executed (FIFO, LIFO, priority order)?
    - How many tasks may execute concurrently?
    - How many tasks may be queued pending execution?
    - If a task has to be rejected because the system is overloaded, 
      which task should be selected as the victim 
        - how should the application be notified?
    - What actions should be taken before or after executing a task?


### Thread Pools

#### A thread pool manages a homogeneous pool of worker threads 

#### A thread pool is tightly bound to a work queue 
- holding tasks waiting to be executed 

#### The Simple Life of Worker Threads 
- request the next task from the work queue 
- execute it 
- go back to waiting for another task

#### Advantages of Executing Tasks in Thread Pool
- Reusing an existing thread instead of creating a new one
- The latency associated with thread creation does not delay task execution 
    - thus improving responsiveness 
- Having enough threads to keep the processors busy by properly tuning the size 
  of the thread pool
    - while not having so many that the app runs out of memory
    - thrashes (顛簸) due to competition among threads for resources


#### Predefined Configurations of Thread Pools

``` 
public static ExecutorService newFixedThreadPool(
        int nThreads, 
        ThreadFactory threadFactory) 
```
- ``` nThreads ``` the number of threads in the pool
- ``` threadFactory ``` the factory to use when creating new threads
- Creates a thread pool that reuses a fixed number of threads operating
  off a shared unbounded queue
- <span style="color:#FF7700">___If fewer than ``` nThreads ``` threads are running, a new thread is 
  created to handle the request, even if other worker threads are idle___</span>
- Using the provided ThreadFactory to create new threads when needed
- Attempts to keep the pool size constant
- Adding new threads if a thread dies due to an unexpected ``` Exception ```
- At any point, at most nThreads threads will be active processing tasks
- If additional tasks are submitted when all threads are active, 
  they will wait in the queue until a thread is available
- If any thread terminates due to a failure during execution prior to 
  shutdown, a new one will take its place if needed to execute subsequent 
  tasks
- The threads in the pool will exist until it is explicitly shutdown 


``` 
public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory)
```
- Creates a thread pool that creates new threads as needed 
- Will reuse previously constructed threads when they are available 
- Uses the provided ThreadFactory to create new threads when needed
- A cached thread pool has more flexibility to reap (obtain) idle threads 
  when the current size of the pool exceeds the demand for processing
- Add new threads when demand increases 
- Places No BOUNDS on the size of the POOL


``` 
public static ScheduledExecutorService newSingleThreadScheduledExecutor(
        ThreadFactory threadFactory)
```
- Creates a single-threaded executor that can schedule commands to run after a 
  given delay, or to execute periodically 
- However that if this single thread terminates due to a failure during 
  execution prior to shutdown, a new one will take its place if needed to 
  execute subsequent tasks 
- Tasks are guaranteed to execute sequentially, and no more than one task will 
  be active at any given time 
- Unlike the otherwise equivalent ``` newScheduledThreadPool(1, threadFactory) ``` 
  the returned executor is guaranteed not to be reconfigurable to use additional 
  threads
- A single-threaded executor creates a single worker thread to process tasks
- Replacing it if it dies unexpectedly 
- Tasks are guaranteed to be processed sequentially according to the order 
  imposed by the task queue (FIFO, LIFO, priority order)
- Single-threaded executors also provide sufficient internal synchronization to 
  guarantee that any memory writes made by tasks are visible to subsequent tasks
    - this means that objects can be safely confined to the “task thread” even 
      though that thread may be replaced with another from time to time


``` 
public static ScheduledExecutorService newScheduledThreadPool(
        int corePoolSize, ThreadFactory threadFactory)
```
- Creates a thread pool that can schedule commands to run after a given delay, 
  or to execute periodically
- ``` corePoolSize ``` the number of threads to keep in the pool, even if they 
- are idle
- A fixed-size thread pool that supports delayed and periodic task execution, 
  similar to Timer

#### Benifits of Using Thread Pool Executor
- Opens the door to all sorts of additional opportunities for 
  - tuning 
  - Management 
  - Monitoring 
  - Logging 
  - Error Reporting 
  - Other possibilities that would have been far more difficult to add 
    without a task execution framework


### Executor Lifecycle

#### Running
- ``` ExecutorServices ``` are initially created in the running state

#### Shutting Down
- The JVM can’t exit until all the (nondaemon) threads have terminated 
- Failing to shut down an Executor could prevent the JVM from exiting
- The state of previously submitted tasks
    - May have completed 
    - May be currently running 
    - May be queued awaiting execution
- ___Graceful Shut Down___
    - Finish what having started but don’t accept any new work
- ___Abrupt Shut Down___
    - Turn off the power to the machine room
- Feed back information to the application about the status of tasks that were 
  affected by the shutdown
- ___``` ExecutorService ``` Methods of Lifecycle Management___
  ```
  void shutdown()
  ```
    - Graceful Shut Down 
    - Initiates an orderly shutdown in which previously submitted tasks are 
      executed
    - Previously submitted tasks are allowed to complete, including those that
      have not yet begun execution
    - No new tasks will be accepted 
    - Invocation has no additional effect if already shut down 
    - This method does not wait for previously submitted tasks to complete 
      execution, use ``` awaitTermination ``` to do that
  ``` 
  List<Runnable> shutdownNow()
  ```
    - Abrupt Shut Down
    - Attempts to stop all actively executing tasks 
    - Halts the processing of waiting tasks 
    - Attempts to cancel outstanding tasks and does not start any tasks that
      are queued but not begun
    - Returns a list of the tasks that were awaiting execution
    - This method does not wait for actively executing tasks to terminate, use 
      ``` awaitTermination ``` to do that
    - There are no guarantees beyond best-effort attempts to stop processing 
      actively executing tasks
    - Typical implementations will cancel via ``` Thread.interrupt() ``` 
        - so any task that fails to respond to interrupts may never terminate
  ``` 
  boolean isShutdown()
  ```
    - Returns true if this executor has been shut down
  ``` 
  boolean isTerminated()
  ```
    - Returns ``` true ``` if all tasks have completed following shut down 
    - ``` isTerminated ``` is never ``` true ``` unless either shutdown or 
      shutdownNow was called first
  ``` 
  boolean awaitTermination(long timeout, TimeUnit unit) 
          throws InterruptedException
  ```
    - Blocks until all tasks have completed execution after a shutdown request 
    - Or the timeout occurs, or the current thread is interrupted, whichever 
      happens first 
- Tasks submitted to an ``` ExecutorService ``` after it has been shut down are 
  handled by the rejected execution handler ``` RejectedExecutionHandler ```
    - A handler for tasks that cannot be executed by a ``` ThreadPoolExecutor ```
    - Might silently discard the task 
    - or might cause execute to throw the unchecked ``` RejectedExecutionException ```
  ```
  public ThreadPoolExecutor(
          int corePoolSize,
          int maximumPoolSize,
          long keepAliveTime,
          TimeUnit unit,
          BlockingQueue<Runnable> workQueue,
          RejectedExecutionHandler handler)
  ```


#### Terninated
- Once all tasks have completed, the ``` ExecutorService ``` transitions to the 
  Terminated State
- Wait for an ``` ExecutorService ``` to reach the terminated state with 
  ``` awaitTermination ``` method 
    - or poll for whether it has yet terminated with ``` isTerminated ```
- It is common to follow ``` shutdown ``` immediately by ``` awaitTermination ```
    - Creating the effect of synchronously shutting down the ``` ExecutorService ```


### Delayed & Periodic Tasks

#### Problems of ``` java.util.Timer ```
- Single Thread
- ``` java.util.TimerTask::run ``` does not catch Exception
  - Unchecked ```Exception ``` thrown from a ``` TimerTask ``` terminates the 
    timer thread 
  - ``` Timer ``` also doesn’t resurrect the thread in this situation
      - instead, it erroneously assumes the entire Timer was cancelled
      - ``` TimerTasks ``` that are already scheduled but not yet executed are 
        never run, and new tasks cannot be scheduled

#### ``` ScheduledThreadPoolExecutor ``` deals properly with ill-behaved tasks


## Finding Exploitable Parallelism

### [``` Callable ``` & ``` Runnable ``` & ``` Future ```](https://www.baeldung.com/java-runnable-callable)

#### The Lifecycle of a Task Executed by an ``` Executor ```
- created 
- submitted 
- started 
- completed

#### Cancel Tasks
- In the Executor Framework, tasks that have been submitted but not yet started 
  can always be cancelled
- Tasks that have started can sometimes be cancelled if they are responsive to 
  interruption
- Cancelling a task that has already completed has no effect

#### [``` Future<V> ```](https://www.baeldung.com/java-future)
- Represents the lifecycle of a task and provides methods to 
    - test whether the task has completed or been cancelled 
    - retrieve its result 
    - cancel the task
- ``` boolean cancel(boolean mayInterruptIfRunning) ```
    - Attempts to cancel execution of this task
    - This method has no effect if the task is already completed or cancelled, 
      or could not be cancelled for some other reason 
    - Otherwise, if this task has not started when cancel is called, this task 
      should never run 
    - If the task has already started, then the ```mayInterruptIfRunning ``` 
      parameter determines whether the thread executing this task (when known by 
      the implementation) is interrupted in an attempt to stop the task 
    - The return value from this method does not necessarily indicate whether 
      the task is now cancelled; use ``` isCancelled() ``` instead
- ``` boolean isCancelled() ```
    - Returns ``` true ``` if this task was cancelled before it completed normally
- ``` boolean isDone() ```
    - Returns ``` true ``` if this task completed 
    - Completion may be due to normal termination, an exception, or cancellation 
        - in all of these cases, this method will return ``` true ```
- ``` V get() throws InterruptedException, ExecutionException ```
    - Waits if necessary for the computation to complete, and then retrieves its 
      result
    - It returns immediately or throws an ``` Exception ``` if the task has  
      already completed
        - but if not it blocks until the task completes
    - If the task completes by throwing an exception, ``` get() ``` rethrows it 
      wrapped in an ``` ExecutionException ```
    - If it was cancelled, ``` get() ``` throws ``` CancellationException ```
    - If ``` get() ``` throws ``` ExecutionException ```, the underlying 
      exception can be retrieved with ``` getCause ```
- ``` 
  V get(long timeout, TimeUnit unit) throws InterruptedException, 
          ExecutionException, TimeoutException 
  ```
    - Waits if necessary for at most the given time for the computation to 
      complete, and then retrieves its result, if available 
- Ways to create a Future to describe a task
    - The ``` submit ``` methods in ``` ExecutorService ``` all return a 
      ``` Future ``` 
    - Submit a ``` Runnable ``` or a ``` Callable ``` to an executor and get 
      back a ``` Future ``` that can be used to retrieve the result or cancel 
      the task 
    - Explicitly instantiate a ``` FutureTask ``` for a given ``` Runnable ``` 
      or ``` Callable ```` 
        - ``` FutureTask ``` implements ``` Runnable ```, it can be submitted 
          to an ``` Executor ``` for execution or executed directly by calling 
          its run method

> ___homogeneous___ adjective /ˌhəʊməˈdʒiːniəs/ also homogenous /həˈmɑːdʒənəs/) 
> consisting of things or people that are all the same or all of the same type

> ___heterogeneous___ adjective /ˌhetərəˈdʒiːniəs/ consisting of many different 
> kinds of people or things

### The Real Performance Payoff 
- of Dividing a Program’s Workload into Tasks

#### Comes when there are a large number of tasks which are
- independent 
- homogeneous tasks 
- and can be processed concurrently


### ___Limitations of Parallelizing Heterogeneous Tasks___

#### Assigning a different type of task to each worker does not scale well 
- If several more people show up, it is not obvious how they can help 
  without getting in the way (礙手礙腳) or significantly restructuring the 
  division of labor
- Without finding finer-grained parallelism among similar tasks, this approach 
  will yield diminishing returns

> diminish verb /dɪˈmɪnɪʃ/ to become smaller, weaker, etc.; to make something 
> become smaller, weaker, etc.

#### A further problem with dividing ___heterogeneous tasks___ among multiple workers
- the tasks may have disparate sizes
- If dividing tasks A and B between two workers 
    - but A takes ten times as long as B 
    - Only speeded up the total process by 9%

#### Some amount of coordination overhead is always required 
- when dividing a task among multiple workers 
- for the division to be worthwhile, this overhead must be more than 
  compensated by productivity improvements due to parallelism 


### [CompletableFuture](README_CompletableFuture.md)


## Cancellation and Shutdown


### The Goal: Getting Tasks and Threads to Stop 
- #### Safely 
- #### Quickly 
- #### Reliably


### Java DOES NOT Provide Any Stopping Mechanism 
- for safely forcing a thread to stop what it is doing


### Java Provides ___Interruption___, a Cooperative Mechanism 
- that ___lets one thread ask another to stop___ what it is doing


### The Cooperative Approach is Required because 
- we rarely want a task, thread, or service to stop immediately 
- since that could leave shared data structures in an inconsistent state
- tasks and services can be coded so that, when requested, they clean up any 
  work currently in progress and then terminate
- This provides greater flexibility 
    - since the task code itself is usually better able to assess the cleanup 
      required than is the code requesting cancellation


### Task Cancellation Overview

#### An activity is cancellable if external code can move it to completion 
- before its normal completion

#### Reasons why to Cancel an Activity
- User-Requested Cancellation
- Time Limited Activities
- Application Events
    - When one task finds a solution, all other tasks still searching are cancelled
- Errors
- Shutdown

#### There is NO SAFE WAY to Preemptively Stop a Thread In Java
- and therefore no safe way to preemptively stop a task

#### Only ___Cooperative Mechanisms___ 
- by which the task and the code requesting cancellation follow an agreed-upon 
  protocol
- setting a "Cancellation Requested" Flag that the task checks periodically
- if it finds the flag set, the task terminates early

#### How ___Cooperative Mechanisms___ Works?
- One such ___Cooperative Mechanisms___ is setting a cancellation-requested-flag 
  that the task checks periodically
- If it finds the flag set, the task terminates early 

#### A <span style="color:#FF7700">Cancellation Policy</span>  is MUST-HAVE 
- If a task that wants to be cancellable 
- It specifies the HOW, WHEN, and WHAT of Cancellation
    - <span style="color:#FF7700">HOW other code can request cancellation</span>
    - <span style="color:#FF7700">WHEN the task checks whether cancellation has been requested</span>
    - <span style="color:#FF7700">WHAT actions the task takes in response to a cancellation request</span>


### Interruption

#### Thread Interruption is a ___Cooperative Mechanisms___ for 
- A thread A to signal another thread B that it should, at its convenience and 
  if it feels like it, stop what it is doing and do something else

#### <span style="color:#FF7700">There is Nothing in the API or Language Specification that</span> 
- <span style="color:#FF7700">ties interruption to any specific cancellation semantics</span>
- <span style="color:#FF7700">but in practice, using Interruption for Anything but Cancellation is Fragile 
  and Difficult to sustain in larger applications</span> 

#### Each Thread has a boolean interrupted Status
- Interrupting a thread sets its ``` interrupted ``` status to ``` true ```

#### Thread contains methods for interrupting a thread 

#### Thread contains methods for querying the interrupted status of a thread

```  
public class Thread {

    public void interrupt() { ... }
    public boolean isInterrupted() { ... }
    
    public static boolean interrupted() { ... } // Clear
    ...
}
```

#### The ``` interrupt ``` method interrupts the target thread

#### The ``` isInterrupted ``` returns the interrupted status of the target thread

#### The poorly named static ``` interrupted ``` method
- clears the interrupted status of the current thread 
- and returns its previous value
- this is ___the only way to clear the interrupted status___

#### Blocking library methods like ``` Thread.sleep ``` and ``` Object.wait ``` 
- They try to detect when a thread has been interrupted and return early 
- They respond to interruption by 
    - <span style="color:#FF7700">clearing the interrupted status</span>
    - <span style="color:#FF7700">and also throwing ``` InterruptedException ```</span>
- Indicating that the blocking operation completed early due to interruption
- The JVM makes no guarantees on how quickly a blocking method will detect 
  interruption, but in practice this happens reasonably quickly

#### If a thread is interrupted when it is NOT Blocked
- its interrupted status is set (In this way interruption is "Sticky")
- It is UP TO the activity being cancelled to poll the interrupted status to 
  detect interruption
- If it doesn’t trigger an InterruptedException, evidence of interruption 
  persists until someone deliberately clears the interrupted status

> Calling interrupt does not necessarily stop the target thread from doing what 
> it is doing; 

> Calling interrupt merely delivers the message that interruption has been 
> requested

#### A good way to think about Interruption is that 
- it does not actually interrupt a running thread
- <span style="color:#FF7700">it just requests that the thread interrupt itself at the next convenient 
  opportunity</span>
    - These opportunities are called <span style="color:#FF7700">Cancellation Points</span>

#### Methods ``` wait ```, ``` sleep ```, and ``` join ```, take Interruption Requests Seriously
- They throw an exception when 
    - they receive an interrupt request
    - they encounter an already set interrupt status upon entry (在進入時) 

#### Well Behaved Methods 
> Leave the interruption request in place so that the calling code can do 
> something with it 

#### Poorly Behaved Methods swallow the interrupt request
- thus denying code, further up the call stack, the opportunity to act on it

#### <span style="color:#FF7700">CAUTION</span> The static ``` interrupted ``` method should be used with caution
- because it clears the current thread’s interrupted status

> <span style="color:#FF7700">Interruption is usually the most sensible way to implement Cancellation</span>

#### <span style="color:#FF7700">Task Cancellation Template</span>

``` 
class PrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;
    
    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }
    
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            
            // Cancellation Point 1
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException consumed) {
            /* Allow thread to exit */
            // Cancellation Point 2
            Thread.currentThread().interrupt();
        }
    }
    public void cancel() { interrupt(); }
}

```


### Interruption Policies

#### An Interruption Policy Determines 
- <span style="color:#FF7700">How a thread interprets an interruption request</span> 
- <span style="color:#FF7700">What it does (if anything) when Interruption is detected</span> 
- <span style="color:#FF7700">What units of work are considered atomic with respect to interruption</span>
- <span style="color:#FF7700">How quickly it reacts to interruption</span>

#### The Most Sensible (also Standard) Interruption Policy is 
- THREAD-LEVEL or SERVICE-LEVEL Cancellation 
    - <span style="color:#FF7700">Exit as quickly as practical</span>
    - <span style="color:#FF7700">Cleaning up if necessary </span>
    - <span style="color:#FF7700">Notifying some owning entity that the thread is exiting</span>

#### Other interruption policies, such as pausing or resuming a service
- Threads or Thread Pools with nonstandard interruption policies may need to be 
  restricted to tasks that have been written with an awareness of the policy 

#### It is Important to Distinguish between 
- how Tasks and Threads should react to interruption

#### A single Interrupt Request may have more than one Desired Recipient
- Interrupting a worker thread in a thread pool can mean both 
    - cancel the current task 
    - and shut down the worker thread

#### Tasks borrow threads from thread pool to execute

#### For a thread pool, any Code Outside of the Thread Pool Implementation 
- Doesn’t Own the Thread

#### Code, which doesn’t own the thread, should Preserve the Interrupted Status 
- so that the owning code can eventually act on it 
- even if the “guest” code acts on the interruption as well 
- This is why most blocking library methods simply throw ``` InterruptedException ```
  in response to an interrupt

#### Most blocking library methods only and simply throw ``` InterruptedException ```
- in response to an interrupt
- They will never process the interrupted status change in a thread they own
- so they implement the most reasonable cancellation policy for task or library 
  code
    - Get out of the way as quickly as possible
    - Communicate the interruption back to the caller 
        - so that code higher up on the call stack can take further action

#### A Task Needn’t Necessarily Drop Everything When it Detects an Interruption
- It can choose to postpone it until a more opportune time by remembering that 
  it was interrupted
- finishing the task it was performing
- and then throwing ``` InterruptedException ```
- or otherwise indicating interruption 

> opportune adj. /ˌɑːpərˈtuːn/ (of a time) Suitable for doing a particular thing, 
> so that it is likely to be successful

#### A task should not assume anything about the interruption policy 
- of its executing thread 
- unless it is explicitly designed to run within a service that has a specific 
  interruption policy

#### <span style="color:#FF7700">A Task Should Preserve the Executing Thread’s Interruption Status</span>
- If it is not simply going to propagate ``` InterruptedException ``` to its caller
    - it should restore the interruption status after catching ``` InterruptedException ```

> ``` Thread.currentThread().interrupt(); ```

> Task code should not make assumptions about what interruption means to its 
> executing thread 
 
> Cancellation code should not make assumptions about the interruption policy of 
> arbitrary threads

> ___A thread should be interrupted only by its owner___

> The owner of an interrupted thread can encapsulate knowledge of the thread's 
> interruption policy in an appropriate cancellation mechanism such as a 
> ``` ExecutorService::shutdown ```  method

> Because each thread has its own interruption policy, <span style="color:#FF7700">___you should not interrupt 
> a thread unless you know what interruption means to that thread___</span>


### Responding to Interruption

#### Two practical Strategies for Handling ``` InterruptedException ```
- Propagate the exception (possibly after some task-specific cleanup), 
  making the method an ``` interruptible ``` blocking method, too
    - Propagating ``` InterruptedException ```: adding ``` InterruptedException ``` 
      to the ``` throws ``` clause
    ```
    BlockingQueue<Task> queue;
    ...
    public Task getNextTask() throws InterruptedException {
        return queue.take();
    }
    ```
- Restore the interruption status so that code higher up on the call stack can 
  deal with it

#### <span style="color:#FF7700"> The Standard way to Handle Situations that NOT Able to Propagate ``` InterruptedException ```</span>
- Tasks defined by ``` Runnable::run ```, for example
- Restore the interrupted status by calling ``` interrupt ``` again
- <span style="color:#FF7700">What Should NOT DO</span>  is 
    - Swallow the ``` InterruptedException ``` by catching it and doing nothing 
      in the catch block,
        - unless the code is actually implementing the interruption policy for 
          a thread

> Only code that implements a thread’s interruption policy may swallow an 
> interruption request 

> General-purpose task and library code should never swallow interruption 
> requests 

#### <span style="color:#FF7700"> Tasks that do Not Support Cancellation but Still Call Interruptible Blocking Methods </span>
- Have to call them in a loop 
- Retrying when interruption is detected
- Should save the interruption status locally 
- Restore the interruption status just before returning, rather than immediately 
  upon catching ```InterruptedException```
  (在返回之前恢復中斷狀態，而不是在捕獲 InterruptedException 時立即恢復)
``` 
// Do Not Support Cancellation
public Task getNextTask(BlockingQueue<Task> queue) {
    
    // 
    boolean interrupted = false;
    
    try {
        while (true) {
            try {
                if (someReason()) {
                    break;
                }
                return queue.take();
            } catch (InterruptedException e) {
                interrupted = true;
                // fall through and retry
            }
        }
    } finally {
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
```

#### Setting the Interrupted Status Too Early could result in an Infinite Loop
- Most interruptible blocking methods check the interrupted status on entry
- Throw ``` InterruptedException ``` immediately if it is set

> Interruptible Methods Usually Poll for Interruption BEFORE Blocking or DOING 
> any significant work, so as to be as responsive to interruption as possible 

#### <span style="color:#FF7700">Tasks DO NOT Call Interruptible Blocking Methods</span>
- Polling the current thread'’'s interrupted status throughout the task code 
- Choosing a polling frequency is a tradeoff between efficiency and responsiveness
    - For high responsiveness requirements, you cannot call potentially long-running 
      methods that are not themselves responsive to interruption


### Timed Run

#### The Domain
- spend up to ten minutes looking for the answer
- enumerate all the answers you can in ten minutes
- What to do if the task throws an unchecked exception
- What to do if task finished earily than the scheduled timeout

> DO NOT Violates the Rule - Never interrupt the thread unless you know it's interruption policy

> In Task Code: DO NOT INTERRUPT THE CURRENT THREAD


### Cancellation via Future

#### ``` ExecutorService.submit ``` returns a ``` Future ``` describing the task 

#### ``` Future ``` has method ``` boolean cancel(boolean mayInterruptIfRunning) ``` 
- ``` mayInterruptIfRunning ```, and returns a value indicating whether the 
  cancellation attempt was successful 
- ``` mayInterruptIfRunning ``` only tells whether it was able to deliver the interruption, 
  not whether the task detected and acted on it
- When ``` mayInterruptIfRunning ``` is true and the task is currently running 
  in some thread, then that thread is interrupted
- Setting ``` mayInterruptIfRunning ``` to ``` false ``` means "do not run this 
  task if it hasn't started yet" 
    - and should be used for tasks that are not designed to handle interruption 

#### Is it OK to call ``` cancel(true) ``` method of ``` Future ``` ???
- The task execution threads created by the standard Executor implementations 
  implement an interruption policy that lets tasks be cancelled using interruption 
- So <span style="color:#FF7700">it is safe to set ``` mayInterruptIfRunning ```
  to be ``` true ``` when cancelling tasks through their ``` Futures ``` when
  they are running in a standard ``` Executor ```</span>

> Always Call ``` Future.cancel(true) ``` to Cancel a Task running with an ``` Executor ```

> Never ever Interrupt a pool thread directly when attempting to cancel a task, 
> because you won’t know what task is running when the interrupt request is 
> delivered


### Timed Run with Cancellation via Future
``` 
// Cancelling task r, whose result is no longer needed
public static void timedRun(Runnable r, long timeout, TimeUnit unit) 
        throws InterruptedException {
        
    Future<?> task = taskExec.submit(r);
    try {
        task.get(timeout, unit);
    } catch (TimeoutException e) {
        // task will be cancelled below
    } catch (ExecutionException e) {
        // exception thrown in task; rethrow
        throw launderThrowable(e.getCause());
    } finally {
        // Harmless if task already completed
        task.cancel(true); // interrupt if running
    }
}
```

### <span style="color:#FF7700">Dealing with NON_INTERRUPTIBLE Blocking</span>

#### Many Blocking Library Methods 
- respond to interruption by returning early and throwing ``` InterruptedException ``` 
    - which makes it easier to build tasks that are responsive to cancellation 

#### <span style="color:#FF7700">Blocking Methods or Blocking Mechanisms NOT Responsive to Interruption</span> 
- Synchronous socket I/O in ``` java.io ```
    - The read and write methods in ``` InputStream ``` and ``` OutputStream ``` 
      in ``` java.io ``` are not responsive to interruption
- Synchronous I/O in ``` java.nio ```
    - ``` InterruptibleChannel ``` and most standard Channels implement 
      ``` InterruptibleChannel ```
- Asynchronous I/O with Selector
- Lock Acquisition
    - If a thread is blocked waiting for an Intrinsic Lock, there is NO WAY to
      stop it
    - the explicit ``` Lock ``` classes offer the ``` lockInterruptibly ``` 
      method which allows to wait for a lock and still be responsive to interrupts

#### The Solution for Dealing with NON_INTERRUPTIBLE Blocking
``` 
public class ReaderThread extends Thread {
    private final Socket socket;
    private final InputStream in;
    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored) { 
            // ... ...
        } finally {
             // Response IOException instead of InterruptedException
            super.interrupt();
        }
    }
    public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                int count = in.read(buf);
                if (count < 0) { 
                    break;
                } else if (count > 0) {
                    processBuffer(buf, count);
                }
           }
        } catch (IOException e) { /* Allow thread to exit */ }
    }
}
```

#### Encapsulating Nonstandard Cancellation with ``` newTaskFor ``` by Using
- the ``` newTaskFor ``` hook added to ``` ThreadPoolExecutor ```
- The ``` newTaskFor ``` hook is a factory method that creates the ``` Future ``` 
  representing the task
    - it returns a ``` RunnableFuture ```
- Customizing the task ``` Future ``` in order to override ``` Future.cancel ```
- Customizing ``` Future.cancel ``` can 
    - perform logging 
    - gather statistics on cancellation
    - can also cancel activities that are not responsive to interruption 

``` 
public interface CancellableTask<T> extends Callable<T> {
    void cancel();
    RunnableFuture<T> newTask();
}

@ThreadSafe
public class CancellingExecutor extends ThreadPoolExecutor {
    ...
    protected<T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask) {
            return ((CancellableTask<T>) callable).newTask();
        } else {
            return super.newTaskFor(callable);
        }
    }
}

public abstract class SocketUsingTask<T> implements CancellableTask<T> {
    @GuardedBy("this") 
    private Socket socket;
    protected synchronized void setSocket(Socket s) { socket = s; }
    public synchronized void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) { }
    }
    // Let a CancellableTask create its own Future
    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this) {
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}
```


### Stopping a THREAD_BASED Service

#### Thread Ownership
- The owner of a thread is the class that created the thread 
- A thread pool owns is the owner of it's worker threads 
    - if those threads need to be interrupted, the thread pool should take care 
      of it
    - the thread pool service provides lifecycle methods for shutting itself down 
      that also shut down the owned threads
- Thread ownership is not transitive
    - The application owns the thread pool service
    - the thread pool service owns the worker threads
    - the application doesn’t own the worker threads
    - therefore, the application should not attempt to stop them directly
    - however, the application can shut down the thread pool service
    - the thread pool service can shut down the threads 

> Provide lifecycle methods whenever a thread-owning service has a lifetime 
> longer than that of the method that created it 


### ``` ExecutorService ``` Shutdown


### Poison Pills
#### Poison pills work only when the number of producers and consumers is known
#### Poison pills work reliably only with unbounded queues


### Limitations of ``` shutdownNow ```
#### When an ``` ExecutorService ``` is shut down abruptly with ``` shutdownNow ```
- it attempts to cancel the tasks currently in progress 
- and returns a list of tasks that were submitted but never started 
    - so that they can be logged or saved for later processing 
- NO general way to find out which tasks started but did not complete
- NO way of knowing the state of the tasks in progress at shutdown time 
    - unless the tasks themselves perform some sort of checkpointing


### <span style="color:#FF7700">Identify Tasks Started but NOT Complete normally at Shutdown Time</span>
#### The tasks must preserve the thread's interrupted status when they return
#### To know Which Tasks have NOT Completed at Shutdown Time
- Should know which tasks were in progress when the executor was shut down
``` 
public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService exec;
    private final Set<Runnable> tasksCancelledAtShutdown =
            Collections.synchronizedSet(new HashSet<Runnable>());
    public List<Runnable> getCancelledTasks() {
        if (!exec.isTerminated())
            throw new IllegalStateException(...);
        return new ArrayList<Runnable>(tasksCancelledAtShutdown);
    }
    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted())
                        tasksCancelledAtShutdown.add(runnable);
                }
            }
        });
    }
    // delegate other ExecutorService methods to exec
}
```
#### Save task state when they are cancelled
``` 
public abstract class WebCrawler {
    private volatile TrackingExecutor exec;
    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<URL>();
    ...
    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) submitCrawlTask(url);
        urlsToCrawl.clear();
    }
    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT))
                    saveUncrawled(exec.getCancelledTasks());
        } finally {
            exec = null;
        }
    }
    protected abstract List<URL> processPage(URL url);
    private void saveUncrawled(List<Runnable> uncrawled) {
        for (Runnable task : uncrawled)
            urlsToCrawl.add(((CrawlTask) task).getPage());
    }
    private void submitCrawlTask(URL u) {
        exec.execute(new CrawlTask(u));
    }
    private class CrawlTask implements Runnable {
        private final URL url;
        ...
        public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) return;
                submitCrawlTask(link);
            }
        }
        public URL getPage() { return url; }
    }
}
```


### <span style="color:#FF7700">Handling Abnormal Thread Termination</span>

#### Any Code can Throw a ``` RuntimeException ```

#### Task-Processing Threads in a Thread Pool
- The Thread Pool should call tasks within a try-catch block that catches 
  unchecked exceptions 
- or within a try-finally block to ensure that if the thread exits abnormally 
  the framework is informed of this and can take corrective action

> Consider catching ``` RuntimeException ``` when calling unknown, untrusted 
> code through an abstraction such as Runnable

#### Approach 1: Catch ``` Throwable ``` in Task
``` 
public void run() {
    Throwable thrown = null;
    try {
        while (!isInterrupted()) {
            runTask(getTaskFromWorkQueue());
        }
    } catch (Throwable e) {
        thrown = e;
    } finally {
        threadExited(this, thrown);
    }
}
```

> When writing a worker thread class that executes submitted tasks, or when 
> calling untrusted external code (such as dynamically loaded plugins)
> use the code above  to prevent a poorly written task or plugin from taking 
> down the thread that happens to call it 

#### Approach 2: Use ``` UncaughtExceptionHandler ```
``` 
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE, 
                "Thread terminated with exception: " + t.getName(), e);
    }
}
```
- The ``` Thread ``` API provides the ``` UncaughtExceptionHandler ``` facility 
  detecting when a thread dies due to an uncaught exception
- When a thread exits due to an uncaught exception, the JVM reports this event
  to an application-provided ``` UncaughtExceptionHandler ``` 
    - if no handler exists, the default behavior is to print the stack trace to 
      ``` System.err ```

> In LONG_RUNNING Applications, ALWAYS Use ``` UncaughtExceptionHandler ``` for 
> all threads that at least log the exception 

#### To Set an ``` UncaughtExceptionHandler ``` for Pool Threads
- Provide a ``` ThreadFactory ``` to the ``` ThreadPoolExecutor ``` Constructor
- As with all thread manipulation, only the thread's owner should change its 
  ``` UncaughtExceptionHandler ```

#### To be Notified when a Task Fails due to an Exception
- Wrap the task with a Runnable or Callable that catches the exception 
- override the ``` afterExecute ``` hook in ``` ThreadPoolExecutor ```

#### Tasks ONLY Submitted with ``` Executor.execute(Runnable command) ```
- can be handled by ``` UncaughtExceptionHandler ```

Exceptions thrown from tasks make it to the ``` UncaughtExceptionHandler ``` 
- only for tasks submitted with ``` Executor.execute(Runnable command) ```

#### Tasks Submitted ``` submit(Runnable task) ``` exception will be rethrown 
- by ``` Future.get ```, wrapped in an ``` ExecutionException ```


### <span style="color:#FF7700">JVM Shutdown</span>

#### Shutdown Hooks
- In an orderly shutdown, the JVM first starts all registered Shutdown Hooks
    - Shutdown Hooks are unstarted threads that are registered with 
      ``` Runtime.addShutdownHook ```
- The JVM makes no guarantees on the order in which shutdown hooks are started
- If any application threads (daemon or nondaemon) are still running at shutdown 
  time, they continue to run concurrently with the shutdown process 
- When all shutdown hooks have completed, the JVM may choose to run finalizers 
  if ``` runFinalizersOnExit ``` is true, and then halts
- The JVM makes no attempt to stop or interrupt any application threads that are 
  still running at shutdown time
- If the Shutdown Hooks or finalizers don’t complete
    - then the orderly shutdown process “hangs” 
    - and the JVM must be shut down abruptly
        - In an abrupt shutdown, the JVM is not required to do anything other 
          than halt the JVM, and Shutdown Hooks will not run

#### <span style="color:#FF7700">Shutdown Hooks all Run Concurrently</span>

#### <span style="color:#FF7700">Shutdown Hooks should be THREAD_SAFE</span>
- they must use synchronization when accessing shared data 
    - and should be careful to avoid deadlock, just like any other concurrent 
      code

#### Shutdown Hooks should not make assumptions about the state of the application
- other services have shut down already?
- all normal threads have completed?
- why the JVM is shutting down?

#### Shutdown Hooks should Exit as Quickly as possible

#### Shutdown Hooks can be used for Service or Application Cleanup

#### Shutdown Hooks should NOT rely on services 
- that can be shut down by the application or other shutdown hooks

#### <span style="color:#FF7700">Use A Single Shutdown Hook for All Services</span>
- rather than one for each service
- and have it call a series of shutdown actions 
- ensures that shutdown actions execute sequentially in a single thread
- avoiding the possibility of race conditions or deadlock between shutdown actions

> Executing Shutdown Actions Sequentially rather than concurrently eliminates 
> many potential sources of failure

#### For Example: 
``` 
public void start() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
            try { 
                LogService.this.stop(); 
            } catch (InterruptedException ignored) {
                // ... ...
            }
        }
    });
}
```


### Daemon Threads

#### Threads are divided into two types 
- normal threads 
- and Daemon Threads

#### When the JVM starts up, all the threads it creates are Daemon Threads
- such as garbage collector and other housekeeping threads except the main thread

#### A new created thread inherits the daemon status of the thread that created it
- any threads created by the main thread are also normal threads

#### Normal threads and Daemon Threads differ only in what happens when they exit

#### When the JVM halts, any remaining daemon threads are abandoned 
- ``` finally ``` blocks are not executed 
- stacks are not unwound
- the JVM just exits 

#### Daemon threads should be used sparingly 
> Few processing activities can be safely abandoned at any time with no cleanup

> It's dangerous to use daemon threads for tasks that might perform any sort of I/O

#### Daemon threads are best saved for “housekeeping” tasks
- such as a background thread that periodically removes expired entries from an 
  in-memory cache


### <span style="color:#FF7700">Avoid Finalizers</span>

#### The garbage collector reclaimes memory resources when they are no longer needed 
- but some resources must be explicitly returned to the operating system when 
  no longer needed, such as file or socket handles

#### The combination of finally blocks and explicit close methods does a better job
- of resource management than finalizers 
- the sole exception is when you need to manage objects that hold resources 
  acquired by native methods
    - for these reasons and others, work hard to avoid writing or using classes 
      with finalizers



[Applying Thread Pools](README_Applying_Thread_Pools.md)