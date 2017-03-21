package com.xiaobitipao.sample.test.thead.coll012;

import java.util.Vector;

/**
 * <pre>
 * 多线程使用 Vector 或者 HashTable 的简单线程同步问题示例
 * </pre>
 */
public class Tickets {

    public static void main(String[] args) {

        // 初始化火车票池并添加火车票:避免线程同步可采用 Vector 替代 ArrayList，HashTable 替代 HashMap
        final Vector<String> tickets = new Vector<String>();
        for (int i = 1; i <= 1000; i++) {
            tickets.add("火车票-" + i);
        }

        // Map<String, String> map = Collections.synchronizedMap(new
        // HashMap<String, String>());

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                while (true) {
                    if (tickets.isEmpty())
                        break;
                    System.out.println(Thread.currentThread().getName() + "---" + tickets.remove(0));
                }
            }, "线程-" + i).start();
        }
    }
}
