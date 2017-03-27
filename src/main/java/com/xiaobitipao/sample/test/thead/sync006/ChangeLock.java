package com.xiaobitipao.sample.test.thead.sync006;

/**
 * <pre>
 * 锁对象的改变问题：
 * 尽量不在内部修改锁内容，否则容易导致锁对象发生改变
 * </pre>
 */
public class ChangeLock {

    private String lock = "lock";

    private void method() {
        synchronized (lock) {
            try {
                System.out.println("当前线程 : " + Thread.currentThread().getName() + "开始");
                // 尽量不在内部修改锁内容，否则容易导致锁对象发生改变
                lock = "change lock";
                Thread.sleep(2000);
                System.out.println("当前线程 : " + Thread.currentThread().getName() + "结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        final ChangeLock changeLock = new ChangeLock();

        Thread t1 = new Thread(() -> changeLock.method(), "t1");
        Thread t2 = new Thread(() -> changeLock.method(), "t2");

        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}
