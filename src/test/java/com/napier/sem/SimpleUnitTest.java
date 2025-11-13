package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for populationApp class methods that don't require
 * a database connection. Focuses on data output and formatting.
 */
public class SimpleUnitTest {

    private populationApp app;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        app = new populationApp();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Test that outputCountryReport correctly prints a message
     * when given an empty list.
     */
    @Test
    void testOutputCountryReportWithEmptyList() {
        app.outputCountryReport(new ArrayList<>(), "EmptyReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No country data available."),
                "Expected message for empty country list");
    }

    /**
     * Test that outputCountryReport generates Markdown for valid data.
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

        String filePath = "TestCountryReport.md";
        app.outputCountryReport(mockCountries, filePath);

        // Verify console output does not indicate missing data
        String output = outContent.toString();
        assertFalse(output.contains("No country data available."),
                "Expected country report to be generated");

        // Verify Markdown file was created and contains the expected content
        File f = new File("./reports/" + filePath);
        assertTrue(f.exists(), "Expected report file to exist");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(f.toPath()));
            assertTrue(content.contains("| GBR | United Kingdom |"),
                    "Expected report content for United Kingdom");
        } catch (IOException e) {
            fail("Error reading generated report file");
        }
    }

    /**
     * Test that printCountryReport prints table headers properly.
     */
    @Test
    void testPrintCountryReportHeader() {
        ArrayList<countryReport> mock = new ArrayList<>();
        countryReport c = new countryReport();
        c.code = "USA";
        c.name = "United States";
        c.continent = "North America";
        c.region = "North America";
        c.population = 331000000;
        c.capital = "Washington D.C.";
        mock.add(c);

        app.printCountryReport(mock);

        String output = outContent.toString();
        assertTrue(output.contains("Code"), "Expected table header 'Code'");
        assertTrue(output.contains("United States"), "Expected output to include country name");
    }

    // -------------------------------------------------------
    // Placeholder tests for future reports (disabled for now)
    // -------------------------------------------------------

    // @Test
    // void testOutputCityReport_NotYetImplemented() {
    //     fail("City report output not yet implemented");
    // }

    // @Test
    // void testOutputLanguageReport_NotYetImplemented() {
    //     fail("Language report output not yet implemented");
    // }
}
