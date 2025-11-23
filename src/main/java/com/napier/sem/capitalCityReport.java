package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The capitalCityReport class represents a data model for reporting
 * information about a capital city, including its name, country, and population.
 * <p>
 * This class is intended to be used as a simple container for capital city data
 * for reporting purposes.
 */
public class capitalCityReport {

    /**
     * The name of the capital city.
     */
    public String name;

    /**
     * The country in which the capital city is located.
     */
    public String country;

    /**
     * The population of the capital city.
     */
    public int population;



    public static ArrayList<capitalCityReport> getAllCapitalCitiesByPopulation(int limit) {
        ArrayList<capitalCityReport> capitals = new ArrayList<>();
        if (populationApp.con == null) return capitals;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name AS capital_name, country.name AS country_name, city.population
        FROM city
        JOIN country ON city.id = country.capital
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setInt(1, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                capitalCityReport c = new capitalCityReport();
                c.name = rset.getString("capital_name");
                c.country = rset.getString("country_name");
                c.population = rset.getInt("population");
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return capitals;
    }
    public static ArrayList<capitalCityReport> getCapitalCitiesByContinent(String continent, int limit) {
        ArrayList<capitalCityReport> capitals = new ArrayList<>();
        if (populationApp.con == null) return capitals;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name AS capital_name, country.name AS country_name, city.population
        FROM city
        JOIN country ON city.id = country.capital
        WHERE country.continent = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                capitalCityReport c = new capitalCityReport();
                c.name = rset.getString("capital_name");
                c.country = rset.getString("country_name");
                c.population = rset.getInt("population");
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return capitals;
    }

    public static ArrayList<capitalCityReport> getCapitalCitiesByRegion(String region, int limit) {
        ArrayList<capitalCityReport> capitals = new ArrayList<>();
        if (populationApp.con == null) return capitals;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name AS capital_name, country.name AS country_name, city.population
        FROM city
        JOIN country ON city.id = country.capital
        WHERE country.region = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                capitalCityReport c = new capitalCityReport();
                c.name = rset.getString("capital_name");
                c.country = rset.getString("country_name");
                c.population = rset.getInt("population");
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return capitals;
    }


    public static ArrayList<capitalCityReport> getTopCapitalCitiesInWorld(int limit) {
        ArrayList<capitalCityReport> capitals = new ArrayList<>();
        if (populationApp.con == null) return capitals;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name AS capital_name, country.name AS country_name, city.population
        FROM city
        JOIN country ON city.id = country.capital
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setInt(1, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                capitalCityReport c = new capitalCityReport();
                c.name = rset.getString("capital_name");
                c.country = rset.getString("country_name");
                c.population = rset.getInt("population");
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return capitals;
    }

    public static ArrayList<capitalCityReport> getTopCapitalCitiesByContinent(String continent, int limit) {
        ArrayList<capitalCityReport> capitals = new ArrayList<>();
        if (populationApp.con == null) return capitals;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name AS capital_name, country.name AS country_name, city.population
        FROM city
        JOIN country ON city.id = country.capital
        WHERE country.continent = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                capitalCityReport c = new capitalCityReport();
                c.name = rset.getString("capital_name");
                c.country = rset.getString("country_name");
                c.population = rset.getInt("population");
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return capitals;
    }

    public static ArrayList<capitalCityReport> getTopCapitalCitiesByRegion(String region, int limit) {
        ArrayList<capitalCityReport> capitals = new ArrayList<>();
        if (populationApp.con == null) return capitals;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name AS capital_name, country.name AS country_name, city.population
        FROM city
        JOIN country ON city.id = country.capital
        WHERE country.region = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                capitalCityReport c = new capitalCityReport();
                c.name = rset.getString("capital_name");
                c.country = rset.getString("country_name");
                c.population = rset.getInt("population");
                capitals.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return capitals;
    }

    /**
     * Outputs a list of capital city reports into a markdown-formatted file.
     * Each capital city appears as a row in a Markdown table.
     *
     * @param capitals List of capitalCityReport objects to write.
     * @param filename Name of the output file to generate.
     */
    public static void outputCapitalCityReport(ArrayList<capitalCityReport> capitals, String filename) {
        // Handle empty or null data
        if (capitals == null || capitals.isEmpty()) {
            try {
                File dir = new File("./reports/capitalCityReports/");
                dir.mkdirs();

                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# Capital City Report\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No capital city data available, wrote placeholder file.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Country | Population |\r\n");
        sb.append("| --- | --- | --- |\r\n");

        // Write one row per capital city
        for (capitalCityReport capital : capitals) {
            if (capital == null) continue;
            sb.append("| ")
                    .append(capital.name).append(" | ")
                    .append(capital.country).append(" | ")
                    .append(capital.population).append(" |\r\n");
        }

        try {
            File dir = new File("./reports/capitalCityReports/");
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

