# TESTING.md

## Overview
This document outlines the testing strategy for our DevOps population-reporting application.  
Our goal is to ensure that both the logic of the program and its interaction with the database (unit + integration) are thoroughly validated.

---

## 1. Testing Strategy

### Unit Testing
Unit tests check the program logic that does not depend on a database.  
JUnit 5 is used for writing and running tests.

Examples of what unit tests cover:
- Input validation
- Output generation
- Handling of empty or invalid data
- File writing logic

Unit test file:
- "SimpleUnitTest.java"

---

### Integration Testing
Integration tests verify the connection to the MySQL database and test SQL queries that retrieve data.  
They run automatically using GitHub Actions with a Dockerized MySQL instance.

Examples of what integration tests check:
- Database connection can be established
- Tables contain valid data (e.g., countries, cities)
- Queries return correct population results

Integration test file:
- "PopulationAppIntegrationTest.java"

---

## 2. Test Execution

### Running Test Locally
1. Open the project in an IDE (such as IntelliJ or VS Code).
2. Ensure Maven and JDK 17 are installed.
3. Run the following command in the terminal:
   
    mvn test
   
4. Check the console output for test results.

---

### Running Tests in GitHub Actions
The GitHub Actions workflow (github/workflows/main.yml) runs tests automatically when code is pushed.  
It has two jobs:
- **UnitTests:** runs JUnit tests without database.
- **IntegrationTests:** starts a MySQL Docker container and runs database tests.

---


### GitHub Actions CI
- Workflow automatically runs on every push.
- Jobs:
  1. **UnitTests**: Runs SimpleUnitTest.java without database dependency.
  2. **IntegrationTests**: Spins up Docker MySQL, runs PopulationAppIntegrationTest.java, then shuts down Docker containers.
  3. **Build**: Packages the application JAR and runs Docker Compose for verification.

---

## 3. Example Tests

### Unit Test Example
- Tests that the "outputCountryReport()" method prints a message when no data is provided.
- Tests that mock data generates a proper report file.

### Integration Test Example
- Tests that the connection to MySQL works.
- Tests that querying the "country" table returns data.
- Tests that top city population queries return positive values.

---

## 4. Next Steps
When more report types are implemented (e.g., continent, region, capital city, language, population summary), new test methods will be added for:
- Query correctness
- Output formatting
- Data completeness

## 5. Notes
- All tests are using JUnit 5.
- Integration tests think the database container has port 33060.
- Tests will run by themselves in GitHub actions.

