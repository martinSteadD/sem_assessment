package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class populationApp
{
    private Connection con = null;

    public static void main(String[] args)
    {
        // Create the new app
        populationApp appPop = new populationApp();

        // Connect to database
        appPop.connect();
        //Get country report
        ArrayList<countryReport> countries = appPop.getAllCountriesByPopulation();

        //display results
        appPop.printCountryReport(countries);

        //Disconnect from database
        appPop.disconnect();
    }

    /**
     * This method will connect to the app to the world.sql database,
     * allowing the user to retrieve the information they require.
     */
    public void connect(){
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Connection to the database
        int retries = 100;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(1000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://localhost:33060/world?useSSL=false&allowPublicKeyRetrieval=true", "root", "example");
                System.out.println("Successfully connected");
                // Wait a bit
                Thread.sleep(1000);
                // Exit for loop
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * This method disconnects the app from the world database.
     */
    public void disconnect(){
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * this method when connected to the database, and retrieve the required information and store it in the arraylist
     * @return
     */
    public ArrayList<countryReport> getAllCountriesByPopulation() {
        ArrayList<countryReport> countries = new ArrayList<>();
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
     * This method will print/display the information in the getAllCountriesByPopulation()
     * format the information to the users preferences
     * @param countries
     */
    public void printCountryReport(ArrayList<countryReport> countries) {
        System.out.printf("%-5s %-47s %-15s %-28s %-11s %-10s%n", "Code", "Name", "Continent", "Region", "Population", "Capital");
        for (countryReport c : countries) {
            System.out.printf("%-5s %-47s %-15s %-28s %-11d %-10s%n",
                    c.code, c.name, c.continent, c.region, c.population, c.capital);
        }
    }
}
