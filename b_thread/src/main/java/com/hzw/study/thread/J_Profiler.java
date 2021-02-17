package com.hzw.study.thread;

import sun.java2d.cmm.Profile;

import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description ThreadLocal 线程变量，是一个以ThreadLocal对象为键，任意对象为值的存储结构，
 * 这个结构被附带在线程上(具体是ThreadLocal的内部类ThreadLocalMap对象),就是说一个线程可以根据一个ThreadLocal对象查询到绑定在这个线程上的值
 * @createTime 2021/2/17 20:54
 */
public class J_Profiler {
    private static final ThreadLocal<Long> time_threadlocal = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return System.currentTimeMillis();
        }
    };

    public static final void begin(){
        time_threadlocal.set(System.currentTimeMillis());
    }
    public static final long end(){
        return time_threadlocal.get();
    }

    public static void main(String[] args) throws InterruptedException {
        J_Profiler.begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost: "+ J_Profiler.end()+" mills");
    }
}
