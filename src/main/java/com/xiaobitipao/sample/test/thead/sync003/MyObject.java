package com.xiaobitipao.sample.test.thead.sync003;

/**
 * <pre>
 * 对象锁的同步和异步问题:
 * </pre>
 */
public class MyObject {

    public synchronized void method1() {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void method2() {
        System.out.println(Thread.currentThread().getName());
    }

    public void method3() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {

        final MyObject mo = new MyObject();

        /**
         * <pre>
         * t1 线程先持有 MyObject 对象的锁，t2 线程可以以异步的方式调用 MyObject 对象中的非 synchronized 修饰的方法
         * t1 线程先持有 MyObject 对象的锁，t2 线程如果在这个时候调用 MyObject 对象中的同步（synchronized）方法则需等待，也就是同步
         * </pre>
         */
        Thread t1 = new Thread(() -> mo.method1(), "t1");
        Thread t2 = new Thread(() -> mo.method2(), "t2");
        Thread t3 = new Thread(() -> mo.method3(), "t3");

        t1.start();
        t2.start();
        t3.start();
    }
}
