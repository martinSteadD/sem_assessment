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
    public long totalPopulation;

    /**
     * The population living in cities within the area.
     */
    public long cityPopulation;

    /**
     * The percentage of the total population that lives in cities.
     */
    public double cityPercentage;

    /**
     * The population living outside cities (non-urban areas).
     */
    public long nonCityPopulation;

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
    public static ArrayList<populationReport> getPopulationByContinent() {
        ArrayList<populationReport> pops = new ArrayList<>();
        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return pops;
        }

        try {
            Statement stmt = con.createStatement();
            String query = """
            SELECT
                country.Continent AS name,
                SUM(country.Population) AS totalPopulation,
                IFNULL(SUM(city.Population), 0) AS cityPopulation,
                ROUND((IFNULL(SUM(city.Population), 0) / SUM(country.Population)) * 100, 2) AS cityPercentage,
                (SUM(country.Population) - IFNULL(SUM(city.Population), 0)) AS nonCityPopulation,
                ROUND(((SUM(country.Population) - IFNULL(SUM(city.Population), 0)) / SUM(country.Population)) * 100, 2) AS nonCityPercentage
            FROM country
            LEFT JOIN city ON country.Code = city.CountryCode
            GROUP BY country.Continent
            ORDER BY totalPopulation DESC;
        """;

            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                populationReport p = new populationReport();
                p.name = rset.getString("name");
                p.totalPopulation = rset.getLong("totalPopulation");
                p.cityPopulation = rset.getLong("cityPopulation");
                p.cityPercentage = rset.getDouble("cityPercentage");
                p.nonCityPopulation = rset.getLong("nonCityPopulation");
                p.nonCityPercentage = rset.getDouble("nonCityPercentage");
                pops.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving continent data: " + e.getMessage());
        }
        return pops;
    }

    public static ArrayList<populationReport> getPopulationByRegion() {
        ArrayList<populationReport> pops = new ArrayList<>();
        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return pops;
        }

        try {
            Statement stmt = con.createStatement();
            String query = """
            SELECT
                country.Region AS name,
                SUM(country.Population) AS totalPopulation,
                IFNULL(SUM(city.Population), 0) AS cityPopulation,
                ROUND((IFNULL(SUM(city.Population), 0) / SUM(country.Population)) * 100, 2) AS cityPercentage,
                (SUM(country.Population) - IFNULL(SUM(city.Population), 0)) AS nonCityPopulation,
                ROUND(((SUM(country.Population) - IFNULL(SUM(city.Population), 0)) / SUM(country.Population)) * 100, 2) AS nonCityPercentage
            FROM country
            LEFT JOIN city ON country.Code = city.CountryCode
            GROUP BY country.Region
            ORDER BY totalPopulation DESC;
        """;

            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                populationReport p = new populationReport();
                p.name = rset.getString("name");
                p.totalPopulation = rset.getLong("totalPopulation");
                p.cityPopulation = rset.getLong("cityPopulation");
                p.cityPercentage = rset.getDouble("cityPercentage");
                p.nonCityPopulation = rset.getLong("nonCityPopulation");
                p.nonCityPercentage = rset.getDouble("nonCityPercentage");
                pops.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving region data: " + e.getMessage());
        }
        return pops;
    }

    public static ArrayList<populationReport> getPopulationByCountry() {
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
            ORDER BY totalPopulation DESC;
        """;

            ResultSet rset = stmt.executeQuery(query);
            while (rset.next()) {
                populationReport p = new populationReport();
                p.name = rset.getString("name");
                p.totalPopulation = rset.getLong("totalPopulation");
                p.cityPopulation = rset.getLong("cityPopulation");
                p.cityPercentage = rset.getDouble("cityPercentage");
                p.nonCityPopulation = rset.getLong("nonCityPopulation");
                p.nonCityPercentage = rset.getDouble("nonCityPercentage");
                pops.add(p);
            }
        } catch (Exception e) {
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
        if (pops == null || pops.isEmpty()) {
            try {
                File dir = new File("./reports/populationReports/");
                dir.mkdirs();
                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# Population Report\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No population data available, wrote placeholder file.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| Name | Total Pop | City Pop | City % | Non City Pop | Non City % |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");

        for (populationReport pop : pops) {
            if (pop == null) continue;
            sb.append("| ")
                    .append(pop.name).append(" | ")
                    .append(pop.totalPopulation).append(" | ")
                    .append(pop.cityPopulation).append(" | ")
                    .append(String.format("%.2f", pop.cityPercentage)).append("% | ")
                    .append(pop.nonCityPopulation).append(" | ")
                    .append(String.format("%.2f", pop.nonCityPercentage)).append("% |\r\n");
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

