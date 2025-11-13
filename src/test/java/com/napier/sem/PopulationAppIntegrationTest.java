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
        int retries = 10; // Number of connection retries
        int delayMs = 2000; // Delay between retries (in milliseconds)

        for (int i = 0; i < retries; i++) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Connect to the local Docker MySQL database
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:33060/world?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example"
                );

                System.out.println("Connected to database on attempt " + (i + 1));
                return; // exit loop once connected - success
            } catch (Exception e) {
                System.out.println("DB not ready, retrying... (" + (i + 1) + "/" + retries + ")");
                try {
                    Thread.sleep(delayMs); //wait before next retry
                } catch (InterruptedException ignored) {}
            }
        }

        // Fail the test suite if connection cannot be established
        fail("Failed to connect to the database after " + retries + " attempts");
    }

    /**
     * Test that the database connection is successfully established.
     * Fails if the connection object is null.
     */
    @Test
    void testDatabaseConnection() {
        // Ensure the database connection is not null
        assertNotNull(con, "Database connection should not be null");
    }

    /**
     * Test that the 'country' table contains at least one record.
     * Executes a COUNT query and checks the result is greater than zero.
     */
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

    /**
     * Test that the 'city' table contains data and top cities by population
     * can be retrieved. Verifies that the highest population is greater than zero.
     */
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



    // future tests will be added for each of the reports
}
