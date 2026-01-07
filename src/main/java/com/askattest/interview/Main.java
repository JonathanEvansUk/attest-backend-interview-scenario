package com.askattest.interview;

import com.askattest.interview.models.RespondentStatistics;
import com.askattest.interview.models.Survey;
import com.askattest.interview.repository.ResponseRepo;
import com.askattest.interview.repository.SurveyRepo;
import com.askattest.interview.service.SurveyStatisticsService;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        SurveyRepo surveys;
        ResponseRepo responses;
        SurveyStatisticsService surveyStatisticsService = new SurveyStatisticsService();
        try {
            surveys = new SurveyRepo();
            responses = new ResponseRepo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Logger.getGlobal().log(Level.INFO, surveys.surveyById(200).toString());
        Logger.getGlobal().log(Level.INFO, responses.responsesByRespondent(300).toString());

        Survey survey = surveys.surveyById(200);
        Map<Integer, RespondentStatistics> respondentStatistics = surveyStatisticsService.calculateStatistics(survey, responses.listResponses());

        respondentStatistics.forEach((respondent, stats) ->
            Logger.getGlobal().log(Level.INFO, "Respondent %d answered %d questions for a total payout of %d pence"
                .formatted(respondent, stats.questionsAnswered(), stats.totalPayout())));
    }
}