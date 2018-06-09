package com.realtime.statistics.service.impl;

import com.realtime.statistics.cache.StatisticsCache;
import com.realtime.statistics.config.Constants;
import com.realtime.statistics.dto.Statistics;
import com.realtime.statistics.dto.StatisticsSummary;
import com.realtime.statistics.dto.Transaction;
import com.realtime.statistics.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsCache statisticsCache;

    @Autowired
    public StatisticsServiceImpl(StatisticsCache statisticsCache) {
        this.statisticsCache = statisticsCache;
    }

    @Override
    public StatisticsSummary get() {
        long currentTime = System.currentTimeMillis();
        StatisticsSummary summary = statisticsCache.getData().values()
                .stream()
                .filter(s -> (currentTime - s.getTimestamp()) / 1000 < Constants.LAST_INTERESTED_INTERVAL_IN_SECONDS)
                .map(StatisticsSummary::new)
                .reduce(new StatisticsSummary(), (s1, s2) -> {
                    s1.setSum(s1.getSum() + s2.getSum());
                    s1.setCount(s1.getCount() + s2.getCount());
                    s1.setMax(Double.compare(s1.getMax(), s2.getMax()) > 0 ? s1.getMax() : s2.getMax());
                    s1.setMin(Double.compare(s1.getMin(), s2.getMin()) < 0 ? s1.getMin() : s2.getMin());
                    return s1;
                });
        updateStatisticsSummary(summary);
        log.debug("[SaM's Statistics Service] calculated statistics summary for last minute => {}", summary);
        log.debug("[SaM's Statistics Service] contains in-memory statistics until now: {}, size is {}", statisticsCache.getData(), statisticsCache.getData().size());
        return summary;
    }


    @Override
    public void compute(Transaction transaction, Long currentTimestamp) {

        log.debug("[SaM's Statistics Service] is computing statistics based on new received transaction => {}", transaction);
        int second = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(transaction.getTimestamp()), ZoneId.systemDefault())
                .getSecond();

        statisticsCache.getData().compute(second, (key, existingStatistic) -> {
            if (Objects.isNull(existingStatistic) || isStatisticsUnderInterestedInterval(currentTimestamp, existingStatistic)) {
                return createStatistics(transaction);
            }
            return updateStatistics(transaction, existingStatistic);
        });
    }

    private boolean isStatisticsUnderInterestedInterval(Long currentTimestamp, Statistics existingStatistic) {
        return (currentTimestamp - existingStatistic.getTimestamp()) / 1000 >= Constants.LAST_INTERESTED_INTERVAL_IN_SECONDS;
    }

    private Statistics updateStatistics(Transaction transaction, Statistics existingStatistic) {
        existingStatistic.setCount(existingStatistic.getCount() + 1);
        existingStatistic.setSum(existingStatistic.getSum() + transaction.getAmount());
        if (Double.compare(transaction.getAmount(), existingStatistic.getMax()) > 0) {
            existingStatistic.setMax(transaction.getAmount());
        }
        if (Double.compare(transaction.getAmount(), existingStatistic.getMin()) < 0) {
            existingStatistic.setMin(transaction.getAmount());
        }
        return existingStatistic;
    }

    private Statistics createStatistics(Transaction transaction) {
        Statistics statistics = new Statistics();
        statistics.setTimestamp(transaction.getTimestamp());
        statistics.setSum(transaction.getAmount());
        statistics.setMax(transaction.getAmount());
        statistics.setMin(transaction.getAmount());
        statistics.setCount(1L);
        return statistics;
    }

    private void updateStatisticsSummary(StatisticsSummary summary) {
        summary.setAvg(calculateAverage(summary));
        summary.setMin(Double.compare(summary.getMin(), Double.MAX_VALUE) == 0 ? 0.00 : summary.getMin());
        summary.setMax(Double.compare(summary.getMax(), Double.MIN_VALUE) == 0 ? 0.00 : summary.getMax());
    }

    private Double calculateAverage(StatisticsSummary summary) {
        Double average = summary.getCount() > 0L ? (summary.getSum() / summary.getCount()) : 0.00;
        return Double.valueOf(Constants.DECIMAL_FORMAT.format(average));
    }
}
