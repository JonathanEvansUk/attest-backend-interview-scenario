package com.askattest.interview.service;

import com.askattest.interview.models.Question;
import com.askattest.interview.models.RespondentStatistics;
import com.askattest.interview.models.Response;
import com.askattest.interview.models.Survey;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SurveyStatisticsService {

    public Map<Integer, RespondentStatistics> calculateStatistics(Survey survey, List<Response> responses) {
        Map<Integer, Question> questionsById = survey.questions.stream()
            .collect(Collectors.toMap(q -> q.id, Function.identity()));

        // only include responses to questions in the survey
        Map<Integer, List<Response>> responsesByRespondent = responses.stream()
            .filter(response -> questionsById.containsKey(response.question))
            .collect(Collectors.groupingBy(response -> response.respondent));

        return responsesByRespondent.entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> calculateRespondentStatistics(e.getValue(), questionsById)
            ));
    }

    private RespondentStatistics calculateRespondentStatistics(List<Response> responses, Map<Integer, Question> questionsById) {
        int totalPayout = responses.stream()
            .map(response -> response.question)
            .map(questionsById::get)
            .filter(Objects::nonNull)
            .mapToInt(question -> question.payout)
            .sum();

        return new RespondentStatistics(responses.size(), totalPayout);
    }
}
