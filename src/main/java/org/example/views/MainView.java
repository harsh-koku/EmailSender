package org.example.views;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.utils.AnimationUtils;

/**
 * Main view with modern sidebar navigation and content area
 */
public class MainView {
    
    private BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;
    private HBox headerBar;
    private Button themeToggleBtn;
    private Button sidebarToggleBtn;
    
    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_WIDTH_EXPANDED = 240;
    private static final double SIDEBAR_WIDTH_COLLAPSED = 60;
    
    // Navigation items
    private Button dashboardBtn;
    private Button composeBtn;
    private Button templatesBtn;
    private Button historyBtn;
    private Button contactsBtn;
    private Button settingsBtn;
    
    public MainView() {
        createLayout();
        setupSidebar();
        setupHeaderBar();
        setupContentArea();
        applyStyles();
    }
    
    private void createLayout() {
        root = new BorderPane();
        root.getStyleClass().add("main-container");
        
        // Create main sections
        sidebar = new VBox();
        contentArea = new StackPane();
        headerBar = new HBox();
        
        // Setup layout
        root.setLeft(sidebar);
        root.setCenter(contentArea);
        root.setTop(headerBar);
    }
    
    private void setupSidebar() {
        sidebar.getStyleClass().addAll("sidebar", "sidebar-expanded");
        sidebar.setPrefWidth(SIDEBAR_WIDTH_EXPANDED);
        sidebar.setMinWidth(SIDEBAR_WIDTH_EXPANDED);
        sidebar.setMaxWidth(SIDEBAR_WIDTH_EXPANDED);
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(8);
        
        // App title/logo
        Label appTitle = new Label("Email Sender");
        appTitle.getStyleClass().addAll("title-2", "app-title");
        appTitle.setPadding(new Insets(0, 0, 20, 0));
        
        // Navigation items
        dashboardBtn = createNavigationItem("📊", "Dashboard", true);
        composeBtn = createNavigationItem("✏️", "Compose", false);
        templatesBtn = createNavigationItem("📄", "Templates", false);
        historyBtn = createNavigationItem("📋", "History", false);
        contactsBtn = createNavigationItem("👥", "Contacts", false);
        
        // Spacer to push settings to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        settingsBtn = createNavigationItem("⚙️", "Settings", false);
        
        sidebar.getChildren().addAll(
            appTitle,
            dashboardBtn,
            composeBtn,
            templatesBtn,
            historyBtn,
            contactsBtn,
            spacer,
            settingsBtn
        );
    }
    
    private Button createNavigationItem(String icon, String text, boolean active) {
        Button btn = new Button();
        
        // Create content layout
        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().addAll("navigation-icon", "icon");
        iconLabel.setMinWidth(24);
        iconLabel.setPrefWidth(24);
        iconLabel.setMaxWidth(24);
        
        // Text
        Label textLabel = new Label(text);
        textLabel.getStyleClass().addAll("navigation-text", "text", "body-medium");
        
        content.getChildren().addAll(iconLabel, textLabel);
        btn.setGraphic(content);
        btn.setText("");
        
        // Styling
        btn.getStyleClass().addAll("navigation-item", "modern-button");
        if (active) {
            btn.getStyleClass().add("active");
        }
        
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        
        // Add hover animations
        AnimationUtils.addHoverScaleEffect(btn, 1.02);
        
        return btn;
    }
    
    private void setupHeaderBar() {
        headerBar.getStyleClass().add("header-bar");
        headerBar.setAlignment(Pos.CENTER_LEFT);
        headerBar.setPadding(new Insets(12, 20, 12, 20));
        headerBar.setSpacing(12);
        
        // Sidebar toggle button
        sidebarToggleBtn = new Button("☰");
        sidebarToggleBtn.getStyleClass().addAll("btn-icon", "sidebar-toggle");
        sidebarToggleBtn.setOnAction(e -> toggleSidebar());
        AnimationUtils.addHoverRotateEffect(sidebarToggleBtn, 90);
        
        // Page title (will be updated by controller)
        Label pageTitle = new Label("Dashboard");
        pageTitle.getStyleClass().addAll("title-3", "page-title");
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Theme toggle button
        themeToggleBtn = new Button("🌙");
        themeToggleBtn.getStyleClass().addAll("btn-icon", "theme-toggle");
        AnimationUtils.addHoverScaleEffect(themeToggleBtn, 1.1);
        
        // User profile button
        Button profileBtn = new Button("👤");
        profileBtn.getStyleClass().addAll("btn-icon", "profile-btn");
        AnimationUtils.addHoverScaleEffect(profileBtn);
        
        headerBar.getChildren().addAll(
            sidebarToggleBtn,
            pageTitle,
            spacer,
            themeToggleBtn,
            profileBtn
        );
    }
    
    private void setupContentArea() {
        contentArea.getStyleClass().add("content-area");
        contentArea.setPadding(new Insets(20));
        
        // Create scroll pane for content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("content-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Initial content (will be replaced by controller)
        VBox welcomeContent = createWelcomeContent();
        scrollPane.setContent(welcomeContent);
        
        contentArea.getChildren().add(scrollPane);
    }
    
