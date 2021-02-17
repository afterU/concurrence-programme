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
     * https://zhuanlan.zhihu.com/p/158168592?from_voters_page=true
     * -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
     *
     * -Xcomp : 让JVM以编译模式执行代码，即JVM会在第一次运行时即将所有字节码编译为本地代码
     * -XX:+UnlockDiagnosticVMOptions : 解锁诊断功能
     * -XX:+PrintAssembly : 输出反汇编后的汇编指令
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

    public static void main(String[] args) {
        System.out.println(DoubleCheckLock.getInstance());
    }

}
