@echo on
setlocal enabledelayedexpansion

REM Set paths
set "JAVAFX_PATH=%~dp0javafx-sdk-win"
set "LIB_PATH=%~dp0lib"
set "PROJECT_PATH=%~dp0src"
set "OUT_PATH=%~dp0out"

echo JAVAFX_PATH: %JAVAFX_PATH%
echo LIB_PATH: %LIB_PATH%
echo PROJECT_PATH: %PROJECT_PATH%
echo OUT_PATH: %OUT_PATH%

REM Create output directory if it doesn't exist
if not exist "%OUT_PATH%" mkdir "%OUT_PATH%"

REM Check JavaFX JARs
if not exist "%JAVAFX_PATH%\lib\javafx.controls.jar" (
    echo ERROR: JavaFX controls not found at %JAVAFX_PATH%\lib\javafx.controls.jar
    goto error
)
if not exist "%JAVAFX_PATH%\lib\javafx.fxml.jar" (
    echo ERROR: JavaFX FXML not found at %JAVAFX_PATH%\lib\javafx.fxml.jar
    goto error
)

REM Check libs
if not exist "%LIB_PATH%\itextpdf-5.5.13.3.jar" (
    echo WARNING: iText PDF library not found. Reports may not work.
)

echo =================
echo Setting classpath
echo =================

REM Build classpath with all JARs
set CLASSPATH="%OUT_PATH%;%PROJECT_PATH%"
for %%f in ("%LIB_PATH%\*.jar") do set CLASSPATH=!CLASSPATH!;%%f
for %%f in ("%JAVAFX_PATH%\lib\*.jar") do set CLASSPATH=!CLASSPATH!;%%f

echo CLASSPATH: %CLASSPATH%
echo.

REM Compile the project
echo ==================
echo Compiling project
echo ==================
javac -d "%OUT_PATH%" -cp %CLASSPATH% ^
    src\aracyonetim\App.java ^
    src\aracyonetim\controller\*.java ^
    src\aracyonetim\model\*.java ^
    src\aracyonetim\dao\*.java ^
    src\aracyonetim\db\*.java ^
    src\aracyonetim\util\*.java

if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    goto error
)

REM Copy FXML files
echo =============================
echo Copying FXML and resource files
echo =============================
xcopy /y /i "src\aracyonetim\view\*.fxml" "out\aracyonetim\view\"
mkdir "%OUT_PATH%\database" 2>nul
copy /y "database\init.sql" "%OUT_PATH%\database\" 2>nul

REM Run the application
echo ====================
echo Running application
echo ====================
echo.
java --module-path "%JAVAFX_PATH%\lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     -cp %CLASSPATH% ^
     aracyonetim.App

if %errorlevel% neq 0 (
    echo ERROR: Application execution failed!
    goto error
)

goto end

:error
echo.
echo ==============================
echo An error occurred running the application.
echo ==============================
pause
exit /b 1

:end
echo.
echo Application completed successfully.
pause 