package com.realtime.statistics.controller;

import com.realtime.statistics.StatisticsApplication;
import com.realtime.statistics.dto.StatisticsSummary;
import com.realtime.statistics.dto.Transaction;
import com.realtime.statistics.fixtures.StatisticsFixtures;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StatisticsApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatisticsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1_contextLoads() {
    }

    @Test
    public void test2_getStatistics_shouldReturnDefaultResponse() {

        //when
        ResponseEntity<StatisticsSummary> response = testRestTemplate
                .getForEntity(StatisticsFixtures.baseUrl
                        + "/statistics", StatisticsSummary.class);

        //then
        assertEquals(StatisticsFixtures.getDefaultStatisticSummary(), response.getBody());
    }

    @Test
    public void test3_getStatistics_shouldReturnOkStatus() {

        //when
        ResponseEntity<StatisticsSummary> response = testRestTemplate
                .getForEntity(StatisticsFixtures.baseUrl
                        + "/statistics", StatisticsSummary.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void test4_getStatistics_shouldReturnProperResponseForOneTransaction() {

        //given
        Long currentTimestamp = System.currentTimeMillis();
        Double transactionAmount = 100d;
        this.createTransaction(transactionAmount, currentTimestamp);
        StatisticsSummary expectedStatisticsSummary = StatisticsFixtures
                .getExpectedStatisticSummary(transactionAmount, 1l);

        //when
        ResponseEntity<StatisticsSummary> response = testRestTemplate
                .getForEntity(StatisticsFixtures.baseUrl
                        + "/statistics", StatisticsSummary.class);

        //then
        assertEquals(expectedStatisticsSummary, response.getBody());

    }

    @Test
    public void test5_getStatistics_shouldReturnSameResponseAfterAddingTransactionWithOldTimestamp() {

        //given
        Long oldTimestamp = System.currentTimeMillis() - 61000;
        Double transactionAmount = 100d;
        this.createTransaction(transactionAmount, oldTimestamp);
        StatisticsSummary expectedStatisticsSummary = StatisticsFixtures
                .getExpectedStatisticSummary(transactionAmount, 1l);

        //when
        ResponseEntity<StatisticsSummary> response = testRestTemplate
                .getForEntity(StatisticsFixtures.baseUrl
                        + "/statistics", StatisticsSummary.class);

        //then
        assertEquals(expectedStatisticsSummary, response.getBody());

    }

    private void createTransaction(Double amount, Long timestamp) {

        HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(amount, timestamp));
        ResponseEntity<Object> response = testRestTemplate
                .exchange(StatisticsFixtures.baseUrl
                        + "/transactions", HttpMethod.POST, request, Object.class);
    }

}