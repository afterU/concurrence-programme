package com.hzw.study.thread;

import com.hzw.study.utils.SleepUtils;
import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;

import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description 线程中断,线程的中断可以理解为线程的一个标识位属性，抛出InterruptedException异常之前会先将线程的标识位清除，这时isInterrupted()返回false
 * @createTime 2021/2/17 17:04
 */
public class E_Interrupted {
    public static void main(String[] args)  {
        //sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "sleepThread");
        sleepThread.setDaemon(true);
        //busyThread不停的运行
        Thread busyThread = new Thread(new BusyRunner(), "busyThread");
        busyThread.setDaemon(true);

        sleepThread.start();
        busyThread.start();

        //休眠5s，让sleepThread和busyThread充分运行

        try {
            TimeUnit.SECONDS.sleep(5);
            sleepThread.interrupt();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            busyThread.interrupt();
        }catch (Exception e){
            e.printStackTrace();
        }



        System.out.println("SleepThread interrupted is "+sleepThread.isInterrupted());
        System.out.println("busyThread interrupted is "+busyThread.isInterrupted());

        //防止sleepThread和busyThread立刻推出
        SleepUtils.second(10);
    }
    static class SleepRunner implements Runnable{

        @Override
        public void run() {
            while (true) {
                SleepUtils.second(10);
            }
        }
    }
    static class BusyRunner implements Runnable{

        @Override
        public void run() {
            while (true){

            }
        }
    }
}
