package com.xiaobitipao.sample.test.thead.sync007;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * volatile 关键字的非原子性：
 * volatile 关键字只有可见性，并不具备 synchronized 关键字的原子性（同步）
 * 一般 volatile 用于针对于多个线程可见的操作，并不能代替 synchronized 的同步功能
 * 要实现原子性，一般使用 atomic 包的系列对象支持原子性（atomic 包的系列对象只保证本身方法原子性，并不保证多次操作的原子性）
 * </pre>
 */
public class AtomicUse {

    private static AtomicInteger count = new AtomicInteger(0);

    // 单个 addAndGet 在一个方法内是原子性的，
    // 多个 addAndGet 在一个方法内是非原子性的，需要加 synchronized 进行修饰，保证 4 个 addAndGet 整体原子性
    // public int multiAdd() {
    public synchronized int multiAdd() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        count.addAndGet(1);
        count.addAndGet(2);
        count.addAndGet(3);
        count.addAndGet(4); // +10
        return count.get();
    }

    public static void main(String[] args) {

        final AtomicUse au = new AtomicUse();

        List<Thread> ts = new ArrayList<Thread>();
        for (int i = 0; i < 50; i++) {
            ts.add(new Thread(() -> System.out.println(au.multiAdd())));
        }

        for (Thread t : ts) {
            t.start();
        }
    }
}
