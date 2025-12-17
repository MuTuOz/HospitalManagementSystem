package com.hospitalmanagement;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupUtil {
    // Tries to run mysqldump if path provided in HMS_MYSQLDUMP_PATH env var.
    // Fallback: prints instructions and returns false.
    public static boolean runBackup() {
        String dumpPath = System.getenv("HMS_MYSQLDUMP_PATH");
        String dbUrl = System.getenv("HMS_DB_URL");
        String dbUser = System.getenv("HMS_DB_USER");
        String dbPass = System.getenv("HMS_DB_PASS");
        if (dumpPath == null || dbUrl == null || dbUser == null) {
            System.out.println("Backup not configured (HMS_MYSQLDUMP_PATH or DB creds missing). Skipping.");
            return false;
        }
        try {
            // dbUrl like jdbc:mysql://host:3306/dbname
            String dbName = "hms";
            try {
                var uri = new java.net.URI(dbUrl.substring(5)); // remove 'jdbc:'
                String path = uri.getPath();
                if (path != null && path.length() > 1) dbName = path.substring(1);
            } catch (Exception ignored) {}

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String fname = "hms_backup_" + LocalDateTime.now().format(fmt) + ".sql";
            String outPath = Paths.get(System.getProperty("user.home"), fname).toString();

            ProcessBuilder pb = new ProcessBuilder(dumpPath, "-u", dbUser, "-p" + (dbPass == null ? "" : dbPass), dbName, "-r", outPath);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int code = p.waitFor();
            if (code == 0) {
                System.out.println("Backup saved to: " + outPath);
                return true;
            } else {
                System.out.println("mysqldump exited with code: " + code);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Restore database from SQL file using mysql client. Requires HMS_MYSQL_PATH or mysql in PATH and DB creds env vars.
    public static boolean restoreFrom(java.io.File sqlFile) {
        if (sqlFile == null || !sqlFile.exists()) return false;
        String mysqlPath = System.getenv("HMS_MYSQL_PATH");
        if (mysqlPath == null) mysqlPath = "mysql"; // hope it's in PATH
        String dbUrl = System.getenv("HMS_DB_URL");
        String dbUser = System.getenv("HMS_DB_USER");
        String dbPass = System.getenv("HMS_DB_PASS");
        if (dbUrl == null || dbUser == null) {
            System.out.println("DB connection info missing (HMS_DB_URL/HMS_DB_USER). Cannot restore.");
            return false;
        }
        String dbName = "hms";
        try {
            var uri = new java.net.URI(dbUrl.substring(5));
            String path = uri.getPath();
            if (path != null && path.length() > 1) dbName = path.substring(1);
        } catch (Exception ignored) {}

        try {
            // Use: mysql -u user -pPassword dbname -e "source file.sql"
            String exec = mysqlPath;
            ProcessBuilder pb = new ProcessBuilder(exec, "-u", dbUser, "-p" + (dbPass == null ? "" : dbPass), dbName, "-e", "source " + sqlFile.getAbsolutePath());
            pb.redirectErrorStream(true);
            Process p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            int code = p.waitFor();
            if (code == 0) {
                System.out.println("Restore completed from: " + sqlFile.getAbsolutePath());
                return true;
            } else {
                System.out.println("mysql exited with code: " + code);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
