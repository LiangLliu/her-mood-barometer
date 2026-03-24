#!/usr/bin/env bash

# ADB screenshot script
# Usage: ./screenshot.sh <save-path/filename.png>
# Example: ./screenshot.sh ./screencap/record.png

set -euo pipefail

# Switch to project root (parent of the script directory)
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_ROOT" || exit 1

if ! command -v adb &> /dev/null; then
    echo "Error: adb command not found. Please install Android SDK platform-tools."
    exit 1
fi

# Check if a device is connected
if ! adb devices 2>/dev/null | grep -qw "device"; then
    echo "Error: No Android device connected. Please connect a device or start an emulator."
    exit 1
fi

if [ -z "${1:-}" ]; then
    echo "Error: Please specify the screenshot save path"
    echo "Usage: $0 <filepath.png>"
    echo "Example: $0 ./screencap/record.png"
    exit 1
fi

# Handle absolute and relative paths
INPUT_PATH="$1"
if [[ "$INPUT_PATH" = /* ]]; then
    OUTPUT_PATH="$INPUT_PATH"
else
    OUTPUT_PATH="$PROJECT_ROOT/$INPUT_PATH"
fi

TEMP_PATH="/sdcard/screenshot_tmp.png"

# Create target directory if it doesn't exist
OUTPUT_DIR=$(dirname "$OUTPUT_PATH")
mkdir -p "$OUTPUT_DIR"

echo "Taking screenshot..."
if ! adb shell screencap -p "$TEMP_PATH"; then
    echo "Error: Screenshot failed, please check device connection"
    exit 1
fi

echo "Pulling to $OUTPUT_PATH ..."
if ! adb pull "$TEMP_PATH" "$OUTPUT_PATH"; then
    echo "Error: Failed to pull file"
    exit 1
fi

echo "Cleaning up temp file on device..."
adb shell rm "$TEMP_PATH"

echo "Screenshot saved to: $OUTPUT_PATH"
