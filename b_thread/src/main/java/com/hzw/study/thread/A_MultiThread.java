package com.hzw.study.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description  java程序天生就是多线程程序 JMX查看java程序有哪些线程
 * @createTime 2021/2/17 11:51
 */
public class A_MultiThread {

    public static void main(String[] args) {
        //获取java线程管理ThreadMXBean
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        //不需要获取同步的monitor和synchronized信息，仅获取线程和线程堆栈信息
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            System.out.println("["+threadInfo.getThreadId()+"] "+threadInfo.getThreadName());

        }
        //[6] Monitor Ctrl-Break
        //[5] Attach Listener
        //[4] Signal Dispatcher  //分发处理发送给JVM信号的线程
        //[3] Finalizer          //调用对象finalize方法的线程
        //[2] Reference Handler  // 清除reference的线程
        //[1] main               // main线程，用户程序入口



    }
}
