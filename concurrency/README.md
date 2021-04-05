# Java Concurrency in Practice

### Resources
  - [Home](https://jcip.net/)
  - [Source Code](https://jcip.net/listings.html)
  - [Errata](https://jcip.net/errata.html)


# Part I Fundamentals


## The Cheat Sheet of the Concurrency Fundamentals

1.  The less mutable state, the easier it is to ensure thread safety

2.  Make fields final unless they need to be mutable

3.  Immutable objects are automatically thread-safe
    - Immutable objects simplify concurrent programming tremendously
    - Immutable objects are simpler and safer, and can be shared freely
      without locking or defensive copying

4.  Encapsulation makes it practical to manage the complexity
    - Encapsulating data within objects makes it easier to preserve their
      invariants
    - Encapsulating synchronization within objects makes it easier to comply
      with their synchronization policy

5.  Guard each mutable variable with a lock

6.  Guard all variables in an invariant with the same lock

7.  Hold locks for the duration of compound actions

8.  A program that accesses a mutable variable from multiple threads without 
    synchronization is a broken program

9.  Don’t rely on clever reasoning about why you don’t need to synchronize

10. Include thread safety in the design process—or explicitly document that 
    your class is not thread-safe

11. Document your synchronization policy


## Safe Publication Idioms

1. To publish an object safely, both the reference to the object and the 
   object’s state must be made visible to other threads at the same time
   
   A properly constructed object can be safely published by:

    - Initializing an object reference from a static initializer
      - ``` public static Holder holder = new Holder(42); ```
    
    - Storing a reference to it into a volatile field or AtomicReference
      
    - Storing a reference to it into a final field of a properly constructed object
      
    - Storing a reference to it into a field that is properly guarded by a lock
        - the thread-safe library collections offer safe publication guarantees
        - Other handoff mechanisms in the class library (such as ``` Future ``` and 
          ``` Exchanger ```) also constitute safe publication


2. To share mutable objects safely, they must be safely published and be either 
   thread-safe or guarded by a lock 

   The publication requirements for an object depend on its mutability:
     - Immutable objects can be published through any mechanism
     - Effectively immutable objects must be safely published
     - Mutable objects must be safely published, and must be either threadsafe 
       or guarded by a lock


## Sharing Objects Safely

  - When you publish an object, you should document how the object can be 
    accessed, ask yourself:
      - What you are allowed to do with it 
      - Do you need to acquire a lock before using it
      - Are you allowed to modify its state, or only to read it


  - The most useful policies for using and sharing objects in a concurrent 
    program are:
    - __Thread-Confined__ : 
      A thread-confined object is owned exclusively by and confined to one 
      thread, and can be modified by its owning thread
    - __Shared Read-Only__ : 
      A shared read-only object can be accessed concurrently by multiple threads 
      without additional synchronization, but cannot be modified by any thread
      - Shared read-only objects include immutable and effectively immutable 
        objects
    - __Shared Thread-Safe__ : 
      A thread-safe object performs synchronization internally, so multiple 
      threads can freely access it through its public interface without further 
      synchronization
    - __Guarded__ A guarded object can be accessed only with a specific lock 
      held  
      - Guarded objects include those that are encapsulated within other 
        thread-safe objects and published objects that are known to be guarded 
        by a specific lock


## Invariant

> An invariant is any logical rule that must be obeyed throughout the execution 
> of your program that can be communicated to a human, but not to your compiler 
> INVARIANTS ARE BAD

This definition can be cleaved out conditions into two groups 
  - those the compiler can be trusted with enforcing
  - and those that must be documented, discussed, commented, or otherwise 
    communicated to contributors in order for them to interact with the codebase 
    without introducing bugs 


## Patterns for Structuring Thread-Safe Classes

### The Design of Thread-Safe Classes

1. The design process for a thread-safe class should include these three basic elements
   - Identify the variables that form the object’s state 
   - Identify the invariants that constrain the state variables 
   - Establish a policy for managing concurrent access to the object’s state 


2. Gathering synchronization requirements
   > You cannot ensure thread safety without understanding an object’s invariants 
   > and postconditions. Constraints on the valid values or state transitions for 
   > state variables can create atomicity and encapsulation requirements.


3. State-Dependent Operations

   - To create operations that wait for a precondition to become true before 
     proceeding, it is often easier to use existing library classes, such as 
     blocking queues or semaphores, to provide the desired state-dependent 
     behavior


4. State ownership

   - When defining which variables form an object’s state, we want to consider 
     only the data that object owns. Ownership is not embodied explicitly in 
     the language, but is instead an element of class design


### Instance Confinement

1. Encapsulating data within an object confines access to the data to the 
   object’s methods, making it easier to ensure that the data is always accessed 
   with the appropriate lock held
   - If an object is intended to be confined to a specific scope, then letting 
     it escape from that scope is a bug
   - Confined objects can also escape by publishing other objects such as 
     iterators or inner class instances that may indirectly publish the 
     confined objects


2. Confinement makes it easier to build thread-safe classes because a class that 
   confines its state can be analyzed for thread safety without having to 
   examine the whole program


#### The Java Monitor Pattern

  > An object following the Java monitor pattern encapsulates all its mutable 
  > state and guards it with the object’s own intrinsic lock


### Delegating thread safety

  > If a class is composed of multiple independent thread-safe state variables 
  > and has no operations that have any invalid state transitions, then it can 
  > delegate thread safety to the underlying state variables
 
  > If a state variable is 
  > thread-safe, 
  > does not participate in any invariants that constrain its value, 
  > and has no prohibited state transitions for any of its operations, 
  > then it can safely be published


### Adding Functionality to Existing Thread-Safe Classes


### Building Blocks


# Part II Structuring Concurrent Applications


## Chapter 6 Task Execution


### 6.1 Executing Tasks in Threads

### 6.2 Separating the specification of Execution Policy from Task Submission

#### Execution Policy (What, Where, When, How)

- In what thread will tasks be executed?

- In what order should tasks be executed (FIFO, LIFO, priority order)?

- How many tasks may execute concurrently?

- How many tasks may be queued pending execution?

- If a task has to be rejected because the system is overloaded, which task 
  should be selected as the victim, and how should the application be notified?

- What actions should be taken before or after executing a task?


### 6.3 Finding Exploitable Parallelism

1. A further problem with dividing heterogeneous tasks among multiple workers 
   is that the tasks may have disparate sizes

2. If you divide tasks A and B between two workers but A takes ten times as 
   long as B, you’ve only speeded up the total process by 9%

3. Finally, dividing a task among multiple workers always involves some amount 
   of coordination overhead; for the division to be worthwhile, this overhead 
   must be more than compensated by productivity improvements due to parallelism


## Chapter 7 Cancellation and Shutdown

### Overview

#### This chapter addresses mechanisms for 
  - Cancellation
  - Interruption
  - How to code tasks and services to be responsive to cancellation requests


### 7.1 Task Cancellation


1.  What does _cancellable_ meat?

    - An activity is _cancellable_ if external code can move it to completion 
      before its normal completion

      - User-requested cancellation
      - Time-limited activities
      - Application events
      - Errors
      - Shutdown


2. The ___Cooperative Mechanisms___

    - There is no safe way to preemptively stop a thread in Java, and therefore 
      no safe way to preemptively stop a task
      
    - There are only ___cooperative mechanisms___, by which the task and the code 
      requesting cancellation follow an agreed-upon protocol

    - How it works?

      - One such ___cooperative mechanism___ is setting a 
        “cancellation requested” flag that the task checks periodically
        - If it finds the flag set, the task terminates early 


3.  The ___Cancellation Policy___
    - A task that wants to be cancellable must have a ___cancellation policy___ 
      that specifies the “how”, “when”, and “what” of ___cancellation___
      - ___How other code can request cancellation___
      - ___When the task checks whether cancellation has been requested___
      - ___What actions the task takes in response to a cancellation request___


4.  ___Interruption___ is usually the most sensible way to implement 
    ___cancellation___

    - There is nothing in the API or language specification that ties
      ___interruption___ to any specific cancellation semantics, but in practice, 
      using ___interruption___ for anything but ___cancellation___ is fragile and difficult 
      to sustain in larger applications
      
    - Calling interrupt does not necessarily stop the target thread from doing 
      what it is doing; it merely delivers the message that ___interruption___ has 
      been requested
      
    - Threads should also have an ___interruption policy___
      - An interruption policy determines how a thread interprets an
        ___interruption___ request
        - What it does (if anything) 
        - When one is detected, what units of work are considered atomic with 
          respect to ___interruption___, and 
        - How quickly it reacts to ___interruption___
      - The most sensible ___interruption policy___ is some form of thread-level 
        or servicelevel cancellation: 
        - Exit as quickly as practical 
        - Cleaning up if necessary, and 
        - Possibly notifying some owning entity that the thread is exiting
        
    - Distinguish between how tasks and threads should react to ___interruption___ 
      - Tasks do not execute in threads they own; they borrow threads owned by 
        a service such as a thread pool
      - Code that doesn’t own the thread (for a thread pool, any code outside 
        of the thread pool implementation) should be careful to preserve the 
        interrupted status so that the owning code can eventually act on it
      - A task needn’t necessarily drop everything when it detects an 
        interruption request 
        - It can choose to postpone it until a more opportune time by 
          remembering that it was interrupted, finishing the task it was 
          performing, and then throwing ``` InterruptedException ``` or 
          otherwise indicating interruption
          - This technique can protect data structures from corruption when an 
            activity is interrupted in the middle of an update
      - A task should not assume anything about the interruption policy of its 
        executing thread unless it is explicitly designed to run within a service 
        that has a specific interruption policy
      - Whether a task interprets interruption as cancellation or takes some 
        other action on interruption, it should take care to preserve the 
        executing thread’s interruption status
        - If it is not simply going to propagate ``` InterruptedException ``` 
          to its caller, it should restore the interruption status after 
          catching ``` InterruptedException ```
          - ``` Thread.currentThread().interrupt(); ```
      - A thread should be interrupted only by its owner 
        - The owner can encapsulate knowledge of the thread’s interruption 
          policy in an appropriate cancellation mechanism such as a shutdown 
          method
      - Because each thread has its own interruption policy, you should not 
        interrupt a thread unless you know what interruption means to that 
        thread

    - Responding to interruption
      - Two practical strategies for handling ``` InterruptedException ```
        - Propagate the exception (possibly after some task-specific cleanup), 
          making your method an interruptible blocking method, too;
        - Restore the interruption status so that code higher up on the call 
          stack can deal with it
      - Another way to preserve the interruption request when don’t want to or 
        cannot propagate ``` InterruptedException ```
        - Restore the interrupted status by calling interrupt again
          - ``` Thread.currentThread().interrupt(); ```
        - What should not do is swallow the InterruptedException by catching it 
          and doing nothing in the catch block
        - Only code that implements a thread’s interruption policy may swallow 
          an interruption request ]
          - General-purpose task and library code should never swallow 
            interruption requests
     - Activities that do not support cancellation but still call interruptible 
       blocking methods will have to call them in a loop, retrying when 
       interruption is detected 
       - Setting the interrupted status too early could result in an infinite 
         loop, because most interruptible blocking methods check the interrupted 
         status on entry and throw InterruptedException immediately if it is set
       - Interruptible methods usually poll for interruption before blocking or 
         doing any significant work, so as to be as responsive to interruption 
         as possible 


5.  Cancellation via ``` Future ```

    > The General Principle: It is better to use existing library classes than to roll your own

    - The ``` cancel ``` method of ``` Future ```
      - ``` boolean cancel(boolean mayInterruptIfRunning) ```
      - Attempts to cancel execution of this task
      - This attempt will fail if the task has already completed, has already 
        been cancelled, or could not be cancelled for some other reason
      - If successful, and this task has not started when cancel is called, this 
        task should never run
      - If the task has already started, then the mayInterruptIfRunning 
        parameter determines whether the thread executing this task should be 
        interrupted in an attempt to stop the task 
      - After this method returns 
        - Subsequent calls to ``` isDone() ``` will always return ``` true ```
        - Subsequent calls to ``` isCancelled() ``` will always return true 
          if this method returned true 
      - [Parameters] 
        - ``` mayInterruptIfRunning ``` - true if the thread executing this 
          task should be interrupted; otherwise, in-progress tasks are allowed 
          to complete
        - This only says whether it was able to deliver the interruption, not 
          whether the task detected and acted on it 
        - When ``` mayInterruptIfRunning ``` is true and the task is currently 
          running in some thread, then that thread is interrupted
        - Setting this argument to ``` false ``` means “don’t run this task if 
          it hasn’t started yet”, and should be used for tasks that are not 
          designed to handle interruption
      - [Returns] 
        - ``` false ``` if the task could not be cancelled, typically because 
          it has already completed normally; true otherwise

    - The task execution threads created by the standard Executor 
      implementations implement an interruption policy that lets tasks be 
      cancelled using interruption, so it is safe to set mayInterruptIfRunning 
      when cancelling tasks through their Futures when they are running in a 
      standard Executor
      - You Should Not Interrupt a pool ``` Thread ``` directly when attempting 
        to cancel a task, because you won’t know what task is running when the 
        interrupt request is delivered—do this only through the task’s ``` Future ```
      - This is yet another reason to code tasks to treat interruption as a 
        cancellation request: then they can be cancelled through their Futures

    > When ``` Future.get ``` throws ``` InterruptedException ``` or
    > ``` TimeoutException ``` and you know that the result is no longer needed
    > by the program, cancel the task with ``` Future.cancel ``` 


6.  Dealing with Non-Interruptible Blocking

    - Why non-interruptible threads are blocked

        - Synchronous socket I/O in ``` java.io ```
            - The common form of blocking I/O in server applications is reading 
              or writing to a socket
            - Unfortunately, the ``` read ``` and ``` write ``` methods in 
              ``` InputStream ``` and ``` OutputStream ``` are not responsive to 
              interruption
            - However, closing the underlying socket makes any threads blocked 
              in ``` read ``` or ``` write ``` throw a ``` SocketException ```
            - ``` Socket::close ```
              - Any thread currently blocked in an I/O operation upon this 
                socket will throw a SocketException 
              - Once a socket has been closed, it is not available for further 
                networking use (i.e. can't be reconnected or rebound); a new 
                socket needs to be created
              - Closing this socket will also close the socket's InputStream and 
                OutputStream
              - If this socket has an associated channel then the channel is 
                closed as well
        
        - Synchronous I/O in ``` java.nio ```
            - Interrupting a ``` thread ``` waiting on an InterruptibleChannel 
              causes it to throw ``` ClosedByInterruptException ``` and close 
              the channel 
                - And, also causes all other threads blocked on the channel to 
                  throw ``` ClosedByInterruptException ```) 
            - Closing an ``` InterruptibleChannel ``` causes threads blocked on 
              channel operations to throw ``` AsynchronousCloseException ```
            - Most standard Channels implement ``` InterruptibleChannel ``` like
              ```  FileChannel ```

        - Asynchronous I/O with Selector
            - If a thread is blocked in Selector.select (in 
              ``` java.nio.channels ```), calling ``` close ``` or ``` wakeup ``` 
              causes it to return prematurely

        - Lock acquisition
            - If a thread is blocked waiting for an intrinsic lock, there is 
              nothing you can do to stop it, ___short of___ ensuring that it 
              eventually acquires the lock and makes enough progress that you 
              can get its attention some other way
            - However, the explicit ``` Lock ``` classes offer the 
              lockInterruptibly method, which allows you to wait for a lock and 
              still be responsive to interrupts

> ___short of (doing) something___ - 
> without something; without doing something; unless something happens
> - Short of a miracle, we're certain to lose
> - Short of asking her to leave (= and we don't want to do that) there's not a 
>   lot we can do about the situation.

> ___Intrinsic Lock___ - 
> Every Java object can implicitly act as a lock for purposes of 
> synchronization; these built-in locks are called intrinsic locks 
> or monitor locks 
> The lock is automatically acquired by the executing thread before 
> entering a synchronized block and automatically released when 
> control exits the synchronized block, whether by the normal control 
> path or by throwing an exception out of the block  
> The only way to acquire an intrinsic lock is to enter a 
> synchronized block or method guarded by that lock 


7.  Encapsulating nonstandard cancellation with ``` ThreadPoolExecutor::newTaskFor ```

      - Create an interface which extends Callable
        ``` 
        public interface ICancellableTask<T> extends Callable<T> {
            void cancel();
            RunnableFuture<T> newTask();
        } 
        ```

      - Create a new class to inherits from ThreadPoolExecutor and override the 
        ``` newTaskFor ``` method
        ``` 
        public class CancellingExecutor extends ThreadPoolExecutor {
            // Overriden constructors 
            @Override
            protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
                if (callable instanceof ICancellableTask) {
                    return ((ICancellableTask<T>) callable).newTask();
                } else {
                    return super.newTaskFor(callable);
                }
            }
        }
        ```

      - Create a new task class which implements ICancellableTask
        ``` 
        @Slf4j
        public class CancellableSocketTask implements ICancellableTask<Void> {
        
            static final int BUFSZ = 512;
        
            @GuardedBy("this")
            private final Socket socket;
        
            private CancellableSocketTask(@NonNull Socket socket) {
                this.socket = socket;
            }
        
            public static CancellableSocketTask of(@NonNull Socket socket) {
                return new CancellableSocketTask(socket);
            }
        
            // From ICancellableTask and called by RunnableFuture::cancel
            @Override 
            public synchronized void cancel() {
                log.info(">>>>>>> Being asked for cancelling this task.");
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException ioe) {
                    log.warn(">>>>>>> Caught {} when closing the Socket",
                            ioe.getClass().getSimpleName());
                }
            }
        
            // From ICancellableTask and called by ThreadPoolExecutor::newTaskFor 
            @Override 
            public RunnableFuture<Void> newTask() {
                log.info(">>>>>>> Creating a new FutureTask which contains this SocketTask");
                return new FutureTask<>(this) {
                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        try {
                            CancellableSocketTask.this.cancel();
                        } finally {
                            return super.cancel(mayInterruptIfRunning);
                        }
                    }
                };
            }

            @Override
            public Void call() throws Exception {
        
                try (InputStream in = this.socket.getInputStream()) {
                    byte[] buf = new byte[BUFSZ];
                    while (true) {
                        int count = in.read(buf);
                        if (count < 0) {
                            break;
                        } else if (count > 0) {
                            processBuffer(buf, count);
                        }
                    }
                } catch (IOException e) { /* Allow thread to exit */
                    // SocketException
                    log.error(">>>>>>> Caught {} when reading the Socket.",
                            e.getClass().getSimpleName());
                } finally {
                    log.info(">>>>>>> Stopped reading.");
                }
        
                return null;
            }
        
            private void processBuffer(byte[] buf, int count) {
                log.info(">>>>>>> Processing {} bytes buffer", count);
            }

        }///:~
        ```


### 7.2 Stopping a Thread-Based Service

> Sensible encapsulation practices dictate that you should not manipulate a 
> thread—interrupt it, modify its priority, etc.—unless you own it 


> But, the thread API has no formal concept of thread ownership: 
> a thread is represented with a ``` Thread ``` object that can be freely shared 
> like any other object


> However, it makes sense to think of a thread as having an owner, 
> and this is usually the class that created the thread


> So a thread pool owns its worker threads, and if those threads need to be 
> interrupted, the thread pool should take care of it 


> Thread ownership IS NOT Transitive: the application may own the service and 
> the service may own the worker threads, but the application doesn’t own the 
> worker threads and therefore should not attempt to stop them directly


> Instead, the service should provide lifecycle methods for shutting itself 
> down that also shut down the owned threads; then the application can shut down 
> the service, and the service can shut down the threads 


> Executor-Service provides the shutdown and shutdownNow methods; 
> other thread-owning services should provide a similar shutdown mechanism

> Provide lifecycle methods whenever a thread-owning service has a lifetime 
> longer than that of the method that created it 


#### Example: A Logging Service

> Cancelling a producerconsumer activity requires cancelling both the producers 
> and the consumers 


#### 7.2.3 Poison Pills

A ___Poison Pill___
  - A recognizable object placed on the queue that means “when you get this, 
    stop.”
  - With a FIFO queue, ___Poison Pills___ ensure that consumers finish the 
    work on their queue before shutting down
  - Any work submitted prior to submitting the ___Poison Pill___ will be 
    retrieved before the pill
  - Producers should not submit any work after putting a poison pill on the 
    queue
  - ___Poison Pills___ work only when the number of producers and consumers 
    is known
  - ___Poison Pills___ work reliably only with unbounded queues


#### 7.2.4 One-Shot Execution Service

- A method needs to process a batch of tasks and does not return until all the 
  tasks are finished
    - Using private ``` Executor ``` whose lifetime is bounded by that method to
      simplify service lifecycle management
    - The ``` invokeAll ``` and ``` invokeAny ``` methods can often be useful 
      in such situations


#### 7.2.5 How to Keep Track of Cancelled Tasks after ``` shutdownNow ```

##### The Requirement

- To know which tasks have not completed, you need to know not only which tasks 
  didn’t start, but also which tasks were in progress when the executor was 
  shut down

- However, there is no general way to find out which tasks started but did not 
  omplete
    - This means that there is no way of knowing the state of the tasks in 
      progress at shutdown time unless the tasks themselves perform some sort 
      of checkpointing


#### The Solution

- The technique for determining which tasks were in progress at shutdown time:
  - Encapsulating an ``` ExecutorService ```
  - Instrumenting ``` execute ``` (and similarly submit, not shown) to remember 
    which tasks were cancelled after shutdown
      - Identifying which tasks started but did not complete normally
  - In order for this technique to work, the tasks must preserve the thread’s 
    interrupted status when they return, which well behaved tasks will do anyway 
    
    ``` 
    Thread.currentThread().interrupt();
    ```

    ``` 
    @Override
    public void execute(final Runnable runnable) {
        this.executor.execute(() -> {
            try {
                runnable.run();
            } finally {
                if (isShutdown() && Thread.currentThread().isInterrupted()) {
                    this.tasksCancelledAtShutdown.add(runnable);
                }
            }
        });
    }
    ```


### 7.3 Handling Abnormal Thread Termination

#### 7.3.0 A proactive approach to the problem of unchecked exceptions

> Task-processing threads such as the worker threads in a thread pool should 
> call tasks within a try-catch block that catches unchecked exceptions, or 
> within a try-finally block to ensure that if the thread exits abnormally 
> the framework is informed of this and can take corrective action

> This is one of the few times when you might want to consider catching 
> RuntimeException—when you are calling unknown, untrusted code through an 
> abstraction such as Runnable 


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


#### 7.3.1 Uncaught Exception Handlers

- The ``` Thread ``` API also provides the ``` UncaughtExceptionHandler ``` 
  facility, which lets you detect when a thread dies due to an uncaught exception 
    - When a thread exits due to an uncaught exception, the JVM reports this 
      event to an application-provided ``` UncaughtExceptionHandler ```
    - What the handler should do with an uncaught exception depends on your 
      quality-of-service requirements


- The most common response is to write an error message and stack trace to the 
  application log
  ``` 
    @Slf4j
    public class UncaughtExceptionLoggingHandler implements
    Thread.UncaughtExceptionHandler {
    
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error(">>>>>>> Thread {} terminated with exception: {}", 
                    t.getName(), e);
        }
    
    }
  ```


> In long-running applications, always use uncaught exception handlers for all 
> threads that at least log the exception 


- To set an ``` UncaughtExceptionHandler ``` for pool threads, provide a 
  ``` ThreadFactory ``` to the ``` ThreadPoolExecutor ``` constructor


- The standard thread pools allow an uncaught task exception to terminate the 
  pool thread, but use a try-finally block to be notified when this happens so 
  the thread can be replaced


- Without an uncaught exception handler or other failure notification mechanism, 
  tasks can appear to fail silently, which can be very confusing


- If you want to be notified when a task fails due to an exception so that you 
  can take some task-specific recovery action, either wrap the task with a 
  Runnable or Callable that catches the exception or override the ``` afterExecute ```
  hook in ``` ThreadPoolExecutor ```
    - ``` protected void afterExecute(Runnable r, Throwable t) ```


- Note: Somewhat confusingly, exceptions thrown from tasks make it to the 
  uncaught exception handler only for tasks submitted with execute
  

- For tasks submitted with submit, any thrown exception, checked or not, 
  is considered to be part of the task’s return status
    - If a task submitted with submit terminates with an exception, it is 
      rethrown by ``` Future.get ```, wrapped in an ``` ExecutionException ```


### 7.4 JVM Shutdown

#### Different Ways to Shutdown JVM

- The JVM can shut down in an orderly manner
    - An orderly shutdown is initiated when the last “normal” (nondaemon) thread 
      terminates, someone calls System.exit, 
    - By other platform-specific means  (such as sending a SIGINT or 
      hitting Ctrl-C)
        - While this is the standard and preferred way for the JVM to shut down

- The JVM can also shut down in abrupt manner
    - Calling Runtime.halt
    - Killing the JVM process through the operating system (such as sending a 
      SIGKILL)


#### 7.4.1 Shutdown Hooks

- In an orderly shutdown, the JVM first starts all registered shutdown hooks
    - Shutdown hooks are unstarted threads that are registered with 
      ``` Runtime.addShutdownHook ``` (no guarantees on the order)
    - If any application threads (daemon or nondaemon) are still running at 
      shutdown time, they continue to run concurrently with the shutdown process
    - When all shutdown hooks have completed, the JVM may choose to run 
      finalizers if ``` runFinalizersOnExit ``` is true, and then halts
    - The JVM makes no attempt to stop or interrupt any application threads 
      that are still running at shutdown time; they are abruptly terminated 
      when the JVM eventually halts
    - If the shutdown hooks or finalizers don’t complete, then the orderly 
      shutdown process “hangs” and the JVM must be shut down abruptly


- In an abrupt shutdown, the JVM is not required to do anything other than halt 
  the JVM; shutdown hooks will not run


- Shutdown hooks should be thread-safe
    - They must use synchronization when accessing shared data and should be 
      careful to avoid deadlock
    - They should not make assumptions about 
        - the state of the application
            - whether other services have shut down already
            - all normal threads have completed or not
        - why the JVM is shutting down
    - Must therefore be coded extremely defensively


- Shutdown hooks should exit as quickly as possible

- Because shutdown hooks all run concurrently, shutdown hooks should not rely 
  on services that can be shut down by the application or other shutdown hooks 
    - Use a single shutdown hook for all services, rather than one for each 
      service, and have it call a series of shutdown actions 
    - This ensures that shutdown actions execute sequentially in a single thread, 
      thus avoiding the possibility of race conditions or deadlock between 
      shutdown actions


> Using only one single shutdown hook for all services can be used whether or 
> not you use shutdown hooks 


> Executing shutdown actions sequentially rather than concurrently eliminates 
> many potential sources of failure 


> In applications that maintain explicit dependency information among services, 
> executing shutdown actions sequentially can also ensure that shutdown actions 
> are performed in the right order 


#### 7.4.2 Daemon threads

- The existence of daemon threads will not prevent the JVM from shutting down
  This is what daemon threads are for 


- Threads are divided into two types
    - Normal threads 
    - daemon threads


- When the JVM starts up, all the threads it creates, except the main thread, 
  are daemon threads, such as
    - Garbage collector 
    - Other housekeeping threads 


- Any threads created by the main thread are also normal threads
    - When a new thread is created, it inherits the daemon status of the thread 
      that created it


- When a thread exits, the JVM performs an inventory of running threads, and 
  if the only threads that are left are daemon threads, it initiates an orderly 
  shutdown; when the JVM halts, the JVM just exits
    - Any remaining daemon threads are abandoned 
    - All finally blocks are not executed, stacks are not unwound


- Daemon threads should not be used for any sort of I/O


- Daemon threads are best saved for “housekeeping” tasks, such as a background 
  thread that periodically removes expired entries from an in-memory cache 


> Daemon threads are not a good substitute for properly managing the lifecycle 
> of services within an application


## Chapter 8 Applying Thread Pools

### 8.1 Implicit Couplings between Tasks and Execution Policies

#### Overview
> Not all tasks are compatible with all Execution Policies

- Types of tasks that require specific execution policies include
    - Dependent Tasks
    - Tasks that exploit thread confinement
        - Single-threaded executors make stronger promises about concurrency 
          than do arbitrary thread pools. They guarantee that tasks are not 
          executed concurrently
    - Response-Time-Sensitive Tasks
    - Tasks that use ``` ThreadLocal ```
        - ``` ThreadLocal ``` makes sense to use in pool threads only if the 
          thread-local value has a lifetime that is bounded by that of a task
        - ``` ThreadLocal ``` should not be used in pool threads to communicate 
          values between tasks


> Thread pools work best when tasks are homogeneous and independent

> Mixing long-running and short-running tasks risks “clogging” the pool unless 
> it is very large

> Submitting tasks that depend on other tasks risks deadlock unless the pool is 
> unbounded and large enough that tasks are never queued or rejected

> Tasks that exploit thread confinement require sequential execution

> Document the requirements above so that future maintainers do not undermine 
> safety or liveness by substituting an incompatible execution policy


#### 8.1.1 Thread Starvation Deadlock

- What is ___thread starvation deadlock___
     - If tasks that depend on other tasks execute in a thread pool, they can 
       deadlock
     -  In a single-threaded executor, a task that submits another task to the 
        same  executor and waits for its result will ___always deadlock___
     - In larger thread pools, if all threads are executing tasks that are 
       blocked waiting for other tasks still on the work queue


> Whenever submitting to an Executor tasks that are not independent, be aware of 
> the possibility of thread starvation deadlock, and document any pool sizing or 
> configuration constraints in the code or configuration file where the Executor 
> is configured


#### 8.1.2 Long-running tasks

- Thread pools can have responsiveness problems if tasks can block for extended 
  periods of time, even if deadlock is not a possibility


### 8.2 Sizing thread pools


#### Overview

> The ideal size for a thread pool depends on the types of tasks that will be 
> submitted and the characteristics of the deployment system


> Thread pool sizes should be provided by a configuration mechanism or computed 
> dynamically by consulting ``` Runtime.availableProcessors ```

- To size a thread pool properly, need to understand 
    - the computing environment 
    - the resource budget 
    - the nature of the tasks


> If having different categories of tasks with very different behaviors, 
> consider using multiple thread pools so each can be tuned according to its 
> workload


- How many processors does the deployment system have?


- How much memory?


- Do tasks perform mostly computation, I/O, or some combination?


- Do they require a scarce resource, such as a JDBC connection?


#### Compute-Intensive Tasks

- An Ncpu-processor system usually achieves optimum utilization with a 
  thread pool of ___Ncpu + 1___ threads
    - Even Compute-Intensive Threads occasionally take a page fault or pause for 
      some other reason, so an “extra” runnable thread prevents CPU cycles from 
      going unused when this happens  


#### For Tasks that also include I/O or other Blocking Operations

- Needs a larger pool since not all of the threads will be schedulable at all 
  times


- In order to size the pool properly, you must estimate the ratio of waiting 
  time to compute time for your tasks 


- Alternatively, the size of the thread pool can be tuned by running the 
  application using several different pool sizes under a benchmark load and 
  observing the level of CPU utilization

![Thread Pool Size Calculation](Thread_Pool_Size_Calculation.png 
"Thread Pool Size Calculation")


- Other Resources that can contribute to sizing constraints
    - Memory 
    - File handles 
    - Socket handles 
    - Database Connections
    - Add up how much of that resource each task requires and divide that into 
      the total quantity available, the result will be an upper bound on the 
      pool size


> When tasks require a pooled resource such as database connections, 
> thread pool size and resource pool size affect each other

>If each task requires a connection, the effective size of the thread pool is 
> limited by the connection pool size

> When the only consumers of connections are pool tasks, the effective size of 
> the connection pool is limited by the thread pool size


### 8.3 Configuring ``` ThreadPoolExecutor ```

#### The most general constructor of ``` ThreadPoolExecutor ```

``` 
public ThreadPoolExecutor(
        int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        BlockingQueue<Runnable> workQueue,
        ThreadFactory threadFactory,
        RejectedExecutionHandler handler) { ... }
```

#### 8.3.1 Thread creation and teardown

- The ___core pool size___, ___maximum pool size___, and ___keep-alive time___ 
  govern thread ___creation___ and ___teardown___


- ``` corePoolSize ```, the core size is the target size
    - The implementation attempts to maintain the pool at this size even when 
      there are no tasks to execute and will not create more threads than this 
      unless the work queue is full
    - When a ThreadPoolExecutor is initially created, the core threads are not 
      started immediately but instead as tasks are submitted, unless you call 
      ``` prestartAllCoreThreads ```


- ``` maximumPoolSize ```, the maximum pool size is the upper bound on how many 
  pool threads can be active at once 


- ``` keepAliveTime ```, a thread that has been idle for longer than the 
  ``` keepAliveTime ``` becomes a candidate for reaping and can be terminated 
  if the current pool size exceeds the core size


- ``` allowCoreThreadTimeOut ``` allows you to request that all pool threads be 
  able to time out; enable this feature with a core size of zero if you want a 
  bounded thread pool with a bounded work queue but still have all the threads 
  torn down when there is no work to do


#### 8.3.2 Managing Queued Tasks

- Bounded thread pools limit the number of tasks that can be executed concurrently


- With tasks that depend on other tasks, bounded thread pools or queues can 
  cause thread starvation deadlock; instead, use an unbounded pool configuration 
  like newCachedThreadPool


#### 8.3.3 Saturation Policies

> ``` AbortPolicy ```, ``` CallerRunsPolicy ```, ``` DiscardPolicy ```, and 
> ``` DiscardOldestPolicy ```

- The saturation policy for a ThreadPoolExecutor can be modified by calling
  ``` 
  // ThreadPoolExecutor::setRejectedExecutionHandler
  public void setRejectedExecutionHandler(RejectedExecutionHandler handler) 
  ```

- The saturation policy is also used when a task is submitted to an Executor 
  that has been shut down


- The default policy: ``` AbortPolicy ``` causes execute to throw the unchecked 
  ``` RejectedExecutionException ```


- The ``` DiscardPolicy ``` silently discards the newly submitted task if it 
  cannot be  queued for execution 


- the ``` DiscardOldestPolicy ``` discards the task that would otherwise be 
  executed next and tries to resubmit the new task


- The ``` CallerRunsPolicy ``` implements a form of throttling that neither 
  discards tasks nor throws an exception, but instead tries to slow down the 
  flow of new tasks by pushing some of the work back to the caller
  ``` 
  public static class ThreadPoolExecutor.CallerRunsPolicy extends Object 
        implements RejectedExecutionHandler
  ```

    - A handler for rejected tasks that runs the rejected task directly in the 
      calling thread of the execute method, unless the executor has been shut 
      down, in which case the task is discarded 
    - Since this would probably take some time, the main thread cannot submit 
      any more tasks for at least a little while and giving the worker threads 
      some time to catch up on the backlog 
    - Enabling more graceful degradation under load


- How to ___make ``` execute ``` block___ when the work queue is full
    - Using a ``` Semaphore ``` to bound the task injection rate as shown in 
      ``` BoundedExecutor ```
    - In such an approach, use an ___unbounded queue___ 
        - There’s no reason to bound both the queue size and the injection rate
    - Set the bound on the semaphore to be equal to the pool size plus the 
      number of queued tasks you want to allow
        - Since the semaphore is bounding the number of tasks both currently 
          executing and awaiting execution

    ``` 
    public class BoundedExecutor {
        private final ExecutorService executor;
        private final Semaphore semaphore;
        public void submitTask(final Runnable command) 
                throws InterruptedException {
            this.semaphore.acquire();
            try {
                this.executor.execute(() -> {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                });
            } catch (RejectedExecutionException ree) {
                semaphore.release();
            }
        }
    }
    ```

![Working of Semaphore](Working-of-Semaphore-in-Java.png
"Working of Semaphore")


#### 8.3.4 Thread Factories

- Specifying a thread factory allows to customize the configuration of pool threads


- ``` ThreadFactory ``` has a single method, ``` newThread ```, that is called 
  whenever a thread pool needs to create a new thread 


- Reasons to use a custom thread factory
    - To specify an ``` UncaughtExceptionHandler ``` for pool threads
    - To instantiate an instance of a custom ``` Thread ``` class, such as one 
      that performs debug logging
    - To modify the priority
    - To set the daemon status
    - To give pool threads more meaningful names to simplify interpreting thread 
      dumps and error logs
    - To take advantage of ___security policies___ to grant permissions to 
      particular codebases
        - To use the ``` privilegedThreadFactory ``` factory method in Executors 
          to construct your thread factory
        - It creates pool threads that have the same permissions, 
          ``` AccessControlContext ```, and ``` contextClassLoader ``` as
          the thread creating the ``` privilegedThreadFactory ```
        - Otherwise, threads created by the thread pool inherit permissions from 
          whatever client happens to be calling ``` execute ``` or ``` submit ``` 
          at the time a new thread is needed, which could cause confusing 
          security-related exceptions


#### 8.3.5 Customizing ThreadPoolExecutor after Construction

- The options passed to the ThreadPoolExecutor constructors can also be modified 
  after construction via setters
    - core thread pool size 
    - maximum thread pool size 
    - keep-alive time 
    - thread factory 
    - rejected execution handler

- If the Executor is created through one of the factory methods in ``` Executors ```
  (except ``` newSingleThreadExecutor ``` method), you can cast the result to 
  ``` ThreadPoolExecutor ``` to access the setters
  ``` 
    ExecutorService exec = Executors.newCachedThreadPool();
    if (exec instanceof ThreadPoolExecutor) {
        ((ThreadPoolExecutor) exec).setCorePoolSize(10);
    } else {
        throw new AssertionError("Oops, bad assumption");
    }
  ```


- ``` Executors::unconfigurableExecutorService ```,  a factory method, takes an 
  existing ``` ExecutorService ``` and wraps it with one exposing only the 
  methods of ``` ExecutorService ``` so it cannot be further configured
  

- ``` Executors::newSingleThreadExecutor ``` returns an ``` ExecutorService ``` wrapped 
  in ``` unconfigurableExecutorService ```, rather than a raw ThreadPoolExecutor
    - A single-threaded executor is actually implemented as a thread pool with 
      one thread, it also promises not to execute tasks concurrently


- To prevent the execution policy from being modified, wrap the 
  ``` ExecutorService ``` with an ``` unconfigurableExecutorService ``` 
  ``` 
  public static ExecutorService unconfigurableExecutorService(
          ExecutorService executor)
  ```


### 8.4 Extending ``` ThreadPoolExecutor ```

#### Overview

- ``` ThreadPoolExecutor ``` was designed for extension and providing several 
  “hooks” for subclasses to override to extend the behavior of 
  ``` ThreadPoolExecutor ``` : 
    - ``` beforeExecute ``` and ``` afterExecute ``` 
        - are called in the thread that executes the task
        - can be used for adding logging, timing, monitoring, or statistics 
          gathering
        - ``` afterExecute ``` is called whether the task completes by returning 
          normally from run or by throwing an Exception
        - If the task completes with an ``` Error ```, ``` afterExecute ``` is 
          not called
        - If beforeExecute throws a ``` RuntimeException ```, the task is not 
          executed and ``` afterExecute ``` is not called
    - ``` terminated ```  
        - The ``` terminated ``` hook is called when the thread pool completes 
          the shutdown process, after all tasks have finished and all worker 
          threads have shut down 
        - Used to release resources allocated by the ``` Executor ``` during its 
          lifecycle, perform notification or logging, or finalize statistics 
          gathering


``` 
protected void beforeExecute(Thread t, Runnable r)
```
- Parameters 
    - Thread t - the thread that will run task r
    - Runnable r - the task that will be executed
- Method invoked prior to executing the given Runnable in the given thread
- This method is invoked by thread t that will execute task r, and may be 
  used to re-initialize ThreadLocals, or to perform logging 
- This implementation does nothing, but may be customized in subclasses
- Note: To properly nest multiple overridings, subclasses should generally 
  invoke ``` super.beforeExecute ``` ___at the end___ of this method 


``` 
protected void afterExecute(Runnable r, Throwable t)
```
- Parameters:
    - Runnable r - the runnable that has completed
    - Throwable t - the exception that caused termination, or ``` null ``` if 
      execution completed normally
- Method invoked upon completion of execution of the given ``` Runnable ```
- This method is invoked by the thread that executed the task
- If non-null, the ``` Throwable ``` is the uncaught RuntimeException or 
  ``` Error ``` that caused execution to terminate abruptly
- This implementation does nothing, but may be customized in subclasses
    - Note: To properly nest multiple overridings, subclasses should generally 
      invoke ``` super.afterExecute ``` at the beginning of this method 
- Note: 
    - When actions are enclosed in tasks (such as ``` FutureTask ```) either 
      explicitly or via methods such as ``` submit ```, these task objects 
      catch and maintain computational exceptions, and so they do not cause 
      abrupt termination, and the internal exceptions are not passed to this 
      method
    - To trap both kinds of failures in this method
    ``` 
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?> && ((Future<?>)r).isDone()) {
            try {
                Object result = ((Future<?>) r).get();
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                // ignore/reset
                Thread.currentThread().interrupt();
            }
        }
        if (t != null)
            System.out.println(t);
        }
    }
    ```

``` 
protected void terminated()
```
- Method invoked when the ``` Executor ``` has terminated
- Default implementation does nothing
- Note: To properly nest multiple overridings, subclasses should generally 
  invoke ``` super.terminated ``` within this method 


#### 8.4.1 Example: adding statistics to a thread pool


### 8.5 Parallelizing Recursive Algorithms

#### Overview

> Sequential loop iterations are suitable for parallelization when each 
> iteration is independent of the others and the work done in each iteration of 
> the loop body is significant enough to offset the cost of managing a new task


- Loops, whose iterations are independent, being frequently good candidates for 
  parallelization if the loop body
  contain 
    - nontrivial computation 
    - perform potentially blocking I/O 


#### Loop Parallelization for Recursive Designs

- The easier case is when each iteration does not require the results of the 
  recursive iterations it invokes
    - the traversal is still sequential: only the calls to compute are executed 
      in parallel


#### 8.5.1 Example: A Puzzle Framework

- Result-Bearing Latch 
    - The requirement: 
        - In order to stop searching when we find a solution, we need a way to 
          determine whether any thread has found a solution yet
        - If we want to accept the first solution found, we also need to update the 
          solution only if no other task has already found one
    - It's often easier and less error-prone to use existing library classes 
      rather than low-level language mechanisms
    - ``` ValueLatch ``` below uses a ``` CountDownLatch ``` to provide the 
      needed latching behavior, and uses locking to ensure that the solution is 
      set only once

``` 
package com.yulikexuan.concurrency.threadpool.recursive;

import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.CountDownLatch;

public class ValueLatch<T> {

    @GuardedBy("this")
    private T value = null;

    private final CountDownLatch doneLatch = new CountDownLatch(1);

    public boolean isSet() {
        return (doneLatch.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            doneLatch.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        doneLatch.await();
        synchronized (this) {
            return value;
        }
    }

}
```