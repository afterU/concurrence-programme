package com.hzw.study.thread;

import com.hzw.study.utils.SleepUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description 线程的等待/通知机制
 * @createTime 2021/2/17 19:34
 */
public class H_WaitNotify {
    static  boolean flag = true;
    static Object lock = new Object();

    /**
     * 运行结果
     * Thread[waitThread,5,main] flag is true. wait@ 19:52:36
     * Thread[notifyThread,5,main] hold lock. notify @ 19:52:37
     * Thread[notifyThread,5,main] hold lock again. sleep @ 19:52:42
     * Thread[waitThread,5,main] flag is false. running@ 19:52:47
     */
    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(), "waitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(), "notifyThread");
        notifyThread.start();
    }
    static class Wait implements Runnable{

        @Override
        public void run() {
            //加锁，拥有lock的monitor
            synchronized (lock){
                //当条件不满足时，继续wait，同时释放了lock锁
                while (flag) {
                    try {
                        System.out.println(Thread.currentThread()+" flag is true. wait@ "+ new SimpleDateFormat("HH:mm:ss").format(new Date()));
                        lock.wait();
                    }catch (Exception e){

                    }
                }
                //条件满足时，完成工作
                System.out.println(Thread.currentThread()+" flag is false. running@ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }
    static class Notify implements Runnable{

        @Override
        public void run() {
            //加锁，拥有lock的Monitor
            synchronized (lock){
                //获取locak锁，然后进行通知，通知时不会释放lock锁
                //直到当前线程释放了lock后，WaitThread才能从wait方法中返回
                System.out.println(Thread.currentThread()+" hold lock. notify @ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));

                lock.notifyAll();
                flag = false;
                SleepUtils.second(5);

            }
            //再次加锁
            synchronized (lock){

                System.out.println(Thread.currentThread()+" hold lock again. sleep @ "+new SimpleDateFormat("HH:mm:ss").format(new Date()));

                SleepUtils.second(5);

            }
        }
    }
}
