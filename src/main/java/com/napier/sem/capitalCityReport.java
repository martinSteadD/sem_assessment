package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The {@code capitalCityReport} class represents a data model and reporting utility
 * for capital city information, including its name, country, and population.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Acts as a simple container for capital city data retrieved from the database</li>
 *   <li>Provides static methods to query capital city data at different scopes:
 *       <ul>
 *         <li>All capital cities in the world</li>
 *         <li>Capital cities by continent</li>
 *         <li>Capital cities by region</li>
 *         <li>Top N capital cities globally, by continent, or by region</li>
 *       </ul>
 *   </li>
 *   <li>Generates Markdown‑formatted reports from query results</li>
 * </ul>
 * <p>
 * Workflow:
 * <ol>
 *   <li>SQL queries are executed via JDBC using {@code populationApp.con}</li>
 *   <li>Results are mapped into {@code capitalCityReport} objects</li>
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
 * // Retrieve top 10 capital cities in Europe
 * ArrayList<capitalCityReport> capitals =
 *     capitalCityReport.getTopCapitalCitiesByContinent("Europe", 10);
 *
 * // Output results to Markdown file
 * capitalCityReport.outputCapitalCityReport(capitals, "TopCapitalsEurope.md");
 * }</pre>
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


    /**
     * Retrieves a list of all capital cities ordered by population in descending order.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables to identify capitals</li>
     *   <li>Maps each result row into a {@link capitalCityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param limit maximum number of capital cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link capitalCityReport} objects containing capital name,
     *         country name, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves a list of capital cities within a specified continent,
     * ordered by population in descending order.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given continent</li>
     *   <li>Maps each result row into a {@link capitalCityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param continent the name of the continent to filter capital cities by (e.g., {@code "Asia"})
     * @param limit     maximum number of capital cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link capitalCityReport} objects containing capital name,
     *         country name, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves a list of capital cities within a specified region,
     * ordered by population in descending order.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given region</li>
     *   <li>Maps each result row into a {@link capitalCityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param region the name of the region to filter capital cities by (e.g., {@code "Western Europe"})
     * @param limit  maximum number of capital cities to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link capitalCityReport} objects containing capital name,
     *         country name, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N capital cities in the world,
     * ordered by population in descending order.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables
     *       to identify capital cities</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link capitalCityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param limit maximum number of capital cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link capitalCityReport} objects containing capital name,
     *         country name, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N capital cities within a specified continent,
     * ordered by population in descending order.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given continent</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link capitalCityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param continent the name of the continent to filter capital cities by (e.g., {@code "Europe"})
     * @param limit     maximum number of capital cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link capitalCityReport} objects containing capital name,
     *         country name, and population; may be empty if no connection or query fails
     */
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

    /**
     * Retrieves the top N capital cities within a specified region,
     * ordered by population in descending order.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code city} and {@code country} tables,
     *       filtering by the given region</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link capitalCityReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code populationApp.con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param region the name of the region to filter capital cities by (e.g., {@code "Western Europe"})
     * @param limit  maximum number of capital cities to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link capitalCityReport} objects containing capital name,
     *         country name, and population; may be empty if no connection or query fails
     */
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
     * Outputs a list of capital city reports into a Markdown-formatted file.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if the provided list of {@link capitalCityReport} objects is {@code null} or empty</li>
     *   <li>If empty, generates a placeholder Markdown file with a "No results found" message</li>
     *   <li>If data exists, builds a Markdown table with headers: Name, Country, Population</li>
     *   <li>Writes one row per capital city into the table</li>
     *   <li>Saves the file under {@code ./reports/capitalCityReports/} with the given filename</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code capitals} is {@code null} or empty, a placeholder file is created</li>
     *   <li>If a {@link capitalCityReport} entry is {@code null}, it is skipped</li>
     *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
     * </ul>
     *
     * @param capitals list of {@link capitalCityReport} objects to write; may be {@code null} or empty
     * @param filename name of the output file to generate (e.g., {@code "CapitalCitiesWorld.md"})
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

