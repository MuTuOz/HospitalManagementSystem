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
        // Check for test mode (1 minute interval)
        String testMode = System.getenv("HMS_BACKUP_TEST_MODE");
        if ("true".equalsIgnoreCase(testMode)) {
            // Run every 1 minute for testing
            svc.scheduleAtFixedRate(() -> {
                System.out.println("[BackupScheduler] Running scheduled backup (TEST MODE - 1 min interval)...");
                BackupUtil.runBackup();
            }, 10, 1, TimeUnit.MINUTES);
            System.out.println("BackupScheduler started in TEST MODE. Running every 1 minute.");
            return;
        }
        
        // Default: Run first backup after 60 seconds, then every 24 hours
        long initialDelay = 60; // 60 seconds
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
