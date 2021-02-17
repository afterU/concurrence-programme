# 1. Thread类的常用api
方法 | 功能 | 备注
---|---|---
static Thread currentThread() |返回当前线程，即当前代码执行线程(对象)|同一段代码对Thread.currentThread()的调用，其返回值可能对应不同的线程(对象)
void run() | 用于实现线程的任务处理逻辑| 该方法是由Java虚拟机直接调用的，一般情况下应用程序不应该直接调用该方法
void start() | 启动相应线程| 该方法的返回并不代表线程已经被启动，一个Thread实例的start方法只能被调用一次，多次调用会导致异常的抛出
void join | 等待相应线程运行结束| 若线程A调用线程B的join方法，那么线程A的运行会被暂停，直到线程B运行结束。join()是synchronized同步加锁方法内部调用wait()方法
static void yield()| 使当前线程主动放弃其对处理器的占用，当前线程从运行状态转为就绪状态，这可能导致当前线程被暂停| 这个方法是不可靠的，该方法被调用时当前线程可能仍然继续运行
static void sleep(long millis)|使当前线程休眠(暂停运行)制定的时间，保持对象锁|
void suspend()|使线程暂停，过期方法|不建议使用。原因：以suspend()方法，调用之后，线程不会释放已经占用的资源(比如锁)，而是占有资源进入睡眠状态，这样会导致死锁问题。
void resume()|使线程恢复，过期方法|不建议使用。
void stop()|使线程停止，过期方法|不建议使用。stop()方法在终结一个线程时不会保证线程的资源正常释放，通常是没有给予线程完成资源释放工作机会，因此会导致程序可能工作在不确定的状态
# 2. 线程的状态
状态 | 说明
---|---
NEW | 初始状态，线程被构建，但是还没有调用start()方法
RUNNABLE|运行状态，Java线程将操作系统中的就绪和运行两种状态笼统的称为“运行中”
BLOCKED|阻塞状态，表示线程阻塞于锁
WAITING|等待状态，表示线程进入等待状态，进入该状态表示当前线程需要等待其他线程做出一些特定动作(通知/中断)
TIME_WAITING| 超时等待状态，该状态不同于WAITING，它是可以在指定的时间自行返回的
TERMINATED| 终止状态，表示当前线程已经执行完毕
-![](https://i.loli.net/2021/02/17/BukVKw8ylULXHGr.png)
# 3. 等待/通知机制
- 等待/通知相关方法是任意java对象都具备的，因为这些方法被定义在所有对象的超类Object上

方法名称 | 描述
---|---
notify()|通知一个在对象上等待的线程，使其从wait()方法返回，而返回的前提是线程获取到了对象的锁
notifyAll()|通知所有等待在该对象的线程
wait()|调用该方法的线程进入waiting状态，只有等待另外线程通知或被中断才会返回，调用wait()，会释放对象的锁
wait(long)|超时等待一段时间(毫秒)，等待n毫秒，如果没有通知就超时返回
wait(long,int)|对于超时时间更细粒度的控制，可以达到纳秒
- 等待通知机制的流程
![](https://i.loli.net/2021/02/17/4DIZkfRmYLX6baJ.png)
    - WaitThread首先获取了对象的锁，然后调用对象的wait()方法，从而放弃了锁并进入了对象的等待队列
    WaitQueue中，进入等待状态。由于WaitThread释放了对象的锁，NotifyThread随后获取了对象的锁，并调用对象的notify()方法
    将WaitThread从WaitQueue移到SynchronizedQueue中，此时WaitThread的状态变为阻塞状态。NotifyThread释放了锁之后，WaitThread
    再次获取到了锁从wait()方法返回继续执行

# 4. 线程的ThreadLocal
- ThreadLocal 线程变量，是一个以ThreadLocal对象为键，任意对象为值的存储结构，这个结构被附带在线程上(具体是ThreadLocal的内部类ThreadLocalMap对象),就是说一个线程可以根据一个ThreadLocal对象查询到绑定在这个线程上的值