# 1. JMM
## 1.1 什么是JMM模型
Java内存模型(Java Memory Model简称JMM)是一种抽象的概念，并不真实存在，它描
述的是一组规则或规范，通过这组规范定义了程序中各个变量（包括实例字段，静态字段和构
成数组对象的元素）的访问方式。JVM运行程序的实体是线程，而每个线程创建时JVM都会为
其创建一个工作内存(有些地方称为栈空间)，用于存储线程私有的数据，而Java内存模型中规
定所有变量都存储在主内存，主内存是共享内存区域，所有线程都可以访问，但线程对变量的
操作(读取赋值等)必须在工作内存中进行，首先要将变量从主内存拷贝的自己的工作内存空
间，然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，
工作内存中存储着主内存中的变量副本拷贝，前面说过，工作内存是每个线程的私有数据区
域，因此不同的线程间无法访问对方的工作内存，线程间的通信(传值)必须通过主内存来完
成。
## 1.2 JMM不同于JVM内存区域模型
JMM与JVM内存区域的划分是不同的概念层次，更恰当说JMM描述的是一组规则，通过
这组规则控制程序中各个变量在共享数据区域和私有数据区域的访问方式，JMM是围绕原子
性，有序性、可见性展开。JMM与Java内存区域唯一相似点，都存在共享数据区域和私有数
据区域，在JMM中主内存属于共享数据区域，从某个程度上讲应该包括了堆和方法区，而工作
内存数据线程私有数据区域，从某个程度上讲则应该包括程序计数器、虚拟机栈以及本地方法
栈。
## 1.3 Java内存模型与硬件内存架构的关系
通过对前面的硬件内存架构、Java内存模型以及Java多线程的实现原理的了解，我们应该
已经意识到，多线程的执行最终都会映射到硬件处理器上进行执行，但Java内存模型和硬件内
存架构并不完全一致。对于硬件内存来说只有寄存器、缓存内存、主内存的概念，并没有工作
内存(线程私有数据区域)和主内存(堆内存)之分，也就是说Java内存模型对内存的划分对硬件内
存并没有任何影响，因为JMM只是一种抽象的概念，是一组规则，并不实际存在，不管是工作
内存的数据还是主内存的数据，对于计算机硬件来说都会存储在计算机主内存中，当然也有可
能存储到CPU缓存或者寄存器中，因此总体上来说，Java内存模型和计算机硬件内存架构是一
个相互交叉的关系，是一种抽象概念划分与真实物理硬件的交叉。(注意对于Java内存区域划分
也是同样的道理)
## 1.4 JMM存在的必要性
在明白了Java内存区域划分、硬件内存架构、Java多线程的实现原理与Java内存模型的具
体关系后，接着来谈谈Java内存模型存在的必要性。由于JVM运行程序的实体是线程，而每个
线程创建时JVM都会为其创建一个工作内存(有些地方称为栈空间)，用于存储线程私有的数
据，线程与主内存中的变量操作必须通过工作内存间接完成，主要过程是将变量从主内存拷贝
的每个线程各自的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，如
果存在两个线程同时对一个主内存中的实例对象的变量进行操作就有可能诱发线程安全问题。
假设主内存中存在一个共享变量x，现在有A和B两条线程分别对该变量x=1进行操作，
A/B线程各自的工作内存中存在共享变量副本x。假设现在A线程想要修改x的值为2，而B线程
却想要读取x的值，那么B线程读取到的值是A线程更新后的值2还是更新前的值1呢？答案是，
不确定，即B线程有可能读取到A线程更新前的值1，也有可能读取到A线程更新后的值2，这是
因为工作内存是每个线程私有的数据区域，而线程A变量x时，首先是将变量从主内存拷贝到A
线程的工作内存中，然后对变量进行操作，操作完成后再将变量x写回主内，而对于B线程的也
是类似的，这样就有可能造成主内存与工作内存间数据存在一致性问题，假如A线程修改完后
正在将数据写回主内存，而B线程此时正在读取主内存，即将x=1拷贝到自己的工作内存中，
这样B线程读取到的值就是x=1，但如果A线程已将x=2写回主内存后，B线程才开始读取的
话，那么此时B线程读取到的就是x=2，但到底是哪种情况先发生呢？
## 1.5 JMM-同步八种操作介绍
- （1）lock(锁定)：作用于主内存的变量，把一个变量标记为一条线程独占状态
- （2）unlock(解锁)：作用于主内存的变量，把一个处于锁定状态的变量释放出来，释放后的
变量才可以被其他线程锁定
- （3）read(读取)：作用于主内存的变量，把一个变量值从主内存传输到线程的工作内存中，
以便随后的load动作使用
- （4）load(载入)：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作
内存的变量副本中
- （5）use(使用)：作用于工作内存的变量，把工作内存中的一个变量值传递给执行引擎
- （6）assign(赋值)：作用于工作内存的变量，它把一个从执行引擎接收到的值赋给工作内存
的变量
- （7）store(存储)：作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，
以便随后的write的操作
- （8）write(写入)：作用于工作内存的变量，它把store操作从工作内存中的一个变量的值传送
到主内存的变量中

如果要把一个变量从主内存中复制到工作内存中，就需要按顺序地执行read和load操作，
如果把变量从工作内存中同步到主内存中，就需要按顺序地执行store和write操作。但Java内
存模型只要求上述操作必须按顺序执行，而没有保证必须是连续执行。
### 1.5.1 同步规则分析
  - 1）不允许一个线程无原因地（没有发生过任何assign操作）把数据从工作内存同步回主内存
  中
  - 2）一个新的变量只能在主内存中诞生，不允许在工作内存中直接使用一个未被初始化（load
  或者assign）的变量。即就是对一个变量实施use和store操作之前，必须先自行assign和load
  操作。
  - 3）一个变量在同一时刻只允许一条线程对其进行lock操作，但lock操作可以被同一线程重复
  执行多次，多次执行lock后，只有执行相同次数的unlock操作，变量才会被解锁。lock和
  unlock必须成对出现。
  - 4）如果对一个变量执行lock操作，将会清空工作内存中此变量的值，在执行引擎使用这个变
  量之前需要重新执行load或assign操作初始化变量的值。
  - 5）如果一个变量事先没有被lock操作锁定，则不允许对它执行unlock操作；也不允许去
  unlock一个被其他线程锁定的变量。
  - 6）对一个变量执行unlock操作之前，必须先把此变量同步到主内存中（执行store和write操
  作）
## 1.6 并发编程的可见性，原子性与有序性问题
### 1.6.1 原子性
- 原子性指的是一个操作是不可中断的，即使是在多线程环境下，一个操作一旦开始就不会
被其他线程影响。
- 在java中，对基本数据类型的变量的读取和赋值操作是原子性操作有点要注意的是，对于
32位系统的来说，long类型数据和double类型数据(对于基本数据类型，
byte,short,int,float,boolean,char读写是原子操作)，它们的读写并非原子性的，也就是说如
果存在两条线程同时对long类型或者double类型的数据进行读写是存在相互干扰的，因为对
于32位虚拟机来说，每次原子读写是32位的，而long和double则是64位的存储单元，这样会
导致一个线程在写时，操作完前32位的原子操作后，轮到B线程读取时，恰好只读取到了后32
位的数据，这样可能会读取到一个既非原值又不是线程修改值的变量，它可能是“半个变
量”的数值，即64位数据被两个线程分成了两次读取。但也不必太担心，因为读取到“半个变
量”的情况比较少见，至少在目前的商用的虚拟机中，几乎都把64位的数据的读写操作作为原
子操作来执行，因此对于这个问题不必太在意，知道这么回事即可。
    ```$xslt
     X=10; //原子性（简单的读取、将数字赋值给变量）
     Y = x; //变量之间的相互赋值，不是原子操作
     X++; //对变量进行计算操作
     X = x+1;
    ```
