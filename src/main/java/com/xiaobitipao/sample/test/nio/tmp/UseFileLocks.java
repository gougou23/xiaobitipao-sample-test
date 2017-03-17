package com.xiaobitipao.sample.test.nio.tmp;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * <pre>
 * 锁定文件:
 * 这个程序获取一个文件上的锁，持有三秒钟，然后释放它。如果同时运行这个程序的多个实例，您会看到每个实例依次获得锁。
 * </pre>
 */
public class UseFileLocks {

    private static final int start = 10;
    private static final int end = 20;

    public static void main(String args[]) throws Exception {

        // Get file channel
        RandomAccessFile raf = new RandomAccessFile("data/nio/copyFileTo.txt", "rw");
        FileChannel fc = raf.getChannel();

        // Get lock
        System.out.println("trying to get lock");
        FileLock lock = fc.lock(start, end, false);
        System.out.println("got lock!");

        // Pause
        System.out.println("pausing");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
        }

        // Release lock
        System.out.println("going to release lock");
        lock.release();
        System.out.println("released lock");

        raf.close();
    }
}
