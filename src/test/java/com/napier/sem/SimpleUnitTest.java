package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for verifying behaviour of report-generation methods that do not
 * require a database connection.
 * Tests:
 * Markdown file output behaviour
 * Console messages shown during report generation
 * Handling of both valid data and empty datasets
 */

public class SimpleUnitTest {

    private populationApp app;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    /**
     * Before each test:
     * Create a new instance
     * Redirect System.out to temporary
     * Capture all console output for verification
     */
    @BeforeEach
    void setUp() {
        app = new populationApp();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    /**
     * outputCountryReport() should successfully generate a Markdown report when provided with valid mock data
     */
    @Test
    void testOutputCountryReportWithMockData() {
        ArrayList<countryReport> mockCountries = new ArrayList<>();
        countryReport c = new countryReport();
        c.code = "GBR";
        c.name = "United Kingdom";
        c.continent = "Europe";
        c.region = "British Isles";
        c.population = 67000000;
        c.capital = "London";
        mockCountries.add(c);

        countryReport.outputCountryReport(mockCountries, "TestCountryReport.md");

        String output = outContent.toString();
        assertFalse(output.contains("No country data available."),
                "Expected country report to be generated");
    }

    /**
     * outputCountryReport() should print a fallback message and generate
     * a placeholder Markdown file when the provided list is empty
     */

    @Test
    void testOutputCountryReportWithEmptyList() {
        countryReport.outputCountryReport(new ArrayList<>(), "EmptyReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No country data available, wrote placeholder file."),
                "Expected message for empty country list");

    }

    /**
     * After each test:
     * Restore System.out to its original state
     * Ensures no interference between test cases
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
