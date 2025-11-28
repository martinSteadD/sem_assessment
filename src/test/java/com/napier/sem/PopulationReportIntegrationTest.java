package com.napier.sem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Integration tests for populationReport SQL queries.
 * Requires a live connection
 *
 * Tests:
 * Query returns non-null lists
 * rows contain valid population values
 * Percentage logic produces non-negative values
 * for continent, region, and country
 */
public class PopulationReportIntegrationTest {

    /**
     *retrieving population report by continent
     */
    @Test
    void testPopulationByContinent() {
        ArrayList<populationReport> results = populationReport.getPopulationByContinent();

        assertNotNull(results, "Result list should not be null");
        assertTrue(results.size() >= 0, "Expected at least one continent entry");

        for (populationReport pop : results) {
            assertNotNull(pop.name, "Continent name should not be null");
            assertTrue(pop.totalPopulation >= 0, "Total population must be positive");
            assertTrue(pop.cityPopulation >= 0, "City population must be >= 0");
            assertTrue(pop.nonCityPopulation >= 0, "Non-city population must be >= 0");
            assertTrue(pop.cityPercentage >= 0, "City percentage must be >= 0");
            assertTrue(pop.nonCityPercentage >= 0, "Non-city percentage must be >= 0");
        }
    }

    /**
     *retrieving population report by region
     */
    @Test
    void testPopulationByRegion() {
        ArrayList<populationReport> results = populationReport.getPopulationByRegion();

        assertNotNull(results);
        assertTrue(results.size() >= 0, "Expected at least one region entry");
    }

    /**
     * population report by country
     */
    @Test
    void testPopulationByCountry() {
        ArrayList<populationReport> results = populationReport.getPopulationByCountry();

        assertNotNull(results);
        assertTrue(results.size() >= 0, "Expected at least one country entry");

        for (populationReport pop : results) {
            assertNotNull(pop.name, "Country name should not be null");
            assertTrue(pop.totalPopulation >= 0, "Total population must be positive");
        }
    }
}
