# Expense Tracker Service

A Java-based backend service for tracking personal expenses with persistent storage and RESTful APIs.
Implements clean layering and simple financial record management using Spring Boot.

## Short Description
A Spring Boot application that allows creating, viewing, updating, and deleting expense records.
Designed with a clear separation of concerns and database-backed state management.

## Tech Stack
- Language: Java 17
- Frameworks: Spring Boot, Spring Web, Spring Data JPA, Hibernate
- Build Tool: Maven
- Database: PostgreSQL

## Key Features
- Create and manage expense records
- Categorize expenses for better tracking
- Retrieve expenses by ID or as a list
- Persistent storage using JPA and PostgreSQL
- Layered architecture with controller, service, and repository
- Basic validation and structured error handling

## API Endpoints

Create Expense

POST /api/expenses

Request Body:
    { "title": "Groceries", "amount": 1200, "category": "FOOD", "expenseDate": "2025-01-10" }

Response:
    201 Created

---

Get Expense by ID

GET /api/expenses/{id}

Response:
    200 OK
    404 Not Found

---

Get All Expenses (with optional pagination)

GET /api/expenses?page=0&size=10

Query Params:
    page = default 0
    size = default 10

Response:
    Paged list of expenses

---

Update Expense

PUT /api/expenses/{id}

Request Body:
    { "title": "Groceries", "amount": 1300, "category": "FOOD", "expenseDate": "2025-01-10" }

Response:
    200 OK
    404 Not Found

---

Delete Expense

DELETE /api/expenses/{id}

Response:
    204 No Content
    404 Not Found

## Getting Started

Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL instance

Run Instructions
- Configure database properties in application.properties
- Build the project using `mvn clean install`
- Run the application using `mvn spring-boot:run`
- Service runs on port 8080

## Project Structure

    src/main/java
    └── com.example.expensetracker
        ├── ExpenseTrackerApplication.java
        │   -> Spring Boot application entry point
        │
        ├── controller
        │   └── ExpenseController.java
        │       -> Exposes REST APIs for managing expenses
        │
        ├── service
        │   └── ExpenseService.java
        │       -> Contains business logic for expense operations
        │
        ├── repository
        │   └── ExpenseRepository.java
        │       -> JPA repository for expense persistence
        │
        ├── entity
        │   └── Expense.java
        │       -> JPA entity representing an expense record
        │
        └── dto
            ├── ExpenseRequest.java
            │   -> Request payload for creating or updating expenses
            └── ExpenseResponse.java
                -> API response model for expense data


## Why This Project Matters
The service demonstrates core backend fundamentals such as RESTful API design, relational data modeling, transactional persistence, and clean code organization aligned with real-world backend systems.