### 1.6.1 可见性
- 理解了指令重排现象后，可见性容易了，可见性指的是当一个线程修改了某个共享变量的
值，其他线程是否能够马上得知这个修改的值。对于串行程序来说，可见性是不存在的，因为
我们在任何一个操作中修改了某个变量的值，后续的操作中都能读取这个变量值，并且是修改
过的新值。
- 但在多线程环境中可就不一定了，前面我们分析过，由于线程对共享变量的操作都是线程
拷贝到各自的工作内存进行操作后才写回到主内存中的，这就可能存在一个线程A修改了共享
变量x的值，还未写回主内存时，另外一个线程B又对主内存中同一个共享变量x进行操作，但
此时A线程工作内存中共享变量x对线程B来说并不可见，这种工作内存与主内存同步延迟现象
就造成了可见性问题，另外指令重排以及编译器优化也可能导致可见性问题，通过前面的分
析，我们知道无论是编译器优化还是处理器优化的重排现象，在多线程环境下，确实会导致程
序轮序执行的问题，从而也就导致可见性问题。   
### 1.6.2 有序性
有序性是指对于单线程的执行代码，我们总是认为代码的执行是按顺序依次执行的，这样
的理解并没有毛病，毕竟对于单线程而言确实如此，但对于多线程环境，则可能出现乱序现
象，因为程序编译成机器码指令后可能会出现指令重排现象，重排后的指令与原指令的顺序未
必一致，要明白的是，在Java程序中，倘若在本线程内，所有操作都视为有序行为，如果是多
线程环境下，一个线程中观察另外一个线程，所有操作都是无序的，前半句指的是单线程内保
证串行语义执行的一致性，后半句则指指令重排现象和工作内存与主内存同步延迟现象。
## 1.7 JMM如何解决原子性&可见性&有序性问题
### 1.7.1 原子性问题
除了JVM自身提供的对基本数据类型读写操作的原子性外，可以通过 synchronized和
Lock实现原子性。因为synchronized和Lock能够保证任一时刻只有一个线程访问该代码块。
### 1.7.2 可见性问题
volatile关键字保证可见性。当一个共享变量被volatile修饰时，它会保证修改的值立即被
其他的线程看到，即修改的值立即更新到主存中，当其他线程需要读取时，它会去内存中读取
新值。synchronized和Lock也可以保证可见性，因为它们可以保证任一时刻只有一个线程能
访问共享资源，并在其释放锁之前将修改的变量刷新到内存中。
### 1.7.3 有序性问题
在Java里面，可以通过volatile关键字来保证一定的“有序性”（具体原理在下一节讲述
volatile关键字）。另外可以通过synchronized和Lock来保证有序性，很显然，synchronized
和Lock保证每个时刻是有一个线程执行同步代码，相当于是让线程顺序执行同步代码，自然就
保证了有序性。

