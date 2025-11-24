package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for outputCountryReport() file creation and formatting.
 */
public class CountryReportUnitTest {

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Test writing a country report with valid data.
     */
    @Test
    void testOutputCountryReportWithData() {
        ArrayList<countryReport> mock = new ArrayList<>();
        countryReport c = new countryReport();
        c.code = "GBR";
        c.name = "United Kingdom";
        c.continent = "Europe";
        c.region = "British Isles";
        c.population = 67000000;
        c.capital = "London";
        mock.add(c);

        String fileName = "TestCountryReport.md";
        countryReport.outputCountryReport(mock, fileName);

        File f = new File("./reports/countryReports/" + fileName);
        assertTrue(f.exists(), "Expected report file to be created");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(f.toPath()));
            assertTrue(content.contains("| GBR | United Kingdom |"),
                    "Expected UK row in report file");
        } catch (Exception e) {
            fail("Failed to read generated report file");
        }
    }

    /**
     * Test behavior when the list is empty.
     */
    @Test
    void testOutputCountryReportEmptyList() {
        countryReport.outputCountryReport(new ArrayList<>(), "EmptyCountryReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No country data available"),
                "Expected message for empty dataset");
    }
}
