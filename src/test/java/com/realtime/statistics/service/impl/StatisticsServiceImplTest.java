package com.realtime.statistics.service.impl;

import com.realtime.statistics.cache.StatisticsCache;
import com.realtime.statistics.dto.StatisticsSummary;
import com.realtime.statistics.dto.Transaction;
import com.realtime.statistics.fixtures.StatisticsFixtures;
import com.realtime.statistics.service.StatisticsService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatisticsServiceImplTest {

    private StatisticsCache statisticsCache;

    private StatisticsService classUnderTest;

    @Before
    public void setUp() {
        statisticsCache = new StatisticsCache();
        classUnderTest = new StatisticsServiceImpl(statisticsCache);
    }

    @Test
    public void get_shouldReturnDefaultSummaryWhenThereIsNoDataInStatisticsCache() {

        //given
        clearStatisticsCache();
        StatisticsSummary expectedStatisticsSummary = StatisticsFixtures.getDefaultStatisticSummary();

        //when
        StatisticsSummary actualStatisticsSummary = classUnderTest.get();

        //then
        assertEquals(actualStatisticsSummary, expectedStatisticsSummary);
    }

    @Test
    public void compute_shouldStoreNewEntryInStatisticCache() {

        //given
        clearStatisticsCache();
        Transaction transaction = new Transaction(100d, System.currentTimeMillis());

        //when
        classUnderTest.compute(transaction, System.currentTimeMillis());

        //then
        assertEquals(1, statisticsCache.getData().size());
    }

    @Test
    public void compute_shouldStoreOnlyOneEntryInStatisticsCacheWhenTwoTransactionHasSameTimestamp() {

        //given
        clearStatisticsCache();
        Long timestamp = System.currentTimeMillis();
        Transaction transaction1 = new Transaction(100d, timestamp);
        Transaction transaction2 = new Transaction(200d, timestamp);

        //when
        classUnderTest.compute(transaction1, timestamp);
        classUnderTest.compute(transaction2, timestamp);

        //then
        assertEquals(1, statisticsCache.getData().size());
    }

    @Test
    public void compute_shouldStoreTwoEntryInStatisticsCacheWhenTwoTransactionAreOfDifferentSeconds() {

        //given
        clearStatisticsCache();
        Long timestamp = System.currentTimeMillis();
        Transaction transaction1 = new Transaction(100d, timestamp);
        Transaction transaction2 = new Transaction(200d, timestamp + 1500);

        //when
        classUnderTest.compute(transaction1, timestamp);
        classUnderTest.compute(transaction2, timestamp);

        //then
        assertEquals(2, statisticsCache.getData().size());
    }

    private void clearStatisticsCache() {
        statisticsCache.getData().clear();
    }
}