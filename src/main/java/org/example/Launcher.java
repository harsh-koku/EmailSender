package org.example;

/**
 * Launcher class to avoid JavaFX module issues in IDEs
 * Use this class as the main class in your IDE instead of MainApp
 */
public class Launcher {
    public static void main(String[] args) {
        // This launches the JavaFX application without extending Application
        MainApp.main(args);
    }
}