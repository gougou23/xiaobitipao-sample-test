package com.xiaobitipao.sample.test.thead.conn008;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 线程之间通信概念：
 * 线程是操作系统中独立的个体，但这些个体如果不经过特殊的处理就不能成为一个整体，线程间的通信就成为整体的必用方式之一。
 * 当线程存在通信指挥，系统间的交互性会更强大，在提高 CPU 利用率的同时还会使开发人员对线程任务在处理的过程中进行有效的把控和监督。
 * 
 * 使用 wait/notify 方法实现线程间的通信。(注意这两个方法都是 Object 类的方法，换句话说，java 为所有的对象都提供了这两个方法)
 * 1．wait 和 notify 必须配合 synchronized 关键字使用
 * 2．wait 方法释放锁，notify 方法不释放锁
 * </pre>
 */
public class ListAdd1 {

    private volatile static List<String> list = new ArrayList<>();

    public void add() {
        list.add("xiaobitipao");
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {

        final ListAdd1 list1 = new ListAdd1();

        Thread t1 = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    list1.add();
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素 ...");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            // 由于通过死循环进行轮训，这里会消耗 CPU 资源
            while (true) {
                if (list1.size() == 5) {
                    System.out.println("当前线程收到通知：" + Thread.currentThread().getName() + " list size = 5 线程停止 ...");
                    throw new RuntimeException();
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
