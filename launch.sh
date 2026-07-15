#!/usr/bin/env bash

# Exit immediately if any command fails
set -e

echo "========================================"
echo "      Compiling & Launching Game        "
echo "========================================"

echo "Cleaning old class files..."
find . -name "*.class" -delete

echo "Compiling source files..."
mkdir -p bin
find . -name "*.java" > sources.txt
javac -d bin @sources.txt
rm sources.txt

# 3. Run the application
echo "Starting application..."
echo "========================================"
java -cp bin Main
