package com.napier.sem;

import org.junit.jupiter.api.*;
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

    @Test
    void testOutputCountryReportWithEmptyList() {
        countryReport.outputCountryReport(new ArrayList<>(), "EmptyReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No country data available."),
                "Expected message for empty country list");
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
}
