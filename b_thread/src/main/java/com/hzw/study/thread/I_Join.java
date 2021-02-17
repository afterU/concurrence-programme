package com.hzw.study.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description Thread.join()方法，当前线程A等待thread线程终止之后才从thread.join()返回，join()是synchronized加锁的方法，内部调用wait()，因此同时具有同步和等待的效果
 * @createTime 2021/2/17 20:44
 */
public class I_Join {
    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            //每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
            Thread thread = new Thread(new Domino(previous), String.valueOf(i));
            thread.start();
            previous = thread;

        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName()+" terminate.");
    }

    static class Domino implements Runnable{

        private Thread thread;
        public Domino(Thread thread){
            this.thread = thread;
        }
        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" terminate.");
        }
    }
}
