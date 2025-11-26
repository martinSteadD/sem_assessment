package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The {@code cityReport} class represents a data model and reporting utility
 * for city information, including its name, country, district, and population.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Acts as a simple container for city data retrieved from the database</li>
 *   <li>Provides static methods to query city data at different scopes:
 *       <ul>
 *         <li>All cities in the world</li>
 *         <li>Cities by continent</li>
 *         <li>Cities by region</li>
 *         <li>Cities by country</li>
 *         <li>Cities by district</li>
 *         <li>Top N cities globally, by continent, region, country, or district</li>
 *       </ul>
 *   </li>
 *   <li>Generates Markdown‑formatted reports from query results</li>
 * </ul>
 * <p>
 * Workflow:
 * <ol>
 *   <li>SQL queries are executed via JDBC using {@code populationApp.con}</li>
 *   <li>Results are mapped into {@code cityReport} objects</li>
 *   <li>Collections of these objects are returned for further processing</li>
 *   <li>Output methods format the data into Markdown tables and write them to files</li>
 * </ol>
 * <p>
 * Edge‑case handling:
 * <ul>
 *   <li>Query methods cap {@code limit} values (42 for general queries, 10 for Top N queries)</li>
 *   <li>If no database connection exists, methods return an empty list</li>
 *   <li>If input lists are {@code null} or empty, output methods generate placeholder files</li>
 *   <li>File I/O errors are caught and logged without halting execution</li>
 * </ul>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * // Retrieve top 10 cities in Asia
 * ArrayList<cityReport> cities =
 *     cityReport.getTopCitiesByContinent("Asia", 10);
 *
 * // Output results to Markdown file
 * cityReport.outputCityReport(cities, "TopCitiesAsia.md");
 * }</pre>
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
     * Retrieves all cities in the world ordered by population in descending order,
     * including the associated country and district information.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param limit maximum number of cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
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

    /**
     * Retrieves a list of cities within a specified continent,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given continent</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param continent the name of the continent to filter cities by (e.g., {@code "Asia"})
     * @param limit     maximum number of cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves a list of cities within a specified region,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given region</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param region the name of the region to filter cities by (e.g., {@code "Western Europe"})
     * @param limit  maximum number of cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves a list of cities within a specified country,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given country name</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param country the name of the country to filter cities by (e.g., {@code "China"})
     * @param limit   maximum number of cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves a list of cities within a specified district,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given district</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param district the name of the district to filter cities by (e.g., {@code "California"})
     * @param limit    maximum number of cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N cities in the world,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param limit maximum number of cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N cities within a specified continent,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given continent</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param continent the name of the continent to filter cities by (e.g., {@code "Europe"})
     * @param limit     maximum number of cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N cities within a specified region,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given region</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param region the name of the region to filter cities by (e.g., {@code "Western Europe"})
     * @param limit  maximum number of cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N cities within a specified district,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given district</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param district the name of the district to filter cities by (e.g., {@code "California"})
     * @param limit    maximum number of cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N cities within a specified country,
     * ordered by population in descending order. Each result includes
     * the city name, associated country, district, and population.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given country name</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link cityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param country the name of the country to filter cities by (e.g., {@code "Japan"})
     * @param limit   maximum number of cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link cityReport} objects containing city name,
     *         country name, district, and population; may be empty if no connection or query fails
     */
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
     * Outputs a list of city reports into a Markdown-formatted file.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if the provided list of {@link cityReport} objects is {@code null} or empty</li>
     *   <li>If empty, generates a placeholder Markdown file with a "No results found" message</li>
     *   <li>If data exists, builds a Markdown table with headers: Name, Country, District, Population</li>
     *   <li>Writes one row per city into the table</li>
     *   <li>Saves the file under {@code ./reports/cityReports/} with the given filename</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code cities} is {@code null} or empty, a placeholder file is created</li>
     *   <li>If a {@link cityReport} entry is {@code null}, it is skipped</li>
     *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
     * </ul>
     *
     * @param cities   list of {@link cityReport} objects to write; may be {@code null} or empty
     * @param filename name of the output file to generate (e.g., {@code "TopCitiesEurope.md"})
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
