package com.xiaobitipao.sample.test.nio.tmp;

import java.nio.*;

/**
 * 缓冲区初探
 */
public class UseFloatBuffer {

    public static void main(String args[]) throws Exception {

        FloatBuffer buffer = FloatBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put(i);
        }

        buffer.flip();

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
