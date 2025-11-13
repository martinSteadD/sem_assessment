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

    public static ArrayList<populationReport> getAllPopulation() {
        ArrayList<populationReport> pops = new ArrayList<>();

        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return pops;
        }

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
            System.out.println("Error retrieving country data: " + e.getMessage());
        }

        return pops;
    }

    public static void outputPopReport(ArrayList<populationReport> pops, String filename) {
        if (pops == null || pops.isEmpty()) {
            System.out.println("No population data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Total Pop | City Pop | City % | Non City Pop | Non City % |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");

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
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