## 1.8 as-if-serial语义
- as-if-serial语义的意思是：不管怎么重排序（编译器和处理器为了提高并行度），（单线
程）程序的执行结果不能被改变。编译器、runtime和处理器都必须遵守as-if-serial语义。
- 为了遵守as-if-serial语义，编译器和处理器不会对存在数据依赖关系的操作做重排序，因
为这种重排序会改变执行结果。但是，如果操作之间不存在数据依赖关系，这些操作就可能被
编译器和处理器重排序。
## 1.9 happens-before 原则
happens-before 原则来辅助保证程序执行的原子性、可见性以及有序性的问题，它是判断数
据是否存在竞争、线程是否安全的依据
程序顺序原则，即在一个线程内必须保证语义串行性，也就是说按照代码顺序执行。
2. 锁规则 解锁(unlock)操作必然发生在后续的同一个锁的加锁(lock)之前，也就是说，
如果对于一个锁解锁后，再加锁，那么加锁的动作必须在解锁动作之后(同一个锁)。
3. volatile规则 volatile变量的写，先发生于读，这保证了volatile变量的可见性，简单
的理解就是，volatile变量在每次被线程访问时，都强迫从主内存中读该变量的值，而当
该变量发生变化时，又会强迫将最新的值刷新到主内存，任何时刻，不同的线程总是能
够看到该变量的最新值。
4. 线程启动规则 线程的start()方法先于它的每一个动作，即如果线程A在执行线程B的
start方法之前修改了共享变量的值，那么当线程B执行start方法时，线程A对共享变量
的修改对线程B可见
5. 传递性 A先于B ，B先于C 那么A必然先于C
6. 线程终止规则 线程的所有操作先于线程的终结，Thread.join()方法的作用是等待当前
执行的线程终止。假设在线程B终止之前，修改了共享变量，线程A从线程B的join方法
成功返回后，线程B对共享变量的修改将对线程A可见。
7. 线程中断规则 对线程 interrupt()方法的调用先行发生于被中断线程的代码检测到中
断事件的发生，可以通过Thread.interrupted()方法检测线程是否中断。
8. 对象终结规则 对象的构造函数执行，结束先于finalize()方法
## 1.10 volatile
### 1.10.1 volatile 内存语义
volatile是Java虚拟机提供的轻量级的同步机制。volatile关键字有如下两个作用
1. 保证被volatile修饰的共享变量对所有线程总数可见的，也就是当一个线程修改了一
个被volatile修饰共享变量的值，新值总是可以被其他线程立即得知。
2. 禁止指令重排序优化
### 1.10.2 volatile 可见性
关于volatile的可见性作用，我们必须意识到被volatile修饰的变量对所有线程总数立即可
见的，对volatile变量的所有写操作总是能立刻反应到其他线程中
- 示例
```java
public class VolatileVisibilitySample {
        volatile boolean initFlag = false;
        public void save(){
            this.initFlag = true;
            String threadname = Thread.currentThread().getName();
            System.out.println("线程："+threadname+":修改共享变量initFlag");
        }
        public void load(){
            String threadname = Thread.currentThread().getName();
            while (!initFlag){
                //线程在此处空跑，等待initFlag状态改变
            }
            System.out.println("线程："+threadname+"当前线程嗅探到initFlag的状态的改变");
        }
        public static void main(String[] args){
            VolatileVisibilitySample sample = new VolatileVisibilitySample();
            Thread threadA = new Thread(()->{
                sample.save();
            },"threadA");
            Thread threadB = new Thread(()->{
                sample.load();
            },"threadB");
            threadB.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadA.start();
        }
    }
```
- 线程A改变initFlag属性之后，线程B马上感知到
### 1.10.3 volatile 无法保证原子性
- 示例
```java
public class VolatileAtomicSample {

    private static volatile int counter = 0;
    public static  void increase(){
        counter++; //不是一个原子操作,第一轮循环结果是没有刷入主存，这一轮循环已经无效
        //1 load counter 到工作内存
        //2 add counter 执行自加
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    increase();
                }
            });
            thread.start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(counter);
    }

}

```
- 在并发场景下，counter变量的任何改变都会立马反应到其他线程中，但是如此存在多条线程同时
  调用increase()方法的话，就会出现线程安全问题，毕竟counter++;操作并不具备原子性，该操作是
  先读取值，然后写回一个新值，相当于原来的值加上1，分两步完成，如果第二个线程在第一
  个线程读取旧值和写回新值期间读取i的域值，那么第二个线程就会与第一个线程一起看到同一
  个值，并执行相同值的加1操作，这也就造成了线程安全失败，因此对于increase方法必须使
  用synchronized修饰，以便保证线程安全，需要注意的是一旦使用synchronized修饰方法
  后，由于synchronized本身也具备与volatile相同的特性，即可见性，因此在这样种情况下就
  完全可以省去volatile修饰变量。
### 1.10.4 volatile 禁止重排优化
- volatile关键字另一个作用就是禁止指令重排优化，从而避免多线程环境下程序出现乱序
  执行的现象,volatile变量正是通过内存屏障实现其在内存中的语义，即可见性和禁止重排优化
