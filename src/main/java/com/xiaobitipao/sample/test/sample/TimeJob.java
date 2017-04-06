package com.xiaobitipao.sample.test.sample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.xiaobitipao.sample.test.utils.DateUtils;

public class TimeJob {

    public static void main(String args[]) throws Exception {

        Thread command = new Thread(() -> System.out.println(DateUtils.formatSystemDate()));
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(command, 0, 2, TimeUnit.SECONDS);
    }
}
