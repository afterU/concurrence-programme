package com.hzw.study.jmm;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description  volatile的禁止指令重排序，在单例的双重检测(DCL)使用
 * @createTime 2021/2/15 23:20
 */
public class DoubleCheckLock {
    /**
     * 查看汇编指令
     * -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
     */
    private static DoubleCheckLock instance;
    private DoubleCheckLock(){}
    public static DoubleCheckLock getInstance(){
        //第一次检测
        if (instance==null){
            //同步
            synchronized (DoubleCheckLock.class){
                if (instance == null){
                //多线程环境下可能会出现问题的地方
                    instance = new DoubleCheckLock();//对象创建过程，本质可以分文三步
                    //1.分配对象内存空间
                    //2.初始化对象
                    //3.设置instance指向刚分配的内存地址
                }
            }
        }
        return instance;
    }

}
