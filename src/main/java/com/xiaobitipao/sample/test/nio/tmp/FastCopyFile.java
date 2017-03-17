package com.xiaobitipao.sample.test.nio.tmp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.xiaobitipao.sample.test.utils.DateUtils;

/**
 * 直接缓冲区读写
 */
public class FastCopyFile {

    public static void main(String args[]) throws Exception {

        // if (args.length < 2) {
        // System.err.println("Usage: java FastCopyFile infile outfile");
        // System.exit(1);
        // }
        //
        // String infile = args[0];
        // String outfile = args[1];

        String infile = "data/nio/copyFileFrom.txt";
        String outfile = "data/nio/copyFileTo.txt";

        System.out.println("处理开始：" + DateUtils.formatSystemDate());

        try (FileInputStream fin = new FileInputStream(infile);
                FileOutputStream fout = new FileOutputStream(outfile);
                FileChannel fcin = fin.getChannel();
                FileChannel fcout = fout.getChannel();) {

            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

            while (fcin.read(buffer) != -1) {
                buffer.flip();
                fcout.write(buffer);
                buffer.clear();
            }

            System.out.println("done!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("处理终了：" + DateUtils.formatSystemDate());
    }
}
