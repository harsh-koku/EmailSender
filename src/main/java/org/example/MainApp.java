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

/**
 * Modern JavaFX Email Sender Application
 * Features: Responsive design, animations, theme switching, glassmorphism UI
 * Features: Responsive design, animations, theme switching, glassmorphism UI
 * Features: Responsive design, animations, theme switching, glassmorphism UI
 * Features: Responsive design, animations, theme switching, glassmorphism UI
 */
public class MainApp extends Application {
    
    private static final String APP_TITLE = "Email Sender Pro";
    private static final double MIN_WIDTH = 1200;
    private static final double MIN_HEIGHT = 800;
    
    private Stage primaryStage;
    private MainController mainController;
    private ThemeManager themeManager;
    
    @Override
    public void init() throws Exception {
        super.init();
        // Initialize theme manager
        themeManager = ThemeManager.getInstance();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        
        // Configure the primary stage
        setupPrimaryStage();
        
        // Create main view and controller
        MainView mainView = new MainView();
        mainController = new MainController(mainView, themeManager);
        
        // Create scene
        Scene scene = new Scene(mainView.getRoot(), MIN_WIDTH, MIN_HEIGHT);
        
        // Apply initial theme
        themeManager.applyTheme(scene, ThemeManager.Theme.LIGHT);
        
        // Set scene and show stage
        primaryStage.setScene(scene);
        
        // Show with fade-in animation
        showWithAnimation();
        
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
        
        // Force center on PRIMARY screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = screenBounds.getMinX() + (screenBounds.getWidth() - MIN_WIDTH) / 2;
        double centerY = screenBounds.getMinY() + (screenBounds.getHeight() - MIN_HEIGHT) / 2;
        
        primaryStage.setX(centerX);
        primaryStage.setY(centerY);
        
        System.out.println("Screen bounds: " + screenBounds);
        System.out.println("Window will be at: " + centerX + ", " + centerY);
        
        // Set application icon (you can add an icon file later)
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        // Modern window styling
        primaryStage.initStyle(StageStyle.DECORATED);
    }
    
    private void showWithAnimation() {
        // Force window to be visible and in front
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setOpacity(1.0); // Start fully visible
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
        
        // Debug output
        System.out.println("=== WINDOW OPENED ===");
        System.out.println("Window position: " + primaryStage.getX() + ", " + primaryStage.getY());
        System.out.println("Window size: " + primaryStage.getWidth() + " x " + primaryStage.getHeight());
        System.out.println("Window visible: " + primaryStage.isShowing());
        System.out.println("====================");
        
        // Remove always on top after 2 seconds
        javafx.animation.Timeline removeOnTop = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), e -> {
                primaryStage.setAlwaysOnTop(false);
            })
        );
        removeOnTop.play();
        
        // Optional: Fade-in animation (disabled for debugging)
        /*
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(
            javafx.util.Duration.millis(800), primaryStage.getScene().getRoot()
        );
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        javafx.animation.Timeline stageOpacity = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.millis(800),
                new javafx.animation.KeyValue(primaryStage.opacityProperty(), 1.0))
        );
        
        stageOpacity.play();
        fadeIn.play();
        */
    }
    
    public static void main(String[] args) {
        // Set system properties for better rendering
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        
        launch(args);
    }
}
