# USE CASE: Generate a Country Report

## CHARACTERISTIC INFORMATION

### Goal in Context

As a news writer, I want a way to view the most populated countries across an area so that I can construct stories that will appeal to them.

### Scope

Company.

### Level

Primary task.

### Preconditions

Database connection established.

### Success End Condition

A report is available for the user to view.

### Failed End Condition

No report is produced.

### Primary Actor

News Writer.

### Trigger

Manager request a story from the news writer

## MAIN SUCCESS SCENARIO

1. User selects “Country Reports” option.
2. System prompts for report type (World, Continent, Region, or Top N).
3. User enters filter criteria.
4. System retrieves country data from the database.
5. System orders results by population (descending).
6. System displays the report in a table format.

## EXTENSIONS

1. Database connection failure.
2. Invalid input (e.g., N not numeric).

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0
