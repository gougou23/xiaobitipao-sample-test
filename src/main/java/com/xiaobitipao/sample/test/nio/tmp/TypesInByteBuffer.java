package com.xiaobitipao.sample.test.nio.tmp;

import java.nio.ByteBuffer;

/**
 * ByteBuffer 的使用
 */
public class TypesInByteBuffer {

    public static void main(String args[]) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(30);
        buffer.putLong(7000000000000L);
        buffer.putDouble(Math.PI);

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
    }
}
