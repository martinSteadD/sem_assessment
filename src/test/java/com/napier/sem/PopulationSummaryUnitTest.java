package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for population summary Markdown output.
 *
 * Tests:
 * Markdown file output
 * Correct formatting
 * Placeholder file creation for empty data
 */
public class PopulationSummaryUnitTest {

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
     * test for outputPopSummary() with valid mock data
     */
    @Test
    void testOutputPopSummaryWithData() {
        ArrayList<populationSummary> mock = new ArrayList<>();

        populationSummary ps = new populationSummary();
        ps.name = "Europe";
        ps.population = 710000000L;
        ps.level = "Continent";
        mock.add(ps);

        String fileName = "TestPopSummary.md";
        populationSummary.outputPopSummary(mock, fileName);

        File f = new File("./reports/populationReports/" + fileName);
        assertTrue(f.exists(), "Expected output file to exist");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(f.toPath()));
            assertTrue(content.contains("Europe"), "Expected name 'Europe'");
            assertTrue(content.contains("710000000"), "Expected population number");
            assertTrue(content.contains("Continent"), "Expected level 'Continent'");
        } catch (IOException e) {
            fail("Error reading generated file");
        }
    }

    /**
     * test for outputPopSummary() with empty list
     */
    @Test
    void testOutputPopSummaryEmpty() {
        populationSummary.outputPopSummary(new ArrayList<>(), "EmptyPopSummary.md");

        String output = outContent.toString();
        assertTrue(output.contains("No population summary data available"),
                "Expected console message for empty summary list");
    }
}
