package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.models.EmailTemplate;
import org.example.utils.AnimationUtils;

/**
 * Template Management View for creating and managing email templates
 */
public class TemplateManagementView {
    
    private VBox root;
    private ListView<EmailTemplate> templatesListView;
    private ObservableList<EmailTemplate> templates;
    private TextField templateNameField;
    private ComboBox<String> categoryComboBox;
    private TextField subjectField;
    private TextArea contentArea;
    private CheckBox isHtmlCheckBox;
    private Button createNewBtn;
    private Button saveTemplateBtn;
    private Button deleteTemplateBtn;
    private Button duplicateTemplateBtn;
    private Button previewTemplateBtn;
    private Label statusLabel;
    private TextField searchField;
    private ComboBox<String> filterCategoryComboBox;
    
    // Preview components
    private VBox previewPane;
    private Label previewSubjectLabel;
    private TextArea previewContentArea;
    private Button closePreviewBtn;
    
    public TemplateManagementView() {
        templates = FXCollections.observableArrayList();
        createTemplateManagementView();
    }
    
    private void createTemplateManagementView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("template-management-container");
        
        // Header
        HBox header = createHeader();
        
        // Main content with split pane
        SplitPane splitPane = new SplitPane();
        splitPane.getStyleClass().add("modern-split-pane");
        
        // Left side - Template list and search
        VBox leftPane = createTemplateListPane();
        
