# Project Setup Instructions

## Required Dependencies

Please download the following JAR files and place them in the `lib` directory:

1. iText PDF (already in lib):

   - Download from: https://github.com/itext/itextpdf/releases/download/5.5.13.3/itextpdf-5.5.13.3.jar

2. Apache POI (for Excel) (already in lib):

   - Download from: https://dlcdn.apache.org/poi/release/bin/poi-bin-5.2.3-20220909.zip
   - Extract and copy these files to lib:
     - poi-5.2.3.jar
     - poi-ooxml-5.2.3.jar
     - poi-ooxml-lite-5.2.3.jar

3. Apache Commons CSV (already in lib):
   - Download from: https://dlcdn.apache.org/commons/csv/binaries/commons-csv-1.10.0-bin.zip
   - Extract and copy commons-csv-1.10.0.jar to lib

## Directory Structure

Make sure your project has the following structure:

```
SEProject/
├── javafx-sdk-win/     (Your JavaFX SDK)
├── lib/                (All dependency JARs)
├── src/
│   └── aracyonetim/    (Source code)
├── out/                (Compiled classes)
└── run.bat            (Run script)
```

## How to Run

1. Make sure you have Java 17 or later installed
2. Make sure all dependencies are in the lib directory
3. Double-click run.bat or run it from the command line

## Troubleshooting

If you get any errors:

1. Make sure JAVA_HOME is set to Java 17 or later
2. Verify all JAR files are in the lib directory
3. Check that JavaFX SDK is in the javafx-sdk-win directory
