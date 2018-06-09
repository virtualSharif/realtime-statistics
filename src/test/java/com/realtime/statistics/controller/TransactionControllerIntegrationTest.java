package com.realtime.statistics.controller;

import com.realtime.statistics.StatisticsApplication;
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
public class TransactionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1_contextLoads() {
    }

    @Test
    public void test2_addTransaction_shouldReturnHttpStatusCreated() {

        //given
        HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(120d, System.currentTimeMillis()));

        //when
        ResponseEntity<Object> response = testRestTemplate
                .exchange(StatisticsFixtures.baseUrl
                        + "/transactions", HttpMethod.POST, request, Object.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test3_addTransaction_shouldReturnHttpStatusNoContent() {

        //given
        HttpEntity<Transaction> request = new HttpEntity<>(new Transaction(120d, System.currentTimeMillis() - 610000));

        //when
        ResponseEntity<Object> response = testRestTemplate
                .exchange(StatisticsFixtures.baseUrl
                        + "/transactions", HttpMethod.POST, request, Object.class);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}