package com.napier.sem;

/**
 * The populationSummary class represents a simplified data model
 * for reporting the total population of a geographic area.
 * <p>
 * This class is typically used for high-level summaries where detailed
 * breakdowns (e.g., city vs non-city) are not required.
 */
public class populationSummary {

    /**
     * The name of the geographic area (e.g., country, region, or continent).
     */
    public String name;

    /**
     * The total population of the specified area.
     */
    public int population;
}

