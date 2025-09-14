#!/bin/bash
# Email Sender Pro Launch Script

echo "📧 Email Sender Pro - Starting Application..."
echo "=============================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH."
    echo "Please install Java 17 or higher to run this application."
    echo "Download from: https://www.oracle.com/java/technologies/downloads/"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo "☕ Java Version: $JAVA_VERSION"

# Set JavaFX module path if needed (uncomment if required)
# export PATH_TO_FX=/path/to/javafx/lib
# JAVAFX_ARGS="--module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml,javafx.graphics"

echo "🔧 Compiling application..."
./mvnw clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "🚀 Starting Email Sender Pro..."
    ./mvnw javafx:run
else
    echo "❌ Compilation failed. Please check the error messages above."
    exit 1
fi