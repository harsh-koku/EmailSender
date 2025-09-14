@echo off
echo.
echo ================================================
echo   Modern JavaFX Email Sender Pro
echo   Starting the application...
echo ================================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java 17 or higher to run this application.
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo Java version:
java -version
echo.

REM Check if mvnw.cmd exists
if not exist "mvnw.cmd" (
    echo ERROR: Maven wrapper not found!
    echo Please make sure you're in the correct directory.
    pause
    exit /b 1
)

REM Compile and run the application
echo Compiling project...
call mvnw.cmd clean compile
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Build failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Build successful! Launching JavaFX application...
echo.
echo Note: Some CSS warnings are normal and don't affect functionality.
echo You can safely ignore them.
echo.

REM Run the JavaFX application
call mvnw.cmd javafx:run

echo.
echo Application closed.
pause
