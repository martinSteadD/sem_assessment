package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The {@code languageReport} class represents a data model and reporting utility
 * for spoken language information, including the number of speakers and
 * the percentage of the world's population that speaks the language.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Acts as a simple container for language data retrieved from the database</li>
 *   <li>Provides static methods to query language data for a fixed set of target languages:
 *       <ul>
 *         <li>Chinese</li>
 *         <li>English</li>
 *         <li>Hindi</li>
 *         <li>Spanish</li>
 *         <li>Arabic</li>
 *       </ul>
 *   </li>
 *   <li>Generates Markdown‑formatted reports from query results</li>
 * </ul>
 * <p>
 * Workflow:
 * <ol>
 *   <li>SQL queries are executed via JDBC using {@code populationApp.con}</li>
 *   <li>World population is calculated from the {@code country} table</li>
 *   <li>Speakers for target languages are aggregated from the {@code countrylanguage} table</li>
 *   <li>Percentages are computed relative to the world population</li>
 *   <li>Results are mapped into {@code languageReport} objects</li>
 *   <li>Output methods format the data into Markdown tables and write them to files</li>
 * </ol>
 * <p>
 * Edge‑case handling:
 * <ul>
 *   <li>If no database connection exists, methods return an empty list</li>
 *   <li>If input lists are {@code null} or empty, output methods generate placeholder files</li>
 *   <li>File I/O errors are caught and logged without halting execution</li>
 *   <li>If world population cannot be retrieved, percentages default to {@code 0.0}</li>
 * </ul>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * // Retrieve language report for Chinese, English, Hindi, Spanish, and Arabic
 * ArrayList<languageReport> languages =
 *     languageReport.getAllLanguageReport();
 *
 * // Output results to Markdown file
 * languageReport.outputLanguageReport(languages, "GlobalLanguages.md");
 * }</pre>
 */
public class languageReport {

    /**
     * The name of the language.
     */
    public String language;

    /**
     * The total number of people who speak the language.
     */
    public long speakers;

    /**
     * The percentage of the world's population that speaks this language.
     */
    public double percentage;

    /**
     * Retrieves the language report for Chinese, English, Hindi, Spanish, and Arabic,
     * including the number of speakers and their share of the global population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Uses the shared database connection from {@code populationApp.con}</li>
     *   <li>Calculates the total world population from the {@code country} table</li>
     *   <li>Aggregates speaker counts for the target languages from the {@code countrylanguage} table</li>
     *   <li>Computes each language's percentage of the world population</li>
     *   <li>Maps results into {@link languageReport} objects</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If no database connection exists, an empty list is returned</li>
     *   <li>If world population cannot be retrieved, percentages default to {@code 0.0}</li>
     *   <li>If SQL execution fails, the stack trace is printed and partial/empty results may be returned</li>
     * </ul>
     *
     * @return an {@code ArrayList} of {@link languageReport} objects containing language name,
     *         number of speakers, and percentage of world population; may be empty if no connection or query fails
     */
    public static ArrayList<languageReport> getAllLanguageReport() {
        ArrayList<languageReport> reports = new ArrayList<>();

        try {
            Connection con = populationApp.con; // use the shared connection

            // Step 1: Get world population
            long worldPopulation = 0;
            try (PreparedStatement stmtWorld = con.prepareStatement(
                    "SELECT SUM(Population) AS WorldPopulation FROM country")) {
                ResultSet rsWorld = stmtWorld.executeQuery();
                if (rsWorld.next()) {
                    worldPopulation = rsWorld.getLong("WorldPopulation");
                }
            }

            // Step 2: Get speakers for target languages
            String sql = "SELECT cl.Language, SUM(c.Population * cl.Percentage / 100) AS Speakers " +
                    "FROM countrylanguage cl " +
                    "JOIN country c ON cl.CountryCode = c.Code " +
                    "WHERE cl.Language IN (?, ?, ?, ?, ?) " +
                    "GROUP BY cl.Language " +
                    "ORDER BY Speakers DESC";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, "Chinese");
                stmt.setString(2, "English");
                stmt.setString(3, "Hindi");
                stmt.setString(4, "Spanish");
                stmt.setString(5, "Arabic");

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    languageReport lr = new languageReport();
                    lr.language = rs.getString("Language");
                    lr.speakers = rs.getLong("Speakers");
                    lr.percentage = (worldPopulation > 0)
                            ? (double) lr.speakers / worldPopulation * 100
                            : 0.0;
                    reports.add(lr);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reports;
    }

    /**
     * Outputs a list of language reports into a Markdown-formatted file.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if the provided list of {@link languageReport} objects is {@code null} or empty</li>
     *   <li>If empty, generates a placeholder Markdown file with a "No results found" message</li>
     *   <li>If data exists, builds a Markdown table with headers: Language, Speakers, % of World Population</li>
     *   <li>Writes one row per language into the table</li>
     *   <li>Saves the file under {@code ./reports/languageReports/} with the given filename</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code languages} is {@code null} or empty, a placeholder file is created</li>
     *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
     * </ul>
     *
     * @param languages list of {@link languageReport} objects to write; may be {@code null} or empty
     * @param filename  name of the output file to generate (e.g., {@code "GlobalLanguages.md"})
     */
    public static void outputLanguageReport(ArrayList<languageReport> languages, String filename) {
        if (languages == null || languages.isEmpty()) {
            try {
                File dir = new File("./reports/languageReports/");
                dir.mkdirs();
                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# Language Report\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No language data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| Language | Speakers | % of World Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");

        for (languageReport lang : languages) {
            sb.append("| ")
                    .append(lang.language).append(" | ")
                    .append(String.format("%,d", lang.speakers)).append(" | ")
                    .append(String.format("%.2f%%", lang.percentage)).append(" |\r\n");
        }

        try {
            File dir = new File("./reports/languageReports/");
            dir.mkdirs();
            File outFile = new File(dir, filename);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                writer.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
