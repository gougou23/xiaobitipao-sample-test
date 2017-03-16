package com.xiaobitipao.sample.test.thead.sync005;

/**
 * <pre>
 * synchronized 的锁重入:
 * 关键字 synchronized 拥有锁重入的功能
 * 也就是在使用 synchronized 时，当一个线程得到了一个对象的锁以后，再次请求此对象时是可以再次得到该对象的锁的
 * </pre>
 */
public class SyncDubbo2 {

    static class Main {

        public int i = 10;

        public synchronized void operationSupper() {
            try {
                i--;
                System.out.println("Main print i = " + i);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Sub extends Main {

        public synchronized void operationSub() {
            try {
                while (i > 0) {
                    i--;
                    System.out.println("Sub print i = " + i);
                    Thread.sleep(100);
                    this.operationSupper();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            Sub sub = new Sub();
            sub.operationSub();
        });

        t1.start();
    }
}
