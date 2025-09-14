package org.example.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.models.EmailHistory;
import org.example.utils.AnimationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Contact History View for viewing and managing email history
 */
public class ContactHistoryView {
    
    private VBox root;
    private TableView<EmailHistory> historyTableView;
    private ObservableList<EmailHistory> emailHistory;
    private TextField searchField;
    private ComboBox<String> statusFilterComboBox;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Button refreshBtn;
    private Button exportBtn;
    private Button deleteBtn;
    private Label statusLabel;
    
    // Statistics components
    private Label totalEmailsLabel;
    private Label successfulEmailsLabel;
    private Label failedEmailsLabel;
    private Label successRateLabel;
    private PieChart statusChart;
    private LineChart<String, Number> timelineChart;
    
    public ContactHistoryView() {
        emailHistory = FXCollections.observableArrayList();
        createHistoryView();
    }
    
    private void createHistoryView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("history-container");
        
        // Header
        HBox header = createHeader();
        
        // Statistics section
        HBox statsSection = createStatisticsSection();
        
        // Filter and search section
        VBox filterSection = createFilterSection();
        
        // History table
        VBox tableSection = createTableSection();
        
        root.getChildren().addAll(header, statsSection, filterSection, tableSection);
        
        // Add entrance animations
        AnimationUtils.staggeredEntrance(header, statsSection, filterSection, tableSection).play();
    }
    
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("📋 Email History");
        title.getStyleClass().addAll("title-2", "history-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Action buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        refreshBtn = new Button("🔄 Refresh");
        refreshBtn.getStyleClass().addAll("btn-secondary", "modern-button");
        
        exportBtn = new Button("📊 Export");
        exportBtn.getStyleClass().addAll("btn-outline", "modern-button");
        
        deleteBtn = new Button("🗑️ Delete Selected");
        deleteBtn.getStyleClass().addAll("btn-ghost", "modern-button");
        
        actionButtons.getChildren().addAll(refreshBtn, exportBtn, deleteBtn);
        
        // Status label
        statusLabel = new Label("Email history and analytics");
        statusLabel.getStyleClass().addAll("body-small", "status-text");
        
        header.getChildren().addAll(title, spacer, actionButtons, statusLabel);
        
        return header;
    }
    
    private HBox createStatisticsSection() {
        HBox statsSection = new HBox(20);
        statsSection.setAlignment(Pos.CENTER_LEFT);
        
        // Statistics cards
        VBox totalCard = createStatsCard("📧", "Total Emails", "0", "total-emails");
        totalEmailsLabel = (Label) ((VBox) totalCard.getChildren().get(1)).getChildren().get(1);
        
        VBox successCard = createStatsCard("✅", "Successful", "0", "successful-emails");
        successfulEmailsLabel = (Label) ((VBox) successCard.getChildren().get(1)).getChildren().get(1);
        
        VBox failedCard = createStatsCard("❌", "Failed", "0", "failed-emails");
        failedEmailsLabel = (Label) ((VBox) failedCard.getChildren().get(1)).getChildren().get(1);
        
        VBox rateCard = createStatsCard("📈", "Success Rate", "0%", "success-rate");
        successRateLabel = (Label) ((VBox) rateCard.getChildren().get(1)).getChildren().get(1);
        
        // Charts section
        VBox chartsBox = new VBox(12);
        chartsBox.getStyleClass().addAll("glass-card", "charts-section");
        chartsBox.setPrefWidth(400);
        
        Label chartsTitle = new Label("📊 Analytics");
        chartsTitle.getStyleClass().addAll("title-4", "section-title");
        
        // Status pie chart
        statusChart = new PieChart();
        statusChart.getStyleClass().add("modern-pie-chart");
        statusChart.setPrefHeight(200);
        statusChart.setLegendVisible(false);
        statusChart.setTitle("Email Status Distribution");
        
        chartsBox.getChildren().addAll(chartsTitle, statusChart);
        
        statsSection.getChildren().addAll(totalCard, successCard, failedCard, rateCard, chartsBox);
        
        // Add animations
        AnimationUtils.addHoverScaleEffect(totalCard, 1.02);
        AnimationUtils.addHoverScaleEffect(successCard, 1.02);
        AnimationUtils.addHoverScaleEffect(failedCard, 1.02);
        AnimationUtils.addHoverScaleEffect(rateCard, 1.02);
        AnimationUtils.addHoverScaleEffect(chartsBox, 1.01);
        
        return statsSection;
    }
    
    private VBox createStatsCard(String icon, String title, String value, String styleClass) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("glass-card", "stats-card", styleClass);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(150);
        card.setPrefHeight(100);
        
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().addAll("stats-icon");
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        VBox textBox = new VBox(4);
        textBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().addAll("caption", "stats-title");
        
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().addAll("title-3", "stats-value");
        
        textBox.getChildren().addAll(titleLabel, valueLabel);
        card.getChildren().addAll(iconLabel, textBox);
        
        return card;
    }
    
    private VBox createFilterSection() {
        VBox filterSection = new VBox(16);
        filterSection.getStyleClass().addAll("glass-card", "filter-section");
        
        Label filterTitle = new Label("🔍 Search & Filter");
        filterTitle.getStyleClass().addAll("title-4", "section-title");
        
        // Filter controls
        HBox filterRow = new HBox(16);
        filterRow.setAlignment(Pos.CENTER_LEFT);
        
        // Search field
        VBox searchBox = new VBox(6);
        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("field-label");
        
        searchField = new TextField();
        searchField.getStyleClass().add("modern-text-field");
        searchField.setPromptText("Search by subject, recipient, or content...");
        searchField.setPrefWidth(250);
        
        searchBox.getChildren().addAll(searchLabel, searchField);
        
        // Status filter
        VBox statusBox = new VBox(6);
        Label statusLabel = new Label("Status:");
        statusLabel.getStyleClass().add("field-label");
        
        statusFilterComboBox = new ComboBox<>();
        statusFilterComboBox.getStyleClass().add("modern-combo-box");
        statusFilterComboBox.getItems().addAll("All Status", "Sent", "Delivered", "Failed", "Draft");
        statusFilterComboBox.setValue("All Status");
        statusFilterComboBox.setPrefWidth(120);
        
        statusBox.getChildren().addAll(statusLabel, statusFilterComboBox);
        
        // Date range
        VBox dateBox = new VBox(6);
        Label dateLabel = new Label("Date Range:");
        dateLabel.getStyleClass().add("field-label");
        
        HBox dateRow = new HBox(8);
        dateRow.setAlignment(Pos.CENTER_LEFT);
        
        startDatePicker = new DatePicker();
        startDatePicker.getStyleClass().add("modern-date-picker");
        startDatePicker.setPromptText("Start date");
        startDatePicker.setPrefWidth(140);
        
        Label toLabel = new Label("to");
        toLabel.getStyleClass().add("body-small");
        
        endDatePicker = new DatePicker();
        endDatePicker.getStyleClass().add("modern-date-picker");
        endDatePicker.setPromptText("End date");
        endDatePicker.setPrefWidth(140);
        
        dateRow.getChildren().addAll(startDatePicker, toLabel, endDatePicker);
        dateBox.getChildren().addAll(dateLabel, dateRow);
        
        // Clear filters button
        Button clearFiltersBtn = new Button("🔄 Clear");
        clearFiltersBtn.getStyleClass().addAll("btn-outline", "modern-button");
        clearFiltersBtn.setAlignment(Pos.BOTTOM_CENTER);
        
        filterRow.getChildren().addAll(searchBox, statusBox, dateBox, clearFiltersBtn);
        
        filterSection.getChildren().addAll(filterTitle, filterRow);
        
        AnimationUtils.addHoverScaleEffect(filterSection, 1.01);
        AnimationUtils.addHoverScaleEffect(clearFiltersBtn);
        
        return filterSection;
    }
    
    private VBox createTableSection() {
        VBox tableSection = new VBox(12);
        tableSection.getStyleClass().addAll("glass-card", "table-section");
        
        Label tableTitle = new Label("📋 Email History");
        tableTitle.getStyleClass().addAll("title-4", "section-title");
        
        // Create table
        historyTableView = new TableView<>(emailHistory);
        historyTableView.getStyleClass().add("modern-table-view");
        historyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Table columns
        TableColumn<EmailHistory, String> subjectColumn = new TableColumn<>("Subject");
        subjectColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSubject()));
        subjectColumn.setPrefWidth(200);
        
        TableColumn<EmailHistory, String> recipientsColumn = new TableColumn<>("Recipients");
        recipientsColumn.setCellValueFactory(data -> {
            int count = data.getValue().getRecipients().size();
            String text = count == 1 ? "1 recipient" : count + " recipients";
            return new javafx.beans.property.SimpleStringProperty(text);
        });
        recipientsColumn.setPrefWidth(120);
        
        TableColumn<EmailHistory, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().name()));
        statusColumn.setCellFactory(col -> new TableCell<EmailHistory, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    getStyleClass().removeAll("status-sent", "status-delivered", "status-failed", "status-draft");
                    getStyleClass().add("status-" + item.toLowerCase());
                }
            }
        });
        statusColumn.setPrefWidth(100);
        
        TableColumn<EmailHistory, String> sentDateColumn = new TableColumn<>("Sent Date");
        sentDateColumn.setCellValueFactory(data -> {
            LocalDateTime sentAt = data.getValue().getSentAt();
            String dateStr = sentAt != null ? sentAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")) : "-";
            return new javafx.beans.property.SimpleStringProperty(dateStr);
        });
        sentDateColumn.setPrefWidth(150);
        
        TableColumn<EmailHistory, String> successRateColumn = new TableColumn<>("Success Rate");
        successRateColumn.setCellValueFactory(data -> {
            double rate = data.getValue().getSuccessRate();
            return new javafx.beans.property.SimpleStringProperty(String.format("%.1f%%", rate));
        });
        successRateColumn.setPrefWidth(100);
        
        TableColumn<EmailHistory, String> senderColumn = new TableColumn<>("Sender");
        senderColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSenderEmail()));
        senderColumn.setPrefWidth(180);
        
        historyTableView.getColumns().addAll(subjectColumn, recipientsColumn, statusColumn, sentDateColumn, successRateColumn, senderColumn);
        
        // Context menu for table
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewDetailsItem = new MenuItem("📄 View Details");
        MenuItem resendItem = new MenuItem("📤 Resend");
        MenuItem deleteItem = new MenuItem("🗑️ Delete");
        contextMenu.getItems().addAll(viewDetailsItem, resendItem, new SeparatorMenuItem(), deleteItem);
        historyTableView.setContextMenu(contextMenu);
        
        tableSection.getChildren().addAll(tableTitle, historyTableView);
        
        AnimationUtils.addHoverScaleEffect(tableSection, 1.01);
        
        return tableSection;
    }
    
    // Getters for controller access
    public VBox getRoot() { return root; }
    public TableView<EmailHistory> getHistoryTableView() { return historyTableView; }
    public ObservableList<EmailHistory> getEmailHistory() { return emailHistory; }
    public TextField getSearchField() { return searchField; }
    public ComboBox<String> getStatusFilterComboBox() { return statusFilterComboBox; }
    public DatePicker getStartDatePicker() { return startDatePicker; }
    public DatePicker getEndDatePicker() { return endDatePicker; }
    public Button getRefreshBtn() { return refreshBtn; }
    public Button getExportBtn() { return exportBtn; }
    public Button getDeleteBtn() { return deleteBtn; }
    public Label getStatusLabel() { return statusLabel; }
    public PieChart getStatusChart() { return statusChart; }
    
    public void setStatusText(String text) {
        statusLabel.setText(text);
    }
    
    public void updateStatistics(int total, int successful, int failed, double successRate) {
        totalEmailsLabel.setText(String.valueOf(total));
        successfulEmailsLabel.setText(String.valueOf(successful));
        failedEmailsLabel.setText(String.valueOf(failed));
        successRateLabel.setText(String.format("%.1f%%", successRate));
        
        // Update pie chart
        statusChart.getData().clear();
        if (total > 0) {
            if (successful > 0) {
                statusChart.getData().add(new PieChart.Data("Successful (" + successful + ")", successful));
            }
            if (failed > 0) {
                statusChart.getData().add(new PieChart.Data("Failed (" + failed + ")", failed));
            }
        } else {
            statusChart.getData().add(new PieChart.Data("No data", 1));
        }
    }
}
