package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.models.EmailHistory;
import org.example.models.EmailTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Data manager for JSON-based persistence of application data
 */
public class DataManager {
    
    private static final String APP_DIR = "EmailSender";
    private static final String TEMPLATES_FILE = "templates.json";
    private static final String HISTORY_FILE = "history.json";
    private static final String SETTINGS_FILE = "settings.json";
    
    private final ObjectMapper objectMapper;
    private final Path dataDirectory;
    
    public DataManager() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dataDirectory = getOrCreateDataDirectory();
    }
    
    /**
     * Get or create the application data directory
     */
    private Path getOrCreateDataDirectory() {
        String userHome = System.getProperty("user.home");
        Path appDataDir = Paths.get(userHome, APP_DIR);
        
        try {
            if (!Files.exists(appDataDir)) {
                Files.createDirectories(appDataDir);
            }
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
            // Fallback to current directory
            return Paths.get("data");
        }
        
        return appDataDir;
    }
    
    // Template persistence
    
    /**
     * Save templates to JSON file
     */
    public void saveTemplates(List<EmailTemplate> templates) {
        try {
            Path templatesFile = dataDirectory.resolve(TEMPLATES_FILE);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(templatesFile.toFile(), templates);
        } catch (IOException e) {
            System.err.println("Failed to save templates: " + e.getMessage());
        }
    }
    
    /**
     * Load templates from JSON file
     */
    public List<EmailTemplate> loadTemplates() {
        Path templatesFile = dataDirectory.resolve(TEMPLATES_FILE);
        
        if (!Files.exists(templatesFile)) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(templatesFile.toFile(), 
                new TypeReference<List<EmailTemplate>>() {});
        } catch (IOException e) {
            System.err.println("Failed to load templates: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Email history persistence
    
    /**
     * Save email history to JSON file
     */
    public void saveHistory(List<EmailHistory> history) {
        try {
            Path historyFile = dataDirectory.resolve(HISTORY_FILE);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(historyFile.toFile(), history);
        } catch (IOException e) {
            System.err.println("Failed to save history: " + e.getMessage());
        }
    }
    
    /**
     * Load email history from JSON file
     */
    public List<EmailHistory> loadHistory() {
        Path historyFile = dataDirectory.resolve(HISTORY_FILE);
        
        if (!Files.exists(historyFile)) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(historyFile.toFile(), 
                new TypeReference<List<EmailHistory>>() {});
        } catch (IOException e) {
            System.err.println("Failed to load history: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Settings persistence
    
    /**
     * Save application settings to JSON file
     */
    public void saveSettings(Map<String, Object> settings) {
        try {
            Path settingsFile = dataDirectory.resolve(SETTINGS_FILE);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(settingsFile.toFile(), settings);
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
        }
    }
    
    /**
     * Load application settings from JSON file
     */
    public Map<String, Object> loadSettings() {
        Path settingsFile = dataDirectory.resolve(SETTINGS_FILE);
        
        if (!Files.exists(settingsFile)) {
            return getDefaultSettings();
        }
        
        try {
            return objectMapper.readValue(settingsFile.toFile(), 
                new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            return getDefaultSettings();
        }
    }
    
    /**
     * Get default application settings
     */
    private Map<String, Object> getDefaultSettings() {
        Map<String, Object> defaults = new HashMap<>();
        
        // Email settings
        defaults.put("senderEmail", "");
        defaults.put("emailPassword", "");
        defaults.put("smtpHost", "smtp.gmail.com");
        defaults.put("smtpPort", "587");
        defaults.put("enableSSL", false);
        defaults.put("enableSTARTTLS", true);
        
        // Appearance settings
        defaults.put("darkTheme", false);
        defaults.put("enableAnimations", true);
        defaults.put("animationSpeed", 1.0);
        defaults.put("enableSounds", false);
        
        // General settings
        defaults.put("autoSaveDrafts", true);
        defaults.put("confirmBeforeSend", true);
        defaults.put("maxHistoryEntries", 1000);
        
        return defaults;
    }
    
    // Utility methods
    
    /**
     * Export templates to a custom file
     */
    public void exportTemplates(List<EmailTemplate> templates, File file) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, templates);
    }
    
    /**
     * Import templates from a custom file
     */
    public List<EmailTemplate> importTemplates(File file) throws IOException {
        return objectMapper.readValue(file, new TypeReference<List<EmailTemplate>>() {});
    }
    
    /**
     * Export email history to a custom file
     */
    public void exportHistory(List<EmailHistory> history, File file) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, history);
    }
    
    /**
     * Get data directory path
     */
    public Path getDataDirectory() {
        return dataDirectory;
    }
    
    /**
     * Clear all data files (for reset functionality)
     */
    public void clearAllData() {
        try {
            Files.deleteIfExists(dataDirectory.resolve(TEMPLATES_FILE));
            Files.deleteIfExists(dataDirectory.resolve(HISTORY_FILE));
            Files.deleteIfExists(dataDirectory.resolve(SETTINGS_FILE));
        } catch (IOException e) {
            System.err.println("Failed to clear data files: " + e.getMessage());
        }
    }
    
    /**
     * Backup data to a specified directory
     */
    public void backupData(Path backupDirectory) throws IOException {
        if (!Files.exists(backupDirectory)) {
            Files.createDirectories(backupDirectory);
        }
        
        // Copy all data files to backup directory
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        
        Path templatesFile = dataDirectory.resolve(TEMPLATES_FILE);
        if (Files.exists(templatesFile)) {
            Files.copy(templatesFile, backupDirectory.resolve("templates-" + timestamp + ".json"));
        }
        
        Path historyFile = dataDirectory.resolve(HISTORY_FILE);
        if (Files.exists(historyFile)) {
            Files.copy(historyFile, backupDirectory.resolve("history-" + timestamp + ".json"));
        }
        
        Path settingsFile = dataDirectory.resolve(SETTINGS_FILE);
        if (Files.exists(settingsFile)) {
            Files.copy(settingsFile, backupDirectory.resolve("settings-" + timestamp + ".json"));
        }
    }
}
