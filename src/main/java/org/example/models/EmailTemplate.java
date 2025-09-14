package org.example.models;

import java.time.LocalDateTime;

/**
 * Email template data model
 */
public class EmailTemplate {
    private String id;
    private String name;
    private String subject;
    private String content;
    private String category;
    private boolean isHtml;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public EmailTemplate() {
        this.id = java.util.UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isHtml = true;
    }
    
    public EmailTemplate(String name, String subject, String content) {
        this();
        this.name = name;
        this.subject = subject;
        this.content = content;
    }
    
    public EmailTemplate(String name, String subject, String content, String category, boolean isHtml) {
        this(name, subject, content);
        this.category = category;
        this.isHtml = isHtml;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { 
        this.subject = subject;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getContent() { return content; }
    public void setContent(String content) { 
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { 
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isHtml() { return isHtml; }
    public void setHtml(boolean html) { 
        isHtml = html;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public String toString() {
        return name + " (" + category + ")";
    }
}
