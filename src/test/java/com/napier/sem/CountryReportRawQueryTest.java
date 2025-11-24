package com.napier.sem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Raw SQL test to validate country table integrity.
 */
public class CountryReportRawQueryTest {

    @Test
    void testCountryTableHasValidRows() {
        try {
            Statement stmt = populationApp.con.createStatement();
            ResultSet rs = stmt.executeQuery("""
                    SELECT name, population
                    FROM country
                    ORDER BY population DESC
                    LIMIT 1;
            """);

            assertTrue(rs.next(), "Expected at least one row");
            assertNotNull(rs.getString("name"));
            assertTrue(rs.getInt("population") > 0);
        } catch (Exception e) {
            fail("SQL error: " + e.getMessage());
        }
    }
}
