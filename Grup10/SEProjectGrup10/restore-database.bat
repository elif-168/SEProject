@echo off
echo Running database restoration script...

REM Set JAVAFX library path
set JAVAFX_HOME=javafx-sdk-win
set PATH_TO_FX=%JAVAFX_HOME%\lib

REM Set database path
set DATABASE_PATH=database\aracyonetim.db

REM Delete the old database file if it exists
if exist %DATABASE_PATH% (
    echo Deleting old database file...
    del %DATABASE_PATH%
    echo Old database file deleted.
)

REM Create the database directory if it doesn't exist
if not exist database (
    echo Creating database directory...
    mkdir database
    echo Database directory created.
)

REM Run the database restoration application
echo Starting database restoration...
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp "./out/production/arac-yonetim-sistemi;./lib/*;./target/classes" RestoreDatabase

echo Database restoration completed.
echo.
echo You should now be able to log in using one of these accounts:
echo Email: admin@example.com    Password: admin    Role: FIRMA_YETKILISI
echo Email: external@example.com    Password: 123456    Role: DIS_FIRMA_YETKILISI
echo.
echo Waiting for 5 seconds before launching the application...
timeout /t 5

REM Launch the main application
echo Starting the main application...
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp "./out/production/arac-yonetim-sistemi;./lib/*;./target/classes" aracyonetim.App 