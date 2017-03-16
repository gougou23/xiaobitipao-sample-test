package com.xiaobitipao.sample.test.thead.sync001;

/**
 * <pre>
 * 线程安全概念：
 *     当多个线程访问某一个类（对象或方法）时，这个对象始终都能表现出正确的行为，那么这个类（对象或方法）就是线程安全的。
 * synchronized：
 *     可以在任意对象及方法上加锁，而加锁的这段代码称为"互斥区"或"临界区"
 * </pre>
 */
public class MyThread extends Thread {

	private int count = 5;

	// 测试使用/不使用 synchronized 的区别
	// public synchronized void run() {
	public void run() {
		count--;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}

	/**
	 * <pre>
	 * 当多个线程访问 MyThread 的 run 方法时，以排队的方式进行处理（这里排队是按照 CPU 分配的先后顺序而定的）
	 * 一个线程想要执行 synchronized 修饰的方法里的代码：
	 * 首先是尝试获得锁，
	 * 如果拿到锁，执行 synchronized 代码体内容；
	 * 如果拿不到锁，这个线程就会不断的尝试获得这把锁，直到拿到为止。 而且是多个线程同时去竞争这把锁。（也就是会有锁竞争的问题，会导致 CPU 负载增加）
	 * </pre>
	 */
	public static void main(String[] args) {

		MyThread myThread = new MyThread();

		Thread t1 = new Thread(myThread, "t1");
		Thread t2 = new Thread(myThread, "t2");
		Thread t3 = new Thread(myThread, "t3");
		Thread t4 = new Thread(myThread, "t4");
		Thread t5 = new Thread(myThread, "t5");

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
	}
}
