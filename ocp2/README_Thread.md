# OCP II - Threads

## Defining, Instantiating, and Starting Threads

> ___OCP Objective 10.1 Create worker threads using Runnable, Callable, and use an ExecutorService to concurrently execute tasks___ 

### Java, "thread" means two different things:
- An instance of class ``` java.lang.Thread ```
- A thread of execution
    - An individual lightweight process that has its own call stack 
    - In Java, there is one thread per call stack / one call stack per thread

> ### When it comes to threads, very little is guaranteed 

> Don't make the mistake of designing your program to be dependent on a 
> particular implementation of the JVM

> Different JVMs can run threads in profoundly (very seriously; completely) 
> different ways 

### Thread methods: 
- ``` start() ```
- ``` yield() ```
- ``` sleep() ```
- ``` run() ```

### The thread of execution - the new call stack - always begins by invoking run()

### Defining a Thread
- Extend the ``` java.lang.Thread ``` class
- Implement the ``` Runnable ``` interface
- Lambda

### Instantiating a Thread
- Every thread of execution begins as an instance of class Thread 
- Regardless of whether your run() method is in a Thread subclass or a Runnable 
  implementation class, you still need a Thread object to do the work 
- ``` MyThread t = new MyThread() ```
- ___The ``` Thread ``` is the "worker," and the ``` Runnable ``` is the "job" to 
  be done___ 
- If you create a thread using the no-arg constructor, the thread will call its 
  own ``` run() ``` method when it's time to start working 
- Pass a single ``` Runnable ``` instance to multiple ``` Thread ``` objects 
    - so that the same ``` Runnable ``` becomes the target of multiple threads 
- Pass a ``` Thread ``` to another ``` Thread ``` 's constructor 
- Examples:
  ```
  Thread t = new Thread(new MyRunnable());
  ```
  
  ``` 
  Thread t = new Thread(
          () -> System.out.println("Important job running in a Runnable"));
  ```
  
  ``` 
  Thread t = new Thread(new MyThread()); // but it's legal
  ```

### Thread's Constructors
- ``` Thread() ```
- ``` Thread(Runnable target) ```
- ``` Thread(Runnable target, String name) ```
- ``` Thread(String name) ```

### Thread States
- new state
- alive state
    - Once the ``` start() ``` method is called, the thread is considered 
      alive, even though the ``` run() ``` method may not have actually started 
      executing yet
- A thread is considered dead (no longer alive) after the ``` run() ``` method 
  completes
- The ``` isAlive() ``` method is the best way to determine if a thread has been 
  started but has not yet completed its ``` run() ``` method

### Starting a Thread
- ``` thread.start() ```
- what happens after you call ``` start() ``` ?
    - A new thread of execution starts (with a new call stack)
    - The thread moves from the new state to the runnable state
    - When the thread gets a chance to execute, its target run() method will run
    
### Starting and Running Multiple Threads
> Nothing is guaranteed except this: Each thread will start, and each thread 
> will run to completion
- Within each thread, things will happen in a predictable order
- But the actions of different threads can mix in unpredictable ways
- Just because a series of threads are started in a particular order doesn't 
  mean they'll run in that order

> A thread is done being a thread when its target run() method completes

> Once a thread has been started, it can never be started again


### The Thread Scheduler

> The order in which runnable threads are chosen to run is not guaranteed

#### Methods from the ``` java.lang.Thread ``` can influence thread scheduling 

``` 
public static void sleep(long millis) throws InterruptedException // Overloaded
public static void yield()
public final void join() throws InterruptedException // Overloaded
public final void setPriority(int newPriority)
```

#### Thread-Related Methods from the ``` java.lang.Object ``` Class

``` 
public final void wait() throws InterruptedException // Overloaded
public final void notify()
public final void notifyAll()
```

## Thread States and Transitions

### Thread States

             WAITING / BLOCKING 
               /           \
              /             \
    NEW -> RUNNABLE <- -> RUNNING -> DEAD

#### Some methods may look like they tell another thread to block, but they don't 
- If having a reference t to another thread
  ```
  t.sleep(); 
  t.yield();
  ```
- But these are actually static methods of the Thread class
- They don't affect the instance t
- Instead, they are defined to ___ALWAYS AFFECT THE THREAD THAT'S CURRENTLY EXECUTING___

#### ``` suspend() ``` of ``` Thread ``` lets one thread tell another to suspend
- But the ``` suspend() ``` method has been deprecated and will be removed
- Don't study it; don't use it

#### ``` stop() ``` lets one thread forces another thread to stop executing
- But the ``` stop() ``` method has been deprecated since v1.2
- Don't study it; don't use it

