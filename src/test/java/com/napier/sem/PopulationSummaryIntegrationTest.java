package com.napier.sem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Integration tests for populationSummary SQL queries
 * Requires connection to db
 *
 *Tests:
 * Query returns results
 * World summary exists
 * Continent summary exists
 * Country summary exists
 * Values are not null and no negative
 */
public class PopulationSummaryIntegrationTest {

    /**
     * retrieving the full population summary list
     */
    @Test
    void testPopulationSummaryQuery() {

        ArrayList<populationSummary> results = populationSummary.getAllPopulationSummary();

        assertNotNull(results, "Result list should not be null");
        assertTrue(results.size() >= 0, "Population summary should contain entries");

        //ensure key levels exist
        boolean hasWorld = false;
        boolean hasContinent = false;
        boolean hasCountry = false;

        for (populationSummary ps : results) {
            //basic field validation
            assertNotNull(ps.name, "Name must be non-null");
            assertTrue(ps.population >= 0, "Population must be non-negative");
            assertNotNull(ps.level, "Level must be non-null");

            //check presence of expected summary types
            if (ps.level.equalsIgnoreCase("World")) hasWorld = true;
            if (ps.level.equalsIgnoreCase("Continent")) hasContinent = true;
            if (ps.level.equalsIgnoreCase("Country")) hasCountry = true;
        }

        assertTrue(hasWorld, "Expected a world-level summary");
        assertTrue(hasContinent, "Expected at least one continent-level summary");
        assertTrue(hasCountry, "Expected at least one country-level summary");
    }
}
