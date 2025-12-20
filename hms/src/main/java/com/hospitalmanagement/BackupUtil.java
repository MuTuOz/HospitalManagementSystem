package com.hospitalmanagement;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.io.FileInputStream;

public class BackupUtil {
    // Tries to run mysqldump if path provided in HMS_MYSQLDUMP_PATH env var or database.properties.
    // Fallback: prints instructions and returns false.
    public static boolean runBackup() {
        String dumpPath = System.getenv("HMS_MYSQLDUMP_PATH");
        String dbUrl = System.getenv("HMS_DB_URL");
        String dbUser = System.getenv("HMS_DB_USER");
        String dbPass = System.getenv("HMS_DB_PASS");
        
        // If env vars not set, try to read from database.properties
        if (dumpPath == null || dbUrl == null || dbUser == null) {
            try {
                Properties props = new Properties();
                
                // Try multiple locations for database.properties
                String[] possiblePaths = {
                    "database.properties",
                    "src/main/resources/database.properties",
                    System.getProperty("user.dir") + "/database.properties",
                    System.getProperty("user.dir") + "/src/main/resources/database.properties"
                };
                
                boolean loaded = false;
                for (String path : possiblePaths) {
                    try {
                        FileInputStream fis = new FileInputStream(path);
                        props.load(fis);
                        fis.close();
                        System.out.println("[BackupUtil] Loaded database.properties from: " + path);
                        loaded = true;
                        break;
                    } catch (IOException e) {
                        // Try next path
                    }
                }
                
                if (loaded) {
                    if (dumpPath == null) {
                        dumpPath = props.getProperty("mysqldump.path", "mysqldump");
                    }
                    if (dbUrl == null) {
                        // Try both property name styles
                        dbUrl = props.getProperty("db.url");
                        if (dbUrl == null) {
                            String host = props.getProperty("DB_HOST", "localhost");
                            String port = props.getProperty("DB_PORT", "3306");
                            String dbName = props.getProperty("DB_NAME", "hospital_management");
                            dbUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
                        }
                    }
                    if (dbUser == null) {
                        // Try both property name styles
                        dbUser = props.getProperty("db.user");
                        if (dbUser == null) {
                            dbUser = props.getProperty("DB_USER");
                        }
                    }
                    if (dbPass == null) {
                        // Try both property name styles
                        dbPass = props.getProperty("db.password");
                        if (dbPass == null) {
                            dbPass = props.getProperty("DB_PASSWORD");
                        }
                    }
                    
                    System.out.println("[BackupUtil] Loaded config - URL: " + dbUrl + ", User: " + dbUser);
                }
            } catch (Exception e) {
                System.out.println("[BackupUtil] Could not read database.properties: " + e.getMessage());
            }
        }
        
        if (dbUrl == null || dbUser == null) {
            System.out.println("Backup not configured (DB URL or User missing). Skipping.");
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
            String backupDir = Paths.get(System.getProperty("user.home"), "HMS_Backups").toString();
            new java.io.File(backupDir).mkdirs();
            String outPath = Paths.get(backupDir, fname).toString();

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
        
        // If env vars not set, try to read from database.properties
        if (dbUrl == null || dbUser == null) {
            try {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream("database.properties");
                props.load(fis);
                fis.close();
                
                if (dbUrl == null) {
                    dbUrl = props.getProperty("db.url");
                }
                if (dbUser == null) {
                    dbUser = props.getProperty("db.user");
                }
                if (dbPass == null) {
                    dbPass = props.getProperty("db.password");
                }
            } catch (Exception e) {
                System.out.println("Could not read database.properties: " + e.getMessage());
            }
        }
        
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
