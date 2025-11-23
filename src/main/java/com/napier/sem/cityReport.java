package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    public static ArrayList<cityReport> getAllCitiesByPopulation(int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        ORDER BY city.population DESC
        LIMIT 42;
    """)) {
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getCitiesByContinent(String continent, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE country.continent = ?
        ORDER BY city.population DESC
        LIMIT 42;
    """)) {
            pstmt.setString(1, continent);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getCitiesByRegion(String region, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE country.region = ?
        ORDER BY city.population DESC
        LIMIT 42;
    """)) {
            pstmt.setString(1, region);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getCitiesByCountry(String country, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE country.name = ?
        ORDER BY city.population DESC
        LIMIT 42;
    """)) {
            pstmt.setString(1, country);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getCitiesByDistrict(String district, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE city.district = ?
        ORDER BY city.population DESC
        LIMIT 42;
    """)) {
            pstmt.setString(1, district);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getTopCitiesInWorld(int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setInt(1, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getTopCitiesByContinent(String continent, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE country.continent = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getTopCitiesByRegion(String region, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE country.region = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }

    public static ArrayList<cityReport> getTopCitiesByDistrict(String district, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE city.district = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, district);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return cities;
    }
    public static ArrayList<cityReport> getTopCitiesByCountry(String country, int limit) {
        ArrayList<cityReport> cities = new ArrayList<>();
        if (populationApp.con == null) return cities;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = populationApp.con.prepareStatement("""
        SELECT city.name, country.name AS country_name,
               city.district, city.population
        FROM city
        JOIN country ON city.countrycode = country.code
        WHERE country.name = ?
        ORDER BY city.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, country);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                cityReport c = new cityReport();
                c.name = rset.getString("name");
                c.country = rset.getString("country_name");
                c.district = rset.getString("district");
                c.population = rset.getInt("population");
                cities.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
        // Handle empty or null data
        if (cities == null || cities.isEmpty()) {
            try {
                File dir = new File("./reports/cityReports/");
                dir.mkdirs();

                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# City Report\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No city data available, wrote placeholder file.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");

        // Write one row per city
        for (cityReport city : cities) {
            if (city == null) continue;
            sb.append("| ")
                    .append(city.name).append(" | ")
                    .append(city.country).append(" | ")
                    .append(city.district).append(" | ")
                    .append(city.population).append(" |\r\n");
        }

        try {
            File dir = new File("./reports/cityReports/");
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
