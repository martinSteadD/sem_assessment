package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class populationApp {
    private Connection con = null;

    public static void main(String[] args) {
        populationApp appPop = new populationApp();

        // Connect to database
        appPop.connect();

        // Get country report
       ArrayList<countryReport> countries = appPop.getAllCountriesByPopulation();

        // Display results
       appPop.printCountryReport(countries);

        // Disconnect from database
        appPop.disconnect();
    }

    /**
     * Connects to the MySQL database using JDBC.
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(1000);
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?useSSL=false&allowPublicKeyRetrieval=true",
                        "root", "example"
                );
                System.out.println("Successfully connected");
                Thread.sleep(1000);
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
     * Disconnects from the database.
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

    /**
     * Retrieves all countries ordered by population.
     */
    public ArrayList<countryReport> getAllCountriesByPopulation() {
        ArrayList<countryReport> countries = new ArrayList<>();

        if (con == null) {
            System.out.println("Connection not established — cannot retrieve data.");
            return countries;
        }

        try {
            Statement stmt = con.createStatement();
            String query = "SELECT code, name, continent, region, population, capital FROM country ORDER BY population DESC";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                countryReport c = new countryReport();
                c.code = rset.getString("code");
                c.name = rset.getString("name");
                c.continent = rset.getString("continent");
                c.region = rset.getString("region");
                c.population = rset.getInt("population");
                c.capital = rset.getString("capital");
                countries.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving country data: " + e.getMessage());
        }

        return countries;
    }

    /**
     * Prints the country report in formatted output.
     */
    public void printCountryReport(ArrayList<countryReport> countries) {
        System.out.printf("%-5s %-47s %-15s %-28s %-11s %-10s%n", "Code", "Name", "Continent", "Region", "Population", "Capital");
        for (countryReport c : countries) {
            System.out.printf("%-5s %-47s %-15s %-28s %-11d %-10s%n",
                    c.code, c.name, c.continent, c.region, c.population, c.capital);
        }
    }

    // Placeholder methods for future reports
    public ArrayList<cityReport> getAllCityByPopulation() { return new ArrayList<>(); }
    public void printCityReport(ArrayList<cityReport> cities) {}
    public ArrayList<capitalCityReport> getAllCapitalCityByPopulation() { return new ArrayList<>(); }
    public void printCapitalCityReport(ArrayList<capitalCityReport> cities) {}
    public ArrayList<populationReport> getAllPopulation() { return new ArrayList<>(); }
    public void printPopulationReport(ArrayList<populationReport> population) {}
    public ArrayList<populationSummary> getAllPopulationSummary() { return new ArrayList<>(); }
    public void printPopulationSummary(ArrayList<populationSummary> summary) {}
    public ArrayList<languageReport> getAllLanguageByPopulation() { return new ArrayList<>(); }
    public void printLanguageReport(ArrayList<languageReport> languages) {}
}
