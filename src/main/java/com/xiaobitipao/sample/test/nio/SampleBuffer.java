package com.xiaobitipao.sample.test.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class SampleBuffer {

	public static void main(String[] args) throws Exception {

		// readAll();

		// testBuffer();

		testBuffer2();
	}

	public static void readAll() throws Exception {

		RandomAccessFile aFile = new RandomAccessFile("data/nio/nio-data.txt", "rw");

		FileChannel inChannel = aFile.getChannel();

		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = inChannel.read(buf);

		while (bytesRead != -1) {
			System.out.println("Read " + bytesRead);
			buf.flip();
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get());
			}
			buf.clear();
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
	}

	public static void testBuffer() throws Exception {

		RandomAccessFile aFile = new RandomAccessFile("data/nio/nio-data.txt", "rw");

		FileChannel inChannel = aFile.getChannel();

		// 创建一个 48 byte 的缓冲区
		ByteBuffer buf = ByteBuffer.allocate(48);

		// 把数据通过 channel 读入缓冲区
		int bytesRead = inChannel.read(buf);

		// 读取的字节数
		System.out.println("Read " + bytesRead);
		System.out.println();

		// 反转：将Buffer从写模式切换到读模式
		buf.flip();

		// 当前位置，限制，容量
		System.out.println("position=" + buf.position());
		System.out.println("limit=" + buf.limit());
		System.out.println("capacity=" + buf.capacity());
		System.out.println();

		// 读取一个 byte
		System.out.print((char) buf.get());

		// 设置标记（如果后面需要用到 reset 方法的话）
		// 此处通过调用 mark 方法将标记设置为 1（因为此时的 position 为1）
		// buf.mark();

		// 读取一个 byte
		System.out.println((char) buf.get());
		System.out.println();

		// 读取两个值后的当前位置，限制，容量
		System.out.println("position=" + buf.position());
		System.out.println("limit=" + buf.limit());
		System.out.println("capacity=" + buf.capacity());
		System.out.println();

		// 压缩
		// 压缩并不会清空缓冲区中已经读取得数据
		// 压缩会更新 position = limit - position
		// 压缩会更新 limit = capacity
		// buf.compact();

		// 重至
		// 需要事先调用 mark 设置标记
		// 不能和压缩 compact 同时使用
		// 重至会更新 position 为设置的标记（即设置标记时的 position）
		// buf.reset();

		// 清空缓冲区
		// 清空缓冲区并不会清空缓冲区中的数据
		// 清空缓冲区会更新 position = 0
		// 清空缓冲区会更新 limit = capacity
		// buf.clear();

		System.out.println("position=" + buf.position());
		System.out.println("limit=" + buf.limit());
		System.out.println("capacity=" + buf.capacity());
		System.out.println((char) buf.get());
		System.out.println((char) buf.get());
		System.out.println("position=" + buf.position());
		System.out.println();

		// 一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入
		// 实际上不会清空缓冲区中的数据
		// 只有在写入操作时才会清空缓冲区内容
		buf.clear();

		aFile.close();
	}

	public static void testBuffer2() throws Exception {

		CharBuffer buff = CharBuffer.allocate(8);

		System.out.println("capacity:" + buff.capacity());
		System.out.println("limit:" + buff.limit());
		System.out.println("position:" + buff.position());
		System.out.println();

		buff.put('a');
		buff.put('b');
		buff.put('c');

		System.out.println("加入三个元素后,position=" + buff.position());
		System.out.println();

		buff.flip();

		System.out.println("执行flip后,limit=" + buff.limit());
		System.out.println("position=" + buff.position());
		System.out.println();

		// 取出第一个元素
		System.out.println("第一个元素(position=0):" + buff.get());
		System.out.println("取出第一个元素后,position=" + buff.position());
		System.out.println();

		buff.clear();

		System.out.println("执行clear方法后,limit=" + buff.limit());
		System.out.println("执行clear方法后,position=" + buff.position());
		System.out.println("执行clear后,缓冲区的内容并没有被清空.第三个元素为:" + buff.get(2));
		System.out.println("执行绝对读取后,position=" + buff.position());
	}
}
