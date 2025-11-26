package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The {@code populationSummary} class represents a simplified data model and reporting utility
 * for aggregated population information across different geographic levels.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Acts as a container for population data retrieved from the database</li>
 *   <li>Provides static methods to query population totals across multiple levels:
 *       <ul>
 *         <li>World</li>
 *         <li>Continent</li>
 *         <li>Region</li>
 *         <li>Country</li>
 *         <li>District</li>
 *         <li>City</li>
 *       </ul>
 *   </li>
 *   <li>Generates Markdown‑formatted reports from query results</li>
 * </ul>
 * <p>
 * Workflow:
 * <ol>
 *   <li>SQL queries are executed via JDBC using {@code populationApp.con}</li>
 *   <li>Population totals are aggregated using {@code UNION ALL} queries</li>
 *   <li>Results are mapped into {@code populationSummary} objects</li>
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
 * // Retrieve population summaries across all levels
 * ArrayList<populationSummary> summaries =
 *     populationSummary.getAllPopulationSummary();
 *
 * // Output results to Markdown file
 * populationSummary.outputPopSummary(summaries, "PopulationSummary.md");
 * }</pre>
 */
public class populationSummary extends populationApp{

    /**
     * The name of the geographic area (e.g., country, region, or continent).
     */
    public String name;

    /**
     * The total population of the specified area.
     */
    public long population;

    /**
     * The level of area (Continent, Region, Country)
     */
    public String level;


    /**
     * Retrieves a high-level population summary across multiple geographic levels,
     * including world, continent, region, country, district, and city totals.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if a valid database connection exists; returns an empty list if not</li>
     *   <li>Executes a compound SQL query using {@code UNION ALL} to aggregate population data</li>
     *   <li>Calculates totals for:
     *       <ul>
     *         <li>World</li>
     *         <li>Each continent</li>
     *         <li>Each region</li>
     *         <li>Each country</li>
     *         <li>Each district</li>
     *         <li>Each city</li>
     *       </ul>
     *   </li>
     *   <li>Orders results by level and name</li>
     *   <li>Maps each result row into a {@link populationSummary} object</li>
     *   <li>Collects and returns the results as an {@code ArrayList}</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is {@code null}, an empty list is returned and a warning is logged</li>
     *   <li>If SQL execution fails, the error message is logged and partial/empty results may be returned</li>
     * </ul>
     *
     * @return an {@code ArrayList} of {@link populationSummary} objects containing
     *         the name of the area, its population, and its level (World, Continent, Region, Country, District, City);
     *         may be empty if no connection or query fails
     */
    public static ArrayList<populationSummary> getAllPopulationSummary() {
        ArrayList<populationSummary> popsums = new ArrayList<>();

        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return popsums;
        }

        try {
            Statement stmt = con.createStatement();

            String query = """
            (
                SELECT
                    'World' AS name,
                    SUM(Population) AS population,
                    'World' AS level
                FROM country
            )
            UNION ALL
            (
                SELECT
                    Continent AS name,
                    SUM(Population) AS population,
                    'Continent' AS level
                FROM country
                GROUP BY Continent
            )
            UNION ALL
            (
                SELECT
                    Region AS name,
                    SUM(Population) AS population,
                    'Region' AS level
                FROM country
                GROUP BY Region
            )
            UNION ALL
            (
                SELECT
                    Name AS name,
                    Population AS population,
                    'Country' AS level
                FROM country
            )
            UNION ALL
            (
                SELECT
                    District AS name,
                    SUM(Population) AS population,
                    'District' AS level
                FROM city
                GROUP BY District
            )
            UNION ALL
            (
                SELECT
                    Name AS name,
                    Population AS population,
                    'City' AS level
                FROM city
            )
            ORDER BY level, name;
        """;

            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                populationSummary ps = new populationSummary();
                ps.name = rset.getString("name");
                ps.population = rset.getLong("population");
                ps.level = rset.getString("level");
                popsums.add(ps);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving population summary data: " + e.getMessage());
        }

        return popsums;
    }

    /**
     * Outputs a list of population summaries into a Markdown-formatted file.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Checks if the provided list of {@link populationSummary} objects is {@code null} or empty</li>
     *   <li>If empty, generates a placeholder Markdown file with a "No results found" message</li>
     *   <li>If data exists, builds a Markdown table with headers: Name, Population, Level</li>
     *   <li>Writes one row per population summary into the table</li>
     *   <li>Saves the file under {@code ./reports/populationReports/} with the given filename</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code popsums} is {@code null} or empty, a placeholder file is created</li>
     *   <li>If a {@link populationSummary} entry is {@code null}, it is skipped</li>
     *   <li>If file I/O fails, the stack trace is printed and execution continues</li>
     * </ul>
     *
     * @param popsums  list of {@link populationSummary} objects to write; may be {@code null} or empty
     * @param filename name of the output file to generate (e.g., {@code "PopulationSummary.md"})
     */
    public static void outputPopSummary(ArrayList<populationSummary> popsums, String filename) {
        if (popsums == null || popsums.isEmpty()) {
            try {
                File dir = new File("./reports/populationReports/");
                dir.mkdirs();
                File outFile = new File(dir, filename);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                    writer.write("# Population Summary\n\n");
                    writer.write("No results found for this query.\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No population summary data available, wrote placeholder file.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("| Name | Population | Level |\r\n");
        sb.append("| --- | --- | --- |\r\n");

        for (populationSummary popsum : popsums) {
            if (popsum == null) continue;
            sb.append("| ")
                    .append(popsum.name).append(" | ")
                    .append(popsum.population).append(" | ")
                    .append(popsum.level).append(" |\r\n");
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








