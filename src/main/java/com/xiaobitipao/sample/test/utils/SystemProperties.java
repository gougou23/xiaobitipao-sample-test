package com.xiaobitipao.sample.test.utils;

public class SystemProperties {

    public static void printSystemProperties() {

        System.out.println("java版本号：" + System.getProperty("java.version"));
        System.out.println("Java提供商名称：" + System.getProperty("java.vendor"));
        System.out.println("Java提供商网站：" + System.getProperty("java.vendor.url"));
        System.out.println("jre目录：" + System.getProperty("java.home"));
        System.out.println("Java虚拟机规范版本号：" + System.getProperty("java.vm.specification.version"));
        System.out.println("Java虚拟机规范提供商：" + System.getProperty("java.vm.specification.vendor"));
        System.out.println("Java虚拟机规范名称：" + System.getProperty("java.vm.specification.name"));
        System.out.println("Java虚拟机版本号：" + System.getProperty("java.vm.version"));
        System.out.println("Java虚拟机提供商：" + System.getProperty("java.vm.vendor"));
        System.out.println("Java虚拟机名称：" + System.getProperty("java.vm.name"));
        System.out.println("Java规范版本号：" + System.getProperty("java.specification.version"));
        System.out.println("Java规范提供商：" + System.getProperty("java.specification.vendor"));
        System.out.println("Java规范名称：" + System.getProperty("java.specification.name"));
        System.out.println("Java类版本号：" + System.getProperty("java.class.version"));
        System.out.println("Java类路径：" + System.getProperty("java.class.path"));
        System.out.println("Java lib路径：" + System.getProperty("java.library.path"));
        System.out.println("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir"));
        System.out.println("Java编译器：" + System.getProperty("java.compiler"));
        System.out.println("Java执行路径：" + System.getProperty("java.ext.dirs"));
        System.out.println("操作系统名称：" + System.getProperty("os.name"));
        System.out.println("操作系统的架构：" + System.getProperty("os.arch"));
        System.out.println("操作系统版本号：" + System.getProperty("os.version"));
        System.out.println("文件分隔符：" + System.getProperty("file.separator"));
        System.out.println("路径分隔符：" + System.getProperty("path.separator"));
        System.out.println("直线分隔符：" + System.getProperty("line.separator"));
        System.out.println("操作系统用户名：" + System.getProperty("user.name"));
        System.out.println("操作系统用户的主目录：" + System.getProperty("user.home"));
        System.out.println("当前程序所在目录：" + System.getProperty("user.dir"));
    }

    public static void main(String[] args) {
        printSystemProperties();
    }
}
