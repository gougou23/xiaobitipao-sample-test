package com.xiaobitipao.sample.test.sample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimeJob {
    public static void main(String args[]) throws Exception {
        Temp command = new Temp();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(command, 2, 3, TimeUnit.SECONDS);
    }
}

class Temp extends Thread {
    public void run() {
        System.out.println("run");
    }
}
