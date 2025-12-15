package com.hospitalmanagement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    private static Connection connection = null;

    static {
        loadDatabaseConfig();
    }

    private static void loadDatabaseConfig() {
        Properties props = new Properties();
        
        // Try to load from external config file first
        try (InputStream input = new FileInputStream("database.properties")) {
            props.load(input);
            System.out.println("Loaded database configuration from database.properties");
        } catch (IOException e) {
            // If external file not found, try to load from resources
            try (InputStream input = DatabaseConnection.class.getResourceAsStream("/database.properties")) {
                if (input != null) {
                    props.load(input);
                    System.out.println("Loaded database configuration from resources");
                } else {
                    System.out.println("database.properties not found, using default values");
                }
            } catch (IOException ex) {
                System.out.println("Could not load database.properties, using defaults");
            }
        }
        
        // Get properties or use defaults
        String host = props.getProperty("DB_HOST", "localhost");
        String port = props.getProperty("DB_PORT", "3306");
        String dbName = props.getProperty("DB_NAME", "hospital_management_system");
        USERNAME = props.getProperty("DB_USER", "root");
        PASSWORD = props.getProperty("DB_PASSWORD", "Al1071145301");
        
        // Build connection URL
        URL = "jdbc:mysql://" + host + ":" + port + "/" + dbName;
        
        System.out.println("Database URL: " + URL);
        System.out.println("Database User: " + USERNAME);
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}