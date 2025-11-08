package com.napier.sem;

/**
 * The populationReport class represents a data model for reporting
 * population statistics of a geographic area, including total population,
 * city population, and non-city population breakdowns.
 * <p>
 * This class is used to structure population-related data for reporting purposes.
 */
public class populationReport {

    /**
     * The name of the geographic area (e.g., country, region, or city group).
     */
    public String name;

    /**
     * The total population of the area.
     */
    public int totalPopulation;

    /**
     * The population living in cities within the area.
     */
    public int cityPopulation;

    /**
     * The percentage of the total population that lives in cities.
     */
    public double cityPercentage;

    /**
     * The population living outside cities (non-urban areas).
     */
    public int nonCityPopulation;

    /**
     * The percentage of the total population that lives outside cities.
     */
    public double nonCityPercentage;
}
