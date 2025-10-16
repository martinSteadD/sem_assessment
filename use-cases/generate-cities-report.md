# USE CASE: Generate Cities Report

## CHARACTERISTIC INFORMATION

### Goal in Context

As a Journalist, I want to identify the most populated cities so that I can plan my next travel destination.

### Scope

Company.

### Level

Primary task.

### Preconditions

Database connection established.

### Success End Condition

A report is available for the user to view with the fields:
- Name
- Country
- District
- Population

### Failed End Condition

No report is produced.

### Primary Actor

Journalist.

### Trigger

Journalist needs to choose a destination for travel

## MAIN SUCCESS SCENARIO

1. User selects “City Reports” option.
2. System prompts for report type and filter.
3. User provides input (e.g., “Europe”, “Top 10 cities in Japan”).
4. System queries the database for city data.
5. System sorts results by population.
6. System displays report.

## EXTENSIONS

1. Database connection failure.
2. Invalid input (e.g., N not numeric).

## SUB-VARIATIONS

1. User generates report for all cities in the world.
2. User generates report for all cities in the continent.
3. User generates report for all cities in the region.
4. User generates report for all cities in the country.
5. User generates report for all cities in the district.

## SCHEDULE

**DUE DATE**: Release 1.0
