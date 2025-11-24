package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for outputCapitalCityReport().
 * These tests do not require connection
 */
public class CapitalCityUnitTest {

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
     * Verify placeholder file generation when list is empty.
     */
    @Test
    void testOutputCapitalCityReport_EmptyList() {
        ArrayList<capitalCityReport> list = new ArrayList<>();

        capitalCityReport.outputCapitalCityReport(list, "EmptyCapitalReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No capital city data available"),
                "Expected notification for empty report");
    }

    /**
     * Verify a valid Markdown report is created with correct content.
     */
    @Test
    void testOutputCapitalCityReport_WithData() {
        ArrayList<capitalCityReport> list = new ArrayList<>();
        capitalCityReport c = new capitalCityReport();
        c.name = "Paris";
        c.country = "France";
        c.population = 2148000;
        list.add(c);

        String filename = "TestCapitalReport.md";
        capitalCityReport.outputCapitalCityReport(list, filename);

        File file = new File("./reports/capitalCityReports/" + filename);
        assertTrue(file.exists(), "Expected output file to exist");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            assertTrue(content.contains("| Paris | France |"),
                    "Expected data row for Paris");
        } catch (IOException e) {
            fail("Failed to read generated file");
        }
    }

    /**
     * Verify that null entries in the list do not crash the report generator.
     */
    @Test
    void testOutputCapitalCityReport_WithNullEntry() {
        ArrayList<capitalCityReport> list = new ArrayList<>();
        list.add(null); // add a null entry

        capitalCityReport.outputCapitalCityReport(list, "NullEntryReport.md");

        File file = new File("./reports/capitalCityReports/NullEntryReport.md");
        assertTrue(file.exists(), "Expected output file to be created even with null entry");
    }
}
