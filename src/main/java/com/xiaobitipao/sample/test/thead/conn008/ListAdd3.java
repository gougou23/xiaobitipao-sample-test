package com.xiaobitipao.sample.test.thead.conn008;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * wait notfiy 方法，wait释放锁，notfiy不释放锁
 * 
 * @author alienware
 *
 */
public class ListAdd3 {

    private volatile static List<String> list = new ArrayList<>();

    public void add() {
        list.add("xiaobitipao");
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {

        final ListAdd3 list2 = new ListAdd3();

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    list2.add();
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素 ...");
                    Thread.sleep(500);
                    if (list2.size() == 5) {
                        System.out.println("已经发出通知 ...");
                        countDownLatch.countDown();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            if (list2.size() != 5) {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("当前线程：" + Thread.currentThread().getName() + "收到通知线程停止 ...");
            throw new RuntimeException();
        }, "t2");

        t2.start();
        t1.start();
    }
}
