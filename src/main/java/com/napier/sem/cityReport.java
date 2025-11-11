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


    public static ArrayList<cityReport> getAllCitiesByPopulation() {
        ArrayList<cityReport> cities = new ArrayList<>();

        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return cities;
        }

        try {
            Statement stmt = con.createStatement();
            String query = """
                SELECT ci.name AS city_name, co.name AS country_name, ci.district, ci.population
                FROM city ci
                LEFT JOIN country co ON  ci.id = co.capital
                ORDER BY ci.population DESC;
            """;

            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("city_name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving country data: " + e.getMessage());
        }

        return cities;
    }

    public static void outputCityReport(ArrayList<cityReport> cities, String filename) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");

        for (cityReport city : cities) {
            if (city == null) continue;
            sb.append("| " +
                    city.name + " | " +
                    city.country + " | " +
                    city.district + " | " +
                    city.population + " |\r\n");
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
