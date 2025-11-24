package com.napier.sem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Integration tests for languageReport SQL queries.
 * Requires live connection
 */
public class LanguageReportIntegrationTest {

    /**
     * Test that getAllLanguageReport() returns the 5 required languages
     * and contains valid numeric data.
     */
    @Test
    void testGetAllLanguageReport() {
        ArrayList<languageReport> results = languageReport.getAllLanguageReport();

        assertNotNull(results, "Result list should not be null");
        assertEquals(5, results.size(), "Expected exactly 5 language entries");

        for (languageReport lr : results) {
            assertNotNull(lr.language, "Language name must not be null");
            assertTrue(lr.speakers > 0, "Speakers count must be positive");
            assertTrue(lr.percentage > 0, "Percentage must be positive");
        }
    }

    /**
     * Ensures languages are ordered by number of speakers in DESC order.
     */
    @Test
    void testLanguageSorting() {
        ArrayList<languageReport> results = languageReport.getAllLanguageReport();

        long previous = Long.MAX_VALUE;

        for (languageReport lr : results) {
            assertTrue(lr.speakers <= previous,
                    "Languages must be sorted by speakers in descending order");
            previous = lr.speakers;
        }
    }
}