### Preventing Thread Execution

#### Sleeping 
> When a thread sleeps, it drifts off (to fall asleep) somewhere and doesn't 
> return to runnable until it wakes up

#### Thread Priorities 

> Don't rely on thread priorities when designing your multithreaded application 
> Because thread-scheduling priority behavior is not guaranteed, 
> it’s better to avoid modifying thread priorities
> Usually, default priority will be fine 

- In most JVMs, the scheduler does use thread priorities in one important way
    - If a thread enters the runnable state and it has a higher priority than 
      any of the threads in the pool AND a higher priority than the currently 
      running thread
        - ___the lower-priority running thread USUALLY will be bumped back to 
          runnable and the highest-priority thread will be chosen to run___
        - In other words, at any given time, the currently running thread 
          USUALLY will not have a priority that is lower than any of the threads 
          in the pool
        - IN MOST CASES, the running thread will be of equal or greater priority 
          than the highest-priority threads in the pool 
        - This is just AS CLOSE to a guarantee about scheduling as you'll get 
          from the JVM specification, so you MUST NEVER RELY ON thread priorities 
          to guarantee the correct behavior of your program 

> A thread gets a default priority that is the priority of the thread of 
> execution that creates it

> Can also set a thread's priority directly by calling the ``` setPriority() ```
> method on a Thread instance

- Priorities are set using a positive integer, usually between 1 and 10 
- The JVM will never change a thread's priority
- However, values 1 through 10 are not guaranteed
- Although the default priority is 5, the Thread class has the three following
  constants (static final variables) that define the range of thread priorities
  ``` 
  Thread.MIN_PRIORITY (1)
  Thread.NORM_PRIORITY (5)
  Thread.MAX_PRIORITY (10)
  ```

#### ``` yield() ``` method
``` public static void yield() ```

> Make the CURRENTLY RUNNING thread head back to runnable to allow other threads 
> of the same priority to get their turn

> So the intention is to use ``` yield() ``` to promote graceful turn-taking 
> among equal-priority threads 

> In reality, the ``` yield() ``` method isn't guaranteed to do what it claims

> Even if ``` yield() ``` does cause a thread to step out of running and back 
> to runnable, there's NO GUARANTEE the yielding thread won't just be chosen 
> again over all the others! 

> So while ``` yield() ``` might—and often does—make a running thread give up 
> its slot to another runnable thread of the same priority, there's NO GUARANTEE

> A ``` yield() ``` won't ever cause a thread to go to the 
> waiting/sleeping/blocking state 

> At most, a ``` yield() ``` will cause a thread to go from running to runnable, 
> but again, it might have no effect at all 

#### The ``` join() ``` Method

``` public final void join() throws InterruptedException ```
``` public final void join(long millis) throws InterruptedException ```

> The non-static ``` join() ``` method of class Thread lets one thread 
> "join onto the end" of another thread 

- If having a thread ``` main ``` (can be other thead) that can't do its work 
  until another thread A has completed its work, then you want thread ``` main ``` 
  to "join" thread A

  ``` 
    public static void main(String[] arge) {
        ... ...
        Thread a = new Thread(() -> { ... ...});
        a.start();
        a.join(); //joins the main thread to the end of a thread 
        // This blocks the current main thread from becoming runnable until 
        // after a thread is no longer alive
        // the main thread said "Join me (the current thread) to the end of a, 
        // so that a thread must finish before I (the current thread) can run again."
    }
  ```

#### A call to ``` sleep() ``` GUARANTEED to cause the current thread to stop executing 
- for at least the specified sleep duration (although it might be interrupted before 
  its specified time) 

#### A call to ``` yield() ``` NOT GUARANTEED to do much of anything 
- although typically, it will cause the currently running thread to move back to 
  runnable so that a thread of the same priority can have a chance 

#### A call to ``` join() ``` GUARANTEED to cause the current thread to stop executing 
- Until the thread it joins with (in other words, the thread it calls ``` join() ``` on) 
  completes, or if the thread it's trying to join with is not alive, the current 
  thread won't need to back out (退出)

> back out (of something) : to decide that you are no longer going to take part 
> in something that has been agreed
> - He lost confidence and backed out of the deal at the last minute.


## Synchronizing Code, Thread Problems 

> Objective 10.2: Identify potential threading problems among deadlock, 
> starvation, livelock, and race conditions 

> Objective 10.3: Use synchronized keyword and ``` java.util.concurrent.atomic ```
> package to control the order of thread execution 


### Synchronization and Locks

