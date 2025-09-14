package org.example.controllers;

import javafx.scene.control.Alert;
import org.example.models.EmailHistory;
import org.example.views.ContactHistoryView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Email History functionality
 */
public class HistoryController {
    
    private final ContactHistoryView view;
    private List<EmailHistory> allHistory;
    
    public HistoryController(ContactHistoryView view) {
        this.view = view;
        this.allHistory = new ArrayList<>();
        initialize();
        loadSampleHistory();
    }
    
    private void initialize() {
        // Setup event handlers
        view.getRefreshBtn().setOnAction(e -> handleRefresh());
        view.getExportBtn().setOnAction(e -> handleExport());
        view.getDeleteBtn().setOnAction(e -> handleDelete());
        
        // Search and filter handlers
        view.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        view.getStatusFilterComboBox().valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        view.getStartDatePicker().valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        view.getEndDatePicker().valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        
        // Table selection handler
        view.getHistoryTableView().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            view.getDeleteBtn().setDisable(newVal == null);
        });
        
        view.getDeleteBtn().setDisable(true);
    }
    
    private void handleRefresh() {
        view.setStatusText("Refreshing email history...");
        
        // In a real application, this would reload from database or file
        // For now, we'll just refresh the current data
        applyFilters();
        updateStatistics();
        
        view.setStatusText("Email history refreshed");
    }
    
    private void handleExport() {
        if (view.getEmailHistory().isEmpty()) {
            showAlert("Export Error", "No email history data to export.");
            return;
        }
        
        // Simulate export functionality
        showAlert("Export", 
            "Export functionality would save the email history to a CSV or Excel file.\\n\\n" +
            "This feature is coming soon!");
        
        view.setStatusText("Export feature coming soon");
    }
    
    private void handleDelete() {
        EmailHistory selected = view.getHistoryTableView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select an email history entry to delete.");
            return;
        }
        
        // Confirmation dialog
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Delete Email History");
        confirmDialog.setContentText("Are you sure you want to delete this email history entry?\\n\\nSubject: " + selected.getSubject());
        
        confirmDialog.showAndWait().ifPresent(result -> {
            if (result.getButtonData().isDefaultButton()) {
                allHistory.remove(selected);
                view.getEmailHistory().remove(selected);
                updateStatistics();
                view.setStatusText("Email history entry deleted");
            }
        });
    }
    
    private void applyFilters() {
        List<EmailHistory> filteredHistory = new ArrayList<>(allHistory);
        
        // Apply search filter
        String searchText = view.getSearchField().getText().toLowerCase().trim();
        if (!searchText.isEmpty()) {
            filteredHistory.removeIf(history -> 
                !history.getSubject().toLowerCase().contains(searchText) &&
                !history.getSenderEmail().toLowerCase().contains(searchText) &&
                !history.getContent().toLowerCase().contains(searchText)
            );
        }
        
        // Apply status filter
        String statusFilter = view.getStatusFilterComboBox().getValue();
        if (statusFilter != null && !statusFilter.equals("All Status")) {
            filteredHistory.removeIf(history -> 
                !history.getStatus().name().equalsIgnoreCase(statusFilter)
            );
        }
        
        // Apply date filters
        if (view.getStartDatePicker().getValue() != null) {
            filteredHistory.removeIf(history -> {
                if (history.getSentAt() == null) return true;
                return history.getSentAt().toLocalDate().isBefore(view.getStartDatePicker().getValue());
            });
        }
        
        if (view.getEndDatePicker().getValue() != null) {
            filteredHistory.removeIf(history -> {
                if (history.getSentAt() == null) return true;
                return history.getSentAt().toLocalDate().isAfter(view.getEndDatePicker().getValue());
            });
        }
        
        // Update the observable list
        view.getEmailHistory().setAll(filteredHistory);
        
        view.setStatusText("Showing " + filteredHistory.size() + " of " + allHistory.size() + " entries");
    }
    
    private void updateStatistics() {
        int total = allHistory.size();
        int successful = (int) allHistory.stream()
            .mapToInt(EmailHistory::getSuccessfulDeliveries)
            .sum();
        int failed = (int) allHistory.stream()
            .mapToInt(EmailHistory::getFailedDeliveries)
            .sum();
        
        double successRate = total > 0 ? (double) successful / (successful + failed) * 100.0 : 0.0;
        
        view.updateStatistics(total, successful, failed, successRate);
    }
    
    private void loadSampleHistory() {
        // Add sample email history data
        List<String> recipients1 = List.of("user1@example.com", "user2@example.com", "user3@example.com");
        EmailHistory history1 = new EmailHistory("Welcome to Our Service", 
            "Welcome email content here...", recipients1, "admin@company.com");
        history1.setStatus(EmailHistory.Status.DELIVERED);
        history1.setSentAt(LocalDateTime.now().minusDays(1));
        history1.setDeliveredAt(LocalDateTime.now().minusDays(1).plusMinutes(5));
        history1.setSuccessfulDeliveries(3);
        history1.setFailedDeliveries(0);
        
        List<String> recipients2 = List.of("customer1@example.com", "customer2@example.com");
        EmailHistory history2 = new EmailHistory("Monthly Newsletter", 
            "Newsletter content here...", recipients2, "marketing@company.com");
        history2.setStatus(EmailHistory.Status.DELIVERED);
        history2.setSentAt(LocalDateTime.now().minusDays(3));
        history2.setDeliveredAt(LocalDateTime.now().minusDays(3).plusMinutes(2));
        history2.setSuccessfulDeliveries(2);
        history2.setFailedDeliveries(0);
        
        List<String> recipients3 = List.of("client@example.com");
        EmailHistory history3 = new EmailHistory("Meeting Reminder", 
            "Meeting reminder content...", recipients3, "admin@company.com");
        history3.setStatus(EmailHistory.Status.SENT);
        history3.setSentAt(LocalDateTime.now().minusHours(2));
        history3.setSuccessfulDeliveries(1);
        history3.setFailedDeliveries(0);
        
        List<String> recipients4 = List.of("test@example.com", "invalid-email", "user@test.com");
        EmailHistory history4 = new EmailHistory("Test Email Campaign", 
            "Test campaign content...", recipients4, "test@company.com");
        history4.setStatus(EmailHistory.Status.DELIVERED);
        history4.setSentAt(LocalDateTime.now().minusDays(7));
        history4.setDeliveredAt(LocalDateTime.now().minusDays(7).plusMinutes(10));
        history4.setSuccessfulDeliveries(2);
        history4.setFailedDeliveries(1);
        
        allHistory.addAll(List.of(history1, history2, history3, history4));
        
        // Apply initial filters and update statistics
        applyFilters();
        updateStatistics();
        
        view.setStatusText("Loaded " + allHistory.size() + " email history entries");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Public methods for adding new history entries
    public void addEmailHistory(EmailHistory history) {
        allHistory.add(history);
        applyFilters();
        updateStatistics();
        view.setStatusText("New email history entry added");
    }
    
    public List<EmailHistory> getAllHistory() {
        return new ArrayList<>(allHistory);
    }
    
    public ContactHistoryView getView() {
        return view;
    }
}