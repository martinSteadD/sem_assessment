package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The countryReport class represents a data model for reporting
 * information about a country, including its code, name, continent, region,
 * population, and capital city.
 * <p>
 * This class is used to structure country-related data for reporting purposes.
 */
public class countryReport extends populationApp {

    /**
     * The ISO code representing the country.
     */
    public String code;

    /**
     * The name of the country.
     */
    public String name;

    /**
     * The continent on which the country is located.
     */
    public String continent;

    /**
     * The region within the continent where the country is situated.
     */
    public String region;

    /**
     * The total population of the country.
     */
    public int population;

    /**
     * The capital city of the country.
     */
    public String capital;

    /**
     * Retrieves all countries from the database ordered by population in descending order.
     *
     * @return a list of countryReport objects containing country data
     */
    public static ArrayList<countryReport> getAllCountriesByPopulation() {
        ArrayList<countryReport> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return countries;
        }

        try {
            Statement stmt = con.createStatement();
            String query = """
            SELECT country.code, country.name, country.continent, country.region,
                   country.population, city.name AS capital_name
            FROM country
            LEFT JOIN city ON country.capital = city.id
            ORDER BY country.population DESC;
        """;

            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                countryReport c = new countryReport();
                c.code = rset.getString("code");
                c.name = rset.getString("name");
                c.continent = rset.getString("continent");
                c.region = rset.getString("region");
                c.population = rset.getInt("population");
                c.capital = rset.getString("capital_name");
                countries.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving country data: " + e.getMessage());
        }

        return countries;
    }

    public static void outputCountryReport(ArrayList<countryReport> countries, String filename) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No country data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Code | Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");

        for (countryReport country : countries) {
            if (country == null) continue;
            sb.append("| " + country.code + " | " +
                    country.name + " | " +
                    country.continent + " | " +
                    country.region + " | " +
                    country.population + " | " +
                    country.capital + " |\r\n");
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




