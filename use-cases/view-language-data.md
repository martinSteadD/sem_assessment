# USE CASE: View World Demographic by Language

## CHARACTERISTIC INFORMATION

### Goal in Context

As a Demographics Watchdog, I want to view the language makeup of various areas so that I can back up my sources with reliable data

### Scope

Company.

### Level

Secondary task.

### Preconditions

Database connection established.

### Success End Condition

User is able to view the data on languages in the world including;
- Chinese
- English
- Hindi
- Spanish
- Arabic
  (Include percentages of total world population)

### Failed End Condition

Data is inaccessible.

### Primary Actor

Demographics Watchdog.

### Trigger

Manager has requested language data to back up sources and reinforce agency credibility

## MAIN SUCCESS SCENARIO

1. User selects “Language Statistics”.
2. System queries total speakers per language.
3. System calculates percentage of world population.
4. System sorts results descending by speakers.
5. System displays table of results.

## EXTENSIONS

1. Database connection failure.
2. Invalid input (e.g., N not numeric).

## SUB-VARIATIONS

NONE

## SCHEDULE

**DUE DATE**: Release 1.0
