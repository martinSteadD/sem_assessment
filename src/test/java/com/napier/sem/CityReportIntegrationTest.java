package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for cityReport database queries.
 * These tests require a running MySQL
 */
public class CityReportIntegrationTest {

    static Connection con;

    @BeforeAll
    static void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "example"
            );

            // Ensure static reference is available
            populationApp.con = con;
        }
        catch (Exception e) {
            fail("Could not connect to database: " + e.getMessage());
        }
    }

    /**
     * Test main world city population report.
     */
    @Test
    void testAllCitiesByPopulation() {
        ArrayList<cityReport> cities = cityReport.getAllCitiesByPopulation(50);

        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Expected at least 1 city in results");

        assertTrue(
                cities.get(0).population >= cities.get(cities.size() - 1).population,
                "Cities should be ordered by population DESC"
        );
    }

    /**
     * continent-specific report.
     */
    @Test
    void testCitiesByContinent() {
        ArrayList<cityReport> cities = cityReport.getCitiesByContinent("Europe", 50);

        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Expected European cities");

        for (cityReport c : cities) {
            assertNotNull(c.name);
            assertNotNull(c.country);
            assertTrue(c.population > 0);
        }
    }

    /**
     * region-specific report.
     */
    @Test
    void testCitiesByRegion() {
        ArrayList<cityReport> cities = cityReport.getCitiesByRegion("Western Europe", 50);

        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Expected cities in Western Europe");
    }

    /**
     * country-specific report.
     */
    @Test
    void testCitiesByCountry() {
        ArrayList<cityReport> cities = cityReport.getCitiesByCountry("France", 50);

        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Expected cities in France");
    }

    /**
     * district-specific report.
     */
    @Test
    void testCitiesByDistrict() {
        ArrayList<cityReport> cities = cityReport.getCitiesByDistrict("Île-de-France", 50);

        assertNotNull(cities);
        // Not guaranteed to exist, but query must not crash:
        assertNotNull(cities, "Query should not return null");
    }

    /**
     * top N world cities.
     */
    @Test
    void testTopCitiesInWorld() {
        ArrayList<cityReport> cities = cityReport.getTopCitiesInWorld(50);

        assertNotNull(cities);
        assertTrue(cities.size() <= 10, "Limit should cap results to 10");
    }

    /**
     * top N cities by continent.
     */
    @Test
    void testTopCitiesByContinent() {
        ArrayList<cityReport> cities = cityReport.getTopCitiesByContinent("Asia", 40);

        assertNotNull(cities);
        assertTrue(cities.size() <= 10);
    }

    /**
     * top N cities by region.
     */
    @Test
    void testTopCitiesByRegion() {
        ArrayList<cityReport> cities = cityReport.getTopCitiesByRegion("Southern Europe", 40);

        assertNotNull(cities);
        assertTrue(cities.size() <= 10);
    }

    /**
     * top N cities by district.
     */
    @Test
    void testTopCitiesByDistrict() {
        ArrayList<cityReport> cities = cityReport.getTopCitiesByDistrict("California", 40);

        assertNotNull(cities);
        assertTrue(cities.size() <= 10);
    }

    /**
     * top N cities by country.
     */
    @Test
    void testTopCitiesByCountry() {
        ArrayList<cityReport> cities = cityReport.getTopCitiesByCountry("Germany", 40);

        assertNotNull(cities);
        assertTrue(cities.size() <= 10);
    }
}
