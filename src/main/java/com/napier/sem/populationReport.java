package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The populationReport class represents a data model for reporting
 * population statistics of a geographic area, including total population,
 * city population, and non-city population breakdowns.
 * <p>
 * This class is used to structure population-related data for reporting purposes.
 */
public class populationReport extends populationApp {

    /**
     * The name of the geographic area (e.g., country, region, or city group).
     */
    public String name;

    /**
     * The total population of the area.
     */
    public int totalPopulation;

    /**
     * The population living in cities within the area.
     */
    public int cityPopulation;

    /**
     * The percentage of the total population that lives in cities.
     */
    public double cityPercentage;

    /**
     * The population living outside cities (non-urban areas).
     */
    public int nonCityPopulation;

    /**
     * The percentage of the total population that lives outside cities.
     */
    public double nonCityPercentage;


    /**
     * Retrieves population statistics for all countries, including:
     * Total population
     * Population living in cities
     * Population living outside cities
     * Percentage of people in cities and non-city areas
     *
     * @return ArrayList of populationReport objects containing country population data.
     */
    public static ArrayList<populationReport> getAllPopulation() {
        ArrayList<populationReport> pops = new ArrayList<>();

        // Prevent writing empty or null data
        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return pops;
        }

        // SQL query:
        // Gets country name and total population
        // Sums populations of all cities in that country (IFNULL handles countries without cities)
        // Calculates city % and non-city % of the population
        // Groups by country to get one row per country
        // Orders results alphabetically by country name
        try {
            Statement stmt = con.createStatement();
            String query = """
                    SELECT
                        country.Name AS name,
                        country.Population AS totalPopulation,
                        IFNULL(SUM(city.Population), 0) AS cityPopulation,
                        ROUND((IFNULL(SUM(city.Population), 0) / country.Population) * 100, 2) AS cityPercentage,
                        (country.Population - IFNULL(SUM(city.Population), 0)) AS nonCityPopulation,
                        ROUND(((country.Population - IFNULL(SUM(city.Population), 0)) / country.Population) * 100, 2) AS nonCityPercentage
                    FROM country
                    LEFT JOIN city ON country.Code = city.CountryCode
                    GROUP BY country.Code, country.Name, country.Population
                    ORDER BY country.Name;
                """;

            ResultSet rset = stmt.executeQuery(query);

            // Convert SQL results into populationReport objects
            while (rset.next()) {
                populationReport c = new populationReport();
                c.name = rset.getString("name");
                c.totalPopulation = rset.getInt("totalPopulation");
                c.cityPopulation = rset.getInt("CityPopulation");
                c.cityPercentage = rset.getDouble("CityPercentage");
                c.nonCityPopulation = rset.getInt("NonCityPopulation");
                c.nonCityPercentage = rset.getDouble("NonCityPercentage");
                pops.add(c);
            }
        } catch (Exception e) {
            // Print error if anything goes wrong during query execution or processing
            System.out.println("Error retrieving country data: " + e.getMessage());
        }

        return pops;
    }

    /**
     * Outputs a list of city reports into a markdown-formatted file.
     * Each city appears as a row in a Markdown table.
     *
     * @param pops List of populationReport objects to write.
     * @param filename Name of the output file to generate.
     */
    public static void outputPopReport(ArrayList<populationReport> pops, String filename) {
        // Prevent writing empty or null data
        if (pops == null || pops.isEmpty()) {
            System.out.println("No population data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Total Pop | City Pop | City % | Non City Pop | Non City % |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");

        // Build Markdown table rows from the population data
        for (populationReport pop : pops) {
            if (pop == null) continue;
            sb.append("| " +
                    pop.name + " | " +
                    pop.totalPopulation + " | " +
                    pop.cityPopulation + " | " +
                    pop.cityPercentage + " | " +
                    pop.nonCityPopulation + " | " +
                    pop.nonCityPercentage + " |\r\n");
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

