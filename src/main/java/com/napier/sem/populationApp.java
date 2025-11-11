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
        ArrayList<countryReport> countries = countryReport.getAllCountriesByPopulation();
        app.outputCountryReport(countries, "CountryPopulation.md");

        // Run city report directly
        ArrayList<cityReport> cities = cityReport.getAllCitiesByPopulation();
        app.outputCityReport(cities, "CityPopulation.md");

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


    public void outputCountryReport(ArrayList<countryReport> countries, String filename) {
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


    public void outputCityReport(ArrayList<cityReport> cities, String filename) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Markdown header
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");

        for (cityReport city : cities) {
            if (city == null) continue;
            sb.append("| " +
                      city.name + " | " +
                      city.country + " | " +
                      city.district + " | " +
                      city.population + " |\r\n");
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

    // Placeholder methods for future reports
    public ArrayList<capitalCityReport> getAllCapitalCityByPopulation() { return new ArrayList<>(); }

    public void printCapitalCityReport(ArrayList<capitalCityReport> cities) {}

    public ArrayList<populationReport> getAllPopulation() { return new ArrayList<>(); }

    public void printPopulationReport(ArrayList<populationReport> population) {}

    public ArrayList<populationSummary> getAllPopulationSummary() { return new ArrayList<>(); }

    public void printPopulationSummary(ArrayList<populationSummary> summary) {}

    public ArrayList<languageReport> getAllLanguageByPopulation() { return new ArrayList<>(); }

    public void printLanguageReport(ArrayList<languageReport> languages) {}
}
