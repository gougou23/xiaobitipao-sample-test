package com.xiaobitipao.sample.test.thead.sync005;

/**
 * <pre>
 * synchronized 异常： 
 * 出现异常，锁自动释放
 * 
 * 对于 web 应用，异常释放锁的情况，如果不及时处理，很可能对你的应用程序业务逻辑产生严重的错误。
 * 比如你现在执行一个队列任务，很多对象都在等待第一个对象正确执行完毕再去释放锁，
 * 但是第一个对象由于异常的出现，导致业务逻辑没有正常执行完毕就释放了锁，
 * 那么可想而知后续的对象执行的都是错误的逻辑。
 * 所以这一点要引起注意，在编写代码的时候一定要考虑周全。
 * </pre>
 */
public class SyncException {

    private int i = 0;

    public synchronized void operation() {

        while (true) {
            try {
                i++;
                Thread.sleep(100);
                System.out.println(Thread.currentThread().getName() + " , i = " + i);
                if (i == 20) {
                    // Integer.parseInt("a");
                    throw new RuntimeException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                // } catch (Exception e) {
                // System.out.println(222);
                // e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        final SyncException se = new SyncException();
        Thread t1 = new Thread(() -> se.operation(), "t1");
        t1.start();
    }
}
