package com.hzw.study.jmm;


/**
 * @author huangzhiwei
 * @version 1.0
 * @description volatile的可见性问题
 * @createTime 2021/2/15 23:20
 */
public class VolatileVisibilitySample {
    //3中情况分析
    //1. initFlag没有volatile修饰，结果只有refresh方法有输出结果，load方法一直死循环等待，因为线程A修改initFlag的值对线程B不可见
    //2. initFlag有volatile修饰，结果refresh方法输出，load方法输出
    //3. initFlag没有volatile修饰，但是load方法的while循环中加synchronized锁，结果refresh方法输出，load方法输出。因为synchronized会触发重新加载主内存变量的值

    //主内存中的变量 initFlag ，测试该变量在各个线程的工作内存中的可见性
//    private boolean initFlag = false;
    private volatile boolean initFlag = false;
    static Object object = new Object();

    public void refresh(){
        this.initFlag = true;
        String threadName = Thread.currentThread().getName();
        System.out.println("线程："+threadName+":修改共享变量initFlag");
    }

    public void load(){
        String threadname = Thread.currentThread().getName();
        int i = 0;
        while (!initFlag){
//            synchronized (object){
//                i++;
//            }

        }
        System.out.println("线程："+threadname+"当前线程嗅探到initFlag的状态的改变"+i);
    }

    public static void main(String[] args){
        VolatileVisibilitySample sample = new VolatileVisibilitySample();
        Thread threadA = new Thread(()->{
            sample.refresh();
        },"threadA");

        Thread threadB = new Thread(()->{
            sample.load();
        },"threadB");

        threadB.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.start();
    }



}
