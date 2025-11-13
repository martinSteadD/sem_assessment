package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The cityReport class represents a data model for reporting
 * information about a city, including its name, country, district, and population.
 * <p>
 * This class is used to structure city-related data for reporting purposes.
 */
public class cityReport extends populationApp {

    /**
     * The name of the city.
     */
    public String name;

    /**
     * The country in which the city is located.
     */
    public String country;

    /**
     * The district or administrative region of the city.
     */
    public String district;

    /**
     * The population of the city.
     */
    public int population;

    /**
     * Retrieves all cities in the world ordered by population (desc)
     * including the associated country and district information.
     *
     * @return ArrayList of cityReport objects containing city details
     */
    public static ArrayList<cityReport> getAllCitiesByPopulation() {
        ArrayList<cityReport> cities = new ArrayList<>();

        // Ensure database connection is available before executing query
        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return cities;
        }

        try {
            Statement stmt = con.createStatement();

            // SQL query:
            // Returns city name, country name, district, and population
            // Joins city to country using CountryCode
            // Sorts results by population (largest first)
            String query = """
                SELECT
                    ci.name AS city_name,
                    co.name AS country_name,
                    ci.district,
                    ci.population
                FROM city ci
                LEFT JOIN country co ON ci.CountryCode = co.Code
                ORDER BY ci.population DESC;
                
            """;

            ResultSet rset = stmt.executeQuery(query);

            // Convert SQL result rows into cityReport objects
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("city_name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            // Handle any SQL or data processing errors
            System.out.println("Error retrieving country data: " + e.getMessage());
        }

        return cities;
    }

    /**
     * Outputs a list of city reports into a markdown-formatted file.
     * Each city appears as a row in a Markdown table.
     *
     * @param cities List of cityReport objects to write.
     * @param filename Name of the output file to generate.
     */
    public static void outputCityReport(ArrayList<cityReport> cities, String filename) {
        // Prevent writing empty or null data
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");

        // Write one row per city
        for (cityReport city : cities) {
            if (city == null) continue;
            sb.append("| " +
                    city.name + " | " +
                    city.country + " | " +
                    city.district + " | " +
                    city.population + " |\r\n");
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
