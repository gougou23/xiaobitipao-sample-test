package com.xiaobitipao.sample.test.sample;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;

public class HypericSigar {

    public static void main(String[] args) {
        try {
            System.out.println("----------------------------------");
            System.out.println("System 信息，从 jvm 获取");
            property();

            System.out.println("----------------------------------");
            System.out.println("cpu信息");
            cpu();

            System.out.println("----------------------------------");
            System.out.println("内存信息");
            memory();

            System.out.println("----------------------------------");
            System.out.println("操作系统信息");
            os();

            System.out.println("----------------------------------");
            System.out.println("用户信息");
            who();

            System.out.println("----------------------------------");
            System.out.println("文件系统信息");
            file();

            System.out.println("----------------------------------");
            System.out.println("网络信息");
            net();

            System.out.println("----------------------------------");
            System.out.println("以太网信息");
            ethernet();
            System.out.println("----------------------------------");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    // System 信息，从 jvm 获取
    private static void property() throws UnknownHostException {

        Runtime runtime = Runtime.getRuntime();
        Properties props = System.getProperties();
        Map<String, String> env = System.getenv();

        System.out.println("用户名:" + "\t\t\t" + env.get("USERNAME"));
        System.out.println("计算机名:" + "\t\t\t" + env.get("COMPUTERNAME"));
        System.out.println("计算机域名:" + "\t\t" + env.get("USERDOMAIN"));
        System.out.println("本地ip地址:" + "\t\t" + InetAddress.getLocalHost().getHostAddress());
        System.out.println("本地主机名:" + "\t\t" + InetAddress.getLocalHost().getHostName());
        System.out.println("JVM可以使用的总内存:" + "\t" + runtime.totalMemory());
        System.out.println("JVM可以使用的剩余内存:" + "\t" + runtime.freeMemory());
        System.out.println("JVM可以使用的处理器个数:" + "\t" + runtime.availableProcessors());
        System.out.println("Java的运行环境版本：" + "\t" + props.getProperty("java.version"));
        System.out.println("Java的运行环境供应商：" + "\t" + props.getProperty("java.vendor"));
        System.out.println("Java供应商的URL：" + "\t\t" + props.getProperty("java.vendor.url"));
        System.out.println("Java的安装路径：" + "\t\t" + props.getProperty("java.home"));
        System.out.println("Java的虚拟机规范版本：" + "\t" + props.getProperty("java.vm.specification.version"));
        System.out.println("Java的虚拟机规范供应商：" + "\t" + props.getProperty("java.vm.specification.vendor"));
        System.out.println("Java的虚拟机规范名称：" + "\t" + props.getProperty("java.vm.specification.name"));
        System.out.println("Java的虚拟机实现版本：" + "\t" + props.getProperty("java.vm.version"));
        System.out.println("Java的虚拟机实现供应商：" + "\t" + props.getProperty("java.vm.vendor"));
        System.out.println("Java的虚拟机实现名称：" + "\t" + props.getProperty("java.vm.name"));
        System.out.println("Java运行时环境规范版本：" + "\t" + props.getProperty("java.specification.version"));
        System.out.println("Java运行时环境规范供应商：" + "\t" + props.getProperty("java.specification.vender"));
        System.out.println("Java运行时环境规范名称：" + "\t" + props.getProperty("java.specification.name"));
        System.out.println("Java的类格式版本号：" + "\t" + props.getProperty("java.class.version"));
        System.out.println("Java的类路径：" + "\t\t" + props.getProperty("java.class.path"));
        System.out.println("加载库时搜索的路径列表：" + "\t" + props.getProperty("java.library.path"));
        System.out.println("默认的临时文件路径：" + "\t" + props.getProperty("java.io.tmpdir"));
        System.out.println("一个或多个扩展目录的路径：" + "\t" + props.getProperty("java.ext.dirs"));
        System.out.println("操作系统的名称：" + "\t\t" + props.getProperty("os.name"));
        System.out.println("操作系统的构架：" + "\t\t" + props.getProperty("os.arch"));
        System.out.println("操作系统的版本：" + "\t\t" + props.getProperty("os.version"));
        System.out.println("文件分隔符：" + "\t\t" + props.getProperty("file.separator"));
        System.out.println("路径分隔符：" + "\t\t" + props.getProperty("path.separator"));
        System.out.println("行分隔符：" + "\t\t" + props.getProperty("line.separator"));
        System.out.println("用户的账户名称：" + "\t\t" + props.getProperty("user.name"));
        System.out.println("用户的主目录：" + "\t\t" + props.getProperty("user.home"));
        System.out.println("用户的当前工作目录：" + "\t" + props.getProperty("user.dir"));
    }

    // cpu信息
    private static void cpu() throws SigarException {

        Sigar sigar = new Sigar();
        CpuInfo cpuInfos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = sigar.getCpuPercList();

        System.out.println("cpu 总量参数情况:" + sigar.getCpu());
        System.out.println("cpu 总百分比情况:" + sigar.getCpuPerc());

        for (int i = 0; i < cpuInfos.length; i++) {
            CpuInfo info = cpuInfos[i];
            System.out.println("第" + (i + 1) + "块CPU信息");
            System.out.println("CPU的总量MHz:" + "\t" + info.getMhz());
            System.out.println("CPU生产商:" + "\t" + info.getVendor());
            System.out.println("CPU类别:" + "\t\t" + info.getModel());
            System.out.println("CPU缓存数量:" + "\t" + info.getCacheSize());
            printCpuPerc(cpuList[i]);
        }
    }

    private static void printCpuPerc(CpuPerc cpu) {

        System.out.println("CPU用户使用率:" + "\t" + CpuPerc.format(cpu.getUser()));
        System.out.println("CPU系统使用率:" + "\t" + CpuPerc.format(cpu.getSys()));
        System.out.println("CPU当前等待率:" + "\t" + CpuPerc.format(cpu.getWait()));
        System.out.println("CPU当前错误率:" + "\t" + CpuPerc.format(cpu.getNice()));
        System.out.println("CPU当前空闲率:" + "\t" + CpuPerc.format(cpu.getIdle()));
        System.out.println("CPU总的使用率:" + "\t" + CpuPerc.format(cpu.getCombined()));
    }

    // 内存信息
    private static void memory() throws SigarException {

        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        Swap swap = sigar.getSwap();

        System.out.println("内存总量:" + "\t\t" + mem.getTotal() / 1024L + "K av");
        System.out.println("当前内存使用量:" + "\t" + mem.getUsed() / 1024L + "K used");
        System.out.println("当前内存剩余量:" + "\t" + mem.getFree() / 1024L + "K free");

        System.out.println("交换区总量:" + "\t" + swap.getTotal() / 1024L + "K av");
        System.out.println("当前交换区使用量:" + "\t" + swap.getUsed() / 1024L + "K used");
        System.out.println("当前交换区剩余量:" + "\t" + swap.getFree() / 1024L + "K free");
    }

    // 操作系统信息
    private static void os() {

        OperatingSystem OS = OperatingSystem.getInstance();

        System.out.println("操作系统:" + "\t\t\t\t" + OS.getArch());
        System.out.println("操作系统CpuEndian():" + "\t\t" + OS.getCpuEndian());
        System.out.println("操作系统DataModel():" + "\t\t" + OS.getDataModel());
        System.out.println("操作系统的描述:" + "\t\t\t" + OS.getDescription());
        System.out.println("操作系统类型 OS.getName():" + "\t\t" + OS.getName());
        System.out.println("操作系统类型 OS.getPatchLevel():" + "\t" + OS.getPatchLevel());//
        System.out.println("操作系统的卖主:" + "\t\t\t" + OS.getVendor());
        System.out.println("操作系统的卖主名:" + "\t\t\t" + OS.getVendorCodeName());
        System.out.println("操作系统名称:" + "\t\t\t" + OS.getVendorName());
        System.out.println("操作系统卖主类型:" + "\t\t\t" + OS.getVendorVersion());
        System.out.println("操作系统的版本号:" + "\t\t\t" + OS.getVersion());
    }

    // 用户信息
    private static void who() throws SigarException {

        Sigar sigar = new Sigar();
        Who who[] = sigar.getWhoList();

        if (who != null && who.length > 0) {
            for (int i = 0; i < who.length; i++) {
                System.out.println("当前系统进程表中的用户名-" + String.valueOf(i));
                Who _who = who[i];
                System.out.println("用户控制台:" + "\t\t" + _who.getDevice());
                System.out.println("用户host:" + "\t\t" + _who.getHost());
                System.out.println("getTime():" + "\t\t" + _who.getTime());
                System.out.println("当前系统进程表中的用户名:" + "\t" + _who.getUser());
            }
        }
    }

    // 文件系统信息
    private static void file() throws Exception {
        Sigar sigar = new Sigar();
        FileSystem fslist[] = sigar.getFileSystemList();

        for (int i = 0; i < fslist.length; i++) {
            System.out.println("分区的盘符名称-" + i);
            FileSystem fs = fslist[i];
            System.out.println("盘符名称:" + "\t\t\t" + fs.getDevName());
            System.out.println("盘符路径:" + "\t\t\t" + fs.getDirName());
            System.out.println("盘符标志:" + "\t\t\t" + fs.getFlags());//
            System.out.println("盘符类型:" + "\t\t\t" + fs.getSysTypeName());
            System.out.println("盘符类型名:" + "\t\t" + fs.getTypeName());
            System.out.println("盘符文件系统类型:" + "\t\t" + fs.getType());

            switch (fs.getType()) {
            case 0:
                // TYPE_UNKNOWN ：未知
                break;
            case 1:
                // TYPE_NONE
                break;
            case 2:
                // TYPE_LOCAL_DISK : 本地硬盘
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                System.out.println(fs.getDevName() + "文件系统总大小:" + "\t" + usage.getTotal() + "KB");
                System.out.println(fs.getDevName() + "文件系统剩余大小:" + "\t" + usage.getFree() + "KB");
                System.out.println(fs.getDevName() + "文件系统可用大小:" + "\t" + usage.getAvail() + "KB");
                System.out.println(fs.getDevName() + "文件系统已经使用量:" + "\t" + usage.getUsed() + "KB");
                double usePercent = usage.getUsePercent() * 100D;
                System.out.println(fs.getDevName() + "文件系统资源的利用率:" + "\t" + usePercent + "%");
                break;
            case 3:
                // TYPE_NETWORK ：网络
                break;
            case 4:
                // TYPE_RAM_DISK ：闪存
                break;
            case 5:
                // TYPE_CDROM ：光驱
                break;
            case 6:
                // TYPE_SWAP ：页面交换
                break;
            }
        }
        return;
    }

    // 网络信息
    private static void net() throws Exception {

        Sigar sigar = new Sigar();
        String ifNames[] = sigar.getNetInterfaceList();

        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            System.out.println("网络设备名:" + "\t\t" + name);
            System.out.println("IP地址:" + "\t\t\t" + ifconfig.getAddress());
            System.out.println("子网掩码:" + "\t\t\t" + ifconfig.getNetmask());
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                System.out.println("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            System.out.println(name + "接收的总包裹数:" + "\t" + ifstat.getRxPackets());
            System.out.println(name + "发送的总包裹数:" + "\t" + ifstat.getTxPackets());
            System.out.println(name + "接收到的总字节数:" + "\t" + ifstat.getRxBytes());
            System.out.println(name + "发送的总字节数:" + "\t" + ifstat.getTxBytes());
            System.out.println(name + "接收到的错误包数:" + "\t" + ifstat.getRxErrors());
            System.out.println(name + "发送数据包时的错误数:" + "\t" + ifstat.getTxErrors());
            System.out.println(name + "接收时丢弃的包数:" + "\t" + ifstat.getRxDropped());
            System.out.println(name + "发送时丢弃的包数:" + "\t" + ifstat.getTxDropped());
        }
    }

    // 以太网信息
    private static void ethernet() throws SigarException {

        Sigar sigar = null;
        sigar = new Sigar();
        String[] ifaces = sigar.getNetInterfaceList();

        for (int i = 0; i < ifaces.length; i++) {
            NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
            if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                continue;
            }
            System.out.println(cfg.getName() + "IP地址:" + "\t\t" + cfg.getAddress());
            System.out.println(cfg.getName() + "网关广播地址:" + "\t" + cfg.getBroadcast());
            System.out.println(cfg.getName() + "网卡MAC地址:" + "\t\t" + cfg.getHwaddr());
            System.out.println(cfg.getName() + "子网掩码:" + "\t\t" + cfg.getNetmask());
            System.out.println(cfg.getName() + "网卡描述信息:" + "\t" + cfg.getDescription());
            System.out.println(cfg.getName() + "网卡类型" + "\t\t" + cfg.getType());
        }
    }
}
