package com.hzw.study.dbpool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description 数据库连接池的简单实现
 * @createTime 2021/2/17 22:00
 */
public class ConnectionPool {

    //一个双向队列存储连接
    private LinkedList<Connection> pool = new LinkedList<>();

    public ConnectionPool(int initialSize){
        if (initialSize>0){
            for (int i = 0; i < initialSize; i++) {
                 pool.add(ConnectionDriver.createConnection());
            }
        }
    }
    //将连接放回到连接池
    public void releaseConnection(Connection connection){
        if (connection != null){
            synchronized (pool){
                //连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个链接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }
    //在mills时间内无法获取到连接，将会返回null
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            if (mills <= 0){
                while (pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else{
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (pool.isEmpty()&&remaining>0){
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()){
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
