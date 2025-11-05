package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PopulationAppIntegrationTest {

    static Connection con;

    @BeforeAll
    static void init() {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to Docker MySQL database
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "example"
            );
        } catch (Exception e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }

    @Test
    void testDatabaseConnection() {
        assertNotNull(con, "Database connection should not be null");
    }

    @Test
    void testCountryCount() {
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM country");
            assertTrue(rs.next(), "ResultSet should have at least one row");
            int count = rs.getInt("total");
            assertTrue(count > 0, "Country table should have at least one row");
        } catch (Exception e) {
            fail("Error querying country table: " + e.getMessage());
        }
    }

    @Test
    void testCityPopulationQuery() {
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT Name, Population FROM city ORDER BY Population DESC LIMIT 5"
            );
            assertTrue(rs.next(), "ResultSet should have at least one city");
            int topPopulation = rs.getInt("Population");
            assertTrue(topPopulation > 0, "Top city population should be greater than zero");
        } catch (Exception e) {
            fail("Error querying city table: " + e.getMessage());
        }
    }

    // Optional: add more tests for continents, regions, capitals, etc.
}
