package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The {@code populationReport} class represents a data model and reporting utility
 * for population statistics of a geographic area, including total population,
 * city population, and non-city population breakdowns.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Acts as a container for population data retrieved from the database</li>
 *   <li>Provides static methods to query population breakdowns across multiple levels:
 *       <ul>
 *         <li>Continent</li>
 *         <li>Region</li>
 *         <li>Country</li>
 *         <li>District</li>
 *       </ul>
 *   </li>
 *   <li>Generates Markdown‑formatted reports from query results</li>
 * </ul>
 * <p>
 * Workflow:
 * <ol>
 *   <li>SQL queries are executed via JDBC using {@code populationApp.con}</li>
 *   <li>Population totals are aggregated from {@code country} and {@code city} tables</li>
 *   <li>City and non‑city populations are calculated, along with their percentages</li>
 *   <li>Results are mapped into {@code populationReport} objects</li>
 *   <li>Collections of these objects are returned for further processing</li>
 *   <li>Output methods format the data into Markdown tables and write them to files</li>
 * </ol>
 * <p>
 * Edge‑case handling:
 * <ul>
 *   <li>If no database connection exists, query methods return an empty list</li>
 *   <li>If input lists are {@code null} or empty, output methods generate placeholder files</li>
 *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
 * </ul>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * // Retrieve population breakdowns by continent
 * ArrayList<populationReport> reports =
 *     populationReport.getPopulationByContinent();
 *
 * // Output results to Markdown file
 * populationReport.outputPopReport(reports, "PopulationByContinent.md");
 * }</pre>
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
     * Retrieves population statistics grouped by continent, including totals and breakdowns
     * of city versus non-city populations.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables</li>
     *   <li>Aggregates population data for each continent:
     *       <ul>
     *         <li>Total population</li>
     *         <li>Population living in cities</li>
     *         <li>Percentage of population living in cities</li>
     *         <li>Population living outside cities</li>
     *         <li>Percentage of population living outside cities</li>
     *       </ul>
     *   </li>
     *   <li>Orders results by total population (largest first)</li>
     *   <li>Maps each result row into a {@link populationReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned and a warning is logged</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @return an {@code ArrayList} of {@link populationReport} objects containing continent name,
     *         total population, city population and percentage, non-city population and percentage;
     *         may be empty if no connection or query fails
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

    /**
     * Retrieves population statistics grouped by region, including totals and breakdowns
     * of city versus non-city populations.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables</li>
     *   <li>Aggregates population data for each region:
     *       <ul>
     *         <li>Total population</li>
     *         <li>Population living in cities</li>
     *         <li>Percentage of population living in cities</li>
     *         <li>Population living outside cities</li>
     *         <li>Percentage of population living outside cities</li>
     *       </ul>
     *   </li>
     *   <li>Orders results by total population (largest first)</li>
     *   <li>Maps each result row into a {@link populationReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned and a warning is logged</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @return an {@code ArrayList} of {@link populationReport} objects containing region name,
     *         total population, city population and percentage, non-city population and percentage;
     *         may be empty if no connection or query fails
     */
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

    /**
     * Retrieves population statistics grouped by country, including totals and breakdowns
     * of city versus non-city populations.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Executes a SQL query joining {@code country} and {@code city} tables</li>
     *   <li>Aggregates population data for each country:
     *       <ul>
     *         <li>Total population</li>
     *         <li>Population living in cities</li>
     *         <li>Percentage of population living in cities</li>
     *         <li>Population living outside cities</li>
     *         <li>Percentage of population living outside cities</li>
     *       </ul>
     *   </li>
     *   <li>Groups results by country code, name, and population</li>
     *   <li>Orders results by total population (largest first)</li>
     *   <li>Maps each result row into a {@link populationReport} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned and a warning is logged</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @return an {@code ArrayList} of {@link populationReport} objects containing country name,
     *         total population, city population and percentage, non-city population and percentage;
     *         may be empty if no connection or query fails
     */
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
     * Outputs a list of population reports into a Markdown-formatted file.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if the provided list of {@link populationReport} objects is {@code null} or empty</li>
     *   <li>If empty, generates a placeholder Markdown file with a "No results found" message</li>
     *   <li>If data exists, builds a Markdown table with headers:
     *       <ul>
     *         <li>Name</li>
     *         <li>Total Population</li>
     *         <li>City Population</li>
     *         <li>City Percentage</li>
     *         <li>Non-City Population</li>
     *         <li>Non-City Percentage</li>
     *       </ul>
     *   </li>
     *   <li>Writes one row per population report into the table</li>
     *   <li>Saves the file under {@code ./reports/populationReports/} with the given filename</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code pops} is {@code null} or empty, a placeholder file is created</li>
     *   <li>If a {@link populationReport} entry is {@code null}, it is skipped</li>
     *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
     * </ul>
     *
     * @param pops     list of {@link populationReport} objects to write; may be {@code null} or empty
     * @param filename name of the output file to generate (e.g., {@code "PopulationReport.md"})
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

