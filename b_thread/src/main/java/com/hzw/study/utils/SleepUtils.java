package com.hzw.study.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author huangzhiwei
 * @version 1.0
 * @description
 * @createTime 2021/2/17 16:58
 */
public class SleepUtils {
    public static final void second(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {


        }
    }
}
