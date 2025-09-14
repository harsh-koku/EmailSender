package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.example.models.Contact;
import org.example.models.EmailTemplate;
import org.example.utils.AnimationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Email Composer View with file selection and rich text editing
 */
public class EmailComposerView {
    
    private VBox root;
    private TextField subjectField;
    private TextArea contentArea;
    private ListView<Contact> recipientsListView;
    private ObservableList<Contact> recipients;
    private Label fileSelectionLabel;
    private Button selectFileBtn;
    private Button addRecipientBtn;
    private Button sendEmailBtn;
    private Button saveAsDraftBtn;
    private Button loadTemplateBtn;
    private ComboBox<EmailTemplate> templateComboBox;
    private CheckBox isHtmlCheckBox;
    private ProgressBar sendingProgress;
    private Label statusLabel;
    private File selectedFile;
    private Consumer<File> fileSelectionHandler;
    
    public EmailComposerView() {
        recipients = FXCollections.observableArrayList();
        createEmailComposer();
    }
    
    private void createEmailComposer() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("composer-container");
        
        // Header
        HBox header = createHeader();
        
        // Template selection
        VBox templateSection = createTemplateSection();
        
        // Recipients section
        VBox recipientsSection = createRecipientsSection();
        
        // Subject section
        VBox subjectSection = createSubjectSection();
        
        // Content section
        VBox contentSection = createContentSection();
        
        // Actions section
        VBox actionsSection = createActionsSection();
        
        root.getChildren().addAll(
            header,
            templateSection,
            recipientsSection,
            subjectSection,
            contentSection,
            actionsSection
        );
        
        // Add entrance animations
        AnimationUtils.staggeredEntrance(
            header, templateSection, recipientsSection, 
            subjectSection, contentSection, actionsSection
        ).play();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("📝 Compose Email");
        title.getStyleClass().addAll("title-2", "composer-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Status label
        statusLabel = new Label("Ready to compose");
        statusLabel.getStyleClass().addAll("body-small", "status-text");
        
        header.getChildren().addAll(title, spacer, statusLabel);
        
        return header;
    }
    
    private VBox createTemplateSection() {
        VBox section = new VBox(12);
        section.getStyleClass().addAll("glass-card", "composer-section");
        
        Label sectionTitle = new Label("📄 Email Template");
        sectionTitle.getStyleClass().addAll("title-3", "section-title");
        
        HBox templateRow = new HBox(12);
        templateRow.setAlignment(Pos.CENTER_LEFT);
        
        templateComboBox = new ComboBox<>();
        templateComboBox.getStyleClass().add("modern-combo-box");
        templateComboBox.setPromptText("Select a template...");
        templateComboBox.setPrefWidth(250);
        
        loadTemplateBtn = new Button("Load Template");
        loadTemplateBtn.getStyleClass().addAll("btn-secondary", "modern-button");
        
        Button manageTemplatesBtn = new Button("Manage");
        manageTemplatesBtn.getStyleClass().addAll("btn-outline", "modern-button");
        
        templateRow.getChildren().addAll(templateComboBox, loadTemplateBtn, manageTemplatesBtn);
        
        section.getChildren().addAll(sectionTitle, templateRow);
        
        AnimationUtils.addHoverScaleEffect(section, 1.01);
        
        return section;
    }
    
