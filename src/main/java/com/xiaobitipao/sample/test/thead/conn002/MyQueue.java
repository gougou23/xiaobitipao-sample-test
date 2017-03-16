package com.xiaobitipao.sample.test.thead.conn002;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 使用 wait/notify 模拟 BlockingQueue：
 * BlockingQueue 首先是一个队列，并且支持阻塞的机制，阻塞的放入和得到数据。
 * 这里实现 LinkedBlockingQueue 里面的两个简单方法 put 和  take。
 * put(anObject)：
 * 把 anObject 加到 BlockingQueue 里，如果 BlockingQueue 没有空间，则调用此方法的线程被阻塞，直到 BlockingQueue 里面有空间再继续。
 * take：
 * 取走 BlockingQueue 里排在首位的对象，如果 BlockingQueue 为空，阻断并进入等待状态直到 BlockingQueue 有新的数据被加入。
 * </pre>
 */
public class MyQueue {

    // 1.需要一个承装元素的集合
    private LinkedList<Object> list = new LinkedList<>();

    // 2.需要一个计数器
    private AtomicInteger count = new AtomicInteger(0);

    // 3.需要指定上限和下限
    private final int minSize = 0;

    // final 修饰的变量必须初始化(也可以在构造函数中初始化)
    private final int maxSize;

    // 4.构造方法
    public MyQueue(int size) {
        this.maxSize = size;
    }

    // 5.初始化一个对象 用于加锁
    private final Object lock = new Object();

    // 把对象加到 BlockingQueue 里，如果 BlockQueue 没有空间，则调用此方法的线程被阻断，直到 BlockingQueue
    // 里面有空间再继续
    public void put(Object obj) {
        synchronized (lock) {
            if (count.get() == this.maxSize) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 加入元素
            list.add(obj);
            System.out.println("新加入的元素为:" + obj);
            // 计数器累加
            count.incrementAndGet();
            // 通知另外一个线程（唤醒）
            lock.notify();
        }
    }

    // 取走 BlockingQueue 里排在首位的对象，若 BlockingQueue 为空，阻断进入等待状态直到 BlockingQueue
    // 有新的数据被加入
    public Object take() {
        Object ret = null;
        synchronized (lock) {
            if (count.get() == this.minSize) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 移除元素
            ret = list.removeFirst();
            // 计数器递减
            count.decrementAndGet();
            // 唤醒另外一个线程
            lock.notify();
        }
        return ret;
    }

    public int getSize() {
        return this.count.get();
    }

    public static void main(String[] args) {

        final MyQueue mq = new MyQueue(5);
        mq.put("a");
        mq.put("b");
        mq.put("c");
        mq.put("d");
        mq.put("e");

        System.out.println("当前容器的长度:" + mq.getSize());

        Thread t1 = new Thread(() -> {
            mq.put("f");
            mq.put("g");
        }, "t1");

        t1.start();

        Thread t2 = new Thread(() -> {
            Object o1 = mq.take();
            System.out.println("移除的元素为:" + o1);
            Object o2 = mq.take();
            System.out.println("移除的元素为:" + o2);
        }, "t2");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.start();
    }
}
