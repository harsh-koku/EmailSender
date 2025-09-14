package org.example.utils;

import javafx.scene.Scene;
import java.util.ArrayList;
import java.util.List;

/**
 * Theme Manager for handling light and dark themes
 * Supports dynamic theme switching with smooth transitions
 */
public class ThemeManager {
    
    public enum Theme {
        LIGHT("light-theme.css"),
        DARK("dark-theme.css");
        
        private final String cssFile;
        
        Theme(String cssFile) {
            this.cssFile = cssFile;
        }
        
        public String getCssFile() {
            return cssFile;
        }
    }
    
    private static ThemeManager instance;
    private Theme currentTheme = Theme.LIGHT;
    private final List<Scene> managedScenes = new ArrayList<>();
    
    private ThemeManager() {}
    
    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }
    
    /**
     * Apply theme to a scene and register it for future theme changes
     */
    public void applyTheme(Scene scene, Theme theme) {
        if (scene == null) return;
        
        // Remove existing theme stylesheets
        scene.getStylesheets().removeIf(stylesheet -> 
            stylesheet.contains("light-theme.css") || 
            stylesheet.contains("dark-theme.css"));
        
        // Add new theme stylesheet
        String themeUrl = getClass().getResource("/css/" + theme.getCssFile()).toExternalForm();
        scene.getStylesheets().add(themeUrl);
        
        // Add base styles
        String baseUrl = getClass().getResource("/css/base-styles.css").toExternalForm();
        if (!scene.getStylesheets().contains(baseUrl)) {
            scene.getStylesheets().add(0, baseUrl);
        }
        
        // Add animations styles
        String animationsUrl = getClass().getResource("/css/animations.css").toExternalForm();
        if (!scene.getStylesheets().contains(animationsUrl)) {
            scene.getStylesheets().add(animationsUrl);
        }
        
        // Register scene for future updates
        if (!managedScenes.contains(scene)) {
            managedScenes.add(scene);
        }
        
        currentTheme = theme;
    }
    
    /**
     * Toggle between light and dark themes
     */
    public void toggleTheme() {
        Theme newTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
        switchToTheme(newTheme);
    }
    
    /**
     * Switch to specific theme for all managed scenes
     */
    public void switchToTheme(Theme theme) {
        for (Scene scene : managedScenes) {
            applyTheme(scene, theme);
        }
        currentTheme = theme;
    }
    
    public Theme getCurrentTheme() {
        return currentTheme;
    }
    
    public boolean isDarkTheme() {
        return currentTheme == Theme.DARK;
    }
    
    public void unregisterScene(Scene scene) {
        managedScenes.remove(scene);
    }
    
    /**
     * Set specific theme (for compatibility with settings)
     */
    public void setDarkTheme(boolean isDark) {
        Theme targetTheme = isDark ? Theme.DARK : Theme.LIGHT;
        if (targetTheme != currentTheme) {
            switchToTheme(targetTheme);
        }
    }
}