#### Every object in Java has a built-in lock (only one)
- that only comes into play when the object has synchronized method code 
- When entering a synchronized non-static method, we automatically acquire the 
  lock associated with the current instance of the class whose code we're
  executing

#### Remember the following Key Points about locking and synchronization:
- Only methods (or blocks) can be synchronized, not variables or classes
- Each object has just one lock
- Not all methods in a class need to be synchronized 
- A class can have both synchronized and non-synchronized methods 
- Once a thread acquires the lock on an object, no other thread can enter any 
  of the synchronized methods in that class (for that object)
- If a class has both synchronized and non-synchronized methods, multiple 
  threads can still access the class's non-synchronized methods
- ___If a thread goes to sleep, it holds any locks it has___ 
    - it doesn't release them 
- A thread can acquire more than one lock
- You can synchronize a block of code rather than a method
- Synchronization HURT concurrency
- When synchronizing a block of code, you specify which object's lock you want 
  to use as the lock

#### Can Static Methods Be Synchronized?

> ``` static ``` methods can be synchronized

- Only need one lock per class to synchronize static methods
    - A lock for the whole class
    - Every class loaded in Java has a corresponding instance of ```java.lang.Class ``` 
      representing that class
        - It's that ``` java.lang.Class ``` instance whose lock is used to 
          protect any synchronized static methods of the class
      ``` 
      public static int getCount() {
          synchronized (MyClass.class) {
              return count;
          }
      }
      ```
      MyClass.class is called a class literal


#### What Happens If a Thread Can't Get the Lock?

- If a thread tries to enter a synchronized method and the lock is already taken,
    - the thread is said to be blocked on the object's lock 
    - Essentially, the thread goes into a kind of POOL for that particular object 
    - and the thread has to sit there until the lock is released 
        - and the thread can again become runnable/running

- When thinking about blocking, it's important to pay attention to which objects 
  are being used for locking:
    - Threads calling non-static synchronized methods in the same class will 
      only block each other if they're invoked using the same instance 
    - if they're called using two different instances, they get two locks, which 
      do not interfere with each other
    - Threads calling static synchronized methods in the same class will always 
      block each other—they all lock on the same Class instance
    - A static synchronized method and a non-static synchronized method will not 
      block each other, ever
    - For synchronized blocks, you have to look at exactly what object has been 
      used for locking 
        - Threads that synchronize on the same object will block each other 
        - Threads that synchronize on different objects will not 

#### Methods and Lock Status

| Give up Locks | Keep Locks | Class Defining the Method
|---|---|---|
| ``` wait() ``` | ``` notify() ``` | ``` java.lang.Object ``` 
| | ``` join() ``` | ``` java.lang.Thread ```
| | ``` sleep() ``` | ``` java.lang.Thread ```
| | ``` yield() ``` | ``` java.lang.Thread ```


#### What Happens if a non-static method that accesses a static field?

#### What Happens if a static method that accesses a non-static field (using an instance)?

> A static synchronized method and a non-static synchronized method will not 
> block each other

> Methods that access changeable fields need to be synchronized 

> Access to static fields should be done using static synchronized methods 

> Access to non-static fields should be done using non-static synchronized methods 

#### What if you really need to access both static and non-static fields in a method?

> You will live a longer, happier life if you JUST DON'T DO IT 


#### Thread-Safe Classes

> The moral here is that just because a class is described as "thread-safe" 
> doesn't mean it is always thread-safe

> If individual methods are synchronized, that may not be enough
> You may be better off putting in synchronization at a higher level


### Thread Deadlock

### Thread Livelock

### Thread Starvation

### Race Conditions
> A race condition is when two or more threads try to access and change a 
> shared resource at the same time, and the result is dependent on the order 
> in which the code is executed by the threads

> In a multithreaded environment, we need to make sure our code is designed so
> it's not dependent on the ordering of the threads 

> In situations where our code is dependent on ordering or dependent on certain 
> operations not being interrupted, we can get race conditions 


## Thread Interaction ((OCP Objectives 10.2 and 10.3)

### Thread Communication 
- Threads should know the status of an event that the threads care about

### The ``` Object ``` class has three methods, that help threads' communication
- ``` wait() ``` 
- ``` notify() ``` 
- ``` notifyAll() ```

### Using ``` wait() ``` and ``` notify() ``` 
- Lets one thread put itself into a "waiting room" until some other thread 
  notifies it that there's a reason to come back out

> ``` wait(), notify() ```, and ``` notifyAll() ``` must be called from within 
> a synchronized context 

> A thread can't invoke a ``` wait() ``` or ``` notify() ``` method on an 
> object unless it owns that object's lock 

