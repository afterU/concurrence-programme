package com.hzw.study.thread;

import com.hzw.study.utils.SleepUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description 线程的状态
 * @createTime 2021/2/17 16:16
 */
public class C_ThreadState {
    /**
     * 1. jps 查到该程序进程ID
     * 2. jstack 进程ID
     * 3. dump的数据
     *
     //BlockedThread-2 阻塞状态，等待Blocked.class 锁
     "BlockedThread-2" #14 prio=5 os_prio=0 tid=0x0000009c06869800 nid=0x1204 waiting for monitor entry [0x0000009c071df000]
     java.lang.Thread.State: BLOCKED (on object monitor)

     //BlockedThread-1 获取到了Blocked.class锁
     "BlockedThread-1" #13 prio=5 os_prio=0 tid=0x0000009c06869000 nid=0xab0 waiting on condition [0x0000009c070de000]
     java.lang.Thread.State: TIMED_WAITING (sleeping)

     //WaitingThread 等待状态
     "WaitingThread" #12 prio=5 os_prio=0 tid=0x0000009c06867800 nid=0x3118 in Object.wait() [0x0000009c06fdf000]
     java.lang.Thread.State: WAITING (on object monitor)

     // TimeWaitingThread 超时等待状态
     "TimeWaitingThread" #11 prio=5 os_prio=0 tid=0x0000009c06862800 nid=0x3b14 waiting on condition [0x0000009c06ede000]
     java.lang.Thread.State: TIMED_WAITING (sleeping)
     */

    public static void main(String[] args) {
        //创建4个线程，1个不断 睡眠；1个线程一直等待；2个Blocked线程，1个加锁成功，1个加锁失败
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();

        new Thread(new Waiting(),"WaitingThread").start();

        new Thread(new Blocked(),"BlockedThread-1").start();

        new Thread(new Blocked(),"BlockedThread-2").start();
    }

    //该线程不断的进行睡眠
    static class TimeWaiting implements Runnable{

        @Override
        public void run() {
            while (true){
                SleepUtils.second(100);
            }
        }
    }
    // 该线程在Waiting.class 实例上等待
    static class Waiting implements  Runnable{

        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class){
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    // 该线程在Block.class实例上加锁，不会释放该锁
    static class Blocked implements Runnable{

        @Override
        public void run() {
            synchronized (Blocked.class){


                while (true) {
                    SleepUtils.second(100);
                }
            }
        }
    }

}
