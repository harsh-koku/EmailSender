package org.example.controllers;

import javafx.scene.control.Alert;
import org.example.models.EmailTemplate;
import org.example.views.TemplateManagementView;

import java.time.LocalDateTime;

/**
 * Controller for Template Management functionality
 */
public class TemplateController {
    
    private final TemplateManagementView view;
    
    public TemplateController(TemplateManagementView view) {
        this.view = view;
        initialize();
        loadSampleTemplates();
    }
    
    private void initialize() {
        // Setup event handlers
        view.getCreateNewBtn().setOnAction(e -> handleCreateNew());
        view.getSaveTemplateBtn().setOnAction(e -> handleSaveTemplate());
        view.getDeleteTemplateBtn().setOnAction(e -> handleDeleteTemplate());
        view.getDuplicateTemplateBtn().setOnAction(e -> handleDuplicateTemplate());
        view.getPreviewTemplateBtn().setOnAction(e -> handlePreviewTemplate());
        
        // Template list selection handler
        view.getTemplatesListView().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadTemplateToEditor(newVal);
            }
        });
    }
    
    private void handleCreateNew() {
        view.clearEditor();
        view.setStatusText("Ready to create new template");
    }
    
    private void handleSaveTemplate() {
        String name = view.getTemplateNameField().getText().trim();
        String subject = view.getSubjectField().getText().trim();
        String content = view.getContentArea().getText().trim();
        String category = view.getCategoryComboBox().getValue();
        boolean isHtml = view.getIsHtmlCheckBox().isSelected();
        
        // Validation
        if (name.isEmpty()) {
            showAlert("Validation Error", "Please enter a template name.");
            return;
        }
        
        if (subject.isEmpty()) {
            showAlert("Validation Error", "Please enter a subject line.");
            return;
        }
        
        if (content.isEmpty()) {
            showAlert("Validation Error", "Please enter template content.");
            return;
        }
        
        // Create new template
        EmailTemplate template = new EmailTemplate(name, subject, content, category != null ? category : "Custom", isHtml);
        
        // Check if template with same name exists
        EmailTemplate existing = view.getTemplates().stream()
            .filter(t -> t.getName().equals(name))
            .findFirst()
            .orElse(null);
        
        if (existing != null) {
            // Update existing template
            existing.setSubject(subject);
            existing.setContent(content);
            existing.setCategory(category != null ? category : "Custom");
            existing.setHtml(isHtml);
            view.setStatusText("Template updated: " + name);
        } else {
            // Add new template
            view.getTemplates().add(template);
            view.setStatusText("Template saved: " + name);
        }
        
        // Refresh the list view
        view.getTemplatesListView().refresh();
        
        showAlert("Template Saved", "Template '" + name + "' has been saved successfully!");
    }
    
    private void handleDeleteTemplate() {
        EmailTemplate selected = view.getTemplatesListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a template to delete.");
            return;
        }
        
        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Template");
        confirmDialog.setContentText("Are you sure you want to delete the template '" + selected.getName() + "'?");
        
        confirmDialog.showAndWait().ifPresent(result -> {
            if (result.getButtonData().isDefaultButton()) {
                view.getTemplates().remove(selected);
                view.clearEditor();
                view.setStatusText("Template deleted: " + selected.getName());
            }
        });
    }
    
    private void handleDuplicateTemplate() {
        EmailTemplate selected = view.getTemplatesListView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a template to duplicate.");
            return;
        }
        
        // Create duplicate
        EmailTemplate duplicate = new EmailTemplate(
            selected.getName() + " (Copy)",
            selected.getSubject(),
            selected.getContent(),
            selected.getCategory(),
            selected.isHtml()
        );
        
        view.getTemplates().add(duplicate);
        view.setStatusText("Template duplicated: " + duplicate.getName());
    }
    
    private void handlePreviewTemplate() {
        String subject = view.getSubjectField().getText().trim();
        String content = view.getContentArea().getText().trim();
        
        if (subject.isEmpty() || content.isEmpty()) {
            showAlert("Preview Error", "Please enter both subject and content to preview.");
            return;
        }
        
        // Show preview dialog (simplified)
        Alert previewDialog = new Alert(Alert.AlertType.INFORMATION);
        previewDialog.setTitle("Template Preview");
        previewDialog.setHeaderText("Subject: " + subject);
        previewDialog.setContentText(content.length() > 200 ? content.substring(0, 200) + "..." : content);
        previewDialog.getDialogPane().setPrefWidth(500);
        previewDialog.showAndWait();
        
        view.setStatusText("Template preview shown");
    }
    
    private void loadTemplateToEditor(EmailTemplate template) {
        view.loadTemplate(template);
        view.setStatusText("Loaded template: " + template.getName());
    }
    
    private void loadSampleTemplates() {
        // Add sample templates if list is empty
        if (view.getTemplates().isEmpty()) {
            view.getTemplates().addAll(
                new EmailTemplate("Welcome Email", "Welcome to Our Service!", 
                    "Dear {name},\n\nWelcome to our service! We're excited to have you on board.\n\nBest regards,\nThe Team", 
                    "Welcome", false),
                new EmailTemplate("Newsletter", "Monthly Newsletter - Latest Updates", 
                    "Hello {name},\n\nHere's our latest newsletter with exciting updates!\n\nBest regards,\nYour Team", 
                    "Newsletter", false),
                new EmailTemplate("Meeting Reminder", "Meeting Reminder - {date}", 
                    "Hi {name},\n\nThis is a friendly reminder about our upcoming meeting.\n\nSee you there!\nBest regards", 
                    "Business", false)
            );
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public TemplateManagementView getView() {
        return view;
    }
}