- 示例
```java
public class DoubleCheckLock {
    /**
     * 查看汇编指令
     * -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
     */
    private static DoubleCheckLock instance;
    private DoubleCheckLock(){}
    public static DoubleCheckLock getInstance(){
        //第一次检测
        if (instance==null){
            //同步
            synchronized (DoubleCheckLock.class){
                if (instance == null){
                //多线程环境下可能会出现问题的地方
                    instance = new DoubleCheckLock();//对象创建过程，本质可以分文三步
                    //1.分配对象内存空间
                    //2.初始化对象
                    //3.设置instance指向刚分配的内存地址
                }
            }
        }
        return instance;
    }

}

```
- 上述代码一个经典的单例的双重检测的代码，这段代码在单线程环境下并没有什么问题，
但如果在多线程环境下就可以出现线程安全问题。
    1. 原因在于某一个线程执行到第一次检测，读取到的instance不为null时，instance的引用对象可能没有完成初始化。因为instance = new DoubleCheckLock();可以分为以下3步完成(伪代码)
       ```java
        memory = allocate();//1.分配对象内存空间
        instance(memory);//2.初始化对象
        instance = memory;//3.设置instance指向刚分配的内存地址，此时
        instance！=null
        ```
    2. 由于步骤1和步骤2间可能会重排序，如下：
        ```java
        memory=allocate();//1.分配对象内存空间
        instance=memory;//3.设置instance指向刚分配的内存地址，此时instance！
        =null，但是对象还没有初始化完成！
        instance(memory);//2.初始化对象
        ```
    3. 由于步骤2和步骤3不存在数据依赖关系，而且无论重排前还是重排后程序的执行结果在单线程中并没有改变，因此这种重排优化是允许的。但是指令重排只会保证串行语义的执行的一致性(单线程)，但并不会关心多线程间的语义一致性。所以当一条线程访问instance不为null时，由于instance实例未必已初始化完成，也就造成了线程安全问题。
    4. 那么该如何解决呢，很简单，我们使用volatile禁止instance变量被执行指令重排优化即可。
        ```java
        //禁止指令重排优化
        private volatile static DoubleCheckLock instance;
        ```
#### 1.10.4.1 内存屏障(Memory Barriers/Memory Fence)
内存屏障，又称内存栅栏，是一个CPU指令，它的作用有两个，一是保证特定操作的执行
顺序，二是保证某些变量的内存可见性（利用该特性实现volatile的内存可见性）。由于编译
器和处理器都能执行指令重排优化。如果在指令间插入一条Memory Barrier则会告诉编译器
和CPU，不管什么指令都不能和这条Memory Barrier指令重排序，也就是说通过插入内存屏
障禁止在内存屏障前后的指令执行重排序优化。Memory Barrier的另外一个作用是强制刷出
各种CPU的缓存数据，因此任何CPU上的线程都能读取到这些数据的最新版本。

