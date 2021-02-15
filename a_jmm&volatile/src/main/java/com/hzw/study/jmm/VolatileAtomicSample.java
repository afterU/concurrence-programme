package com.hzw.study.jmm;


/**
 * @author huangzhiwei
 * @version 1.0
 * @description  volatile 无法保证原子性
 * @createTime 2021/2/15 23:20
 */
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
