# Calculator

An Android calculator written in Java. The whole expression is entered at once (with
parentheses and percentages) and evaluated by a custom parser: conversion to reverse
Polish notation followed by stack-based evaluation.

## Features

- Addition, subtraction, multiplication, division, percentages, parentheses, sign change
- Expression editing: cursor movement, deleting a character, clearing
- Evaluation via reverse Polish notation (no third-party engines)
- The result is preserved on screen rotation; a separate landscape layout
- A settings screen with a light/dark theme switch (DayNight)

## Tech stack

| Area     | Technologies                                |
|----------|---------------------------------------------|
| Language | Java 8                                      |
| UI       | Activity, ConstraintLayout, Material Components |
| Build    | Gradle 7.3.3, Android Gradle Plugin 7.0.4   |

## Project structure

```
app/src/main/java/com/tlenparty/calculator/
├── MainActivity.java     # keypad, expression parsing and evaluation
├── Choose_theme.java     # settings: theme selection
├── CalculatorLogic.java  # display state preserved across rotation
└── Actions.java          # enumeration of operations
```

## Build

Requires JDK 11–17 and Android SDK 30.

```bash
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`.
