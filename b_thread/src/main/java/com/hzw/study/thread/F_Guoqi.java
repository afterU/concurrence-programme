package com.hzw.study.thread;

import com.hzw.study.utils.SleepUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description 线程中的过期方法,suspend()暂停；resume()恢复；stop()停止；测试确实能达到暂停，恢复，停止的效果，
 * 但是不建议使用。原因：以suspend()方法为例，调用之后，线程不会释放已经占用的资源(比如锁)，而是占有资源进入睡眠状态，这样会导致死锁问题。
 * stop()方法在终结一个线程时不会保证线程的资源正常释放，通常是没有给予线程完成资源释放工作机会，因此会导致程序可能工作在不确定的状态
 *
 * @createTime 2021/2/17 19:01
 */
public class F_Guoqi {

    public static void main(String[] args) throws InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(new Runner(), "printThread");
        printThread.setDaemon(true);
        printThread.start();
        TimeUnit.SECONDS.sleep(3);
        //将printThread线程进行暂停，输出内容工作停止
        printThread.suspend();
        System.out.println("main suspend printThread at "+format.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        //将printThread线程进行恢复，输出内容继续
        printThread.resume();
        System.out.println("main resume printThread at "+format.format(new Date()));
        TimeUnit.SECONDS.sleep(3);
        //将printThread线程进行终止，输出内容停止
        printThread.stop();
        System.out.println("main stop printThread at "+format.format(new Date()));
        TimeUnit.SECONDS.sleep(3);

    }

    static class Runner implements Runnable{

        @Override
        public void run() {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                System.out.println(Thread.currentThread().getName()+" Run at "+format.format(new Date()));
                SleepUtils.second(1);
            }
        }
    }
}
