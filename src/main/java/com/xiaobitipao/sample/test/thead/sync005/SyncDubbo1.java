package com.xiaobitipao.sample.test.thead.sync005;

/**
 * <pre>
 * synchronized 的锁重入:
 * 关键字 synchronized 拥有锁重入的功能
 * 也就是在使用 synchronized 时，当一个线程得到了一个对象的锁以后，再次请求此对象时是可以再次得到该对象的锁的
 * </pre>
 */
public class SyncDubbo1 {

    public synchronized void method1() {
        System.out.println("method1 ...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        method2();
    }

    public synchronized void method2() {
        System.out.println("method2 ...");
        method3();
    }

    public synchronized void method3() {
        System.out.println("method3 ...");
    }

    public static void main(String[] args) {
        final SyncDubbo1 sd = new SyncDubbo1();
        Thread t1 = new Thread(() -> sd.method1());
        // JRE(JVM) 判断程序是否执行结束的标准是所有的前台线程执行完毕了，而不管后台线程的状态
        // 创建一个线程，默认是前台线程，即程序会等待线程结束才退出
        // 如果设置 setDaemon(true) 即把该线程设置为后台线程（守护线程），如果程序主线程结束，那么不管后台线程的状态如何会结束程序
        // 在使用后台线程时候一定要注意这个问题
        // t1.setDaemon(true);
        t1.start();
        System.out.println("44444444444444444");
    }
}
