# TESTING.md

## Overview
This document outlines the testing strategy for our DevOps population-reporting application.  
Our goal is to ensure that both the logic of the program and its interaction with the database (unit + integration) are thoroughly validated.

---

## 1. Testing Strategy

### Unit Testing
Unit tests verify the smallest components of the system in isolation, without requiring a database connection.  
We use **JUnit 5** as our testing framework.

- Example target methods:
    - Input validation
    - Data formatting
    - SQL query string generation
    - Report ordering and filtering logic

Unit test files are stored under:
