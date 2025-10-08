
# Use Cases - Population Reporting System

## Overview
This document describes all use cases for the Population Reporting System project, developed as part of the Software Engineering Methods module.  
The system allows users to generate and view various population reports based on countries, cities, capital cities, population breakdowns, and language statistics.

## Actors
- *User* – The main actor representing a member of the organisation accessing population data.
- *Database* – External system that stores all population information and provides it to the application.
- *System* – The internal system that connects to the database, processes queries, and generates reports.

## Use Case List

| **Use Case ID** | **Use Case Name** | **Primary Actor** | **Description** |
|---|-------------------|-------------------|-----------------|
| 1 | View Country Reports | User | View country population data globally, by continent, or region, ordered by population or top N. |
| 2 | View City Reports | User | View city population data for world, continent, region, country, or district, ordered by population or top N. |
| 3 | View Capital City Reports | User | View capital city data by region, continent, or globally, or top N capitals by population. |
| 4 | View Population by Area | User | View total population of a selected area (world, continent, region, country, district, or city). |
| 5 | View Population Breakdown | User | View total, urban, and rural population for each continent, region, or country. |
| 6 | View Language Statistics | User | View the number of speakers of selected world languages, including the percentage of the world population. |
| 7 | Search and Filter Reports | User | Refine report results by search criteria such as name, region, or N value. |
| 8 | Export Report | User | Export any generated report to CSV, text, or PDF format. |
| 9 | Connect to Database | System | Establish a connection to the SQL database before running any reports. |
| 10 | Generate Report | System | Internal process that runs queries and formats the data for display. |

## Detailed Use Cases

### UC1 - View Country Reports
**Actors:** User, Database  
**Description:** User views population data for countries, optionally filtered by region or continent, or showing the top N countries by population.  
**Preconditions:** Database connection established.  
**Main Flow:**
1. User selects “Country Reports” option.  
2. System prompts for report type (World, Continent, Region, or Top N).  
3. User enters filter criteria.  
4. System retrieves country data from the database.  
5. System orders results by population (descending).  
6. System displays the report in a table format.  
**Postconditions:** Report displayed successfully.  
**Exceptions:**
- Database connection failure.  
- Invalid input (e.g., N not numeric).

### UC2 - View City Reports
**Actors:** User, Database  
**Description:** User views population data for cities across different scopes (world, continent, region, country, or district) or views top N cities.  
**Preconditions:** Database connection established.  
**Main Flow:**
1. User selects “City Reports” option.  
2. System prompts for report type and filter.  
3. User provides input (e.g., “Europe”, “Top 10 cities in Japan”).  
4. System queries the database for city data.  
5. System sorts results by population.  
6. System displays report.  
**Postconditions:** City report displayed.  
**Exceptions:** Same as UC1.

### UC3 - View Capital City Reports
**Actors:** User, Database  
**Description:** User views population data for capital cities globally, by region, or by continent, including top N capitals.  
**Main Flow:**
1. User selects “Capital City Reports”.  
2. System prompts for area scope or N value.  
3. User provides input.  
4. System queries database for capital cities.  
5. System orders by population.  
6. Report displayed.  
**Postconditions:** Capital city report displayed.

### UC4 - View Population by Area
**Actors:** User, Database  
**Description:** User views total population for a selected area (world, continent, region, country, district, or city).  
**Main Flow:**
1. User selects “Population by Area”.  
2. System prompts for area type and name.  
3. System retrieves population data.  
4. System displays total population.  
**Postconditions:** Population information displayed.

### UC5 - View Population Breakdown
**Actors:** User, Database  
**Description:** User views total population, city population, and non-city population (with percentages) for each continent, region, or country.  
**Main Flow:**
1. User selects “Population Breakdown”.  
2. System retrieves total, urban, and rural population data.  
3. System calculates percentages.  
4. System displays breakdown.  
**Postconditions:** Population breakdown displayed.

### UC6 - View Language Statistics
**Actors:** User, Database  
**Description:** User views the number and percentage of speakers for selected world languages (Chinese, English, Hindi, Spanish, Arabic).  
**Main Flow:**
1. User selects “Language Statistics”.  
2. System queries total speakers per language.  
3. System calculates percentage of world population.  
4. System sorts results descending by speakers.  
5. System displays table of results.  
**Postconditions:** Language statistics displayed.

### UC7 - Search and Filter Reports
**Actors:** User  
**Description:** Allows user to refine report results by entering search terms or filters (e.g., by name, region, or N value).  
**Extensions:** This use case extends UC1–UC6.  
**Postconditions:** Filtered data displayed.

### UC8 - Export Report
**Actors:** User  
**Description:** User exports a generated report to a chosen format (e.g., CSV, text, or PDF).  
**Preconditions:** Report already generated.  
**Main Flow:**
1. User clicks “Export” button.  
2. System prompts for format and file name.  
3. System saves file to disk.  
**Postconditions:** File exported successfully.

### UC9 - Connect to Database
**Actors:** System  
**Description:** System establishes a connection to the SQL database before executing any reports.  
**Main Flow:**
1. System reads database configuration.  
2. System attempts connection.  
3. On success, system becomes ready for queries.  
4. On failure, error message displayed.  
**Postconditions:** Database connection established.

### UC10 - Generate Report
**Actors:** System  
**Description:** Internal use case included by UC1–UC6 to run queries and format the output.  
**Main Flow:**
1. System validates input and report type.  
2. System executes appropriate SQL query.  
3. System formats data for display.  
4. Returns formatted data to calling use case.  
**Postconditions:** Data ready for presentation.

## Relationships
- UC1–UC6 **include** UC10 (Generate Report).  
- UC7 **extends** UC1–UC6 (Search and Filter functionality).  
- UC9 (Connect to Database) must be executed before UC10.

*End of Use Case Document*
