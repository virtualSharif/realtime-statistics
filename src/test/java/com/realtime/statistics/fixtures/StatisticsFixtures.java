package com.realtime.statistics.fixtures;

import com.realtime.statistics.dto.StatisticsSummary;

public class StatisticsFixtures {

    public static final String baseUrl = "http://localhost:8080";

    public static StatisticsSummary getDefaultStatisticSummary() {
        Double defaultAmount = 0.0;
        StatisticsSummary statisticsSummary = new StatisticsSummary();
        statisticsSummary.setSum(defaultAmount);
        statisticsSummary.setMax(defaultAmount);
        statisticsSummary.setMin(defaultAmount);
        statisticsSummary.setCount(0l);
        statisticsSummary.setAvg(defaultAmount);
        return statisticsSummary;
    }

    public static StatisticsSummary getExpectedStatisticSummary(Double transactionAmount, Long count) {
        StatisticsSummary expectedStatisticsSummary = new StatisticsSummary();
        expectedStatisticsSummary.setAvg(transactionAmount);
        expectedStatisticsSummary.setMax(transactionAmount);
        expectedStatisticsSummary.setMin(transactionAmount);
        expectedStatisticsSummary.setCount(count);
        expectedStatisticsSummary.setSum(transactionAmount);
        return expectedStatisticsSummary;
    }
}
