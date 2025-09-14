@echo off
echo ===============================================
echo   EmailSender Pro - IntelliJ Helper Script
echo ===============================================

echo.
echo Ensuring dependencies are available...
call mvnw.cmd dependency:copy-dependencies -DoutputDirectory=target/lib -q

echo.
echo Starting application with JavaFX modules...
echo.

java --module-path "target\lib" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -cp "target\classes;target\lib\*" org.example.EmailSenderApp

echo.
echo Application closed.
pause