package com.hospitalmanagement;

/**
 * Launcher class for creating executable JAR
 * This is needed because JavaFX applications cannot be launched directly from JAR
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
