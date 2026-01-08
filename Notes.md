# Notes for take home task

Following the `How we assess your answers` section I focussed on correctness of code, followed by readability, and then
performance.

Some notes on each assessment criteria below.

### Correctness

Task logic is defined
in [SurveyStatisticsService](src/main/java/com/askattest/interview/service/SurveyStatisticsService.java), returning a
record wrapping statistics for each respondent of the survey.

This approach is used to avoid multiple iterations of the responses list. For the sample response this is not an issue,
but for large datasets we would want to avoid multiple iterations.

Unit tests have been provided in [SurveyStatisticsServiceTest](src/test/java/com/askattest/interview/service/SurveyStatisticsServiceTest.java).

For full transparency, the unit tests were generated using Gemini.

### Readability - Refactoring Notes

Given more time I would have made following changes:

- Use records for Models where possible, this brings benefits of immutability, accessor methods, equals and hashcode
  implementations, reduced boilerplate
- Introduce Lombok for cleaner, maintainable code (possibly controversial), simpler logging
- Spring Bootify
- Move file loading logic to separate class


### Performance

Performance is reasonable for small datasets.

Time and Space complexity are both O(Q + R), and the approach scales linearly.

For larger datasets we would want to avoid loading the full list of responses into memory. 
Instead, we should process a Stream of data and avoid grouping each respondent's responses into a list in memory, instead merging data in one iteration.

Ideally, we would defer such aggregations to the database layer.