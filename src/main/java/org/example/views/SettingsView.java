package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.utils.AnimationUtils;

/**
 * Settings View for application configuration
 */
public class SettingsView {
    
    private VBox root;
    private TextField senderEmailField;
    private PasswordField emailPasswordField;
    private TextField smtpHostField;
    private TextField smtpPortField;
    private CheckBox enableSSLCheckBox;
    private CheckBox enableSTARTTLSCheckBox;
    private Button saveSettingsBtn;
    private Button testConnectionBtn;
    private Button resetToDefaultBtn;
    private Label statusLabel;
    private TabPane tabPane;
    
    // Theme settings
    private RadioButton lightThemeRadio;
    private RadioButton darkThemeRadio;
    private CheckBox enableAnimationsCheckBox;
    private CheckBox enableSoundsCheckBox;
    private Slider animationSpeedSlider;
    
    public SettingsView() {
        createSettingsView();
    }
    
    private void createSettingsView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("settings-container");
        
        // Header
        HBox header = createHeader();
        
        // Tab pane for different settings categories
        tabPane = new TabPane();
        tabPane.getStyleClass().add("modern-tab-pane");
        
        // Email Settings Tab
        Tab emailTab = new Tab("📧 Email Settings");
        emailTab.setClosable(false);
        emailTab.setContent(createEmailSettingsPane());
        
        // Appearance Settings Tab
        Tab appearanceTab = new Tab("🎨 Appearance");
        appearanceTab.setClosable(false);
        appearanceTab.setContent(createAppearanceSettingsPane());
        
        // General Settings Tab
        Tab generalTab = new Tab("⚙️ General");
        generalTab.setClosable(false);
        generalTab.setContent(createGeneralSettingsPane());
        
        tabPane.getTabs().addAll(emailTab, appearanceTab, generalTab);
        
        root.getChildren().addAll(header, tabPane);
        
        // Add entrance animations
        AnimationUtils.staggeredEntrance(header, tabPane).play();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("⚙️ Settings");
        title.getStyleClass().addAll("title-2", "settings-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Status label
        statusLabel = new Label("Configure your application settings");
        statusLabel.getStyleClass().addAll("body-small", "status-text");
        
        header.getChildren().addAll(title, spacer, statusLabel);
        
        return header;
    }
    
    private ScrollPane createEmailSettingsPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Email Configuration Section
        VBox emailConfig = new VBox(16);
        emailConfig.getStyleClass().addAll("glass-card", "settings-section");
        
        Label emailConfigTitle = new Label("📧 Email Configuration");
        emailConfigTitle.getStyleClass().addAll("title-3", "section-title");
        
        // Sender Email
        HBox senderEmailRow = new HBox(12);
        senderEmailRow.setAlignment(Pos.CENTER_LEFT);
        
        Label senderLabel = new Label("Sender Email:");
        senderLabel.getStyleClass().add("settings-label");
        senderLabel.setPrefWidth(150);
        
        senderEmailField = new TextField();
        senderEmailField.getStyleClass().add("modern-text-field");
        senderEmailField.setPromptText("your-email@gmail.com");
        senderEmailField.setPrefWidth(300);
        
        senderEmailRow.getChildren().addAll(senderLabel, senderEmailField);
        
        // Email Password
        HBox passwordRow = new HBox(12);
        passwordRow.setAlignment(Pos.CENTER_LEFT);
        
        Label passwordLabel = new Label("App Password:");
        passwordLabel.getStyleClass().add("settings-label");
        passwordLabel.setPrefWidth(150);
        
        emailPasswordField = new PasswordField();
        emailPasswordField.getStyleClass().add("modern-text-field");
        emailPasswordField.setPromptText("Your app password");
        emailPasswordField.setPrefWidth(300);
        
        Button showPasswordBtn = new Button("👁️");
        showPasswordBtn.getStyleClass().addAll("btn-icon", "show-password-btn");
        showPasswordBtn.setTooltip(new Tooltip("Show/Hide password"));
        
        passwordRow.getChildren().addAll(passwordLabel, emailPasswordField, showPasswordBtn);
        
        // Help text for app password
        Label helpText = new Label("💡 For Gmail, use App Password instead of regular password. Enable 2FA and generate an App Password in your Google Account settings.");
        helpText.getStyleClass().addAll("body-small", "help-text");
        helpText.setWrapText(true);
        helpText.setMaxWidth(500);
        
        emailConfig.getChildren().addAll(emailConfigTitle, senderEmailRow, passwordRow, helpText);
        
        // SMTP Configuration Section
        VBox smtpConfig = new VBox(16);
        smtpConfig.getStyleClass().addAll("glass-card", "settings-section");
        
