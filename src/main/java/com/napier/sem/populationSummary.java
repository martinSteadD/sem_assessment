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

        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return popsums;
        }

        try {
            Statement stmt = con.createStatement();

            String query = """
            (
                SELECT
                    'World' AS name,
                    SUM(Population) AS population,
                    'World' AS level
                FROM country
            )
            UNION ALL
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
            UNION ALL
            (
                SELECT
                    District AS name,
                    SUM(Population) AS population,
                    'District' AS level
                FROM city
                GROUP BY District
            )
            UNION ALL
            (
                SELECT
                    Name AS name,
                    Population AS population,
                    'City' AS level
                FROM city
            )
            ORDER BY level, name;
        """;

            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                populationSummary ps = new populationSummary();
                ps.name = rset.getString("name");
                ps.population = rset.getLong("population");
                ps.level = rset.getString("level");
                popsums.add(ps);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving population summary data: " + e.getMessage());
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
            try {
                File dir = new File("./reports/populationReports/");
                dir.mkdirs();
                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# Population Summary\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No population summary data available, wrote placeholder file.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| Name | Population | Level |\r\n");
        sb.append("| --- | --- | --- |\r\n");

        for (populationSummary popsum : popsums) {
            if (popsum == null) continue;
            sb.append("| ")
                    .append(popsum.name).append(" | ")
                    .append(popsum.population).append(" | ")
                    .append(popsum.level).append(" |\r\n");
        }

        try {
            File dir = new File("./reports/populationReports/");
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








