@echo off
setlocal enabledelayedexpansion

REM Set paths
set "JAVAFX_PATH=%~dp0javafx-sdk-win"
set "SQLITE_PATH=%~dp0lib"
set "PROJECT_PATH=%~dp0src"

echo JavaFX PATH: %JAVAFX_PATH%
echo SQLITE PATH: %SQLITE_PATH%
echo PROJECT PATH: %PROJECT_PATH%

REM Check if JavaFX exists
if not exist "%JAVAFX_PATH%\lib\javafx.controls.jar" (
    echo ERROR: JavaFX SDK not found at %JAVAFX_PATH%
    pause
    exit /b 1
)

REM Check libraries
echo Checking libraries...
if not exist "%SQLITE_PATH%\sqlite-jdbc-3.49.1.0.jar" (
    echo WARNING: SQLite JDBC driver not found
)

REM Set JavaFX modules - include all necessary modules
set "JAVAFX_MODULES=javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.web,javafx.swing,javafx.media"

REM Create output directory if it doesn't exist
if not exist "out" mkdir out

REM Clean output directory
echo Cleaning previous build...
del /q /s out\* > nul 2>&1

REM Compile the project
echo Compiling the project...
javac -verbose --module-path "%JAVAFX_PATH%\lib" ^
      --add-modules %JAVAFX_MODULES% ^
      -cp "%SQLITE_PATH%\*;%PROJECT_PATH%" ^
      -d out ^
      src/aracyonetim/App.java ^
      src/aracyonetim/controller/*.java ^
      src/aracyonetim/model/*.java ^
      src/aracyonetim/dao/*.java ^
      src/aracyonetim/db/*.java ^
      src/aracyonetim/util/*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Copy FXML and CSS files to output directory
echo Copying resource files...
xcopy /E /Y /I src\aracyonetim\view out\aracyonetim\view

REM Run the application
echo Running the application...
java --module-path "%JAVAFX_PATH%\lib" ^
     --add-modules %JAVAFX_MODULES% ^
     -cp "out;%SQLITE_PATH%\*" ^
     aracyonetim.App

pause 