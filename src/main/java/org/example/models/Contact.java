package org.example.models;

import java.time.LocalDateTime;

/**
 * Contact data model
 */
public class Contact {
    private String name;
    private String email;
    private String phone;
    private String company;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Contact() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Contact(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }
    
    public Contact(String name, String email, String phone, String company, String notes) {
        this(name, email);
        this.phone = phone;
        this.company = company;
        this.notes = notes;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { 
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { 
        this.company = company;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { 
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return email != null ? email.equals(contact.email) : contact.email == null;
    }
    
    @Override
    public int hashCode() {
        return email != null ? email.hashCode() : 0;
    }
}
