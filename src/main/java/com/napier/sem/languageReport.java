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
 * The languageReport class represents a data model for reporting
 * information about a spoken language, including the number of speakers and
 * its share of the global population.
 * <p>
 * This class is typically used to structure language-related data for reporting purposes.
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

    public ArrayList<languageReport> getLanguageReport(Connection con) {
        ArrayList<languageReport> reports = new ArrayList<>();

        try {
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

