package com.hzw.study.thread;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description 安全的终止线程,这种使用表示位或中断操作的方式能够使线程在终止时有机会去清理资源，而不是武断的将线程停止，因此这种终止线程的做法更安全
 * @createTime 2021/2/17 19:22
 */
public class G_Shutdown {
    public static void main(String[] args) throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountThread");
        countThread.start();
        //睡眠1秒，main线程对countThread进行中断，是countThread能够感知中断而结束
        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();

        Runner two = new Runner();
        countThread = new Thread(two, "CountThread");
        countThread.start();
        //睡眠1秒，main线程对Runner two进行取消，使countThread能够感知on为false而结束
        TimeUnit.SECONDS.sleep(1);
        two.cancel();


    }

    private static class Runner implements Runnable{
        private long i;
        private volatile boolean on = true;
        @Override
        public void run() {
            while (on&& !Thread.currentThread().isInterrupted()){
                i++;
            }
            System.out.println("Count i = "+i);
        }

        public void cancel(){
            on = false;
        }

    }
}
