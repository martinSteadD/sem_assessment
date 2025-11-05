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

'src/test/java/com.napier.sem/'


Example unit test file: SimpleUnitTest.java  
Key checks:
- populationApp.main() runs without throwing exceptions
- Handles invalid database URLs gracefully
- Retry logic executes without errors
- Console output messages verified for connection attempts

---

### Integration Testing
Integration tests validate interactions with the database to ensure queries and data retrieval work correctly.

- Example integration test file: PopulationAppIntegrationTest.java
- Key checks:
    - Database connection established
    - Country table contains rows
    - City population queries return expected data
    - Can fetch top populated cities or other demographic information

Integration tests require a running Docker MySQL database (configured in docker-compose.yml) and are run separately from unit tests.

---

## 2. Test Execution

### Local Testing
1. Start the database using Docker Compose:

'docker compose up -d'

2. Run unit tests:

'mvn -Dtest=com.napier.sem.SimpleUnitTest test'

3. Run integration tests:

'mvn -Dtest=com.napier.sem.PopulationAppIntegrationTest test'

4. Stop Docker database after testing:

'docker compose down'


### GitHub Actions CI
- Workflow automatically runs on every push.
- Jobs:
  1. **UnitTests**: Runs SimpleUnitTest.java without database dependency.
  2. **IntegrationTests**: Spins up Docker MySQL, runs PopulationAppIntegrationTest.java, then shuts down Docker containers.
  3. **Build**: Packages the application JAR and runs Docker Compose for verification.

---

## 3. Best Practices
- Keep unit tests independent of the database.
- Integration tests should cover all database interactions.
- Avoid modifying main application code for testing; tests should adapt to current API.
- Use descriptive assertion messages to clarify test failures.
- Capture console output when testing main() method behavior.

---

## 4. Next Steps
- Expand integration tests for all use cases:
  - Countries by population
  - Cities by population
  - Capital city reports
  - Continents and regions population
- Ensure all GitHub Actions jobs pass consistently.
- Monitor code coverage and add tests for untested areas.

