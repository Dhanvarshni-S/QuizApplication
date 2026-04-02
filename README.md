Role-Based Online Quiz Application
Overview

The Role-Based Online Quiz Application is a Java-based application that allows users to take quizzes based on their assigned roles. It is designed to showcase core Java programming concepts including inheritance, threads, interfaces, exception handling, packages, and OOP principles. The application integrates with MongoDB for storing user data, quiz questions, and results.

This project can be used for educational purposes or as a foundation for more advanced quiz and learning platforms.

Features
Role-Based Access
Admin: Can add, update, and delete quiz questions.
User: Can take quizzes assigned to their role.
Quiz Management
Supports multiple-choice questions (MCQs).
Time-limited quizzes using Java threads.
Result Tracking
Stores and retrieves quiz results from MongoDB.
Exception Handling
Graceful handling of invalid inputs, database connection errors, and timeouts.
Modular Design
Clean package structure for easy maintenance and scalability.
Interface & OOP
Use of interfaces and inheritance for flexible role management.
Technologies Used
Programming Language: Java (Core Java)
Database: MongoDB
Concepts Implemented:
Inheritance
Threads
Interfaces
Exception Handling
Packages
Object-Oriented Programming (OOP)
QuizApp/
│
├── frontend/                   # Basic frontend
│   ├── index.html
│   ├── script.js
│   └── styles.css
│
├── lib/                        # Required libraries
│   ├── bson-4.11.1.jar
│   ├── mongodb-driver-core-4.x.jar
│   └── mongodb-driver-sync-4.x.jar
│
└── src/                        # Java backend
    ├── Admin.java
    ├── Handlers.java
    ├── Main.java
    ├── MongoDBConnection.java
    ├── Participant.java
    ├── Question.java
    ├── Quiz.java
    ├── QuizOperations.java
    ├── Server.java
    ├── SimpleServer.java
    ├── TestDB.java
    ├── TimerThread.java
    └── User.java
