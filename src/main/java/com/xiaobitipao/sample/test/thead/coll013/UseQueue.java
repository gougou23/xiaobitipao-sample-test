package com.xiaobitipao.sample.test.thead.coll013;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class UseQueue {

    public static void main(String[] args) throws Exception {

        // 高性能无阻塞无界队列：ConcurrentLinkedQueue
        ConcurrentLinkedQueue<String> q1 = new ConcurrentLinkedQueue<>();
        q1.offer("a");
        q1.offer("b");
        q1.offer("c");
        q1.offer("d");
        q1.add("e");

        System.out.println(q1.poll()); // a 从头部取出元素，并从队列里删除
        System.out.println(q1.size()); // 4
        System.out.println(q1.peek()); // b
        System.out.println(q1.size()); // 4

        System.out.println("----------------------");

        // 有界队列：ArrayBlockingQueue
        ArrayBlockingQueue<String> array = new ArrayBlockingQueue<>(5);
        array.put("a");
        array.put("b");
        array.add("c");
        array.add("d");
        // array.add("e");
        // array.add("f");
        System.out.println(array.offer("a", 3, TimeUnit.SECONDS));

        System.out.println("----------------------");

        // 阻塞队列
        LinkedBlockingQueue<String> q2 = new LinkedBlockingQueue<>();
        q2.offer("a");
        q2.offer("b");
        q2.offer("c");
        q2.offer("d");
        q2.offer("e");
        q2.add("f");
        System.out.println(q2.size());

        for (Iterator<String> iterator = q2.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            System.out.println(string);
        }

        List<String> list = new ArrayList<>();
        System.out.println(q2.drainTo(list, 3));
        System.out.println(list.size());
        for (String string : list) {
            System.out.println(string);
        }

        System.out.println("----------------------");

        final SynchronousQueue<String> q3 = new SynchronousQueue<String>();
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(q3.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> q3.add("asdasd"));
        t2.start();
    }
}
