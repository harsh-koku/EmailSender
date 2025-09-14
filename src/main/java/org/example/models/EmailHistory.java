package org.example.models;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Email history data model
 */
public class EmailHistory {
    public enum Status {
        DRAFT, SENT, DELIVERED, FAILED, BOUNCED
    }
    
    private String id;
    private String subject;
    private String content;
    private List<String> recipients;
    private String senderEmail;
    private Status status;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private String errorMessage;
    private int totalRecipients;
    private int successfulDeliveries;
    private int failedDeliveries;
    
    public EmailHistory() {
        this.id = java.util.UUID.randomUUID().toString();
        this.status = Status.DRAFT;
    }
    
    public EmailHistory(String subject, String content, List<String> recipients, String senderEmail) {
        this();
        this.subject = subject;
        this.content = content;
        this.recipients = recipients;
        this.senderEmail = senderEmail;
        this.totalRecipients = recipients.size();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public List<String> getRecipients() { return recipients; }
    public void setRecipients(List<String> recipients) { 
        this.recipients = recipients; 
        this.totalRecipients = recipients != null ? recipients.size() : 0;
    }
    
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public int getTotalRecipients() { return totalRecipients; }
    public void setTotalRecipients(int totalRecipients) { this.totalRecipients = totalRecipients; }
    
    public int getSuccessfulDeliveries() { return successfulDeliveries; }
    public void setSuccessfulDeliveries(int successfulDeliveries) { this.successfulDeliveries = successfulDeliveries; }
    
    public int getFailedDeliveries() { return failedDeliveries; }
    public void setFailedDeliveries(int failedDeliveries) { this.failedDeliveries = failedDeliveries; }
    
    public double getSuccessRate() {
        if (totalRecipients == 0) return 0.0;
        return (double) successfulDeliveries / totalRecipients * 100.0;
    }
    
    public String getStatusIcon() {
        switch (status) {
            case DRAFT: return "📝";
            case SENT: return "📤";
            case DELIVERED: return "✅";
            case FAILED: return "❌";
            case BOUNCED: return "↩️";
            default: return "❓";
        }
    }
    
    @Override
    public String toString() {
        return subject + " (" + status + ")";
    }
}
