package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleUnitTest {

    /**
     * Test that main() runs without throwing exceptions.
     * We capture the console output to verify expected messages.
     */
    @Test
    void testMainRunsWithoutException() {
        // Capture standard output to a stream for verification
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Run main with default arguments
            populationApp.main(new String[]{});
            String output = outContent.toString();

            // Verify that the output contains either a connection attempt or failure message
            assertTrue(output.contains("Connecting to database") || output.contains("Failed to connect"),
                    "Expected output to mention connecting to the database or failure message");
        } catch (Exception e) {
            // Fail the test if any exception occurs
            fail("populationApp.main() threw an exception: " + e.getMessage());
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Test main() with an invalid database URL.
     * Should handle failure gracefully and print "Failed to connect".
     */
    @Test
    void testMainWithInvalidDBURL() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Pass invalid URL as argument to simulate connection failure
            populationApp.main(new String[]{"jdbc:mysql://invalidhost:3306/world"});
            String output = outContent.toString();

            // Check that output contains the failure message
            assertTrue(output.contains("Failed to connect"),
                    "Expected output to mention failed connection with invalid URL");
        } catch (Exception e) {
            fail("populationApp.main() threw an exception: " + e.getMessage());
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }

    /**
     * Test that multiple connection attempts run without exceptions.
     * This simulates the retry loop in main().
     */
    @Test
    void testMultipleConnectionAttempts() {
        // Capture console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Run main normally to simulate retries
            populationApp.main(new String[]{});
            String output = outContent.toString();

            // Verify that at least one "Connecting to database..." message appears
            assertTrue(output.contains("Connecting to database"), "Expected output to show connection attempts");
        } catch (Exception e) {
            fail("populationApp.main() threw an exception during retries: " + e.getMessage());
        } finally {
            // Restore original System.out
            System.setOut(originalOut);
        }
    }
}
