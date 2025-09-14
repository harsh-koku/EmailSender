# 📧 Email Sender Pro

<div align="center">
  <img src="https://img.shields.io/badge/Java-17+-blue.svg" alt="Java Version">
  <img src="https://img.shields.io/badge/JavaFX-20+-green.svg" alt="JavaFX Version">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License">
  <img src="https://img.shields.io/badge/Status-Ready%20for%20Production-brightgreen.svg" alt="Status">
</div>

A modern, professional JavaFX email sender application with beautiful glassmorphism UI, advanced features, and smooth animations. Send bulk emails with style and efficiency!

## ✨ Features

### 🎨 **Modern UI/UX**
- **Glassmorphism Design**: Beautiful, modern interface with glass-like effects
- **Dark/Light Theme**: Instant theme switching with smooth transitions
- **Smooth Animations**: Professional animations and transitions throughout the app
- **Responsive Layout**: Adaptive design that works on any screen size
- **Collapsible Sidebar**: Space-efficient navigation with icon-only collapsed mode

### 📧 **Email Management**
- **Bulk Email Sending**: Send to multiple recipients from Excel/CSV files
- **Email Templates**: Create, manage, and reuse email templates
- **Personalization**: Use variables like {name}, {email}, {company} for personalized emails
- **Progress Tracking**: Real-time progress bar and status updates during sending
- **Email History**: View detailed history with analytics and success rates
- **HTML/Plain Text**: Support for both HTML and plain text emails

### 📊 **Analytics & Monitoring**
- **Interactive Dashboard**: Visual analytics with charts and metrics
- **Success Rate Tracking**: Monitor delivery rates and performance
- **Email History Search**: Advanced filtering and search capabilities
- **Performance Metrics**: Real-time FPS and memory usage monitoring
- **Export Functionality**: Export data to various formats (Coming Soon)

### 🔧 **Advanced Features**
- **SMTP Configuration**: Flexible email server settings (Gmail, Outlook, custom)
- **Connection Testing**: Test email settings before sending
- **Data Persistence**: Automatic saving of templates, history, and settings
- **Performance Optimization**: GPU acceleration and memory management
- **Backup & Restore**: Automatic data backup functionality

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (Download from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/))
- **Maven 3.6+** (or use included Maven wrapper)

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/EmailSender.git
   cd EmailSender
   ```

2. **Build and run**:
   ```bash
   # Using Maven wrapper (recommended)
   ./mvnw clean compile javafx:run
   
   # Or using system Maven
   mvn clean compile javafx:run
   ```

3. **Alternative: Use the batch file** (Windows):
   ```cmd
   run.bat
   ```

## 📋 Configuration

### Gmail Setup (Recommended)

1. **Enable 2-Factor Authentication** on your Google account
2. **Generate App Password**:
   - Go to [Google App Passwords](https://myaccount.google.com/apppasswords)
   - Create a new app password for "Email Sender Pro"
3. **Configure in Settings**:
   - Open Email Sender Pro → Settings → Email Settings
   - Enter your Gmail address and the generated app password
   - Test the connection

### Other Email Providers

| Provider | SMTP Host | Port | SSL/STARTTLS |
|----------|-----------|------|-------------|
| **Gmail** | smtp.gmail.com | 587 | STARTTLS |
| **Outlook** | smtp-mail.outlook.com | 587 | STARTTLS |
| **Yahoo** | smtp.mail.yahoo.com | 587 | STARTTLS |
| **Custom** | your-smtp-server.com | 587/465 | STARTTLS/SSL |

## 📄 File Formats

### Excel Files (.xlsx)
```
| Name      | Email              | Company    |
|-----------|--------------------|-----------| 
| John Doe  | john@example.com   | Tech Corp  |
| Jane Smith| jane@company.org   | Design Inc |
```

### CSV Files (.csv)
```csv
name,email,company
John Doe,john@example.com,Tech Corp
Jane Smith,jane@company.org,Design Inc
```

**Required Columns**: `email`  
**Optional Columns**: `name`, `company`, or any custom fields

## 🎯 Usage Guide

### 1. **Dashboard** 📊
- View email statistics and performance metrics
- Quick access to recent activities
- Visual analytics with interactive charts

### 2. **Compose Email** ✏️
- Import recipients from Excel/CSV files
- Write personalized email content
- Use templates for common emails
- Preview before sending

### 3. **Templates** 📄
- Create reusable email templates
- Organize by categories (Marketing, Business, etc.)
- Use variables for personalization
- Preview and test templates

### 4. **History** 📋
- View all sent emails with detailed analytics
- Filter by status, date, or content
- Export history data (Coming Soon)
- Track delivery success rates

### 5. **Settings** ⚙️
- Configure email account settings
- Customize appearance and themes
- Test email connection
- Manage application preferences

## 🎨 Customization

### Themes
- **Light Theme**: Clean, professional appearance
- **Dark Theme**: Modern, eye-friendly dark mode
- **Instant Switching**: Change themes without restart

### Animations
- **Speed Control**: Adjust animation speed (0.5x to 2.0x)
- **Enable/Disable**: Turn animations on/off
- **Performance Optimization**: Automatic throttling based on performance

## 🔧 Development

### Project Structure
```
src/main/java/org/example/
├── controllers/     # Application controllers (MVC pattern)
├── models/         # Data models (Contact, EmailTemplate, etc.)
├── utils/          # Utility classes (ThemeManager, DataManager, etc.)
├── views/          # JavaFX view classes
└── MainApp.java    # Application entry point

src/main/resources/
└── css/            # Stylesheets (themes, animations, base styles)
```

### Building from Source
```bash
# Compile
mvn clean compile

# Run
mvn javafx:run

# Package (creates executable JAR)
mvn clean package

# Run tests
mvn test
```

## 🐛 Troubleshooting

### Common Issues

**Email not sending?**
- ✅ Check internet connection
- ✅ Verify email credentials in Settings
- ✅ Use App Password for Gmail (not regular password)
- ✅ Test connection in Settings → Email Settings

**Application won't start?**
- ✅ Ensure Java 17+ is installed: `java -version`
- ✅ Check JavaFX dependencies are included
- ✅ Try using the Maven wrapper: `./mvnw javafx:run`

**Performance issues?**
- ✅ Reduce animation speed in Settings
- ✅ Disable animations if needed
- ✅ Check system resources (CPU, memory)

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/amazing-feature`
3. **Commit** your changes: `git commit -m 'Add amazing feature'`
4. **Push** to the branch: `git push origin feature/amazing-feature`
5. **Open** a Pull Request

### Development Guidelines
- Follow existing code style and patterns
- Add JavaDoc comments for public methods
- Test your changes thoroughly
- Update documentation as needed

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **JavaFX** for the amazing UI framework
- **Apache POI** for Excel file processing
- **Jakarta Mail** for email functionality
- **Jackson** for JSON data persistence
- **ControlsFX** for additional UI components

## 📞 Support

Need help? Here are your options:

- 📚 **Documentation**: Check this README and inline comments
- 🐛 **Issues**: [Report bugs or request features](https://github.com/harsh-koku/EmailSender/issues)
- 💬 **Discussions**: [Join community discussions](https://github.com/harsh-koku/EmailSender/discussions)

---

<div align="center">
  <p>Made with ❤️ using JavaFX</p>
  <p>Give this project a ⭐ if you found it helpful!</p>
</div>