package com.xiaobitipao.sample.test.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {

    public static byte[] gzip(byte[] data) throws Exception {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(bos);) {
            gzip.write(data);
            gzip.finish();
            return bos.toByteArray();
        }
    }

    public static byte[] ungzip(byte[] data) throws Exception {

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                GZIPInputStream gzip = new GZIPInputStream(bis);) {
            byte[] buf = new byte[1024];
            int num = -1;
            while ((num = gzip.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, num);
            }
            byte[] ret = bos.toByteArray();
            bos.flush();
            return ret;
        }
    }

    public static void main(String[] args) throws Exception {

        // 读取文件
        String readPath = System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar + "netty" + File.separatorChar + "photo.jpg";

        // 写出文件
        String writePath = System.getProperty("user.dir") + File.separatorChar + "data" + File.separatorChar + "netty" + File.separatorChar + "photo_new.jpg";

        try (FileInputStream in = new FileInputStream(readPath); FileOutputStream fos = new FileOutputStream(writePath);) {

            byte[] data = new byte[in.available()];
            in.read(data);

            System.out.println("文件原始大小:" + data.length);

            byte[] ret1 = GzipUtils.gzip(data);
            System.out.println("压缩之后大小:" + ret1.length);

            byte[] ret2 = GzipUtils.ungzip(ret1);
            System.out.println("还原之后大小:" + ret2.length);

            // 写出文件
            fos.write(ret2);
        }
    }
}