        Label smtpConfigTitle = new Label("🌐 SMTP Configuration");
        smtpConfigTitle.getStyleClass().addAll("title-3", "section-title");
        
        // SMTP Host
        HBox hostRow = new HBox(12);
        hostRow.setAlignment(Pos.CENTER_LEFT);
        
        Label hostLabel = new Label("SMTP Host:");
        hostLabel.getStyleClass().add("settings-label");
        hostLabel.setPrefWidth(150);
        
        smtpHostField = new TextField("smtp.gmail.com");
        smtpHostField.getStyleClass().add("modern-text-field");
        smtpHostField.setPrefWidth(300);
        
        hostRow.getChildren().addAll(hostLabel, smtpHostField);
        
        // SMTP Port
        HBox portRow = new HBox(12);
        portRow.setAlignment(Pos.CENTER_LEFT);
        
        Label portLabel = new Label("SMTP Port:");
        portLabel.getStyleClass().add("settings-label");
        portLabel.setPrefWidth(150);
        
        smtpPortField = new TextField("587");
        smtpPortField.getStyleClass().add("modern-text-field");
        smtpPortField.setPrefWidth(100);
        
        portRow.getChildren().addAll(portLabel, smtpPortField);
        
        // SSL/TLS Options
        VBox securityBox = new VBox(8);
        
        enableSSLCheckBox = new CheckBox("Enable SSL");
        enableSSLCheckBox.getStyleClass().add("modern-check-box");
        
        enableSTARTTLSCheckBox = new CheckBox("Enable STARTTLS");
        enableSTARTTLSCheckBox.getStyleClass().add("modern-check-box");
        enableSTARTTLSCheckBox.setSelected(true);
        
        securityBox.getChildren().addAll(enableSSLCheckBox, enableSTARTTLSCheckBox);
        
        smtpConfig.getChildren().addAll(smtpConfigTitle, hostRow, portRow, securityBox);
        
        // Action Buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_LEFT);
        
        saveSettingsBtn = new Button("💾 Save Settings");
        saveSettingsBtn.getStyleClass().addAll("btn-primary", "modern-button");
        
        testConnectionBtn = new Button("🔍 Test Connection");
        testConnectionBtn.getStyleClass().addAll("btn-secondary", "modern-button");
        
        resetToDefaultBtn = new Button("🔄 Reset to Default");
        resetToDefaultBtn.getStyleClass().addAll("btn-outline", "modern-button");
        
        actionButtons.getChildren().addAll(saveSettingsBtn, testConnectionBtn, resetToDefaultBtn);
        
        content.getChildren().addAll(emailConfig, smtpConfig, actionButtons);
        
        // Add animations
        AnimationUtils.addHoverScaleEffect(emailConfig, 1.01);
        AnimationUtils.addHoverScaleEffect(smtpConfig, 1.01);
        AnimationUtils.addHoverScaleEffect(saveSettingsBtn);
        AnimationUtils.addHoverScaleEffect(testConnectionBtn);
        AnimationUtils.addHoverScaleEffect(resetToDefaultBtn);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("modern-scroll-pane");
        scrollPane.setFitToWidth(true);
        
