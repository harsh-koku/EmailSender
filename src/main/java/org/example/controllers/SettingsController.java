package org.example.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.example.utils.DataManager;
import org.example.utils.ThemeManager;
import org.example.views.SettingsView;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Map;
import java.util.Properties;

/**
 * Controller for Settings functionality
 */
public class SettingsController {
    
    private final SettingsView view;
    private final DataManager dataManager;
    private final ThemeManager themeManager;
    private Map<String, Object> currentSettings;
    
    public SettingsController(SettingsView view, DataManager dataManager, ThemeManager themeManager) {
        this.view = view;
        this.dataManager = dataManager;
        this.themeManager = themeManager;
        this.currentSettings = dataManager.loadSettings();
        
        initialize();
        loadSettingsIntoView();
    }
    
    private void initialize() {
        // Setup event handlers
        view.getSaveSettingsBtn().setOnAction(e -> handleSaveSettings());
        view.getTestConnectionBtn().setOnAction(e -> handleTestConnection());
        view.getResetToDefaultBtn().setOnAction(e -> handleResetToDefault());
        
        // Theme change handlers
        view.getLightThemeRadio().setOnAction(e -> handleThemeChange(false));
        view.getDarkThemeRadio().setOnAction(e -> handleThemeChange(true));
        
        // Animation speed slider
        view.getAnimationSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
            // Update the label next to the slider
            String speedText = String.format("%.1fx", newVal.doubleValue());
            // Find and update the speed label (assuming it's accessible)
            // This could be improved by making the label accessible from the view
        });
        
        // Real-time validation
        view.getSenderEmailField().textProperty().addListener((obs, oldText, newText) -> {
            validateEmailField();
        });
        
        view.getSmtpPortField().textProperty().addListener((obs, oldText, newText) -> {
            validatePortField();
        });
    }
    
    private void loadSettingsIntoView() {
        // Email settings
        view.getSenderEmailField().setText((String) currentSettings.getOrDefault("senderEmail", ""));
        view.getEmailPasswordField().setText((String) currentSettings.getOrDefault("emailPassword", ""));
        view.getSmtpHostField().setText((String) currentSettings.getOrDefault("smtpHost", "smtp.gmail.com"));
        view.getSmtpPortField().setText(String.valueOf(currentSettings.getOrDefault("smtpPort", "587")));
        view.getEnableSSLCheckBox().setSelected((Boolean) currentSettings.getOrDefault("enableSSL", false));
        view.getEnableSTARTTLSCheckBox().setSelected((Boolean) currentSettings.getOrDefault("enableSTARTTLS", true));
        
        // Theme settings
        boolean darkTheme = (Boolean) currentSettings.getOrDefault("darkTheme", false);
        if (darkTheme) {
            view.getDarkThemeRadio().setSelected(true);
        } else {
            view.getLightThemeRadio().setSelected(true);
        }
        
        // Animation settings
        view.getEnableAnimationsCheckBox().setSelected((Boolean) currentSettings.getOrDefault("enableAnimations", true));
        
        Object animationSpeedObj = currentSettings.get("animationSpeed");
        double animationSpeed = 1.0;
        if (animationSpeedObj instanceof Number) {
            animationSpeed = ((Number) animationSpeedObj).doubleValue();
        }
        view.getAnimationSpeedSlider().setValue(animationSpeed);
        
        // General settings
        view.getEnableSoundsCheckBox().setSelected((Boolean) currentSettings.getOrDefault("enableSounds", false));
    }
    
    private void handleSaveSettings() {
        if (!validateAllFields()) {
            return;
        }
        
        // Collect settings from view
        currentSettings.put("senderEmail", view.getSenderEmailField().getText().trim());
        currentSettings.put("emailPassword", view.getEmailPasswordField().getText());
        currentSettings.put("smtpHost", view.getSmtpHostField().getText().trim());
        currentSettings.put("smtpPort", view.getSmtpPortField().getText().trim());
        currentSettings.put("enableSSL", view.getEnableSSLCheckBox().isSelected());
        currentSettings.put("enableSTARTTLS", view.getEnableSTARTTLSCheckBox().isSelected());
        
        // Theme settings
        currentSettings.put("darkTheme", view.getDarkThemeRadio().isSelected());
        currentSettings.put("enableAnimations", view.getEnableAnimationsCheckBox().isSelected());
        currentSettings.put("animationSpeed", view.getAnimationSpeedSlider().getValue());
        currentSettings.put("enableSounds", view.getEnableSoundsCheckBox().isSelected());
        
        // Save to file
        dataManager.saveSettings(currentSettings);
        
        // Apply theme changes immediately
        applyThemeSettings();
        
        view.setStatusText("Settings saved successfully!");
        showAlert(Alert.AlertType.INFORMATION, "Settings Saved", 
            "All settings have been saved successfully. Email settings will be used for sending emails.");
    }
    
    private void handleTestConnection() {
        if (!validateEmailFields()) {
            return;
        }
        
        view.setStatusText("Testing email connection...");
        
        // Run connection test in background
        Task<Boolean> testTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return testEmailConnection(
                    view.getSenderEmailField().getText().trim(),
                    view.getEmailPasswordField().getText(),
                    view.getSmtpHostField().getText().trim(),
                    Integer.parseInt(view.getSmtpPortField().getText().trim()),
                    view.getEnableSSLCheckBox().isSelected(),
                    view.getEnableSTARTTLSCheckBox().isSelected()
                );
            }
        };
        
        testTask.setOnSucceeded(e -> {
            boolean success = testTask.getValue();
            Platform.runLater(() -> {
                if (success) {
                    view.setStatusText("Email connection test successful!");
                    showAlert(Alert.AlertType.INFORMATION, "Connection Test", 
                        "Email connection test was successful! Your settings are working correctly.");
                } else {
                    view.setStatusText("Email connection test failed!");
                    showAlert(Alert.AlertType.ERROR, "Connection Test Failed", 
                        "Failed to connect to the email server. Please check your settings and try again.");
                }
            });
        });
        
        testTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                Throwable exception = testTask.getException();
                view.setStatusText("Connection test failed: " + exception.getMessage());
                showAlert(Alert.AlertType.ERROR, "Connection Test Failed", 
                    "Failed to test email connection:\n" + exception.getMessage());
            });
        });
        
        Thread testThread = new Thread(testTask);
        testThread.setDaemon(true);
        testThread.start();
    }
    
    private void handleResetToDefault() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Reset Settings");
        confirmation.setHeaderText("Reset to Default Settings");
        confirmation.setContentText("Are you sure you want to reset all settings to their default values? This action cannot be undone.");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Clear current settings and load defaults
                currentSettings = dataManager.loadSettings(); // This will return defaults if no file exists
                dataManager.saveSettings(currentSettings);
                
                // Reload settings into view
                loadSettingsIntoView();
                applyThemeSettings();
                
                view.setStatusText("Settings reset to default values");
                showAlert(Alert.AlertType.INFORMATION, "Settings Reset", 
                    "All settings have been reset to their default values.");
            }
        });
    }
    
    private void handleThemeChange(boolean isDark) {
        themeManager.setDarkTheme(isDark);
        view.setStatusText("Theme changed to " + (isDark ? "dark" : "light") + " mode");
    }
    
    private boolean testEmailConnection(String senderEmail, String password, String host, 
                                       int port, boolean ssl, boolean starttls) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        
        if (ssl) {
            props.put("mail.smtp.ssl.enable", "true");
        } else if (starttls) {
            props.put("mail.smtp.starttls.enable", "true");
        }
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });
        
        // Test connection
        Transport transport = session.getTransport("smtp");
        transport.connect(host, port, senderEmail, password);
        transport.close();
        
        return true;
    }
    
    private void applyThemeSettings() {
        boolean darkTheme = (Boolean) currentSettings.getOrDefault("darkTheme", false);
        themeManager.setDarkTheme(darkTheme);
    }
    
    
    private boolean validateAllFields() {
        return validateEmailFields() && validatePortField();
    }
    
    private boolean validateEmailFields() {
        String email = view.getSenderEmailField().getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please enter a valid email address.");
            return false;
        }
        
        String host = view.getSmtpHostField().getText().trim();
        if (host.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "SMTP host cannot be empty.");
            return false;
        }
        
        return true;
    }
    
    private boolean validateEmailField() {
        String email = view.getSenderEmailField().getText().trim();
        boolean valid = email.isEmpty() || email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        
        if (!valid) {
            view.getSenderEmailField().getStyleClass().add("error");
        } else {
            view.getSenderEmailField().getStyleClass().remove("error");
        }
        
        return valid;
    }
    
    private boolean validatePortField() {
        String portText = view.getSmtpPortField().getText().trim();
        boolean valid = true;
        
        if (!portText.isEmpty()) {
            try {
                int port = Integer.parseInt(portText);
                valid = port > 0 && port <= 65535;
            } catch (NumberFormatException e) {
                valid = false;
            }
        }
        
        if (!valid) {
            view.getSmtpPortField().getStyleClass().add("error");
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                "Please enter a valid port number (1-65535).");
        } else {
            view.getSmtpPortField().getStyleClass().remove("error");
        }
        
        return valid;
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Getters
    public SettingsView getView() {
        return view;
    }
    
    public Map<String, Object> getCurrentSettings() {
        return currentSettings;
    }
    
    // Get specific setting values
    public String getSenderEmail() {
        return (String) currentSettings.getOrDefault("senderEmail", "");
    }
    
    public String getEmailPassword() {
        return (String) currentSettings.getOrDefault("emailPassword", "");
    }
    
    public String getSmtpHost() {
        return (String) currentSettings.getOrDefault("smtpHost", "smtp.gmail.com");
    }
    
    public int getSmtpPort() {
        Object portObj = currentSettings.get("smtpPort");
        if (portObj instanceof String) {
            try {
                return Integer.parseInt((String) portObj);
            } catch (NumberFormatException e) {
                return 587;
            }
        } else if (portObj instanceof Number) {
            return ((Number) portObj).intValue();
        }
        return 587;
    }
    
    public boolean isSSLEnabled() {
        return (Boolean) currentSettings.getOrDefault("enableSSL", false);
    }
    
    public boolean isSTARTTLSEnabled() {
        return (Boolean) currentSettings.getOrDefault("enableSTARTTLS", true);
    }
}
