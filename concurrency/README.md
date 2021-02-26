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

