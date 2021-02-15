package com.hzw.study.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;


/**
 * @author huangzhiwei
 * @version 1.0
 * @description  反射获取Unsafe的实例
 * @createTime 2021/2/15 23:20
 */
public class UnsafeInstance {

    public static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
