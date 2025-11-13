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
                ORDER BY level, name;
            """;

            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                populationSummary c = new populationSummary();
                c.level = rset.getString("level");
                c.name = rset.getString("name");
                c.population = rset.getLong("population");

                popsums.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving population data: " + e.getMessage());
        }

        return popsums;
    }

    public static void outputPopSummary(ArrayList<populationSummary> popsums, String filename) {
        if (popsums == null || popsums.isEmpty()) {
            System.out.println("No population summary data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Level | Name | Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");

        for (populationSummary popsum : popsums) {
            if (popsum == null) continue;
            sb.append("| " +
                    popsum.name + " | " +
                    popsum.population + " | " +
                    popsum.level + " |\r\n");
        }

        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}








