package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The {@code countryReport} class represents a data model and reporting utility
 * for country information, including its ISO code, name, continent, region,
 * population, and capital city.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Acts as a container for country data retrieved from the database</li>
 *   <li>Provides static methods to query country data at different scopes:
 *       <ul>
 *         <li>All countries by population</li>
 *         <li>Top N countries globally</li>
 *         <li>Top N countries by continent</li>
 *         <li>Top N countries by region</li>
 *         <li>Countries filtered by continent or region (with capped limits)</li>
 *       </ul>
 *   </li>
 *   <li>Generates Markdown‑formatted reports from query results</li>
 * </ul>
 * <p>
 * Workflow:
 * <ol>
 *   <li>SQL queries are executed via JDBC using {@code populationApp.con}</li>
 *   <li>Results are mapped into {@code countryReport} objects</li>
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
 * // Retrieve top 10 countries in Asia
 * ArrayList<countryReport> countries =
 *     countryReport.getTopCountriesByContinent("Asia", 10);
 *
 * // Output results to Markdown file
 * countryReport.outputCountryReport(countries, "TopCountriesAsia.md");
 * }</pre>
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
     * Retrieves a list of countries worldwide, ordered by population in descending order.
     * Each result includes the country code, name, continent, region, population, and capital city.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link countryReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param limit maximum number of countries to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link countryReport} objects containing country code,
     *         name, continent, region, population, and capital city; may be empty if no connection or query fails
     */
    public static ArrayList<countryReport> getAllCountriesByPopulation(int limit) {
        ArrayList<countryReport> countries = new ArrayList<>();
        if (con == null) return countries;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = con.prepareStatement("""
        SELECT country.code, country.name, country.continent, country.region,
               country.population, city.name AS capital_name
        FROM country
        LEFT JOIN city ON country.capital = city.id
        ORDER BY country.population DESC
        LIMIT ?;
    """)) {
            pstmt.setInt(1, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
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
            System.out.println("Error: " + e.getMessage());
        }
        return countries;
    }

    /**
     * Retrieves a list of countries within a specified continent,
     * ordered by population in descending order. Each result includes
     * the country code, name, continent, region, population, and capital city.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables,
     *       filtering by the given continent</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link countryReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param continent the name of the continent to filter countries by (e.g., {@code "Africa"})
     * @param limit     maximum number of countries to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link countryReport} objects containing country code,
     *         name, continent, region, population, and capital city; may be empty if no connection or query fails
     */
    public static ArrayList<countryReport> getCountriesByContinent(String continent, int limit) {
        ArrayList<countryReport> countries = new ArrayList<>();
        if (con == null) return countries;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = con.prepareStatement("""
        SELECT country.code, country.name, country.continent, country.region,
               country.population, city.name AS capital_name
        FROM country
        LEFT JOIN city ON country.capital = city.id
        WHERE country.continent = ?
        ORDER BY country.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
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
            System.out.println("Error: " + e.getMessage());
        }
        return countries;
    }

    /**
     * Retrieves a list of countries within a specified region,
     * ordered by population in descending order. Each result includes
     * the country code, name, continent, region, population, and capital city.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 42 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables,
     *       filtering by the given region</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link countryReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param region the name of the region to filter countries by (e.g., {@code "Western Europe"})
     * @param limit  maximum number of countries to return; capped internally at {@code 42}
     * @return an {@code ArrayList} of {@link countryReport} objects containing country code,
     *         name, continent, region, population, and capital city; may be empty if no connection or query fails
     */
    public static ArrayList<countryReport> getCountriesByRegion(String region, int limit) {
        ArrayList<countryReport> countries = new ArrayList<>();
        if (con == null) return countries;

        int cappedLimit = Math.min(limit, 42);
        try (PreparedStatement pstmt = con.prepareStatement("""
        SELECT country.code, country.name, country.continent, country.region,
               country.population, city.name AS capital_name
        FROM country
        LEFT JOIN city ON country.capital = city.id
        WHERE country.region = ?
        ORDER BY country.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
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
            System.out.println("Error: " + e.getMessage());
        }
        return countries;
    }

    /**
     * Retrieves the top N countries in the world,
     * ordered by population in descending order. Each result includes
     * the country code, name, continent, region, population, and capital city.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link countryReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param limit maximum number of countries to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link countryReport} objects containing country code,
     *         name, continent, region, population, and capital city; may be empty if no connection or query fails
     */
    public static ArrayList<countryReport> getTopCountriesByPopulation(int limit) {
        ArrayList<countryReport> countries = new ArrayList<>();
        if (con == null) return countries;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = con.prepareStatement("""
        SELECT country.code, country.name, country.continent, country.region,
               country.population, city.name AS capital_name
        FROM country
        LEFT JOIN city ON country.capital = city.id
        ORDER BY country.population DESC
        LIMIT ?;
    """)) {
            pstmt.setInt(1, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
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
            System.out.println("Error: " + e.getMessage());
        }
        return countries;
    }

    /**
     * Retrieves the top N countries within a specified continent,
     * ordered by population in descending order. Each result includes
     * the country code, name, continent, region, population, and capital city.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables,
     *       filtering by the given continent</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link countryReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param continent the name of the continent to filter countries by (e.g., {@code "Asia"})
     * @param limit     maximum number of countries to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link countryReport} objects containing country code,
     *         name, continent, region, population, and capital city; may be empty if no connection or query fails
     */
    public static ArrayList<countryReport> getTopCountriesByContinent(String continent, int limit) {
        ArrayList<countryReport> countries = new ArrayList<>();
        if (con == null) return countries;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = con.prepareStatement("""
        SELECT country.code, country.name, country.continent, country.region,
               country.population, city.name AS capital_name
        FROM country
        LEFT JOIN city ON country.capital = city.id
        WHERE country.continent = ?
        ORDER BY country.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, continent);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
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
            System.out.println("Error: " + e.getMessage());
        }
        return countries;
    }

    /**
     * Retrieves the top N countries within a specified region,
     * ordered by population in descending order. Each result includes
     * the country code, name, continent, region, population, and capital city.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Caps the requested {@code limit} at a maximum of 10 for reproducibility</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables,
     *       filtering by the given region</li>
     *   <li>Orders results by population (largest first)</li>
     *   <li>Maps each result row into a {@link countryReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @param region the name of the region to filter countries by (e.g., {@code "Western Europe"})
     * @param limit  maximum number of countries to return; capped internally at {@code 10}
     * @return an {@code ArrayList} of {@link countryReport} objects containing country code,
     *         name, continent, region, population, and capital city; may be empty if no connection or query fails
     */
    public static ArrayList<countryReport> getTopCountriesByRegion(String region, int limit) {
        ArrayList<countryReport> countries = new ArrayList<>();
        if (con == null) return countries;

        int cappedLimit = Math.min(limit, 10);
        try (PreparedStatement pstmt = con.prepareStatement("""
        SELECT country.code, country.name, country.continent, country.region,
               country.population, city.name AS capital_name
        FROM country
        LEFT JOIN city ON country.capital = city.id
        WHERE country.region = ?
        ORDER BY country.population DESC
        LIMIT ?;
    """)) {
            pstmt.setString(1, region);
            pstmt.setInt(2, cappedLimit);
            ResultSet rset = pstmt.executeQuery();
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
            System.out.println("Error: " + e.getMessage());
        }
        return countries;
    }


    /**
     * Outputs a list of country reports into a Markdown-formatted file.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if the provided list of {@link countryReport} objects is {@code null} or empty</li>
     *   <li>If empty, generates a placeholder Markdown file with a "No results found" message</li>
     *   <li>If data exists, builds a Markdown table with headers:
     *       <ul>
     *         <li>Code</li>
     *         <li>Name</li>
     *         <li>Continent</li>
     *         <li>Region</li>
     *         <li>Population</li>
     *         <li>Capital</li>
     *       </ul>
     *   </li>
     *   <li>Writes one row per country into the table</li>
     *   <li>Saves the file under {@code ./reports/countryReports/} with the given filename</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code countries} is {@code null} or empty, a placeholder file is created</li>
     *   <li>If a {@link countryReport} entry is {@code null}, it is skipped</li>
     *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
     * </ul>
     *
     * @param countries list of {@link countryReport} objects to write; may be {@code null} or empty
     * @param filename  name of the output file to generate (e.g., {@code "CountryReport.md"})
     */
    public static void outputCountryReport(ArrayList<countryReport> countries, String filename) {
        // Handle empty or null data
        if (countries == null || countries.isEmpty()) {
            try {
                File dir = new File("./reports/countryReports/");
                dir.mkdirs();

                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# Country Report\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No country data available, wrote placeholder file.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Code | Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");

        // Build Markdown table rows from the country data
        for (countryReport country : countries) {
            if (country == null) continue;
            sb.append("| ")
                    .append(country.code).append(" | ")
                    .append(country.name).append(" | ")
                    .append(country.continent).append(" | ")
                    .append(country.region).append(" | ")
                    .append(country.population).append(" | ")
                    .append(country.capital).append(" |\r\n");
        }

        try {
            File dir = new File("./reports/countryReports/");
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




