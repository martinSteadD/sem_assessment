# USE CASE: Generate Top 10 Population in an Area

## CHARACTERISTIC INFORMATION

### Goal in Context

As the Manager for the News Agency, I want a program to show me the top 10 populated countries in an area of my choosing so that I can identify better locations for our office.

### Scope

Company.

### Level

Secondary task.

### Preconditions

Database connection established.

### Success End Condition

A report is available for the user to view.

### Failed End Condition

No report is produced.

### Primary Actor

News Manager.

### Trigger

Manager is looking for new office location in a populated area

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

1. User generates report for top 10 populated countries in the world.
2. User generates report for top 10 populated countries in the continent.
3. User generates report for top 10 populated countries in the region.
4. User generates report for top 10 populated cities in the world.
5. User generates report for top 10 populated cities in the continent.
6. User generates report for top 10 populated cities in the region.
7. User generates report for top 10 populated cities in the country.
8. User generates report for top 10 populated cities in the district.
9. User generates report for top 10 populated capital cities in the world.
10. User generates report for top 10 populated capital cities in the continent.
11. User generates report for top 10 populated capital cities in the region.

## SCHEDULE

**DUE DATE**: Release 1.0
