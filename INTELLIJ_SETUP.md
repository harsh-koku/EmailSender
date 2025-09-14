# 🚀 IntelliJ IDEA Setup for Direct JavaFX Execution

## 📋 Step-by-Step Setup Instructions

### Method 1: Using Run Configuration (Recommended)

1. **Open IntelliJ IDEA** and load the EmailSender project

2. **Create New Run Configuration:**
   - Go to `Run` → `Edit Configurations...`
   - Click `+` → `Application`
   - Name: `EmailSender Direct`

3. **Configure the Application:**
   - **Main Class**: `org.example.EmailSenderApp`
   - **VM Options**: Copy and paste this exactly:
   ```
   --module-path "C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-base/20/javafx-base-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-controls/20/javafx-controls-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-fxml/20/javafx-fxml-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-graphics/20/javafx-graphics-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-base/20/javafx-base-20-win.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-controls/20/javafx-controls-20-win.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-fxml/20/javafx-fxml-20-win.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-graphics/20/javafx-graphics-20-win.jar" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics
   ```
   - **Working Directory**: `D:\Project Main\EmailSender`
   - **Use classpath of module**: `EmailSender`

4. **Click Apply and OK**

5. **Select your configuration** from the dropdown and click the green ▶️ **Run** button

### Method 2: Using Project Template Configuration

1. **Restart IntelliJ IDEA** (to pick up the pre-created configuration)
2. **Look for "EmailSender Pro"** in the run configurations dropdown
3. **Select it and click Run** ▶️

### Method 3: Quick VM Options Setup

If you just want to modify an existing run configuration:

1. **Right-click** on `EmailSenderApp.java`
2. **Select "Run 'EmailSenderApp.main()'"**
3. **After first run (which will fail), go to Run → Edit Configurations**
4. **Add the VM Options** from Method 1 above
5. **Apply and run again**

## 🔧 Troubleshooting

### Issue: "Module javafx.base not found"
- **Solution**: Make sure the VM options are correctly set as shown above
- **Double-check**: The Maven repository path matches your system

### Issue: JavaFX JARs not found
- **Run this command first**: `mvnw.cmd dependency:resolve`
- **Verify JARs exist** in your Maven repository at: `C:/Users/Asus Tuf/.m2/repository/org/openjfx/`

### Issue: ClassPath problems
- **Ensure**: "Use classpath of module" is set to "EmailSender"
- **Check**: Working directory is set to project root

## ⚡ Quick Copy-Paste VM Options

For easy copying:
```
--module-path "C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-base/20/javafx-base-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-controls/20/javafx-controls-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-fxml/20/javafx-fxml-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-graphics/20/javafx-graphics-20.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-base/20/javafx-base-20-win.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-controls/20/javafx-controls-20-win.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-fxml/20/javafx-fxml-20-win.jar;C:/Users/Asus Tuf/.m2/repository/org/openjfx/javafx-graphics/20/javafx-graphics-20-win.jar" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics
```

## ✅ Verification

After setup, you should be able to:
1. **Click the green Run button** directly in IntelliJ
2. **See the EmailSender Pro application** launch
3. **No "Module not found" errors**

**Status**: Ready for direct execution from IntelliJ IDEA! 🎉