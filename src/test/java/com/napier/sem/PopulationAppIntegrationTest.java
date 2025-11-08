package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PopulationAppIntegrationTest {

    // Connection object used for all tests
    static Connection con;

    @BeforeAll
    static void init() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the local Docker MySQL database
            // Port 33060 is mapped to Docker container's 3306
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "example"
            );
        } catch (Exception e) {
            // Fail the test suite if connection cannot be established
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseConnection() {
        // Ensure the database connection is not null
        assertNotNull(con, "Database connection should not be null");
    }

    @Test
    void testCountryCount() {
        try (Statement stmt = con.createStatement()) {
            // Query the total number of countries
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM country");
            assertTrue(rs.next(), "ResultSet should have at least one row");

            int count = rs.getInt("total");
            // Check that the country table contains data
            assertTrue(count > 0, "Country table should have at least one row");
        } catch (Exception e) {
            fail("Error querying country table: " + e.getMessage());
        }
    }

    @Test
    void testCityPopulationQuery() {
        try (Statement stmt = con.createStatement()) {
            // Query top 5 cities by population
            ResultSet rs = stmt.executeQuery(
                    "SELECT Name, Population FROM city ORDER BY Population DESC LIMIT 5"
            );
            assertTrue(rs.next(), "ResultSet should have at least one city");

            int topPopulation = rs.getInt("Population");
            // Validate that the top city's population is positive
            assertTrue(topPopulation > 0, "Top city population should be greater than zero");
        } catch (Exception e) {
            fail("Error querying city table: " + e.getMessage());
        }
    }

    // Optional: Add more integration tests for continents, regions, capital cities, language queries, etc.
}
