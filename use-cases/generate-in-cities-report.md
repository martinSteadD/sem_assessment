# USE CASE: Generate a Report on Population Counts Inside and Outsite Capital Cities

## CHARACTERISTIC INFORMATION

### Goal in Context

As a Migration Expert, I want to view the population of people living in an area within the cities and out with, so that I can better understand holiday trends.

### Scope

Company.

### Level

Primary task.

### Preconditions

Database connection established.

### Success End Condition

A report is available for the user to view with the fields:
- Name
- Total Population
- Population living in cities
- %in cities
- Population Not living in cities
- %out cities

### Failed End Condition

No report is produced.

### Primary Actor

Migration Expert.

### Trigger

Manager has requested a report on upcoming holiday trends.

## MAIN SUCCESS SCENARIO

1. User selects “Population Breakdown”
2. System retrieves total, urban, and rural population data
3. System calculates percentages
4. System displays breakdown


## EXTENSIONS

1. Database connection failure.
2. Invalid input (e.g., N not numeric).

## SUB-VARIATIONS

1. User generates report of urban population in continent.
2. User generates report of urban population in region.
3. User generates report of urban population in country.
4. User generates report of rural population in continent.
5. User generates report of rural population in region.
6. User generates report of rural population in country.

## SCHEDULE

**DUE DATE**: Release 1.0
