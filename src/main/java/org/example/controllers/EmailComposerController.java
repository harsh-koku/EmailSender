package org.example.controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.example.CsvReader;
import org.example.ExcelReader;
import org.example.MailSender;
import org.example.models.Contact;
import org.example.models.EmailHistory;
import org.example.models.EmailTemplate;
import org.example.utils.DataManager;
import org.example.views.EmailComposerView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for Email Composer functionality
 */
public class EmailComposerController {
    
    private final EmailComposerView view;
    private final List<EmailTemplate> templates;
    private final List<EmailHistory> emailHistory;
    private final DataManager dataManager;
    
    public EmailComposerController(EmailComposerView view) {
        this.view = view;
        this.templates = new ArrayList<>();
        this.emailHistory = new ArrayList<>();
        this.dataManager = new DataManager();
        initialize();
        loadSampleTemplates();
    }
    
    private void initialize() {
        // Setup event handlers
        view.getSendEmailBtn().setOnAction(e -> handleSendEmail());
        view.getSaveAsDraftBtn().setOnAction(e -> handleSaveDraft());
        view.getLoadTemplateBtn().setOnAction(e -> handleLoadTemplate());
        
        // File selection is already handled in the view
        // We just need to override the loadContactsFromFile method
        setupFileLoading();
        
        // Populate template combo box
        view.getTemplateComboBox().getItems().setAll(templates);
    }
    
    private void setupFileLoading() {
        // Set the file selection handler on the view
        view.setFileSelectionHandler(this::loadContactsFromFile);
    }
    
