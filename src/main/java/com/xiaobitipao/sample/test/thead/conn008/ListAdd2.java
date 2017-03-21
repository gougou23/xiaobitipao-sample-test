package com.xiaobitipao.sample.test.thead.conn008;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 使用 wait/notify:
 * 区别：wait 释放锁，notfiy 不释放锁
 * 问题：即使发出了通知，直到第一个线程执行结束，第二个线程还是无法响应。
 * </pre>
 */
public class ListAdd2 {

    private volatile static List<String> list = new ArrayList<>();

    public void add() {
        list.add("xiaobitipao");
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {

        final ListAdd2 list2 = new ListAdd2();

        // 实例化出来一个 lock
        // 当使用 wait 和 notify 的时候 ， 一定要配合着 synchronized 关键字去使用
        final Object lock = new Object();

        Thread t1 = new Thread(() -> {
            try {
                synchronized (lock) {
                    for (int i = 0; i < 10; i++) {
                        list2.add();
                        System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素 ...");
                        Thread.sleep(500);
                        if (list2.size() == 5) {
                            System.out.println("已经发出通知 ...");
                            lock.notify();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                if (list2.size() != 5) {
                    try {
                        System.out.println("t2进入 ...");
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("当前线程：" + Thread.currentThread().getName() + "收到通知线程停止 ...");
                throw new RuntimeException();
            }
        }, "t2");

        t2.start();
        t1.start();
    }
}
