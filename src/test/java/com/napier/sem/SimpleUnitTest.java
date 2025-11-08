package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testConnectionFailureMessage() {
        // Simulate failed connection by calling connect with invalid host and 1 retry
        app.connect("invalidhost:3306", 0);  // Should fail quickly
        String output = outContent.toString();

        assertTrue(output.contains("Failed to connect"),
                "Expected output to mention failed connection");
    }

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

        app.outputCountryReport(mockCountries, "TestCountryReport.md");

        // Check that the Markdown content was generated
        String output = outContent.toString();
        assertTrue(output.contains("No country data available.") == false,
                "Expected country report to be generated");
    }

    @Test
    void testConnectionAttemptLogging() {
        app.connect("invalidhost:3306", 0);  // Simulate retry loop
        String output = outContent.toString();

        assertTrue(output.contains("Connecting to database"),
                "Expected output to show connection attempts");
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
