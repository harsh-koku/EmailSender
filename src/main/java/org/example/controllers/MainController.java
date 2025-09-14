package org.example.controllers;

import javafx.scene.control.Button;
import org.example.utils.ThemeManager;
import org.example.utils.DataManager;
import org.example.views.*;
import org.example.controllers.*;

/**
 * Main controller for handling navigation and theme management
 */
public class MainController {

    private final MainView mainView;
    private final ThemeManager themeManager;
    private final DataManager dataManager;

    // View controllers
    private DashboardController dashboardController;
    private EmailComposerController emailComposerController;
    private TemplateController templateController;
    private HistoryController historyController;
    private SettingsController settingsController;

    private Button currentActiveBtn;

    public MainController(MainView mainView, ThemeManager themeManager) {
        this.mainView = mainView;
        this.themeManager = themeManager;
        this.dataManager = new DataManager();

        initialize();
        setupNavigationHandlers();
        setupThemeToggle();

        // Show dashboard by default
        showDashboard();
    }

    private void initialize() {
        currentActiveBtn = mainView.getDashboardBtn();
    }

    private void setupNavigationHandlers() {
        mainView.getDashboardBtn().setOnAction(e -> {
            setActiveNavigation(mainView.getDashboardBtn());
            showDashboard();
        });

        mainView.getComposeBtn().setOnAction(e -> {
            setActiveNavigation(mainView.getComposeBtn());
            showCompose();
        });

        mainView.getTemplatesBtn().setOnAction(e -> {
            setActiveNavigation(mainView.getTemplatesBtn());
            showTemplates();
        });

        mainView.getHistoryBtn().setOnAction(e -> {
            setActiveNavigation(mainView.getHistoryBtn());
            showHistory();
        });

        mainView.getContactsBtn().setOnAction(e -> {
            setActiveNavigation(mainView.getContactsBtn());
            showContacts();
        });

        mainView.getSettingsBtn().setOnAction(e -> {
            setActiveNavigation(mainView.getSettingsBtn());
            showSettings();
        });
    }

    private void setupThemeToggle() {
        mainView.getThemeToggleBtn().setOnAction(e -> {
            themeManager.toggleTheme();
            updateThemeToggleIcon();
        });
        updateThemeToggleIcon();
    }

    private void updateThemeToggleIcon() {
        String icon = themeManager.isDarkTheme() ? "☀️" : "🌙";
        mainView.getThemeToggleBtn().setText(icon);
    }

    private void setActiveNavigation(Button activeBtn) {
        // Remove active class from current button
        if (currentActiveBtn != null) {
            currentActiveBtn.getStyleClass().remove("active");
        }

        // Add active class to new button
        activeBtn.getStyleClass().add("active");
        currentActiveBtn = activeBtn;
    }

    private void showDashboard() {
        if (dashboardController == null) {
            DashboardView dashboardView = new DashboardView();
            dashboardController = new DashboardController(dashboardView, this::showCompose);
        }
        mainView.setContent(dashboardController.getView().getRoot());
    }

    private void showCompose() {
        // Update navigation state to show compose as active
        setActiveNavigation(mainView.getComposeBtn());
        
        if (emailComposerController == null) {
            EmailComposerView emailComposerView = new EmailComposerView();
            emailComposerController = new EmailComposerController(emailComposerView);
        }
        mainView.setContent(emailComposerController.getView().getRoot());
    }

    private void showTemplates() {
        if (templateController == null) {
            TemplateManagementView templateView = new TemplateManagementView();
            templateController = new TemplateController(templateView);
        }
        mainView.setContent(templateController.getView().getRoot());
    }

    private void showHistory() {
        if (historyController == null) {
            ContactHistoryView historyView = new ContactHistoryView();
            historyController = new HistoryController(historyView);
        }
        mainView.setContent(historyController.getView().getRoot());
    }

    private void showContacts() {
        // Contact management is coming soon - show placeholder
        javafx.scene.layout.VBox contactsView = createPlaceholderView(
                "👥", "Contact Management",
                "Organize and manage your email contacts and distribution lists.\n\nThis feature is coming soon!"
        );
        mainView.setContent(contactsView);
    }

    private void showSettings() {
        if (settingsController == null) {
            SettingsView settingsView = new SettingsView();
            settingsController = new SettingsController(settingsView, dataManager, themeManager);
        }
        mainView.setContent(settingsController.getView().getRoot());
    }

    private javafx.scene.layout.VBox createPlaceholderView(String icon, String title, String description) {
        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(20);
        content.setAlignment(javafx.geometry.Pos.CENTER);
        content.setPadding(new javafx.geometry.Insets(60));

        // Main card
        javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(24);
        card.getStyleClass().addAll("glass-card", "placeholder-card");
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPadding(new javafx.geometry.Insets(48));
        card.setMaxWidth(500);

        // Icon
        javafx.scene.control.Label iconLabel = new javafx.scene.control.Label(icon);
        iconLabel.setStyle("-fx-font-size: 64px;");
        iconLabel.getStyleClass().add("placeholder-icon");

        // Title
        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label(title);
        titleLabel.getStyleClass().addAll("title-2", "placeholder-title");

        // Description
        javafx.scene.text.Text descText = new javafx.scene.text.Text(description);
        descText.getStyleClass().addAll("body-large", "placeholder-description");
        descText.setWrappingWidth(400);
        descText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Coming soon badge
        javafx.scene.control.Label badge = new javafx.scene.control.Label("Coming Soon");
        badge.getStyleClass().addAll("badge", "badge-info");
        badge.setStyle(
            "-fx-background-color: -fx-info; " +
            "-fx-text-fill: white; " +
            "-fx-background-radius: 20px; " +
            "-fx-padding: 8px 16px; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: 600;"
        );
        
        card.getChildren().addAll(iconLabel, titleLabel, descText, badge);
        content.getChildren().add(card);
        
        return content;
    }
}
