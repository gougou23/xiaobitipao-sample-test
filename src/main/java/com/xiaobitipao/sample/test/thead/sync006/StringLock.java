package com.xiaobitipao.sample.test.thead.sync006;

/**
 * <pre>
 * 特别注意 String 常量池的缓存功能问题，就是不要使用 String 常量加锁，会出现死循环的问题
 * </pre>
 */
public class StringLock {

    public void method() {
        // synchronized (new String("字符串常量")) {
        synchronized ("字符串常量") {
            try {
                while (true) {
                    System.out.println("当前线程 : " + Thread.currentThread().getName() + "开始");
                    Thread.sleep(1000);
                    System.out.println("当前线程 : " + Thread.currentThread().getName() + "结束");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        final StringLock stringLock = new StringLock();

        Thread t1 = new Thread(() -> stringLock.method(), "t1");
        Thread t2 = new Thread(() -> stringLock.method(), "t2");

        t1.start();
        t2.start();
    }
}
