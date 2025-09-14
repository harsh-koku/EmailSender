package org.example.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import org.example.utils.AnimationUtils;

/**
 * Modern dashboard view with animated cards, charts, and metrics
 */
public class DashboardView {
    
    private VBox root;
    
    public DashboardView() {
        createDashboard();
    }
    
    private void createDashboard() {
        root = new VBox(24);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dashboard-container");
        
        // Dashboard header
        HBox header = createDashboardHeader();
        
        // Metrics cards row
        HBox metricsRow = createMetricsCards();
        
        // Charts row
        HBox chartsRow = createChartsRow();
        
        // Activity section
        VBox activitySection = createActivitySection();
        
        root.getChildren().addAll(header, metricsRow, chartsRow, activitySection);
        
        // Add staggered entrance animations
        AnimationUtils.staggeredEntrance(header, metricsRow, chartsRow, activitySection).play();
    }
    
    private HBox createDashboardHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleSection = new VBox(4);
        
        Label title = new Label("Dashboard Overview");
        title.getStyleClass().addAll("title-2", "dashboard-title");
        
        Label subtitle = new Label("Monitor your email campaign performance");
        subtitle.getStyleClass().addAll("body-medium", "dashboard-subtitle");
        
        titleSection.getChildren().addAll(title, subtitle);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Quick action buttons
        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);
        
        javafx.scene.control.Button composeBtn = new javafx.scene.control.Button("📝 New Email");
        composeBtn.getStyleClass().addAll("btn-primary", "modern-button");
        
        javafx.scene.control.Button refreshBtn = new javafx.scene.control.Button("🔄 Refresh");
        refreshBtn.getStyleClass().addAll("btn-secondary", "modern-button");
        
        actions.getChildren().addAll(composeBtn, refreshBtn);
        
        AnimationUtils.addHoverScaleEffect(composeBtn);
        AnimationUtils.addHoverScaleEffect(refreshBtn);
        
        header.getChildren().addAll(titleSection, spacer, actions);
        
        return header;
    }
    
    private HBox createMetricsCards() {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);
        
        VBox emailsSent = createMetricCard("📤", "Emails Sent", "1,247", "+12%", true);
        VBox deliveryRate = createMetricCard("✅", "Delivery Rate", "98.5%", "+0.8%", true);
        VBox openRate = createMetricCard("👀", "Open Rate", "24.3%", "+2.1%", true);
        VBox clickRate = createMetricCard("🖱️", "Click Rate", "8.7%", "-0.5%", false);
        
        row.getChildren().addAll(emailsSent, deliveryRate, openRate, clickRate);
        
        // Make cards grow equally
        emailsSent.setMaxWidth(Double.MAX_VALUE);
        deliveryRate.setMaxWidth(Double.MAX_VALUE);
        openRate.setMaxWidth(Double.MAX_VALUE);
        clickRate.setMaxWidth(Double.MAX_VALUE);
        
        HBox.setHgrow(emailsSent, Priority.ALWAYS);
        HBox.setHgrow(deliveryRate, Priority.ALWAYS);
        HBox.setHgrow(openRate, Priority.ALWAYS);
        HBox.setHgrow(clickRate, Priority.ALWAYS);
        
        return row;
    }
    
    private VBox createMetricCard(String icon, String title, String value, String change, boolean isPositive) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("dashboard-card", "metric-card");
        card.setPadding(new Insets(24));
        card.setAlignment(Pos.TOP_LEFT);
        
        // Header with icon and title
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("metric-icon");
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("body-medium", "metric-title");
        
        header.getChildren().addAll(iconLabel, titleLabel);
        
        // Value
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("title-1", "metric-value");
        
        // Change indicator
        HBox changeBox = new HBox(8);
        changeBox.setAlignment(Pos.CENTER_LEFT);
        
        String changeIcon = isPositive ? "📈" : "📉";
        String changeClass = isPositive ? "metric-change-positive" : "metric-change-negative";
        
        Label changeIconLabel = new Label(changeIcon);
        changeIconLabel.setStyle("-fx-font-size: 14px;");
        
        Label changeLabel = new Label(change);
        changeLabel.getStyleClass().addAll("body-small", changeClass);
        
        changeBox.getChildren().addAll(changeIconLabel, changeLabel);
        
        card.getChildren().addAll(header, valueLabel, changeBox);
        
        // Add hover effects
        AnimationUtils.addHoverScaleEffect(card, 1.02);
        
        return card;
    }
    
    private HBox createChartsRow() {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);
        row.setPrefHeight(300);
        
        // Email trends chart
        VBox trendsCard = createEmailTrendsChart();
        
        // Email types pie chart
        VBox typesCard = createEmailTypesChart();
        
        row.getChildren().addAll(trendsCard, typesCard);
        
        HBox.setHgrow(trendsCard, Priority.ALWAYS);
        HBox.setHgrow(typesCard, Priority.ALWAYS);
        
        return row;
    }
    
    private VBox createEmailTrendsChart() {
        VBox card = new VBox(16);
        card.getStyleClass().addAll("dashboard-card", "chart-card");
        card.setPadding(new Insets(24));
        
        // Chart title
        Label title = new Label("Email Trends (Last 7 Days)");
        title.getStyleClass().addAll("title-3", "chart-title");
        
        // Create area chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        yAxis.setLabel("Emails");
        
        AreaChart<Number, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle("Email Activity");
        chart.setLegendVisible(false);
        chart.getStyleClass().add("email-trends-chart");
        chart.setPrefHeight(200);
        
        // Sample data
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 120));
        series.getData().add(new XYChart.Data<>(2, 145));
        series.getData().add(new XYChart.Data<>(3, 167));
        series.getData().add(new XYChart.Data<>(4, 134));
        series.getData().add(new XYChart.Data<>(5, 198));
        series.getData().add(new XYChart.Data<>(6, 176));
        series.getData().add(new XYChart.Data<>(7, 203));
        
        chart.getData().add(series);
        
        card.getChildren().addAll(title, chart);
        
        AnimationUtils.addHoverScaleEffect(card, 1.01);
        
        return card;
    }
    
    private VBox createEmailTypesChart() {
        VBox card = new VBox(16);
        card.getStyleClass().addAll("dashboard-card", "chart-card");
        card.setPadding(new Insets(24));
        
        // Chart title
        Label title = new Label("Email Types Distribution");
        title.getStyleClass().addAll("title-3", "chart-title");
        
        // Create pie chart
        PieChart chart = new PieChart();
        chart.getData().addAll(
            new PieChart.Data("Marketing", 45),
            new PieChart.Data("Transactional", 30),
            new PieChart.Data("Newsletters", 20),
            new PieChart.Data("Others", 5)
        );
        
        chart.setLegendVisible(true);
        chart.getStyleClass().add("email-types-chart");
        chart.setPrefHeight(200);
        
        card.getChildren().addAll(title, chart);
        
        AnimationUtils.addHoverScaleEffect(card, 1.01);
        
        return card;
    }
    
    private VBox createActivitySection() {
        VBox section = new VBox(16);
        section.getStyleClass().add("activity-section");
        
        Label title = new Label("Recent Activity");
        title.getStyleClass().addAll("title-3", "section-title");
        
        VBox activityCard = new VBox(16);
        activityCard.getStyleClass().addAll("dashboard-card", "activity-card");
        activityCard.setPadding(new Insets(24));
        
        // Activity items
        VBox activityList = new VBox(12);
        
        activityList.getChildren().addAll(
            createActivityItem("📧", "Newsletter sent to 500 subscribers", "2 minutes ago", "success"),
            createActivityItem("✅", "Campaign 'Summer Sale' delivered successfully", "15 minutes ago", "success"),
            createActivityItem("⚠️", "High bounce rate detected in recent campaign", "1 hour ago", "warning"),
            createActivityItem("📊", "Weekly report generated", "2 hours ago", "info"),
            createActivityItem("🔄", "Email template updated", "3 hours ago", "info")
        );
        
        activityCard.getChildren().addAll(activityList);
        section.getChildren().addAll(title, activityCard);
        
        return section;
    }
    
    private HBox createActivityItem(String icon, String message, String time, String type) {
        HBox item = new HBox(16);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12));
        item.getStyleClass().addAll("activity-item", "activity-item-" + type);
        
        // Status indicator
        Circle statusDot = new Circle(4);
        statusDot.getStyleClass().addAll("status-dot", "status-" + type);
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("activity-icon");
        iconLabel.setStyle("-fx-font-size: 16px;");
        
        // Message
        VBox messageBox = new VBox(4);
        
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().addAll("body-medium", "activity-message");
        
        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().addAll("body-small", "activity-time");
        
        messageBox.getChildren().addAll(messageLabel, timeLabel);
        
        item.getChildren().addAll(statusDot, iconLabel, messageBox);
        
        HBox.setHgrow(messageBox, Priority.ALWAYS);
        
        // Add subtle hover effect
        item.setOnMouseEntered(e -> item.getStyleClass().add("activity-item-hover"));
        item.setOnMouseExited(e -> item.getStyleClass().remove("activity-item-hover"));
        
        return item;
    }
    
    public VBox getRoot() {
        return root;
    }
}
