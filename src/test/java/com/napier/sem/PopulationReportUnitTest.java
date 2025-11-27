package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Markdown output of populationReport.
 * Not require any connection
 *
 * Tests:
 * Markdown file creation
 * Correct format
 * Writing properr placeholder file if input empty
 */
public class PopulationReportUnitTest {

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
     *outputPopReport() method with valid mock data
     */
    @Test
    void testOutputPopReportWithData() {
        ArrayList<populationReport> mock = new ArrayList<>();

        populationReport p = new populationReport();
        p.name = "Europe";
        p.totalPopulation = 700_000_000L;
        p.cityPopulation = 450_000_000L;
        p.cityPercentage = 64.28;
        p.nonCityPopulation = 250_000_000L;
        p.nonCityPercentage = 35.72;

        mock.add(p);

        String fileName = "TestPopulationReport.md";
        populationReport.outputPopReport(mock, fileName);

        File f = new File("./reports/populationReports/" + fileName);
        assertTrue(f.exists(), "Expected population report file to be created");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(f.toPath()));
            assertTrue(content.contains("Europe"), "Expected entry for Europe");
            assertTrue(content.contains("700000000"), "Expected total population value");
            assertTrue(content.contains("64.28%"), "Expected formatted percentage");
        } catch (Exception e) {
            fail("Error reading output file");
        }
    }

    /**
     *check the empty dataset case writes placeholder file
     */
    @Test
    void testOutputPopReportEmpty() {
        populationReport.outputPopReport(new ArrayList<>(), "EmptyPopReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No population data available"),
                "Expected message for empty population list");
    }
}