> IF ``` wait() ``` DOES NOT ACQUIRE A LOCK ON THE OBJECT ``` obj ``` BEFORE
> CALLING ``` obj.wait() ```, ``` wait() ``` THROWS AN 
> ``` IllegalMonitorStateException ``` 


### The methods ``` wait() ``` and ``` notify() ``` are instance methods of Object

### Every object can have a list of threads that are waiting for a signal of the obj
- (a notification) from the object 
- A thread gets on this Waiting List by executing the ``` wait() ``` method of 
  the target object
- From that moment, it doesn't execute any further instructions until the 
  ``` notify() ``` method of the target object is called
- If many threads are waiting on the same object, only one will be chosen 
  (in no guaranteed order) to proceed with its execution
- If there are no threads waiting, then no particular action is taken

> When the ``` wait() ``` method is invoked on an object, the thread executing 
> that code gives up its lock on the object immediately

> However, when ``` notify() ``` is called, that doesn't mean the thread gives
> up its lock at that moment

> If the thread is still completing synchronized code, the lock is not released 
> until the thread moves out of synchronized code 

> So just because notify() is called, this doesn't mean the lock becomes 
> available at that moment 

### Using notifyAll() When Many Threads May Be Waiting

``` notifyAll(); // Will notify all waiting threads ```

- All of the threads will be notified and start competing to get the lock
- As the lock is used and released by each thread, all of them will get into 
  action without a need for further notification 

> An object can have many threads waiting on it, and using ``` notify() ``` will 
> affect only one of them. Which one, exactly, is not specified and depends on 
> the JVM implementation

> So DO NOT rely on a particular thread being notified in preference to another 
> the best way to do this is by using ``` notifyAll() ```


### Using ``` wait() ``` in a Loop and Do Double-Check 

#### The issue to be solved
- ``` wait() ``` was called after ``` notify() ``` or ``` notifyAll() ``` and
  no more calling of ``` notify() ``` or ``` notifyAll() ```

#### The Solution

``` 
public void run() {
    while (true) {
        synchronized (jobs) {
            // wait until at least one job is available
            while (jobs.isEmpty()) { // Do double-check
                try {
                    jobs.wait();
                } catch (InterruptedException ie) { }
            }
            // If we get here, we know that jobs is not empty
            MachineInstructions instructions = jobs.remove(0);
            // Send machine steps to hardware
        }
    }
}
```

> It's possible that a thread has accidentally sent an extra notify() that was 
> not intended 

> There's also a possible situation called ___spontaneous wakeup___ that may 
> exist in some situations, a thread may wake up even though no code has called 
> ``` notify() ``` or ``` notifyAll() ```

> Sometimes, the JVM may call ``` notify() ``` for reasons of its own 

> Or code in some other class calls it for reasons you just don't know

> When your thread wakes up from a ``` wait() ```, you don't know for sure 
> why it was awakened

#### The Reason of the Solution

> By putting the ```wait() method ``` in a while loop and rechecking the 
> condition that represents what we were waiting for, we ensure that whatever 
> the reason we woke up, we will re-enter the ``` wait() ``` if (and only if) 
> the thing we were waiting for has not happened yet 

#### The MORAL: 
- MORAL 1
> When using ``` wait() ``` and ``` notify() ``` or ```notifyAll() ```,
> you should almost always also have a while loop around the ``` wait() ``` 
> that checks a condition and forces continued waiting until the condition is met 

- MORAL 2
> Should also make use of the required synchronization for the ``` wait() ``` 
> and ``` notify() ``` calls to also protect whatever other data you're sharing 
> between threads 

> If seeing code that fails to do these, there's usually something wrong with 
> the code, even if you have a hard time seeing what exactly the problem is 

### ``` Object ``` Methods for Thread Communication

``` 
public final void wait() throws InterruptedException
public final void wait(long timeoutMillis) throws InterruptedException
public final void wait(long timeoutMillis, int nanos) throws InterruptedException

public final void notify()
public final void notifyAll()
```

### ``` Thread ``` Methods

``` 
public void start()

public static Thread currentThread()
public static void yield()

public static void sleep(long millis) throws InterruptedException
public static void sleep(long millis, int nanos) throws InterruptedException

public final void join()throws InterruptedException
public final void join(long millis) throws InterruptedException
public final void join(long millis, int nanos) throws InterruptedException
                
public void interrupt()
public static boolean interrupted()
public boolean isInterrupted()
public final boolean isAlive()
public final void setPriority(int newPriority)
public final int getPriority()
public final void setName(String name)
public final String getName()
```
