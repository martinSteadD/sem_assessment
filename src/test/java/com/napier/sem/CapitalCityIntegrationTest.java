package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Integration tests for capitalCityReport database queries.
 * These tests require a running database.
 */
public class CapitalCityIntegrationTest {

    static Connection con;

    @BeforeAll
    static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "example"
            );

            populationApp.con = con; // ensure static reference is set
        } catch (Exception e) {
            fail("Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * Test that getAllCapitalCitiesByPopulation returns valid results.
     */
    @Test
    void testAllCapitalCitiesByPopulation() {
        ArrayList<capitalCityReport> capitals = capitalCityReport.getAllCapitalCitiesByPopulation(50);

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty(), "Expected at least one capital city");

        // Capitals must be sorted by population descending
        assertTrue(capitals.get(0).population >= capitals.get(capitals.size() - 1).population);
    }

    /**
     * Test continent-filtered capital cities.
     * Example: Europe should return European capitals only.
     */
    @Test
    void testCapitalCitiesByContinent() {
        ArrayList<capitalCityReport> capitals = capitalCityReport.getCapitalCitiesByContinent("Europe", 10);

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty(), "Expected European capital cities");

        for (capitalCityReport c : capitals) {
            assertNotNull(c.country);
            assertNotNull(c.name);
            assertTrue(c.population > 0);
        }
    }

    /**
     * Test region-filtered capital cities.
     */
    @Test
    void testCapitalCitiesByRegion() {
        ArrayList<capitalCityReport> capitals = capitalCityReport.getCapitalCitiesByRegion("Western Europe", 10);

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty(), "Expected Western Europe capitals");

        for (capitalCityReport c : capitals) {
            assertTrue(c.population > 0);
        }
    }

    /**
     * Test world top N capitals.
     * Should always return <= 10 based on capped limit logic.
     */
    @Test
    void testTopCapitalCitiesInWorld() {
        ArrayList<capitalCityReport> capitals = capitalCityReport.getTopCapitalCitiesInWorld(50);

        assertNotNull(capitals);
        assertTrue(capitals.size() <= 10, "Limit should cap results to 10");
        assertTrue(capitals.get(0).population > 0);
    }

    /**
     * Test top N capitals by continent.
     */
    @Test
    void testTopCapitalCitiesByContinent() {
        ArrayList<capitalCityReport> capitals = capitalCityReport.getTopCapitalCitiesByContinent("Asia", 50);

        assertNotNull(capitals);
        assertTrue(capitals.size() <= 10);
    }

    /**
     * Test top N capitals by region.
     */
    @Test
    void testTopCapitalCitiesByRegion() {
        ArrayList<capitalCityReport> capitals = capitalCityReport.getTopCapitalCitiesByRegion("Eastern Asia", 50);

        assertNotNull(capitals);
        assertTrue(capitals.size() <= 10);
    }
}
