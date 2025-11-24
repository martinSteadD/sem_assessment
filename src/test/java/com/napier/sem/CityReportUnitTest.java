package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for outputCityReport.
 * These tests do noty require a  connection
 */
public class CityReportUnitTest {

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
     * placeholder file is created when list is empty.
     */
    @Test
    void testOutputCityReport_EmptyList() {
        ArrayList<cityReport> list = new ArrayList<>();

        cityReport.outputCityReport(list, "EmptyCityReport.md");

        String output = outContent.toString();
        assertTrue(
                output.contains("No city data available"),
                "Expected message for empty city list"
        );
    }

    /**
     * proper Markdown output is created when valid data exists.
     */
    @Test
    void testOutputCityReport_WithData() {
        ArrayList<cityReport> list = new ArrayList<>();

        cityReport c = new cityReport();
        c.name = "Berlin";
        c.country = "Germany";
        c.district = "Berlin";
        c.population = 3600000;

        list.add(c);

        String filename = "TestCityReport.md";

        cityReport.outputCityReport(list, filename);

        File file = new File("./reports/cityReports/" + filename);
        assertTrue(file.exists(), "Expected Markdown output file");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            assertTrue(
                    content.contains("| Berlin | Germany | Berlin | 3600000 |"),
                    "Expected Berlin row in report"
            );
        }
        catch (IOException e) {
            fail("Could not read report file");
        }
    }

    /**
     * null entries in list do not crash output.
     */
    @Test
    void testOutputCityReport_WithNullEntry() {
        ArrayList<cityReport> list = new ArrayList<>();
        list.add(null);

        cityReport.outputCityReport(list, "NullCityReport.md");

        File file = new File("./reports/cityReports/NullCityReport.md");
        assertTrue(file.exists(), "Expected file to be created even with null entries");
    }
}
