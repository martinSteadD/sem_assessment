# USE CASE: Generate Capital Cities Report

## CHARACTERISTIC INFORMATION

### Goal in Context

As an Economist I want a breakdown of the most populated capital cities across an area so that I can provide publicly available finance data

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
- Population

### Failed End Condition

No report is produced.

### Primary Actor

Economist.

### Trigger

Manager has requested an update to public financial data in capital cities

## MAIN SUCCESS SCENARIO

1. User selects “Capital City Reports”.
2. System prompts for area scope or N value.
3. User provides input.
4. System queries database for capital cities.
5. System orders by population.
6. Report displayed.

## EXTENSIONS

1. Database connection failure.
2. Invalid input (e.g., N not numeric).

## SUB-VARIATIONS

1. User generates report for all capital cities in the world.
2. User generates report for all capital cities in the continent.
3. User generates report for all capital cities in the region.

## SCHEDULE

**DUE DATE**: Release 1.0
