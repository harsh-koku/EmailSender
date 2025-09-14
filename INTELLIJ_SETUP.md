# IntelliJ IDEA Setup Guide for EmailSender

This guide will walk you through setting up and running the EmailSender project in IntelliJ IDEA.

## Prerequisites

- IntelliJ IDEA (Community or Ultimate edition)
- Java 17 or higher (you have Java 24 installed)
- Git (optional, for version control)

## Step-by-Step Setup

### 1. Import the Project

1. **Open IntelliJ IDEA**
2. **Import Project:**
   - Click "Open" on the welcome screen
   - Navigate to `D:\Project Main\EmailSender`
   - Select the folder and click "OK"
   - IntelliJ will detect it as a Maven project and import it automatically

### 2. Configure Project SDK

1. **Go to File → Project Structure (Ctrl+Alt+Shift+S)**
2. **In the "Project" tab:**
   - Set "Project SDK" to your Java installation (Java 24)
   - Set "Project language level" to "17 - Sealed types, always-strict floating-point semantics"
3. **Click "Apply" and "OK"**

### 3. Configure Maven

1. **Go to File → Settings (Ctrl+Alt+S)**
2. **Navigate to Build, Execution, Deployment → Build Tools → Maven**
3. **Ensure:**
   - "Maven home path" is set correctly (IntelliJ can auto-detect)
   - "User settings file" is using the default location
   - "Local repository" is set to your preferred location
4. **Click "Apply" and "OK"**

### 4. Reload Maven Project

1. **Open the Maven tool window** (View → Tool Windows → Maven)
2. **Click the "Reload All Maven Projects" button** (circular arrow icon)
3. **Wait for dependencies to download** (this may take a few minutes for the first time)

### 5. Set Up Environment Variables (Method 1: Run Configuration)

The project includes a pre-configured run configuration. To customize it:

1. **Go to Run → Edit Configurations**
2. **Select "EmailSender Main" from the list**
3. **In the "Environment variables" section:**
   - Change `EMAIL_SENDER` to your actual Gmail address
   - Change `EMAIL_PASSWORD` to your Gmail App Password
4. **Click "Apply" and "OK"**

### 6. Alternative: Set Environment Variables (Method 2: Manual Configuration)

If you prefer to create your own run configuration:

1. **Go to Run → Edit Configurations**
2. **Click the "+" button and select "Application"**
3. **Configure:**
   - Name: `EmailSender`
   - Main class: `org.example.Main`
   - Module: `EmailSender`
   - Working directory: `$PROJECT_DIR$`
4. **In Environment variables, add:**
   - `EMAIL_SENDER` = `your-email@gmail.com`
   - `EMAIL_PASSWORD` = `your-app-password`
5. **Click "Apply" and "OK"**

### 7. Prepare Email Data

Ensure you have either:
- `emails.xlsx` (Excel file) with columns: name, email
- `emails.csv` (CSV file) with columns: name, email

The project already includes a sample `emails.csv` file for testing.

### 8. Run the Application

**Option 1: Use the pre-configured run configuration**
1. **Select "EmailSender Main" from the run configuration dropdown** (top-right)
2. **Click the green "Run" button or press Shift+F10**

**Option 2: Run directly from the Main class**
1. **Open `src/main/java/org/example/Main.java`**
2. **Right-click in the editor and select "Run 'Main.main()'"**
3. **Note: You'll need to configure environment variables in the run configuration that gets created**

### 9. Debug the Application

1. **Set breakpoints** by clicking in the left gutter of the code editor
2. **Select your run configuration**
3. **Click the "Debug" button or press Shift+F9**
4. **Use the debugger controls** to step through code, inspect variables, etc.

## Project Structure in IntelliJ

```
EmailSender/
├── .idea/                          # IntelliJ project files
│   └── runConfigurations/         # Pre-configured run settings
├── src/
│   └── main/java/org/example/     # Java source files
│       ├── Main.java              # Main application (▶️ Run from here)
│       ├── MailSender.java        # Email sending logic
│       ├── ExcelReader.java       # Excel file reader
│       └── CsvReader.java         # CSV file reader
├── target/                        # Compiled classes (auto-generated)
├── emails.csv                     # Sample data file
├── pom.xml                       # Maven configuration
└── README.md                     # Main documentation
```

## Useful IntelliJ Tips

### Maven Tool Window
- **View → Tool Windows → Maven**
- Use this to:
  - Reload Maven projects
  - Run Maven goals (compile, clean, etc.)
  - View dependency tree

### Terminal in IntelliJ
- **View → Tool Windows → Terminal**
- Run Maven commands directly: `./mvnw.cmd compile`

### Code Navigation
- **Ctrl+Click** on any class/method to jump to its definition
- **Ctrl+N** to find and open any class
- **Ctrl+Shift+N** to find and open any file

### Live Templates and Code Generation
- **Type `psvm` and press Tab** → generates `public static void main`
- **Type `sout` and press Tab** → generates `System.out.println()`

## Troubleshooting

### Common Issues

1. **"Cannot resolve symbol" errors**
   - Reload Maven project (Maven tool window → refresh button)
   - File → Invalidate Caches and Restart

2. **Wrong Java version**
   - File → Project Structure → Project → Change SDK to Java 17+

3. **Maven not working**
   - File → Settings → Build Tools → Maven → Check Maven home path

4. **Environment variables not working**
   - Check Run → Edit Configurations → Environment variables
   - Make sure "Pass parent environment" is checked

5. **Email sending fails**
   - Verify Gmail App Password is correct
   - Check internet connection
   - Ensure 2FA is enabled on Gmail account

### Getting Help

- **IntelliJ Documentation:** https://www.jetbrains.com/help/idea/
- **Maven in IntelliJ:** https://www.jetbrains.com/help/idea/maven-support.html
- **Project README:** See `README.md` for general project information

## Security Reminder

⚠️ **Never commit your actual email credentials to version control!**
- Keep real credentials in environment variables
- The sample configuration uses placeholder values
- Consider using IntelliJ's password manager for sensitive data
