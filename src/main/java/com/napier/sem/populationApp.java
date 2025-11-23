package com.napier.sem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * The populationApp class serves as the main entry point for generating
 * population-related reports from the world database. It connects to a MySQL database,
 * retrieves data, and prints formatted reports to the console.
 */
public class populationApp {
    /**
     * JDBC connection to the database.
     */
    public static Connection con = null;

    /**
     * Main method that drives the application workflow:
     * connects to the database, retrieves country data, prints the report,
     * and disconnects from the database.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        populationApp app = new populationApp();

        // Connect to database
        String dbHost = System.getenv("DB_HOST");
        String dbPort = System.getenv("DB_PORT");

        if (dbHost == null || dbHost.isEmpty()) dbHost = "db";
        if (dbPort == null || dbPort.isEmpty()) dbPort = "3306";

        String dbLocation = dbHost + ":" + dbPort;
        app.connect(dbLocation, 3000);

        // Run country report directly
// 1. All countries in the world (limit 42)
        ArrayList<countryReport> countriesWorld = countryReport.getAllCountriesByPopulation(42);
        countryReport.outputCountryReport(countriesWorld, "CountryPopulation.md");

// 2. All countries in a continent (limit 42)
        ArrayList<countryReport> countriesContinent = countryReport.getCountriesByContinent("Asia", 42);
        countryReport.outputCountryReport(countriesContinent, "CountryPopulation_Asia.md");

// 3. All countries in a region (limit 42)
        ArrayList<countryReport> countriesRegion = countryReport.getCountriesByRegion("Eastern Europe", 42);
        countryReport.outputCountryReport(countriesRegion, "CountryPopulation_EasternEurope.md");

// 4. Top N populated countries in the world (limit 10)
        ArrayList<countryReport> topCountriesWorld = countryReport.getTopCountriesByPopulation(10);
        countryReport.outputCountryReport(topCountriesWorld, "TopCountriesWorld.md");

// 5. Top N populated countries in a continent (limit 10)
        ArrayList<countryReport> topCountriesContinent = countryReport.getTopCountriesByContinent("Africa", 10);
        countryReport.outputCountryReport(topCountriesContinent, "TopCountries_Africa.md");

// 6. Top N populated countries in a region (limit 10)
        ArrayList<countryReport> topCountriesRegion = countryReport.getTopCountriesByRegion("South America", 10);
        countryReport.outputCountryReport(topCountriesRegion, "TopCountries_SouthAmerica.md");




        // Run city report directly
        ArrayList<cityReport> cities = cityReport.getAllCitiesByPopulation();
        cityReport.outputCityReport(cities, "CityPopulation.md");


        // Run population report directly
        ArrayList<populationReport> pops = populationReport.getAllPopulation();
        populationReport.outputPopReport(pops,"PopulationReport.md");

        // Run population summary report directly
        ArrayList<populationSummary> popsums = populationSummary.getAllPopulationSummary();
        populationSummary.outputPopSummary(popsums,"PopulationSummary.md");


        // Disconnect
        app.disconnect();
    }



    /**
     * Connects to the MySQL database using JDBC.
     * Retries up to 20 times if connection fails.
     */
    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 50;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database at " + location + "...");
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true",
                        "root", "example"
                );
                System.out.println("Successfully connected");
                Thread.sleep(5000);
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
     * Disconnects from the database if a connection exists.
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

    // Placeholder methods for future reports
    public ArrayList<capitalCityReport> getAllCapitalCityByPopulation() { return new ArrayList<>(); }

    public void printCapitalCityReport(ArrayList<capitalCityReport> cities) {}



    public ArrayList<languageReport> getAllLanguageByPopulation() { return new ArrayList<>(); }

    public void printLanguageReport(ArrayList<languageReport> languages) {}
}
