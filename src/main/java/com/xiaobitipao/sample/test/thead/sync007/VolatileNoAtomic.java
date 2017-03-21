package com.xiaobitipao.sample.test.thead.sync007;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * volatile 关键字的非原子性：
 * volatile 关键字只有可见性，并不具备 synchronized 关键字的原子性（同步）
 * 一般 volatile 用于针对于多个线程可见的操作，并不能代替 synchronized 的同步功能
 * 要实现原子性，一般使用 atomic 包以支持原子性（atomic 包下的类只保证本身方法原子性，并不保证多次操作的原子性）
 * </pre>
 */
public class VolatileNoAtomic extends Thread {

    // private static volatile int count;
    private static AtomicInteger count = new AtomicInteger(0);

    private static void addCount() {
        for (int i = 0; i < 1000; i++) {
            // count++ ;
            count.incrementAndGet();
        }
        System.out.println(count);
    }

    public void run() {
        addCount();
    }

    public static void main(String[] args) {

        VolatileNoAtomic[] arr = new VolatileNoAtomic[100];
        for (int i = 0; i < 10; i++) {
            arr[i] = new VolatileNoAtomic();
        }

        for (int i = 0; i < 10; i++) {
            arr[i].start();
        }
    }
}
