package com.hospitalmanagement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BackupScheduler {
    private static final ScheduledExecutorService svc = Executors.newSingleThreadScheduledExecutor();

    public static void start() {
        // Schedule first run at next midnight, then every 24 hours
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = now.with(LocalTime.MIDNIGHT).plusDays(1);
        long initialDelay = java.time.Duration.between(now, next).getSeconds();
        long period = Duration.ofDays(1).getSeconds();
        svc.scheduleAtFixedRate(() -> {
            System.out.println("[BackupScheduler] Running scheduled backup...");
            BackupUtil.runBackup();
        }, initialDelay, period, TimeUnit.SECONDS);
        System.out.println("BackupScheduler started. First run in " + initialDelay + " seconds.");
    }

    public static void stop() {
        svc.shutdownNow();
    }
}
