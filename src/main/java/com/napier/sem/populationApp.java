package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

/**
 * The {@code populationApp} class serves as the main entry point for generating
 * population-related reports from the world database.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Establishes a JDBC connection to the MySQL database</li>
 *   <li>Retrieves country population data via DAO methods</li>
 *   <li>Outputs formatted Markdown reports to the /reports directory</li>
 * </ul>
 * <p>
 * This class demonstrates reproducible workflow: connect → query → output → disconnect.
 */
public class populationApp {
    /**
     * JDBC connection to the database.
     * <p>
     * Shared across methods to allow consistent connection handling.
     */
    public static Connection con = null;

    /**
     * Main method that drives the application workflow:
     * <ol>
     *   <li>Connects to the database using environment variables</li>
     *   <li>Retrieves country data at different scopes (world, continent, region)</li>
     *   <li>Generates Markdown reports with population statistics</li>
     *   <li>Disconnects from the database</li>
     * </ol>
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        populationApp app = new populationApp();

        // Connects to database
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");
        // Defaults to the Docker service values if environment variables are missing
        if (dbHost == null || dbHost.isEmpty()) dbHost = "db";
        if (dbPort == null || dbPort.isEmpty()) dbPort = "3306";

        String dbLocation = dbHost + ":" + dbPort;
        app.connect(dbLocation, 3000);

        // Report Generation Section

        // Country Reports

        // All countries in the world (limit 42)
        ArrayList<countryReport> countriesWorld = countryReport.getAllCountriesByPopulation(42);
        countryReport.outputCountryReport(countriesWorld, "CountryPopulation.md");

        // All countries in a continent (limit 42)
        ArrayList<countryReport> countriesContinent = countryReport.getCountriesByContinent("Asia", 42);
        countryReport.outputCountryReport(countriesContinent, "CountryPopulation_Asia.md");

        // All countries in a region (limit 42)
        ArrayList<countryReport> countriesRegion = countryReport.getCountriesByRegion("Eastern Europe", 42);
        countryReport.outputCountryReport(countriesRegion, "CountryPopulation_EasternEurope.md");

        // Top N populated countries in the world (limit 10)
        ArrayList<countryReport> topCountriesWorld = countryReport.getTopCountriesByPopulation(10);
        countryReport.outputCountryReport(topCountriesWorld, "TopCountriesWorld.md");

        // Top N populated countries in a continent (limit 10)
        ArrayList<countryReport> topCountriesContinent = countryReport.getTopCountriesByContinent("Africa", 10);
        countryReport.outputCountryReport(topCountriesContinent, "TopCountries_Africa.md");

        // Top N populated countries in a region (limit 10)
        ArrayList<countryReport> topCountriesRegion = countryReport.getTopCountriesByRegion("South America", 10);
        countryReport.outputCountryReport(topCountriesRegion, "TopCountries_SouthAmerica.md");


        // City Reports

        // All cities in the world (limit 42)
        ArrayList<cityReport> cities = cityReport.getAllCitiesByPopulation(42);
        cityReport.outputCityReport(cities, "CityPopulation.md");

        // All cities in a continent (Asia, limit 42)
        ArrayList<cityReport> citiesContinent = cityReport.getCitiesByContinent("Asia", 42);
        cityReport.outputCityReport(citiesContinent, "CitiesByContinent.md");

        // All cities in a region (Eastern Asia, limit 42)
        ArrayList<cityReport> citiesRegion = cityReport.getCitiesByRegion("Eastern Asia", 42);
        cityReport.outputCityReport(citiesRegion, "CitiesByRegion.md");

        // All cities in a specific country (China, limit 42)
        ArrayList<cityReport> citiesCountry = cityReport.getCitiesByCountry("China", 42);
        cityReport.outputCityReport(citiesCountry, "CitiesByCountry.md");

        // All cities in a district (California, limit 42)
        ArrayList<cityReport> citiesDistrict = cityReport.getCitiesByDistrict("California", 42);
        cityReport.outputCityReport(citiesDistrict, "CitiesByDistrict.md");


        // Top N City Reports

        // Top N populated cities in the world (limit 10)
        ArrayList<cityReport> topCitiesWorld = cityReport.getTopCitiesInWorld(10);
        cityReport.outputCityReport(topCitiesWorld, "TopCitiesWorld.md");

        // Top N populated cities in a continent (Europe, limit 10)
        ArrayList<cityReport> topCitiesContinent = cityReport.getTopCitiesByContinent("Europe", 10);
        cityReport.outputCityReport(topCitiesContinent, "TopCitiesContinent.md");

        // Top N populated cities in a region (Western Europe, limit 10)
        ArrayList<cityReport> topCitiesRegion = cityReport.getTopCitiesByRegion("Western Europe", 10);
        cityReport.outputCityReport(topCitiesRegion, "TopCitiesRegion.md");

        // Top N populated cities in a specific country (Germany, limit 10)
        ArrayList<cityReport> topCitiesCountry = cityReport.getTopCitiesByCountry("Germany", 10);
        cityReport.outputCityReport(topCitiesCountry, "TopCitiesCountry.md");

        // Top N populated cities in a district (Bavaria, limit 10)
        ArrayList<cityReport> topCitiesDistrict = cityReport.getTopCitiesByDistrict("Bavaria", 10);
        cityReport.outputCityReport(topCitiesDistrict, "TopCitiesDistrict.md");


        // Capital City Reports

        // All capital cities in the world (limit 42)
        ArrayList<capitalCityReport> capitalsWorld = capitalCityReport.getAllCapitalCitiesByPopulation(42);
        capitalCityReport.outputCapitalCityReport(capitalsWorld, "CapitalCitiesWorld.md");

        // All capital cities in a continent (Asia, limit 42)
        ArrayList<capitalCityReport> capitalsContinent = capitalCityReport.getCapitalCitiesByContinent("Asia", 42);
        capitalCityReport.outputCapitalCityReport(capitalsContinent, "CapitalCitiesByContinent.md");

        // All capital cities in a region (Eastern Asia, limit 42)
        ArrayList<capitalCityReport> capitalsRegion = capitalCityReport.getCapitalCitiesByRegion("Eastern Asia", 42);
        capitalCityReport.outputCapitalCityReport(capitalsRegion, "CapitalCitiesByRegion.md");

        // Top N populated capital cities in the world (limit 10)
        ArrayList<capitalCityReport> topCapitalsWorld = capitalCityReport.getTopCapitalCitiesInWorld(10);
        capitalCityReport.outputCapitalCityReport(topCapitalsWorld, "TopCapitalCitiesWorld.md");

        // Top N populated capital cities in a continent (Europe, limit 10)
        ArrayList<capitalCityReport> topCapitalsContinent = capitalCityReport.getTopCapitalCitiesByContinent("Europe", 10);
        capitalCityReport.outputCapitalCityReport(topCapitalsContinent, "TopCapitalCitiesContinent.md");

        // Top N populated capital cities in a region (Western Europe, limit 10)
        ArrayList<capitalCityReport> topCapitalsRegion = capitalCityReport.getTopCapitalCitiesByRegion("Western Europe", 10);
        capitalCityReport.outputCapitalCityReport(topCapitalsRegion, "TopCapitalCitiesRegion.md");


        // Population Reports

        // Population by continent
        ArrayList<populationReport> popsContinent = populationReport.getPopulationByContinent();
        populationReport.outputPopReport(popsContinent, "PopulationByContinent.md");

        // Population by region
        ArrayList<populationReport> popsRegion = populationReport.getPopulationByRegion();
        populationReport.outputPopReport(popsRegion, "PopulationByRegion.md");

        // Population by country
        ArrayList<populationReport> popsCountry = populationReport.getPopulationByCountry();
        populationReport.outputPopReport(popsCountry, "PopulationByCountry.md");

        // Population Summary Report

        // Overall population summary
        ArrayList<populationSummary> popsums = populationSummary.getAllPopulationSummary();
        populationSummary.outputPopSummary(popsums, "PopulationSummary.md");

        // Language Report

        // Language distribution report
        ArrayList<languageReport> langs = languageReport.getAllLanguageReport();
        languageReport.outputLanguageReport(langs, "LanguageReport.md");


        // Disconnects from the database
        app.disconnect();
    }

    /**
     * Establishes a JDBC connection to the MySQL database.
     * <p>
     * Workflow:
     * <ul>
     *   <li>Loads the MySQL JDBC driver</li>
     *   <li>Attempts to connect to the database at the specified location</li>
     *   <li>Retries connection multiple times if initial attempts fail</li>
     * </ul>
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If the driver cannot be loaded, the application exits with error code -1</li>
     *   <liIf connection fails, retries are attempted up to the configured limit</li>
     *   <li>If the thread is interrupted during sleep, a warning is logged</li>
     * </ul>
     *
     * @param location host and port of the database (e.g., "db:3306")
     * @param delay    delay in milliseconds between connection attempts
     */
    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 50; // Maximum number of connection attempts
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database at " + location + "...");
            try {
                Thread.sleep(delay); // Waits before attempting connection
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true",
                        "root", "example"
                );
                System.out.println("Successfully connected");
                Thread.sleep(5000); // Stabilises the connection before proceeding
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Closes the JDBC connection to the database if one exists.
     * <p>
     * Edge-case handling:
     * <ul>
     *   <li>If {@code con} is null, no action is taken</li>
     *   <li>If closing the connection fails, an error message is logged</li>
     * </ul>
     * <p>
     * This method ensures resources are released cleanly after database operations.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

}
