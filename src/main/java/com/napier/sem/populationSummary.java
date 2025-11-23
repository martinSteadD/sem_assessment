package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The populationSummary class represents a simplified data model
 * for reporting the total population of a geographic area.
 * <p>
 * This class is typically used for high-level summaries where detailed
 * breakdowns (e.g., city vs non-city) are not required.
 */
public class populationSummary extends populationApp{

    /**
     * The name of the geographic area (e.g., country, region, or continent).
     */
    public String name;

    /**
     * The total population of the specified area.
     */
    public long population;

    /**
     * The level of area (Continent, Region, Country)
     */
    public String level;


    /**
     * Retrieves a high-level population summary across continents, regions, and countries.
     * Each row indicates the total population for a geographic level:
     * Continent
     * Region
     * Country
     *
     * @return ArrayList of populationSummary objects containing population totals.
     */
    public static ArrayList<populationSummary> getAllPopulationSummary() {
        ArrayList<populationSummary> popsums = new ArrayList<>();

        // Prevent writing empty or null data
        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return popsums;
        }

        try {
            Statement stmt = con.createStatement();

            // SQL query:
            // First subquery: sum population by continent
            // Second subquery: sum population by region
            // Third subquery: population by individual country
            // UNION ALL combines all levels into a single result set
            // The result is ordered by 'level' (Continent, Region, Country) and then by name
            String query = """
                (
                    
                    SELECT
                        Continent AS name,
                        SUM(Population) AS population,
                        'Continent' AS level
                    FROM country
                    GROUP BY Continent
                )
                UNION ALL
                (
                    
                    SELECT
                        Region AS name,
                        SUM(Population) AS population,
                        'Region' AS level
                    FROM country
                    GROUP BY Region
                )
                UNION ALL
                (

                    SELECT
                        Name AS name,
                        Population AS population,
                        'Country' AS level
                    FROM country
                )
                ORDER BY level, name
            """;

            ResultSet rset = stmt.executeQuery(query);

            // Convert SQL results into populationSummary objects
            while (rset.next()) {
                populationSummary c = new populationSummary();
                c.name = rset.getString("name");
                c.population = rset.getLong("population");
                c.level = rset.getString("level");
                popsums.add(c);
            }
        } catch (Exception e) {
            // Print error if anything goes wrong during query execution or processing
            System.out.println("Error retrieving population data: " + e.getMessage());
        }

        return popsums;
    }


    /**
     * Outputs a list of city reports into a markdown-formatted file.
     * Each city appears as a row in a Markdown table.
     *
     * @param popsums List of populationSummary objects to write.
     * @param filename Name of the output file to generate.
     */
    public static void outputPopSummary(ArrayList<populationSummary> popsums, String filename) {
        if (popsums == null || popsums.isEmpty()) {
            System.out.println("No population summary data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Population | Level |\r\n");
        sb.append("| --- | --- | --- |\r\n");

        // Build Markdown table rows from the population summary data
        for (populationSummary popsum : popsums) {
            if (popsum == null) continue;
            sb.append("| " +
                    popsum.name + " | " +
                    popsum.population + " | " +
                    popsum.level + " |\r\n");
        }

        try {
            // Ensure reports/ directory exists
            new File("./reports/").mkdir();
            // Write Markdown content to specified file
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            // Print details if writing the file fails
            e.printStackTrace();
        }
    }

}








