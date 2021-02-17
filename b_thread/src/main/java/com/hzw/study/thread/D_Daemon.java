package com.hzw.study.thread;

import com.hzw.study.utils.SleepUtils;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description Daemon线程（守护线程）,结论：Daemon线程，不能依赖finally块中的内容来确保执行关闭或清理资源的逻辑
 * @createTime 2021/2/17 16:56
 */
public class D_Daemon {
    public static void main(String[] args) {
        Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
        thread.setDaemon(true);
        thread.start();
    }

    static class DaemonRunner implements Runnable{

        @Override
        public void run() {
            try {
                SleepUtils.second(100);
            }finally {
                System.out.println("DaemonThread finally run. ");
            }

        }
    }
}
