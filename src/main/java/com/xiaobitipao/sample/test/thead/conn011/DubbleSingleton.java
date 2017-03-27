package com.xiaobitipao.sample.test.thead.conn011;

public class DubbleSingleton {

    private DubbleSingleton() {
    }

    /**
     * <pre>
     * 当线程执行到第一个 null 检查时，ds 引用的对象有可能还没有完成初始化
     * 原因在于：
     * ds = new DubbleSingleton();
     * 代码可以分解为以下三行伪代码
     * memory = allocate();　　// 1：分配对象的内存空间
     * ctorInstance(memory);　 // 2：初始化对象
     * instance = memory;　　     // 3：设置instance指向刚分配的内存地址
     * 上述代码的 2 和 3 可能会被重排，重排之后的顺序可能会是
     * memory = allocate();　　// 1：分配对象的内存空间
     * instance = memory;　　     // 3：设置instance指向刚分配的内存地址(此时对象还没有被初始化)
     * ctorInstance(memory);　 // 2：初始化对象
     * 
     * 当对象的引用为 volatile 后，上述伪代码的 2 和 3 的重排序，在多线程环境中将被禁止
     * 所以这里的 volatile 是必须的
     * </pre>
     */
    private static volatile DubbleSingleton ds;

    public static DubbleSingleton getDs() {

        if (ds == null) {
            try {
                // 模拟初始化对象的准备时间 ...
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (DubbleSingleton.class) {
                if (ds == null) {
                    ds = new DubbleSingleton();
                }
            }
        }
        return ds;
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()), "t1");
        Thread t2 = new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()), "t2");
        Thread t3 = new Thread(() -> System.out.println(DubbleSingleton.getDs().hashCode()), "t3");

        t1.start();
        t2.start();
        t3.start();
    }
}
