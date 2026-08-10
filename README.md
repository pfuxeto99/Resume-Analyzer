# DevFilter

DevFilter is an **ATS (Applicant Tracking System) optimization platform** designed for IT and Computer Science students.

Many technically skilled graduates are rejected by automated recruitment systems because their resumes are not optimized for ATS parsing. Poor formatting, missing technical keywords, weak project sections, and inconsistent resume structures can prevent candidates from passing initial screening stages.

DevFilter addresses this problem by analyzing technical resumes and providing recommendations to improve **ATS compatibility, resume structure, and keyword optimization**.

## Features

* ATS resume analysis
* Keyword matching
* CV optimization recommendations
* Resume structure analysis
* Technical keyword identification
* Modern JavaFX dashboard interface

## Technologies

* **Java**
* **JavaFX**
* **HTML**
* **CSS**

# Requirements

Before running DevFilter, make sure the following software is installed.

### 1. Java JDK 21

DevFilter requires a Java Development Kit (JDK) to compile and run the application.

**Download Java JDK 21:**

https://adoptium.net/temurin/releases/?version=21

After installing Java, verify that it is installed correctly by opening Command Prompt or a terminal and running:

```bash
java --version
```

You should see information about your installed Java version.

### 2. Eclipse IDE

DevFilter is designed to be opened and run using **Eclipse IDE**.

**Download Eclipse:**

https://www.eclipse.org/downloads/

Install Eclipse and make sure it is configured to use your installed JDK.


### 3. JavaFX SDK 26.0.2

DevFilter uses JavaFX for its graphical user interface.

**Download JavaFX:**

https://gluonhq.com/products/javafx/

Download the **JavaFX SDK** for your operating system.

For this project, JavaFX **26.0.2** was used during development.

After downloading the SDK, extract it to a location on your computer.

For example:

```text
C:\Eclipse\javafx-sdk-26.0.2
```

Inside the JavaFX SDK there should be a `lib` folder containing the JavaFX libraries:

```text
javafx-sdk-26.0.2
└── lib
    ├── javafx.base.jar
    ├── javafx.controls.jar
    ├── javafx.fxml.jar
    ├── javafx.graphics.jar
    └── ...
```
# Installation and Setup

## 1. Clone the Repository

Clone the DevFilter repository from GitHub:

```bash
git clone https://github.com/pfuxeto99/Resume-Analyzer.git
```

Alternatively, download the repository as a ZIP file from GitHub and extract it.


## 2. Open the Project in Eclipse

1. Open **Eclipse IDE**.
2. Select **File → Import**.
3. Select **Existing Projects into Workspace**.
4. Select the DevFilter project folder.
5. Click **Finish**.

Make sure Eclipse is using the correct JDK.

You can check this under:

**Window → Preferences → Java → Installed JREs**

Select your installed JDK.


# JavaFX Configuration

DevFilter requires the JavaFX SDK to be configured before running the application.

## 1. Locate the JavaFX `lib` Folder

For example:

```text
C:\Eclipse\javafx-sdk-26.0.2\lib
```

Your path may be different depending on where you extracted JavaFX.


## 2. Configure VM Arguments

In Eclipse:

1. Right-click the main application.
2. Select **Run As → Run Configurations**.
3. Select your Java application.
4. Open the **Arguments** tab.
5. Find the **VM arguments** section.
6. Add the following:

```text
--module-path "C:\Eclipse\javafx-sdk-26.0.2\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```

### Important

If your JavaFX SDK is located somewhere else, change the path.

For example, if your JavaFX SDK is located at:

```text
C:\Users\YourName\Downloads\javafx-sdk-26.0.2
```

your VM arguments would be:

```text
--module-path "C:\Users\YourName\Downloads\javafx-sdk-26.0.2\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```

The module path **must point to the ****`lib`**** folder**.


# Running the Application

After configuring JavaFX:

1. Open the main application class in Eclipse.
2. Right-click the class.
3. Select **Run As → Java Application**.

The DevFilter JavaFX application should launch.

If JavaFX is configured correctly, the application's graphical interface will open.


# User Interface

DevFilter uses **JavaFX** to provide a desktop graphical user interface.

The interface is designed to provide users with access to resume analysis, ATS-related feedback, keyword matching, and optimization recommendations.

Screenshots can be added to this section to demonstrate the application's interface.

Example:

```markdown
## Screenshots

![DevFilter Dashboard](screenshots/dashboard.png)
```

Place screenshots inside a `screenshots` folder in the project repository.



# ATS Resume Analysis

DevFilter focuses on several areas that can affect how a resume performs when processed by an Applicant Tracking System.

These include:

* Technical keywords
* Resume structure
* Relevant skills
* Project descriptions
* Technical experience
* Keyword relevance
* Overall ATS compatibility

The goal is to help students identify areas where their resumes can be improved before submitting applications.


# Future Improvements

Potential future improvements to DevFilter include:

* More advanced ATS scoring
* Job-description matching
* Industry-specific resume recommendations
* Support for additional resume formats
* Improved keyword analysis
* More detailed resume analytics
* Resume comparison against job descriptions
* Automated resume optimization
* Exporting optimized resumes
* Additional user interface improvements

# Development Environment

DevFilter was developed using:

* **Java**
* **JavaFX 26.0.2**
* **Eclipse IDE**
* **HTML**
* **CSS**


The application was developed as a JavaFX-based desktop application.


# Author

**Mabunda Pfuxeto**

Computer Science Student
Software Developer


# Project Purpose

DevFilter was developed to help IT and Computer Science students improve their resumes and better understand how Applicant Tracking Systems evaluate and filter candidate applications.

The project combines software development, user interface design, resume analysis, and ATS optimization into a single application.



