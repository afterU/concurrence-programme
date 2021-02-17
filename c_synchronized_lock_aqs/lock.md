# 1.并发编程
- 临界资源：多线程编程中，有可能会出现多个线程同时访问同一个共享、可变资源的情况，
这个资源我们称之其为临界资源；
- 并发安全问题：所有的并发模式在解决线程安全问题时，采用的方案都是 序列化访
问临界资源。即在同一时刻，只能有一个线程访问临界资源，也称作同步互斥访问。
- Java 中，提供了两种方式来实现同步互斥访问：synchronized 和 Lock
# 2. synchronized
## 2.1 synchronized 原理
- synchronized内置锁是一种对象锁(锁的是对象而非引用)，作用粒度是对象，可以用来实现对临界资源的同步互斥访问，是可重入的
    - 加锁方式
        1. 同步实例方法，锁是当前实例对象
        2. 同步类方法，锁是当前类对象
        3. 同步代码块，锁是括号里面的对象
- synchronized底层原理
    - synchronized是基于JVM内置锁实现，通过内部对象Monitor(监视器锁)实
现，基于进入与退出Monitor对象实现方法与代码块同步，监视器锁的实现依赖
底层操作系统的Mutex lock（互斥锁）实现，它是一个重量级锁性能较低
    - synchronized关键字被编译成字节码后会被翻译成monitorenter 和
      monitorexit 两条指令分别在同步块逻辑代码的起始位置与结束位置。
## 2.1 对象如何记录synchronized的加锁
- 对象内存布局
![](https://i.loli.net/2021/02/16/aUzDT5v7dsCS1lu.png)
- 对象头中mark word对synchronized加锁数据的存储
![](https://i.loli.net/2021/02/16/yTFQLz1AhjOx2G9.png)
## 2.2 锁的膨胀升级过程
- 锁的状态总共有四种，无锁状态、偏向锁、轻量级锁和重量级锁。随着锁的
  竞争，锁可以从偏向锁升级到轻量级锁，再升级的重量级锁，但是锁的升级是单
  向的，也就是说只能从低到高升级，不会出现锁的降级
  ![](https://i.loli.net/2021/02/16/af14o5DiQ8RUKAg.png)
## 2.3 JVM对synchronized锁的优化
- 锁粗化
- 锁清除
- 逃逸分析
    - 使用逃逸分析，编译器可以对代码做如下优化：
        1. 同步省略。如果一个对象被发现只能从一个线程被访问到，那么对于这个对象的操作可以不考虑同步。
        2. 将堆分配转化为栈分配。如果一个对象在子程序中被分配，要使指向该对象的指针永远不会逃逸，对象可能是栈分配的候选，而不是堆分配。
        3. 分离对象或标量替换。有的对象可能不需要作为一个连续的内存结构存在也可以被访问到，那么对象的部分（或全部）可以不存储在内存，而是存储在CPU寄存器中。
    - 是不是所有的对象和数组都会在堆内存分配空间？
        不一定,因为对象逃逸的存在，对象可能会在栈空间
    - 具体逃逸分析
        - 通过JVM参数可指定是否开启逃逸分析， ­XX:+DoEscapeAnalysis ： 表示开启逃逸分析 ­XX:­DoEscapeAnalysis ： 表示关闭逃逸分析 从jdk 1.7开始已经默认开始逃逸分析
        ```java
          1. 关闭逃逸分析，同时调大堆空间，避免堆内GC的发生，如果有GC信息将会被打印出来
              VM运行参数：­Xmx4G ­Xms4G ­XX:­DoEscapeAnalysis ­XX:+PrintGCDetails ­XX:+HeapDumpOnOutOfMemoryError
          2. 开启逃逸分析
              VM运行参数：­Xmx4G ­Xms4G ­XX:+DoEscapeAnalysis ­XX:+PrintGCDetails ­XX:+HeapDumpOnOutOfMemoryError
          3.  执行main方法后
              jps 查看进程
              jmap ­histo 进程ID
        ```