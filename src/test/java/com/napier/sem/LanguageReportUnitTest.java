package com.napier.sem;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for verifying Markdown output for languageReport.
 * These do not require a live database.
 */
public class LanguageReportUnitTest {

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
     * Verify that a valid report file is generated with proper formatting.
     */
    @Test
    void testOutputLanguageReportWithData() {
        ArrayList<languageReport> mock = new ArrayList<>();

        languageReport lr = new languageReport();
        lr.language = "English";
        lr.speakers = 1200000000L;
        lr.percentage = 15.23;
        mock.add(lr);

        String fileName = "TestLanguageReport.md";
        languageReport.outputLanguageReport(mock, fileName);

        File f = new File("./reports/languageReports/" + fileName);
        assertTrue(f.exists(), "Expected language report file to be created");

        try {
            String content = new String(java.nio.file.Files.readAllBytes(f.toPath()));
            assertTrue(content.contains("English"), "Expected language 'English' in output file");
            assertTrue(content.contains("1,200,000,000"), "Expected formatted speaker count");
            assertTrue(content.contains("15.23%"), "Expected formatted percentage");
        } catch (Exception e) {
            fail("Failed to read generated report file");
        }
    }

    /**
     * Test output behavior for empty or null data.
     */
    @Test
    void testOutputLanguageReportEmpty() {
        languageReport.outputLanguageReport(new ArrayList<>(), "EmptyLanguageReport.md");

        String output = outContent.toString();
        assertTrue(output.contains("No language data available"),
                "Expected message indicating no data");
    }
}