    private VBox createWelcomeContent() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(40));
        
        // Welcome card
        VBox welcomeCard = new VBox(16);
        welcomeCard.getStyleClass().addAll("glass-card", "welcome-card");
        welcomeCard.setAlignment(Pos.CENTER);
        welcomeCard.setPadding(new Insets(40));
        welcomeCard.setMaxWidth(600);
        
        Label welcomeTitle = new Label("Welcome to Email Sender Pro");
        welcomeTitle.getStyleClass().addAll("title-1", "welcome-title");
        
        Text welcomeText = new Text("Experience a modern, professional email management interface with " +
            "smooth animations, responsive design, and intuitive navigation.");
        welcomeText.getStyleClass().addAll("body-large", "welcome-description");
        welcomeText.setWrappingWidth(500);
        
        Button getStartedBtn = new Button("Get Started");
        getStartedBtn.getStyleClass().addAll("btn-primary", "modern-button", "btn-glow");
        getStartedBtn.setPrefWidth(200);
        AnimationUtils.addHoverScaleEffect(getStartedBtn);
        
        welcomeCard.getChildren().addAll(welcomeTitle, welcomeText, getStartedBtn);
        
        // Feature cards
        HBox featureCards = new HBox(20);
        featureCards.setAlignment(Pos.CENTER);
        
        VBox feature1 = createFeatureCard("🎨", "Modern Design", 
            "Beautiful glassmorphism UI with smooth animations");
        VBox feature2 = createFeatureCard("🌓", "Dark/Light Theme", 
            "Toggle between themes with instant switching");
        VBox feature3 = createFeatureCard("📱", "Responsive", 
            "Adaptive layout that works on any screen size");
        
        featureCards.getChildren().addAll(feature1, feature2, feature3);
        
        content.getChildren().addAll(welcomeCard, featureCards);
        
        // Add entrance animations
        AnimationUtils.entranceAnimation(welcomeCard).play();
        
        return content;
    }
    
    private VBox createFeatureCard(String icon, String title, String description) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("glass-card", "glass-card-hover", "feature-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24));
        card.setPrefWidth(180);
        card.setMaxWidth(180);
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("feature-icon");
        iconLabel.setStyle("-fx-font-size: 32px;");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("title-3", "feature-title");
        
        Text descText = new Text(description);
        descText.getStyleClass().addAll("body-small", "feature-description");
        descText.setWrappingWidth(140);
        
        card.getChildren().addAll(iconLabel, titleLabel, descText);
        
        AnimationUtils.addHoverScaleEffect(card, 1.03);
        
        return card;
    }
    
    private void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        
        double targetWidth = sidebarCollapsed ? SIDEBAR_WIDTH_COLLAPSED : SIDEBAR_WIDTH_EXPANDED;
        
        // Animate sidebar width
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);
        
        // Update sidebar styling
        if (sidebarCollapsed) {
            sidebar.getStyleClass().remove("sidebar-expanded");
            sidebar.getStyleClass().add("sidebar-collapsed");
            sidebar.setPrefWidth(SIDEBAR_WIDTH_COLLAPSED);
            sidebar.setMinWidth(SIDEBAR_WIDTH_COLLAPSED);
            sidebar.setMaxWidth(SIDEBAR_WIDTH_COLLAPSED);
        } else {
            sidebar.getStyleClass().remove("sidebar-collapsed");
            sidebar.getStyleClass().add("sidebar-expanded");
            sidebar.setPrefWidth(SIDEBAR_WIDTH_EXPANDED);
            sidebar.setMinWidth(SIDEBAR_WIDTH_EXPANDED);
            sidebar.setMaxWidth(SIDEBAR_WIDTH_EXPANDED);
        }
        
        // Animate text opacity
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                HBox content = (HBox) btn.getGraphic();
                if (content != null && content.getChildren().size() > 1) {
                    Node textNode = content.getChildren().get(1);
                    if (sidebarCollapsed) {
                        AnimationUtils.fadeOut(textNode).play();
                    } else {
                        AnimationUtils.fadeIn(textNode).play();
                    }
                }
            }
        });
    }
    
    private void applyStyles() {
        // Apply CSS classes and setup initial animations
        root.getStyleClass().add("main-view");
        
        // Add entrance animations for sidebar items
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.setOpacity(0);
                AnimationUtils.fadeIn(node).play();
            }
        });
    }
    
    // Getters for controller access
    public BorderPane getRoot() {
        return root;
    }
    
    public StackPane getContentArea() {
        return contentArea;
    }
    
    public Button getThemeToggleBtn() {
        return themeToggleBtn;
    }
    
    public Button getDashboardBtn() {
        return dashboardBtn;
    }
    
    public Button getComposeBtn() {
        return composeBtn;
    }
    
    public Button getTemplatesBtn() {
        return templatesBtn;
    }
    
    public Button getHistoryBtn() {
        return historyBtn;
    }
    
    public Button getContactsBtn() {
        return contactsBtn;
    }
    
    public Button getSettingsBtn() {
        return settingsBtn;
    }
    
    public void setContent(Node content) {
        contentArea.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("content-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        contentArea.getChildren().add(scrollPane);
        
        // Add entrance animation
        content.setOpacity(0);
        AnimationUtils.fadeIn(content).play();
    }
}
