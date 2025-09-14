# 🚀 Email Sender Pro - Deployment Guide

This guide will help you deploy and run Email Sender Pro in different environments.

## 📋 Prerequisites

### Required Software
- **Java 17+** (JDK or JRE)
- **Maven 3.6+** (optional, wrapper included)

### System Requirements
- **OS**: Windows 10+, macOS 10.14+, Linux (Ubuntu 18.04+)
- **RAM**: Minimum 512MB, Recommended 1GB+
- **Disk Space**: 100MB for application + dependencies

## 🖥️ Platform-Specific Instructions

### Windows

#### Option 1: Using Batch File (Easiest)
```cmd
# Double-click run.bat or execute in Command Prompt:
run.bat
```

#### Option 2: Manual Command Line
```cmd
# Using Maven Wrapper
mvnw.cmd clean compile javafx:run

# Using System Maven
mvn clean compile javafx:run
```

### macOS/Linux

#### Option 1: Using Shell Script
```bash
# Make executable and run
chmod +x run.sh
./run.sh
```

#### Option 2: Manual Command Line
```bash
# Using Maven Wrapper
./mvnw clean compile javafx:run

# Using System Maven
mvn clean compile javafx:run
```

## 🏗️ Building Distribution

### Create Executable JAR
```bash
mvn clean package
```
The JAR file will be created in the `target/` directory.

### Running the JAR
```bash
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar target/EmailSender-1.0-SNAPSHOT.jar
```

## ⚙️ Configuration

### Email Settings
The application supports various email providers:

| Provider | SMTP Server | Port | Security |
|----------|-------------|------|----------|
| Gmail | smtp.gmail.com | 587 | STARTTLS |
| Outlook | smtp-mail.outlook.com | 587 | STARTTLS |
| Yahoo | smtp.mail.yahoo.com | 587 | STARTTLS |

### Environment Variables (Optional)
```bash
# Pre-configure email settings
export EMAIL_SENDER=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password
```

### Application Data
User data is stored in:
- **Windows**: `%USERPROFILE%\EmailSender\`
- **macOS**: `~/EmailSender/`
- **Linux**: `~/EmailSender/`

## 🐛 Troubleshooting

### Common Issues

#### "Java not found"
**Solution**: Install Java 17+ and ensure it's in your system PATH
```bash
# Check Java installation
java -version
javac -version
```

#### "JavaFX components not found"
**Solution**: The project includes JavaFX dependencies, but if issues persist:
1. Update your Maven dependencies: `mvn clean install -U`
2. Ensure you're using Java 17+

#### "Module not found" errors
**Solution**: If using system Java 11+, you may need to add JavaFX modules:
```bash
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics
```

#### Performance Issues
**Solutions**:
- Increase JVM heap size: `-Xmx1g`
- Disable animations in Settings → Appearance
- Reduce animation speed in Settings

#### Email Sending Issues
**Solutions**:
1. Check internet connection
2. Verify email credentials in Settings
3. Use App Passwords for Gmail (not regular password)
4. Check firewall/antivirus blocking SMTP connections

## 🔐 Security Considerations

### Email Credentials
- Never commit real credentials to version control
- Use App Passwords for Gmail and similar services
- Configure credentials through the Settings UI

### Data Protection
- User email lists are stored locally only
- No data is transmitted except for email sending
- Email history can be cleared from the application

## 📊 Performance Tuning

### JVM Options
For better performance, you can add these JVM options:

```bash
# Memory optimization
-Xmx1g -Xms256m

# JavaFX performance
-Dprism.lcdtext=false -Dprism.text=t2k

# Garbage collection optimization
-XX:+UseG1GC -XX:MaxGCPauseMillis=100
```

### Application Settings
- Reduce animation speed in Settings → Appearance
- Disable animations for low-end hardware
- Limit concurrent email sending for slower connections

## 🏢 Enterprise Deployment

### Corporate Networks
- Configure proxy settings if required
- Ensure SMTP ports (587, 465, 25) are not blocked
- Test email connectivity before deployment

### Multi-User Environment
- Each user gets their own data directory
- Settings are stored per-user
- No global configuration required

## 📈 Monitoring and Logging

### Application Logs
Logs are stored in the user data directory:
- Windows: `%USERPROFILE%\EmailSender\logs\`
- macOS/Linux: `~/EmailSender/logs/`

### Performance Monitoring
The application includes built-in performance monitoring:
- View FPS and memory usage in development
- Performance automatically optimizes based on system capabilities

## 🔄 Updates and Maintenance

### Updating the Application
1. Backup user data directory
2. Replace application files
3. Restart the application

### Data Backup
User data is automatically backed up to:
`[UserDataDir]/backups/`

### Clearing Data
To reset the application:
1. Close the application
2. Delete the user data directory
3. Restart to recreate with defaults

## 📞 Support

If you encounter issues not covered here:

1. **Check the logs** in your user data directory
2. **Review the README.md** for additional information
3. **Report issues** on the project's issue tracker
4. **Join discussions** for community support

## 🎯 Quick Start Checklist

- [ ] Java 17+ installed and in PATH
- [ ] Project downloaded/cloned
- [ ] Dependencies resolved (`mvn clean compile`)
- [ ] Email credentials configured
- [ ] Test email sent successfully
- [ ] Application running smoothly

---

**Happy Emailing! 📧**