        return scrollPane;
    }
    
    private ScrollPane createAppearanceSettingsPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Theme Section
        VBox themeSection = new VBox(16);
        themeSection.getStyleClass().addAll("glass-card", "settings-section");
        
        Label themeTitle = new Label("🎨 Theme Settings");
        themeTitle.getStyleClass().addAll("title-3", "section-title");
        
        ToggleGroup themeGroup = new ToggleGroup();
        
        lightThemeRadio = new RadioButton("☀️ Light Theme");
        lightThemeRadio.getStyleClass().add("modern-radio-button");
        lightThemeRadio.setToggleGroup(themeGroup);
        
        darkThemeRadio = new RadioButton("🌙 Dark Theme");
        darkThemeRadio.getStyleClass().add("modern-radio-button");
        darkThemeRadio.setToggleGroup(themeGroup);
        
        themeSection.getChildren().addAll(themeTitle, lightThemeRadio, darkThemeRadio);
        
        // Animation Settings
        VBox animationSection = new VBox(16);
        animationSection.getStyleClass().addAll("glass-card", "settings-section");
        
        Label animationTitle = new Label("✨ Animation Settings");
        animationTitle.getStyleClass().addAll("title-3", "section-title");
        
        enableAnimationsCheckBox = new CheckBox("Enable Animations");
        enableAnimationsCheckBox.getStyleClass().add("modern-check-box");
        enableAnimationsCheckBox.setSelected(true);
        
        // Animation Speed
        HBox speedRow = new HBox(12);
        speedRow.setAlignment(Pos.CENTER_LEFT);
        
        Label speedLabel = new Label("Animation Speed:");
        speedLabel.getStyleClass().add("settings-label");
        speedLabel.setPrefWidth(120);
        
        animationSpeedSlider = new Slider(0.5, 2.0, 1.0);
        animationSpeedSlider.getStyleClass().add("modern-slider");
        animationSpeedSlider.setShowTickLabels(true);
        animationSpeedSlider.setShowTickMarks(true);
        animationSpeedSlider.setMajorTickUnit(0.5);
        animationSpeedSlider.setPrefWidth(200);
        
        Label speedValueLabel = new Label("1.0x");
        speedValueLabel.getStyleClass().add("settings-value-label");
        
        speedRow.getChildren().addAll(speedLabel, animationSpeedSlider, speedValueLabel);
        
        animationSection.getChildren().addAll(animationTitle, enableAnimationsCheckBox, speedRow);
        
        content.getChildren().addAll(themeSection, animationSection);
        
        AnimationUtils.addHoverScaleEffect(themeSection, 1.01);
        AnimationUtils.addHoverScaleEffect(animationSection, 1.01);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("modern-scroll-pane");
        scrollPane.setFitToWidth(true);
        
        return scrollPane;
    }
    
    private ScrollPane createGeneralSettingsPane() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // General Preferences
        VBox generalSection = new VBox(16);
        generalSection.getStyleClass().addAll("glass-card", "settings-section");
        
        Label generalTitle = new Label("⚙️ General Preferences");
        generalTitle.getStyleClass().addAll("title-3", "section-title");
        
        enableSoundsCheckBox = new CheckBox("Enable Sound Effects");
        enableSoundsCheckBox.getStyleClass().add("modern-check-box");
        
        CheckBox autoSaveDraftsCheckBox = new CheckBox("Auto-save drafts");
        autoSaveDraftsCheckBox.getStyleClass().add("modern-check-box");
        autoSaveDraftsCheckBox.setSelected(true);
        
        CheckBox confirmBeforeSendCheckBox = new CheckBox("Confirm before sending emails");
        confirmBeforeSendCheckBox.getStyleClass().add("modern-check-box");
        confirmBeforeSendCheckBox.setSelected(true);
        
        generalSection.getChildren().addAll(generalTitle, enableSoundsCheckBox, autoSaveDraftsCheckBox, confirmBeforeSendCheckBox);
        
        // About Section
        VBox aboutSection = new VBox(16);
        aboutSection.getStyleClass().addAll("glass-card", "settings-section");
        
        Label aboutTitle = new Label("ℹ️ About");
        aboutTitle.getStyleClass().addAll("title-3", "section-title");
        
        Label appNameLabel = new Label("Email Sender Pro");
        appNameLabel.getStyleClass().addAll("title-4", "app-name");
        
        Label versionLabel = new Label("Version 1.0.0");
        versionLabel.getStyleClass().add("body-medium");
        
        Label descriptionLabel = new Label("A modern, professional email sender application with advanced features and beautiful UI.");
        descriptionLabel.getStyleClass().addAll("body-medium", "app-description");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(400);
        
        aboutSection.getChildren().addAll(aboutTitle, appNameLabel, versionLabel, descriptionLabel);
        
        content.getChildren().addAll(generalSection, aboutSection);
        
        AnimationUtils.addHoverScaleEffect(generalSection, 1.01);
        AnimationUtils.addHoverScaleEffect(aboutSection, 1.01);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("modern-scroll-pane");
        scrollPane.setFitToWidth(true);
        
        return scrollPane;
    }
    
    // Getters for controller access
    public VBox getRoot() { return root; }
    public TextField getSenderEmailField() { return senderEmailField; }
    public PasswordField getEmailPasswordField() { return emailPasswordField; }
    public TextField getSmtpHostField() { return smtpHostField; }
    public TextField getSmtpPortField() { return smtpPortField; }
    public CheckBox getEnableSSLCheckBox() { return enableSSLCheckBox; }
    public CheckBox getEnableSTARTTLSCheckBox() { return enableSTARTTLSCheckBox; }
    public Button getSaveSettingsBtn() { return saveSettingsBtn; }
    public Button getTestConnectionBtn() { return testConnectionBtn; }
    public Button getResetToDefaultBtn() { return resetToDefaultBtn; }
    public Label getStatusLabel() { return statusLabel; }
    public RadioButton getLightThemeRadio() { return lightThemeRadio; }
    public RadioButton getDarkThemeRadio() { return darkThemeRadio; }
    public CheckBox getEnableAnimationsCheckBox() { return enableAnimationsCheckBox; }
    public CheckBox getEnableSoundsCheckBox() { return enableSoundsCheckBox; }
    public Slider getAnimationSpeedSlider() { return animationSpeedSlider; }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
}
