package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleUnitTest {

    /**
     * Test that main() runs without throwing exceptions.
     * We capture the console output and check for expected messages.
     */
    @Test
    void testMainRunsWithoutException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Run main with default args
            populationApp.main(new String[]{});
            String output = outContent.toString();

            // Check that either a connection attempt or failure message was printed
            assertTrue(output.contains("Connecting to database") || output.contains("Failed to connect"),
                    "Expected output to mention connecting to the database or failure message");
        } catch (Exception e) {
            fail("populationApp.main() threw an exception: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test main() with an invalid database URL.
     * Should not throw exceptions, should print "Failed to connect".
     */
    @Test
    void testMainWithInvalidDBURL() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Pass invalid URL as argument
            populationApp.main(new String[]{"jdbc:mysql://invalidhost:3306/world"});
            String output = outContent.toString();

            assertTrue(output.contains("Failed to connect"),
                    "Expected output to mention failed connection with invalid URL");
        } catch (Exception e) {
            fail("populationApp.main() threw an exception: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Test that multiple connection attempts run without throwing exceptions.
     * This simulates the retry loop in main().
     */
    @Test
    void testMultipleConnectionAttempts() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            populationApp.main(new String[]{});
            String output = outContent.toString();

            // There should be at least one "Connecting to database..." printed
            assertTrue(output.contains("Connecting to database"), "Expected output to show connection attempts");
        } catch (Exception e) {
            fail("populationApp.main() threw an exception during retries: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }
}
