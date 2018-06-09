package com.realtime.statistics.service;

import com.realtime.statistics.dto.StatisticsSummary;
import com.realtime.statistics.dto.Transaction;

public interface StatisticsService {

    StatisticsSummary get();

    void compute(Transaction transaction, Long currentTimestamp);
}
