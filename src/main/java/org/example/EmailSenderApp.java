package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.controllers.MainController;
import org.example.utils.ThemeManager;
import org.example.views.MainView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * EmailSender Application - Single Entry Point
 * 
 * This application can run in two modes:
 * 1. GUI Mode (default): Modern JavaFX interface with full features
 * 2. Console Mode: Command-line bulk email sending from CSV/Excel files
 * 
 * Usage:
 * - GUI Mode: java EmailSenderApp
 * - Console Mode: java EmailSenderApp --console
 */
public class EmailSenderApp extends Application {
    
    private static final String APP_TITLE = "EmailSender Pro";
    private static final double MIN_WIDTH = 1200;
    private static final double MIN_HEIGHT = 800;
    
    private Stage primaryStage;
    private MainController mainController;
    private ThemeManager themeManager;
    
    @Override
    public void init() throws Exception {
        super.init();
        themeManager = ThemeManager.getInstance();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        setupPrimaryStage();
        
        // Create main view and controller
        MainView mainView = new MainView();
        mainController = new MainController(mainView, themeManager);
        
        // Create scene
        Scene scene = new Scene(mainView.getRoot(), MIN_WIDTH, MIN_HEIGHT);
        themeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);
        
        primaryStage.setScene(scene);
        showApplication();
        
        // Handle close request gracefully
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
    
    private void setupPrimaryStage() {
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        
        // Center on screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - MIN_WIDTH) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - MIN_HEIGHT) / 2;
        
        primaryStage.setX(centerX);
        primaryStage.setY(centerY);
        
        // Set application icon if available
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        primaryStage.initStyle(StageStyle.DECORATED);
    }
    
    private void showApplication() {
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
        
        System.out.println("✅ EmailSender Pro started successfully!");
        System.out.println("📍 Window position: " + primaryStage.getX() + ", " + primaryStage.getY());
        System.out.println("📏 Window size: " + primaryStage.getWidth() + " x " + primaryStage.getHeight());
    }
    
    /**
     * Console mode for bulk email sending from files
     */
    private static void runConsoleMode(String[] args) {
        System.out.println("🚀 EmailSender Console Mode");
        System.out.println("==========================");
        
        try {
            List<Map<String, String>> people = null;
            
            // Try to read from Excel file first, then CSV
            if (new File("emails.xlsx").exists()) {
                System.out.println("📖 Reading from emails.xlsx...");
                people = ExcelReader.readExcel("emails.xlsx");
            } else if (new File("emails.csv").exists()) {
                System.out.println("📖 Reading from emails.csv...");
                people = CsvReader.readCsv("emails.csv");
            } else {
                System.err.println("❌ Error: No email data file found!");
                System.err.println("📋 Please create either 'emails.xlsx' or 'emails.csv' with columns 'name' and 'email'");
                System.err.println("📝 Example CSV content:");
                System.err.println("name,email");
                System.err.println("John Doe,john.doe@example.com");
                System.exit(1);
            }
            
            if (people == null || people.isEmpty()) {
                System.err.println("❌ No valid email records found in the file!");
                System.exit(1);
            }
            
            // Email configuration
            String subject = "Welcome to Our Service!";
            String bodyTemplate = "<h2>Hello %s,</h2><p>This is a test email from our Java app.</p>";
            
            System.out.println("📊 Found " + people.size() + " recipients");
            System.out.println("📧 Starting email sending process...");
            
            int successCount = 0;
            int failureCount = 0;
            
            for (Map<String, String> person : people) {
                String name = person.get("name");
                String email = person.get("email");
                
                if (name == null || email == null) {
                    System.err.println("⚠️  Warning: Skipping record with missing name or email");
                    failureCount++;
                    continue;
                }
                
                String body = String.format(bodyTemplate, name);
                
                try {
                    MailSender.sendMail(email, subject, body);
                    System.out.println("✅ Mail sent to " + email);
                    successCount++;
                    Thread.sleep(2000); // Delay to avoid spam detection
                } catch (Exception e) {
                    System.err.println("❌ Failed to send email to " + email + ": " + e.getMessage());
                    failureCount++;
                }
            }
            
            System.out.println("\n📈 Email Sending Summary:");
            System.out.println("✅ Successful: " + successCount);
            System.out.println("❌ Failed: " + failureCount);
            System.out.println("📧 Total processed: " + people.size());
            System.out.println("🎉 Email sending process completed!");
            
        } catch (Exception e) {
            System.err.println("❌ Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Main entry point - supports both GUI and console modes
     */
    public static void main(String[] args) {
        // Check for console mode
        if (args.length > 0 && ("--console".equals(args[0]) || "-c".equals(args[0]) || "console".equals(args[0]))) {
            runConsoleMode(args);
            return;
        }
        
        // GUI Mode (default)
        System.out.println("🚀 Starting EmailSender Pro (GUI Mode)");
        System.out.println("💡 For console mode, use: java EmailSenderApp --console");
        
        // Set system properties for better rendering
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        
        launch(args);
    }
}