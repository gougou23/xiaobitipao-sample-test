package com.xiaobitipao.sample.test.thead.sync002;

/**
 * <pre>
 * 多个线程多个锁：
 * 每个线程都可以拿到自己指定的锁，分别获得锁之后，执行 synchronized 方法体的内容。
 * </pre>
 */
public class MultiThread {

    // private static int num = 0;
    private int num = 0;

    // 测试使用/不使用 static 时输出结果的差异
    // public static synchronized void printNum(String tag) {
    public synchronized void printNum(String tag) {
        try {
            if (tag.equals("a")) {
                num = 100;
                System.out.println("tag a, set num over!");
                Thread.sleep(1000);
            } else {
                num = 200;
                System.out.println("tag b, set num over!");
            }
            System.out.println("tag " + tag + ", num = " + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 关键字 synchronized 取得的锁都是对象锁，而不是把一段代码（方法）当做锁，
     * 所以代码中哪个线程先执行 synchronized 关键字的方法，哪个线程就持有该方法所属对象的锁（Lock），他们互不影响
     * 
     * 在静态方法上加 synchronized 关键字，表示锁定.class类，类一级别的锁（独占.class类）。
     * </pre>
     */
    public static void main(String[] args) {

        // 这里定义了两个不同的对象
        final MultiThread m1 = new MultiThread();
        final MultiThread m2 = new MultiThread();

        // t1 线程获得 m1 对象的锁
        // t2 线程获得 m2 对象的锁
        // 因为两个线程使用不同的对象，所以他们获取的对象锁互不影响
        // 如果 printNum 方法用 synchronized 修饰了但不是 static 的话，两个线程是可以同时执行 printNum
        // 方法的
        // 如果 printNum 方法用 synchronized 修饰同时又是 static 的话，由于是类级别锁，所以两个线程步是不可以同时执行
        // printNum 方法的
        Thread t1 = new Thread(() -> m1.printNum("a"));
        Thread t2 = new Thread(() -> m2.printNum("b"));

        t1.start();
        t2.start();
    }
}
