package com.askattest.interview;

import com.askattest.interview.models.Question;
import com.askattest.interview.models.Response;
import com.askattest.interview.models.Survey;
import com.askattest.interview.repository.ResponseRepo;
import com.askattest.interview.repository.SurveyRepo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        SurveyRepo surveys;
        ResponseRepo responses;
        try {
            surveys = new SurveyRepo();
            responses = new ResponseRepo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Logger.getGlobal().log(Level.INFO, surveys.surveyById(200).toString());
        Logger.getGlobal().log(Level.INFO, responses.responsesByRespondent(300).toString());

        Survey survey = surveys.surveyById(200);

        // first find the questions within the survey
        Map<Integer, Question> questionsById = survey.questions.stream()
            .collect(Collectors.toMap(q -> q.id, Function.identity()));

        // for each response, check if the question is from the survey in question
        Map<Integer, List<Response>> responsesByRespondent = responses.listResponses()
            .stream()
            .filter(response -> questionsById.containsKey(response.question))
            .collect(Collectors.groupingBy(response -> response.respondent));

        // could validate that respondent has not submitted multiple responses for same question
        responsesByRespondent.forEach((respondent, responseList) -> {
                String message = "Respondent %s has answered %s questions".formatted(respondent, responseList.size());
                Logger.getGlobal().log(Level.INFO, message);
            }
        );

        responsesByRespondent.forEach((respondent, responseList) -> {
            int totalPayout = responseList.stream()
                .map(response -> response.question)
                .map(questionsById::get)
                .filter(Objects::nonNull)
                .mapToInt(q -> q.payout)
                .sum();

            String message = "Respondent %s has earned %s pence".formatted(respondent, totalPayout);
            Logger.getGlobal().log(Level.INFO, message);
        });
    }
}