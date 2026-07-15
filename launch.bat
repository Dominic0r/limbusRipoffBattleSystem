@echo off
title Compiling and Launching Game
echo ========================================
echo       Compiling ^& Launching Game        
echo ========================================

echo Cleaning old class files...
del /s /q *.class >nul 2>&1

echo Compiling source files...
if not exist bin mkdir bin

dir /s /b *.java > sources.txt
javac -d bin @sources.txt
del sources.txt

echo Starting application...
echo ========================================
java -cp bin Main
pause