        // Right side - Template editor
        VBox rightPane = createTemplateEditorPane();
        
        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.4);
        
        root.getChildren().addAll(header, splitPane);
        
        // Add entrance animations
        AnimationUtils.staggeredEntrance(header, splitPane).play();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("📄 Template Management");
        title.getStyleClass().addAll("title-2", "template-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Status label
        statusLabel = new Label("Manage your email templates");
        statusLabel.getStyleClass().addAll("body-small", "status-text");
        
        header.getChildren().addAll(title, spacer, statusLabel);
        
        return header;
    }
    
    private VBox createTemplateListPane() {
        VBox leftPane = new VBox(16);
        leftPane.getStyleClass().add("template-list-pane");
        leftPane.setPrefWidth(400);
        
        // Search and filter section
        VBox searchSection = new VBox(12);
        searchSection.getStyleClass().addAll("glass-card", "search-section");
        
        Label searchTitle = new Label("🔍 Search & Filter");
        searchTitle.getStyleClass().addAll("title-4", "section-title");
        
        // Search field
        searchField = new TextField();
        searchField.getStyleClass().add("modern-text-field");
        searchField.setPromptText("Search templates...");
        
        // Filter by category
        HBox filterRow = new HBox(12);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        
        Label filterLabel = new Label("Category:");
        filterLabel.getStyleClass().add("body-medium");
        
        filterCategoryComboBox = new ComboBox<>();
        filterCategoryComboBox.getStyleClass().add("modern-combo-box");
        filterCategoryComboBox.getItems().addAll("All Categories", "Welcome", "Marketing", "Business", "Support", "Newsletter");
        filterCategoryComboBox.setValue("All Categories");
        filterCategoryComboBox.setPrefWidth(150);
        
        filterRow.getChildren().addAll(filterLabel, filterCategoryComboBox);
        
        searchSection.getChildren().addAll(searchTitle, searchField, filterRow);
        
        // Template list
        VBox listSection = new VBox(12);
        listSection.getStyleClass().addAll("glass-card", "list-section");
        
        HBox listHeader = new HBox(12);
        listHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label listTitle = new Label("📝 Templates");
        listTitle.getStyleClass().addAll("title-4", "section-title");
        
        Region listSpacer = new Region();
        HBox.setHgrow(listSpacer, Priority.ALWAYS);
        
        createNewBtn = new Button("➕ New");
        createNewBtn.getStyleClass().addAll("btn-primary", "modern-button", "compact-button");
        
        listHeader.getChildren().addAll(listTitle, listSpacer, createNewBtn);
        
        templatesListView = new ListView<>(templates);
        templatesListView.getStyleClass().add("modern-list-view");
        templatesListView.setCellFactory(lv -> new TemplateListCell());
        templatesListView.setPrefHeight(400);
        
        listSection.getChildren().addAll(listHeader, templatesListView);
        
        leftPane.getChildren().addAll(searchSection, listSection);
        
        // Add animations
        AnimationUtils.addHoverScaleEffect(searchSection, 1.01);
        AnimationUtils.addHoverScaleEffect(listSection, 1.01);
        AnimationUtils.addHoverScaleEffect(createNewBtn);
        
        return leftPane;
    }
    
    private VBox createTemplateEditorPane() {
        VBox rightPane = new VBox(16);
        rightPane.getStyleClass().add("template-editor-pane");
        
        // Editor header
        HBox editorHeader = new HBox(12);
        editorHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label editorTitle = new Label("✏️ Template Editor");
        editorTitle.getStyleClass().addAll("title-4", "section-title");
        
        Region editorSpacer = new Region();
        HBox.setHgrow(editorSpacer, Priority.ALWAYS);
        
        // Action buttons
        HBox actionButtons = new HBox(8);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        previewTemplateBtn = new Button("👀 Preview");
        previewTemplateBtn.getStyleClass().addAll("btn-outline", "modern-button", "compact-button");
        
        duplicateTemplateBtn = new Button("📋 Duplicate");
        duplicateTemplateBtn.getStyleClass().addAll("btn-secondary", "modern-button", "compact-button");
        
        deleteTemplateBtn = new Button("🗑️ Delete");
        deleteTemplateBtn.getStyleClass().addAll("btn-ghost", "modern-button", "compact-button");
        
        actionButtons.getChildren().addAll(previewTemplateBtn, duplicateTemplateBtn, deleteTemplateBtn);
        
        editorHeader.getChildren().addAll(editorTitle, editorSpacer, actionButtons);
        
        // Editor form
        VBox editorForm = new VBox(16);
        editorForm.getStyleClass().addAll("glass-card", "editor-form");
        
        // Template name and category
        HBox nameRow = new HBox(12);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        
        VBox nameBox = new VBox(8);
        Label nameLabel = new Label("Template Name:");
        nameLabel.getStyleClass().add("field-label");
        
        templateNameField = new TextField();
        templateNameField.getStyleClass().add("modern-text-field");
        templateNameField.setPromptText("Enter template name...");
        
        nameBox.getChildren().addAll(nameLabel, templateNameField);
        
        VBox categoryBox = new VBox(8);
        Label categoryLabel = new Label("Category:");
        categoryLabel.getStyleClass().add("field-label");
        
        categoryComboBox = new ComboBox<>();
        categoryComboBox.getStyleClass().add("modern-combo-box");
        categoryComboBox.getItems().addAll("Welcome", "Marketing", "Business", "Support", "Newsletter", "Custom");
        categoryComboBox.setEditable(true);
        
        categoryBox.getChildren().addAll(categoryLabel, categoryComboBox);
        
        nameRow.getChildren().addAll(nameBox, categoryBox);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        
        // Subject field
        VBox subjectBox = new VBox(8);
        Label subjectLabel = new Label("Email Subject:");
        subjectLabel.getStyleClass().add("field-label");
        
        subjectField = new TextField();
        subjectField.getStyleClass().add("modern-text-field");
        subjectField.setPromptText("Enter email subject...");
        
        subjectBox.getChildren().addAll(subjectLabel, subjectField);
        
        // Content area
        VBox contentBox = new VBox(8);
        
        HBox contentHeader = new HBox(12);
        contentHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label contentLabel = new Label("Email Content:");
        contentLabel.getStyleClass().add("field-label");
        
        Region contentSpacer = new Region();
        HBox.setHgrow(contentSpacer, Priority.ALWAYS);
        
        isHtmlCheckBox = new CheckBox("HTML Content");
        isHtmlCheckBox.getStyleClass().add("modern-check-box");
        
        contentHeader.getChildren().addAll(contentLabel, contentSpacer, isHtmlCheckBox);
        
        contentArea = new TextArea();
        contentArea.getStyleClass().add("modern-text-area");
        contentArea.setPromptText("Enter your email content here...\\n\\nTip: Use {name}, {email}, {company} for personalization!");
        contentArea.setPrefRowCount(12);
        contentArea.setWrapText(true);
        
        // Variables help
        Label variablesHelp = new Label("📄 Available variables: {name}, {email}, {company}, {date}, {month}, {year}");
        variablesHelp.getStyleClass().addAll("body-small", "help-text");
        
        contentBox.getChildren().addAll(contentHeader, contentArea, variablesHelp);
        
        // Save button
        saveTemplateBtn = new Button("💾 Save Template");
        saveTemplateBtn.getStyleClass().addAll("btn-primary", "modern-button", "save-btn");
        
        editorForm.getChildren().addAll(nameRow, subjectBox, contentBox, saveTemplateBtn);
        
        rightPane.getChildren().addAll(editorHeader, editorForm);
        
        // Add animations
        AnimationUtils.addHoverScaleEffect(editorForm, 1.01);
        AnimationUtils.addHoverScaleEffect(previewTemplateBtn);
        AnimationUtils.addHoverScaleEffect(duplicateTemplateBtn);
        AnimationUtils.addHoverScaleEffect(deleteTemplateBtn);
        AnimationUtils.addHoverScaleEffect(saveTemplateBtn);
        
        return rightPane;
    }
    
    // Custom list cell for templates
    private class TemplateListCell extends ListCell<EmailTemplate> {
        @Override
        protected void updateItem(EmailTemplate template, boolean empty) {
            super.updateItem(template, empty);
            
            if (empty || template == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox container = new VBox(4);
                container.setPadding(new Insets(8));
                
                HBox header = new HBox(8);
                header.setAlignment(Pos.CENTER_LEFT);
                
                Label nameLabel = new Label(template.getName());
                nameLabel.getStyleClass().addAll("body-medium", "template-name");
                
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                
                Label categoryBadge = new Label(template.getCategory());
                categoryBadge.getStyleClass().addAll("badge", "category-badge");
                
                header.getChildren().addAll(nameLabel, spacer, categoryBadge);
                
                Label subjectLabel = new Label(template.getSubject());
                subjectLabel.getStyleClass().addAll("body-small", "template-subject");
                
                Label dateLabel = new Label("Created: " + template.getCreatedAt().toLocalDate().toString());
                dateLabel.getStyleClass().addAll("caption", "template-date");
                
                container.getChildren().addAll(header, subjectLabel, dateLabel);
                setGraphic(container);
                setText(null);
                
                // Add selection styling
                container.getStyleClass().add("template-list-item");
            }
        }
    }
    
    // Getters for controller access
    public VBox getRoot() { return root; }
    public ListView<EmailTemplate> getTemplatesListView() { return templatesListView; }
    public ObservableList<EmailTemplate> getTemplates() { return templates; }
    public TextField getTemplateNameField() { return templateNameField; }
    public ComboBox<String> getCategoryComboBox() { return categoryComboBox; }
    public TextField getSubjectField() { return subjectField; }
    public TextArea getContentArea() { return contentArea; }
    public CheckBox getIsHtmlCheckBox() { return isHtmlCheckBox; }
    public Button getCreateNewBtn() { return createNewBtn; }
    public Button getSaveTemplateBtn() { return saveTemplateBtn; }
    public Button getDeleteTemplateBtn() { return deleteTemplateBtn; }
    public Button getDuplicateTemplateBtn() { return duplicateTemplateBtn; }
    public Button getPreviewTemplateBtn() { return previewTemplateBtn; }
    public Label getStatusLabel() { return statusLabel; }
    public TextField getSearchField() { return searchField; }
    public ComboBox<String> getFilterCategoryComboBox() { return filterCategoryComboBox; }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
    
    public void clearEditor() {
        templateNameField.clear();
        categoryComboBox.setValue(null);
        subjectField.clear();
        contentArea.clear();
        isHtmlCheckBox.setSelected(false);
    }
    
    public void loadTemplate(EmailTemplate template) {
        if (template != null) {
            templateNameField.setText(template.getName());
            categoryComboBox.setValue(template.getCategory());
            subjectField.setText(template.getSubject());
            contentArea.setText(template.getContent());
            isHtmlCheckBox.setSelected(template.isHtml());
        }
    }
}
