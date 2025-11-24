package com.napier.sem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Integration tests for countryReport database queries.
 * These tests require a running MySQL database (Docker).
 */
public class CountryReportIntegrationTest {

    /**
     * Test retrieving all countries ordered by population.
     * Ensures the query returns data and the first entry has valid fields.
     */
    @Test
    void testGetAllCountriesByPopulation() {
        ArrayList<countryReport> results =
                countryReport.getAllCountriesByPopulation(10);

        assertNotNull(results, "List should not be null");
        assertFalse(results.isEmpty(), "List should not be empty");

        countryReport first = results.get(0);
        assertNotNull(first.name, "Country name should not be null");
        assertTrue(first.population > 0, "Population must be positive");
    }

    /**
     * Test retrieving countries filtered by continent.
     */
    @Test
    void testGetCountriesByContinent() {
        ArrayList<countryReport> results =
                countryReport.getCountriesByContinent("Europe", 10);

        assertNotNull(results);
        assertFalse(results.isEmpty(), "Expected European countries");

        for (countryReport c : results) {
            assertEquals("Europe", c.continent,
                    "Expected only countries from Europe");
        }
    }

    /**
     * Test retrieving countries filtered by region.
     */
    @Test
    void testGetCountriesByRegion() {
        ArrayList<countryReport> results =
                countryReport.getCountriesByRegion("Western Europe", 10);

        assertNotNull(results);
        assertFalse(results.isEmpty(), "Expected Western European countries");

        for (countryReport c : results) {
            assertEquals("Western Europe", c.region,
                    "Expected region: Western Europe");
        }
    }

    /**
     * Test top N countries by population.
     */
    @Test
    void testTopCountriesByPopulation() {
        ArrayList<countryReport> results =
                countryReport.getTopCountriesByPopulation(5);

        assertNotNull(results);
        assertEquals(5, results.size(), "Should return exactly 5 entries");

        int prev = Integer.MAX_VALUE;
        for (countryReport c : results) {
            assertTrue(c.population <= prev, "List must be sorted DESC");
            prev = c.population;
        }
    }
}
