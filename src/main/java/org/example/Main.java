package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Map<String, String>> people = null; // Try to read from Excel file first, then CSV
        if (new File("emails.xlsx").exists()) {
            System.out.println("Reading from emails.xlsx...");
            people = ExcelReader.readExcel("emails.xlsx");
        } else if (new File("emails.csv").exists()) {
            System.out.println("Reading from emails.csv...");
            people = CsvReader.readCsv("emails.csv");
        } else {
            System.err.println("Error: No email data file found!");
            System.err.println("Please create either 'emails.xlsx' or 'emails.csv' with columns 'name' and 'email'");
            System.err.println("Example CSV content:");
            System.err.println("name,email");
            System.err.println("John Doe,john.doe@example.com");
            System.exit(1);
        }
        String subject = "Welcome to Our Service!";
        String bodyTemplate = "<h2>Hello %s,</h2><p>This is a test email from our Java app.</p>";
        System.out.println("Found " + people.size() + " recipients");
        for (Map<String, String> person : people) {
            String name = person.get("name");
            String email = person.get("email");
            if (name == null || email == null) {
                System.err.println("Warning: Skipping record with missing name or email");
                continue;
            }
            String body = String.format(bodyTemplate, name);
            try {
                MailSender.sendMail(email, subject, body);
                Thread.sleep(2000); // small delay to avoid spam detection
            } catch (Exception e) {
                System.err.println("Failed to send email to " + email + ": " + e.getMessage());
            }
        }
        System.out.println("Email sending process completed!");
    }
}