    public void loadContactsFromFile(File file) {
        if (file == null) return;
        
        Task<List<Contact>> loadTask = new Task<List<Contact>>() {
            @Override
            protected List<Contact> call() throws Exception {
                List<Contact> contacts = new ArrayList<>();
                
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".xlsx")) {
                    // Load from Excel
                    List<Map<String, String>> data = ExcelReader.readExcel(file.getAbsolutePath());
                    
                    for (Map<String, String> row : data) {
                        String name = row.get("name");
                        String email = row.get("email");
                        
                        if (email != null && !email.isEmpty()) {
                            // Validate email
                            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                                contacts.add(new Contact(name != null && !name.isEmpty() ? name : email, email));
                            }
                        }
                    }
                } else if (fileName.endsWith(".csv")) {
                    // Load from CSV
                    List<Map<String, String>> data = CsvReader.readCsv(file.getAbsolutePath());
                    
                    for (Map<String, String> row : data) {
                        // Try common column names for name and email
                        String name = row.get("name");
                        if (name == null) name = row.get("Name");
                        if (name == null) name = row.get("NAME");
                        
                        String email = row.get("email");
                        if (email == null) email = row.get("Email");
                        if (email == null) email = row.get("EMAIL");
                        if (email == null) email = row.get("e-mail");
                        if (email == null) email = row.get("E-mail");
                        
                        if (email != null && !email.isEmpty()) {
                            // Validate email
                            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                                contacts.add(new Contact(name != null && !name.isEmpty() ? name : email, email));
                            }
                        }
                    }
                }
                
                return contacts;
            }
        };
        
        loadTask.setOnSucceeded(e -> {
            List<Contact> loadedContacts = loadTask.getValue();
            Platform.runLater(() -> {
                view.getRecipients().clear();
                view.getRecipients().addAll(loadedContacts);
                view.setStatusText(String.format("Loaded %d contacts from %s", 
                    loadedContacts.size(), file.getName()));
            });
        });
        
        loadTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                Throwable exception = loadTask.getException();
                showAlert("File Loading Error", 
                    "Failed to load contacts from file: " + exception.getMessage());
                view.setStatusText("Failed to load file: " + exception.getMessage());
            });
        });
        
        // Show loading status
        view.setStatusText("Loading contacts from file...");
        
        // Run task in background thread
        Thread loadThread = new Thread(loadTask);
        loadThread.setDaemon(true);
        loadThread.start();
    }
    
    private void handleSendEmail() {
        // Validate input
        String subject = view.getSubjectField().getText().trim();
        String content = view.getContentArea().getText().trim();
        List<Contact> recipients = new ArrayList<>(view.getRecipients());
        
        if (subject.isEmpty()) {
            showAlert("Validation Error", "Please enter an email subject.");
            return;
        }
        
        if (content.isEmpty()) {
            showAlert("Validation Error", "Please enter email content.");
            return;
        }
        
        if (recipients.isEmpty()) {
            showAlert("Validation Error", "Please add at least one recipient.");
            return;
        }
        
        // Get email credentials from settings only
        String senderEmail = getEmailCredentialFromSettings("senderEmail");
        String emailPassword = getEmailCredentialFromSettings("emailPassword");
        
        if (senderEmail == null || senderEmail.isEmpty() || emailPassword == null || emailPassword.isEmpty()) {
            showAlert("Configuration Error", 
                "Email credentials not configured.\n\n" +
                "Please configure your email settings in the Settings tab.\n\n" +
                "For Gmail: Enable 2FA and use an App Password");
            return;
        }
        
        // Create email history entry
        List<String> recipientEmails = recipients.stream()
            .map(Contact::getEmail)
            .toList();
        
        EmailHistory history = new EmailHistory(subject, content, recipientEmails, senderEmail);
        history.setStatus(EmailHistory.Status.SENT);
        history.setSentAt(LocalDateTime.now());
        
        // Start sending process
        sendEmailsAsync(subject, content, recipients, history, senderEmail, emailPassword);
    }
    
    private void sendEmailsAsync(String subject, String content, List<Contact> recipients, EmailHistory history, String senderEmail, String emailPassword) {
        view.showProgress(true);
        view.setProgress(0.0);
        view.setStatusText("Sending emails...");
        
        Task<Void> sendTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int total = recipients.size();
                int successful = 0;
                int failed = 0;
                
                for (int i = 0; i < recipients.size(); i++) {
                    Contact contact = recipients.get(i);
                    final int currentIndex = i; // Make a final copy for lambda
                    
                    try {
                        // Personalize content
                        String personalizedContent = personalizeContent(content, contact);
                        
                        // Get email settings
                        var settings = dataManager.loadSettings();
                        String smtpHost = (String) settings.get("smtpHost");
                        String smtpPort = (String) settings.get("smtpPort");
                        boolean enableSSL = (Boolean) settings.get("enableSSL");
                        boolean enableSTARTTLS = (Boolean) settings.get("enableSTARTTLS");
                        
                        // Send email using static method with settings
                        MailSender.sendMail(contact.getEmail(), subject, personalizedContent, 
                                          senderEmail, emailPassword, smtpHost, smtpPort, enableSSL, enableSTARTTLS);
                        successful++;
                        
                        Platform.runLater(() -> {
                            view.setStatusText(String.format("Sent to %s (%d/%d)", 
                                contact.getEmail(), currentIndex + 1, total));
                        });
                        
                    } catch (Exception e) {
                        failed++;
                        System.err.println("Failed to send to " + contact.getEmail() + ": " + e.getMessage());
                    }
                    
                    // Update progress
                    double progress = (double) (currentIndex + 1) / total;
                    Platform.runLater(() -> view.setProgress(progress));
                    
                    // Small delay to avoid overwhelming the mail server
                    Thread.sleep(1000);
                }
                
                // Update history
                history.setSuccessfulDeliveries(successful);
                history.setFailedDeliveries(failed);
                if (successful > 0) {
                    history.setStatus(EmailHistory.Status.DELIVERED);
                    history.setDeliveredAt(LocalDateTime.now());
                }
                
                return null;
            }
        };
        
        sendTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                view.showProgress(false);
                int successful = history.getSuccessfulDeliveries();
                int failed = history.getFailedDeliveries();
                
                view.setStatusText(String.format("Email sending completed! Success: %d, Failed: %d", 
                    successful, failed));
                
                // Add to history
                emailHistory.add(history);
                
                // Show completion dialog
                showAlert("Email Sent", 
                    String.format("Email sending completed!\n\nSuccessful: %d\nFailed: %d\nSuccess Rate: %.1f%%",
                        successful, failed, history.getSuccessRate()));
            });
        });
        
        sendTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                view.showProgress(false);
                Throwable exception = sendTask.getException();
                view.setStatusText("Email sending failed: " + exception.getMessage());
                showAlert("Sending Error", "Failed to send emails: " + exception.getMessage());
            });
        });
        
        // Run in background thread
        Thread sendThread = new Thread(sendTask);
        sendThread.setDaemon(true);
        sendThread.start();
    }
    
    private String personalizeContent(String content, Contact contact) {
        String personalized = content;
        
        // Replace placeholders
        personalized = personalized.replace("{name}", contact.getName() != null ? contact.getName() : "Valued Customer");
        personalized = personalized.replace("{email}", contact.getEmail());
        personalized = personalized.replace("{company}", contact.getCompany() != null ? contact.getCompany() : "your organization");
        
        return personalized;
    }
    
    private void handleSaveDraft() {
        String subject = view.getSubjectField().getText().trim();
        String content = view.getContentArea().getText().trim();
        
        if (subject.isEmpty() && content.isEmpty()) {
            showAlert("Save Draft", "Nothing to save. Please enter subject or content.");
            return;
        }
        
        // Create draft entry
        List<String> recipientEmails = view.getRecipients().stream()
            .map(Contact::getEmail)
            .toList();
        
        EmailHistory draft = new EmailHistory(subject, content, recipientEmails, 
            getEmailCredentialFromSettings("senderEmail"));
        draft.setStatus(EmailHistory.Status.DRAFT);
        
        emailHistory.add(draft);
        
        view.setStatusText("Draft saved successfully!");
        showAlert("Draft Saved", "Your email draft has been saved successfully.");
    }
    
    private void handleLoadTemplate() {
        EmailTemplate selected = view.getTemplateComboBox().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Template Selected", "Please select a template to load.");
            return;
        }
        
        // Load template into fields
        view.getSubjectField().setText(selected.getSubject());
        view.getContentArea().setText(selected.getContent());
        view.getIsHtmlCheckBox().setSelected(selected.isHtml());
        
        view.setStatusText("Template loaded: " + selected.getName());
    }
    
    private void loadSampleTemplates() {
        // Add some sample templates
        templates.add(new EmailTemplate(
            "Welcome Email",
            "Welcome to {company}!",
            "Dear {name},\n\nWelcome to our service! We're excited to have you on board.\n\nBest regards,\nThe Team",
            "Welcome",
            false
        ));
        
        templates.add(new EmailTemplate(
            "Newsletter",
            "Monthly Newsletter - {month}",
            "Hello {name},\n\nHere's our latest newsletter with exciting updates and news!\n\nBest regards,\n{company}",
            "Marketing",
            false
        ));
        
        templates.add(new EmailTemplate(
            "Meeting Reminder",
            "Meeting Reminder - {date}",
            "Hi {name},\n\nThis is a friendly reminder about our upcoming meeting.\n\nSee you there!\nBest regards",
            "Business",
            false
        ));
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Get email credential from settings file only
     */
    private String getEmailCredentialFromSettings(String settingsKey) {
        try {
            var settings = dataManager.loadSettings();
            if (settings.containsKey(settingsKey)) {
                String settingsValue = settings.get(settingsKey).toString();
                if (settingsValue != null && !settingsValue.trim().isEmpty()) {
                    return settingsValue.trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading settings: " + e.getMessage());
        }
        
        return null;
    }
    
    // Getters for accessing data from other controllers
    public List<EmailTemplate> getTemplates() {
        return templates;
    }
    
    public List<EmailHistory> getEmailHistory() {
        return emailHistory;
    }
    
    public EmailComposerView getView() {
        return view;
    }
}