屏障类型 | 指令示例 | 说明
---|---|---
LoadLoad |Load1；LoadLoad；Load2| 确保Load1数据的装载先于Load2及所有后续装载指令的装载
StoreStore | Store1;StoreStore;Store2| 确保Store1数据对其他处理器可见(刷新到内存)先于Store2及所有后续存储指令的存储
LoadStore | Load1;StoreStore;Store2| 确保Load1数据装载先于Store2及所有后续存储指令刷新到内存
StoreLoad | Store1;StoreStore;Load2| 确保Store1数据对其他处理器变得可见(指刷新到内存)先于Load2及所有后续装载指令的装载。<br/>StoreLoad会使该屏障之前的所有内存访问指令(存储和装载指令)完成之后，才执行该屏障之后的内存访问指令
- StoreLoad全能型屏障，具有其他3种屏障的效果，该屏障开销大，会把写缓冲区中的全部数据刷新到内存中
### 1.10.5 volatile 内存语义的实现
- JMM针对编译器制定的volatile重排序规则
![](https://i.loli.net/2021/02/15/wDKXRPGpUqVjcuB.png)
    - 举例来说，第三行最后一个单元格的意思是：在程序中，当第一个操作为普通变量的读或写时，如果第二个操作为volatile写，则编译器不能重排序这两个操作。
    从上图可以看出：
        - 当第二个操作是volatile写时，不管第一个操作是什么，都不能重排序。这个规则确保volatile写之前的操作不会被编译器重排序到volatile写之后。
        - 当第一个操作是volatile读时，不管第二个操作是什么，都不能重排序。这个规则确保volatile读之后的操作不会被编译器重排序到volatile读之前。
        - 当第一个操作是volatile写，第二个操作是volatile读时，不能重排序。
- 为了实现volatile的内存语义，编译器在生成字节码时，会在指令序列中插入内存屏障来
禁止特定类型的处理器重排序。对于编译器来说，发现一个最优布置来最小化插入屏障的总数
几乎不可能。为此，JMM采取保守策略。下面是基于保守策略的JMM内存屏障插入策略。
    - 在每个volatile写操作的前面插入一个StoreStore屏障。
    - 在每个volatile写操作的后面插入一个StoreLoad屏障。
    - 在每个volatile读操作的后面插入一个LoadLoad屏障。
    - 在每个volatile读操作的后面插入一个LoadStore屏障。
    
上述内存屏障插入策略非常保守，但它可以保证在任意处理器平台，任意的程序中都能得
到正确的volatile内存语义
- volatile修饰的变量会在汇编代码中添加Lock前缀指令，使处理器做两件事
    - 将当前处理器缓存行的数据写回到系统内存
    - 这个处理器缓存写回内存的操作会导致其他处理器缓存无效
### 1.10.6 volatile 大量使用会导致总线风暴
- 原因：大量volatile会使mesi缓存一致性协议需要不断的嗅探
    主内存并且cas不断循环无效交互导致总线带宽达到峰值
- 将部分volatile使用synchronized代替，降低程序的并发度
# 2. 并发编程
## 2.1 并发&并行
- 并发不等同于并行：并发指多个任务交替进行，而并行是指真正意义上的“同时进行”。实际上，如果系统内只有一个CPU，而使用多线程时，那么真实系统环境下不能并行，只能通过切换时间片的方式交替进行，而成为并发执行任务。真正并行只能出现在拥有多个CPU的系统中
### 2.1.1 并发优点
1. 充分利用多核CPU的计算能力
2. 方便进行业务拆分，提升应用性能
### 2.1.2 并发问题
1.  高并发场景下，导致频繁的上下文切换
2. 临界区线程安全问题，容易出现死锁造成服务不可用
## 2.2 上下文切换
CPU通过时间片分配算法来循环执行任务，当前任务执行一个时间片后会切换到下一个任务。但是，在切换前会保存上一个任务的状态，以便下次切换回这个任务时，可以再次加载这个任务的状态，从任务保存到再加载的过程就是一次上下文切换。
### 2.2.1 上下文切换的性能开销
- 直接消耗：指的是CPU寄存器需要保存和加载, 系统调度器的代码需要执行, TLB实例需要重新加载, CPU 的pipeline需要刷掉
- 间接消耗：指的是多核的cache之间得共享数据, 间接消耗对于程序的影响要看线程工作区操作数据的大小
### 2.2.2 测试上下文切换次数和时长
- 使用Lmbench3测量上下文切换的时长
- 使用vmstat测量上下文切换次数
![](https://i.loli.net/2021/02/16/J7RFwTvBnUZ2CaW.png)
CS(Content Switch)表示上下文切换次数
### 2.2.3 减少上下文切换的次数
- 无锁并发编程，多线程竞争锁时，会引起上下文切换，所以多线程处理数据时，可以用一些办法来避免使用锁，如将数据的ID按照Hash算法取摸分段，不同的线程处理不同段的数据
- CAS算法，Java的Atomic包使用CAS算法来更新数据，而不需要加锁
- 使用最少的线程，避免创建不需要的线程，比如任务少，但是创建了很多线程来处理，这样会造成大量线程都处于等待状态
- 协程，在单线程中实现多任务的调度，并在单线程中维持多个任务间的切换

使用最少线程的实战
    1. jstack命令dump线程信息
    2. 统计所有线程分别处于什么状态
    3. 打开dump文件查看处于等待状态的线程，减少该等待状态的线程的数量