    private VBox createRecipientsSection() {
        VBox section = new VBox(12);
        section.getStyleClass().addAll("glass-card", "composer-section");
        
        Label sectionTitle = new Label("👥 Recipients");
        sectionTitle.getStyleClass().addAll("title-3", "section-title");
        
        // File selection row
        HBox fileRow = new HBox(12);
        fileRow.setAlignment(Pos.CENTER_LEFT);
        
        selectFileBtn = new Button("📁 Select Excel/CSV File");
        selectFileBtn.getStyleClass().addAll("btn-primary", "modern-button", "file-select-btn");
        
        fileSelectionLabel = new Label("No file selected");
        fileSelectionLabel.getStyleClass().addAll("body-medium", "file-status");
        
        fileRow.getChildren().addAll(selectFileBtn, fileSelectionLabel);
        
        // Manual recipient addition
        HBox manualRow = new HBox(12);
        manualRow.setAlignment(Pos.CENTER_LEFT);
        
        TextField emailField = new TextField();
        emailField.getStyleClass().add("modern-text-field");
        emailField.setPromptText("Enter email address...");
        emailField.setPrefWidth(200);
        
        TextField nameField = new TextField();
        nameField.getStyleClass().add("modern-text-field");
        nameField.setPromptText("Enter name (optional)...");
        nameField.setPrefWidth(150);
        
        addRecipientBtn = new Button("➕ Add");
        addRecipientBtn.getStyleClass().addAll("btn-secondary", "modern-button");
        
        manualRow.getChildren().addAll(nameField, emailField, addRecipientBtn);
        
        // Recipients list
        recipientsListView = new ListView<>(recipients);
        recipientsListView.getStyleClass().add("modern-list-view");
        recipientsListView.setPrefHeight(120);
        recipientsListView.setCellFactory(lv -> new ContactListCell());
        
        // Recipients count
        Label recipientsCount = new Label("0 recipients");
        recipientsCount.getStyleClass().addAll("body-small", "recipients-count");
        
        // Bind count to list size
        recipients.addListener((javafx.beans.Observable obs) -> {
            int count = recipients.size();
            recipientsCount.setText(count + " recipient" + (count != 1 ? "s" : ""));
        });
        
        section.getChildren().addAll(
            sectionTitle, fileRow, manualRow, recipientsListView, recipientsCount
        );
        
        // Add event handlers
        selectFileBtn.setOnAction(e -> handleFileSelection());
        addRecipientBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String name = nameField.getText().trim();
            if (!email.isEmpty()) {
                addRecipient(name.isEmpty() ? email : name, email);
                emailField.clear();
                nameField.clear();
            }
        });
        
        AnimationUtils.addHoverScaleEffect(section, 1.01);
        AnimationUtils.addHoverScaleEffect(selectFileBtn);
        
        return section;
    }
    
    private VBox createSubjectSection() {
        VBox section = new VBox(12);
        section.getStyleClass().addAll("glass-card", "composer-section");
        
        Label sectionTitle = new Label("📬 Subject Line");
        sectionTitle.getStyleClass().addAll("title-3", "section-title");
        
        subjectField = new TextField();
        subjectField.getStyleClass().add("modern-text-field");
        subjectField.setPromptText("Enter email subject...");
        
        section.getChildren().addAll(sectionTitle, subjectField);
        
        AnimationUtils.addHoverScaleEffect(section, 1.01);
        
        return section;
    }
    
    private VBox createContentSection() {
        VBox section = new VBox(12);
        section.getStyleClass().addAll("glass-card", "composer-section");
        
        HBox titleRow = new HBox(12);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        
        Label sectionTitle = new Label("✏️ Email Content");
        sectionTitle.getStyleClass().addAll("title-3", "section-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        isHtmlCheckBox = new CheckBox("HTML Content");
        isHtmlCheckBox.getStyleClass().add("modern-check-box");
        isHtmlCheckBox.setSelected(false);
        
        titleRow.getChildren().addAll(sectionTitle, spacer, isHtmlCheckBox);
        
        contentArea = new TextArea();
        contentArea.getStyleClass().add("modern-text-area");
        contentArea.setPromptText("Enter your email content here...\n\nTip: Use {name} for personalized greetings!");
        contentArea.setPrefRowCount(10);
        contentArea.setWrapText(true);
        
        // Content preview
        Label previewLabel = new Label("📄 Preview Variables: {name}, {email}, {company}");
        previewLabel.getStyleClass().addAll("body-small", "preview-help");
        
        section.getChildren().addAll(titleRow, contentArea, previewLabel);
        
        AnimationUtils.addHoverScaleEffect(section, 1.01);
        
        return section;
    }
    
    private VBox createActionsSection() {
        VBox section = new VBox(16);
        section.getStyleClass().addAll("glass-card", "composer-section", "actions-section");
        
        Label sectionTitle = new Label("🚀 Actions");
        sectionTitle.getStyleClass().addAll("title-3", "section-title");
        
        // Progress bar (initially hidden)
        sendingProgress = new ProgressBar();
        sendingProgress.getStyleClass().add("modern-progress-bar");
        sendingProgress.setPrefWidth(Double.MAX_VALUE);
        sendingProgress.setVisible(false);
        
        // Action buttons
        HBox buttonsRow = new HBox(12);
        buttonsRow.setAlignment(Pos.CENTER_LEFT);
        
        sendEmailBtn = new Button("📤 Send Email");
        sendEmailBtn.getStyleClass().addAll("btn-primary", "modern-button", "send-btn");
        
        saveAsDraftBtn = new Button("💾 Save Draft");
        saveAsDraftBtn.getStyleClass().addAll("btn-secondary", "modern-button");
        
        Button previewBtn = new Button("👀 Preview");
        previewBtn.getStyleClass().addAll("btn-outline", "modern-button");
        
        Button clearBtn = new Button("🗑️ Clear All");
        clearBtn.getStyleClass().addAll("btn-ghost", "modern-button");
        
        buttonsRow.getChildren().addAll(sendEmailBtn, saveAsDraftBtn, previewBtn, clearBtn);
        
        section.getChildren().addAll(sectionTitle, sendingProgress, buttonsRow);
        
        // Add button animations
        AnimationUtils.addHoverScaleEffect(sendEmailBtn);
        AnimationUtils.addHoverScaleEffect(saveAsDraftBtn);
        AnimationUtils.addHoverScaleEffect(previewBtn);
        AnimationUtils.addHoverScaleEffect(clearBtn);
        
        // Add event handlers
        clearBtn.setOnAction(e -> clearAll());
        previewBtn.setOnAction(e -> showPreview());
        
        return section;
    }
    
    private void handleFileSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel or CSV File");
        
        // Set file filters
        FileChooser.ExtensionFilter excelFilter = 
            new FileChooser.ExtensionFilter("Excel Files (*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter csvFilter = 
            new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter allFilter = 
            new FileChooser.ExtensionFilter("All Supported Files", "*.xlsx", "*.csv");
        
        fileChooser.getExtensionFilters().addAll(allFilter, excelFilter, csvFilter);
        
        // Set initial directory to user's Documents folder
        String userHome = System.getProperty("user.home");
        File initialDir = new File(userHome, "Documents");
        if (initialDir.exists()) {
            fileChooser.setInitialDirectory(initialDir);
        }
        
        // Show file dialog
        selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        
        if (selectedFile != null) {
            fileSelectionLabel.setText("📄 " + selectedFile.getName());
            fileSelectionLabel.getStyleClass().remove("file-status");
            fileSelectionLabel.getStyleClass().add("file-selected");
            
            // Load contacts from file
            loadContactsFromFile(selectedFile);
            
            statusLabel.setText("File loaded: " + selectedFile.getName());
        }
    }
    
    private void loadContactsFromFile(File file) {
        if (fileSelectionHandler != null) {
            fileSelectionHandler.accept(file);
        } else {
            // Fallback behavior if no handler is set
            statusLabel.setText("Loading contacts from file...");
        }
    }
    
    private void addRecipient(String name, String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }
        
        Contact contact = new Contact(name, email);
        if (!recipients.contains(contact)) {
            recipients.add(contact);
            statusLabel.setText("Added recipient: " + email);
        } else {
            showAlert("Duplicate Email", "This email address is already in the recipients list.");
        }
    }
    
    private void clearAll() {
        subjectField.clear();
        contentArea.clear();
        recipients.clear();
        selectedFile = null;
        fileSelectionLabel.setText("No file selected");
        fileSelectionLabel.getStyleClass().remove("file-selected");
        fileSelectionLabel.getStyleClass().add("file-status");
        statusLabel.setText("All fields cleared");
    }
    
    private void showPreview() {
        if (subjectField.getText().trim().isEmpty() || contentArea.getText().trim().isEmpty()) {
            showAlert("Preview Error", "Please enter both subject and content to preview.");
            return;
        }
        
        // This will be implemented to show a preview dialog
        statusLabel.setText("Preview functionality coming soon!");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Custom ListCell for contacts
    private class ContactListCell extends ListCell<Contact> {
        @Override
        protected void updateItem(Contact contact, boolean empty) {
            super.updateItem(contact, empty);
            
            if (empty || contact == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox container = new HBox(10);
                container.setAlignment(Pos.CENTER_LEFT);
                
                Label emailIcon = new Label("📧");
                
                VBox info = new VBox(2);
                Label nameLabel = new Label(contact.getName());
                nameLabel.getStyleClass().add("contact-name");
                Label emailLabel = new Label(contact.getEmail());
                emailLabel.getStyleClass().addAll("body-small", "contact-email");
                
                info.getChildren().addAll(nameLabel, emailLabel);
                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                Button removeBtn = new Button("✕");
                removeBtn.getStyleClass().addAll("btn-icon", "remove-btn");
                removeBtn.setOnAction(e -> {
                    recipients.remove(contact);
                    statusLabel.setText("Removed: " + contact.getEmail());
                });
                
                container.getChildren().addAll(emailIcon, info, spacer, removeBtn);
                setGraphic(container);
                setText(null);
            }
        }
    }
    
    // Getters for controller access
    public VBox getRoot() { return root; }
    public TextField getSubjectField() { return subjectField; }
    public TextArea getContentArea() { return contentArea; }
    public ObservableList<Contact> getRecipients() { return recipients; }
    public Button getSendEmailBtn() { return sendEmailBtn; }
    public Button getSaveAsDraftBtn() { return saveAsDraftBtn; }
    public Button getLoadTemplateBtn() { return loadTemplateBtn; }
    public ComboBox<EmailTemplate> getTemplateComboBox() { return templateComboBox; }
    public CheckBox getIsHtmlCheckBox() { return isHtmlCheckBox; }
    public ProgressBar getSendingProgress() { return sendingProgress; }
    public Label getStatusLabel() { return statusLabel; }
    public File getSelectedFile() { return selectedFile; }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
    
    public void setProgress(double progress) {
        sendingProgress.setProgress(progress);
    }
    
    public void showProgress(boolean show) {
        sendingProgress.setVisible(show);
    }
    
    public void setFileSelectionHandler(Consumer<File> handler) {
        this.fileSelectionHandler = handler;
    }
}
