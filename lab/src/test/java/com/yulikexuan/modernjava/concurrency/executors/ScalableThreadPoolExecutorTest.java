//: com.yulikexuan.modernjava.concurrency.executors.ScalableThreadPoolExecutorTest.java


package com.yulikexuan.modernjava.concurrency.executors;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

/*
 * Core and Maximum Pool Sizes
 *   - corePoolSize: the number of threads to keep in the pool, even if they
 *     are idle, unless allowCoreThreadTimeOut is set
 *   - maximumPoolSize: the maximum number of threads to allow in the pool
 *   - If fewer than corePoolSize threads are running, a new thread is created
 *     to handle the request, even if other worker threads are idle
 *   - If fewer than maximumPoolSize threads are running, a new thread will be
 *     created to handle the request only if the queue is full
 *   - By setting corePoolSize == maximumPoolSize, then create a fixed-size
 *     thread pool
 *   - By setting maximumPoolSize == Integer.MAX_VALUE, then allowing the pool
 *     to accommodate an arbitrary number of concurrent tasks
 *   - Core and maximum pool sizes are set only upon construction
 *     but they may also be changed dynamically using
 *       - setCorePoolSize(int)
 *       - setMaximumPoolSize(int)
 *
 * On-demand Construction
 *   - By default, core threads are initially created and started only when
 *     new tasks arrive
 *   - But this can be overridden dynamically using method
 *       - prestartCoreThread() // Only start one
 *       - prestartAllCoreThreads() // Start all
 *       - Prestart threads if constructing the pool with a non-empty queue
 *
 * Creating New Threads
 *   - New threads are created using a ThreadFactory
 *   - If not otherwise specified
 *     - A Executors.defaultThreadFactory() is used
 *     - Creates threads to all be in the same ThreadGroup and with the same
 *       NORM_PRIORITY priority and non-daemon status
 *   - By supplying a different ThreadFactory, you can alter the thread's name,
 *     thread group, priority, daemon status, etc
 *   - If a ThreadFactory fails to create a thread when asked by returning null
 *     from newThread, the executor will continue, but might not be able to
 *     execute any tasks
 *   - Threads should possess the "modifyThread" RuntimePermission
 *       - If worker threads or other threads using the pool do not possess this
 *         permission, service may be degraded: configuration changes may not take
 *         effect in a timely manner, and a shutdown pool may remain in a state in
 *         which termination is possible but not completed
 *
 * Keep-Alive Times
 *   - If the pool currently has more than corePoolSize threads, excess threads
 *     will be terminated if they have been idle for more than the keepAliveTime
 *     (see getKeepAliveTime(TimeUnit))
 *       - This provides a means of reducing resource consumption when the pool
 *         is not being actively used
 *       - If the pool becomes more active later, new threads will be constructed
 *   - This parameter (getKeepAliveTime(TimeUnit)) can also be changed
 *       dynamically using method setKeepAliveTime(long, TimeUnit)
 *       - Using a value of Long.MAX_VALUE TimeUnit.NANOSECONDS effectively
 *         disables idle threads from ever terminating prior to shut down
 *   - By default, the keep-alive policy applies only when there are more than
 *     corePoolSize threads, but method allowCoreThreadTimeOut(boolean) can be
 *     used to apply this time-out policy to core threads as well, so long as
 *     the keepAliveTime value is non-zero
 *
 * Queuing
 *   - Any BlockingQueue may be used to transfer and hold submitted tasks
 *     - The use of this queue interacts with pool sizing
 *       - If fewer than corePoolSize threads are running, the Executor always
 *         prefers adding a new thread rather than queuing
 *       - If corePoolSize or more threads are running, the Executor always
 *         prefers queuing a request rather than adding a new thread
 *       - If a request cannot be queued, a new thread is created unless this
 *         would exceed maximumPoolSize, in which case, the task will be
 *         rejected
 *   - The three general strategies for queuing
 *     - 1. Direct Handoffs
 *          - A good default choice for a work queue is a SynchronousQueue that
 *            hands off tasks to threads without otherwise holding them
 *          - Here, an attempt to queue a task will fail if no threads are
 *            immediately available to run it, so a new thread will be
 *            constructed
 *          - This policy avoids lockups when handling sets of requests that
 *            might have internal dependencies
 *          - Direct handoffs generally require unbounded maximumPoolSizes to
 *            avoid rejection of new submitted tasks
 *          - This in turn admits the possibility of unbounded thread growth
 *            when commands continue to arrive on average faster than they can
 *            be processed
 *     - 2. Unbounded queues
 *            - Using an unbounded queue (for example a LinkedBlockingQueue
 *              without a predefined capacity) will cause new tasks to wait in
 *              the queue when all corePoolSize threads are busy.
 *            - Thus, no more than corePoolSize threads will ever be created
 *              (And the value of the maximumPoolSize therefore doesn't have
 *              any effect.)
 *            - This may be appropriate when each task is completely independent
 *              of others, so tasks cannot affect each others execution
 *              - For example, in a web page server
 *              - While this style of queuing can be useful in smoothing out
 *                transient bursts of requests, it admits the possibility of
 *                unbounded work queue growth when commands continue to arrive
 *                on average faster than they can be processed
 *     - 3. Bounded queues
 *            - A bounded queue (for example, an ArrayBlockingQueue) helps
 *              prevent resource exhaustion when used with finite
 *              maximumPoolSizes, but can be more difficult to tune and control
 *            - Queue sizes and maximum pool sizes may be traded off for each
 *              other:
 *                - Using large queues and small pools minimizes CPU usage,
 *                  OS resources, and context-switching overhead, but can lead
 *                  to artificially low throughput
 *                    - If tasks frequently block (for example if they are I/O
 *                      bound), a system may be able to schedule time for more
 *                      threads than you otherwise allow
 *                - Use of small queues generally requires larger pool sizes,
 *                  which keeps CPUs busier but may encounter unacceptable
 *                  scheduling overhead, which also decreases throughput
 *
 * Rejected Tasks
 *   - New tasks submitted in method execute(Runnable) will be rejected
 *       - when the Executor has been shut down
 *       - when the Executor uses finite bounds for both maximum threads and
 *         work queue capacity, and is saturated
 *   - In either case, the execute method invokes the
 *     RejectedExecutionHandler.rejectedExecution(Runnable, ThreadPoolExecutor)
 *     method of its RejectedExecutionHandler
 *   - Four predefined handler policies are provided:
 *       1. In the default ThreadPoolExecutor.AbortPolicy, the handler throws a
 *          runtime RejectedExecutionException upon rejection
 *       2. In ThreadPoolExecutor.CallerRunsPolicy, the thread that invokes
 *          execute itself runs the task
 *            - This provides a simple feedback control mechanism that will
 *              slow down the rate that new tasks are submitted
 *       3. In ThreadPoolExecutor.DiscardPolicy, a task that cannot be executed
 *          is simply dropped
 *       4. In ThreadPoolExecutor.DiscardOldestPolicy, if the executor is not
 *          shut down, the task at the head of the work queue is dropped, and
 *          then execution is retried (which can fail again, causing this to be
 *          repeated.)
 *   - It is possible to define and use other kinds of RejectedExecutionHandler
 *     classes
 *       - Doing so requires some care especially when policies are designed to
 *         work only under particular capacity or queuing policies
 *
 * Hook methods
 *   - This class provides protected overridable beforeExecute
 *     (Thread, Runnable) and afterExecute(Runnable, Throwable) methods that
 *     are called before and after execution of each task
 *   - These can be used to manipulate the execution environment
 *       - For example, reinitializing ThreadLocals, gathering statistics, or
 *         adding log entries
 *   - Method terminated() can be overridden to perform any special processing
 *     that needs to be done once the Executor has fully terminated
 *   - If hook, callback, or BlockingQueue methods throw exceptions, internal
 *     worker threads may in turn fail, abruptly terminate, and possibly be
 *     replaced
 *
 * Queue maintenance
 *   - Method getQueue() allows access to the work queue for purposes of
 *     monitoring and debugging
 *   - Use of this method for any other purpose is strongly discouraged
 *   - Two supplied methods:
 *       - remove(Runnable)
 *       - purge()
 *     are available to assist in storage reclamation when large numbers of
 *     queued tasks become cancelled
 *
 * Reclamation
 *   - A pool that is no longer referenced in a program AND has no remaining
 *     threads may be reclaimed (garbage collected) without being explicitly
 *     shutdown
 *       - You can configure a pool to allow all unused threads to eventually
 *         die by setting appropriate keep-alive times, using a lower bound of
 *         zero core threads and/or setting allowCoreThreadTimeOut(boolean)
 */
@DisplayName("Test for Creating Custom ThreadPoolExecutor - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ScalableThreadPoolExecutorTest {

}///:~